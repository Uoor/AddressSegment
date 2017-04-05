package test;

import java.util.Stack;

import org.junit.Test;

public class StackTest {

	@Test
	public void test() {
		Stack<String> s = new Stack<String>();
		String str = "123";
		s.push(str);
		System.out.println(s.get(0));
		System.out.println(s.pop());
	}

}
