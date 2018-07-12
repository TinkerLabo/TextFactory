package kyPkg.etc;

import kyPkg.sql.Connectors;
import kyPkg.sql.IsamConnector;
import kyPkg.task.Inf_ProgressTask;
import kyPkg.task.TaskSQL2File;

public class CommonMethods {
	// ------------------------------------------------------------------------
	public static Inf_ProgressTask queryIsam2File(String wPath, String sql,
			String iPath) {
		return queryIsam2File(wPath, sql, iPath, false);
	}

	// ------------------------------------------------------------------------
	// FIXME 2015-02-06 問題がありそう・・・
	// ------------------------------------------------------------------------
	public static Inf_ProgressTask queryIsam2File(String wPath, String sql,
			String iPath, boolean literalFlag) {
		System.out.println("@isamQuery2File SQL：" + sql);
		Connectors.add(iPath, new IsamConnector(iPath));
		return new TaskSQL2File(wPath, Connectors.get(iPath), sql, literalFlag);
	}
}