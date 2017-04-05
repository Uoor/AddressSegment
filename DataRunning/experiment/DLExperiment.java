package experiment;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.AddressSegment.metadata.model.CharDictionary;
import com.AddressSegment.metadata.model.WordDictionary;
import com.AddressSegment.tool.dao.impl.DictionaryFileOperationDAOImpl;
import com.AddressSegment.tool.dao.impl.DoubleLevenshteinAlgorithmImpl;
import com.AddressSegment.tool.dao.impl.TextPair;
import com.AddressSegment.util.Config;



public class DLExperiment {

	
	public static Configuration config = null;
	public static FileSystem fs = null;
	public static DictionaryFileOperationDAOImpl DF = null;
	public static WordDictionary wordDict = null;
	public static CharDictionary<String> charDict = null;
	public static int rowkey = 0;


	public static class DLMap extends Mapper<Object, Text, FloatWritable, TextPair> {

		protected void setup(Context context) throws IOException {
			wordDict = new WordDictionary();
			charDict = new CharDictionary<String>();
			Configuration conf = new Configuration();
			fs = FileSystem.get(URI.create("hdfs://192.168.31.172:9000"), conf);
			try {
				DF = new DictionaryFileOperationDAOImpl(Config.getDefaultDictionaryHDFSURL(),
						Config.getCharDictionaryHDFSURL(), fs);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DF.putFileToDict(wordDict, charDict);
			System.out.println("********Setup Complete!********");

		}

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			FloatWritable DLValue = null;
			TextPair TP = null;

			// split the parameter to key and value
			String[] strArgs = value.toString().split("\t");
			
			if(strArgs.length == 2){
				DoubleLevenshteinAlgorithmImpl d = null;
				TP = new TextPair(strArgs[0], strArgs[1]);
				try {
					d = new DoubleLevenshteinAlgorithmImpl(wordDict, charDict, fs);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				DLValue = new FloatWritable(d.DoubleLevenshtein("漕宝路124号农业大厦2001室总机间", strArgs[1]));
				context.write(DLValue, TP);
			}
		}
	}

	public static class DLReduce extends Reducer<FloatWritable, TextPair, FloatWritable, TextPair> {
		public void reduce(TextPair key, FloatWritable value, Context context) throws IOException, InterruptedException {
			context.write(value, key);
		}
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();

		//String[] otherArgs = { "", "test/DLoutput1/"};
		Job job = new Job(conf, "DLExperiment");
		
		job.setJarByClass(DLExperiment.class); 
		job.setMapperClass(DLMap.class);
		job.setReducerClass(DLReduce.class);
		
		job.setNumReduceTasks(1);
		job.setOutputKeyClass(FloatWritable.class);
		job.setOutputValueClass(TextPair.class);
		
		FileInputFormat.addInputPaths(job, args[0]);
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		boolean b = job.waitForCompletion(true);
		if (!b) {
			throw new IOException("error with job!");
		}
	}

}
