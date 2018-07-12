package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kyPkg.task.Abs_BaseTask;
import kyPkg.uFile.FileUtil;
import kyPkg.uFile.ListArrayUtil;

// 2016-05-20 yuasa
public class SV_Converter extends Abs_BaseTask {
	// ------------------------------------------------------------------------
	// ���̓N���[�W��
	// ------------------------------------------------------------------------
	private Inf_iClosure reader = null;
	private Inf_oClosure writer = null;
	private String iDelimiter = null;
	private String oDelimiter = "\t";

	private String[] iArray;
	private String[] oArray;
	//	private int[] seqControl = { 1, -1, 3, -1, 5, -1, 7, -1, 9, -1 };///���l�Ń_�~�[
	private List<Integer> seqControl = null;

	//	private Map<Integer, Inf_LineConverter> pmap;
	public String parentDir;
	private HashMap<Integer, String> commentMap;// �R�����g
//	private HashMap<Integer, String> paramMap;// �ϊ��t�B���^�p�����[�^

	//	Inf_LineConverter converter = new SubstrCnv(parmPath);
	//TODO�@�p�����[�^�ɂ��w��ɂ��āE�E�E�E
	//	�p�����[�^��ݒ�t�@�C���Ŏw�肷��
	//	���̓J�����@�^�u�@�t�B���^�[	�R�����g
	//	�Œ�l�𖄂ߍ���
	//	substr�p�̃p�����[�^���g������E�E�E�ώG�ɂȂ�Ȃ��悤�ꕔ�̃p�����[�^���ȗ�������

	//	file2array��������file2list

	//	private int[] genParameter(String[] rec) {
	//		int[] array = new int[rec.length];
	//		for (int i = 0; i < rec.length; i++) {
	//			array[i] = i;
	//		}
	//		return array;
	//	}

	// -------------------------------------------------------------------------
	// �p�����[�^�����݂��Ȃ���Ύ�����������
	// -------------------------------------------------------------------------
	private List<Integer> genParameter(String[] rec) {
		List<Integer> list = new ArrayList();
		for (int i = 0; i < rec.length; i++) {
			list.add(new Integer(i));
		}
		return list;
	}

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public SV_Converter(String outPath, String inPath) {
		parentDir = FileUtil.getParent(inPath);
		reader = new EzReader(inPath);
		writer = new EzWriter(outPath);
	}

	private static int getInt(String val, int defaultVal) {
		try {
			return Integer.valueOf(val.trim());
		} catch (Exception e) {
		}
		return defaultVal;
	}

	public void readParam(String path) {
		int dstCol = -1;
		seqControl = new ArrayList();
		commentMap = new HashMap();
//		paramMap = new HashMap();
		String[] array = ListArrayUtil.file2Array(path);
		for (String element : array) {
			String[] splited = element.split("\t", -1);// ��col���ϊ��t�B���^�p�����[�^���R�����g
			if (splited.length > 0) {
				seqControl.add(new Integer(getInt(splited[0], -1)));
				dstCol++;
			}
			if (splited.length > 1) {
				commentMap.put(dstCol, splited[1]);// �ϊ��t�B���^�p�����[�^
			}
			if (splited.length > 2) {
				commentMap.put(dstCol, splited[2]);// �R�����g
			}
		}
	}

	// -------------------------------------------------------------------------
	// ���s
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("<<START>>");
		long wCnt = 0;
		reader.open();
		writer.open();
		if (iDelimiter == null)
			iDelimiter = reader.getDelimiter();
		iArray = reader.readSplited();
		// --------------------------------------------------------------------
		//�p�����[�^�����݂��Ȃ��ꍇ�͎�����������
		// --------------------------------------------------------------------
		if (seqControl == null) {
			seqControl = genParameter(iArray);
			//���̓f�[�^�Ɠ����t�H���_�Ƀp�����[�^�t�@�C�����o��
			//	ListArrayUtil.array2File(parentDir + "/sv_param.txt", seqControl);
			//	ListArrayUtil.list2file(parentDir + "/sv_param.txt", seqControl);
		}
		int oOcc = seqControl.size();
		while (iArray != null) {
			oArray = new String[oOcc];
			for (int i = 0; i < seqControl.size(); i++) {
				int seq = seqControl.get(i);
				//if (seq >= 0 && seq < iArray.length && i < oArray.length) {
				if (seq >= 0 && seq < iArray.length) {
					oArray[i] = iArray[seq];
				} else {
					oArray[i] = "";//�_�~�[���o��
				}
			}
			writer.write(join(oArray, oDelimiter));
			wCnt++;
			iArray = reader.readSplited();
		}
		reader.close();
		writer.close();
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] argv) {
		test1();
//		testgetInt();
		
	}

	public static void test1() {
		String inPath = "T:/QPR/dat/JICFS_JAN.txt";
		String outPath = "T:/QPR/dat/JICFS_JAN_CNV.txt";
		SV_Converter ins = new SV_Converter(outPath, inPath);
		String paramPath = "T:/QPR/dat/sv_param.txt";
		ins.readParam(paramPath);
		ins.execute();
	}

	public static void testgetInt() {
		System.out.println(">" + getInt("", 999));
		System.out.println("-1>" + getInt("-1", 999));
		System.out.println("0>" + getInt("0", 999));
		System.out.println("1>" + getInt("1", 999));
		System.out.println("2>" + getInt("2", 999));
		System.out.println("3>" + getInt("3", 999));
		System.out.println("4>" + getInt("4", 999));
		System.out.println("5>" + getInt("5", 999));
		System.out.println("6>" + getInt("6", 999));
		System.out.println("7>" + getInt("7", 999));
		System.out.println("8>" + getInt("8", 999));
		System.out.println("9>" + getInt("9", 999));
		System.out.println(" >" + getInt(" ", 999));
		System.out.println(" 1>" + getInt(" 1", 999));
		System.out.println("a>" + getInt("a", 999));
		System.out.println("b>" + getInt("b", 999));
		System.out.println("c>" + getInt("c", 999));
	}

}
