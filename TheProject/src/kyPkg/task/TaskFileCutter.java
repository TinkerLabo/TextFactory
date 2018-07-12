package kyPkg.task;

import java.io.*;

import kyPkg.uFile.FileObj;

public class TaskFileCutter extends Abs_ProgressTask {
	private static final String LF = System.getProperty("line.separator");
	private static final String FS = System.getProperty("file.separator");
	private long limit = Long.MAX_VALUE;
	private long cut = Long.MAX_VALUE;
	private String head = "";
	private String foot = "";
	private String iPath;
	private String oDir;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	public TaskFileCutter(String oDir, String iPath, int cut) {
		this(oDir, iPath, (long) cut);
	}

	public TaskFileCutter(String oDir, String iPath, long cut) {
		super();
		this.oDir = oDir;
		this.iPath = iPath;
		this.cut = cut;
	}

	// ------------------------------------------------------------------------
	// �A�N�Z�b�T
	// ------------------------------------------------------------------------
	public void setFoot(String foot) {
		this.foot = foot;
	}

	public void setHead(String head) {
		this.head = head;
	}

	// ------------------------------------------------------------------------
	// �O������R�[�������g���K�[
	// ------------------------------------------------------------------------
	@Override
	public void execute() {
		super.start("TaskFileCutter",2048);
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
	// for �P�̃e�X�g
	// T:\eclipse\workspace\kyProject\kkk\qpr_buying_data_20080316.txt
	// ------------------------------------------------------------------------
	public static void main(String[] argv) {
//		Inf_ProgressTask ins = new TaskFileCutter("c:/divIt", ResControl.D_DAT + Mrg_Tran.NQDATA_DAT,
//				500000);
//		((TaskFileCutter) ins).setFoot("20080317091554	20080317091554	0500000");
//		ins.execute();
	}

	// ------------------------------------------------------------------------
	// �s���ۂ̏����t
	// ------------------------------------------------------------------------
	class ActualTask {
		ActualTask() {
			// setLengthOfTask(256); // ����������P�ʂ̃T�C�Y

			// I/O etc...
			// setLengthOfTask(256); //����������P�ʂ̃T�C�Y
			// filter01(path_O, path_I);
			// setCurrent(256); //������t�B���^���ł̈ʒu���Z�b�g����΂悢

			filter01(oDir, iPath);

			stop();// ����I��

		}

		// -------------------------------------------------------------------------
		// filter
		// -------------------------------------------------------------------------
		public void filter01(String oDir, String iPath) {

			int seq = 0;
			int lineCount = -1;
			String wRec;

			// ---------------------------------------------------------------------
			// oDir��mkdir��������ق����ǂ�
			// ---------------------------------------------------------------------
			FileObj fObj = new FileObj(iPath);

			long fileSize = fObj.getLength() / 80; // ���R�[�h�����W�O�o�C�g�Ɖ���

			// setLengthOfTask((int) fileSize); // �v���O���X�o�[�̑S�̂̒���

			// ---------------------------------------------------------------------
			// Loop !!
			// ---------------------------------------------------------------------
			try {
				BufferedReader br = new BufferedReader(new FileReader(iPath));
				String oPath;
				FileWriter writer = null;
				long wCut = cut;

				while ((wRec = br.readLine()) != null && limit > lineCount) {
					wCut++;
					if (wCut > cut) {
						if (writer != null) {
							if (!foot.equals("")) {
								writer.write(foot);
								writer.write(LF);
							}
							writer.close();
						}
						seq++;
						oPath = oDir + FS + fObj.getName() + "_" + seq
								+ fObj.getExt();
						System.out.println("TaskFileCutter ������" + oPath);
						writer = new java.io.FileWriter(oPath);
						if (!head.equals("")) {
							writer.write(head);
							writer.write(LF);
						}
						wCut = 1;
					}
					setCurrent(lineCount++); // �v���O���X�o�[�̈ʒu
					writer.write(wRec);
					writer.write(LF);
				}
				br.close();
				if (writer != null) {
					if (!foot.equals("")) {
						writer.write(foot);
						writer.write(LF);
					}
					writer.close();
				}
				System.out.println("����");
			} catch (IOException ie) {
				ie.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
