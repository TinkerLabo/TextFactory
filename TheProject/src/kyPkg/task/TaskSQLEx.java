package kyPkg.task;

import java.util.List;
import kyPkg.sql.*;
//-----------------------------------------------------------------------------
//Stat���c�����@�͂Ȃ����H 
//��kyPkg.util.SwingWorker.java �Ɉˑ����Ă��܂�
//��kyPkg.panel.qpr.calc.Calc_Common.java �Ɉˑ����Ă��܂�
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
        // super.setLengthOfTask(sql.length); // �ő�X�e�b�v��
    }

    // ------------------------------------------------------------------------
    // �O������R�[�������g���K�[
    // ------------------------------------------------------------------------
    @Override
    public void execute() {
        super.start("TaskSQLEx", 2048);
        if (isStarted()) {
            final SwingWrk worker = new SwingWrk() {
                @Override
                public Object construct() {
                    return new ActualTask(); // ���ۂ̏���
                }
            };
            worker.start();
        }
        super.stop();// ����I��
    }

    // ------------------------------------------------------------------------
    // �s���ۂ̏����t
    // ------------------------------------------------------------------------
    class ActualTask {

        ActualTask() {
            // setLengthOfTask(sql.length);
            Inf_JDBC wConnection = connector.getConnection();
            // ---------------------------------------------------------------------
            // if ( canceled == true ){} // �����f�����I�I
            // ---------------------------------------------------------------------
            if (wConnection != null) {
                boolean rtn = false;
                for (int i = 0; i < sql.length; i++) {
                    setCurrent(i + 1);
                    rtn = wConnection.executeUpdate(sql[i]);
                }
                stop();// ����I��
                wConnection.close(); // �c�a���N���[�Y
            }
            stop();
        }
    }
}
