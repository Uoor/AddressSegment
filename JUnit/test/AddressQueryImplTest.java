package test;

import java.util.List;

import org.junit.Test;

import com.AddressSegment.data.dao.impl.AddressQueryImpl;
import com.AddressSegment.metadata.model.Segment;

public class AddressQueryImplTest {

	@Test
	public void test() {
		AddressQueryImpl aqi = new AddressQueryImpl();
		long startMili = System.currentTimeMillis();// ��ǰʱ���Ӧ�ĺ�����
		List<Segment> segment = aqi.querySegment(new Integer(10),
				new Integer(1));
		long endMili = System.currentTimeMillis();
		System.out.println(segment.get(0).getSentence());
		System.out.println("耗时" + (endMili - startMili) + "毫秒");
	}

	@Test
	public void testQueryCount() {
		AddressQueryImpl aqi = new AddressQueryImpl();
		long startMili = System.currentTimeMillis();// ��ǰʱ���Ӧ�ĺ�����
		@SuppressWarnings("unused")
		Integer count = aqi.queryAllCount();
		long endMili = System.currentTimeMillis();
		System.out.println("耗时" + (endMili - startMili) + "毫秒");

	}

}
