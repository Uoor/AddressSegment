package com.AddressSegment.data.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.AddressSegment.data.dao.declare.AddressQuery;
import com.AddressSegment.metadata.model.Segment;
import com.AddressSegment.data.dao.BaseJdbcTemplate;
import com.AddressSegment.data.dao.ModelRowMapper;

public final class AddressQueryImpl extends BaseJdbcTemplate<Segment> implements AddressQuery {

	@Override
	public List<Segment> queryAll() {
		// TODO Auto-generated method stub
		final String querySql = "SELECT C.ID, C.NAME FROM C_ADDRESS C";
		List<Segment> resultList = null;
		try {
			resultList = queryTemplate(new ModelRowMapper<Segment>() {
				@Override
				public Segment mappingRowToModel(ResultSet resultSet, int rowIndex) throws SQLException {
					Segment address = new Segment();
					address.setID(String.valueOf(resultSet.getInt("ID")));
					address.setSentence(resultSet.getString("ADDR_FULL"));
					return address;
				}
			}, querySql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultList;
	}

	@Override
	public List<Segment> querySegment(Integer startRow, Integer endRow) {
		// TODO Auto-generated method stub
		final String querySql = "select * from(select t.*, rownum row_end  from (SELECT C.ID, C.ADDR_FULL FROM C_ADDRESS C) t where rownum <= ?) where row_end > ?";
		List<Segment> resultList = null;
		try {
			resultList = queryTemplate(new ModelRowMapper<Segment>() {
				@Override
				public Segment mappingRowToModel(ResultSet resultSet, int rowIndex) throws SQLException {
					Segment address = new Segment();
					address.setID(String.valueOf(resultSet.getInt("ID")));
					address.setSentence(resultSet.getString("ADDR_FULL"));
					return address;
				}
			}, querySql, startRow, endRow);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultList;
	}

	@Override
	public int queryAllCount() {
		final String countSql = "SELECT COUNT(ID) AS ADDRESS_COUNT FROM C_ADDRESS";
		Integer addressCount = null;
		try {
			addressCount = queryForIntTemplate(new ModelRowMapper<Integer>() {
				@Override
				public Integer mappingRowToModel(ResultSet resultSet, int rowIndex) throws SQLException {
					return resultSet.getInt("ADDRESS_COUNT");
				}
			}, countSql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(addressCount);
		return addressCount.intValue();
	}

	@Override
	public int queryAddressCount(List<?> segmentList) {
		// TODO Auto-generated method stub
		Integer addressCount = null;
		String countSqlstart = "with A as (";
		String countSqlend = ") SELECT COUNT(DISTINCT(KEY)) AS COUNT FROM A";
		String countSqlmain = "select * from data d where d.segment = ";
		String countSqlunion = " union ";
		String countSql = countSqlstart;
		for (int i = 0; i < segmentList.size(); i++) {
			countSql = countSql + countSqlmain + "'" + (String) segmentList.get(i) + "'";
			if (i < segmentList.size() - 1) {
				countSql += countSqlunion;
			}
		}
		countSql += countSqlend;
		try {
			addressCount = queryForIntTemplate(new ModelRowMapper<Integer>() {
				@Override
				public Integer mappingRowToModel(ResultSet resultSet, int rowIndex) throws SQLException {
					return resultSet.getInt("COUNT");
				}
			}, countSql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return addressCount;
	}

	@Override
	public List<String> queryAddress(List<?> segmentList) {
		// TODO Auto-generated method stub
		String countSqlstart = "with A as (";
		String countSqlend = ") SELECT DISTINCT(KEY) AS KEY, c.addr_full FROM A, c_address c where A.key = c.id GROUP BY A.key, c.addr_full ";
		String countSqlmain = "select * from data d where d.segment = ";
		String countSqlunion = " union ";
		String querySql = countSqlstart;
		for (int i = 0; i < segmentList.size(); i++) {
			querySql = querySql + countSqlmain + "'" + (String) segmentList.get(i) + "'";
			if (i < segmentList.size() - 1) {
				querySql += countSqlunion;
			}
		}
		if(segmentList.size() > 2){
			querySql = querySql + countSqlend + "HAVING COUNT(A.key) > " + segmentList.size()/2;
		} else {
			querySql += countSqlend;
		}
		
		System.out.println(querySql);
		List<String> resultList = null;
		try {
			resultList = queryTemplate(querySql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultList;
	}

	@Override
	public String queryAddr_Full(String key) {
		// TODO Auto-generated method stub
		List<String> resultList = null;
		String result = null;
		String querySql = "select c.addr_full as key from c_address c where c.id =  '" + key + "'" ;
		try {
			resultList = queryTemplate(querySql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(resultList.get(0) == null){
			result = "NoValue";
		} else {
			result = resultList.get(0);
		}
		return result;
	}

}
