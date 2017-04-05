package com.AddressSegment.main;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
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
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;

import com.AddressSegment.logic.AddressSplitImpl;
import com.AddressSegment.logic.UndefinedWordRecognize;
import com.AddressSegment.metadata.model.CharDictionary;
import com.AddressSegment.metadata.model.WordDictionary;
import com.AddressSegment.tool.dao.impl.DictionaryFileOperationDAOImpl;
import com.AddressSegment.util.Config;

@SuppressWarnings("unused")
public class AddressSegmentToHbase {

	public static Configuration config = null;
	public static FileSystem fs = null;
	public static DictionaryFileOperationDAOImpl DF = null;
	public static WordDictionary wordDict = null;
	public static CharDictionary<String> charDict = null;
    public static int rowkey = 0;
	//public static HTablePool pool =  new HTablePool(config, 1000);

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
			// DictionaryFileOperationDAOImpl DF = new
			// DictionaryFileOperationDAOImpl(
			// Config.getDefaultDictionaryURL(), Config.getCharDictionaryURL());
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
				// System.out.println(strArgs[0] + "," + strArgs[1]);
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
				wordArray = uwr.getUndefinedWord(wordArray); // Do the
																// unidentifiedword
																// recognizing
				for (int i = 0; i < wordArray.size(); i++) {
					// int row = (int) (rowkey+System.currentTimeMillis());
					Text tmp = new Text(wordArray.get(i));
					context.write(id, tmp);
				}
			}
		}
	}

	public static class AddressReduce extends TableReducer<Text, Text, ImmutableBytesWritable> {
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			//Random ram=new Random();Math.abs(ram.nextInt()%100000);
			rowkey = 0;
			String temp = null;
			for (Text t : values) {
				//Put put = new Put(Bytes.toBytes(key.toString() + String.valueOf(rowkey)));
				temp = t.toString() + key.toString();
				Put put = new Put(Bytes.toBytes(temp));
				put.add(Bytes.toBytes("address"), Bytes.toBytes("segment"), t.toString().getBytes());
				put.add(Bytes.toBytes("address"), Bytes.toBytes("id"), key.toString().getBytes());
				//pool.getTable("data").put(put);
				rowkey++;
				context.write(null, put);
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

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();

		//String[] otherArgs = { "test/input/inputfile" };
		Job job = new Job(conf, "AddressSegmentToHbase");
		job.setJarByClass(AddressSegmentToHbase.class); // class that contains
															// mapper and
		// reducer
		// set other scan attrs
		job.setMapperClass(AddressMap.class);// 设置mapreduce中的mapper reducer

		TableMapReduceUtil.initTableReducerJob(// .initTableReducerJob(
				"data", // output table
				AddressReduce.class, // reducer class
				job);

		job.setNumReduceTasks(1); // at least one, adjust as required
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		// job.setInputFormatClass(TextInputFormat.class);
		// job.setOutputFormatClass(TableOutputFormat.class);
		// job.setOutputKeyClass(ImmutableBytesWritable.class);
		// job.setOutputValueClass(Put.class);

		//job.setOutputFormatClass(TableOutputFormat.class);
		//job.setOutputFormatClass(NullOutputFormat.class);

		FileInputFormat.addInputPaths(job, args[0]);
		boolean b = job.waitForCompletion(true);
		if (!b) {
			throw new IOException("error with job!");
		}

	}

}
