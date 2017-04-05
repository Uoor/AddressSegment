package com.AddressSegment.data.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ModelRowMapper<T>
{
	T mappingRowToModel(ResultSet resultSet, int rowIndex) throws SQLException;
}
