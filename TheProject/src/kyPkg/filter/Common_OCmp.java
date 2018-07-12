package kyPkg.filter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
//�ewriter�̏o�̓J�E���g����肽���E�E�E�E
public class Common_OCmp {
	private String inPath = "";
	private String tempPath = "";
	private String stat = "";
	private List<Writer> writers;
	private HashMap<String, Integer> hmap;
	private List<String> outList;

	private void init() {
		tempPath = "";
		stat = "";
		writers = new ArrayList<Writer>();
		hmap = new HashMap();
		outList = new ArrayList();
	}

	// -------------------------------------------------------------------------
	// ���̓X�g���[�������[�_�[�Ƃ���ꍇ
	// Reader in = new FileReader(inFile);
	// xxx.setReader(in);
	// -------------------------------------------------------------------------
	// String���X�g���[�������ɂ��ă��C�����[�h����ꍇ
	// StringReader in = new StringReader(jTa1.getText());
	// -------------------------------------------------------------------------
	public Common_OCmp() {
		init();
		this.outList.add("");
		addOutFile(outList.get(0), false);
	}
	// -------------------------------------------------------------------------
	// ���o�͂��Ⴄ�ꍇ�iappend ��true�Ȃ�ǉ��������݁j
	// -------------------------------------------------------------------------
	public Common_OCmp(List<String> outLists, String inPath, boolean append) {
		init();
		if (outLists != null) {
			for (Iterator iterator = outLists.iterator(); iterator.hasNext();) {
				String outPath = (String) iterator.next();

				if (inPath.equals(outPath)) {
					outPath = outPath + ".tmp";
					this.tempPath = outPath;
				}

				this.addOutFile(outPath, append);
			}
		}
	}

	public int addOutFile(String outPath, boolean append) {
		Integer index = hmap.get(outPath);
		if (index == null) {
			try {
				if (outPath.equals("")) {
					this.writers.add(new StringWriter(2048));
				} else {
					// append ��true�Ȃ�ǉ���������
					this.writers.add(new FileWriter(outPath, append));
				}
				this.outList.add(outPath);
				index = writers.size() - 1;
				hmap.put(outPath, index);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return index;
	}

	public Writer getWriter(int index) {
		return writers.get(index);
	}

	// -------------------------------------------------------------------------
	// �t�@�C�������
	// -------------------------------------------------------------------------
	public void close() {
		try {

			for (Iterator iterator = writers.iterator(); iterator.hasNext();) {
				Writer writer = (Writer) iterator.next();
				if (writer != null)
					writer.close();
			}

			// io�������p�X�Ȃ�A��Utmp�ɏo�͂����ナ�l�[������
			if (!tempPath.equals("")) {
				File File_i = new File(inPath);
				File_i.delete();
				File File_o = new File(tempPath);
				File_o.renameTo(File_i);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	protected String getOutPath(int index) {
		return outList.get(index);
	}

	protected String getOutDir(int index) {
		return kyPkg.uFile.FileUtil.getParent2(outList.get(index), true);
	}

	public String getString() {
		return getWriter(0).toString();
	}

	// -------------------------------------------------------------------------
	// finalyze
	// -------------------------------------------------------------------------
	protected void finalyze() {
		System.out.println("<@Abs_FileFilter Finalyze>");
		close();
	}

	protected void setStat(String stat) {
		this.stat = stat;
	}

	public String getStat() {
		return this.stat;
	}

}