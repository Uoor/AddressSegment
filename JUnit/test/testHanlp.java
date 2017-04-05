package test;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

public class testHanlp {
	public static int Count(String src) throws IOException {
		Segment segment = HanLP.newSegment().enableOrganizationRecognize(true).enablePlaceRecognize(true);
		FileReader fr = null;
		int result = 0;
		fr = new FileReader(src);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		String[] arrs = null;
		while ((line = br.readLine()) != null) {
			List<Term> termList = segment.seg(line);
			result += termList.size();
		}
		br.close();
		fr.close();

		return result;
	}
	

	public static void main(String[] args) throws IOException {
		String[] testCase = new String[]{
		        "我在上海林原科技有限公司兼职工作，",
		        "同时在上海外国语大学日本文化经济学院学习经济与外语。",
		        "我经常在台川喜宴餐厅吃饭，",
		        "偶尔去地中海影城看电影。",
		        "南京西路1515号10层1002室静安嘉里中心办公室一座上海中华商厦集团股份有限公司",
		        "江西省崇明镇柳林村狮子乡十八里屯9号503室",
		        "北艾局1楼机房内(北艾路886号)",
		        "园工路口",
		        "淞兴路163号（走同泰路门至五楼）",
		        "淞沪路605号4F",
		        "上海市西藏中路268号来福士广场办公楼2602-2605"
		};
		//String line = "C:/Users/HYFrank/Desktop/Noname2.txt";
		String line = "/home/mh/Noname2.txt";
		System.out.println(Count(line));
		Segment segment = HanLP.newSegment().enableOrganizationRecognize(true).enablePlaceRecognize(true);
		List<Term> termList = segment.seg(testCase[1]);
		Iterator<Term> it = termList.iterator();
		while(it.hasNext()){
			System.out.println(it.next().toString());
		}
	}

}
