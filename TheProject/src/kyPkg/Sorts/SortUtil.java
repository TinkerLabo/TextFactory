package kyPkg.Sorts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

// ----------------------------------------------------------------------------
//�@�\�[�g�p�����[�^�̃J�����w���0���͂��߂�i1�ł͂Ȃ��I�I�j
// ----------------------------------------------------------------------------
public class SortUtil {
	public static String[] arraySkip(String[] data, int skip) {
		String[] outData = new String[data.length - skip];
		int j = 0;
		for (int i = skip; i < data.length; i++) {
			outData[j++] = data[i];
		}
		return outData;
	}

	/**************************************************************************
	 * array2CnvMap			���\�[�g�p�}�b�v�����i��w����ޔ����擪�֑}�������Łj	
	 <br>�� �i�Ō�̗v�f����w���ł��邱�Ƃ������Ă��āA���̗v�f�̕\���ʒu��擪�Ɉړ��������ׂ�array2CnvMap���������Ă���j
	 * @param sortParam		�\�[�g�p�����[�^�i�ʒu�A�^�AA:���� D:�~���j�@"0,String,A";	 
	 * @param data			�\�[�g�ΏۂƂ���l�̔z��	{40641: �r�[��,40681: ���L���[����,40691: ���A��,D0400: �r�[��,D0900: ���A��,D0950: �V�W������,D9999: �m���A���R�[���r�[��,��w��};	 
		<hr>
		<br>�o�̓C���[�W
		<br>�\�[�g��̈ʒu�ƁA�\�[�g�O�̈ʒu�Ƃ̑Ή���\���}�b�v�����@�i�����Ӄ\�[�g�ΏۂƂ���l�̓}�b�v�Ɋ܂߂Ȃ��j
		<br>		�\�[�g��̈ʒu:0 �\�[�g�O�̈ʒu:8 �\�[�g�ΏۂƂ���l:��w��  
		<br>		�\�[�g��̈ʒu:0 �\�[�g�O�̈ʒu:0 �\�[�g�ΏۂƂ���l:40641: �r�[��
		<br>		�\�[�g��̈ʒu:1 �\�[�g�O�̈ʒu:1 �\�[�g�ΏۂƂ���l:40681: ���L���[����
		<br>		�\�[�g��̈ʒu:2 �\�[�g�O�̈ʒu:2 �\�[�g�ΏۂƂ���l:40691: ���A��
		<br>		�\�[�g��̈ʒu:3 �\�[�g�O�̈ʒu:3 �\�[�g�ΏۂƂ���l:D0400: �r�[��
		<br>		�\�[�g��̈ʒu:4 �\�[�g�O�̈ʒu:4 �\�[�g�ΏۂƂ���l:D0900: ���A��
		<br>		�\�[�g��̈ʒu:5 �\�[�g�O�̈ʒu:5 �\�[�g�ΏۂƂ���l:D0950: �V�W������
		<br>		�\�[�g��̈ʒu:6 �\�[�g�O�̈ʒu:6 �\�[�g�ΏۂƂ���l:D9999: �m���A���R�[���r�[��
		<hr>
	 **************************************************************************/
	public static HashMap<Integer, Integer> array2CnvMap_plus(String sortParam,
			String[] data, boolean debug) {
		//---------------------------------------------------------------------
		//�o�\�[�g�ΏۂƂ���l�A���̈ʒu�p��v�f�ɂ����X�g������ă\�[�g���s��
		//---------------------------------------------------------------------
		List<String[]> arrayList = new ArrayList();
		for (int i = 0; i < data.length - 1; i++) {//��w���i��ԍŌ�̒l�j���\�[�g�Ώۂ���O��
			arrayList.add(new String[] { data[i], String.valueOf(i) });
		}
		List<String[]> result = sortArrayList(sortParam, arrayList);
		HashMap<Integer, Integer> map = new HashMap();
		map.put(0, data.length - 1);//��w����擪�ɑ}��
		//		System.out.println(
		//				"�\�[�g��̈ʒu:" + 0 + " �\�[�g�O�̈ʒu:" + data.length + " �\�[�g�ΏۂƂ���l:" + data[data.length-1]);
		for (int i = 0; i < result.size(); i++) {
			String[] array = result.get(i);
			map.put(i + 1, Integer.valueOf(array[1]));//��w����擪�ɑ}�������Ԃ񂸂炷
			//			System.out.println(
			//					"�\�[�g��̈ʒu:" + i + " �\�[�g�O�̈ʒu:" + array[1] + " �\�[�g�ΏۂƂ���l:" + array[0]);
		}
		return map;
	}

	/**************************************************************************
	 * array2CnvMap			���\�[�g�p�}�b�v����	
	 * @param sortParam		�\�[�g�p�����[�^�i�ʒu�A�^�AA:���� D:�~���j�@"0,String,A";	 
	 * @param data			�\�[�g�ΏۂƂ���l�̔z��	{40641: �r�[��,40681: ���L���[����,40691: ���A��,D0400: �r�[��,D0900: ���A��,D0950: �V�W������,D9999: �m���A���R�[���r�[��,��w��};	 
	 **************************************************************************/
	public static HashMap<Integer, Integer> array2CnvMap(String sortParam,
			String[] data) {
		//---------------------------------------------------------------------
		//�o�\�[�g�ΏۂƂ���l�A���̈ʒu�p��v�f�ɂ����X�g������ă\�[�g���s��
		//---------------------------------------------------------------------
		List<String[]> arrayList = new ArrayList();
		for (int i = 0; i < data.length; i++) {
			arrayList.add(new String[] { data[i], String.valueOf(i) });
		}
		List<String[]> result = sortArrayList(sortParam, arrayList);
		//---------------------------------------------------------------------
		//�\�[�g��̈ʒu�ƁA�\�[�g�O�̈ʒu�Ƃ̑Ή���\���}�b�v�����@�i�\�[�g�ΏۂƂ���l�̓}�b�v�Ɋ܂߂Ȃ��j
		//		�\�[�g��̈ʒu:0 �\�[�g�O�̈ʒu:0 �\�[�g�ΏۂƂ���l:40641: �r�[��
		//		�\�[�g��̈ʒu:1 �\�[�g�O�̈ʒu:1 �\�[�g�ΏۂƂ���l:40681: ���L���[����
		//		�\�[�g��̈ʒu:2 �\�[�g�O�̈ʒu:2 �\�[�g�ΏۂƂ���l:40691: ���A��
		//		�\�[�g��̈ʒu:3 �\�[�g�O�̈ʒu:3 �\�[�g�ΏۂƂ���l:D0400: �r�[��
		//		�\�[�g��̈ʒu:4 �\�[�g�O�̈ʒu:4 �\�[�g�ΏۂƂ���l:D0900: ���A��
		//		�\�[�g��̈ʒu:5 �\�[�g�O�̈ʒu:5 �\�[�g�ΏۂƂ���l:D0950: �V�W������
		//		�\�[�g��̈ʒu:6 �\�[�g�O�̈ʒu:6 �\�[�g�ΏۂƂ���l:D9999: �m���A���R�[���r�[��
		//		�\�[�g��̈ʒu:7 �\�[�g�O�̈ʒu:7 �\�[�g�ΏۂƂ���l:��w��
		//---------------------------------------------------------------------
		HashMap<Integer, Integer> map = new HashMap();
		for (int i = 0; i < result.size(); i++) {
			String[] array = result.get(i);
			map.put(i, Integer.valueOf(array[1]));
			//			System.out.println(
			//					"�\�[�g��̈ʒu:" + i + " �\�[�g�O�̈ʒu:" + array[1] + " �\�[�g�ΏۂƂ���l:" + array[0]);
		}
		return map;
	}

	public static List<String[]> sortArrayList(String sortParam,
			List<String[]> arrayList) {
		ArrayComparator multiComparator = new ArrayComparator(sortParam);
		// ---------------------------------------------------------------------
		// sort
		// ---------------------------------------------------------------------
		Collections.sort(arrayList, multiComparator);
		List<String[]> outArrayList = new ArrayList();
		if (arrayList.size() > 0) {
			for (String[] array : arrayList) {
				outArrayList.add(array);
			}
		}
		return outArrayList;
	}

	// -------------------------------------------------------------------------
	// test
	// -------------------------------------------------------------------------
	public static void test_Array2CnvMap_01() {
		String[] data = { "", "���v", "4901002045876: �r���a �������X�p�\�[�X�c�i���傤�䕗���W�P�D�S��",
				"4901002125745: �r���a �܂��X�p�y�y�����`�[�m �S�S�D�U��",
				"4901002125752: �r���a �܂��X�p�o�W�� �S�W��",
				"4901002125769: �r���a �܂��X�p�C�V�g�}�g�N���[�� �P�R�O��",
				"4901002132323: �r���a �܂��X�p ���������炱�o�^�[ �T�R�D�S��",
				"4901577020650: �L���[�s�[ ������p�X�^�\�[�X ���炱 �Q�R���~�Q",
				"4901577020667: �L���[�s�[������p�X�^�\�[�X���炵�����q�Q�R���~�Q",
				"4901577020698: �L���[�s�[ ������p�X�^�\�[�X�c�i�}�� �S�O���~�Q",
				"4901577050282: ������p�X�^�\�[�X�O�邠���������Ⴑ �T�U�D�W��",
				"4901577055218: ������~�[�g�\�[�X�t�H���E�h�E���H�[�d���W�O���~�Q",
				"4901577055232: ������J���{�i�[�� �Z���`�[�Y�d���� �V�O���~�Q",
				"4901577055256: ������J�j�̃g�}�g�b�}�X�J���|�[�l�d���V�O���~�Q",
				"4901577055263: ������a�����̂��o�^�[�ݖ��d���ĂT�T���~�Q",
				"4901577055287: ������{���S���r�A���R �����C���d���� �U�O���~�Q",
				"4902110318609: �}�}�[ �p�X�^�L�b�`�� �~�[�g�\�[�X �P�S�O��",
				"4902110318616: �}�}�[ �p�X�^�L�b�`�� �i�|���^�� �P�S�O��",
				"4902110318661: �}�}�[ ���b�`�Z���N�g �~�[�g�\�[�X �Q�U�O��",
				"4902110318685: �}�}�[ ���b�`�Z���N�g �i�|���^�� �Q�S�O��",
				"4902110318708: �}�}�[ ���b�`�Z���N�g �J���{�i�[�� �Q�U�O��",
				"4902110319545: �̓��A�o�����Ɛԃ��C���}�X�J���|�[�l �P�R�S��",
				"4902521110236: ��l�ނ��̃p�X�^ �C�Z�G�r�̃g�}�g�N���[���P�R�O��",
				"4902521110328: ��l�ނ��̃p�X�^ �����炱�̏\���Y���N���[���d����",
				"4902521110335: ��l�ނ��̃p�X�^�g���킢�I�̃g�}�g�N���[���P�W�O��",
				"4902521110359: �I�}�[���C�V�̃g�}�g�\�[�X�X�[�v�d���� �P�W�O��",
				"4902521110366: �n�C���c�����ƃC�x���R�؂̑e�҂��{���l�[�[�P�R�O��",
				"4902521110373: �n�C���c ���؂�x�[�R�����ґ�i�|���^�� �P�R�O��", "��w��" };
		int skip = 2;
		String sortParam = "0,String,A";
		HashMap<Integer, Integer> cnvMap = SortUtil.array2CnvMap(sortParam,
				arraySkip(data, skip));

		debug(cnvMap, data, skip);
	}

	public static void test_Array2CnvMap_02() {
		String[] data = { "0.15", "0.33", "0.13", "0.04", "0.11", "0.33",
				"0.08", "0.02", "0.09", "0.12", "0.10", "0.05", "0.06", "0.02",
				"0.01", "0.01", "0.03", "0.02", "0.03", "0.01", "0.81", "0.50",
				"0.30", "0.08", "0.16", "0.12", "0.07", "0.08", "0.17", "0.09",
				"0.10", "0.06", "0.26", "0.20", "0.07", "0.06", "0.13", "0.22",
				"0.08", "0.08", "0.03", "0.04", "0.03", "0.02", "0.16", "0.01",
				"0.01", "0.07", "0.02", "0.07", "0.04", "0.02", "0.04", "0.09",
				"0.09", "0.05", "0.03", "0.03", "0.05", "0.09", "0.18", "0.42",
				"0.27", "0.44", "0.08", "0.14", "0.06", "0.07", "0.10", "0.19",
				"0.25", "0.07", "0.04", "0.15", "0.07", "0.21", "0.14", "0.08",
				"0.15", "0.11", "0.04", "0.07", "0.03", "0.09", "0.08", "0.20",
				"0.13", "0.10", "0.02", "93.58" };

		String sortParam = "0,Double,D";
		HashMap<Integer, Integer> cnvMap = SortUtil.array2CnvMap(sortParam,
				data);

		debug(cnvMap, data, 0);
	}

	private static void debug(HashMap<Integer, Integer> map, String[] testData,
			int skip) {
		for (int seq = 0; seq < map.size(); seq++) {
			System.out.println("debug key:" + seq + " map:" + map.get(seq)
					+ " val:" + testData[map.get(seq) + skip]);
		}

	}

	//�Ԓl���󂯎��Ȃ���΁@����p���������Ȃ��Ȃ����H�H�@20170529
	public static String[] arrayCnvByMap_orgx(HashMap<Integer, Integer> cnvMap,
			String[] rec, int skip) {
		String[] outRec = new String[rec.length];
		if (cnvMap == null) {
			for (int i = 0; i < rec.length; i++) {
				outRec[i] = rec[i];
				if (outRec[i].equals(""))
					outRec[i] = "0";//20151124
			}
		} else {
			int col = 0;
			for (int i = 0; i < skip; i++) {
				outRec[col] = rec[i];
				if (outRec[col].equals(""))
					outRec[col] = "0";//20151124
				col++;
			}
			//			System.out.println(">>>#20170529# col:" + col + " skip:" + skip);
			for (int seq = 0; seq < cnvMap.size(); seq++) {
				Integer xSeq = cnvMap.get(seq);
				if (xSeq != null) {
					outRec[col] = rec[xSeq + skip];
					if (outRec[col].equals(""))
						outRec[col] = "0";//20151124
					col++;
				} else {
					System.err.println("xSeq==null seq:" + seq);
				}
			}
		}
		//		for (String element : outRec) {
		//			System.out.println("outRec #20170529# :" + element);
		//		}
		return outRec;
	}

	//�Ԓl���󂯎��Ȃ���΁@����p���������Ȃ��Ȃ����H�H�@20170529
	//����p�͊��Ⴂ�ł�����
	public static String[] arrayCnvByMap(HashMap<Integer, Integer> cnvMap,
			String[] rec, int skip) {
		String[] outRec = new String[rec.length];
		for (int i = 0; i < rec.length; i++) {
			outRec[i] = rec[i];
			if (outRec[i].equals(""))
				outRec[i] = "0";//20151124
		}
		if (cnvMap != null) {
			int srcCol = 0;
			int dstCol = 0;
			List<Integer> keyList = new ArrayList(cnvMap.keySet());
			for (Integer key : keyList) {
				Integer val = cnvMap.get(key);
				if (val != null) {
					dstCol = key + skip;
					srcCol = val + skip;
					if (rec.length > srcCol && outRec.length > dstCol) {
						outRec[dstCol] = rec[srcCol];
						if (outRec[dstCol].equals(""))
							outRec[dstCol] = "0";//20151124
					}
				} else {
					System.err.println("xSeq==null seq:" + key);
				}
			}
		}
		return outRec;
	}

	public static String[] arrayCnvByMap_org(HashMap<Integer, Integer> cnvMap,
			String[] rec, int skip) {
		String[] outRec = new String[rec.length];
		if (cnvMap == null) {
			for (int i = 0; i < rec.length; i++) {
				outRec[i] = rec[i];
			}
		} else {
			int col = 0;
			for (int i = 0; i < skip; i++) {
				outRec[col++] = rec[i];
			}
			for (int seq = 0; seq < cnvMap.size(); seq++) {
				outRec[col++] = rec[cnvMap.get(seq) + skip];
			}
		}
		return outRec;
	}

	/**************************************************************************
	 * getCnvMap ��SORT�p�R���o�[�^�쐬				
	 * @param total_Low		���v�s	{���v,100,0.45,2.27,0,23.65,11.01,26.38,9.5,56.45};	 
	 * @param headArray		�w�b�_�[	{���ԂP/���ԂQ,���v,40641: �r�[��,40681: ���L���[����,40691: ���A��,D0400: �r�[��,D0900: ���A��,D0950: �V�W������,D9999: �m���A���R�[���r�[��,��w��};	 
	 * @param skip			�X�L�b�v	skip = 2; 
	 * @param sortPlan		sortPlan = true;	 
	 **************************************************************************/
	public static HashMap<Integer, Integer> getCnvMap(String[] total_Low,
			String[] headArray, int skip, boolean sortPlan) {
		//#createTester--------------------------------------------------
//		System.out.println("public static void testgetCnvMap() {");
//		System.out.println("    String[] total_Low = {"
//				+ kyPkg.util.KUtil.join(total_Low, ",", "\"") + "};");
//		System.out.println("    String[] headArray = {"
//				+ kyPkg.util.KUtil.join(headArray, ",", "\"") + "};");
//		System.out.println("    int skip = " + skip + ";");
//		System.out.println("    boolean sortPlan = " + sortPlan + ";");
//		System.out.println(
//				"    getCnvMap ins = new getCnvMap(total_Low,headArray,skip,sortPlan);");
//		System.out.println("}");
		//--------------------------------------------------
		HashMap<Integer, Integer> cnvMap = null;
		//		int skip = 2;//�擪��2�Z���������ΏۊO�Ƃ������̂ŁE�E�E�Ƃ΂�
		if (sortPlan) {
			//			for (int i = 0; i < total_Low.length; i++) {
			//				System.out.println("###debug20150721### total_Low[" + i + "]"
			//						+ total_Low[i]);
			//			}
			String sortParam = "0,Double,D";
			//TODO ���̒i�K�Ń\�[�g�̑Ώۂ����w�����O���Ă����K�v������E�E�E������ɕϊ��}�b�v�̐擪�ɔ�w����}������K�v������E�E��
			String[] data = SortUtil.arraySkip(total_Low, skip);
			//			System.out.println("    String[] data = {" + kyPkg.util.KUtil.join(data) + "};");
			cnvMap = SortUtil.array2CnvMap_plus(sortParam, data, true);//��w����擪�Ɉړ����鏈�����s���Ă���
		} else {
			//			for (int i = 0; i < headArray.length; i++) {
			//				System.out.println("###debug20150721### headArray[" + i + "]"
			//						+ headArray[i]);
			//			}
			String sortParam = "0,String,A";
			String[] data = SortUtil.arraySkip(headArray, skip);
			cnvMap = SortUtil.array2CnvMap_plus(sortParam, data, true);//��w����擪�Ɉړ����鏈�����s���Ă���
		}
		return cnvMap;
	}

	//-------------------------------------------------------------------------
	//�@�����������̂̃e�X�g��`���̂͂ނÂ�����
	//-------------------------------------------------------------------------
	public static void testArrayCnvByMap() {
		String[] total_Low = { "���v", "100", "54.09", "20.53", "23.74", "27.37",
				"3.13", "3.42", "35.53", "41.88", "62.49", "25.27", "34.59",
				"41.78", "48.67", "70.6", "36.24", "18.03", "63.1", "27.68",
				"4.64", "3.01" };
		String[] headArray = { "���ԂP/���ԂQ", "���v", "A0000: ���{������", "B0000: ��������",
				"C0000: ����������", "D0000: �u�����h����", "D1000: ���N��", "E0000: �������e",
				"F0000: �~�l�����E�H�[�^�[", "G0000: �g������", "H0000: �R�[�q�[����",
				"I0000: ��؂P�O�O������", "I1000: ��؉ʏ`��������", "J0000: �ʏ`�P�O�O��",
				"J1000: �ʏ`����", "L0000: �Y�_����", "M0000: �X�|�[�c����",
				"N0000: �@�\�������i�h�{�h�����N�܂ށj", "O0000: �����E���_��", "Q0000: ��������",
				"R0000: �|����", "��w��" };
		String[] answer = { "���ԂP/���ԂQ", "���v", "��w��", "L0000: �Y�_����",
				"O0000: �����E���_��", "H0000: �R�[�q�[����", "A0000: ���{������", "J1000: �ʏ`����",
				"G0000: �g������", "J0000: �ʏ`�P�O�O��", "M0000: �X�|�[�c����",
				"F0000: �~�l�����E�H�[�^�[", "I1000: ��؉ʏ`��������", "Q0000: ��������",
				"D0000: �u�����h����", "I0000: ��؂P�O�O������", "C0000: ����������",
				"B0000: ��������", "N0000: �@�\�������i�h�{�h�����N�܂ށj", "R0000: �|����",
				"E0000: �������e", "D1000: ���N��", };
		int skip = 2;
		boolean sortPlan = true;
		HashMap<Integer, Integer> cnvMap = getCnvMap(total_Low, headArray, skip,sortPlan);
		//		List<Integer> keyList = new ArrayList(cnvMap.keySet());
		//		for (Integer key : keyList) {
		//			System.out.println("key:" + key + " val:" + cnvMap.get(key));
		//		}
		//		String[] result = arrayCnvByMap(cnvMap, headArray, 2);
		String[] result = arrayCnvByMap(cnvMap, headArray, 2);
		//		for (int i = 0; i < result.length; i++) {
		//			System.out.println("\"" + result[i] + "\",");
		//		}
		boolean isEQ = Arrays.equals(answer, result);
		System.out.println("isEQ:" + isEQ);
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		//		testgetCnvMap();
		testArrayCnvByMap();
	}
}
