package kyPkg.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

// -------------------------------------------------------------------------
// �C���i�[�N���X ParmsObj substr�p�̃p�����[�^
// -------------------------------------------------------------------------
public class RegChecker {
	private List<FilterParam> filterList = null;

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public RegChecker() {
		filterList = new ArrayList();
	}

	public RegChecker(int col, int pos, int len, String regex, int stat) {
		this();
		addFilter(col, pos, len, regex, stat);
	}

	public RegChecker(int col, int pos, int len, String regex, int stat,
			boolean flag) {
		this();
		addFilter(col, pos, len, regex, stat, flag);
	}

	// ------------------------------------------------------------------------
	// . �C�ӂ̕�����\�킷�i�s���L���ƃ}�b�`����ꍇ������񂾂����E�E�j
	// .* �Ȃɂ������������������i�E�E�X�y�[�X�������������H�j
	// \\( ����(��\�킷
	// \\) ����)��\�킷
	// []��() ���ꂩ��*�Ȃǂ͓���ȕ����Ȃ̂ŋC��t����I�I
	//
	// �� �����N���X �\�� �}�b�`�Ώ�
	// [ABC] A,B,C�̂����ꂩ1����
	// [A-Z] A����Z�܂ł̂����ꂩ1����
	// [A-Za-z0-9] A����Z, a����z, 0����9�܂ł̂����ꂩ1����
	// [^ABC] A,B.C�ȊO�̕���
	// [^A-Z] A����Z�ȊO�̕���
	//
	// �၄Jan�R�[�h���ǂ���
	// \s*[0-9]+\s* jan?
	//
	// \w �p�������B[a-zA-Z0-9]�Ɠ��l
	// \W \w�ȊO�̕���
	// \d ���l�����B[0-9]�Ɠ���
	// \D \d�ȊO�̕���
	// \s �󔒕���
	// \S \s�ȊO�̕���
	// \n ���s����
	//
	// �� �J��Ԃ� �\�� �}�b�`�Ώ�
	// A+ 1�ȏ�A������A(A, AA, AAA, ...)
	// A* 0�ȏ�A������A( , A, AA, AAA, ...)
	// A? 0�܂���1�̔C�ӕ���( , A, B, C, ...)
	// A{5} 5��J��Ԃ��B AAAAA�Ɠ���
	// A{3,} 3��ȏ�J��Ԃ��B AAA+�Ɠ���
	// A{3,5} 3��ȏ�5��ȉ��J��Ԃ��B AAAA?A?�Ɠ���
	//
	// �� �ʒu�w�� �\�� �}�b�`�Ώ�
	// ^ �s�̐擪
	// $ �s�̖���
	// ------------------------------------------------------------------------
	// �J�����A�J�n�ʒu�A�����A�u�[���l�A�ݒ�X�e�[�^�X�l�A���W�b�N�X�p�^�[��
	// ------------------------------------------------------------------------
	public void addFilter(int col, int pos, int len, String regex, int stat) {
		filterList.add(new FilterParam(col, pos, len, regex, stat, true));
	}

	public void addFilter(int col, int pos, int len, String regex, int stat,
			boolean flag) {
		filterList.add(new FilterParam(col, pos, len, regex, stat, flag));
	}

	// -------------------------------------------------------------------------
	// �e�L�X�g�݂̂Ńp�����[�^�ݒ肵�����ꍇ���l������
	// -------------------------------------------------------------------------
	private class FilterParam {
		private int col = 0; // �ΏۃJ����

		private int start = 0; // �J�n�ʒu

		private int len = 0; // ����

		private Pattern pattern = null;// �����񌟍��p�^�[��

		private boolean flag = true; // ���������Ƀ}�b�`����ꍇstatus���Z�b�g����Ȃ�Ȃ�True��������"T"

		private int stat = 0; // 1,2,4,8,16,32,64�ȂǕ����\�Ȓl

		// ---------------------------------------------------------------------
		// �R���X�g���N�^
		// regex �ɃJ���}���܂܂������ꍇ�̓f���~�^�[�������Ɏ�������
		// ---------------------------------------------------------------------
		private FilterParam(int col, int pos, int len, String regex, int stat,
				boolean flag) {
			this.col = col;
			this.start = pos;
			this.len = len;
			this.flag = flag;
			this.stat = stat;
			pattern = Pattern.compile(regex);
		}

		// ---------------------------------------------------------------------
		// �A�N�Z�b�T
		// ---------------------------------------------------------------------
		public int getCol() {
			return col;
		}

		public int getStart() {
			return start;
		}

		public int getEnd() {
			return start + len;
		}

		public Pattern getPattern() {
			return pattern;
		}

		public boolean getFlag() {
			return flag;
		}

		public int getStat() {
			return stat;
		}

	} // end of class ParmsObj

	// ------------------------------------------------------------------------
	// �p�^�[���Ƀ}�b�`�����]���l�istat�̍��v�j��Ԃ�
	// ------------------------------------------------------------------------
	public int checkIt(String rec, String delimiter) {
		return checkIt(rec.split(delimiter));
	}

	public int checkIt(String[] array) {
		String wCel = "";
		String wStr = "";
		int status = 0;
		for (FilterParam filter : filterList) {
			int col = filter.getCol();
			int startPos = filter.getStart();
			int endPos = filter.getEnd();
			Pattern pattern = filter.getPattern();
			// String regex = filter.getRegex();
			if (filter != null && array.length > col) {
				wCel = array[col];
				if (wCel.length() >= startPos) {
					if (endPos > 0 && wCel.length() > endPos) {
						wStr = wCel.substring(startPos, endPos);
					} else {
						wStr = wCel.substring(startPos);
					}
					// if (wStr.matches(filter.getRegex()) == filter.getFlag())
					if (pattern.matcher(wStr).matches() == filter.getFlag()) {
						status += filter.getStat();
					}
				}
			}
		}
		return status;// 1,2,4,8,16
	}

	// ########################################################################
	// main
	// ########################################################################
	public static void main(String[] argv) {
		test01();
		test02();
	}

	// ------------------------------------------------------------------------
	// test01
	// ------------------------------------------------------------------------
	public static void test01() {
		String[] rec = { " * DATA BASE REPORT                ",
				" * CONTENTS OF DATABASE            ",
				" * FILE OPTIONS                    ",
				" * FILE SPACE ALLOCATIONS          ",
				" * PHYSICAL LAYOUT OF THE DATABASE ", " OTHER ", };

		RegChecker checker = new RegChecker();
		checker.addFilter(0, 0, 0, "^\\s\\* DATA BASE REPORT .*", 16);
		checker.addFilter(0, 0, 0, "^\\s\\* CONTENTS OF DATABASE .*", 8);
		checker.addFilter(0, 0, 0, "^\\s\\* FILE OPTIONS .*", 4);
		checker.addFilter(0, 0, 0, "^\\s\\* FILE SPACE ALLOCATIONS .*", 2);
		checker.addFilter(0, 0, 0, "^\\s\\* PHYSICAL LAYOUT OF THE DATABASE .*",
				1);
		// filter.addFilter(0, 0, 0, ".*", 32);
		// filter.addFilter(0, 0, 0, "^1\\s*", 0);
		// FilterParams ins = new FilterParams(filter);
		for (int i = 0; i < rec.length; i++) {
			System.out.println(rec[i] + "=>" + checker.checkIt(rec[i], "\t"));
		}
	}

	// ------------------------------------------------------------------------
	// test02
	// ------------------------------------------------------------------------
	public static void test02() {
		String[] rec = {
				"71211886	200711269999123101054000000101080909122170121253151081400",
				"71226695	200711269999123120303110000105010705222130111133232042200",
				"71226710	200711269999123101034100100111060606122050141439131051300",
				"71227570	200711269999123120304110010102010206222130111138232052300",
				"71230060	200711269999123100004000000115020406111140131335131051300",
				"71230648	200711269999123120303110000105010605222130141430232042201",
				"71233302	200711269999123101035111000101030406122020111137131051300",
				"71234127	200711269999123101052000000111030309111070131352151081400",
				"71241123	200711269999123111051000000105030310231070242455252092400",
				"71244109	200711269999123120403100010105010807222130111144242062300", };

		RegChecker checker = new RegChecker();
		// �J�����A�J�n�ʒu�i�O���j�A�����A�u�[���l�A�ݒ�X�e�[�^�X�l�A���W�b�N�X�p�^�[��
		checker.addFilter(0, 0, 1, "^7.*", 16);
		checker.addFilter(1, 54, 1, "3", 1);

		for (int i = 0; i < rec.length; i++) {
			System.out.println(rec[i] + " stat=> "
					+ checker.checkIt(rec[i], "\t"));
		}
	}

}
