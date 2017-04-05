package test;

import java.util.List;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

/**
 * Created by mh on 17-4-5.
 */
public class MyTest {
	public static void main(String[] args){
		String[] testCase = new String[]{
				"厦门市同安区西柯镇官浔村同集中路2002号2号厂房一楼",
				"厦门火炬高新区创业园宏业楼104室",
		};
		Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
		for (String sentence : testCase)
		{
			List<Term> termList = segment.seg(sentence);
			System.out.println(termList);
		}
	}
}
