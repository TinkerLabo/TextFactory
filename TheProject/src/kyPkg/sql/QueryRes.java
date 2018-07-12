package kyPkg.sql;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QueryRes {
	
//	private ResultSetMetaData meta;

	private int colMax = 0;
	private List<String> literaLlist;
	private List<String> colNames;
	private String headerCash = null;
	public String getColName(int index) {
		return colNames.get(index);
	}
	public List<String> getColNames() {
		return colNames;
	}

	public int getColMax() {
		return colMax;
	}

	// ---------------------------------------------------------------------
	// コンストラクタ
	// ---------------------------------------------------------------------
	public QueryRes(ResultSetMetaData meta, String literal) {
		boolean force = false;
		if (literal!=null && literal.startsWith("!")){
			force = true;//この場合はすべての項目をリテラルで囲む！！
			literal=literal.substring(1);
		}
			
//		this.meta = meta;
		try {
			this.colMax = meta.getColumnCount(); // 結果セットの列数
			this.literaLlist = new ArrayList();
			this.colNames = new ArrayList();
			for (int column = 1; column <= this.colMax; column++) {
//				System.out.println(">>>Name:" + meta.getColumnName(column)
//						+ " Type:" + meta.getColumnTypeName(column));
				colNames.add(meta.getColumnName(column));

				if (force == true){
					this.literaLlist.add(literal);
				}else{
					if (meta.getColumnTypeName(column).toUpperCase().equals("VARCHAR")) {
						this.literaLlist.add(literal);
					} else if (meta.getColumnTypeName(column).toUpperCase().equals("CHAR")) {
						this.literaLlist.add(literal);
					} else {
						this.literaLlist.add(null);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			String sMsg = "";
			do {
				if (sMsg.length() > 0) {
					sMsg += " ";
				}
				sMsg += e.getMessage();
			} while ((e = e.getNextException()) != null);
			System.out.println("@QueryRes#error:" + sMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getHeader(String prefix, String suffix, String delimiter) {
		if (headerCash == null) {
			StringBuffer buff = new StringBuffer();
			buff.delete(0, buff.length());
			buff.append(prefix);
			for (Iterator iter = colNames.iterator(); iter.hasNext();) {
				String element = (String) iter.next();
				buff.append(delimiter);
				buff.append(element);
				
			}
//			try {
//				buff.append(meta.getColumnName(1));
//				for (int j = 1; j < this.colMax; j++) {
//					buff.append(delimiter);
//					buff.append(meta.getColumnName(j + 1));
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
			buff.append(suffix);
			this.headerCash = buff.toString();
		}
		return headerCash;
	}

	// public void setHeader(String header) {
	// this.header = header;
	// }

	public String get(int index) {
		return literaLlist.get(index);
	}
}
