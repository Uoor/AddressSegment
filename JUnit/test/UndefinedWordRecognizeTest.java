package test;



import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.junit.Test;

import com.AddressSegment.logic.AlgorithmDaoImpl;
import com.AddressSegment.logic.UndefinedWordRecognize;
import com.AddressSegment.metadata.model.CharDictionary;
import com.AddressSegment.metadata.model.WordDictionary;
import com.AddressSegment.tool.dao.impl.DictionaryFileOperationDAOImpl;
import com.AddressSegment.util.Config;

public class UndefinedWordRecognizeTest {
	public static FileSystem fs = null;
	public static DictionaryFileOperationDAOImpl DF = null;
	public static WordDictionary wordDict = null;
	public static CharDictionary<String> charDict = null;
	
	static{
		wordDict = new WordDictionary();
		charDict = new CharDictionary<String>();
		Configuration conf = new Configuration();
		try {
			fs = FileSystem.get(URI.create("hdfs://192.168.31.172:9000"), conf);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			DF = new DictionaryFileOperationDAOImpl(
					Config.getDefaultDictionaryHDFSURL(), Config.getCharDictionaryHDFSURL(), fs);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//DictionaryFileOperationDAOImpl DF = new DictionaryFileOperationDAOImpl(
		//		Config.getDefaultDictionaryURL(), Config.getCharDictionaryURL());
		DF.putFileToDict(wordDict, charDict);
		System.out.println("********Setup Complete!********");
	}

	@Test
	public void test() throws URISyntaxException, IOException {
		String strTemp = "南汇区,惠南镇南团公路丹桂苑张江高科技园区858弄A16(ｌ）号楼5号综合楼B-30座5层半501室";
		UndefinedWordRecognize uwr = new UndefinedWordRecognize();
		AlgorithmDaoImpl a = new AlgorithmDaoImpl(wordDict, charDict, fs);
		ArrayList<String> wordArrayList = a.runRMM(strTemp);
		Iterator<String> it1 = wordArrayList.iterator();
		while(it1.hasNext()){
			System.out.println(it1.next());
		}
		ArrayList<String> undefinedWordArrayList = uwr.getUndefinedWord(wordArrayList);
		
		Iterator<String> it = undefinedWordArrayList.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
		
	}

}
