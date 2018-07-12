package kyPkg.filter;

import kyPkg.task.Abs_ProgressTask;

public class Flt_Substr2Layout extends Abs_ProgressTask {
	private Inf_iClosure inClosure = null; // ���̓N���[�W��
	private Inf_oClosure outClosure = null; // �o�̓N���[�W��
	private int fldLen;
	private String fldSample;

	// -------------------------------------------------------------------------
	// 2013-03-29 yuasa
	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public Flt_Substr2Layout(String outPath, String inPath) {
		super();
		this.inClosure = new EzReader(inPath);
		this.outClosure = new EzWriter(outPath);
	}

	public long getWriteCount() {
		return outClosure.getWriteCount();
	}

	public void setDelimiter(String delimiter) {
		this.outClosure.setDelimiter(delimiter);
	}

	@Override
	public void execute() {
		super.start("Flt_Substr2Layout", 2048);
		outClosure.open();
		loop();// loop
		outClosure.close();
		super.stop();// ����I��
	}

	// -------------------------------------------------------------------------
	// tranLoop
	// -------------------------------------------------------------------------
	private long loop() {
		long lCount = -1;
		inClosure.open();
		String[] cells = null;
		while ((cells = inClosure.readSplited()) != null) {
			if (cells.length >= 1) {
				cells[1] = cells[1].replaceAll("#", "");
				if (cells[0].startsWith("D,")) {
				} else if (cells[0].startsWith("@,")) {
					String array[] = cells[0].split(",");
					fldLen = array[1].length();
					array[1] = array[1].trim();
					if (array[1].equals("")) {
						fldSample = "";
					} else {
						fldSample = "�s" + array[1].trim() + "�t";
					}
					outClosure.write(
							"" + cells[1] + fldSample + ",," + fldLen + ",");
					lCount++;
				} else {
					if (cells.length >= 2) {
						String array[] = cells[0].split(",");
						if (array.length >= 3) {
							if (array[3].equals("���t�ϊ�6")) {
								fldLen = 6;
							} else {
								fldLen = Integer.parseInt(array[2]);
							}
							outClosure
									.write("" + cells[1] + ",," + fldLen + ",");
							lCount++;
						} else {
							outClosure.write("�������y" + cells[1] + "�z��?��?���@�@,,0,"
									+ cells[0]);
							lCount++;
						}
					}
				}
			}
		}
		inClosure.close();
		return lCount;
	}

	// -------------------------------------------------------------------------
	// �g�p�၄
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		tester();
	}

	// test
	public static void tester() {
		// �A���P�[�g�f�[�^�����ԓ��L�����j�^�[�Ɍ��肷��
		// String userDir = kyPkg.uFile.ResControl.getUserHomeQPR();
		String inPath = "C:/mmJan/JanCnvP.txt";
		String outPath = "C:/mmJan/result.csv";
		Flt_Substr2Layout ins = new Flt_Substr2Layout(outPath, inPath);
		ins.execute();
	}

}
