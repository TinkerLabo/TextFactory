package kyPkg.filter;

import static kyPkg.uFile.FileUtil.getBufferedReader;
import static kyPkg.util.Faker.getDummy;
import static kyPkg.util.KUtil.getIntArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import globals.ResControl;
import kyPkg.task.Abs_ProgressTask;
import kyPkg.uFile.File49ers;
import kyPkg.util.KUtil;

//�@�}�X�^�[�i���j�΃g�����U�N�V�����t�@�C���i���j�̃}�b�`���O�����A�o�͂̓N���[�W�����w��
//�@	XXX ���ƂŁE�E�J�����w�肪�����ꍇ,�t���J���������Ƃ����d�l�ɂ��Ă���
//	XXX ���[�_�[���N���[�W���ɂł��Ȃ����H�H�p�t�H�[�}���X���ӂ݂Ȃ���C���I�I
// last update 2009-11-12 yuasa 
public class MultiMatch extends Abs_ProgressTask {
	private boolean Order = true;// "MT";
	private kyPkg.tools.Elapse elapse;
	private boolean sameSource = false;
	private char funC = 'I'; // Default = Inner Join
	private long writeCount = 0;
	private HashMap<String, ElementsLR_Plus> mixerMap;
	private Set<String> markerset;
	private HashMap<String, Set> setMap;
	private HashMap<String, Inf_BaseClojure> outClosureMap;
	private Inf_BaseClojure defaultClosure;
	private String pathM = "";// Master
	private String pathT = "";// Tran
	private int keyM = 0; // Master���̃L�[�J����
	private int keyT = 0; // Tran���̃L�[�J����
	private int[] dimM;
	private int[] dimT;
	private String dmyM = "DummyM";
	private String dmyT = "DummyT";
	private int maxcolM;
	private int maxcolT;
	private String delimiter = null;
	private String delimM = "\t";
	private String delimT = "\t";
	private boolean elapseCheck = false;

	public boolean isElapseCheck() {
		return elapseCheck;
	}

	public void setElapseCheck(boolean elapseCheck) {
		this.elapseCheck = elapseCheck;
	}

	public int getMaxcolL() {
		return maxcolM;
	}

	public int getMaxcolR() {
		return maxcolT;
	}

	public void setKeyL(int keyL) {
		this.keyM = keyL;
	}

	public void setKeyR(int keyR) {
		this.keyT = keyR;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter; // �o�̓t�@�C���̃f���~�^
		this.delimM = delimiter;// ���̓t�@�C���̃f���~�^
		this.delimT = delimiter;// ���̓t�@�C���̃f���~�^
	}

	// -------------------------------------------------------------------------
	// �����N���X
	// -------------------------------------------------------------------------
	class ElementsLR_Plus {
		// �}�X�^�[���̍��ڂ������X�g�ƂȂ��Ă���i�����L�[���ƃX�^�b�N�����j
		private ArrayList<String> list = null;
		private boolean done = false;

		public boolean isDone() {
			return done;
		}

		public void setDone() {
			this.done = true;
		}

		public ElementsLR_Plus() {
			list = new ArrayList();
			done = false;
		}

		public ArrayList getList() {
			return this.list;
		}

		public void setList(String left) {
			this.list.clear();
			this.list.add(left);
		}

		public void addList(String left) {
			this.list.add(left);
		}
	}

	// �����L�[�̒l�����݂�����X�^�b�N����
	private ElementsLR_Plus modElementVal(String key, String value) {
		ElementsLR_Plus element = (ElementsLR_Plus) mixerMap.get(key);
		if (element == null) {// NotFound
			element = new ElementsLR_Plus();
		}
		element.addList(value); // �����L�[�ł���Βl�̓X�^�b�N����Ă���
		mixerMap.put(key, element);
		return element;
	}

	// �����L�[�̒l�����݂�����㏑������i�X�^�b�N���Ȃ��j
	private ElementsLR_Plus setElementVal(String key, String value) {
		ElementsLR_Plus element = (ElementsLR_Plus) mixerMap.get(key);
		if (element == null) {// NotFound
			element = new ElementsLR_Plus();
		}
		element.setList(value); // �X�^�b�N���Ȃ��I
		mixerMap.put(key, element);
		return element;
	}

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	// �}�b�`���O�������ʂ��}�X�^�[�Ƀ��b�h����ꍇ�p
	public MultiMatch(String pathM, String func, String pathT,
			String delimiter) {
		this(pathM, func, pathM, (int[]) null, pathT, (int[]) null, true);
		this.setDelimiter(delimiter);
	}

	public MultiMatch(String outPath, String func, String pathM, String pathT,
			boolean order) {
		this(outPath, func, pathM, (int[]) null, pathT, (int[]) null, order);
	}

	public MultiMatch(String outPath, String func, String pathM, String dimM,
			String pathT, String dimT, boolean order) {
		this(outPath, func, pathM, KUtil.str2intArray(dimM), pathT,
				KUtil.str2intArray(dimT), order);
	}

	public MultiMatch(String outPath, String func, String pathM, int[] dimM,
			String pathT, int[] dimT, boolean order) {
		this(func, pathM, dimM, pathT, dimT, order);
		this.setOutClosure(outPath);
	}

	public MultiMatch(String func, String pathM, int[] dimM, String pathT,
			int[] dimT, boolean Order) {
		super();
		this.markerset = null;
		this.setMap = null;
		this.outClosureMap = null;
		this.mixerMap = new HashMap();
		this.funC = (func.toUpperCase()).charAt(0); // L,R,I,F�̂����ꂩ
		this.pathM = pathM;
		this.pathT = pathT;
		this.dimM = dimM;
		this.dimT = dimT;
		this.Order = Order;// True:MT false:TM
	}

	// -------------------------------------------------------------------------
	// ID(key)�ɂ��U�蕪���������s���ׂ̎��ʃ}�[�J�[�̐ݒ�i�h���ɂ��l�K�|�W���A��ޕ��������j
	// id�ɂ��邵�����āA����ɑΉ������N���[�W�������s�����邱�Ƃ��ł���
	// �������}�b�s���O���Ă��Ȃ��ꍇ�̓X�y�[�X���}�[�J�[�Ɋ��蓖�Ă�
	// -------------------------------------------------------------------------
	public void setOutClosure(String outPath) {
		this.setOutClosure("", null, new EzWriter(outPath));
	}

	public void setOutClosure(String marker, HashSet set,
			Inf_BaseClojure closure) {
		if (marker.equals("")) {
			defaultClosure = closure;
		} else {
			if (this.markerset == null)
				this.markerset = new HashSet();
			if (this.setMap == null)
				this.setMap = new HashMap();
			if (this.outClosureMap == null)
				this.outClosureMap = new HashMap();
			this.markerset.add(marker);
			this.setMap.put(marker, set);
			this.outClosureMap.put(marker, closure);
		}
	}

	@Override
	public void execute() {
		super.start("MultiMatch", 2048);
		if (elapseCheck) {
			elapse = new kyPkg.tools.Elapse("MultiMatch");
			elapse.start();
		}
		closureInit();// �o�̓N���[�W��open����
		dmyM = "";
		dmyT = "";
		if (pathM.equals(pathT)) {
			// �����\�[�X�̏ꍇ�A��ʂ̃f�[�^��z�肷��ƁE�E�C���R�A�����Ă����Ȃ�
			sameSource = true;
			File49ers f49R = new File49ers(pathT);// TRAN
			// System.out.println("�@master/tran => sameSource");
			// if (delimT == null)
			delimT = f49R.getDelimiter();
			maxcolT = f49R.getMaxColCount();
			maxcolM = maxcolT;
			if (delimiter == null) {
				delimiter = delimT;
			}
		} else {
			sameSource = false;
			File49ers f49M = new File49ers(pathM);// Master
			File49ers f49T = new File49ers(pathT);// TRAN

			// System.out.println("�@pathM =>" + pathM);
			// System.out.println("�@pathT =>" + pathT);

			// if (delimM == null)
			delimM = f49M.getDelimiter();
			// if (delimT == null)
			delimT = f49T.getDelimiter();

			// System.out.println("�@delimM => " + delimM);
			// System.out.println("�@delimT => " + delimT);

			maxcolM = f49M.getMaxColCount();
			maxcolT = f49T.getMaxColCount();
			// ���̓f�[�^�̋�؂肪�����Ȃ炻����g�p����A����Ă�����^�u���f�t�H���g�Ƃ���
			if (delimiter == null) {
				if (delimM.equals(delimT)) {
					delimiter = delimT;
				} else {
					delimiter = "\t";
				}
			}
			// �_�~�[�Z������у_�~�[�p�����[�^�i�S�J�����o�́j�����
			if (dimM == null) {
				dimM = getIntArray(1, maxcolM);
				dmyM = getDummy(maxcolM - 1, delimiter);
			} else {
				dmyM = getDummy(dimM.length, delimiter);
			}
			if (dimT == null) {
				dimT = getIntArray(1, maxcolT);
				dmyT = getDummy(maxcolT - 1, delimiter);
			} else {
				dmyT = getDummy(dimT.length, delimiter);
			}
			// --------------------------------------------------------------------
			// �}�X�^�[INCORE
			// --------------------------------------------------------------------
			masterIncore(pathM, delimM, keyM, dimM);
		}
		writeCount = 1;
		// -----------------------------------------------------------------
		// tranLoop
		// -----------------------------------------------------------------
		tranLoop(pathT, delimT, keyT, dimT, dimM);
		// -----------------------------------------------------------------
		// masterOnly
		// -----------------------------------------------------------------
		long cnt_M = masterOnly();
		// System.out.println("# masterOnly Count:" + cnt_M);
		stop();// ����I��
		closureFin();
		if (elapseCheck)
			elapse.stop();

		super.stop();// ����I��
	}

	// -------------------------------------------------------------------------
	// incore
	// -------------------------------------------------------------------------
	private void masterIncore(String path, String delim, int keyCol,
			int[] dim) {
		long counter = 0;
		String key = "";
		String rec = "";
		StringBuffer buf = new StringBuffer();
		try {
			// System.out.println("@masterIncore	master==>>" + path);
			BufferedReader reader = getBufferedReader(path);
			while ((rec = reader.readLine()) != null) {
				counter++;
				buf.delete(0, buf.length());
				String[] splited = rec.split(delim);
				if (splited != null && splited.length > 0) {
					key = splited[keyCol]; // XXX Key�ʒu�̓[���ł悢�̂��H�I�v�V������ǉ����邩�H�H
					// System.out.println("key==>>" + key);

					for (int i = 0; i < dim.length; i++) {
						if (i >= 1)
							buf.append(delimiter);
						int pos = dim[i];
						if (pos < splited.length) {
							buf.append(splited[pos]);
						}
					}
					// if (key.equals("73225580")) {
					// System.out.println("==>>" + buf.toString());
					// }
					// �����L�[�̃}�X�^�[�f�[�^�̓X�^�b�N�����
					modElementVal(key, buf.toString());
				}
			}
			reader.close();
			// System.out.println("masterIncoreCount:" + counter);
		} catch (IOException ie) {
			ie.printStackTrace();
			abend();
		} catch (Exception e) {
			e.printStackTrace();
			abend();
		}
	}

	// -------------------------------------------------------------------------
	// tranLoop(Tran Data)
	// -------------------------------------------------------------------------
	private void tranLoop(String path, String delim, int keyCol, int[] dimT,
			int[] dimM) {
		long counter = 0;
		// System.out.println("MultiMatch@tranLoop  path:" + path);
		String key = "";
		String rec = "";
		String val_M = null;
		String val_T = null;
		ElementsLR_Plus masterElement = null;
		StringBuffer buffT = new StringBuffer();
		StringBuffer buffM = new StringBuffer();
		try {
			BufferedReader reader = getBufferedReader(path);
			while ((rec = reader.readLine()) != null) {
				// System.out.println("debug>>>" + rec);
				counter++;
				String[] splited = rec.split(delim);
				if (splited != null && splited.length > 0) {
					key = splited[keyCol]; // XXX Key�ʒu�̓[���ł悢�̂��H�I�v�V������ǉ����邩�H�H
					buffT.delete(0, buffT.length());
					for (int i = 0; i < dimT.length; i++) {
						if (i >= 1)
							buffT.append(delimiter);
						int pos = dimT[i];
						if (pos < splited.length) {
							buffT.append(splited[pos]);
						}
					}
					// �����\�[�X�̏ꍇ�}�X�^�[�C���R�A�����Ȃ��̂ŁA�����ł����Ă���
					if (sameSource) {
						buffM.delete(0, buffM.length());
						for (int i = 0; i < dimM.length; i++) {
							if (i >= 1)
								buffM.append(delimiter);
							int pos = dimM[i];
							if (pos < splited.length) {
								buffM.append(splited[pos]);
							}
						}
						masterElement = setElementVal(key, buffM.toString());
					} else {
						masterElement = (ElementsLR_Plus) mixerMap.get(key);
					}
					if (masterElement != null) {
						val_M = null;
						val_T = buffT.toString();
						masterElement.setDone();// �������ݍς݂Ƃ����Ӗ�
						// �X�^�b�N����Ă���}�X�^�[�G�������g�����[�v�������s��
						ArrayList list = masterElement.getList();
						for (Iterator iter = list.iterator(); iter.hasNext();) {
							val_M = (String) iter.next();
							write(key, val_M, val_T);
						}
					} else {
						// Master Not Found(TranOnly!)
						val_M = null;
						write(key, val_M, val_T);
					}
				}
			}
			reader.close();
		} catch (IOException ie) {
			ie.printStackTrace();
			abend();
		} catch (Exception e) {
			e.printStackTrace();
			abend();
		}
		// System.out.println("MultiMatch@tranLoop  end:counter:" + counter);
	}

	// -------------------------------------------------------------------------
	// masterOnly
	// -------------------------------------------------------------------------
	private long masterOnly() {
		long counter = -1;
		String val_M = null;
		Set set = mixerMap.entrySet(); // ����iterator���ĂׂȂ��̂ň�USET���擾����
		Iterator it = set.iterator();
		while (it.hasNext()) {
			setCurrent((int) writeCount);
			java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
			String key = (String) ent.getKey();
			ElementsLR_Plus masterElement = (ElementsLR_Plus) ent.getValue();
			val_M = null;
			// �������ݍς݂ł͂Ȃ�������
			if (masterElement != null && !masterElement.isDone()) {
				ArrayList list = masterElement.getList();
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					val_M = (String) iter.next();
					write(key, val_M, null);
				}
			}
		}
		return counter;
	}

	// -------------------------------------------------------------------------
	// write
	// -------------------------------------------------------------------------
	public void write(String key, String val_M, String val_T) {
		StringBuffer buf = new StringBuffer();
		boolean wSw = false;
		switch (funC) {
		case 'I':
			if (val_M != null && val_T != null) {
				wSw = true;
			}
			break;
		case 'L':
			if (val_M != null) {
				if (val_T == null)
					val_T = dmyT;
				wSw = true;
			}
			break;
		case 'R':
			if (val_T != null) {
				if (val_M == null)
					val_M = dmyM;
				wSw = true;
			}
			break;
		default: // Full Join
			if (val_M == null)
				val_M = dmyM;
			if (val_T == null)
				val_T = dmyT;
			wSw = true;
			break;
		}
		if (wSw) {
			buf.delete(0, buf.length());
			buf.append(key);
			buf.append(delimiter);
			// -----------------------------------------------------------------
			if (Order) {// Master_Tran�̏��Őڍ�����ꍇ
				if (dimM != null) {
					buf.append(val_M);
					if (dimT != null)
						buf.append(delimiter);
				}
				if (dimT != null) {
					buf.append(val_T);
				}
			} else {
				if (dimT != null) {
					buf.append(val_T);
					if (dimM != null)
						buf.append(delimiter);
				}
				if (dimM != null) {
					buf.append(val_M);
				}

			}
			// -----------------------------------------------------------------

			// �ǂꂩ�ɓ��Ă͂܂�΂悢�ꍇ�̓u���[�N����Ό������ǂ��E�E�E
			// �ǂ�ɂ����Ă͂܂�Ȃ�������f�t�H���g�Ƃ�������ɂ͂Ȃ��Ă��Ȃ��E�E�E
			Inf_BaseClojure outClosure = null;
			if (this.markerset == null) {
				outClosure = defaultClosure;
				if (outClosure != null) {
					outClosure.execute(buf.toString());
					writeCount++;
				}
			} else {
				for (Iterator iter = markerset.iterator(); iter.hasNext();) {
					String marker = (String) iter.next();
					Set set = setMap.get(marker);
					if (set != null && set.contains(key)) {
						outClosure = outClosureMap.get(marker);
					}
					if (outClosure != null) {
						outClosure.execute(buf.toString());
						writeCount++;
					}
				}

			}
		}
	}

	// -------------------------------------------------------------------------
	// closure.init();
	// -------------------------------------------------------------------------
	private void closureInit() {
		if (defaultClosure != null) {
			defaultClosure.init();
		}
		if (this.outClosureMap != null) {
			Set set = outClosureMap.entrySet();
			Iterator it = set.iterator();
			while (it.hasNext()) {
				java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
				Inf_BaseClojure closure = (Inf_BaseClojure) ent.getValue();
				if (closure != null)
					closure.init();
			}
		}
	}

	// -------------------------------------------------------------------------
	// closure.fin();
	// -------------------------------------------------------------------------
	private void closureFin() {
		if (defaultClosure != null) {
			defaultClosure.write();
		}
		if (this.outClosureMap != null) {
			Set set = outClosureMap.entrySet();
			Iterator it = set.iterator();
			while (it.hasNext()) {
				java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
				Inf_BaseClojure closure = (Inf_BaseClojure) ent.getValue();
				if (closure != null)
					closure.write();
			}
		}
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
	public static void test1105() {
		// ���ꂼ��K�v�ȃJ�������w�肵���ꍇ
		String pathM = "c:/sample/masterX.txt";
		String pathT = "c:/sample/tranX.txt";
		String dest = "c:/sample/MultiMatchTest.txt";
		int[] dimM = { 1, 2 };
		int[] dimT = { 1, 2, 3, 4, 5, 6 };
		MultiMatch mixer = new MultiMatch(dest, "R", pathM, dimM, pathT, dimT,
				true);
		mixer.setElapseCheck(true);
		mixer.execute();
	}

	public static void testFull() {
		// full�W���C���̗�i�Ⴊ�ǂ��Ȃ����񂶁E�E�E�j
		String atomDir = ResControl.getAtomDir();
		String pathM = atomDir + "�ɓ����������͗p.txt";
		String pathT = atomDir + "�ɓ����������͗p.txt";
		String dest = "c:/sample/MultiMatchTest.txt";
		MultiMatch mixer = new MultiMatch(dest, "full", pathM, "9,1", pathT,
				"8", true);
		mixer.setElapseCheck(true);
		mixer.execute();
	}

	public static void test1020() {
		// �J�������w�肵�Ȃ��ꍇ�͑S���̃J������A������
		String pathM = "c:/sample/masterX.txt";
		String pathT = "c:/sample/tranX.txt";
		String dest = "c:/sample/MultiMatchTest.txt";
		MultiMatch mixer = new MultiMatch(dest, "R", pathM, pathT, true);
		mixer.setElapseCheck(true);
		mixer.execute();
	}

	// public static void test0826() {
	// // -----------------------------------------------------------------
	// // �f�[�^�������@�@���Ԃ��؂蕶���̎w�肪���������Ȃ��Ă��܂��Ă���̂��Ǝv���I
	// // -----------------------------------------------------------------
	// System.out.println("#�@�f�[�^�������@#");
	// String userDir = kyPkg.uFile.ResControl.getQPRHome();
	// String outPath = ResControl.getCurrentPath();
	// String atomDir = ResControl.getAtomDir();
	// String targetPath = atomDir + "�p�o�q�A���P�[�g/�����E���f�B�A��/2010/q36.txt";
	// String modPath = atomDir + "�p�o�q�A���P�[�g/�����E���f�B�A��/2009/q36.txt";
	// MultiMatch mixer = new MultiMatch(outPath, "full", targetPath, modPath,
	// true);
	// mixer.setDelimiter(",");
	// mixer.execute();
	//
	// }

	// public static void test110711() {
	// // ---------------------------------------------------------------------
	// // �A���P�[�g�Ƃ̘A��
	// // �A���P�[�g���ڂƃL�[�}�b�`���O�����āA�w���f�[�^�̂��K�ɁA�A���P�[�g�f�[�^�������������̂��o�͂���
	// // ---------------------------------------------------------------------
	// System.out.println("### MultiMatch ###");
	// String userDir = kyPkg.uFile.ResControl.getQPRHome();
	// String enqMix1 = userDir + "828111000507/calc/enqMix1.txt";
	// String enqPath = ResControl.getCurrentPath();
	// String mapRed1 = userDir + "828111000507/calc/MapReduce1.txt";
	// MultiMatch mixer = new MultiMatch(enqMix1, "R", enqPath, mapRed1, false);
	// mixer.execute();
	//
	// int relPosL = mixer.getMaxcolL();// �A���P�[�g�f�[�^�̃J������
	// int relPosR = mixer.getMaxcolR();// �w���f�[�^�̃J������
	// System.out.println("  maxcolL:" + relPosL);
	// System.out.println("  maxcolR:" + relPosR);
	//
	// }

	public static void main(String[] args) {
		// test1020();
		// testfull();
		// tester();
		// test0826();
		// test110711();
	}

	// public static void tester() {
	// String userDir = kyPkg.uFile.ResControl.getQPRHome();
	// String outPath = userDir + "MapReduce1.txt";
	// String func = "R";
	// String pathM = ResControl.getCurrentPath();
	// String pathT = userDir + "MapReduce0.txt";
	// new MultiMatch(outPath, func, pathM, pathT, true).execute();
	// }

	// �t�B���^���g�����e�X�g�������Ă����E�E�E���đ������w���f�[�^�ɂ������Ă݂邩�H�I
	// ���^�f�[�^����A���X�g�{�b�N�X�Ƀp�����[�^��f���o������
}
