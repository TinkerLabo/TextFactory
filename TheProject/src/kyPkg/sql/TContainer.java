package kyPkg.sql;

import kyPkg.uFile.File49ers;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TContainer {
//	public static Log log = LogFactory.getLog("kyPkg.sql.TexTable.class");
	public static final String APPENDA_CLASS = "kyPkg.sql";
	public static Log log = LogFactory.getLog(TContainer.APPENDA_CLASS);

	private static final String fldPrefix = "fld";
	private static final String DefType = " VARCHAR";
	protected boolean kill = false;
	protected String dataPath;
	protected String tableName;
	protected int maxCol;
	protected String fDefs;
	protected String[] fArray;
	protected String delimiter = "\t";

	// protected String encoding = "";
	// public void setEncoding(String encoding) {
	// this.encoding = encoding;
	// }
	// public String getEncoding() {
	// return encoding;
	// }
	public TContainer(String table, String dataPath, int limitCol) {
		this.tableName = table;
		// this.encoding = encoding;// ���̂Ƃ���g���ĂȂ������E�E�E
		if (limitCol > 0) {
			this.kill = true;
			this.delimiter = "\t";
			this.maxCol = limitCol;
			log.info("�@�@�e�[�u����`���쐬�@�s " + table + "�t (" + this.maxCol + "�J����) "
					+ dataPath);
		} else {
			this.kill = false;
			this.dataPath = dataPath;
			this.maxCol = fileAnalyze(dataPath);
			log.info("�@�@�t�@�C������e�[�u����`�����@�s " + table + "�t (" + this.maxCol
					+ "�J����) " + dataPath);
		}
		fDefs = createFieldsDef(DefType, this.maxCol);
		fArray = TContainer.createFieldsArray(tableName, this.maxCol);
	}

	public boolean isKill() {
		return kill;
	}

	public String getDataPath() {
		return dataPath;
	}

	public String[] getfArray() {
		return fArray;
	}

	public String getDelimiter() {
		return delimiter;
	}

	protected int fileAnalyze(String dataPath) {
		if (new java.io.File(dataPath).exists()) {
			log.info("# �t�@�C���͑��݂��܂����F" + dataPath);
			File49ers f49 = new File49ers(dataPath);
			this.delimiter = f49.getDelimiter();
			this.maxCol = f49.getMaxColCount();
			if (maxCol > 0) {
				return maxCol;
			} else {
				log.info("#error �J��������0�ȉ��ł�:" + dataPath);
				System.out.println("#error �J��������0�ȉ��ł�:" + dataPath);
			}
		} else {
			log.info("#error �t�@�C�������݂��܂���ł����F" + dataPath);
			System.out.println("#error �t�@�C�������݂��܂���ł����F" + dataPath);
		}
		return -1;
	}

	// -------------------------------------------------------------------------
	// ����̃t�B�[���h���쐬����
	// -------------------------------------------------------------------------
	public String createFieldsDef(String type, int colSize) {
		if (colSize < 0)
			colSize = 0;// 20130314
		StringBuffer buf = new StringBuffer();
		for (int seq = 0; seq < colSize; seq++) {
			if (buf.length() > 0)
				buf.append(",");
			if (seq < maxCol) {
				buf.append(fldPrefix);
				buf.append(seq);
				buf.append(type);
			} else {
				buf.append("null");
			}
		}
		return buf.toString();
	}

	// -------------------------------------------------------------------------
	// ����̃t�B�[���h���쐬����
	// -------------------------------------------------------------------------
	public static String[] createFieldsArray(String table, int n) {
		// XXX if(n<0) return null;
		if (n < 0)
			n = 0;// 20130314
		String[] array = new String[n];
		for (int seq = 0; seq < n; seq++) {
			array[seq] = table + "." + fldPrefix + seq;
		}
		return array;
	}

	public String getTableName() {
		return tableName;
	}

	public void cutOff(int cutoff) {
		maxCol -= cutoff;
		fDefs = createFieldsDef(DefType, this.maxCol);
		fArray = createFieldsArray(tableName, this.maxCol);
	}

	public int getMaxCol() {
		return maxCol;
	}

	public String getfDefs() {
		return fDefs;
	}

	public String getFields(int maxCol) {
		return createFieldsDef("", maxCol);
	}

	public String getFld(int col) {
		return fArray[col];
	}

	public String getFldsFrom(int from) {
		return getFlds(from, maxCol);
	}

	private String getFlds(int from, int to) {
		StringBuffer buf = new StringBuffer();
		for (int i = from; i <= to; i++) {
			if (buf.length() > 0)
				buf.append(",");
			buf.append(fArray[i]);
		}
		return buf.toString();
	}

	public TContainer() {
		super();
	}
}