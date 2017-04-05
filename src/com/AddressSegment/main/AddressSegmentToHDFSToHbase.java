package com.AddressSegment.main;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;

import com.AddressSegment.logic.AddressSplitImpl;
import com.AddressSegment.logic.UndefinedWordRecognize;
import com.AddressSegment.main.AddressSegmentTage.AddressReduce;
import com.AddressSegment.metadata.model.CharDictionary;
import com.AddressSegment.metadata.model.WordDictionary;
import com.AddressSegment.tool.dao.impl.DictionaryFileOperationDAOImpl;
import com.AddressSegment.tool.dao.impl.TextPair;
import com.AddressSegment.util.Config;

@SuppressWarnings({ "unused", "deprecation" })
public class AddressSegmentToHDFSToHbase {

	public static Configuration config = null;
	public static FileSystem fs = null;
	public static DictionaryFileOperationDAOImpl DF = null;
	public static WordDictionary wordDict = null;
	public static CharDictionary<String> charDict = null;
	public static int rowkey = 0;
	public static HTablePool pool =  new HTablePool(config, 1000);

	static {
		config = HBaseConfiguration.create();
		//HTablePool pool =  new HTablePool(config, 1000);
	}

	public static class AddressMap extends Mapper<Object, Text, Text, Text> {

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
			String[] strArgs;
			AddressSplitImpl address = new AddressSplitImpl();
			UndefinedWordRecognize uwr = new UndefinedWordRecognize();

			// split the parameter to key and value

			strArgs = value.toString().split(",");

			Text id = new Text(strArgs[0]);
			if (2 > strArgs.length) {
				System.out.println("No Value!");
				return;
			} else {
				ArrayList<String> wordArray = null;
				try {
					wordArray = address.Split(strArgs[1], wordDict, charDict, fs);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // Do the word segment
				ArrayList<String> wordArray1 = uwr.getUndefinedWord(wordArray); // Do the
																// unidentifiedword
																// recognizing

				for (int i = 0; i < wordArray1.size(); i++) {
					Text tmp = new Text(wordArray1.get(i));
					context.write(id, tmp);
				}
			}
		}
	}

	public static class AddressReduce extends Reducer<Text, Text, Text, Text> {
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			rowkey = 0;
			String strKey = key.toString();
			Text temp = null;
			Text temp1 = null;
			for (Text t : values) {
				temp = new Text(t.toString()+strKey);
				temp1 = new Text(t.toString() + '\t' + strKey);
				rowkey++;
				context.write(temp, temp1);
			}
		}
	}

	/*
	 * public static class AddressReduce extends TableReducer<Text, Text,
	 * ImmutableBytesWritable> { Put put1 = null;
	 * 
	 * public void reduce(Text key, Text values, Context context) throws
	 * IOException, InterruptedException { int row = (int)
	 * (rowkey+System.currentTimeMillis()); put1 = new Put(Bytes.toBytes(row));
	 * rowkey++; put1.add(Bytes.toBytes("address"), Bytes.toBytes("segment"),
	 * values.toString().getBytes()); put1.add(Bytes.toBytes("address"),
	 * Bytes.toBytes("id"), key.toString().getBytes()); context.write(null,
	 * put1); } }
	 */

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();

		//String[] otherArgs = { "test/input/part-m-00000", "test/output1/"};
		Job job = new Job(conf, "AddressSegmentToHbaseB");
		job.setJarByClass(AddressSegmentToHDFSToHbase.class); // class that contains
															// mapper and
		// reducer
		// set other scan attrs
		job.setMapperClass(AddressMap.class);// 设置mapreduce中的mapper reducer
		//job.setCombinerClass(AddressReduce.class); 
		job.setReducerClass(AddressReduce.class);

		job.setNumReduceTasks(10); // at least one, adjust as required
		//job.setMapOutputKeyClass(Text.class);
		//job.setMapOutputValueClass(Text.class);
		// job.setInputFormatClass(TextInputFormat.class);
		// job.setOutputFormatClass(TableOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		FileInputFormat.addInputPaths(job, args[0]);
		FileOutputFormat.setOutputPath(job,new Path(args[1]));
		boolean b = job.waitForCompletion(true);
		if (!b) {
			throw new IOException("error with job!");
		}
	}

}
