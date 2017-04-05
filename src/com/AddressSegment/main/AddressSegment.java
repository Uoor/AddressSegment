package com.AddressSegment.main;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;  
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;  

import com.AddressSegment.logic.AddressSplitImpl;
import com.AddressSegment.logic.UndefinedWordRecognize;
import com.AddressSegment.metadata.model.CharDictionary;
import com.AddressSegment.metadata.model.WordDictionary;
import com.AddressSegment.tool.dao.impl.DictionaryFileOperationDAOImpl;
import com.AddressSegment.util.Config;


public class AddressSegment {

	public static class AddressMap extends Mapper<Object, Text, Text, Text> {
		public static FileSystem fs = null;
		public static DictionaryFileOperationDAOImpl DF = null;
		public static WordDictionary wordDict = null;
		public static CharDictionary<String> charDict = null;
		
		protected void setup(Context context) throws IOException {
			wordDict = new WordDictionary();
			charDict = new CharDictionary<String>();
			Configuration conf = new Configuration();
			fs = FileSystem.get(URI.create("hdfs://192.168.31.172:9000"), conf);
			try {
				DF = new DictionaryFileOperationDAOImpl(
						Config.getDefaultDictionaryHDFSURL(), Config.getCharDictionaryHDFSURL(), fs);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//DictionaryFileOperationDAOImpl DF = new DictionaryFileOperationDAOImpl(
			//		Config.getDefaultDictionaryURL(), Config.getCharDictionaryURL());
			DF.putFileToDict(wordDict, charDict);
			System.out.println("********Setup Complete!********");
			
		}

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			String[] strArgs;
			AddressSplitImpl address = new AddressSplitImpl();
			UndefinedWordRecognize uwr = new UndefinedWordRecognize();
			
			//split the parameter to key and value
			
			strArgs = value.toString().split(",");
			
			Text id = new Text(strArgs[0]);
			if (2 > strArgs.length) {
				//System.out.println(strArgs[0] + "," + strArgs[1]);
				System.out.println("No Value!");
				return;
			} else {
				ArrayList<String> wordArray = null;
				try {
					wordArray = address.Split(strArgs[1], wordDict, charDict, fs);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //Do the word segment
				wordArray = uwr.getUndefinedWord(wordArray);             //Do the unidentifiedword recognizing
				
				for (int i = 0; i < wordArray.size(); i++) {
					Text tmp = new Text(wordArray.get(i));
					//tmp.set(new Text(wordArray1.get(i)));
					context.write(tmp, id);
				}
			}
		}
	}
	
	public static class AddressReduce extends Reducer<Text, Text, Text, Text> {
		public void reduce(Text key, Text value, Context context) throws IOException, InterruptedException {
			context.write(key, value);
		}
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		// TODO Auto-generated method stub
		
		Configuration conf = new Configuration();
		
		//String[] otherArgs = {"test/input/part-m-00000", "test/output"};
        Job job = new Job(conf, "AddressSegment");
        
        job.setJarByClass(AddressSegment.class); //设置运行jar中的class名称  
        
        job.setMapperClass(AddressMap.class);//设置mapreduce中的mapper reducer combiner类  
        job.setCombinerClass(AddressReduce.class); 
        job.setReducerClass(AddressReduce.class);  
          
          
        job.setOutputKeyClass(Text.class); //设置输出结果键值对类型
        job.setOutputValueClass(Text.class); 
        
        FileInputFormat.addInputPaths(job, args[0]);//设置mapreduce输入输出文件路径
        FileOutputFormat.setOutputPath(job,new Path(args[1]));
        
        System.exit(job.waitForCompletion(true) ? 0 : 1);

	}

}
