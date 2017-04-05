package com.AddressSegment.main;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.AddressSegment.tool.dao.impl.AddressRegexImpl;

public class WordCount {

	public static class WordCountMap extends Mapper<Object, Text, Text, IntWritable> {

		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();
		private AddressRegexImpl ARI = new AddressRegexImpl();

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			// split the parameter to key and value
			StringTokenizer itr = new StringTokenizer(value.toString());
			while (itr.hasMoreTokens()) {
				word.set(itr.nextToken());
				if (false == ARI.matchCustomizePattern(word.toString(), "[0-9]*")){
					context.write(word, one);
				}
			}
		}
	}

	public static class WourdCountReduce extends Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);
		}
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();

		String[] otherArgs = {"test/input/wordCountTeseInput", "test/output"};
		Job job = new Job(conf, "wordCount");

		job.setJarByClass(WordCount.class); // 设置运行jar中的class名称

		job.setMapperClass(WordCountMap.class);// 设置mapreduce中的mapper reducer
												// combiner类
		job.setCombinerClass(WourdCountReduce.class);
		job.setReducerClass(WourdCountReduce.class);

		job.setOutputKeyClass(Text.class); // 设置输出结果键值对类型
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPaths(job, otherArgs[0]);// 设置mapreduce输入输出文件路径
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}

}
