package kyPkg.uRegex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class ListUtil {

	public ListUtil() {
		// TODO Auto-generated constructor stub
	}

	// -------------------------------------------------------------------------
	//�@�܂��A������d���i�����Ƃ��ꂢ�ɏ�����΂悢�̂����E�E�E�j 20160819
	// -------------------------------------------------------------------------
	/**************************************************************************
	 * limitter		
	 * @author	ken yuasa
	 * @version	1.0
	 * <ul>
	 * <ll>�y�T�v�z�v�f���f���~�^�ŕ������A���Y�J���������K�\���Ɉ�v����s�ɂ��āA�w�肵���J�������t�H�[�}�b�g�������̂����X�g�ŕԂ�
	 * </ul>
	 * @param regCol	���K�\���ŕ]������ΏۂƂ���J����		 
	 * @param regex		���K�\������������	 
	 * @param format	�o�͂Ɏg�p����t�H�[�}�b�g�A�o�̓J�����Ɠ�����%s���w�肷��		 
	 * @param cols		�o�͑ΏۃJ����	 
	 * @param delimiter	���X�g���̕�����𕪊�����		 
	 * @param list		�����ΏۂƂ��镶���񃊃X�g	 
	 **************************************************************************/
	public static List<String> limitter(int[] regCol, String[] regex,
			String format, int[] cols, String delimiter, List<String> list) {
		List<String> resList = new ArrayList();
		Pattern[] pattern = getPatterns(regex, false);
		for (String element : list) {
			String[] array = element.split(delimiter, -1);
			int chkCount = check(regCol, pattern, array);
			if (chkCount == regCol.length) {
				String res = "";
				switch (cols.length) {
				case 1:
					res = String.format(format, array[cols[0]]);
					break;
				case 2:
					res = String.format(format, array[cols[0]], array[cols[1]]);
					break;
				case 3:
					res = String.format(format, array[cols[0]], array[cols[1]],
							array[cols[2]]);
					break;
				case 4:
					res = String.format(format, array[cols[0]], array[cols[1]],
							array[cols[2]], array[cols[3]]);
					break;
				case 5:
					res = String.format(format, array[cols[0]], array[cols[1]],
							array[cols[2]], array[cols[3]], array[cols[4]]);
					break;
				default:
					break;
				}
				resList.add(res);
//				System.out.println("limitter test=>" + res);
			}
		}
		return resList;
	}

	//-------------------------------------------------------------------------
	//	���X�g�̊e�v�f���f���~�^�ŋ�؂�w�肳�ꂽ�ʒu�̗v�f�����j�[�N�ȃ��X�g�ŕԂ� 20160822
	// -------------------------------------------------------------------------
	//	110098000002_�����}�s�d�S�^�O�X�O�V�ӂ��Ĉ��ރ[���[�����@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@,E89093,110098000002,2015/02/03 12:21:51
	//	110098000008_�����}�s�d�S�^�����L���p�h�{�h�����N�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@,COMMON,110098000008,2015/02/03 12:21:51
	//	110098000009_�����}�s�d�S�^�����L���p�X�|�[�c�h�����N�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@,COMMON,110098000009,2015/02/03 12:21:51
	//	110098000130_�����}�s�d�S�^�����L���]�~�l�����E�H�[�^�[�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@,COMMON,110098000130,2015/04/27 12:07:53
	//	123067000001_�t�W�p���^�o�^�[���[���P�T�O�V�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@,COMMON,123067000001,2016/02/25 09:27:00
	//	123067000002_�t�W�p���^����V���[�Y�P�T�O�Q�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@,COMMON,123067000002,2016/01/04 12:35:35
	//	123091000001_�s�W�����^�n���h�\�[�v�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@,COMMON,123091000001,2015/02/03 12:03:26
	//	123620000001_�i�J���^�y�؂̑f�P�S�O�S�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@,COMMON,123620000001,2015/02/02 17:21:45
	//-------------------------------------------------------------------------
	public static List<String> list2uqList(String delimiter, List<String> list,int col) {
		Set<String> set = new HashSet();
		for (String element : list) {
			String[] array = element.split(delimiter, -1);
			if (array.length > col) {
				set.add(array[col]);
			}
		}
		List<String> resList = new ArrayList(set);
		Collections.sort(resList);
		return resList;
	}

	// -------------------------------------------------------------------------
	//�p�^�[���Ɉ�v��������Ԃ��i�����̃p�^�[���Ɉ�v���邩�j
	//TODO�@�p�t�H�[�}���X���P�̗]�n����E�E�E�E
	//TODO	OR�@�@��ł���v����΂悢�ꍇ�@=>�@��ڂ̈�v�Ń��^�[����������
	//TODO	AND�@���ׂĂɈ�v���Ȃ���΂Ȃ�Ȃ��ꍇ�@=>�@��ł���v���Ȃ���΂O��Ԃ�
	// -------------------------------------------------------------------------
	private static Pattern[] getPatterns(String[] regex, boolean ignoreCase) {
		Pattern[] pattern = new Pattern[regex.length];
		for (int col = 0; col < regex.length; col++) {
			pattern[col] = Regex.getPatternEx(regex[col], ignoreCase);
		}
		return pattern;
	}

	// -------------------------------------------------------------------------
	//TODO regCol��pattern�̐��������Ă��Ȃ��Ƃ����Ȃ� 
	// -------------------------------------------------------------------------
	public static int check(int[] regCol, Pattern[] pattern, String[] array) {
		int chkCount = 0;
		for (int seq = 0; seq < regCol.length; seq++) {
			int col = regCol[seq];
			if (array.length > col && pattern.length > seq) {
				if (pattern[seq].matcher(array[col]).matches()) {
					chkCount++;
				}
			}
		}
		return chkCount;
	}


	// -------------------------------------------------------------------------
	//	�w��i�ڂ��s�b�N�A�b�v���郍�W�b�N�Ɏg�p����\��@20160819
	// -------------------------------------------------------------------------
	public static void testLimitter01() {
		List<String> list = new ArrayList();
		list.add(
				"110098000002_�����}�s�d�S�^�O�X�O�V�ӂ��Ĉ��ރ[���[�����@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@,E89093,110098000002,2015/02/03 12:21:51");
		list.add(
				"110098000008_�����}�s�d�S�^�����L���p�h�{�h�����N�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@,COMMON,110098000008,2015/02/03 12:21:51");
		list.add(
				"110098000009_�����}�s�d�S�^�����L���p�X�|�[�c�h�����N�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@,COMMON,110098000009,2015/02/03 12:21:51");
		list.add(
				"110098000130_�����}�s�d�S�^�����L���]�~�l�����E�H�[�^�[�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@,COMMON,110098000130,2015/04/27 12:07:53");
		list.add(
				"123067000001_�t�W�p���^�o�^�[���[���P�T�O�V�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@,COMMON,123067000001,2016/02/25 09:27:00");
		list.add(
				"123067000002_�t�W�p���^����V���[�Y�P�T�O�Q�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@,COMMON,123067000002,2016/01/04 12:35:35");
		list.add(
				"123091000001_�s�W�����^�n���h�\�[�v�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@,COMMON,123091000001,2015/02/03 12:03:26");
		list.add(
				"123620000001_�i�J���^�y�؂̑f�P�S�O�S�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@,COMMON,123620000001,2015/02/02 17:21:45");
		int[] regCol = new int[] { 1, 0 };
		String[] regex = new String[] { "COMMON", "�����L��" };
		String format = "%s �ŏI�X�V���t�F%s";
		int[] col = new int[] { 0, 3 };
		String delimiter = ",";
		List<String> resList = ListUtil.limitter(regCol, regex, format, col,
				delimiter, list);
	}

	// -------------------------------------------------------------------------
	// it2�t�@�C���Q���炉�������i�w��i�ږ��t�H���_�j�𐶐�����o�b�`
	// -------------------------------------------------------------------------
	public static void main(String[] argv) {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("# template #");
		elapse.start();
		//---------------------------------------------------------------------
		testLimitter01();
		//---------------------------------------------------------------------
		elapse.stop();
	}

}
