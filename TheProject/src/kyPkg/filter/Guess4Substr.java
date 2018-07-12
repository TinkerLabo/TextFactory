package kyPkg.filter;

import static kyPkg.converter.Corpus.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Guess4Substr extends Guess {
	private static final String QUESTION = "Question";
	private static final String JICFS = "jicfs";
	private static final String HALF_STRING = "�ݶ�";
	private static final String MULTI_ANS = "multiAns";
	private static final String TIME_STAMP = "timeStamp";
	private static final String WIDE_STRING = "�S�p";
	private static final String NUMERIC = "����";
	private List<String> paramList;

	public List<String> getParamList() {
		return paramList;
	}

	// ------------------------------------------------------------------------
	// �}�g���b�N�X�𕪐͂��āAsubstr�p�̃p�����[�^�𐶐�����v���O����
	// ------------------------------------------------------------------------
	public void autoDetect(String delimType, Vector<Vector> matrix,
			boolean headerOption) {
		paramList = null;
		if (matrix == null || matrix.size() <= 0)
			return;
		paramList = new ArrayList();
		// Header enumerate
		List rows = matrix.get(0);
		int maxCol = rows.size();
		String heads[] = new String[maxCol];
		String guess[] = new String[maxCol];
		Integer maxVal[] = new Integer[maxCol];
		Integer maxLen[] = new Integer[maxCol];
		Integer minLen[] = new Integer[maxCol];
		// --------------------------------------------------------------------
		// �w�b�_�[���̂̏���
		// --------------------------------------------------------------------
		if (headerOption) {
			// �擪�s���w�b�_�[�Ƃ���ꍇ
			for (int col = 0; col < maxCol; col++) {
				heads[col] = rows.get(col).toString();
			}
		} else {
			// �f�t�H���g�̃w�b�_�[���𐶐�
			for (int col = 0; col < maxCol; col++) {
				heads[col] = "#" + String.valueOf(col + 1);
			}
		}
		// --------------------------------------------------------------------
		int skip = 1;
		Object obj;
		for (int col = 0; col < maxCol; col++) {
			guess[col] = "happy!";
			maxVal[col] = -1;
			maxLen[col] = -1;
			minLen[col] = Integer.MAX_VALUE;
			int line = 0;

			for (List rowObj : matrix) {
				line++;
				if (line > skip && rowObj != null) {
					obj = rowObj.get(col);
					if (obj != null) {
						String val = obj.toString().trim();// �X�y�[�X���܂܂Ȃ������ƂȂ�
						int iVal = getMaxVal(val);
						if (maxVal[col] < iVal)
							maxVal[col] = iVal;
						int curLen = val.length();
						if (maxLen[col] < curLen)
							maxLen[col] = curLen;
						if (minLen[col] > curLen)
							minLen[col] = curLen;
						// ���p�����񂩁H
						if (kyPkg.uRegex.Regex.isHalfWidthString(val)) {
							// ���l���H
							if (kyPkg.uRegex.Regex.isNumeric(val)) {
								if (kyPkg.uRegex.Regex.is49Numeric(val)) {
									guess[col] = JICFS;
								} else {
									guess[col] = NUMERIC;
								}
							} else {
								// "^[0-9]{4}\\.[0-9]{2}\\.[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}+$");
								if (kyPkg.uRegex.Regex.isTimeStamp(val)) {
									guess[col] = TIME_STAMP;
								} else {
									if (kyPkg.uRegex.Regex.isMuliAns(val)) {// "^\"[0-9,]*\"$"
										guess[col] = MULTI_ANS;
									} else {
										guess[col] = HALF_STRING;
									}
								}
							}
						} else {
							if (kyPkg.uRegex.Regex.isFullWidthString(val)) {
								guess[col] = WIDE_STRING;
							} else {
								guess[col] = QUESTION;
							}
						}
						// if (col == 9)
						// System.out.println("it's " + guess[col] + " =>"
						// + val);
					}
				}
			}
			String type = "";
			String frm = "1";
			String len = String.valueOf(maxLen[col]);
			String val = "";
			if (col == 9)
				System.out.println("guess[col]=>" + guess[col] + "<=");
			if (guess[col].equals(WIDE_STRING)) {
				type = FIX_WIDE;
			} else {
				if (maxLen[col] == minLen[col]) {
					if (maxLen[col] == 0) {
						// ���ׂċ�i�J�n�ʒu�A�������w�肵�Ă��Ȃ��j
						frm = "";
						len = "";
						type = FIX_HALF;
					} else {
						if (guess[col].equals(TIME_STAMP)) {
							type = DATE_CNV;
						} else {
							if (guess[col].equals(MULTI_ANS)) {
								val = String.valueOf(maxVal[col]);
								type = MULTI_ANS_TO_FLAG2;
							} else {
								type = FIX_HALF;
							}
						}
					}
				} else {
					if (guess[col].equals(NUMERIC)) {
						type = FIX_LEADING_ZERO;
					} else {
						if (guess[col].equals(MULTI_ANS)) {
							val = String.valueOf(maxVal[col]);
							type = MULTI_ANS_TO_FLAG2;
						} else {
							// �����_�������݂���
							// �S�p���p���݁H
							// �����ȊO�̉�
							type = FIX_HALF;
						}
					}
				}
			}
			if (col > 0)
				paramList.add("D," + delimType + "\t");
			String seq = String.valueOf((col + 1));
			// -----------------------------------------------------------------
			// 1,1,13,�Œ蒷���p,\tJan
			// D,�^�u\t
			// 7,1,22,�Œ蒷�S�p,\t��\�p����
			// D,�^�u\t
			// 12,1,7,�Œ�O�[��,\t���i
			// -----------------------------------------------------------------
			paramList.add(seq + "," + frm + "," + len + "," + type + "," + val
					+ "\t" + heads[col]);
		}
	}

	@Override
	public void analyzeIt(String inPath, boolean headOpt) {
		int limit = 20;
		int skip = 0;
		Vector<Vector> matrix = super.getMatrix(inPath, skip, limit);
		// File2Matrix.debugTheMatrix(matrix);// for Debug
		if (matrix == null || matrix.size() == 0) {
			System.out.println("ERROR?! Empty Data?:" + inPath);
			return;
		}

		if (headOpt)
			headOpt = isHeaderExist(matrix);

		String delimType = "�^�u";
		Guess4Substr guess = new Guess4Substr();
		guess.autoDetect(delimType, matrix, headOpt);
	}

	public static void main(String[] argv) {
		String path = "C:/loy1_Head.txt";
		Guess4Substr ins = new Guess4Substr();
		ins.analyzeIt(path, true);
	}

}
