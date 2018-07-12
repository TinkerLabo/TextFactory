package kyPkg.task;

import java.util.List;
import kyPkg.sql.*;
//-----------------------------------------------------------------------------
//Statを残す方法はないか？ 
//※kyPkg.util.SwingWorker.java に依存しています
//※kyPkg.panel.qpr.calc.Calc_Common.java に依存しています
//-----------------------------------------------------------------------------

public class TaskSQLEx extends Abs_ProgressTask {

    private Connector connector = null;
    private String[] sql;

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------
    public TaskSQLEx(String sql) {
        this(new String[]{sql});
    }

    public TaskSQLEx(List<String> list) {
        this((String[]) list.toArray(new String[list.size()]), null);
    }

    public TaskSQLEx(List<String> list, Connector connector) {
        this((String[]) list.toArray(new String[list.size()]), connector);
    }

    public TaskSQLEx(String[] sql) {
        this(sql, null);
    }

    public TaskSQLEx(String sql, Connector connector) {
        this(new String[]{sql}, connector);
    }

    public TaskSQLEx(String[] sql, Connector connector) {
        super();
        this.sql = sql;
        if (connector == null) {
            this.connector = new ServerConnecter();
        } else {
            this.connector = connector;
        }
        // super.setLengthOfTask(sql.length); // 最大ステップ数
    }

    // ------------------------------------------------------------------------
    // 外部からコールされるトリガー
    // ------------------------------------------------------------------------
    @Override
    public void execute() {
        super.start("TaskSQLEx", 2048);
        if (isStarted()) {
            final SwingWrk worker = new SwingWrk() {
                @Override
                public Object construct() {
                    return new ActualTask(); // 実際の処理
                }
            };
            worker.start();
        }
        super.stop();// 正常終了
    }

    // ------------------------------------------------------------------------
    // 《実際の処理》
    // ------------------------------------------------------------------------
    class ActualTask {

        ActualTask() {
            // setLengthOfTask(sql.length);
            Inf_JDBC wConnection = connector.getConnection();
            // ---------------------------------------------------------------------
            // if ( canceled == true ){} // ※中断処理！！
            // ---------------------------------------------------------------------
            if (wConnection != null) {
                boolean rtn = false;
                for (int i = 0; i < sql.length; i++) {
                    setCurrent(i + 1);
                    rtn = wConnection.executeUpdate(sql[i]);
                }
                stop();// 正常終了
                wConnection.close(); // ＤＢをクローズ
            }
            stop();
        }
    }
}
