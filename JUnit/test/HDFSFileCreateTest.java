package test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HDFSFileCreateTest {
	public static Configuration config = null;
	public static FileSystem fs = null;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		fs = FileSystem.get(URI.create("hdfs://192.168.31.172:9000"), conf);
		OutputStream out = fs.create(new Path("/user/root/last/outputSecond/out"));
		out.write("**".getBytes());
		out.close();
		fs.close();

	}

}
