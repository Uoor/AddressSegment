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
import java.util.List;

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

public class QueryAddressID2File {
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
		for(int fileno = 1; fileno < 2; fileno++){
			int k = 0;
			FileOutputStream out=new FileOutputStream(fileOutputPath+fileno);
			PrintStream p=new PrintStream(out);
			
			while ((line = br.readLine()) != null) {
				List<String> listResult = null;
				
				AddressSplitImpl asi = new AddressSplitImpl();
				ArrayList<String> strArray = asi.Split(line, fs);
				UndefinedWordRecognize uwr = new UndefinedWordRecognize();
				ArrayList<String> wordArray1 = uwr.getUndefinedWord(strArray);
				AddressQueryImpl aqi = new AddressQueryImpl();
				listResult = aqi.queryAddress(wordArray1);
				
				System.out.println(rowkey+"\t"+String.valueOf(rowkey*100/(100))+"%");
				for(int i = 0; i < listResult.size(); i+=2){
					p.println(rowkey+"\t"+line+"\t"+listResult.get(i)+"\t"+listResult.get(i+1));
				}
				rowkey++;
				k++;
				if(k == 100)
					break;
			}
			p.close();
		}
		br.close();
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		ComputeAddressCount("C:/Users/HYFrank/Desktop/Noname.txt", "E:/caddress/queryAddressIDSecond");
	}

}
