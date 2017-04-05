package experiment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import com.AddressSegment.logic.AlgorithmDaoImpl;
import com.AddressSegment.logic.UndefinedWordRecognize;
import com.AddressSegment.metadata.model.CharDictionary;
import com.AddressSegment.metadata.model.WordDictionary;
import com.AddressSegment.tool.dao.impl.DictionaryFileOperationDAOImpl;
import com.AddressSegment.util.Config;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;


public class CountTextSegmentWithAlgroithm {
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
	
	public static int Count(String src) throws IOException, URISyntaxException {
		AlgorithmDaoImpl a = new AlgorithmDaoImpl(wordDict, charDict, fs);
		FileOutputStream out=new FileOutputStream("C:/Users/HYFrank/Desktop/re.txt");
		PrintStream p=new PrintStream(out);
		FileReader fr = null;
		int result = 0;
		fr = new FileReader(src);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(src),"GBK"));
		String line = "";
		ArrayList<String> wordArrayList = null;
		while ((line = br.readLine()) != null) {
			wordArrayList = a.runRMM(line);
			UndefinedWordRecognize uwr = new UndefinedWordRecognize();
			ArrayList<String> undefinedWordArrayList = uwr.getUndefinedWord(wordArrayList);
			result += undefinedWordArrayList.size();
			
			Iterator<String> it = undefinedWordArrayList.iterator();
			String temp = "";
			while(it.hasNext()){
				temp += it.next().toString()+"/";
			}
			//System.out.println(temp);
			p.println(temp);
		}
		/*line = br.readLine();
			wordArrayList = a.runRMM(line);
			UndefinedWordRecognize uwr = new UndefinedWordRecognize();
			ArrayList<String> undefinedWordArrayList = uwr.getUndefinedWord(wordArrayList);
			result += wordArrayList.size();
			Iterator<String> it = wordArrayList.iterator();
			while(it.hasNext()){
				System.out.println(it.next());
			}
			Iterator<String> it1 = undefinedWordArrayList.iterator();
			while(it1.hasNext()){
				System.out.println(it1.next());
			}*/
		br.close();
		fr.close();
		p.close();
		
		return result;
	}
	
	public static int CountPrint(String src) throws IOException, URISyntaxException {
		AlgorithmDaoImpl a = new AlgorithmDaoImpl(wordDict, charDict, fs);
		FileOutputStream out=new FileOutputStream("C:/Users/HYFrank/Desktop/re+hanlp.txt");
		PrintStream p=new PrintStream(out);
		FileReader fr = null;
		int result = 0;
		fr = new FileReader(src);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(src),"GBK"));
		String line = "";
		ArrayList<String> wordArrayList = null;
		while ((line = br.readLine()) != null) {
			wordArrayList = a.runRMM(line);
			UndefinedWordRecognize uwr = new UndefinedWordRecognize();
			ArrayList<String> undefinedWordArrayList = uwr.getUndefinedWordWithHanlp(wordArrayList);
			result += undefinedWordArrayList.size();
			
			Iterator<String> it = undefinedWordArrayList.iterator();
			String temp = "";
			while(it.hasNext()){
				temp += it.next().toString()+"/";
			}
			//System.out.println(temp);
			p.println(temp);
		}
		/*line = br.readLine();
			wordArrayList = a.runRMM(line);
			UndefinedWordRecognize uwr = new UndefinedWordRecognize();
			ArrayList<String> undefinedWordArrayList = uwr.getUndefinedWord(wordArrayList);
			result += wordArrayList.size();
			Iterator<String> it = wordArrayList.iterator();
			while(it.hasNext()){
				System.out.println(it.next());
			}
			Iterator<String> it1 = undefinedWordArrayList.iterator();
			while(it1.hasNext()){
				System.out.println(it1.next());
			}*/
		br.close();
		fr.close();
		p.close();
		
		return result;
	}
	
	public static int ResultCompare(String src1, String src2) throws IOException{
		int result = 0;
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(src1),"GBK"));
		BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(src2),"utf-8"));
		FileOutputStream out=new FileOutputStream("C:/Users/HYFrank/Desktop/wrong.txt");
		PrintStream p=new PrintStream(out);
		String line1 = "";
		String line2 = "";
		String[] wordArrayList1 = null;
		String[] wordArrayList2 = null;
		HashSet<String> hSet =  new HashSet<String>();
		while ((line1 = br.readLine()) != null && (line2 = br1.readLine()) != null) {
			wordArrayList1 = line1.split("/");
			wordArrayList2 = line2.split("/");
			String temp = "";
			for (int i = 0; i < wordArrayList1.length; i++){
				//if(hSet.contains(wordArrayList1[i]) == false){
					hSet.add(wordArrayList1[i]);
				//}
			}
			for (int i = 0; i < wordArrayList2.length; i++){
				if(hSet.contains(wordArrayList2[i]) == true){
					result++;
				} else {
					temp += wordArrayList2[i] + "/";
				}
			}
			if(temp != "")
				p.println(temp);
			hSet.clear();
			//System.out.println(temp);
		}
		br.close();
		br1.close();
		p.close();
		return result;
	}
	
	public static int CountWithOutUndefinedWord(String src) throws IOException, URISyntaxException {
		AlgorithmDaoImpl a = new AlgorithmDaoImpl(wordDict, charDict, fs);
		FileOutputStream out=new FileOutputStream("C:/Users/HYFrank/Desktop/re-un.txt");
		PrintStream p=new PrintStream(out);
		FileReader fr = null;
		int result = 0;
		fr = new FileReader(src);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(src),"GBK"));
		String line = "";
		ArrayList<String> wordArrayList = null;
		while ((line = br.readLine()) != null) {
			wordArrayList = a.runRMM(line);
			result += wordArrayList.size();
			
			Iterator<String> it = wordArrayList.iterator();
			String temp = "";
			while(it.hasNext()){
				temp += it.next().toString()+"/";
			}
			//System.out.println(temp);
			p.println(temp);
		}
		/*line = br.readLine();
			wordArrayList = a.runRMM(line);
			UndefinedWordRecognize uwr = new UndefinedWordRecognize();
			ArrayList<String> undefinedWordArrayList = uwr.getUndefinedWord(wordArrayList);
			result += wordArrayList.size();
			Iterator<String> it = wordArrayList.iterator();
			while(it.hasNext()){
				System.out.println(it.next());
			}
			Iterator<String> it1 = undefinedWordArrayList.iterator();
			while(it1.hasNext()){
				System.out.println(it1.next());
			}*/
		br.close();
		fr.close();
		p.close();
		
		return result;
	}
	
	public static int CountHanlp(String src) throws IOException {
		FileOutputStream out=new FileOutputStream("C:/Users/HYFrank/Desktop/hanlp.txt");
		PrintStream p=new PrintStream(out);
		Segment segment = HanLP.newSegment().enableOrganizationRecognize(true).enablePlaceRecognize(true);
		FileReader fr = null;
		int result = 0;
		fr = new FileReader(src);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(src),"GBK"));
		//BufferedReader br = new BufferedReader(fr);
		String line = "";
		while ((line = br.readLine()) != null) {
			List<Term> termList = segment.seg(line);
			Iterator<Term> it = termList.iterator();
			String temp = "";
			while(it.hasNext()){
				temp += it.next().word+"/";
			}
			p.println(temp);
			result += termList.size();
		}
		br.close();
		fr.close();
		p.close();
		return result;
	}
	

	public static void main(String[] args) throws URISyntaxException, IOException {
		// TODO Auto-generated method stub
		
		//String line = "C:/Users/HYFrank/Desktop/Noname2.txt";
		//System.out.println(CountPrint(line));
		//System.out.println(Count(line));
		System.out.println(ResultCompare("C:/Users/HYFrank/Desktop/standard.txt", "C:/Users/HYFrank/Desktop/re.txt"));
		//System.out.println(CountWithOutUndefinedWord(line));
		//System.out.println(CountHanlp(line));
	}

}
