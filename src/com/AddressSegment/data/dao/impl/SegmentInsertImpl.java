package com.AddressSegment.data.dao.impl;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.AddressSegment.data.dao.declare.SegmentInsert;
import com.AddressSegment.metadata.model.Segment;
import com.AddressSegment.data.dao.BaseJdbcTemplate;

public class SegmentInsertImpl extends BaseJdbcTemplate<Segment> implements SegmentInsert {

	@Override
	public boolean batchInsertMidAddressLexerDAO(List<Segment> SegmentList) {
		final String batchInsertSql = "INSERT INTO MID_ADDRESS_LEXER (ID, STANDARD_FEATURE, DECORATE_FEATURE) VALUES (?, ?, ?)";
		List<Object[]> sqlArgs = new LinkedList<Object[]>();
		Object[] currentArg = null;
		for (int i = 0; i < SegmentList.size(); i++) {// MidAddressLexer
														// midAddressLexer :
														// midAddressLexerList)
														// {
			currentArg = new Object[2];
			currentArg[0] = SegmentList.get(i).getID();
			currentArg[1] = SegmentList.get(i).getWordList();// 没结束
			currentArg[2] = SegmentList.get(i).getWordList();// 没结束
			sqlArgs.add(currentArg);
		}
		boolean batchInsertResult = true;
		try {
			int[] batchInsertRows = batchUpdateTemplate(batchInsertSql, sqlArgs);
			for (int eachRows : batchInsertRows) {
				batchInsertResult = batchInsertResult && (-2 == eachRows || 1 == eachRows);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return batchInsertResult;
	}
}
