package com.AddressSegment.data.dao.declare;

import java.util.List;

import com.AddressSegment.metadata.model.Segment;

public interface AddressQuery {
	List<Segment> queryAll();
	List<Segment> querySegment(Integer startRow, Integer endRow);
	int queryAllCount();
	int queryAddressCount(List<?> segmentList);
	List<String> queryAddress(List<?> segmentList);
	String queryAddr_Full(String key);
}
