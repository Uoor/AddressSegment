package test;

import java.util.HashSet;

import org.junit.Test;

public class CharDictionaryTest {

	@Test
	public void testSearchChar() {
		HashSet<String> CharSet = new HashSet<String>();
		CharSet.add("南团公路");
		boolean b = CharSet.contains("南团公路");
		System.out.print(b);
		
	}

}
