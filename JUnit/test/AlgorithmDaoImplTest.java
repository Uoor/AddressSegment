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
import com.AddressSegment.metadata.model.CharDictionary;
import com.AddressSegment.metadata.model.WordDictionary;
import com.AddressSegment.tool.dao.impl.DictionaryFileOperationDAOImpl;
import com.AddressSegment.util.Config;

public class AlgorithmDaoImplTest {
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
	public void testRunRMM() throws URISyntaxException, IOException {
		String strTemp = "南汇区惠南镇南团公路丹桂苑(张江高科技园区)858弄A16(ｌ）号楼5号综合楼B-30座5层半501室";
		AlgorithmDaoImpl a = new AlgorithmDaoImpl(wordDict, charDict, fs);
		long startMili=System.currentTimeMillis();// 当前时间对应的毫秒数
		ArrayList<String> wordArrayList = a.runRMM(strTemp);
		Iterator<String> it1 = wordArrayList.iterator();
		for(;it1.hasNext();){
			System.out.print(it1.next()+"/");
		}
		long endMili=System.currentTimeMillis();
		System.out.println("总耗时为"+(endMili-startMili)+"毫秒");
	}

	@Test
	public void testRunMM() throws URISyntaxException, IOException {
		String strTemp = "南汇区惠南镇南团公路丹桂苑张江高科技园区858弄A16(ｌ）号楼5号综合楼B-30座5层半501室";
		AlgorithmDaoImpl a = new AlgorithmDaoImpl(wordDict, charDict, fs);
		long startMili=System.currentTimeMillis();// 当前时间对应的毫秒数
		ArrayList<String> wordArrayList = a.runMM(strTemp);
		Iterator<String> it1 = wordArrayList.iterator();
		for(;it1.hasNext();){
			System.out.print(it1.next()+"/");
		}
		long endMili=System.currentTimeMillis();
		System.out.println("总耗时为"+(endMili-startMili)+"毫秒");
	}

}
