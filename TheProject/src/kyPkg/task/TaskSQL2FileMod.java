package kyPkg.task;

import java.io.File;

import kyPkg.converter.CelConverter;
import kyPkg.filter.EzReader;
import kyPkg.filter.EzWriter;
import kyPkg.sql.*;

//-----------------------------------------------------------------------------
// ���s���̏�ԁiStatus�j���c�����@�͂Ȃ����H 
// ��kyPkg.util.SwingWorker.java �Ɉˑ����Ă��܂�
// ��kyPkg.panel.qpr.calc.Calc_Common.java �Ɉˑ����Ă��܂�
//-----------------------------------------------------------------------------
public class TaskSQL2FileMod extends Abs_ProgressTask {

	private boolean trimOption = false;
	private boolean keyEnc = true;
	private CelConverter celConverter = null; // 2008/10/31 Halloween
	private boolean preHead = false;
	private String literal = null;
	private String delimiter = "\t";
	private Connector connector = null;
	private String hash;
	private String sqlPath;
	private String outPath;
	private File outFile;
	//  private long sqlCount;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	public TaskSQL2FileMod(String path, Connector cnn, String sqlPath,
			String hash) {
		this(path, cnn, sqlPath, hash, false, false);
	}

	public TaskSQL2FileMod(String path, Connector cnn, String sqlPath,
			String hash, boolean literalFlag, boolean preHead) {
		super();
		//#createTester--------------------------------------------------
		//		System.out.println("public static void testTaskSQL2FileMod() {");
		//		System.out.println("    String path = \"" + path + "\";");
		//		System.out.println("    Connector cnn = " + cnn + ";");
		//		System.out.println("    String sqlPath = \"" + sqlPath + "\";");
		//		System.out.println("    String hash = \"" + hash + "\";");
		//		System.out.println("    boolean literalFlag = " + literalFlag + ";");
		//		System.out.println("    boolean preHead = " + preHead + ";");
		//		System.out.println(
		//				"    TaskSQL2FileMod ins = new TaskSQL2FileMod(path,cnn,sqlPath,hash,literalFlag,preHead);");
		//		System.out.println("}");
		//--------------------------------------------------

		if (hash.equals("")) {
			hash = kyPkg.uDateTime.DateCalc.getTimeStamp(4, 0, "_");
		}
		//	this.sqlCount = sqlCount;
		this.hash = hash;
		this.sqlPath = sqlPath;
		this.outPath = path;
		this.outFile = new File(path + ".tmp");
		//		 System.out.println("@Query2FileThread.query2File SQL:" + sql);
		if (cnn == null) {
			System.out.println("######<cnn == null>#################");
			this.connector = new ServerConnecter();// �f�t�H���g�͓��YSQL�T�[�o�[
		} else {
			this.connector = cnn;
		}
		// super.setLengthOfTask((int)this.sqlCount); // �ő�X�e�b�v��
		if (literalFlag == true) {
			this.setDelimiter(",");
			this.setLiteral("'");
		}
		this.setPreHead(preHead);
	}

	@Override
	public String getMessage() {
		long size = outFile.length();
		if (size == 0) {
			return " �f�[�^���o��...";
		} else {
			// return outFile.getName() + " > " + size + " byte....";
			return " " + size + " byte�o��....";
		}
	}

	// ------------------------------------------------------------------------
	// �ʃX���b�h�œ������ꍇ
	// ------------------------------------------------------------------------
	@Override
	public void execute() {
		super.start("TaskSQL2FileMod", 2048);
		new ActualTask();
		super.stop();// ����I��
	}

	// ------------------------------------------------------------------------
	// �_�C���N�g�ɓ������ꍇ
	// ------------------------------------------------------------------------
	public void executeDirect() {
		new ActualTask();
	}

	// ------------------------------------------------------------------------
	// �s���ۂ̏����t
	// ------------------------------------------------------------------------
	class ActualTask {

		// private kyPkg.tools.Elapse elapse;

		ActualTask() {
			//          System.out.println("@TaskSQL2File.ActualTask  <Start>##");
			JDBC jdbcObj = null;
			try {
				// elapse = new kyPkg.tools.Elapse();
				// elapse.start();
				//		setLengthOfTask((int) sqlCount);
				jdbcObj = connector.getConnection();
				EzWriter outClosure = new EzWriter(outPath);
				outClosure.open();
				// ---------------------------------------------------------------------
				// if ( canceled == true ){} // �����f�����I�I
				// ---------------------------------------------------------------------
				if (jdbcObj != null) {
					EzReader reader = new EzReader(sqlPath);
					reader.open();
					String sql;
					int i = 0;
					while ((sql = reader.readLine()) != null) {
						i++;
						setCurrent(i + 1);
						Query2FileThread thread = new Query2FileThread(jdbcObj,
								outClosure, sql, hash, trimOption);
						thread.setKeyEnc(keyEnc);
						thread.setPreHead(preHead);
						thread.setLiteral(literal); // ����������e�����ň͂����ǂ����E�E�E
						thread.setDelimiter(delimiter); // ��؂蕶���̐ݒ�
						thread.setCelConverter(celConverter);
						thread.start();
						try {
							thread.join(); // �����œ�����������
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					reader.close();
					stop();// ����I��
					//					jdbcObj.close(); // �c�a���N���[�Y
				} else {
					System.out.println("#ERROR:@TaskSQL2File jdbcObj == null");
				}
				outClosure.close();
			} catch (Exception ee) {
				ee.printStackTrace();
			} finally {
				if (jdbcObj != null) {
					jdbcObj.close(); 
					System.out.println("##########################");
					System.out.println("### �c�a���N���[�Y");
					System.out.println("##########################");
				}
				stop();
				// System.out.println("## ActualTask <End> #:" + sqls.length);
				// elapse.stop();
			}
		}
	}

	public void setKeyEnc(boolean keyEnc) {
		this.keyEnc = keyEnc;
	}

	public void setPreHead(boolean preHead) {
		this.preHead = preHead;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public void setLiteral(String literal) {
		this.literal = literal;
	}

	public void setCelConverter(CelConverter celConverter) {
		this.celConverter = celConverter;
	}

	public static void testTaskSQL2FileMod() {
		Connector cnn = Connectors.get("Server");
		String path = "C:/@qpr/home/123620000016/tran/20150401_20160331preX.pre";
		String sqlPath = "C:/@qpr/home/123620000016/tran/20150401_20160331Sql.sql";
		String hash = "xxhashxx";
		boolean literalFlag = false;
		boolean preHead = false;
		TaskSQL2FileMod ins = new TaskSQL2FileMod(path, cnn, sqlPath, hash,
				literalFlag, preHead);
		ins.execute();
	}

	public static void main(String[] argv) {
		testTaskSQL2FileMod();
	}
}
