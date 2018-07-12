package kyPkg.converter;

import java.text.DecimalFormat;
import java.util.*;

public class UnitCnv {

	private static UnitCnv unitCnv;
	private DecimalFormat df;

	// ----------------------------------------------------------------
	public UnitCnv() {
		df = new DecimalFormat("########.###");
		incore();
	}

	public static UnitCnv getInstance() {
		if (unitCnv == null) {
			unitCnv = new UnitCnv();
		}
		return unitCnv;
	}

	// ----------------------------------------------------------------
	// �p�o�q�� convert �P�ʂ̊��߂�����
	// �����F�ϊ���P�ʃR�[�h�A�ϊ����P�ʃR�[�h�A�ϊ�����l
	// �s�g�p��t ��"001"�͂��A"002"�͇s
	// UnitCnv wUnitCnv = null;
	// if (wUnitCnv==null) wUnitCnv = new UnitCnv();
	// wUnitCnv.convert("002","001", 30);
	// ----------------------------------------------------------------
	Object[][] data = { { "000", "�H", new Integer(1000) },
			{ "001", "g", new Integer(1000) },
			{ "002", "kg", new Integer(1000000) },
			{ "003", "mg", new Integer(1) },
			{ "004", "pnd", new Integer(373240) },
			{ "005", "oz", new Integer(31103) },
			{ "101", "ml", new Integer(1) }, { "102", "L", new Integer(1000) },
			{ "103", "kL", new Integer(1000000) },
			{ "104", "�t", new Integer(1) },
			{ "105", "����  ", new Integer(3785) },
			{ "106", "CM3", new Integer(1) },
			{ "107", "M3", new Integer(1000000) },
			{ "201", "cm", new Integer(1000) },
			{ "202", "m", new Integer(100000) },
			{ "203", "km", new Integer(100000000) },
			{ "204", "mm", new Integer(100) },
			{ "205", "�-��", new Integer(91440) },
			{ "206", "���", new Integer(2540) },
			{ "207", "̲-�", new Integer(30480) },
			{ "301", "CM2", new Integer(1000) },
			{ "302", "M2", new Integer(10000000) },
			{ "303", "MM2", new Integer(10) },
			{ "399", "�W��", new Integer(1000) },
			{ "501", "��", new Integer(1000) },
			{ "502", "��", new Integer(1000) },
			{ "503", "�{", new Integer(1000) },
			{ "504", "��", new Integer(1000) },
			{ "505", "��", new Integer(1000) },
			{ "506", "��", new Integer(1000) },
			{ "507", "��", new Integer(1000) },
			{ "508", "��", new Integer(1000) },
			{ "509", "��", new Integer(1000) },
			{ "510", "��", new Integer(1000) },
			{ "511", "��", new Integer(1000) },
			{ "512", "�g", new Integer(1000) },
			{ "513", "��", new Integer(1000) },
			{ "514", "��", new Integer(1000) },
			{ "515", "��", new Integer(1000) },
			{ "516", "��", new Integer(1000) },
			{ "517", "��߾�", new Integer(1000) },
			{ "518", "�V", new Integer(1000) },
			{ "519", "�l�O", new Integer(1000) },
			{ "520", "�H", new Integer(1000) },
			{ "521", "��", new Integer(1000) },
			{ "522", "��", new Integer(1000) },
			{ "523", "����", new Integer(1000) },
			{ "524", "�o", new Integer(1000) },
			{ "525", "��", new Integer(1000) },
			{ "526", "��", new Integer(1000) },
			{ "527", "��", new Integer(1000) },
			{ "528", "�ް�", new Integer(1000) },
			{ "529", "��", new Integer(1000) } };
	HashMap hashCodeNam = new java.util.HashMap();
	HashMap hashCodeVal = new java.util.HashMap();

	// ----------------------------------------------------------------
	// convert �P�ʂ̊��߂�����
	// �����F�ϊ���P�ʃR�[�h�A�ϊ����P�ʃR�[�h�A�ϊ�����l
	// �s�g�p��t ��"001"�͂��A"002"�͇s
	// UnitCnv wUnitCnv = null;
	// if (wUnitCnv==null) wUnitCnv = new UnitCnv();
	// wUnitCnv.convert("002","001", 30);
	// ----------------------------------------------------------------
	public float convertPlus(String unitTo, String unitFrom, int unitVal) {
		//		System.out.println("@UnitCnv.unitTo To:" + unitTo + " From:" + unitFrom + " val:"
		//				+ unitVal);
		float capa = convert(unitTo, unitFrom, unitVal);
		// ---------------------------------------------
		// DB��̒l��*1000�ƂȂ��Ă���̂ŁE�E����K�v������
		// ---------------------------------------------
		if (capa > 0) {
			capa = capa / 1000.0f;
		} else {
			capa = 0;
		}
		//		System.out.println("=> �e��:" + capa);
		return capa;
	}

	public float convert(String unitTo, String unitFrom, int unitVal) {
		float iRtn = -1;// original �ϊ��s�\�Ȃ畉�̒l
		// float iRtn = 0;//20130730 �ϊ��s�\�Ȃ炚������
		String wPrefix1 = unitTo.substring(0, 1);
		String wPrefix2 = unitFrom.substring(0, 1);
		if (wPrefix1.equals(wPrefix2)) {
			if (unitVal != 0) {
				int wVal1 = getVal(unitTo);
				int wVal2 = getVal(unitFrom);
				iRtn = (float) (Math.round(unitVal * wVal2 * 100.0 / wVal1)
						/ 100.0);
			}
			// System.out.println("��" + iRtn + getName(pUnitTo) + " �� " + pValue
			// + " " + getName(pUnitFrom));
		}
		return iRtn;
	}

	public String convert(String pUnitTo, String pUnitFrom, String cel) {
		float f = 0;
		cel = cel.trim();
		if (cel.equals("")) {
			cel = "0";
		}
		int iVal = Integer.valueOf(cel);
		if (iVal > 0) {
			f = convert(pUnitTo, pUnitFrom, iVal) / 1000;
		}
		if (f >= 0.0) {
			// ���ƂŒP�ʋL���ƕ�������ꍇ������̂ŃX�y�[�X������ł���
//			return df.format(f) + ".0~ " + getName(pUnitTo);//20170314 +"��" 
			return df.format(f) + " " + getName(pUnitTo);//20170314 +"��" 
		} else {
			if(!pUnitFrom.trim().equals("")){
				//#createTester--------------------------------------------------
				//				System.out.println("public static void testconvert() {");
				//				System.out.println("    String pUnitTo = \"" + pUnitTo + "\";");
				//				System.out.println("    String pUnitFrom = \"" + pUnitFrom + "\";");
				//				System.out.println("    String cel = \"" + cel + "\";");
				//				System.out.println(
				//						"    convert ins = new convert(pUnitTo,pUnitFrom,cel);");
				//				System.out.println("}");
				//--------------------------------------------------
			}
			return "NotDefined (" + cel + ")";//20170124
		}
	}

	// ----------------------------------------------------------------
	// incore ���߂��e�[�u�����n�b�V���e�[�u����
	// ----------------------------------------------------------------
	public void incore() {
		// System.out.println("### UnitCnv incore ###");
		for (int i = 0; i < data.length; i++) {
			if (data[i].length == 3) {
				// System.out.println("## " + data[i][1]);
				hashCodeNam.put(data[i][0], data[i][2]);
				hashCodeVal.put(data[i][0], data[i][1]);
			}
		}
	}

	// ----------------------------------------------------------------
	// ���߂��l���n�b�V���e�[�u��������o��
	// ----------------------------------------------------------------
	public int getVal(String pKey) {
		int wRtn = -1;
		Object wObj = hashCodeNam.get(pKey);
		if (wObj != null) {
			if (wObj instanceof Integer) {
				wRtn = ((Integer) wObj).intValue();
			}
		}
		return wRtn;
	}

	// ----------------------------------------------------------------
	// �P�ʖ����n�b�V���e�[�u��������o��
	// ----------------------------------------------------------------
	public String getName(String pKey) {
		if (pKey == null) {
			return null;
		}
		pKey = pKey.trim();
		if (!pKey.equals("")) {
			Object wObj = hashCodeVal.get(pKey);
			if (wObj != null) {
				if (wObj instanceof String) {
					return wObj.toString();
				}
			}
		}
		return "";
	}

	// ----------------------------------------------------------------
	public static void main(String[] argv) {
		UnitCnv wUnitCnv = new UnitCnv();
		wUnitCnv.convert("002", "001", 3);
		wUnitCnv.convert("002", "001", 30);
		wUnitCnv.convert("002", "001", 3000);
		wUnitCnv.convert("001", "002", 3);
	}

}
