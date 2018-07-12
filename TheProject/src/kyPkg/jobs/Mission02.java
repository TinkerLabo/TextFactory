package kyPkg.jobs;
import kyPkg.sql.*;

// for Database
import java.sql.*;

public class Mission02{
	//-------------------------------------------------------------------------
	public static void mission(){
		System.out.println("...Mission02.mission()...");
		//---------------------------------------------------------------------
		// DB connection 
		//---------------------------------------------------------------------
		String jURL = ServerConnecter.getDEF_JURL() ;
		String user = ServerConnecter.getDEF_USER(); // ユーザ名
		String pswd = ServerConnecter.getDEF_PASS(); // パスワード
		//---------------------------------------------------------------------
		jURL = "jdbc:postgresql://10.6.20.7:5432/test";
		user = "postgres";
		pswd = "";
		//---------------------------------------------------------------------
		Inf_JDBC ins = new JDBC(jURL,user,pswd);
		String sql = "";
		Connection con = null;
		Statement stmt = null;
		//---------------------------------------------------------------------
		try{
			con  = ins.getConnection();   // conがヌルならエラー処理！！
			stmt = con.createStatement(); // ステートメント生成
			if (stmt != null){
				sql  = "SELECT * FROM jazz;";
				ResultSet rs = stmt.executeQuery(sql);
				dump(rs);
			}
			ins.freeConnection(con);
			ins.releaseAll();
		}catch(Exception ee){ ee.printStackTrace(); }
	}
	//---+---------+---------+---------+---------+---------+---------+---------+
	//	ｄｕｍｐ  あらかじめResultsetのカウントを拾う方法はないのか？？調べよう！
	//---+---------+---------+---------+---------+---------+---------+---------+
	public static int dump(ResultSet rs) throws SQLException {
		int	lcnt = 0;
		ResultSetMetaData rsMeta = rs.getMetaData();
		int	colmax = rsMeta.getColumnCount();
		//Object	obj = null;
		System.out.println("colmax:"+colmax);
		//---------+---------+---------+---------+---------+---------+---------+
		//	Header（列名等処理）
		//---------+---------+---------+---------+---------+---------+---------+
		for (int j = 0;j < colmax ;j++){
			System.out.print("#ColumnName("+(j+1)+"):");
			System.out.print( rsMeta.getColumnName(j+1)+"\n");
		}
		//---------+---------+---------+---------+---------+---------+---------+
		//	Body（データ処理）
		//---------+---------+---------+---------+---------+---------+---------+
		//jTa.setText("");
		for (; rs.next(); ) {
			lcnt++;
			System.out.print(" #"+lcnt );
			for (int i = 0; i < colmax; i++) {
				System.out.print( " <"+(i+1)+">");
				System.out.print(rs.getString(i + 1));
				//jTa.append(obj.toString() + "\t");
				//	※最初のカラムはゼロじゃなくて１なので注意する
				//	obj = rs.getObject(i + 1);	
				//	if (obj==null){
				//		System.out.print("null?" + "\t");
				//	}else{
				//		System.out.print(obj.toString() + "\t");
				//	}
			}
			//jTa.append("\n");
			System.out.print("\n");
		}
		return lcnt;
	}

}
