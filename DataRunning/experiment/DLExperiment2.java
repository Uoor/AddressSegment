package experiment;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.AddressSegment.data.dao.impl.AddressQueryImpl;
import com.AddressSegment.metadata.model.CharDictionary;
import com.AddressSegment.metadata.model.WordDictionary;
import com.AddressSegment.tool.dao.impl.DictionaryFileOperationDAOImpl;
import com.AddressSegment.tool.dao.impl.DoubleLevenshteinAlgorithmImpl;
import com.AddressSegment.util.Config;

public class DLExperiment2 {

	public static Configuration config = null;
	public static FileSystem fs = null;
	public static DictionaryFileOperationDAOImpl DF = null;
	public static WordDictionary wordDict = null;
	public static CharDictionary<String> charDict = null;
	public static int rowkey = 0;
	//public static AddressQueryImpl aqi = null;
	public static HashMap<String, FloatWritable> map = null;

	static {
		//aqi = new AddressQueryImpl();
		map = new HashMap<String, FloatWritable>();
		//config = HBaseConfiguration.create();
		Configuration conf = new Configuration();
		try {
			fs = FileSystem.get(URI.create("hdfs://192.168.31.172:9000"), conf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

	public static class DLMap extends Mapper<Object, Text, Text, FloatWritable> {

		protected void setup(Context context) throws IOException {
			wordDict = new WordDictionary();
			charDict = new CharDictionary<String>();
			
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

			// split the parameter to key and value
			String[] strArgs = value.toString().split("\t");

			if (strArgs.length == 4) {
				DoubleLevenshteinAlgorithmImpl d = null;
				try {
					d = new DoubleLevenshteinAlgorithmImpl(wordDict, charDict, fs);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				if (strArgs[2] != null) {
					//String tmpP = selectByRowKey(strArgs[2]);
					//if (tmpP != "NoValue") {
						DLValue = new FloatWritable(d.DoubleLevenshtein(strArgs[1], strArgs[3]));
						if (map.containsKey(strArgs[0])) {
							if (DLValue.compareTo(map.get(strArgs[0])) == 1) {
								map.put(strArgs[0], DLValue);
							}
						} else {
							map.put(strArgs[0], DLValue);
						}
					//}
				}
			}
		}
	}

	protected void cleanup(DLMap.Context context)
			throws java.io.IOException, InterruptedException {
		for (int i = 1; i < 2822; i++) {
			if (map.containsKey(String.valueOf(i))) {
				context.write(new Text(String.valueOf(i)), map.get(String.valueOf(i)));
			}
		}
	}

	public static class DLReduce extends Reducer<Text, FloatWritable, Text, FloatWritable> {
		public void reduce(Text key, FloatWritable value, Context context) throws IOException, InterruptedException {
			context.write(key, value);
		}

	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();

		String[] otherArgs = { "E:/caddress/queryAddressIDSecond1", "C:/Users/HYFrank/Desktop/caddress/output5" };
		Job job = new Job(conf, "DLExperiment");

		job.setJarByClass(DLExperiment2.class);
		job.setMapperClass(DLMap.class);
		// job.setCombinerClass(DLReduce.class);
		// job.setReducerClass(DLReduce.class);

		job.setNumReduceTasks(1);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FloatWritable.class);

		FileInputFormat.addInputPaths(job, otherArgs[0]);
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		boolean b = job.waitForCompletion(true);
		if (!b) {
			throw new IOException("error with job!");
		} else {
			//OutputStream out = fs.create(new Path("/user/root/last/outputSecond/out"));
			
			for (int i = 0; i < 100; i++) {
				if (map.containsKey(String.valueOf(i))) {
					String temp = String.valueOf(i) + "\t" + map.get(String.valueOf(i));
					//out.write(temp.getBytes()); //write();
					System.out.println(String.valueOf(i) + "\t" + map.get(String.valueOf(i)));
				}
			}
			//aqi.close();
		}
		 
	}

}
