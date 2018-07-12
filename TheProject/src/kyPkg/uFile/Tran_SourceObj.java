package kyPkg.uFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import globals.ResControl;
import kyPkg.uDateTime.DateCalc;

//QTB�t�@�C�����x�[�X�Ƀt�B�[���h��`�A�l�˃��x���ϊ������̐������s��
public class Tran_SourceObj extends SourceObj {
	private String defaultSourceDir;
	// ------------------------------------------------------------------------
	// XXX�@sourceDir�����ۂɑ��݂��邩�A������qtb�Ƃ�alias�����݂��邩�ǂ����m�F����
	// ------------------------------------------------------------------------
	// FIXME WHOLE = "�S��";
	//	public static final String WHOLE = "�S��";
	// public static final int WHOLE_COL = -1; // �S�́i���R�ƑS�̂��w���̂Ɏg�p�j
	// ------------------------------------------------------------------------
	private static final String SOURCE_DIR = "sourceDir";
	private static final String SELECTED_FIELDS = "selectedFields";
	private static final String PREVIOUS_SETTING_PATH = ResControl
			.getQPRHomeQprRes("defaultFields.txt");
	// ------------------------------------------------------------------------
	public static final String WEEK = "�j��";
	public static final String TIME30 = "30������";
	private static final String FLAG2 = "Flag2";
	private static final String FLAG1 = "Flag1";
	private static final String FLAG3 = "Flag3";
	// ------------------------------------------------------------------------
	public static final String MONITORID = "���j�^�[�h�c";
	public static final String MBEF = "�J�n";
	public static final String MAFT = "�I��";
	public static final String TRAN_YMD = "�w�����t";
	public static final String TRAN_HOUR = "�w������";
	public static final String TRAN_PRICE = "�w�����z";
	public static final String TRAN_PRICExCOUNT = "�w�����z�w����";
	public static final String TRAN_COUNT = "�w������";
	public static final String CHANNEL_L = "�w����i�Ƒԁj";
	public static final String CHANNEL_S = "�w����i�ڍׁj";
	// ------------------------------------------------------------------------
	public static final String JAN_CODE = "JanCode";
	public static final String JAN_CODEX = "JAN�R�[�h";
	public static final String JAN_NAME = "�A�C�e������";
	public static final String JAN_PRICE = "�}�X�^���i";
	public static final String JAN_KANA = "�J�i����";
	// ------------------------------------------------------------------------
	public static final String CATEGORY = "�i��";
	public static final String CATEGORY_NAME = "�i�ږ�";
	public static final String MAKER = "���[�J�[";
	public static final String MAKER_NAME = "���[�J�[��";
	public static final String SAL_YMD = "������";
	public static final String REG_YMD = "�o�^��";
	public static final String UPD_YMD = "�X�V��";
	// ------------------------------------------------------------------------
	public static final String SHAPE = "�e��`��";
	public static final String STD1 = "�K�i�i�P�ʁj";
	public static final String STD2 = "�K�i�i�e�ʁj";
	public static final String UNIT = "�e��";
	// ------------------------------------------------------------------------
	//V_ITEM_Dao�Ńt�B�[���h�Ƃ̑Ή��𕹂��Ă���
	public static final String[] JAN_NAME_CODE_KANA_MAKER_CATEGORY = new String[] { JAN_NAME,
			JAN_CODEX, JAN_KANA ,MAKER,CATEGORY};
//	public static final String[] JAN_NAME_CODE_KANA = new String[] { JAN_NAME,
//			JAN_CODEX, JAN_KANA };

	private static String[] pattern2 = new String[] { MONITORID,
			TRAN_PRICExCOUNT, TRAN_PRICE, TRAN_COUNT, JAN_CODE, CHANNEL_S,
			TRAN_YMD, FLAG3, FLAG1, FLAG2, CHANNEL_L, "YM", TRAN_HOUR, TIME30,
			WEEK };

	public static String[] getHeadPattern2() {
		return pattern2;
	}

	public void setBrowseMode() {
		super.setSelectedFields(pattern2);
	}



	// ------------------------------------------------------------------------
	// �\�����ɂ��t�B�[���h�ꗗ�i��������̂��߃��X�g�ƂȂ��Ă���j
	// ------------------------------------------------------------------------
	// FIXME ������ւ�Ɂ@�y�S�́z�@�\��}���������E�E�E
	private void setDefaultFieldList() {
		ArrayList list = new ArrayList();
		list.add(MONITORID);// 0
		list.add(TRAN_YMD);// 1
		list.add(TRAN_HOUR);// 2
		list.add(JAN_CODE);// 3
		list.add(TRAN_PRICExCOUNT);// 4
		list.add(TRAN_PRICE);// 4
		list.add(TRAN_COUNT);// 5
		list.add(CHANNEL_L);// 6
		list.add(CHANNEL_S);// 6
		list.add(JAN_NAME);// 7
		list.add(JAN_PRICE);// 8
		list.add(CATEGORY);// 9
		list.add(CATEGORY_NAME);
		super.setFieldList(list);
	}

	public List<String> getDefaultList() {
		List list = new ArrayList();
		list.add(MONITORID);
		list.add(TRAN_YMD);
		list.add(TRAN_HOUR);
		list.add(JAN_CODE);
		list.add(TRAN_PRICExCOUNT);
		list.add(TRAN_PRICE);
		list.add(TRAN_COUNT);
		// list.add(CHANNEL_L);
		list.add(CHANNEL_S);
		list.add(JAN_NAME);
		list.add(JAN_PRICE);
		list.add(CATEGORY);
		return list;
	}

	// ------------------------------------------------------------------------
	// �\�����ƃf�[�^�x�[�X���ł̃t�B�[���h���ƃf�[�^�x�[�X�̎��ʃt�B�[���h�̑Ή��\
	// ------------------------------------------------------------------------
	private void setDefaultFieldMap() {
		HashMap hmap = new HashMap();
		hmap.put(MONITORID, "TRN.Monitor");
		hmap.put(TRAN_YMD, "TRN.Accept");
		hmap.put(TRAN_HOUR, "TRN.hh as Hour");
		hmap.put(JAN_CODE, "'#'+TRN.JanCode");
		hmap.put(TRAN_PRICExCOUNT, "TRN.Price * TRN.Count");
		hmap.put(TRAN_PRICE, "TRN.Price");
		hmap.put(TRAN_COUNT, "TRN.Count");
		hmap.put(CHANNEL_L, "TRN.Shop2 as shop2");
		hmap.put(CHANNEL_S, "TRN.Shop1 as shop1");
		hmap.put(JAN_NAME, "ITM.NAME");
		hmap.put(JAN_PRICE, "ITM.PRICE");
		hmap.put(CATEGORY, "ITM.CAT");
		hmap.put(CATEGORY_NAME, "CATEGORY.XB1");
		super.setFieldMap(hmap);
	}

	// ------------------------------------------------------------------------
	// �\�����Ƃ��̗�̒l�ɑ΂���ϊ������̑Ή�
	// ------------------------------------------------------------------------
	private void setDefaultDictionary() {
		// ------------------------------------------------------------------------
		// System.out.println("#<<Base_Source.setDictionary()>>#");
		// resDir=>./qpr/res/
		// �������p�^�O�F�w���於�́A�`���l������
		// ------------------------------------------------------------------------
		String resDir = globals.ResControl.getQPRHomeQprRes("");
		HashMap<String, String> channelDic2 = HashMapUtil.file2HashMap(resDir + "channel.dic",
				1);
		HashMap dict = new HashMap();
		dict.put(CHANNEL_L, channelDic2);
		dict.put(CHANNEL_S, channelDic2);
		dict.put(WEEK, HashMapUtil.file2HashMap(resDir + WEEK + ".txt", 1));
		dict.put(TIME30, HashMapUtil.file2HashMap(resDir + "30������.txt", 1));
		super.setDictionary(dict);
	}

	// ------------------------------------------------------------------------
	// �����t�B�[���h�̈ꗗ�����[�U�[�ݒ�t�@�C������ǂݍ���
	// DEFAULT_FIELDS�F"c:/@qpr/qpr/res/defaultFields.txt"
	// ------------------------------------------------------------------------
	private void previousSetting(String path) {
		System.out.println("#################################################");
		System.out.println("## previousSetting:" + path);
		System.out.println("#################################################");
		HashMapObj hmapObj = new HashMapObj(path);
		//---------------------------------------------------------------------
		//�@PREVIOUS_SETTING_PATH => C:/@qpr/home/qpr/res/defaultFields.txt    �̓��e�̗ၫ
		//	selectedFields	���j�^�[�h�c,�w�����t,�w������,JanCode,�w�����z�w����,�w�����z,�w������,�w����i�Ƒԁj,�w����i�ڍׁj,�A�C�e������,�}�X�^���i,�i��,�i�ږ�,���j�^�[���,���ю�,���ѓ����c��w�N��,���ю�N��,���ѓ����c�Ƒ��l��,�q���i�P�W�ˈȉ��j�̓����L��,���c���̓����L��,���w���i��w�N�j�̓����L��,���w���i���w�N�̓����L��,���w���̓����L��,���Z���̓����L��,�V�l�̓����L��,�Z���`��,�l�N��,���єN��,�N�x�N��,����,������,���ѓ����c���g�̎q���̗L��,�E��,�w�����,���E�N��i�P�O�΋敪�j,���E�N��i�T�΋敪�j,���E�N��i���f�B�A�敪�j,�N��i�P�O�΋敪�j,�N��i�T�΋敪�j,�N��i���f�B�A�敪�j,�Z���i�s���{���j,�Z���i�G���A�j,�E�ƃ^�C�v
		//	sourceDir	Z:/S2/rx/enquetes/NQ//03_�����E���N���/2016/
		//---------------------------------------------------------------------
		// �ݒ�t�@�C����SOURCE_DIR��null�Ȃ�f�t�H���g�\�[�X�̃p�X��SOURCE_DIR�Ƃ���
		//---------------------------------------------------------------------
		setSourceDir(hmapObj.getByString(SOURCE_DIR, defaultSourceDir));
		//---------------------------------------------------------------------
		// �ݒ�t�@�C����SELECTED_FIELDS��null�Ȃ�f�t�H���g�t�B�[���h��ݒ�
		//---------------------------------------------------------------------
		setSelectedFields(hmapObj.getByList(SELECTED_FIELDS, getDefaultList()));
		System.out.println("##<previousSettings end>##");
	}

	public void saveSettings(List<String> fields, String sourceDir) {
		System.out.println("#################################################");
		System.out.println("## saveSettings");
		System.out.println("#################################################");
		HashMapObj hmapObj = new HashMapObj();
		hmapObj.put(SOURCE_DIR, sourceDir);
		// list��null�Ȃ�f�t�H���g�̃��X�g��ݒ肷��
		if (fields == null)
			fields = getDefaultList();
		hmapObj.set(SELECTED_FIELDS, fields);
		// hashMap���t�@�C���ɕۑ��H
		hmapObj.save(PREVIOUS_SETTING_PATH);
	}

	// ------------------------------------------------------------------------
	// �R���X�g���N�^(���񂨂�ёO��̑�����)
	// ------------------------------------------------------------------------
	public Tran_SourceObj() {
		System.out.println("<< Base_Source �R���X�g���N�^ >>");
		defaultSourceDir = ResControl.getQtbDir( DateCalc.getThisYear());//ex=> Z:/S2/rx/enquetes/NQ//03_�����E���N���/2016/
		previousSetting(PREVIOUS_SETTING_PATH);
		setDefault();
		incore(getSourceDir());// 20140922
	}

	public void setDefault() {
		System.out.println(" << setDefault() >>");
		setDefaultFieldList();
		setDefaultFieldMap();
		setDefaultDictionary();
	}

	// XXX 20140922 Src_Buy�ŃR�[�����Ă���E�E�E�ǂ�ȏꍇ�ɕK�v�Ȃ̂����؂��Ă���
	public void loadPreSettings() {
		System.out.println("#debug20140922#<< loadPreSettings() >>#");
		defaultSourceDir = super.getSourceDir();
		previousSetting(PREVIOUS_SETTING_PATH);
	}

}
