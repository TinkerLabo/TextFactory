package kyPkg.task;

import kyPkg.converter.CelConverter;
import kyPkg.converter.Inf_ArrayCnv;
//import kyPkg.converter.Inf_LineConverter;
import kyPkg.filter.EzWriter;
import kyPkg.sql.*; //import kyPkg.uFile.ListArrayUtil;
import kyPkg.uFile.FileUtil;

import java.util.List;

//-----------------------------------------------------------------------------
// ���s���̏�ԁiStatus�j���c�����@�͂Ȃ����H 
// ��kyPkg.util.SwingWorker.java �Ɉˑ����Ă��܂�
// ��kyPkg.panel.qpr.calc.Calc_Common.java �Ɉˑ����Ă��܂�
//-----------------------------------------------------------------------------
public class TaskSQL2File extends Abs_ProgressTask {
	// private boolean interrupted = false;
	private boolean trimOpt = false;
	private boolean keyEnc = true;
	private CelConverter celConverter = null; // 2008/10/31 Halloween
	private boolean preHead = false;
	private String literal = null;
	private String delimiter = "\t";
	private Connector connector = null;
	private String hash = "";
	private Inf_ArrayCnv converter;
	private long writeCount = 0;

	private JDBC jdbc;
	// ------------------------------------------------------------------------
	// sql�ɑ΂���n�b�V���l�i�O���[�v���j������ �Ȃ瓯���t�H�[�}�b�g�������K�p�����
	// ------------------------------------------------------------------------

	private String[] sqls;
	private String outPath;
	// ------------------------------------------------------------------------
	// �w�b�_�[
	// ------------------------------------------------------------------------
	private String header = null;

	public void setHeader(String header) {
		this.header = header;
	}

	public void setHeader(List<String> headList, String delimiter) {
		this.header = kyPkg.util.KUtil.list2String(headList, delimiter);
	}

	public void setHeader(String[] headArray, String delimiter) {
		this.header = kyPkg.util.KUtil.array2String(headArray, delimiter);
	}

	public void setTrimOption(boolean trimOption) {
		this.trimOpt = trimOption;
	}

	public void setHash(String hash) {
		this.hash = hash;
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

	public long getWriteCount() {
		return writeCount;
	}

	public void setConverter(Inf_ArrayCnv converter) {
		this.converter = converter;
	}

	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	public TaskSQL2File(String path, Connector cnn, String sql,
			String delimiter) {
		this(path, cnn, sql);
		setDelimiter(delimiter);
	}

	// literalFlag��true�Ȃ炷�ׂĂ̍��ڂɃ��e���������A�J���}��؂�ƂȂ�
	public TaskSQL2File(String path, Connector cnn, String sql,
			boolean literalOpt) {
		this(path, cnn, sql);
		this.setLiteralFlag(literalOpt);
	}

	public TaskSQL2File(String path, Connector cnn, List<String> sqlList) {
		this(path, cnn, list2array(sqlList));
	}

	public TaskSQL2File(String path, Connector cnn, String sql) {
		this(path, cnn, new String[] { sql });
	}

	public TaskSQL2File(String path, String sql) {
		this(path, new String[] { sql });
	}

	public TaskSQL2File(String path, List<String> sqlList) {
		this(path, list2array(sqlList));
	}

	public TaskSQL2File(String path, String[] sqls) {
		this(path, Connectors.get("Server"), sqls);
	}

	public TaskSQL2File(String path, String[] sqls, Inf_ArrayCnv converter) {
		this(path, Connectors.get("Server"), sqls);
		this.converter = converter;
	}

	public TaskSQL2File(String path, Connector cnn, String[] sqls) {
		setMessage("�₢���킹���E�E�E");
//		System.out.println("### TaskSQL2File ### path:" + path);
		this.outPath = path;
		if (cnn == null) {
			this.connector = new ServerConnecter();// �f�t�H���g�͓��YSQL�T�[�o�[
		} else {
			this.connector = cnn;
		}
		this.sqls = sqls;
	}

	public void setLiteralFlag(boolean literalFlag) {
		if (literalFlag == true) {
			this.setDelimiter(",");
			this.setLiteral("'");
		}
	}

	// list => arrayF
	private static String[] list2array(List list) {
		return (String[]) list.toArray(new String[list.size()]);
	}

	// @Override
	// public void stop() {
	// // System.out.println("### TaskSQL2File.STOP() ###");
	// // for (Query2FileThread thread : threads) {
	// // thread.interrupt();
	// // }
	// // interrupted = true;
	// super.stop();
	// // jdbc.close();
	// // setDone(true);
	// }
	// ------------------------------------------------------------------------
	// execute
	// ------------------------------------------------------------------------
	@Override
	public void execute() {
		super.start("TaskSQL2File", 2048);
		if (hash.equals("")) {
			hash = kyPkg.uDateTime.DateCalc.getTimeStamp(4, 0, "_");
		}
		query();
		super.stop();// ����I��
	}

	public void query() {
		// threads = new ArrayList();
		writeCount = 0;
		String info = "";
		// System.out.println("@TaskSQL2File.ActualTask  <Start>##");
		try {
			// elapse = new kyPkg.tools.Elapse();
			// elapse.start();
			// setLengthOfTask(sqls.length);

			jdbc = connector.getConnection();
			EzWriter writer = new EzWriter(outPath, converter);
			writer.setCharsetName(FileUtil.MS932);

			if (header != null) {
				writer.setHeader(header);
			}
			writer.open();
			// ---------------------------------------------------------------------
			// if ( canceled == true ){} // �����f�����I�I
			// ---------------------------------------------------------------------
			if (jdbc != null) {
				for (int i = 0; i < sqls.length; i++) {
					// setCurrent(i + 1);
					info = "PickUp #" + (i + 1) + " of " + sqls.length;
					setMessage(info);
					// setProgress((i + 1), sqls.length);
					try {
						Query2FileThread q2file = new Query2FileThread(jdbc,
								writer, sqls[i], hash, trimOpt);
						// q2file.add(thread);
						q2file.setKeyEnc(keyEnc);
						q2file.setPreHead(preHead);
						q2file.setLiteral(literal); // ����������e�����ň͂����ǂ����E�E�E
						q2file.setDelimiter(delimiter); // ��؂蕶���̐ݒ�
						q2file.setCelConverter(celConverter);
						q2file.start();
						q2file.join(); // �����œ�����������
						writeCount += q2file.getCounter();

					} catch (InterruptedException e) {
						e.printStackTrace();
						System.out.println(
								"InterruptedException@TaskSQL2File:���@���@���@�X���b�h���荞�݁I�I���@���@���@");
						return;
					}
					// if (interrupted) {
					if (!super.isStarted()) {
						System.out.println("Interrupted ���@���f�@���@");
						abend();// �ُ�I��
						jdbc.close(); // �c�a���N���[�Y
						writer.close();
						return;
					}
				}
				jdbc.close(); // �c�a���N���[�Y
			} else {
				System.out.println("#ERROR:@TaskSQL2File jdbcObj == null");
			}
			writer.close();
			long lCount = writer.getWriteCount();
			if (lCount == 0) { // XXX if GUI Version??
				System.out.println("NotFound @TaskSQL2File");
				// JOptionPane.showMessageDialog((Component) null, new
				// JLabel(
				// "NotFound"), "Error...", JOptionPane.ERROR_MESSAGE);
				abend();
			}

		} catch (Exception ee) {
			ee.printStackTrace();
		} finally {
			int val = (int) writeCount;
			putExtra("WriteCount", val);
			// System.out.println("## ActualTask <End> #:" + sqls.length);
			// elapse.stop();
		}
	}

	// ------------------------------------------------------------------------
	// �g�p�၄
	// ------------------------------------------------------------------------
	public static void main(String[] argv) {
		String curDir = globals.ResControl.getCurDir();
		String outPath = curDir + "output.txt";
		String sql = "select * from XXX;";
		Connector connector = new CommonConnector("jurl???", "sa", "");
		kyPkg.task.TaskSQL2File task = new kyPkg.task.TaskSQL2File(outPath,
				connector, sql);
		task.execute();
	}
}
