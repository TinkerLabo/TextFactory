package kyPkg.task;

import java.io.*;

import kyPkg.uFile.FileObj;
import kyPkg.uFile.FileUtil;

public class TaskTemplate extends Abs_ProgressTask {
	private static final String LF = System.getProperty("line.separator");

	private static final String FS = System.getProperty("file.separator");

	private long limit = Long.MAX_VALUE;

	private long cut = 20;

	private String path_I;

	private String path_O;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	public TaskTemplate(String path_O, String path_I, long limit) {
		super();
		this.limit = limit;
		this.path_I = path_I;
		this.path_O = path_O;
	}

	// ------------------------------------------------------------------------
	// �O������R�[�������g���K�[
	// ------------------------------------------------------------------------
	@Override
	public void execute() {
		super.start("TaskTemplate", 2048);
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
			// setLengthOfTask(256); // ����������P�ʂ̃T�C�Y

			// I/O etc...
			// setLengthOfTask(256); //����������P�ʂ̃T�C�Y
			// filter01(path_O, path_I);
			// setCurrent(256); //������t�B���^���ł̈ʒu���Z�b�g����΂悢

			filter01(path_O, path_I);

			stop();// ����I��

		}

		// -------------------------------------------------------------------------
		// filter
		// -------------------------------------------------------------------------
		public void filter01(String oDir, String path) {
			int seq = 1;
			int lineCount = -1;
			String wRec;

			// ---------------------------------------------------------------------
			// oDir��mkdir��������ق����ǂ�
			// ---------------------------------------------------------------------
			FileObj fObj = new FileObj(path);

			long fileSize = fObj.getLength() / 80; // ���R�[�h�����W�O�o�C�g�Ɖ���

			// setLengthOfTask((int) fileSize); // �v���O���X�o�[�̑S�̂̒���

			// ---------------------------------------------------------------------
			// Loop !!
			// ---------------------------------------------------------------------
			try {
				StringBuffer buff = new StringBuffer(256);
				BufferedReader br = FileUtil.getBufferedReader(path);
//				BufferedReader br = new BufferedReader(new FileReader(path));
				String oPath;
				FileWriter writer = null;
				long wCut = 20;

				while ((wRec = br.readLine()) != null && limit < lineCount) {
					wCut++;
					if (wCut > cut) {
						if (seq > 1)
							writer.close();
						seq++;
						oPath = oDir + FS + fObj.getName() + seq
								+ fObj.getExt(); // .tmp�������Ƃ����O��
						System.out.println("taskTemplaete ������" + oPath);
						writer = new java.io.FileWriter(oPath);
						wCut = 0;
					}

					setCurrent(lineCount++); // �v���O���X�o�[�̈ʒu
					buff.delete(0, buff.length());
					buff.append(wRec);
					buff.append(LF);
					writer.write(buff.toString());

				}
				br.close();
				writer.close();

			} catch (IOException ie) {
				ie.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
