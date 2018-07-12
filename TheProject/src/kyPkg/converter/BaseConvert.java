package kyPkg.converter;

import java.util.ArrayList;
import java.util.List;

public class BaseConvert {
	// -------------------------------------------------------------------------
	// �T�v :�@B A S E C O N V E R T�i��ϊ��j
	// -------------------------------------------------------------------------
	// �����ӁI�I�I�I�I�I�������iI,O�j�����������ƕ���킵�����߂ɊO���Ă���܂�
	// QPR�̃��[�J�[�R�[�h�ϊ����Ɏg�p���܂��B
	// -------------------------------------------------------------------------
	// ���͒l���o�͒l�̊�ɕϊ����o�͒l�֕Ԃ��iPL/1�ō쐬�������̂�VB�ֈڐA=>�����java�ɈڐA�j
	// �����ӏo�͕ϐ��̒����ɍ��킹�ďo�͂���̂ŁA
	// ���炩���߁i�X�y�[�X�Ȃǁj�\���ȃT�C�Y�ŏ��������Ă����B
	// ��������@DES = String(256, " ")
	// -------------------------------------------------------------------------
	// ���� :���͒l�̊�A���͒l�A�o�͒l�̊�A�o�͕ϐ�
	// �߂�l :�Ȃ�
	// -------------------------------------------------------------------------
	// �g�p�� :
	// PROT TYPE BASE34 TO BASE10
	// CALL BCnvQPR(34,I_VAL,10,O_VAL)
	// CALL BCnvQPR(34,"2N9C",10,"000000")�@�f36�i��"2N9C"��10�i���֕ϊ�����
	// CALL BCnvQPR(10, "123456", 36, "00000")
	// -------------------------------------------------------------------------
	// �����F���͒l�̊�A���͒l�A�o�͒l�̊�A�o�͒l�̌���
	// �o�͂̌������������ꍇ�́A���܂邾�����ʂ̌����i�[�����
	// -------------------------------------------------------------------------
	public static String conv(int dstBase, String srcStr, int srcBase,
			int column) {
		long val = 0;
		srcStr = srcStr.toUpperCase();
		char[] srcArray = srcStr.toCharArray();
		char[] dstArray = new char[column];
		int maxCol = srcArray.length - 1;
		for (int i = maxCol; i >= 0; i--) {
			char c = srcArray[i];
			switch (c) {
			case '0':
				val = val + 0 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case '1':
				val = val + 1 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case '2':
				val = val + 2 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case '3':
				val = val + 3 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case '4':
				val = val + 4 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case '5':
				val = val + 5 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case '6':
				val = val + 6 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case '7':
				val = val + 7 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case '8':
				val = val + 8 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case '9':
				val = val + 9 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'A':
				val = val + 10 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'B':
				val = val + 11 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'C':
				val = val + 12 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'D':
				val = val + 13 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'E':
				val = val + 14 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'F':
				val = val + 15 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'G':
				val = val + 16 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'H':
				val = val + 17 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'I':
				val = val + 18 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'J':
				val = val + 19 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'K':
				val = val + 20 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'L':
				val = val + 21 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'M':
				val = val + 22 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'N':
				val = val + 23 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'O':
				val = val + 24 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'P':
				val = val + 25 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'Q':
				val = val + 26 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'R':
				val = val + 27 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'S':
				val = val + 28 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'T':
				val = val + 29 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'U':
				val = val + 30 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'V':
				val = val + 31 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'W':
				val = val + 32 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'X':
				val = val + 33 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'Y':
				val = val + 34 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			case 'Z':
				val = val + 35 * (int) (Math.pow(srcBase, (maxCol - i)));
				break;
			default:
				break;
			}
		}
		// System.out.println("val:"+val);
		List<Integer> list = new ArrayList();
		while (val >= dstBase) {
			int mod = (int) (val % dstBase);
			// System.out.println("list.add>"+val+" mod "+dstBase+" =>"+mod);
			list.add(mod);
			val = val / dstBase;
		}
		for (int i = 0; i < dstArray.length; i++) {
			dstArray[i] = '0';
		}
		int seq = dstArray.length - 1;
		// System.out.println("list.add>"+val);
		list.add((int) val);
		for (int index = 0; index < list.size(); index++) {
			int wInt = (int) list.get(index);
			if (seq >= 0) {
				switch (wInt) {
				case 0:
					dstArray[seq--] = '0';
					break;
				case 1:
					dstArray[seq--] = '1';
					break;
				case 2:
					dstArray[seq--] = '2';
					break;
				case 3:
					dstArray[seq--] = '3';
					break;
				case 4:
					dstArray[seq--] = '4';
					break;
				case 5:
					dstArray[seq--] = '5';
					break;
				case 6:
					dstArray[seq--] = '6';
					break;
				case 7:
					dstArray[seq--] = '7';
					break;
				case 8:
					dstArray[seq--] = '8';
					break;
				case 9:
					dstArray[seq--] = '9';
					break;
				case 10:
					dstArray[seq--] = 'A';
					break;
				case 11:
					dstArray[seq--] = 'B';
					break;
				case 12:
					dstArray[seq--] = 'C';
					break;
				case 13:
					dstArray[seq--] = 'D';
					break;
				case 14:
					dstArray[seq--] = 'E';
					break;
				case 15:
					dstArray[seq--] = 'F';
					break;
				case 16:
					dstArray[seq--] = 'G';
					break;
				case 17:
					dstArray[seq--] = 'H';
					break;
				case 18:
					dstArray[seq--] = 'I';
					break;
				case 19:
					dstArray[seq--] = 'J';
					break;
				case 20:
					dstArray[seq--] = 'K';
					break;
				case 21:
					dstArray[seq--] = 'L';
					break;
				case 22:
					dstArray[seq--] = 'M';
					break;
				case 23:
					dstArray[seq--] = 'N';
					break;
				case 24:
					dstArray[seq--] = 'O';
					break;
				case 25:
					dstArray[seq--] = 'P';
					break;
				case 26:
					dstArray[seq--] = 'Q';
					break;
				case 27:
					dstArray[seq--] = 'R';
					break;
				case 28:
					dstArray[seq--] = 'S';
					break;
				case 29:
					dstArray[seq--] = 'T';
					break;
				case 30:
					dstArray[seq--] = 'U';
					break;
				case 31:
					dstArray[seq--] = 'V';
					break;
				case 32:
					dstArray[seq--] = 'W';
					break;
				case 33:
					dstArray[seq--] = 'X';
					break;
				case 34:
					dstArray[seq--] = 'Y';
					break;
				case 35:
					dstArray[seq--] = 'Z';
					break;
				default:
					System.out.println("ERROR?!@makerConvert");
					dstArray[seq--] = '?';
					break;
				}
			}
		}
		return new String(dstArray);
	}

}
