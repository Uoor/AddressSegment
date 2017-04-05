package com.AddressSegment.data.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.AddressSegment.metadata.model.BaseModel;
import com.AddressSegment.util.Config;

public abstract class BaseJdbcTemplate<T extends BaseModel> {
	private static final String DRIVER = Config.getDatabaseDriver();
	private static final String URL = Config.getDatabaseUrl();
	private static final String USERNAME = Config.getDatabaseUsername();
	private static final String PASSWORD = Config.getDatabasePassword();

	private static Connection conn = null;

	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			System.err.println("Cannot Find DataBase Driver");
			System.exit(-1);
		}
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			System.err.println("Cannot Get Database Connection2.");
			System.exit(-1);
		}
	}

	private Connection getConnection() {
		try {
			return DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			System.err.println("Cannot Get Database Connection.");
			System.exit(-1);
			return null;
		}
	}

	private Connection getConnection2() {
		return conn;
	}

	protected int updateTemplate(String updateSql, Object... sqlArgs) throws SQLException {
		Connection connection = getConnection();
		PreparedStatement preStmt = connection.prepareStatement(updateSql);
		int argIndex = 1;
		for (Object arg : sqlArgs) {
			preStmt.setObject(argIndex++, arg);
		}
		int rows = preStmt.executeUpdate();
		close(connection, preStmt, null);
		return rows;
	}

	protected int[] batchUpdateTemplate(String updateSql, List<Object[]> batchArgList) throws SQLException {
		Connection connection = getConnection();
		PreparedStatement preStmt = connection.prepareStatement(updateSql);
		for (Object[] sqlArgs : batchArgList) {
			int argIndex = 1;
			for (Object sqlArg : sqlArgs) {
				preStmt.setObject(argIndex++, sqlArg);
			}
			preStmt.addBatch();
		}
		int[] batchRows = preStmt.executeBatch();
		close(connection, preStmt, null);
		return batchRows;
	}

	protected List<T> queryTemplate(ModelRowMapper<T> mapper, String querySql, Object... sqlArgs) throws SQLException {
		Connection connection = getConnection();
		PreparedStatement preStmt = connection.prepareStatement(querySql, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		int argIndex = 1;
		for (Object arg : sqlArgs) {
			preStmt.setObject(argIndex++, arg);
		}
		ResultSet resultSet = preStmt.executeQuery();
		List<T> resultList = new ArrayList<T>();
		for (int i = 1; resultSet.next(); ++i) {
			resultList.add(mapper.mappingRowToModel(resultSet, i));
		}
		close(connection, preStmt, resultSet);
		return resultList;
	}

	protected List<String> queryTemplate(String querySql, Object... sqlArgs) throws SQLException {
		Connection connection = getConnection2();
		PreparedStatement preStmt = connection.prepareStatement(querySql, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		int argIndex = 1;
		for (Object arg : sqlArgs) {
			preStmt.setObject(argIndex++, arg);
		}
		ResultSet resultSet = preStmt.executeQuery();
		List<String> resultList = new ArrayList<String>();
		while (resultSet.next()) {
			resultList.add(String.valueOf(resultSet.getString("KEY")));
			resultList.add(String.valueOf(resultSet.getString("ADDR_FULL")));
		}
		close(preStmt, resultSet);
		return resultList;
	}

	protected Integer queryForIntTemplate(String querySql, Object... sqlArgs) throws SQLException {
		Connection connection = getConnection();
		PreparedStatement preStmt = connection.prepareStatement(querySql);
		int argIndex = 1;
		for (Object arg : sqlArgs) {
			preStmt.setObject(argIndex++, arg);
		}
		ResultSet resultSet = preStmt.executeQuery();
		Integer resultInt = resultSet.getRow();
		close(connection, preStmt, resultSet);
		return resultInt;
	}

	protected Integer queryForIntTemplate(ModelRowMapper<Integer> mapper, String querySql, Object... sqlArgs)
			throws SQLException {
		Connection connection = getConnection();
		PreparedStatement preStmt = connection.prepareStatement(querySql);
		int argIndex = 1;
		for (Object arg : sqlArgs) {
			preStmt.setObject(argIndex++, arg);
		}
		ResultSet resultSet = preStmt.executeQuery();
		Integer resultInt = null;
		if (resultSet.next()) {
			resultInt = mapper.mappingRowToModel(resultSet, 1);
		}
		close(connection, preStmt, resultSet);
		return resultInt;
	}

	private void close(Connection connection, Statement stmt, ResultSet resultSet) {
		try {
			if (null != resultSet) {
				resultSet.close();
			}
		} catch (SQLException e) {
			System.err.println("Close ResultSet Failed.");
		} finally {
			try {
				if (null != stmt) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.err.println("Close Statment Failed.");
			} finally {
				try {
					if (null != connection) {
						connection.close();
					}
				} catch (SQLException e) {
					System.err.println("Close Connection Failed.");
				}
			}
		}
	}
	
	public void close(Statement stmt, ResultSet resultSet) {
		try {
			if (null != resultSet) {
				resultSet.close();
			}
		} catch (SQLException e) {
			System.err.println("Close ResultSet Failed.");
		} finally {
			try {
				if (null != stmt) {
					stmt.close();
				}
			} catch (SQLException e) {
				System.err.println("Close Statment Failed.");
			}
		}
	}
	
	public void close() {
		try {
			if (null != conn) {
				conn.close();
			}
		} catch (SQLException e) {
			System.err.println("Close Connection Failed.");
		}
	}

}
