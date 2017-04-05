package com.AddressSegment.data.dao.declare;

import java.util.List;

import com.AddressSegment.metadata.model.Segment;



public interface SegmentInsert {
	boolean batchInsertMidAddressLexerDAO(List<Segment> SegmentList);

}
