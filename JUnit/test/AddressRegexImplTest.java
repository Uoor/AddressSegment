package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.AddressSegment.tool.dao.impl.AddressRegexImpl;

public class AddressRegexImplTest {

	/*@Test
	public void test() {
		AddressRegexImpl ari = new AddressRegexImpl();
		System.out.print(ari.matchAddressSegex("5号楼"));
		
	}*/
	
	@Test
	public void testPatternQ() {
		AddressRegexImpl ari = new AddressRegexImpl();
		ari.matchUnidentifiedWord("111111654245");
		while(ari.findMatchWord()){
			System.out.println(ari.getUnidentifiedWord().length());
			System.out.println(ari.getStart());
			System.out.println(ari.getEnd());
		}
		
		
		Pattern q = Pattern.compile("11+");
		Matcher m = q.matcher("1111111654245");
		while(m.find()){
			System.out.println(m.group(0));
		}
		
	}

}
