package kyPkg.filter;

// -------------------------------------------------------------------------
// �C���i�[�N���X ParmsObj substr�p�̃p�����[�^
// -------------------------------------------------------------------------
public class ParmsObj {
	private static final String BAR = "|";
	private static final String RETURN = "���s";
	private static final String SPACE = "�X�y�[�X";
	private static final String TAB = "�^�u";
	private static final String COMMA = "�J���}";
	private int col = -1;
	private int start = -1;
	private int len = -1;
	private int[] parmi;

	private String comment = "";//�R�����g�̓w�b�_�[�ɗ��p�����ꍇ������
	private String filterName = " ";
	private String param = "";//�t�B���^�[�ɗ^����p�����[�^
	private String[] paramArray;
	private String signature = " ";//�t�B���^�[����ӂɔ��ʂ���ׁi�����t�B���^�[�ł��p�����[�^�ɂ���ċ������ς��ׁj

	public String getSignature() {
		return signature;
	}

	public String getParam() {
		return param;
	}

	// ---------------------------------------------------------------------
	// �A�N�Z�b�T
	// ---------------------------------------------------------------------
	public String getFilterName(boolean debug) {
		return filterName;
	}

	public int getCol() {
		return col;
	}

	public int getStart() {
		return start;
	}

	public int getLen() {
		return len;
	}

	public String getParm(int n) {
		if (0 <= n && n < paramArray.length)
			return paramArray[n];
		return null;
	}

	public int getParmi(int n) {
		if (0 <= n && n < parmi.length)
			return parmi[n] - 1;
		return -1;
	}

	public String getComment(boolean debug) {
		return comment;
	}

	// ---------------------------------------------------------------------
	// �R���X�g���N�^
	// ---------------------------------------------------------------------
	// parm �́��p�����[�^ + �^�u + �R�����g
	// jackUp�́A�p�����[�^�ɗ������Ă��鉺�� �C���f�b�N�X�w�肾�ƌ����ڂ킩��Â炢
	// new ParmsObj("1,,2"); �����̏ꍇ�~�ς����ׂ����낤�E�E�E
	// ---------------------------------------------------------------------
	public ParmsObj(Object parm) {
		parse(parm.toString());
	}

	private void parse(String parm) {
		col = -1;
		start = -1;
		len = -1;
		filterName = "";
		comment = "";
		param = "";
		paramArray = new String[] { "" };

		String[] splitedByTAB = parm.split("\t"); // �^�u�ȍ~�̓R�����g�����Ƃ���
		if (splitedByTAB.length >= 2) {
			comment = splitedByTAB[1];
		}
		splitedByTAB[0] = splitedByTAB[0] + ",,, ";// �p�����[�^�������w�肳��Ă��Ȃ��Ƃ���z��
		String[] splitedByComma = splitedByTAB[0].split(","); // �p�����[�^�̓J���}��؂�
		if (splitedByComma[0].equals("@")) {
			filterName = "@";// �Œ蕶����
			if (splitedByComma.length > 1) {
				param = splitedByComma[1];
			}
		} else {
			if (splitedByComma[0].equals("D")) {
				filterName = "D";// ��؂蕶��
				if (splitedByComma[1].equals("")) {
					param = "\t";
				} else if (splitedByComma[1].equals(COMMA)) {
					param = ",";
				} else if (splitedByComma[1].equals(TAB)) {
					param = "\t";
				} else if (splitedByComma[1].equals(SPACE)) {
					param = " ";
				} else if (splitedByComma[1].equals(RETURN)) {
					param = "\n";
				} else if (splitedByComma[1].equals(BAR)) {
					param = ",";
				}
			} else {
				if (splitedByComma[0].matches("\\d+")) {
					filterName = "";
					col = Integer.parseInt(splitedByComma[0]) - 1;//�����ΏۂƂ���J����
					if (splitedByComma.length > 1) {
						if (!splitedByComma[1].equals("*")) {
							if (splitedByComma.length > 1
									&& splitedByComma[1].matches("\\d+")) {
								start = Integer.parseInt(splitedByComma[1]) - 1;
							}
							if (splitedByComma.length > 2
									&& splitedByComma[2].matches("\\d+")) {
								int endPos = Integer
										.parseInt(splitedByComma[2]);
								len = (endPos - start);
							}
							// if (startPos < 0 && endPos >= 0)
							if (start < 0 && len >= 0)
								start = 0;
							if (splitedByComma.length > 3)
								filterName = splitedByComma[3];//�t�B���^�[����
							if (splitedByComma.length > 4) {
								if (!splitedByComma[4].trim().equals("")) {
									param = splitedByComma[4];//�t�B���^�[�p�p�����[�^
									paramArray = param.split(":");
									if (paramArray.length > 0) {
										// ���x���x�p�[�X����I�[�o�[�w�b�h������E�Eint�z���p��
										parmi = new int[paramArray.length];
										for (int i = 0; i < paramArray.length; i++) {
											try {
												parmi[i] = Integer
														.parseInt(getParm(i));
											} catch (NumberFormatException e) {
												parmi[i] = -1; // ���l����Ȃ����̂�-1
											}
										}
									} else {
										parmi = null;
									}
								}
							}
						}
					}
				} else {
					System.out.println("#Error@ParmsObject  Parm:" + parm);
				}
			}
		}
		signature = filterName + param;

	}

} // end of class ParmsObj
