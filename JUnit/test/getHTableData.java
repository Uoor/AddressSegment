package test;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

@SuppressWarnings({ "deprecation", "unused" })
public class getHTableData {
	// private static HBaseConfiguration hbaseConfig = null;
	public static Configuration config = null;

	static {
		config = HBaseConfiguration.create();

	}

	@SuppressWarnings("resource")
	public static void createTable(String tableName) {
		// Configuration config = HBaseConfiguration.create();
		System.out.println("start create table ......");
		try {
			HBaseAdmin hBaseAdmin = new HBaseAdmin(config);
			if (hBaseAdmin.tableExists(tableName)) {// 濡傛灉瀛樺湪瑕佸垱寤虹殑琛紝閭ｄ箞鍏堝垹闄わ紝鍐嶅垱寤�
				hBaseAdmin.disableTable(tableName);
				hBaseAdmin.deleteTable(tableName);
				System.out.println(tableName + " is exist,detele....");
			}
			HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
			tableDescriptor.addFamily(new HColumnDescriptor("address"));
			hBaseAdmin.createTable(tableDescriptor);
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("end create table ......");
	}

	/**
	 * get鏂瑰紡锛岄�氳繃rowKey鏌ヨ
	 * 
	 * @param tablename
	 * @param rowKey
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String selectByRowKey(String tablename, String rowKey) throws IOException {

		Get get = new Get(rowKey.getBytes());
		HTable htable = new HTable(config, Bytes.toBytes(tablename));
		Result r = htable.get(get);
		String strResult = "";
		for (Cell re : r.rawCells()) {
			//System.out.println(String.format("row:%s, family:%s, qualifier:%s, qualifiervalue:%s",
			//		new String(CellUtil.cloneRow(re)), new String(CellUtil.cloneFamily(re)),
			//		new String(CellUtil.cloneQualifier(re)), new String(CellUtil.cloneValue(re))));
			strResult = new String(CellUtil.cloneValue(re));
		}
		return strResult;
	}

	@SuppressWarnings("resource")
	public static void selectBySingleColumnValueFilter(String segmentValue) throws IOException {
		Scan scan = new Scan();
		SingleColumnValueFilter filter1 = new SingleColumnValueFilter("address".getBytes(), "KEY".getBytes(),
				CompareOp.EQUAL, segmentValue.getBytes());
		scan.setFilter(filter1);
		ResultScanner rs = new HTable(config, "data").getScanner(scan);
		for (Result r : rs) {
			/*
			 * for (KeyValue kv : r.raw()) { System.out.println(String.format(
			 * "row:%s, family:%s, qualifier:%s, qualifiervalue:%s, timestamp:%s."
			 * , Bytes.toString(kv.getRow()), Bytes.toString(kv.getFamily()),
			 * Bytes.toString(kv.getQualifier()), Bytes.toString(kv.getValue()),
			 * kv.getTimestamp())); }
			 */

			for (Cell re : r.rawCells()) {
				System.out.println(String.format("row:%s, family:%s, qualifier:%s, qualifiervalue:%s",
						new String(CellUtil.cloneRow(re)), new String(CellUtil.cloneFamily(re)),
						new String(CellUtil.cloneQualifier(re)), new String(CellUtil.cloneValue(re))));
			}
		}
		rs.close();
	}

	@SuppressWarnings("resource")
	public static void selectByRowFilter(String row) throws IOException {
		Scan scan = new Scan();

		Filter filter1 = new RowFilter(CompareOp.EQUAL, new BinaryPrefixComparator(Bytes.toBytes(row)));

		scan.setFilter(filter1);
		ResultScanner rs = new HTable(config, "data").getScanner(scan);
		for (Result r : rs) {
			for (Cell re : r.rawCells()) {
				System.out.println(new String(CellUtil.cloneRow(re)));
				System.out.println(new String(CellUtil.cloneFamily(re)));
				System.out.println(new String(CellUtil.cloneQualifier(re)));
				System.out.println(new String(CellUtil.cloneValue(re)));
			}
		}
	}

	@SuppressWarnings("resource")
	public static void insertData(String tableName) {
		System.out.println("start insert data ......");
		Put put = new Put("112233bbbccccC".getBytes());
		put.add("address".getBytes(), "segment".getBytes(), "aaa".getBytes());
		put.add("address".getBytes(), "id".getBytes(), "bbb".getBytes());
		put.add("address".getBytes(), "segment".getBytes(), "ccc".getBytes());
		try {
			new HTable(config, "data").put(put);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("end insert data ......");
	}

	// @SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		//selectBySingleColumnValueFilter("101672130");
		// createTable("data");
		getHTableData.selectByRowKey("data1", "1");
		// insertData("data1");
		// int a = 1548989798;
		// System.out.println(Bytes.toBytes(a));
		// System.out.println(String.valueOf(a) +System.currentTimeMillis());
		// selectByRowFilter("246号54881814");

		/*
		 * HTable table = new HTable(config, "data"); Scan scan = new Scan();
		 * ResultScanner rs = table.getScanner(scan); for (Result r : rs) { for
		 * (Cell re : r.rawCells()) { System.out.println(String.format(
		 * "row:%s, family:%s, qualifier:%s, qualifiervalue:%s", new
		 * String(CellUtil.cloneRow(re)), new String(CellUtil.cloneFamily(re)),
		 * new String(CellUtil.cloneQualifier(re)), new
		 * String(CellUtil.cloneValue(re))));
		 * 
		 * } }
		 */

		/*
		 * // 鎸塺okey 鍜� column 鏉ユ煡璇紝鏌ヨTom琛宑ourse鍒楁棌鐨勬墍鏈夊垪鍊�
		 * getHTableData.selectByRowKeyColumn("scores", "Tom", "course");
		 * 
		 * // Filter澶氭潯浠舵煡璇紝鏉′欢锛氭煡璇� course鍒楁棌涓璦rt鍒楀�间负97 锛屼笖
		 * course鍒楁棌涓璵ath鍒楀�间负100鐨勮 List<String> arr = new ArrayList<String>();
		 * arr.add("course,art,97"); arr.add("course,math,100");
		 * getHTableData.selectByFilter("scores", arr);
		 */

	}

}
