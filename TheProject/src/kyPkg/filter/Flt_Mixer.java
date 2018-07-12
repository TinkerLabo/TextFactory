package kyPkg.filter;

import static kyPkg.util.Faker.join;
import static kyPkg.util.Faker.joinPlus;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import globals.ResControl;
import globals.ResControlWeb;
import kyPkg.task.Abs_ProgressTask;
//import kyPkg.converter.Inf_ArrayCnv;
import kyPkg.uFile.File49ers;
import kyPkg.uFile.FileUtil;

//�@XXX �t�@�C�������łȂ��A�X�g���[������o�͂ɕς����ق������������m��Ȃ��E�E�E�E
public class Flt_Mixer extends Abs_ProgressTask {
	private Inf_oClosure outClosure = null; // �o�̓N���[�W��
	// L,R,I,F�̂����ꂩ
	public static String FULL = "full";
	public static String RIGHT = "right";
	public static String LEFT = "left";
	public static String INNER = "inner";
	private boolean write_R = true;
	private boolean write_L = true;

	private class ElementsLR {
		private String Left = null;
		private String Right = null;

		public String getLeft() {
			return Left;
		}

		public String getRight() {
			return Right;
		}

		public void setLeft(String left) {
			Left = left;
		}

		public void setRight(String right) {
			Right = right;
		}
	}

	// �f�[�^���ɂ��炩����#�����݂���ꍇ�͓K���ȕ����ɒu��������(���K�\���̓��ꕶ���ȊO���w�肷��)
	private String magicChar = "@";
	private int magicIndex = -1; // ���ӂւ̑}���ʒu�i�������������Z���j
	private char funC = 'I'; // Default => Inner Join
	private String delimiter = "\t";
	private String path_L = "";
	private String path_R = "";
	private String DUMMY_R = "DummyR";
	private String DUMMY_L = "DummyL";
	private long wCnt = 0;
	private HashMap mixerMap;

	// -------------------------------------------------------------------------
	// �o�̓t�@�C���̃f���~�^
	// -------------------------------------------------------------------------
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	// -------------------------------------------------------------------------
	// �L�[���ڈȊO�̏o�͂��R���g���[������ꍇ�Ɏg�p����ifalse�Ȃ�o�͂��Ȃ��j
	// -------------------------------------------------------------------------
	public void setWrite_L(boolean write_L) {
		this.write_L = write_L;
	}

	public void setWrite_R(boolean write_R) {
		this.write_R = write_R;
	}

	public void setMagicIndex(int magicIndex) {
		this.magicIndex = magicIndex;
	}

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// �o�͐�̋�؂蕶�����ȗ������ꍇ�^�u��؂�ƂȂ�
	// �o�̓t�@�C���̊g���q�����؂蕶�������肵�Ă��ǂ������m��Ȃ��E�E�E
	// -------------------------------------------------------------------------
	public Flt_Mixer(String func, String outPath, String path_L, String path_R) {
		this(func, new EzWriter(outPath), path_L, path_R);
	}

	public Flt_Mixer(String func, Inf_oClosure outClosure, String path_L,
			String path_R) {
		super();
		this.funC = (func.toUpperCase()).charAt(0); // L,R,I,F�̂����ꂩ
		this.path_L = path_L;
		this.path_R = path_R;
		this.outClosure = outClosure;
	}

	// -------------------------------------------------------------------------
	// public Flt_Mixer(String func, String path_O, String path_L, String
	// path_R) {
	// this(func, path_O, path_L, path_R, true, true);
	// }
	// private Flt_Mixer(String func, String path_O, String path_L, String
	// path_R,
	// boolean write_R) {
	// this(func, path_O, path_L, path_R, true, write_R);
	// }
	// public Flt_Mixer(String func, String path_O, String path_L, String
	// path_R,
	// boolean write_L, boolean write_R) {
	// this(func, "\t", path_O, path_L, path_R, write_L, write_R);
	// }
	// -------------------------------------------------------------------------
	// execute()
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.start("Flt_Mixer", 2048);
		File49ers f49_L = new File49ers(path_L);
		File49ers f49_R = new File49ers(path_R);
		int max_L = f49_L.getMaxColCount();
		int max_R = f49_R.getMaxColCount();
		// System.out.println("funC   :" + funC);
		// System.out.println("path_L :" + path_L);
		// System.out.println("path_R :" + path_R);
		// System.out.println("delim_L>" + f49_L.getDelimiter() + "< max_L:"+
		// max_L);
		// System.out.println("delim_R>" + f49_R.getDelimiter() + "< max_R:"+
		// max_R);
		DUMMY_L = "";
		DUMMY_R = "";
		if (magicIndex > 1) {
			if (max_L < magicIndex)
				max_L = magicIndex; // magicIndex�͂Q�Z���߈ȏ�Ȃ̂ŗv�f���Ɠ����ɂȂ�
		}
		if (max_L > 1) {
			// �����2�Z���ڈȍ~�Ɏ��߂�_�~�[�Ȃ̂ŗv�f���́[�P�ƂȂ�A�_�~�[�𖄂ߍ��ވʒu�����炵�Ă���
			DUMMY_L = joinPlus(new String[max_L - 1],
					delimiter, magicIndex - 2, magicChar);
		}
		if (max_R > 1) {
			DUMMY_R = join(new String[max_R - 1], delimiter);
		}
		boolean wSw = false;
		String val_L = null;
		String val_R = null;
		wCnt = 1;
		mixerMap = new HashMap();
		long cnt_L = incore('L', path_L, f49_L.getDelimiter(), max_L);
		long cnt_R = incore('R', path_R, f49_R.getDelimiter(), max_R);
		// System.out.println("#cnt_L:" + cnt_L);
		// System.out.println("#cnt_R:" + cnt_R);
		try {
			outClosure.open();
			Set set = mixerMap.entrySet(); // ����iterator���ĂׂȂ��̂ň�USET���擾����
			Iterator it = set.iterator();
			StringBuffer buf = new StringBuffer();
			while (it.hasNext()) {
				setCurrent((int) wCnt);
				java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
				String key = (String) ent.getKey();
				ElementsLR element = (ElementsLR) ent.getValue();
				if (element == null) {
					val_L = null;
					val_R = null;
				} else {
					val_L = element.getLeft();
					val_R = element.getRight();
				}

				wSw = false;
				switch (funC) {
				case 'I':
					if (val_L != null && val_R != null) {
						wSw = true;
					}
					break;
				case 'L':
					if (val_L != null) {
						if (val_R == null)
							val_R = DUMMY_R;
						wSw = true;
					}
					break;
				case 'R':
					if (val_R != null) {
						if (val_L == null)
							val_L = DUMMY_L;
						wSw = true;
					}
					break;
				default: // Full Join
					if (val_L == null)
						val_L = DUMMY_L;
					if (val_R == null)
						val_R = DUMMY_R;
					wSw = true;
					break;
				}
				if (wSw) {
					buf.delete(0, buf.length());
					buf.append(key);
					buf.append(delimiter);
					if (magicIndex > 1) {
						// System.out.println("magicChar:"+magicChar);
						// System.out.println("val_L:"+val_L);
						// System.out.println("val_R:"+val_R);
						val_L = val_L.replaceFirst(magicChar, val_R);
						// System.out.println("aft:"+val_L);
						buf.append(val_L);
					} else {
						if (write_L) {
							buf.append(val_L);
							if (write_R)
								buf.append(delimiter);
						}
						if (write_R)
							buf.append(val_R);
					}
					// buf.append(LF);
					// writer.write(buf.toString());
					outClosure.write(buf.toString());
					wCnt++;
				}
			}
			outClosure.close();

		} catch (Exception e) {
			System.out.println("#ERROR @Flt_Mixer Exception");
			e.printStackTrace();
			abend();
		}
		super.stop();// ����I��

	}

	// -------------------------------------------------------------------------
	// incore
	// -------------------------------------------------------------------------
	private long incore(char whichSide, String path, String delim, int maxCol) {
		long counter = -1;
		// System.out.println("<<Flt_Mixer@incore>>");
		// System.out.println("path:" + path + " delim:" + delim + " max:" +
		// maxCol);
		File file_L = new File(path);
		if (!file_L.isFile()) {
			System.out.println("#error @Flt_Mixer not a File=>" + path);
			return counter;
		}
		if (!file_L.canRead()) {
			System.out.println("#error @Flt_Mixer File can not read =>" + path);
			return counter;
		}
		String key = "";
		String rec = "";
		StringBuffer buf = new StringBuffer();
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(path));
			while ((rec = br.readLine()) != null) {
				counter++;
				buf.delete(0, buf.length());
				String[] splited = rec.split(delim);
				if (splited != null && splited.length > 0) {
					key = splited[0]; // XXX Key�ʒu�̓[���ł悢�̂��H�I�v�V������ǉ����邩�H�H
					for (int i = 1; i < maxCol; i++) {
						if (i > 1)
							buf.append(delimiter); // TODO ���m�F�E�E�Ei>1�ŗǂ��̂��H
						if (magicIndex > 1 && whichSide == 'L'
								&& (magicIndex - 1) == i) {
							buf.append(magicChar);
						} else {
							if (i < splited.length) {
								buf.append(splited[i]);
							}
						}
					}
					ElementsLR element = (ElementsLR) mixerMap.get(key);
					if (element == null)
						element = new ElementsLR();
					if (whichSide == 'L') {
						element.setLeft(buf.toString());
					} else {
						element.setRight(buf.toString());
					}
					mixerMap.put(key, element);
				}
			}
			br.close();
		} catch (IOException ie) {
			ie.printStackTrace();
			abend();
		} catch (Exception e) {
			e.printStackTrace();
			abend();
		}
		return counter;
	}

	// -------------------------------------------------------------------------
	// �g�p�၄
	// new Filters().Flt_Mixer(JoinType,"mix.txt","\t","Left.txt","Right.txt");
	// <<JoinType>>
	// Left �����̃t�@�C�������i�����ɂ������݂��Ȃ��ꍇ�_�~�[�Z������������j
	// Right �E���̃t�@�C�������i�E���ɂ������݂��Ȃ��ꍇ�_�~�[�Z������������j
	// Inner ���E�̗����ɑ��݂���L�[�����i�_�~�[�Z���͔������Ȃ��j
	// Full ���E�̂ǂ��炩�ɂɑ��݂���L�[�����i�ǂ��炩�ɑ��݂��Ȃ��ꍇ�_�~�[�Z������������j
	// -------------------------------------------------------------------------
	// -------------------------------------------------------------------------
	// ���厖�I�I�i���C�t�X�^�C���N���X�^�̃}�[�W�e�X�g�j
	// -------------------------------------------------------------------------
	public static void testLFC() {
		String path_L = ResControlWeb.getD_Resources_Templates("cluster2007.txt");
		String path_R = ResControlWeb.getD_Resources_Templates("cluster2008.txt");
		String outPath = ResControlWeb.getD_Resources_Templates("LFMIX.csv");
		Flt_Mixer mixer = null;
		mixer = new Flt_Mixer(Flt_Mixer.FULL, outPath, path_L, path_R);
		mixer.setDelimiter(",");
		// mixer.setMagicIndex(10); //2�ȏ���w�肷��(�Z����1���犨�肷��)
		mixer.execute();
	}

	public static void test2() {
		// System.out.println("�o�b�`�N������ꍇ�̗�");
 
		String path1 = ResControl.getQPRHomeQprRes("temp02.txt");
		String path2 = ResControl.getQPRHomeQprRes("nqmon.txt");
		String sys300 = ResControl.getQPRHomeQprRes("ASM.txt");
		Flt_Mixer mixer = null;
		mixer = new Flt_Mixer(Flt_Mixer.RIGHT, sys300, path1, path2);
		mixer.setWrite_L(true);
		mixer.setWrite_R(false);
		mixer.execute();
	}

	public static void suzuki20121112() {
		String path_L = "c:/suzuki2012_1112_A.txt";
		String path_R = "c:/suzuki2012_1112_B.txt";
		String outPath = "c:/Pprops2.dat";
		Flt_Mixer mixer = null;
		mixer = new Flt_Mixer(Flt_Mixer.FULL, outPath, path_L, path_R);
		mixer.setDelimiter("\t");
		// mixer.setMagicIndex(10); //2�ȏ���w�肷��(�Z����1���犨�肷��)
		mixer.execute();
	}

	// ------------------------------------------------------------------------
	// �����̃e�X�g�E�E�E�Б�����Ƃ����������ꍇ�ǂ����邩���E�E�E
	// ����Əo�͂�writer�Ƃ��ďo�͎��Ƀt�B���^�[���H�ł���Γs��������
	// ------------------------------------------------------------------------
	public static void test20140618() {
		String path_L = "C:/@qpr/home/123620000036/calc/loyHead_H.txt";
		String path_R = "C:/@qpr/home/123620000036/calc/loyHead_M.txt";
		String outPath = "C:/@qpr/home/123620000036/calc/loyHead_MIX.txt";
		EzWriter writer = new EzWriter(outPath, new ReorderIt(
				"0,1,2,3,4,5,6,7,12,13,14,15,16,17,18,8,9,20"));
		Flt_Mixer mixer = new Flt_Mixer(Flt_Mixer.FULL, writer, path_L, path_R);
		mixer.setDelimiter("\t");
		// mixer.setMagicIndex(10); //2�ȏ���w�肷��(�Z����1���犨�肷��)
		mixer.execute();
	}

	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] args) {
		// testLFC();
		// kyPkg.filter.Flt_Mixer.suzuki20121112();
		test20140618();
	}
}
