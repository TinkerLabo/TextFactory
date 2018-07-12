package kyPkg.util;

import static kyPkg.util.Joint.join;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kyPkg.converter.Inf_ArrayCnv;

//----------------------------------------------------------------------------
//	�����z����J�����ʂɎ����ϊ�����
//----------------------------------------------------------------------------
public class CnvArray implements Inf_ArrayCnv {
	private String notDef = "";
	private HashMap<Integer, HashMap<String, String>> converter = null;
	private String ans;

	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	public CnvArray(String notDef) {
		this.notDef = notDef;
		converter = new HashMap();
	}

	// ------------------------------------------------------------------------
	// �ϊ�����
	// ------------------------------------------------------------------------
	/*
	 * (�� Javadoc)
	 * 
	 * @see kyPkg.util.inf_ArrayCnv#convert(java.lang.String[])
	 */
	@Override
	public String[] convert(String[] rec, int stat) {
		List<Integer> keylist = new ArrayList(converter.keySet());
		for (Integer col : keylist) {
			if (rec.length > col) {
				String cel = rec[col];
				ans = converter.get(col).get(cel);
				if (ans != null) {
					rec[col] = ans;
				} else {
					if (notDef.equals("")) {
						rec[col] = "";
					} else {
						rec[col] = notDef + rec[col];
					}
				}
			}
		}
		return rec;
	}

	// ------------------------------------------------------------------------
	// �J�������Ƃɕϊ�������o�^����
	// ------------------------------------------------------------------------
	public void setDict(int col, HashMap<String, String> dict) {
		converter.put(new Integer(col), dict);
	}

	// ------------------------------------------------------------------------
	// ����m�F�p
	// ------------------------------------------------------------------------
	public void debug(String[] rec) {
		String bef = join(rec, ",");
		String[] array = convert(rec, 0);
		String aft = join(array, ",");
		System.out.println(bef + " => " + aft);
	}

	public static void main(String[] argv) {
		test01();
	}

	public static void test01() {
		HashMap<String, String> dict0 = new HashMap();
		dict0.put("0", "��");
		dict0.put("1", "��");

		HashMap<String, String> dict1 = new HashMap();
		dict1.put("A", "1");
		dict1.put("B", "2");
		dict1.put("C", "3");
		dict1.put("D", "4");
		dict1.put("E", "5");
		dict1.put("F", "6");

		HashMap<String, String> dict2 = new HashMap();
		dict2.put("A", "��");
		dict2.put("B", "��");
		dict2.put("C", "��");
		dict2.put("D", "��");
		dict2.put("E", "��");
		dict2.put("F", "��");

		CnvArray cnv = new CnvArray("NotDefined:");
		cnv.setDict(0, dict0);// �J�����O�ɑ΂��ĕϊ����s��
		cnv.setDict(1, dict1);// �J�����P�ɑ΂��ĕϊ����s��
		cnv.setDict(2, dict2);// �J�����O�ɑ΂��ĕϊ����s��

		cnv.debug("0,A,G".split(","));
		cnv.debug(new String[] { "0", "B", "A" });
		cnv.debug(new String[] { "0", "C", "B" });
		cnv.debug(new String[] { "1", "D", "C" });
		cnv.debug(new String[] { "0", "E", "D" });
		cnv.debug(new String[] { "1", "F", "E" });
		cnv.debug(new String[] { "0", "G", "F" });

	}

	public static void test02() {
		// �ϊ�������p�ӂ���
		HashMap<String, String> dict0 = new HashMap();
		dict0.put("0", "��");
		dict0.put("1", "��");

		CnvArray cnv = new CnvArray("NotDefined:");
		cnv.setDict(0, dict0);// �J�����O�ɑ΂��ĕϊ���o�^����

		// ���[�v���Ȃǂŕϊ�����
		String[] rec = new String[] { "0", "B", "A" };

		if (cnv != null)
			rec = cnv.convert(rec, 0);

	}

	@Override
	public void init() {
	}

	@Override
	public void fin() {
	}
//
	//	selectedFields	���j�^�[�h�c,�w�����t,�w������,JanCode,�w�����z�w����,�w�����z,�w������,�w����i�Ƒԁj,�w����i�ڍׁj,�A�C�e������,�}�X�^���i,�i��,�i�ږ�,���j�^�[���,���ю�,���ѓ����c��w�N��,���ю�N��,���ѓ����c�Ƒ��l��,�q���i�P�W�ˈȉ��j�̓����L��,���c���̓����L��,���w���i��w�N�j�̓����L��,���w���i���w�N�̓����L��,���w���̓����L��,���Z���̓����L��,�V�l�̓����L��,�Z���`��,�l�N��,���єN��,�N�x�N��,����,������,���ѓ����c���g�̎q���̗L��,�E��,�w�����,���E�N��i�P�O�΋敪�j,���E�N��i�T�΋敪�j,���E�N��i���f�B�A�敪�j,�N��i�P�O�΋敪�j,�N��i�T�΋敪�j,�N��i���f�B�A�敪�j,�Z���i�s���{���j,�Z���i�G���A�j,�E�ƃ^�C�v
//	sourceDir	Z:/S2/rx/enquetes/NQ//03_�����E���N���/2016/

	
}
