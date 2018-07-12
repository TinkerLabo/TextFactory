package kyPkg.converter;

import static kyPkg.util.KUtil.range2Array;

import java.util.HashMap;

public class CnvMap implements Inf_StrConverter {
	private String otherName = "Other"; // ���̑��A��Y�����ڂɂ��閼�O
	private HashMap<String, String> map;
	private String[] tagNames;

	// ���̑��A��Y�����ڂɂ��閼�O
	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	// -------------------------------------------------------------------------
	// �v�Z�ɂ�镶�������W�ϊ��A
	// �O����n�܂���̂��P����n�܂�l�ɕϊ�
	// �i�^�O���A�I���W�i���̒l�A�ϊ���SEQ�j�̃}�b�v�ɕϊ�
	// -------------------------------------------------------------------------
	public static CnvMap shiftConverter(String[] tag, int nShift) {
		CnvMap cnvMap = null;
		if (tag != null) {
			String[] srcArray = range2Array(0, tag.length);
			String[] forwardA = range2Array(nShift, tag.length + nShift);
			cnvMap = new CnvMap(tag, srcArray, forwardA);
		} else {
			System.out.println("#ERROR @shiftConverter tag=null");
		}
		return cnvMap;
	}

	// ����؂�ɃV�[�P���X���܂Ƃ߂���@
	public static int[] nConverter(int[] iArray, int n) {
		return nConverter(iArray, n, false);
	}

	public static int[] nConverter(int[] iArray, int n, boolean zero) {
		int[] ��Array = new int[iArray.length];
		int grade = 1;
		if (zero)
			grade = 0; // zero ���܂ޏꍇ
		for (int i = 0; i < iArray.length; i++) {
			��Array[i] = (iArray[i] + (n - grade)) / n;
			System.out.println(" val:" + iArray[i] + " ans:" + ��Array[i]);
		}
		return ��Array;
	}

	public CnvMap(String tagStr, String valStr, String forward,
			String delimiter) {
		this((tagStr != null) ? tagStr.split(delimiter) : null,
				valStr.split(delimiter),
				(forward != null) ? forward.split(delimiter) : null);
	}

	public CnvMap(String[] tags, String[] sources, String[] forWards) {
		if (sources == null) {
			System.out.println("");
			return;// varArray�͕K�{����
		}
		// �ʒu�ɏ[�Ă閼�O��ݒ肷��i0�Ԗڂ́A���̑��p�j
		if (tags == null) {
			// null�̏ꍇ�́A���͒l��1���珇�Ɋ��蓖�Ă�
			tagNames = new String[sources.length + 1];
			for (int i = 0; i < sources.length; i++) {
				tagNames[i + 1] = sources[i];
			}
		} else {
			tagNames = new String[tags.length + 1];
			for (int i = 0; i < tags.length; i++) {
				tagNames[i + 1] = tags[i];
			}
		}
		tagNames[0] = otherName;// �f�t�H���g���́hother�h
		// �l��U��ւ���ꍇ�́A���Y�ʒu�ɍs����̃V�[�P���X�����Ă���
		int forward = 0;
		map = new HashMap();
		for (int i = 0; i < sources.length; i++) {
			String key = sources[i];
			if (forWards != null && forWards.length > i) {
				forward = Integer.parseInt(forWards[i]);
			} else {
				// �s����w�肪�����ꍇ�͂��̂܂�
				forward = i + 1;
			}
			// ������ 0������̂�1�v�f����
			if (forward >= tagNames.length)
				forward = 0; // out of range�Ȃ疵���Ȃ̂ŁE�E
			map.put(key, String.valueOf(forward));
		}
	}

	public String[] getTagArray() {
		return tagNames;
	}

	public String getTagName(int seq) {
		return tagNames[seq];
	}

	public String getTagName(String strSeq) {
		int seq = Integer.parseInt(strSeq);
		return getTagName(seq);
	}

	public int getSeq(String key) {
		// XXX �g�p�p�x�������悤�Ȃ琔�l�p�̃}�b�v�����炩���ߗp�ӂ��Ă���
		String intObj = map.get(key);
		if (intObj != null) {
			return Integer.parseInt(intObj);
		}
		return 0;
	}

	@Override
	public String convert(String val) {
		String wkObj = map.get(val);
		if (wkObj == null)
			return "0";// �O��l�͂O�ƂȂ�
		return wkObj;
	}

	public static void testShiftConverter() {
		String[] tag = { "1", "2", "3", "4", "5", "6" };
		CnvMap cnvMap = shiftConverter(tag, 1);
		String[] forDebug = range2Array(0, tag.length);
		for (int i = 0; i < forDebug.length; i++) {
			String debug = forDebug[i];
			System.out.println(
					"(" + debug + ") convert=>" + cnvMap.convert(debug));
		}
	}

	public static void testNConverter() {
		int[] iArray = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		int[] rArray = nConverter(iArray, 3, true);
		for (int i = 0; i < rArray.length; i++) {
			System.out.println("  ��Array:" + rArray[i]);
		}
	}

	public static void test00() {
		// �v�Z�ɂ�镶�������W�ϊ��A
		// ex �O����n�܂���̂��P����n�܂�ɕϊ��E�E�E�E ���́H�����{�P����
		String sourceVal = "0,1,2,3,4,5,6,7"; // ��
		String forward = "1,2,3,4,5,6,7,8"; // �ϊ���
		String tagArray = "a,b,c,d,e,f,g,h"; // �ϊ��於��
		CnvMap cnvMap = new CnvMap(tagArray, sourceVal, forward, ",");
		String[] names = cnvMap.getTagArray();
		for (int i = 0; i < names.length; i++) {
			System.out.println("names[" + i + "]:" + names[i]);
		}
		String[] dArray = sourceVal.split(",");
		String debug = "";
		for (int i = 0; i < dArray.length; i++) {
			debug = dArray[i];
			System.out
					.print("(" + debug + ") convert=>" + cnvMap.convert(debug));
			System.out.println("          =>" + names[cnvMap.getSeq(debug)]);
		}
	}

	public static void test02() {
		// ---------------------------------------------------------------------
		// ���Ԃ̕ϊ��A
		// �P���Ԃ������R���Ԃ���
		// �P���Ԃ������ϑ����Ԃ���
		// XXX 3���Ԃ����R���o�[�^�����
		// ---------------------------------------------------------------------
		// 5:00 - 5:59
		// 6:00 - 6:59
		// 7:00 - 7:59
		// 8:00 - 8:59
		// 9:00 - 9:59
		// 10:00 - 10:59
		// 11:00 - 11:59
		// 12:00 - 12:59
		// 13:00 - 13:59
		// 14:00 - 14:59
		// 15:00 - 15:59
		// 16:00 - 16:59
		// 17:00 - 17:59
		// 18:00 - 18:59
		// 19:00 - 19:59
		// 20:00 - 20:59
		// 21:00 - 21:59
		// 22:00 - 22:59
		// 23:00 - 23:59
		// 0:00 - 0:59
		// 1:00 - 1:59
		// 2:00 - 2:59
		// 3:00 - 3:59
		// 4:00 - 4:59
		// ----------------------------------------------------------------------
		String sourceVal = "0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23"; // ��
		String forward = "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24"; // �ϊ���
		String tagArray = "a,b,c,d,e,f,g,h"; // �ϊ��於��
		CnvMap cnvMap = new CnvMap(tagArray, sourceVal, forward, ",");
		String[] names = cnvMap.getTagArray();
		for (int i = 0; i < names.length; i++) {
			System.out.println("names[" + i + "]:" + names[i]);
		}
		String[] dArray = sourceVal.split(",");
		String debug = "";
		for (int i = 0; i < dArray.length; i++) {
			debug = dArray[i];
			System.out
					.print("(" + debug + ") convert=>" + cnvMap.convert(debug));
			System.out.println("          =>" + names[cnvMap.getSeq(debug)]);
		}
	}

	// ---------------------------------------------------------------------
	// ���Ԃ̕ϊ��A
	// �P���Ԃ������R���Ԃ���
	// �P���Ԃ������ϑ����Ԃ���
	// ---------------------------------------------------------------------
	// �v�Z�ɂ�镶�������W�ϊ��A
	// ex �O����n�܂���̂��P����n�܂�ɕϊ��E�E�E�E ���́H�����{�P����

	// -------------------------------------------------------------------------
	// XXX �t�@�C������������ł���悤�ɂ��Ă���
	// XXX varArray�ɐ��K�\���͎g���Ȃ����낤���H�H������ƍl���Ă݂� ,hash�̃L�[��regix�H�I����������
	// XXX �� �� �� ��̓I�ɂ���Ă݂悤�I�}�b�`���Ȃ�������regix�p�^�[���}�b�`���O�A�Ή�����L�[�𗎂Ƃ����ނ���
	// XXX �� �� �� regix���ǂ����ǂ�����Ĕ��肷��̂��H�H
	// -------------------------------------------------------------------------
	public static void main(String[] argv) {
		// testShiftConverter();
		test01();
	}

	public static void test01() {
		// ---------------------------------------------------------------------
		// sourceVal��forward���n�b�V���}�b�v�ɂ���āA�l�Ŏ��������A��������Ԃ��i���O��������j
		// �܂�A���͒l��1����n�܂�V�[�P���X�ɒu��������ׂ̃t�B���^�[�Ƃ��Ďg�p����
		// �R���e�L�X�g�͈Ⴄ���A�V�[�P���X�̑���ɐU��ւ���R�[�h�����Ă��ǂ����ȁE�E�E
		// ---------------------------------------------------------------------
		// ����Y���̓C���f�b�N�X�O�Ƃ����i����𗘗p���邩���Ȃ����ɂ��Ă͎�������j
		// sources�̓R�[�h����z�肵�Ă������A������Ȃ�Ȃ�ł������i�e�`�����񂹂���Ύg�p�\�ł��낤�j
		// forwards�͐U��ւ���̈ʒu���A�ȗ�����sources�̏o�����ƂȂ�
		// tagNames�͐U��ւ��於�́A�ȗ�����sources���̂��̂ɂł���
		// ---------------------------------------------------------------------
		String sources = "Zappa,Captain,Lowell,Jerry,Phil,Roy"; // ��
		String forwards = null; // �ϊ���V�[�P���X
		String tagNames = null; // �ϊ��於��
		forwards = "1,1,2,2,3,3";
		tagNames = "Freaks,Guiter,Base";
		// tagArray = "Frank,Beefheart,George,Garchia,Lesh,Estrada"; // �ϊ��於��
		CnvMap cnvMap = new CnvMap(tagNames, sources, forwards, ",");
		String[] names = cnvMap.getTagArray();
		for (int i = 0; i < names.length; i++) {
			System.out.println("names[" + i + "]:" + names[i]);
		}
		String[] dArray = sources.split(",");
		String debug = "";
		for (int i = 0; i < dArray.length; i++) {
			debug = dArray[i];
			System.out
					.print("(" + debug + ") convert=>" + cnvMap.convert(debug));
			System.out.println("          =>" + names[cnvMap.getSeq(debug)]);
		}
		debug = "Eric";
		System.out.print("(" + debug + ") convert=>" + cnvMap.convert(debug));
		System.out.println("          =>" + names[cnvMap.getSeq(debug)]);
		debug = "Duane";
		System.out.print("(" + debug + ") convert=>" + cnvMap.convert(debug));
		System.out.println("          =>" + names[cnvMap.getSeq(debug)]);
	}

}
