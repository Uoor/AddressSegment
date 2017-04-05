package experiment;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class HBaseJaccard {
	
	public static class MyMapper extends TableMapper<Text, Text>  {

		private final IntWritable ONE = new IntWritable(1);
	   	private Text text = new Text();
	   	private Text text1 = new Text();
	    	
	   	public void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException {
	   			String rows = row.toString();
	        	String val = new String(value.getValue(Bytes.toBytes("old"), Bytes.toBytes("ASSEMBLENAME")));
	          	text.set(val);     // we can only emit Writables...
	          	text1.set(rows);
	        	context.write(text, text1);
	   	}
	}
	
	public static class MyReducer extends Reducer<Text, Text, Text, Text>  {
        
		/*public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			int i = 0;
			for (IntWritable val : values) {
				i += val.get();
			}	
			context.write(key, new IntWritable(i));
		}*/
		public void reduce(Text key, Text values, Context context) throws IOException, InterruptedException {
			context.write(key, values);
		}
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		// TODO Auto-generated method stub
		Configuration config = HBaseConfiguration.create();
		Job job = new Job(config,"ExampleSummaryToFile");
		job.setJarByClass(HBaseJaccard.class);     // class that contains mapper and reducer
			        
		Scan scan = new Scan();
		scan.setCaching(500);        // 1 is the default in Scan, which will be bad for MapReduce jobs
		scan.setCacheBlocks(false);  // don't set to true for MR jobs
		// set other scan attrs
			        
		TableMapReduceUtil.initTableMapperJob(
			"c_address",        // input table
			scan,               // Scan instance to control CF and attribute selection
			MyMapper.class,     // mapper class
			Text.class,         // mapper output key
			Text.class, 		// mapper output value
			job);
		job.setReducerClass(MyReducer.class);    // reducer class
		job.setNumReduceTasks(1);    // at least one, adjust as required
		FileOutputFormat.setOutputPath(job, new Path("test/output/outputfile"));  // adjust directories as required
			    
		boolean b = job.waitForCompletion(true);
		if (!b) {
			throw new IOException("error with job!");
		}    

	}

}
