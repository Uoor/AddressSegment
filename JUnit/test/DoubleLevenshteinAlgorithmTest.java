package test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.junit.Test;

import com.AddressSegment.metadata.model.CharDictionary;
import com.AddressSegment.metadata.model.WordDictionary;
import com.AddressSegment.tool.dao.impl.DictionaryFileOperationDAOImpl;
import com.AddressSegment.tool.dao.impl.DoubleLevenshteinAlgorithmImpl;
import com.AddressSegment.util.Config;

public class DoubleLevenshteinAlgorithmTest {
	public static FileSystem fs = null;
	public static DictionaryFileOperationDAOImpl DF = null;
	public static WordDictionary wordDict = null;
	public static CharDictionary<String> charDict = null;

	static {
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
			DF = new DictionaryFileOperationDAOImpl(Config.getDefaultDictionaryHDFSURL(),
					Config.getCharDictionaryHDFSURL(), fs);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// DictionaryFileOperationDAOImpl DF = new
		// DictionaryFileOperationDAOImpl(
		// Config.getDefaultDictionaryURL(), Config.getCharDictionaryURL());
		DF.putFileToDict(wordDict, charDict);
		System.out.println("********Setup Complete!********");
	}

	@Test
	public void test() throws URISyntaxException, IOException {
		String[] strAddress = {"谈家桥路80弄17号", "谈家桥路80弄11号", "80弄谈家桥路15号", "谈家桥路80弄14号", "谈家桥路80弄5号",
				"谈家桥路80弄7号", "谈家桥路80弄9号", "谈家桥路80弄172号", "谈家桥路80弄1号", "谈家桥路80弄13号", "谈家桥路80弄47号"};
		DoubleLevenshteinAlgorithmImpl d = new DoubleLevenshteinAlgorithmImpl(wordDict, charDict, fs);
		for(String it : strAddress){
			System.out.println(it + " 地址距离 = " +d.DoubleLevenshtein("谈家桥路80弄17号", it)*d.Levenshtein("谈家桥路80弄17号", it));
		}
		System.out.println();
		for(String it : strAddress){
			System.out.println(it + " 地址距离 = " +d.DoubleLevenshtein("谈家桥路80弄17号", it));
		}
		System.out.println();
		for(String it : strAddress){
			System.out.println(it + " 地址距离 = " +d.Levenshtein("谈家桥路80弄17号", it));
		}
		
		/*System.out.println("漕宝路381号漕宝路381号漕宝路381号上海徐汇交通协管服务社沈学峰收	与	漕宝路381号"  + " 地址距离 = " + d.DoubleLevenshtein("漕宝路381号", "漕宝路381号漕宝路381号漕宝路381号上海徐汇交通协管服务社沈学峰收")*d.Levenshtein("漕宝路381号", "漕宝路381号漕宝路381号漕宝路381号上海徐汇交通协管服务社沈学峰收"));
		System.out.println("漕宝路381号漕宝路381号漕宝路381号上海徐汇交通协管服务社沈学峰收	与	漕宝路381号"  + " 地址距离 = " + d.Levenshtein("漕宝路381号", "漕宝路381号漕宝路381号漕宝路381号上海徐汇交通协管服务社沈学峰收"));
		System.out.println("漕宝路381号上海徐汇交通协管服务社沈学峰收	与	漕宝路381号" + " 地址距离 = " + d.DoubleLevenshtein("漕宝路381号", "漕宝路381号上海徐汇交通协管服务社沈学峰收")*d.Levenshtein("漕宝路381号", "漕宝路381号上海徐汇交通协管服务社沈学峰收"));
		System.out.println("漕宝路381号上海徐汇交通协管服务社沈学峰收	与	漕宝路381号"  + " 地址距离 = " + d.Levenshtein("漕宝路381号", "漕宝路381号上海徐汇交通协管服务社沈学峰收"));
		System.out.println("漕宝路381号上海徐汇交通协管服务社	与	漕宝路381号交通协管服务社" + " 地址距离 = " + d.DoubleLevenshtein("漕宝路381号交通协管服务社", "漕宝路381号上海徐汇交通协管服务社")*d.Levenshtein("漕宝路381号交通协管服务社", "漕宝路381号上海徐汇交通协管服务社"));
		System.out.println("漕宝路381号上海徐汇交通协管服务社	与	漕宝路381号交通协管服务社" + " 地址距离 = " + d.Levenshtein("漕宝路381号交通协管服务社", "漕宝路381号上海徐汇交通协管服务社"));
		System.out.println("漕宝路381号上海徐汇区交通协管服务社	与	漕宝路381号交通协管服务社" + " 地址距离 = " + d.DoubleLevenshtein("漕宝路381号交通协管服务社", "漕宝路381号上海徐汇区交通协管服务社")*d.Levenshtein("漕宝路381号交通协管服务社", "漕宝路381号上海徐汇区交通协管服务社"));
		System.out.println("漕宝路381号上海徐汇区交通协管服务社	与	漕宝路381号交通协管服务社" + " 地址距离 = " + d.Levenshtein("漕宝路381号交通协管服务社", "漕宝路381号上海徐汇区交通协管服务社"));
		*/
		//System.out.println("谈家桥路80弄11号 " + "地址距离 = " + d.Levenshtein(S, T1));
		/*System.out.println("谈家桥路15号80弄 " + "地址距离 = " + d.Levenshtein(S, T2));
		System.out.println("谈家桥路80弄14号 " + "地址距离 = " + d.Levenshtein(S, T3));
		System.out.println("谈家桥路80弄5号 " + "地址距离 = " + d.Levenshtein(S, T4));
		System.out.println("谈家桥路80弄7号 " + "地址距离 = " + d.Levenshtein(S, T5));
		System.out.println("谈家桥路80弄9号 " + "地址距离 = " + d.Levenshtein(S, T6));
		System.out.println("谈家桥路80弄172号 " + "地址距离 = " + d.Levenshtein(S, T7));
		System.out.println("谈家桥路80弄1号 " + "地址距离 = " + d.Levenshtein(S, T8));
		System.out.println("谈家桥路80弄13号 " + "地址距离 = " + d.Levenshtein(S, T9));
		System.out.println("谈家桥路80弄47号 " + "地址距离 = " + d.Levenshtein(S, T10));*/
		
	}

}
