package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.util.HashMap;
import java.util.List;

import kyPkg.task.Abs_BaseTask;
import kyPkg.uFile.ListArrayUtil;

// 2016-06-02 yuasa   
public class Flt_FormatCnv extends Abs_BaseTask {
	private static final String DEFAULT = "default\t";
	private static final String O_LAYOUT = "oLayout\t";
	private static final String I_LAYOUT = "iLayout\t";
	private static final String P_DELIMITER = "pDelimiter\t"; //�p�����[�^���̋�؂蕶��
	// ------------------------------------------------------------------------
	// ���̓N���[�W��
	// ------------------------------------------------------------------------
	private Inf_iClosure reader = null;
	private Inf_oClosure writer = null;
	private String delimiter = null;
	private static HashMap<String, Integer> inColMap;
	private static String[] iLayouts;
	private static String[] oLayout;
	private static String[] oDefault;
	private static String[] wRecs;

	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	public Flt_FormatCnv(String oPath, String iPath, String pPath) {
		reader = new EzReader(iPath);
		writer = new EzWriter(oPath);
		init(pPath);

	}

	//#########################################################################
	// init
	//#########################################################################
	//ex"item,maker,category,JanName(kanji),kanaJanName(kana),kanaJanName(ryaku)"; 
	private static void init(String pPath) {
		String pDelimiter = ",";
		List<String> paramlist = ListArrayUtil.file2List(pPath);
		for (String element : paramlist) {
			if (element.startsWith("#")) {
			} else if (element.startsWith(P_DELIMITER)) {//�p�����[�^�����ŋ�؂��Ă��邩
				element = element.substring(P_DELIMITER.length());
				pDelimiter = element;
			} else if (element.startsWith(I_LAYOUT)) {//���̓t�B�[���h�̕���
				element = element.substring(I_LAYOUT.length());
				iLayouts = element.split(pDelimiter, -1);
			} else if (element.startsWith(O_LAYOUT)) {//�o�̓t�B�[���h�̕���
				element = element.substring(O_LAYOUT.length());
				oLayout = element.split(pDelimiter, -1);
			} else if (element.startsWith(DEFAULT)) {//�f�t�H���g�l
				element = element.substring(DEFAULT.length());
				oDefault = element.split(pDelimiter, -1);
			}
		}
		//�K�{�p�����[�^�̎w��̗L�����m�F����
		if (oLayout == null) {
			System.out.println("�o�̓p�����[�^���w�肳��Ă��Ȃ��׏����𒆒f���܂�");
			System.exit(999);
		}
		wRecs = new String[oLayout.length];
		parseHead(iLayouts);

	}

	// ------------------------------------------------------------------------
	// ���s
	// ------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("<<START>>");
		long wCnt = 0;
		reader.open();
		writer.open();
		if (delimiter == null)
			delimiter = reader.getDelimiter();
		String[] splited = reader.readSplited();
		while (splited != null) {
			if (wCnt == 0) {

			} else {

				//�������s����iLayout����Ȃ��s�ڂ��g���E�E�E���̎��̋�؂蕶���]�X����肾�낤�@�p�����[�^���œ��ꂳ����΂����񂶂�Ȃ����H�H
				//#############################################################
				// loop
				//#############################################################
				for (int i = 0; i < oLayout.length; i++) {
					Integer col = inColMap.get(oLayout[i]);
					if (col != null && splited.length > col) {
						wRecs[i] = splited[col];
					} else {
						wRecs[i] = oDefault[i];
					}
				}
				//				String delimiter = "��";
				//				System.out.println(">>" + join(wRecs, delimiter));
				writer.write(join(wRecs, delimiter));
			}

			wCnt++;
			splited = reader.readSplited();
		}
		reader.close();
		writer.close();
	}

	//���̓��R�[�h�̃t�B�[���h�̕��т��}�b�v�Ɋi�[����
	private static void parseHead(String[] aHeads) {
		inColMap = new HashMap();
		for (int i = 0; i < aHeads.length; i++) {
			inColMap.put(aHeads[i], new Integer(i));
		}
	}

	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] args) {
		test1();
	}

	public static void test1() {
		String wkDir = "C:/samples/�����e�X�g/";
		String inPath = wkDir + "SYOMSDT_1605.TXT";
		String metaPath = wkDir + "meta.txt";
		String formatedPath = wkDir + "formated.txt";
		//---------------------------------------------------------------------
		//�@�\�[�g�@�@(0�J�����ڂ�������)
		//---------------------------------------------------------------------
		new kyPkg.Sorts.IncoreSort(inPath).execute();
		//---------------------------------------------------------------------
		//�@�t�H�[�}�b�g�ϊ�
		//---------------------------------------------------------------------
		new kyPkg.filter.Flt_FormatCnv(formatedPath, inPath, metaPath).execute();
	}
}
