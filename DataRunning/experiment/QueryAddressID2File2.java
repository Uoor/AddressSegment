package experiment;

import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class QueryAddressID2File2 {

	public static Configuration config = null;
	//public static HTable htable = null;
	
	static{
		config = HBaseConfiguration.create();
	}


	public static String selectByRowKey(String rowKey) throws IOException {
		HConnection HConn = HConnectionManager.createConnection(config);
		HTableInterface HTableItf = HConn.getTable("data1".getBytes());
		Get get = new Get(rowKey.getBytes());
		Result r = HTableItf.get(get);
		String strResult = "";
		for (Cell re : r.rawCells()) {
			strResult = new String(CellUtil.cloneValue(re));
		}
		HTableItf.close();
		HConn.close();
		return strResult;
	}

	public static class DLMap extends Mapper<Object, Text, Text, Text> {

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			// split the parameter to key and value
			String[] strArgs = value.toString().split("\t");

			if (strArgs.length == 3) {
				String tmpP = selectByRowKey(strArgs[2]);
				context.write(value, new Text(tmpP));
			}
		}
	}

	public static class DLReduce extends Reducer<Text, Text, Text, Text> {
		public void reduce(Text key, Text value, Context context) throws IOException, InterruptedException {
			context.write(key, value);
		}
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, URISyntaxException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();

		//String[] otherArgs = { "E:/caddress/queryAddressID101", "C:/Users/HYFrank/Desktop/caddress/output1" };
		Job job = new Job(conf, "QueryAddressID2File2");

		job.setJarByClass(QueryAddressID2File2.class);
		job.setMapperClass(DLMap.class);
		//job.setCombinerClass(DLReduce.class);
		job.setReducerClass(DLReduce.class);

		//job.setNumReduceTasks(1);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		FileInputFormat.addInputPaths(job, args[0]);
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		boolean b = job.waitForCompletion(true);
		if (!b) {
			throw new IOException("error with job!");
		}
		// queryAddr_Full("E:/caddress/queryAddressID",
		// "H:/caddress/queryAddressID");
	}

}
