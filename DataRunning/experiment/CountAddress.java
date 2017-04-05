package experiment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hbase.HBaseConfiguration;

import com.AddressSegment.data.dao.impl.AddressQueryImpl;
import com.AddressSegment.logic.AddressSplitImpl;
import com.AddressSegment.logic.UndefinedWordRecognize;
import com.AddressSegment.metadata.model.CharDictionary;
import com.AddressSegment.metadata.model.WordDictionary;
import com.AddressSegment.tool.dao.impl.DictionaryFileOperationDAOImpl;
import com.AddressSegment.util.Config;

public class CountAddress {
	public static Configuration config = null;
	public static FileSystem fs = null;
	public static DictionaryFileOperationDAOImpl DF = null;
	public static WordDictionary wordDict = null;
	public static CharDictionary<String> charDict = null;
	public static int rowkey = 0;

	// public static HTablePool pool = new HTablePool(config, 1000);

	static {
		config = HBaseConfiguration.create();
		wordDict = new WordDictionary();
		charDict = new CharDictionary<String>();
		Configuration conf = new Configuration();
		try {
			fs = FileSystem.get(URI.create("hdfs://192.168.31.172:9000"), conf);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			DF = new DictionaryFileOperationDAOImpl(Config.getDefaultDictionaryHDFSURL(),
					Config.getCharDictionaryHDFSURL(), fs);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		DF.putFileToDict(wordDict, charDict);
	}
	
	public static void ComputeAddressCount(String fileInputPath, String fileOutputPath) throws IOException, URISyntaxException{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileInputPath),"GBK"));
		String line = "";
		FileOutputStream out=new FileOutputStream(fileOutputPath);
		PrintStream p=new PrintStream(out);
		while ((line = br.readLine()) != null) {
			Integer count = 0;
			
			AddressSplitImpl asi = new AddressSplitImpl();
			ArrayList<String> strArray = asi.Split(line, fs);
			UndefinedWordRecognize uwr = new UndefinedWordRecognize();
			ArrayList<String> wordArray1 = uwr.getUndefinedWord(strArray);
			AddressQueryImpl aqi = new AddressQueryImpl();
			count = aqi.queryAddressCount(wordArray1);
			
			System.out.println(line);
			System.out.println(count);
			p.println(line+"\t"+count);
		}
		p.close();
		br.close();
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		ComputeAddressCount("C:/Users/HYFrank/Desktop/Noname1.txt", "C:/Users/HYFrank/Desktop/countAddress.txt");
	}

}
