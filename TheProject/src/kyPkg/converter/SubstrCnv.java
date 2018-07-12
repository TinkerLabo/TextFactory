package kyPkg.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import globals.ResControl;
import kyPkg.filter.ParmsObj;
import kyPkg.filter.TextFactory;
import kyPkg.uCodecs.CharConv;
import kyPkg.uDateTime.DateCalc;
import kyPkg.uDateTime.Holidays;
import kyPkg.uFile.File2Matrix;

// python PIL 
// encode
//	XXX �����p�����[�^���w��ł��Ȃ��o�O���C��
//	XXX �p�����[�^�[��}��������
//	XXX �t�B���^�[���p�C�v�����ł���Ƃ��ꂵ���ˁE�E�E���[��
//	XXX ������ʃX���b�h��
//	XXX �������v���O�C�����i�O���N���X���j
//	XXX �����g���q�̃f�[�^�ɂ��Ĉꊇ�ϊ��ł���悤�ɂ��������I�H�i��؂��񂪋ꂵ��ł����̂ŁE�E�E�j
//	XXX �t�B���^�𕪊��������i�X�̃t�B���^���v���O�C���Ƃ��Ĉ�����悤�ɏ������������j
//	XXX ���K�\�����Ճ��R���p�C�����Č������グ����

//2009/03/17 yuasa
//2015/05/25 yuasa �t�B���^�̃C���X�^���X���}�b�v���H�i�C���X�^���X�̏������Ɋւ����肪�Ȃ����C�ɂȂ邪�E�E�E���ꂼ��̃t�B���^�ɏ��������[�`����݂��邩�H�j
public class SubstrCnv extends Corpus implements Inf_LineConverter {
	private String[] array = null;
	private List list = null;
	private StringBuffer buf;
	private String cel;
	private String prefix = "";
	private String suffix = "";
	private CharConv charConv;
	private String today;
	private String firstSunday;
//	private DecimalFormat df00 = new DecimalFormat("00");
//	private SimpleDateFormat dformat;

	private List<ParmsObj> params;
	private HashMap<String, Inf_Converter> cnvMap = new HashMap();
	private HashMap<String, Inf_FilterFactory> filterMap;//20170626

	private int getLength() {
		if (list == null) {
			return array.length;
		} else {
			return list.size();
		}
	}

	@Override
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public SubstrCnv() {
		init();
	}

	public SubstrCnv(List paramList) {
		this();
		parseParam(paramList);
	}

	public SubstrCnv(String[] paramArray) {
		this();
		parseParam(Arrays.asList(paramArray));
	}

	public SubstrCnv(String paramPath) {
		this();
		if (paramPath.indexOf(",") > 0) {
			// �_�C���N�g�Ƀp�����[�^���w�肳��Ă���
			parseParam(Arrays.asList(new String[] { paramPath }));
		} else {
			// �p�����[�^�Ƀp�X���w�肳��Ă���
			parseParam(File2Matrix.extract(paramPath, (String) null));
		}
	}

	// -------------------------------------------------------------------------
	// ������
	// -------------------------------------------------------------------------
	@Override
	public void init() {
		today = DateCalc.getToday();
		firstSunday = Holidays.getThisFirstSunday();
//		dformat = kyPkg.uDateTime.DateUtil.getSimpleDateFormat("yyyyMMdd");
		buf = new StringBuffer();
		charConv = CharConv.getInstance();
		// --------------------------------------------------------------------
		//20170628
		// --------------------------------------------------------------------
		filterMap = new HashMap();
		filterMap.put(DictconvertFactory.TITLE, new DictconvertFactory());
		filterMap.put(ReplaceFactory.TITLE, new ReplaceFactory());
		filterMap.put(TranslateFactory.TITLE, new TranslateFactory());
		filterMap.put(RangeConvertFactory.TITLE, new RangeConvertFactory());
	}

	public void parseParam(List paramList) {
		cnvMap = new HashMap();
		params = new ArrayList();
		for (int i = 0; i < paramList.size(); i++) {
			ParmsObj pObj = new ParmsObj(paramList.get(i));
			String filter = pObj.getFilterName(true);
			String param = pObj.getParam();
			String signature = pObj.getSignature();
			Inf_FilterFactory factory = filterMap.get(filter);
			if (factory != null)
				cnvMap.put(signature, factory.getConverter(param));
			params.add(pObj);
		}
	}

	@Override
	public String getHeader() {
		buf.delete(0, buf.length());
		buf.append(prefix);
		for (int i = 0; i < params.size(); i++) {
			ParmsObj pObj = params.get(i);
			if (pObj.getFilterName(true).equals("D")) {
				cel = pObj.getParam();
			} else {
				cel = pObj.getComment(true);
				cel = cel.replaceAll(":", "\t");//TODO �^�u�Ő؂��Ă���E�E�E��肠��H�I�����̃Z���𔭐�����ꍇ�A�����̃w�b�_�[���b�ŋ�؂�H
				if (cel.equals("")) {
					cel = "#col" + (i + 1);
				}
			}
			buf.append(cel);
		}
		buf.append(suffix);
		return buf.toString();
	}

	private void setList(List list) {
		this.list = list;
		this.array = null;
	}

	private void setArray(String[] array) {
		this.array = array;
		this.list = null;
	}

	@Override
	public String[] convert(String[] pArray, int lineNumber) {
		if (pArray == null)
			return null;
		setArray(pArray);
		return new String[] { convert(lineNumber) };
	}

	@Override
	public String convert2Str(String[] pArray, int lineNumber) {
		if (pArray == null)
			return null;
		setArray(pArray);
		return convert(lineNumber);
	}

	@Override
	public String convert2Str(List list, int lineNumber) {
		if (list == null)
			return null;
		setList(list);
		return convert(lineNumber);
	}

	private String colGet(int col) {
		if (list == null) {
			return array[col];
		} else {
			return (String) list.get(col);
		}
	}

	private String convert(int lineNumber) {
		cel = "";
		buf.delete(0, buf.length());
		buf.append(prefix);
		for (int i = 0; i < params.size(); i++) {
			cel = "";
			ParmsObj pObj = params.get(i);
			String filter = pObj.getFilterName(true);
			//			String parm = pObj.getParms(0);
			String parm = pObj.getParam();
			if (filter.equals("D")) { // ��؂蕶��
				cel = parm;
			} else if (filter.equals("@")) {// �Œ蕶����
				parm = parm.toUpperCase();
				if (parm.equals("@SEQ")) {
					cel = String.valueOf(lineNumber + 1);
				} else if (parm.equals("@TODAY")) {
					cel = today;
				} else if (parm.equals("@FIRSTSUNDAY")) {
					cel = firstSunday;
				} else {
					cel = parm;
				}
			} else {
				// substring����
				int col = pObj.getCol();
				if (0 <= col && col < getLength()) {
					String data = colGet(col);
					int start = pObj.getStart();
					int end = start + pObj.getLen();
					if (start < 0) {
						cel = data;
					} else {
						if (start < data.length()) {
							if (end >= 0) {
								// �����炸��������E�E�E���̂܂܂̒���
								if (end > data.length())
									end = data.length();
								cel = data.substring(start, end);
							} else {
								cel = data.substring(start);
							}
						}
					}
				}
				// �t�B���^�[����
				cel = convertIT(pObj, cel);
			}
			// �Y���p�����[�^�����݂��Ȃ��ꍇ�͂��̂܂܂��̃Z�����o�͂����̂��H�H
			buf.append(cel);
		}
		buf.append(suffix);
		return buf.toString();
	}

	// XXX ������if���ł͂Ȃ��n�b�V���}�b�v�ŏ�������
	private String convertIT(ParmsObj pObj, String cel) {
		String filter = pObj.getFilterName(true);
		int len = pObj.getLen();
		String signature = pObj.getSignature();
		if (filter.equals("")) {
			return cel;
		} else if (filter.equalsIgnoreCase(TRIM)) {
			cel = cel.trim();
		} else if (filter.equalsIgnoreCase(UPPER_CASE)) {
			cel = cel.toUpperCase();
		} else if (filter.equalsIgnoreCase(LOWER_CASE)) {
			cel = cel.toLowerCase();
		} else if (filter.equalsIgnoreCase(TO_WIDE)) {
			cel = charConv.cnvWide(cel);
		} else if (filter.equalsIgnoreCase(TO_HALF)) {
			cel = charConv.cnvNarrow(cel);
		} else if (filter.equalsIgnoreCase(FIX_LEN)) {
			cel = CharConv.fixStr(cel, len);
		} else if (filter.equalsIgnoreCase(UPPER_CASE_EX)) {
			cel = cel.toUpperCase();
			cel = charConv.cnvK2N(cel); // �ł�host�p�Ƀm�[�}���C�Y����
			cel = CharConv.fixStr(cel, len);
		} else if (filter.equalsIgnoreCase(FIX_HALF)) { // �Œ蒷���p
			cel = charConv.cnvFixHalf(cel, len);
		} else if (filter.equalsIgnoreCase(FIX_WIDE)) { // �Œ蒷�S�p
			cel = charConv.cnvFixWide2(cel, len);
		} else if (filter.equalsIgnoreCase(FIX_LEADING_ZERO)) {
			cel = CharConv.fixRight(cel.trim(), len, '0');
		} else if (filter.equalsIgnoreCase(FIX_LEADING_SPACE)) {
			cel = CharConv.fixRight(cel.trim(), len, ' ');
		} else if (filter.equalsIgnoreCase(MULTI_ANS_TO_FLAG)) {
			cel = ValueChecker.cnvMultiAns(cel, pObj.getParmi(0) + 1);
		} else if (filter.equalsIgnoreCase(MULTI_ANS_TO_FLAG2)) {
			cel = ValueChecker.cnvMultiAns(cel, pObj.getParmi(0) + 1);
		} else if (filter.equalsIgnoreCase(DATE_CNV)) {
			cel = ValueChecker.cnvYmd(cel);
		} else if (filter.equalsIgnoreCase(DATE_CNV6)) {
			cel = ValueChecker.cnvYmd6(cel);
		} else if (filter.equalsIgnoreCase(PATTERN_MATCH)) {
			// �p�����[�^ regix :match�Ԓl:Unmatch�Ԓl
			// ��> [123]:1:0 1or2or3=> 1 other => 0
			// String otherParm = pObj.getOtherParm();
			cel = ValueChecker.regcheknCnv(cel, pObj.getParm(0),
					pObj.getParm(1), pObj.getParm(2));
		} else {
			Inf_Converter cnv = cnvMap.get(signature);
			if (cnv != null)
				cel = cnv.convert(cel, null);
			//-----------------------------------------------------------------
			// XXX�@2010-08-03 reflector���g���ĊO���N���X���w��ł���悤�ɂ�����
			// XXX�@�N���X�� �p�^�[�� ���t�@�C������ǂݍ���
			// XXX�@���\�b�h�̃V�O�j�`�����������l������
			//-----------------------------------------------------------------
		}
		return cel;
	}

	@Override
	public String convert2Str(String str) {
		return convert2Str(new String[] { str }, 0);
	}

	@Override
	public void fin() {
	}

	public static void util() {
	}

	public List<String> getFilters() {
		List<String> filterNames = new ArrayList();
		// �p�k�̃C���f���g���ق����ˁE�E�E
		filterNames.add("");
		filterNames.add(TRIM);
		filterNames.add(FIX_LEN);
		filterNames.add(FIX_HALF);
		filterNames.add(FIX_WIDE);
		filterNames.add(TO_HALF);
		filterNames.add(TO_WIDE);
		filterNames.add(UPPER_CASE);
		filterNames.add(UPPER_CASE_EX);
		filterNames.add(LOWER_CASE);
		filterNames.add(FIX_LEADING_ZERO);
		filterNames.add(MULTI_ANS_TO_FLAG);
		filterNames.add(MULTI_ANS_TO_FLAG2);
		filterNames.add(DATE_CNV);
		filterNames.add(PATTERN_MATCH); // �p�^�[���}�b�`�ϊ� 2008-01-18
		filterNames.add(DATE_CNV6);
		// filterNames.add(OLD_CATEGORIZE1);
		// filterNames.add(OLD_CATEGORIZE2);
		// --------------------------------------------------------------------
		List<String> filterNames2 = new ArrayList(filterMap.keySet());// 20170628
		for (String filterName : filterNames2) {
			filterNames.add(filterName);
		}
		return filterNames;
	}

	public String getSample(String filter) {
		String sample = "";
		if (filter.equals(Corpus.PATTERN_MATCH)) {
			// ��> [123]:1:0 1or2or3=> 1 other => 0
			//		} else if (filter.equals(Translate.TITLE)) {
			//			txtOther.setText(Translate.SAMPLE);
		} else {
			Inf_FilterFactory factory = filterMap.get(filter);
			if (factory != null)
				sample = factory.getSample();
		}
		return sample;
	}

	//TODO	�t�B���^�[�I�u�W�F�N�g���Ƃɉ�����������Ă�������
	public String getExplain(String filter) {
		String explain = "";
		if (filter.equals(TRIM)) {
			explain = "���ڂ̑O��̋󔒂���菜���܂�";
		} else if (filter.equals(UPPER_CASE)) {
			explain = "�啶���ɕϊ����܂�";
		} else if (filter.equals(LOWER_CASE)) {
			explain = "�������ɕϊ����܂�";
		} else if (filter.equals(TO_WIDE)) {
			explain = "�S�p�����ɕϊ����܂�";
		} else if (filter.equals(TO_HALF)) {
			explain = "�\�Ȍ��蔼�p�����ɕϊ����܂�";
		} else if (filter.equals(FIX_LEN)) {
			explain = "�Œ蒷������ɕϊ����܂��A�J�n�I���ʒu�ɂ�蕶���̒��������߂܂�";
		} else if (filter.equals(FIX_HALF)) {
			explain = "�Œ蒷���p������ɕϊ����܂��A�J�n�I���ʒu�ɂ�蕶���̒��������߂܂�";
		} else if (filter.equals(FIX_WIDE)) {
			explain = "�Œ蒷���p������ɕϊ����܂��A�J�n�I���ʒu�ɂ�蕶���̒��������߂܂�";
		} else if (filter.equals(FIX_LEADING_ZERO)) {
			explain = "���p������̐擪��0�𖄂ߍ���ŌŒ蒷�ɁA�J�n�I���ʒu�ɂ�蕶���̒��������߂܂�";
		} else if (filter.equals(MULTI_ANS_TO_FLAG)) {
			explain = "�}���`�A���T�[���ڂ�1or�X�y�[�X�ɕϊ����܂��Aother�ɏo�͒������w�肵�܂�";
		} else if (filter.equals(MULTI_ANS_TO_FLAG2)) {
			explain = "�}���`�A���T�[���ڂ�1or�X�y�[�X�ɕϊ����܂��Aother�ɏo�͒������w�肵�܂��B���ʂ̓N�I�[�e�[�V�����ň͂܂�܂�";
		} else if (filter.equals(PATTERN_MATCH)) {
			explain = "�����P�Ƀp�^�[���}�b�`�������̂Ɉ����Q���A����ȊO�͈����R���A�T�C������@��>�@[123]:1:0   1or2or3=> 1 other => 0";
		} else if (filter.equals(DATE_CNV)) {
			explain = "YYYYMMDD�^�ɕϊ����܂��A�J�����ʒu�̂ݎw�肷��";
		} else {
			Inf_FilterFactory factory = filterMap.get(filter);
			if (factory != null)
				explain = factory.getExplain();
		}
		return explain;
	}

	@Override
	public List convert(List<String> list, int stat) {
		return null;
	}

	//-------------------------------------------------------------------------
	// main
	//-------------------------------------------------------------------------
	public static void main(String[] argv) {
		test02();
	}

	//-------------------------------------------------------------------------
	// �@�}���`�t���O���̃e�X�g�i����͗ʓI�ȃe�X�g�ɂ��Ȃ�j
	//-------------------------------------------------------------------------
	public static void test02() {
		String param = ResControl.D_DRV + "workspace/gotoTest/Parm.txt";
		String iPath = ResControl.D_DRV
				+ "workspace/gotoTest/54232_rawdata.tsv";
		String oPath2 = ResControl.D_DRV + "workspace/gotoTest/ans2.txt";
		TextFactory substr_B = new TextFactory(oPath2, iPath,
				new SubstrCnv(param));
		substr_B.execute();
	}
}
