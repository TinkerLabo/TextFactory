package kyPkg.task;

import kyPkg.uFile.FileUtil;
import java.io.*;
//-----------------------------------------------------------------------------
// �t�@�C���ҏW���`           k.yuasa 2006.2.13 
//-----------------------------------------------------------------------------

public class HirMover extends Abs_ProgressTask {
	static final String FS = System.getProperty("file.separator");
	private String[] array;
	private String parent;

	public HirMover() {
		super();
	}

	public void init(String[] args) {
		int aLen = args.length;
		if (aLen == 1) {
			init(args[0]);
		}
	}

	// -------------------------------------------------------------------------
	// ���������p�����[�^�ݒ�Ȃ�
	// ���S�̂̃��[�v�񐔂̌��ς���ȂǁE�E�E
	private void init(String pPath) {
		File file = new File(pPath);
		array = file.list();
		parent = FileUtil.getParent(file.getAbsolutePath());
		parent = parent + FS + "Result";
		// setLengthOfTask(array.length);
	}

	// ------------------------------------------------------------------------
	// �O������R�[�������g���K�[
	// ------------------------------------------------------------------------
	@Override
	public void execute() {
		super.start("HirMover",2048);
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

	// -------------------------------------------------------------------------
	class ActualTask {
		ActualTask() {
			System.out.println("## ActualTask ## on HirMover");
			for (int i = 0; i < array.length; i++) {
				setCurrent(i);
				setMessage("Hir Completed " + getCurrent() + " out of "
						+ (getTotalLen() + 1) + ".");
				String path = array[i];
				filterT(path, parent);
			}
			stop();
			setCurrent(getTotalLen());
		}
	}

	// ---+---------+---------+---------+---------+---------+---------+---------+
	/**
	 * filterT �t�B���^�[�v���O���� �w�肳�ꂽ�t�@�C����ǂݍ���ŁA���C�^�[�֏����o��
	 * 
	 * @param path
	 *            ���̓p�X
	 * @param wParent
	 *            �o�͐�e�p�X
	 * @param pBef
	 *            �ϊ��O�A������
	 * @param pAft
	 *            �ϊ���A������
	 * @return �ϊ������ꍇ��true
	 */
	public static boolean filterT(String path, String wParent) {
		File wDir = new File(wParent);
		if (wDir.exists() == false)
			wDir.mkdir();
		boolean swt = false;
		String wRec = "";
		File file = new File(path);
		String wPath_O = "";
		if (file.isFile()) {
			try {
				wPath_O = "";
				String wFnm = file.getName();
				// String wPath_O1 = wParent + wfs + wFnm;
				BufferedReader br = FileUtil.getBufferedReader(path);
//				BufferedReader br = new BufferedReader(new FileReader(file));
				while ((wRec = br.readLine()) != null) {
					if (wRec.matches("\\s*package\\s+.+;\\s*")) {
						wRec = wRec.trim();
						System.out.println("package=>" + wRec);
						int i = wRec.indexOf(" ");
						int j = wRec.indexOf(";");
						wPath_O = wRec.substring(i + 1, j).trim();
						String wArray[] = wPath_O.split("\\.");
						StringBuffer wSbuf = new StringBuffer();
						wSbuf.append(wParent);
						wSbuf.append(FS);
						for (int k = 0; k < wArray.length; k++) {
							wSbuf.append(wArray[k]);
							wSbuf.append(FS);
						}
						wSbuf.append(wFnm);
						// String wPath2 = wPath_O.replaceAll("\\.",wfs);>����͑ʖ�
						wPath_O = wSbuf.toString();
					}
				}
				br.close();
				if (!wPath_O.equals("")) {
					System.out.println("path_O=>" + wPath_O);
					System.out.println("makeParents");
					FileUtil.makeParents(wPath_O);
					System.out.println("copyIt");
					FileUtil.fileCopy(wPath_O, path);
				}
			} catch (IOException ie) {
				ie.printStackTrace();
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			swt = true;
		}
		return swt;
	}
}
