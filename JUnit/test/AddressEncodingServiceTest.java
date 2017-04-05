package test;



import java.util.Iterator;

import org.junit.Test;

import com.AddressSegment.logic.AddressEncodingService;
import com.AddressSegment.metadata.model.CoordinateCode;

public class AddressEncodingServiceTest {

	@Test
	public void test() {
		AddressEncodingService aes = new AddressEncodingService();
		CoordinateCode<String, String> co = new CoordinateCode<String, String>();
		co = aes.addressEncoding("上海市闸北区谈家桥路80弄17号104室");
		Iterator<String> it = co.getCoordinate().keySet().iterator();
		while(it.hasNext()){
			System.out.println(it.next()+","+co.getCoordinate().get(it.next()));
		}
	}


}
