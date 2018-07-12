package kyPkg.sql;

import static kyPkg.sql.ServerConnecter.getDEF_JURL;
import static kyPkg.sql.ServerConnecter.getDEF_PASS;
import static kyPkg.sql.ServerConnecter.getDEF_USER;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class JDBCTest {
	public static final String LF = System.getProperty("line.separator");

	public static void testMonitorCount() {
		List<String> list = new ArrayList();
		// list.add("20110101,20110131");
		// list.add("20110201,20110228");//2
		// list.add("20110301,20110331");
		// list.add("20110401,20110430");//4
		// list.add("20110501,20110531");
		// list.add("20110601,20110630");//6
		// list.add("20110701,20110731");
		// list.add("20110801,20110831");
		// list.add("20110901,20110930");//9
		// list.add("20111001,20111031");
		// list.add("20111101,20111130");//士
		// list.add("20111201,20111231");

		list.add("20120101,20120131");
		list.add("20120201,20120229");// 2
		list.add("20120301,20120331");
		list.add("20120401,20120430");// 4
		list.add("20120402,20120430");// 4
		list.add("20120501,20120531");
		list.add("20120601,20120630");// 6
		list.add("20120701,20120731");
		list.add("20120801,20120831");
		list.add("20120901,20120930");// 9
		list.add("20121001,20121031");
		list.add("20121101,20121130");// 士
		list.add("20121201,20121231");
		String table = "NQMONS";

		table = "STFACE";
		table = "NQFACE";
		System.out.println("###<" + table + ">##########################");
		for (String term : list) {
			String[] terms = term.split(",");
			// int result = getMonitorCount(table,terms[0], terms[1]);
			int result = getMonitorCountEx(table, terms[0], terms[1]);
			System.out.println(terms[0] + "\t-\t" + terms[1] + ":\t" + result);
		}
	}

	private static int getMonitorCountEx(String table, String bef, String aft) {
		int result = -1;
		String jUrl = ServerConnecter.getDEF_JURL();
		String user = ServerConnecter.getDEF_USER();
		String pswd = ServerConnecter.getDEF_PASS();
		// ---------------------------------------------------------------------
		Inf_JDBC jdbcIns = new kyPkg.sql.JDBC(jUrl, user, pswd);
		String sql = "SELECT count(*) FROM " + table
				+ " WHERE SUBSTRING(XB1, 1, 8) <= '" + bef
				+ "' and SUBSTRING(XB1, 9, 8) >= '" + aft + "'";
		List<List> matrix = jdbcIns.query2Matrix(sql);
		if (matrix.size() == 0) {
			System.out.println("Not Found:" + bef + "-" + aft);
		} else {
			for (Iterator iterator = matrix.iterator(); iterator.hasNext();) {
				List cells = (List) iterator.next();
				if (cells != null) {
					if (cells.size() >= 1) {
						result = (Integer) cells.get(0);
					}
				}
			}
		}
		jdbcIns.close();
		return result;
	}

	@Test
	public void testJDBC() {
		fail("まだ実装されていません");
	}

	@Test
	public void testJDBCStringStringString() {
		fail("まだ実装されていません");
	}

	@Test
	public void testGetConnectionStringStringString() {
		fail("まだ実装されていません");
	}

	@Test
	public void testGetConnection() {
		fail("まだ実装されていません");
	}

	@Test
	public void testFreeConnection() {
		fail("まだ実装されていません");
	}

	@Test
	public void testCloseConnection() {
		fail("まだ実装されていません");
	}

	@Test
	public void testClose() {
		fail("まだ実装されていません");
	}

	@Test
	public void testReleaseAll() {
		fail("まだ実装されていません");
	}

	@Test
	public void testCreateStatement() {
		fail("まだ実装されていません");
	}

	@Test
	public void testExecuteBatch() {
		fail("まだ実装されていません");
	}

	@Test
	public void testExecute() {
		fail("まだ実装されていません");
	}

	@Test
	public void testExecuteUpdate() {
		fail("まだ実装されていません");
	}

	@Test
	public void testExecuteQuery() {
		fail("まだ実装されていません");
	}

	@Test
	public void testExecuteQuery2Writer() {
		fail("まだ実装されていません");
	}

	@Test
	public void testEnumMatrix() {
		fail("まだ実装されていません");
	}

	@Test
	public void testGetSchemeList() {
		fail("まだ実装されていません");
	}

	@Test
	public void testGetCatalogList() {
		fail("まだ実装されていません");
	}

	@Test
	public void testGetFieldsListString() {
		fail("まだ実装されていません");
	}

	@Test
	public void testGetFieldsListStringInt() {
		fail("まだ実装されていません");
	}

	@Test
	public void testGetTablesListStringArray() {
		fail("まだ実装されていません");
	}

	@Test
	public void testGetTablesListStringArrayIntArrayString() {
		fail("まだ実装されていません");
	}

	@Test
	public void testGetCatalog() {
		fail("まだ実装されていません");
	}

	@Test
	public void testQuery2ListString() {
		fail("まだ実装されていません");
	}

	@Test
	public void testQuery2ListStringBoolean() {
		fail("まだ実装されていません");
	}

	@Test
	public void testIsExistString() {
		fail("まだ実装されていません");
	}

	@Test
	public void testIsExistStringBoolean() {
		fail("まだ実装されていません");
	}

	@Test
	public void testObject() {
		fail("まだ実装されていません");
	}

	@Test
	public void testGetClass() {
		fail("まだ実装されていません");
	}

	@Test
	public void testHashCode() {
		fail("まだ実装されていません");
	}

	@Test
	public void testEquals() {
		fail("まだ実装されていません");
	}

	@Test
	public void testClone() {
		fail("まだ実装されていません");
	}

	@Test
	public void testToString() {
		fail("まだ実装されていません");
	}

	@Test
	public void testNotify() {
		fail("まだ実装されていません");
	}

	@Test
	public void testNotifyAll() {
		fail("まだ実装されていません");
	}

	@Test
	public void testWaitLong() {
		fail("まだ実装されていません");
	}

	@Test
	public void testWaitLongInt() {
		fail("まだ実装されていません");
	}

	@Test
	public void testWait() {
		fail("まだ実装されていません");
	}

	@Test
	public void testFinalize() {
		fail("まだ実装されていません");
	}

	public static int testGetMonitorCount(String table, String bef,
			String aft) {
		int result = -1;
		String jUrl = ServerConnecter.getDEF_JURL();
		String user = ServerConnecter.getDEF_USER();
		String pswd = ServerConnecter.getDEF_PASS();
		// ---------------------------------------------------------------------
		Inf_JDBC jdbcIns = new kyPkg.sql.JDBC(jUrl, user, pswd);
		String sql = "SELECT  count(*) as mcount FROM " + table + " "
				+ "where ((XK2 <= " + bef + ") and (XK3 >= " + aft
				+ " ) and (XK2 <> 0) );";
		List<List> matrix = jdbcIns.query2Matrix(sql);
		if (matrix.size() == 0) {
			System.out.println("Not Found:" + bef + "-" + aft);
		} else {
			for (Iterator iterator = matrix.iterator(); iterator.hasNext();) {
				List cells = (List) iterator.next();
				if (cells != null) {
					if (cells.size() >= 1) {
						result = (Integer) cells.get(0);
					}
				}
			}
		}
		jdbcIns.close();
		return result;
	}

	public static void testJanSrc() {
		String jUrl = ServerConnecter.getDEF_JURL();
		String user = ServerConnecter.getDEF_USER();
		String pswd = ServerConnecter.getDEF_PASS();
		// ---------------------------------------------------------------------
		Inf_JDBC jdbcIns = new kyPkg.sql.JDBC(jUrl, user, pswd);
		String jan = "4902555110202";
		String sql = "SELECT JanName,Price FROM V_JanInfo where JanCode = '"
				+ jan + "';";
		List<List> matrix = jdbcIns.query2Matrix(sql);
		if (matrix.size() == 0) {
			System.out.println("Not Found:" + jan);
		} else {
			for (Iterator iterator = matrix.iterator(); iterator.hasNext();) {
				List cells = (List) iterator.next();
				if (cells != null) {
					if (cells.size() >= 2) {
						String name = (String) cells.get(0);
						int price = (Integer) cells.get(1);
						System.out.println("name=>" + name + "<=");
						System.out.println("price=>" + price);
						if (name.equals("不二家　ミルキー缶復刻　１０粒　　　　　　　　　　    ")) {
							System.out.println("ok");
						} else {
							System.out.println("NG?");
						}
					}
				}
			}
		}
		jdbcIns.close();
	}

	public static void testTranSrc() {
		String jUrl = getDEF_JURL();
		String user = getDEF_USER();
		String pswd = getDEF_PASS();
		Inf_JDBC jdbcIns = new kyPkg.sql.JDBC(jUrl, user, pswd);
		// ---------------------------------------------------------------------
		String val = "73410876-20140201-4982603001027-00120-00001";
		String array[] = val.split("-");
		if (array.length >= 4) {
			String sql = "SELECT count(*) FROM  NQTR14 where xa1 = '" + array[0]
					+ "' and xba = '" + array[1] + "' and xF1 ='" + array[2]
					+ "' and xg1 = " + array[3] + " and xh1 = " + array[4] + "";
			String ans = jdbcIns.queryOne(sql);
			if (ans.equals("1")) {
				System.out.println("一件のみ ans=>" + Integer.parseInt(ans));
			} else if (ans.equals("0")) {
				System.out.println("見つからず=>" + Integer.parseInt(ans));
			} else {
				System.out.println("複数件　ans=>" + Integer.parseInt(ans));
			}

		}
		// ---------------------------------------------------------------------
		jdbcIns.close();
	}

	public static void main(String[] args) {
		// testJanSrc();
		// testMonitorCount();
		testTranSrc();
	}
}
