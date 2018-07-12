package kyPkg.filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException; //import java.util.List;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import globals.ResControl;
import kyPkg.converter.ConverterList;
import kyPkg.converter.Inf_ArrayCnv;
import kyPkg.converter.Inf_StrConverter;
import kyPkg.uFile.File49ers;
import kyPkg.uFile.FileUtil;

public class EzReader implements Inf_iClosure {
	//	private String charsetName = System.getProperty("file.encoding");
	private String charsetName = FileUtil.getDefaultEncoding();//20161222

	private String path;
	private String delimiter = null;
	private BufferedReader br;
	private String currentRec;
	private int stat;
	// converter etc
	private Inf_StringCnv stringCnv;
	private ConverterList preConverter = null;
	private Inf_ArrayCnv arrayConv = null;
	private Inf_BaseClojure clojure;

	public void setArrayConv(Inf_ArrayCnv arrayConv) {
		this.arrayConv = arrayConv;
	}

	public void setClojure(Inf_BaseClojure clojure) {
		this.clojure = clojure;
	}

	// ���K�\���ɂ�錋��stat�����̐��l���傫���ꍇ�̂ݏo�͂����
	// ���K�\���ɂ�錋��stat�͕����w�肷�邱�Ƃ��ł��A�Ȃ������Z���ꂽ���̂��ŏI�I��stat�ƂȂ�܂�
	private RegChecker regChecker = null;
	private int checkLevel = 0;// �f�t�H���g��0�Ȃ̂ł��ׂďo�͑ΏۂƂȂ�
	private long readCount = 0;

	public long getReadCount() {
		return readCount;
	}

	public void addConverter(int index, Inf_StrConverter converter) {
		if (this.preConverter == null)
			this.preConverter = new ConverterList();
		preConverter.addConverter(index, converter);
	}

	private void setCharsetName(String charsetName) {
		charsetName = charsetName.trim();
		if (charsetName.equals("")) {
			//charsetName = System.getProperty("file.encoding");
			charsetName = FileUtil.getDefaultEncoding();//20161222
		}
		this.charsetName = charsetName;
	}

	public void setStringCnv(Inf_StringCnv stringCnv) {
		this.stringCnv = stringCnv;
	}

	public String getCurrentRec() {
		return currentRec;
	}

	public void setLevel(int level) {
		this.checkLevel = level;
	}

	// private boolean reject = false;// �����Ƀ}�b�`���Ȃ�stat�O�̃f�[�^���������邩�ǂ���
	// // true�Ȃ�Astat=�O�̃f�[�^����������i�f�t�H���g��false�Ȃ̂ŏ�������Ȃ��j
	// public void setReject(boolean reject) {
	// this.reject = reject;
	// }
	// -------------------------------------------------------------------------
	// list���J�����A�J�n�ʒu�i0���n�߂�j�A�����A�u�[���l�A�ݒ�X�e�[�^�X�l�A���W�b�N�X�p�^�[��
	// -------------------------------------------------------------------------
	// ���Y�J�����́A�J�n�ʒu���璷�����i�ȗ�����0��肻�̃Z���S���j��
	// ���W�b�N�X�p�^�[���Ɂi�u�[���l��True�Ȃ�j��v����Ȃ�ݒ�X�e�[�^�X�l�����Z�����
	// -------------------------------------------------------------------------
	// �ŏ��̃Z�����V�Ŏn�܂�Ȃ� 16
	// list.add("0, 0,1,True,16,^7.*");
	// �ŏ��̃Z���i��؂�Ȃ��Œ蒷�Ȃ炻�̃��R�[�h�́j54�o�C�g�߂���1������3�Ȃ�1
	// list.add("1,54,1,True,1,3");
	// -------------------------------------------------------------------------
	// FilterParams paramObj
	// public void setFilter(List<FilterParam> list,int level) {

	@Override
	public void setFilter(RegChecker filter, int level) {
		if (filter != null) {
			this.regChecker = filter;
			this.checkLevel = level;
		}
	}

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	// ���X�g�Ɏw�肵�����W�b�N�X�p�^�[���ɂ��ݒ�l�̍��v��level���傫���ꍇ�͏o�͂����
	// ����ȊO�͏o�͂���Ȃ��H�I
	// ��readLine�����g��Ȃ��ꍇ�Ӗ��𐬂��Ȃ��I
	// public EasyReader(String path, List<FilterParam> list, int level) {
	// -------------------------------------------------------------------------
	public EzReader(String path) {
		this.path = path;
	}

	public EzReader(String path, String charsetName) {
		this(path);
		setCharsetName(charsetName);
	}

	public EzReader(String path, Inf_BaseClojure clojure) {
		this(path);
		setClojure(clojure);
	}

	public EzReader(String path, Inf_ArrayCnv converter) {
		this(path);
		setArrayConv(converter);
	}

	public EzReader(String path, Inf_StringCnv converter) {
		this(path);
		setStringCnv(converter);
	}

	public EzReader(String path, RegChecker filter, int checkLevel) {
		this(path);
		setFilter(filter, checkLevel);
	}

	@Override
	public void open() {
		readCount = 0;
		if (clojure != null)
			clojure.init();
		if (arrayConv != null)
			arrayConv.init();
		if (stringCnv != null)
			stringCnv.init();
		File wkFile = new File(path);
		if (!wkFile.isFile()) {
			System.out.println("#error @EasyReader not a File=>" + path);
			//			new Msgbox(null)
			//					.info("#20150623##error @EasyReader not a File=>" + path);
			return;
		}
		if (!wkFile.canRead()) {
			System.out.println("#error File can not read =>" + path);
			return;
		}
		File49ers f49_L = new File49ers(path, 20, charsetName, null);
		if (this.delimiter == null)
			this.delimiter = f49_L.getDelimiter();
		if (br != null)
			return;// �����C���X�^���X���d�����ēo�^����ꍇ���l�����Ă���
		try {
			if (!new File(path).exists()) {
				System.out.println("File is Not Existed:" + path);
			} else {
				br = new BufferedReader(new InputStreamReader(
						new FileInputStream(path), charsetName));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	public String readLine() {
		currentRec = null;
		if (br != null) {
			try {
				currentRec = br.readLine();
				readCount++;
				if (stringCnv != null)
					currentRec = stringCnv.convert(currentRec);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return currentRec;
	}

	@Override
	public int getStat() {
		getSplited();
		return stat;
	}

	@Override
	public String[] readSplited() {
		readLine();
		return getSplited();
	}

	@Override
	public String[] getSplited() {
		return getSplited(getCurrentRec());
	}

	private String[] getSplited(String rec) {

		stat = 999;
		if (rec == null)
			return null;

		// split�Ɉ�����^���邱�Ƃɂ���ăT�v���X������邱�Ƃ��ł���2011/07/29
		String[] array = rec.split(delimiter, Integer.MAX_VALUE);

		if (array == null)
			return null;

		stat = 0;
		if (preConverter != null)
			array = preConverter.convert(array);

		if (array != null && regChecker != null) {
			stat = regChecker.checkIt(array);
			// ������p�^�[���ɓK������stat�̍��v���A�w�肵���l�ȉ��Ȃ���Ԃ�
			if (stat < checkLevel)
				array = new String[] {};
		}
		if (array != null && clojure != null) {
			clojure.execute(array);
		}

		if (array != null && arrayConv != null) {
			array = arrayConv.convert(array, 0);
		}

		if (array != null) {
			return array.clone();
		} else {
			return null;
		}
	}

	public boolean isEOF() {
		if (currentRec == null)
			return true;
		return false;
	}

	@Override
	public boolean notEOF() {
		if (currentRec == null)
			return false;
		return true;
	}

	@Override
	public void close() {
		if (br == null)
			return;// �����C���X�^���X���d�����ēo�^����ꍇ���l�����Ă���
		try {
			br.close();
			br = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (stringCnv != null)
			stringCnv.fin();
		if (arrayConv != null)
			arrayConv.fin();
		if (clojure != null) {
			clojure.write();
		}
	}

	@Override
	public String getDelimiter() {
		return delimiter;
	}

	@Override
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	@Override
	public String getCurrent() {
		return currentRec;
	}

	public static void main(String[] argv) {
		test01();
	}

	public static void test01() {
		List charsetNames = FileUtil.getCharsetNames();
		String path;
		String dir = ResControl.D_DRV + "resources/templates/codingTest/zapp/";
		for (Iterator iterator1 = charsetNames.iterator(); iterator1
				.hasNext();) {
			String charsetName = (String) iterator1.next();
			if (!charsetName.equals("JISAutoDetect")) {
				path = dir + charsetName + ".txt";
				System.out.println(
						"------------------------------------------------------------------");
				System.out.println("read path:" + path);
				EzReader reader = new EzReader(path, charsetName);
				reader.open();
				String[] splited = reader.readSplited();
				while (splited != null) {
					if (splited.length > 0) {
						for (int i = 0; i < splited.length; i++) {
							System.out.print("ttt>" + splited[i]);
						}
						System.out.println("<ttt");
						splited = reader.readSplited();
					}
				}
				reader.close();

			}
		}
	}

}
