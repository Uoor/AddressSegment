package experiment;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.AddressSegment.data.dao.impl.AddressQueryImpl;
import com.AddressSegment.logic.AddressSplitImpl;
import com.AddressSegment.logic.UndefinedWordRecognize;
import com.AddressSegment.metadata.model.CharDictionary;
import com.AddressSegment.metadata.model.WordDictionary;
import com.AddressSegment.tool.dao.impl.DictionaryFileOperationDAOImpl;
import com.AddressSegment.util.Config;

public class CountAddressHadoop {

	public static Configuration config = null;
	public static FileSystem fs = null;
	public static DictionaryFileOperationDAOImpl DF = null;
	public static WordDictionary wordDict = null;
	public static CharDictionary<String> charDict = null;
	public static int rowkey = 0;


	public static class AddressMap extends Mapper<Object, Text, Text, IntWritable> {

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
			IntWritable count;
			AddressSplitImpl address = new AddressSplitImpl();
			UndefinedWordRecognize uwr = new UndefinedWordRecognize();

			// split the parameter to key and value

			ArrayList<String> wordArray = null;
			try {
				wordArray = address.Split(value.toString(), wordDict, charDict, fs);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Do the word segment
				// Do the unidentifiedword recognizing
			ArrayList<String> wordArray1 = uwr.getUndefinedWord(wordArray);
			AddressQueryImpl aqi = new AddressQueryImpl();
			count = new IntWritable(aqi.queryAddressCount(wordArray1));

			context.write(value, count);

		}
	}

	public static class AddressReduce extends Reducer<Text, IntWritable, Text, IntWritable> {
		public void reduce(Text key, IntWritable value, Context context) throws IOException, InterruptedException {
			context.write(key, value);
		}
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();

		String[] otherArgs = { "C:/Users/HYFrank/Desktop/Noname2.txt", "test/output2/"};
		Job job = new Job(conf, "AddressSegmentToHbaseB");
		
		job.setJarByClass(CountAddressHadoop.class); 
		job.setMapperClass(AddressMap.class);
		job.setReducerClass(AddressReduce.class);
		
		job.setNumReduceTasks(1);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		FileInputFormat.addInputPaths(job, otherArgs[0]);
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		boolean b = job.waitForCompletion(true);
		if (!b) {
			throw new IOException("error with job!");
		}
	}

}
