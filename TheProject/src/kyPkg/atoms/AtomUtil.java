package kyPkg.atoms;

import static kyPkg.util.KUtil.join;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import globals.ResControl;
import kyPkg.etc.AliasRes;
import kyPkg.filter.EzWriter;
import kyPkg.uFile.ListArrayUtil;

public class AtomUtil {
	private static final String TAB = "\t";;

	// --------------------------------------------------------------------
	// �s�T�v�t
	// ���j�^�[�̑��������̓��[�U�[�̃��[�J��C:/@qpr/home/Personal/MonSets/�ȉ��Ɋi�[�����
	// ����̏����ȉ��̂悤�Ȍ`���ŁwcurrentP.txt�x�Ɋi�[���Ă���
	// --------------------------------------------------------------------
	// �p�o�q�A���P�[�g/01_�����E���f�B�A��/2014/TABQ1,Q2,Q3,Q4,Q5,Q6,Q7,Q8,Q9,Q10,Q11S1,Q11S2,Q12S1
	// �p�o�q�A���P�[�g/03_�����E���N���/2014/TABA01,A02,A03,A04,A05,A0601,A0602,A0603,A0604,A0605,A0606,A0608,A07,A08
	// --------------------------------------------------------------------
	// ���̏����W�v���Ԃɓ��������邽�߂ɏW�v�Ώۊ��Ԃ̏I���N���x�[�X�ɁwcurrentP.txt�x�����������āA���̂�
	// �����f�[�^�wcurrent.txt�x
	// �������^�f�[�^�wcurrent.atm�x
	// �����ꂼ�ꐶ��������
	// --------------------------------------------------------------------
	// �p�����[�^�N�����ݍ��񂾃t�@�C�����ɏ��������ĕԂ�
	// �၄
	// int baseYear = 2013;
	// String path = "C:/@qpr/home/Personal/MonSets/"+CURRENT_P_TXT
	// ��L�̃p�����[�^�̏ꍇ�ȉ��̂悤�Ȓl���Ԃ�F
	// "C:/@qpr/home/Personal/MonSets/currentP2013.txt";
	// --------------------------------------------------------------------
	public static String getBasePath(String inPath, int baseYear) {
		String[] pathArray = inPath.split("\\.");
		return pathArray[0] + baseYear + ".txt";
	}

	// --------------------------------------------------------------------
	// �R���X�g���N�^
	// --------------------------------------------------------------------
	// ������<<AtomUtil>>������ baseYear :2011
	// <<AtomUtil>> inPath :C:/@qpr/home/Personal/MonSets/currentP.txt
	// <<AtomUtil>> outPath:C:/@qpr/home/Personal/MonSets/currentP2011.txt
	// --------------------------------------------------------------------
	//	public AtomUtil(int baseYear,boolean debug20161209) {
	public AtomUtil(int baseYear) {
		System.out.println("������������������������������");
		System.out.println("���@�@�b�t�q�q�d�m�s�t�@�C���Đ����@��");
		System.out.println("������������������������������");

		System.out.println("������<<AtomUtil>>������ baseYear :" + baseYear);
		String inPath = ResControl.getCurrentP_TXT();
		String paramPath = AtomUtil.getBasePath(inPath, baseYear);
		System.out.println("<<AtomUtil>> inPath :" + inPath);
		System.out.println("<<AtomUtil>> paramPath:" + paramPath);
		// --------------------------------------------------------------------
		// �e�s����A�s�I�u�W�F�N�g������i�e�s���I�u�W�F�N�g�\���������̂ɕϊ�����j
		// --------------------------------------------------------------------
		// currentP.txt�̒��g�́�������ʁ��@���^�u���@���t�B�[���h�f�B�X�N���v�^���̂悤�ɂȂ��Ă���@�ȉ��ɗ�������@
		// �p�o�q�A���P�[�g/03_�����E���N���/2012/�{TAB�{A01,A02,A03,A04,A05,A0601,A0602,A0603,A0604�E�E�E
		// --------------------------------------------------------------------
		String[] array = ListArrayUtil.file2Array(inPath);
		List<LineObj> objList = new ArrayList();
		for (String element : array) {
			//			System.out.println("element:"+element);
			LineObj lineObj = new LineObj(element);
			objList.add(lineObj);
		}
		// --------------------------------------------------------------------
		// ��ԑ傫���N��������i�ǂ̗v�f���N�������Ȃ��P�[�X���l������E�E�E�j
		// --------------------------------------------------------------------
		int maxYear = 0;
		for (LineObj lineObj : objList) {
			Integer wYear = lineObj.getYear();
			if (wYear != null && maxYear < wYear)
				maxYear = wYear;
		}
		//		System.out.println("maxYear:"+maxYear);
		// --------------------------------------------------------------------
		// �ő�N�Ƃ̍����Z�b�g����
		// --------------------------------------------------------------------
		List<String> resList = new ArrayList();
		for (LineObj lineObj : objList) {
			lineObj.setDiff(maxYear);
			System.out.println(
					"lineObj.getLine(baseYear)=>" + lineObj.getLine(baseYear));
			resList.add(lineObj.getLine(baseYear));
		}
		// --------------------------------------------------------------------
		// �w��p�����[�^�̔N���x�[�X�ɂ����p�����[�^�t�@�C�����o�͂���
		// --------------------------------------------------------------------
		EzWriter.list2File(paramPath, resList);
		// --------------------------------------------------------------------
		// �������������e��current.txt���X�V����
		// �o�͂����p�����[�^�����Ƃɑ����t�@�C�����Đ������č�\������
		// --------------------------------------------------------------------
		HashMap<String, List> fieldMap = AliasRes.paramFile2HashMap(paramPath);
		String xxxx = String.valueOf(baseYear);
		AliasRes.createCurrentData(fieldMap, xxxx);
	}

	// ########################################################################
	// �s�����N���X�t
	// ########################################################################
	class LineObj {
		private static final String SEP = "/";
		private Integer diff = 0;
		private Integer year;
		private String[] elements;
		private String[] spliteds;

		// --------------------------------------------------------------------
		// �s�����N���X�t�R���X�g���N�^
		// ������F�p�o�q�A���P�[�g/01_�����E���f�B�A��/2014/ TAB Q1,Q2,Q3,Q4,Q5,Q6,Q7,Q8
		// --------------------------------------------------------------------
		public LineObj(String element) {
			elements = element.split(TAB);
			if (element.length() > 0) {
				pickUpYearElement(elements[0]);
			}
		}

		// --------------------------------------------------------------------
		// �p�X�����񒆂̔N�ɓ�����v�f�����o���i��ԍŌ�̗v�f��4�P�^�̔N���ǂ�������j
		// ������F�p�o�q�A���P�[�g/01_�����E���f�B�A��/2014/
		// --------------------------------------------------------------------
		private void pickUpYearElement(String srcDir) {
			spliteds = srcDir.split(SEP);
			String lastElement = spliteds[spliteds.length - 1];
			// ���K�\����4�P�^�̔N���ǂ�������
			if (kyPkg.uRegex.Regex.isYear(lastElement)) {
				year = new Integer(lastElement);
			} else {
				year = null;
			}
		}

		// --------------------------------------------------------------------
		// �ő�N�Ƃ̍���diff�ɃZ�b�g����
		// --------------------------------------------------------------------
		public void setDiff(int maxYear) {
			if (year != null) {
				this.diff = year - maxYear;
			} else {
				this.diff = null;
			}
		}

		// --------------------------------------------------------------------
		// �N�ɂ�����v�f������΂����Ԃ�
		// --------------------------------------------------------------------
		public Integer getYear() {
			return year;
		}

		// --------------------------------------------------------------------
		// �x�[�X�N�ɏ��������ĕԂ��i���Ƃ��ƔN�̗v�f�ł͂Ȃ��ꍇ�͂��̂܂܉������Ȃ��j
		// --------------------------------------------------------------------
		public String getLine(int baseYear) {
			if (diff != null) {
				spliteds[spliteds.length - 1] = String
						.valueOf((baseYear - diff));
			}
			return join(spliteds, SEP) + SEP + TAB + elements[1];
		}
	}

	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] argv) {
		test01();
	}

	// ------------------------------------------------------------------------
	// �W�v���O�ɉ]�X
	// ��N�ɍ��킹�ăp�����[�^�����������邪�A���Y�N�ɑ��݂��Ȃ��p�����[�^�����݂���ׂɃG���[�ƂȂ��Ă��܂�
	// ------------------------------------------------------------------------
	public static void test01() {
		// �W�v���Ԃɓ��������邽�߁A�W�v�Ώۊ��Ԃ̏I���N���x�[�X�ɁwcurrentP.txt�x������������
		new AtomUtil(2011);
	}

}
