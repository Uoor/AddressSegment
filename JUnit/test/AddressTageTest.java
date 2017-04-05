package test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.AddressSegment.logic.service.AddressTageMaking;

public class AddressTageTest {

	@Test
	public void test() {
		AddressTageMaking Tage =  new AddressTageMaking();
		assertEquals(Tage.TageMaking("(58楼"), "NUM");
	}

}
