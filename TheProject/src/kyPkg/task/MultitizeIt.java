package kyPkg.task;

import java.io.*;
//-----------------------------------------------------------------------------
//�@�ėpI/O�@�@�@                                      K.Yuasa 2008.2.25
//-----------------------------------------------------------------------------
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import kyPkg.Sorts.Comparator4Delim;
import kyPkg.uFile.ListArrayUtil;
import kyPkg.util.BitMapArray;

public class MultitizeIt extends Abs_ProgressTask {
	private static final String LS = System.getProperty("line.separator");
	private static final int MOTHER = 2;
	private static final int CHILE = 3;
	private String path_I;
	private String path_O;
	private int rangeMother;
	private int rangeChile;
	private int[] converter;
	private String delimita = ",";
	private int[] sortOrders = new int[] { 1, 2 };

	// ------------------------------------------------------------------------
	// Constructor
	// �ăJ�e�S���C�Y���邽�߂�CONVERTER���K�v
	// �Ⴆ�Ό�-�����P�Ƃ���A���͕s�v�Ƃ��������P�[�X���l����
	// ------------------------------------------------------------------------
	public MultitizeIt(String outPath, String outDir, String[] mother,
			String[] chile, String path_I, String delim) {
		this(outPath, outDir, mother, chile, null, path_I, delim);
	}

	public MultitizeIt(String outPath, String outDir, String[] mother,
			String[] chile, int[] converter, String path_I, String delim) {
		super();
		kyPkg.etc.QTBwriter.writeISAMctrl(outDir, mother, chile, delim); // �@�ʒu����낵���Ȃ��C������
		this.path_I = path_I;
		this.path_O = outPath;
		if (mother != null)
			this.rangeMother = mother.length;
		if (chile != null)
			this.rangeChile = chile.length;
		this.converter = converter;
	}

	// ------------------------------------------------------------------------
	// �A�N�Z�b�T
	// ------------------------------------------------------------------------
	public void setDelimita(String delimita) {
		this.delimita = delimita;
	}

	public void setSortOrders(int[] sortOrders) {
		this.sortOrders = sortOrders;
	}

	// ------------------------------------------------------------------------
	// �O������R�[�������g���K�[
	// ------------------------------------------------------------------------
	@Override
	public void execute() {
		super.start("MultitizeIt", 2048);
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
			sortAndEtc();
			stop();// ����I��

		}

		// -------------------------------------------------------------------------
		// sortAndEtc
		// -------------------------------------------------------------------------
		public void sortAndEtc() {
			BitMapArray bmpArray = new kyPkg.util.BitMapArray(rangeMother);
			String saveID = "";
			// String path_T = path_I + ".tmp"; // .tmp�������Ƃ����O��
			int wCnt = -1;
			List list = ListArrayUtil.file2List(path_I);
			Collections.sort(list, new Comparator4Delim(delimita, sortOrders));
			// setLengthOfTask(list.size()); // �v���O���X�o�[�̑S�̂̒���
			// ---------------------------------------------------------------------
			// Loop !!
			// ---------------------------------------------------------------------
			try {
				// File fileTMP = new File(path_T);
				File fileOUT = new File(path_O);
				if (fileOUT.exists())
					fileOUT.delete();
				if (fileOUT.exists()) {
					System.out
							.println("output file is Existed and couldn't Delete!! :"
									+ path_O);
				}
				// fileTMP.renameTo(fileOUT);
				System.out.println("@multitizeIt ������" + path_O);

				FileWriter writer = new java.io.FileWriter(path_O);
				StringBuffer buff = new StringBuffer(256);

				for (Iterator iter = list.iterator(); iter.hasNext();) {

					setCurrent(wCnt++); // �v���O���X�o�[�̈ʒu

					String element = (String) iter.next();
					String[] array = element.split("\t");
					if (array.length > 2) {
						if (!saveID.equals(array[0])) {
							// bmpArray.setEnclosure("'");
							// System.out.println(saveID + "_" +
							// map.toString()+"=>"+insMap.cnv2String(size48));
							// map.clear();
							if (!saveID.equals("")) {
								buff.delete(0, buff.length());
								// System.out.println("rangeChile:" +
								// rangeChile);
								String wRec = saveID + ","
										+ bmpArray.cnv2String(rangeChile);
								// System.out.println(wRec);
								buff.append(wRec);
								buff.append(LS);
								writer.write(buff.toString());
							}
							bmpArray.clear();
						}
						try {
							int dim0 = Integer.parseInt(array[MOTHER]);
							int dim1 = Integer.parseInt(array[CHILE]);
							// System.out.println("dim0=>" + dim0 + " dim1=>" +
							// dim1);
							if (converter != null && converter.length > dim1) {
								dim1 = converter[dim1];
								// System.out.println("converter?=>" + dim1);
							}
							bmpArray.add(dim0, dim1);
						} catch (Exception e) {
							e.printStackTrace();
						}

						saveID = array[0];
					}
				}
				writer.close();
				System.out.println("@multitizeit �����I");
			} catch (IOException ie) {
				ie.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
