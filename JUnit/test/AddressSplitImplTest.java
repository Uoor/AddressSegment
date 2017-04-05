package test;




import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.junit.Test;

import com.AddressSegment.logic.AddressSplitImpl;
import com.AddressSegment.logic.UndefinedWordRecognize;
import com.AddressSegment.metadata.model.CharDictionary;
import com.AddressSegment.metadata.model.WordDictionary;
import com.AddressSegment.tool.dao.impl.DictionaryFileOperationDAOImpl;
import com.AddressSegment.util.Config;

public class AddressSplitImplTest {
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

	@Test
	public void test() throws URISyntaxException, IOException {
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
		
		AddressSplitImpl asi = new AddressSplitImpl();
		ArrayList<String> strArray =  asi.Split("漕宝路381号漕宝路381号漕宝路381号上海徐汇交通协管服务社沈学峰收", fs);//("nimsjdaisa南汇区惠南镇南团公路丹桂苑(张江高科技园区)858弄A16(ｌ）号楼5号综合楼B-30座5层半501室你妈了个", fs);
		/*for(int i = 0; i < strArray.size(); i++){
			System.out.println(strArray.get(i));
		}
		System.out.println(strArray.size());*/
		UndefinedWordRecognize uwr = new UndefinedWordRecognize();
		ArrayList<String> wordArray1 = uwr.getUndefinedWord(strArray);
		
		//String strTemp =  "35464367,南汇区惠南镇南团公路丹桂苑67号门302室";
		//String[] word = strTemp.split(",");
		for(int i = 0; i < wordArray1.size(); i++){
			System.out.println(wordArray1.get(i));
		}
	}

}
