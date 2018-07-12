package kyPkg.filter;

import static globals.ResControl.getCurDir;
import static kyPkg.util.Joint.join;
import static kyPkg.util.KUtil.array2String;
import static kyPkg.util.KUtil.list2String;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import kyPkg.uFile.FileUtil;

public abstract class EzWriterBase implements Inf_BaseClojure, Inf_oClosure {
	//	protected String charsetName = System.getProperty("file.encoding");
	private String charsetName = FileUtil.getDefaultEncoding();//20161222

	protected String inDelimiter = "\t";
	protected String delimiter = "\t";
	protected String LF = System.getProperty("line.separator");
	protected OutputStreamWriter writer;
	protected String preRec = "";
	protected boolean suppress = false;
	protected boolean append = false;
	protected long writeCount = 0;
	protected String outPath = getCurDir() + "tempolary.tmp";
	protected String header = null;
	// ------------------------------------------------------------------------
	// ���ʏ�e���|�����ɏ����o����close����i�K�Ŗ{���̏o�̓p�X�Ƀ��l�[������
	// ���A���A�ǉ��������݃��[�h�̂Ƃ��̓X���b�v���������Ȃ��̂Œ��ӂ���
	// ------------------------------------------------------------------------
	protected boolean swapMode = true;

	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	public EzWriterBase(String path) {
		super();
		FileUtil.makeParents(path);
		this.outPath = path;
		//		DosEmu.del(this.outPath);//20160822 �Ȃ�ƂȂ��ǉ��E�E�E�t�@�C���������ꂸ�O��̌��ʂ��E���Ă��܂��ƌ��Ȃ̂ŁE�E�E
	}

	// ------------------------------------------------------------------------
	// �A�N�Z�b�T
	// ------------------------------------------------------------------------
	public void setCharsetName(String charsetName) {
		charsetName = charsetName.trim();
		if (charsetName.equals("")) {
			charsetName = FileUtil.getDefaultEncoding();//20161222
		}
		this.charsetName = charsetName;
	}

	public void setSuppress(boolean suppress) {
		this.suppress = suppress;
	}

	public void setAppendMode(boolean append) {
		this.append = append; // �����㏑�����[�h�Ȃ�X���b�v�������Ȃ��E�E�E�E
	}

	@Override
	public void setHeader(String header) {
		this.header = header;
	}

	@Override
	public void setHeader(List<String> headList, String delimiter) {
		this.header = list2String(headList, delimiter);
	}

	@Override
	public void setHeader(String[] headArray, String delimiter) {
		this.header = array2String(headArray, delimiter);
	}

	// XXX �ǂ����āE�E�EOutputStreamWriter��extend���Ă��Ȃ��̂������H�H�H�i@20120511�j
	public OutputStreamWriter getWriter() {
		return writer;
	}

	public void setEncoding(String encoding) {
		this.charsetName = encoding;
	}

	@Override
	public void setLF(String lf) {
		LF = lf;
	}

	@Override
	public long getWriteCount() {
		return writeCount;
	}

	@Override
	public String getPath() {
		return outPath;
	}

	@Override
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	// ------------------------------------------------------------------------
	// init
	// ------------------------------------------------------------------------
	@Override
	public void init() {
		open();
	}

	// ------------------------------------------------------------------------
	// fin
	// ------------------------------------------------------------------------
	@Override
	public void write() {
		close();
	}

	// ------------------------------------------------------------------------
	// execute
	// ------------------------------------------------------------------------
	@Override
	public void execute(String[] rec) {
		execute(join(rec, ","));
	}

	@Override
	public void execute(String rec) {
		writeLoco(rec);
	}

	// ------------------------------------------------------------------------
	// open
	// ------------------------------------------------------------------------
	@Override
	public synchronized boolean open() {
		return open(append);
	}

	@Override
	public synchronized boolean open(boolean append) {
		if (append)
			swapMode = false;
		preRec = null;
		writeCount = 0;
		String tmpPath = outPath + ".tmp";// .tmp�������Ƃ����O��
		if (!swapMode)
			tmpPath = outPath;
		if (writer != null)
			return true;// �����C���X�^���X���d�����ēo�^����ꍇ���l�����Ă���
		try {
			File oFile = new File(tmpPath);
			if (append) {
				if (oFile.exists() && !oFile.canWrite()) {
					System.out.println("#error File Can't Write!! :" + tmpPath);
					return false;
				}
			} else {
				if (append == false && oFile.exists())
					oFile.delete(); // �����Ə�����̂��ǂ����A�㏑���������v�m�F����
			}
			//			System.out.println("#20161222# charsetName:"+charsetName);
			writer = new OutputStreamWriter(
					new FileOutputStream(tmpPath, append), charsetName);
		} catch (IOException ie) {
			ie.printStackTrace();
			System.out.println("#ERROR IOException EzWriter.open()" + tmpPath);
		} catch (Exception e) {
			System.out.println("#ERROR Exception   EzWriter.open()" + tmpPath);
			e.printStackTrace();
		}
		if (header != null)
			writeLoco(header);// �w�b�_�[���w�肳��Ă����珑���o��
		return true;
	}

	// ------------------------------------------------------------------------
	// close
	// ------------------------------------------------------------------------
	@Override
	public synchronized void close() {
		if (writer == null)
			return;// �����C���X�^���X���d�����ēo�^����ꍇ���l�����Ă���
		try {
			// System.out.println("close");
			writer.close();
			writer = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (swapMode) {
			// �ړI�̃t�@�C���Ƀ��l�[���E�E�E
			File wFile_O = new File(outPath);
			if (wFile_O.exists())
				wFile_O.delete(); // �����Ə�����̂��ǂ����A�㏑���������v�m�F����
			String tmpPath = outPath + ".tmp";// .tmp�������Ƃ����O��
			File wFile_T = new File(tmpPath);
			wFile_T.renameTo(wFile_O);
		}
	}

	// ------------------------------------------------------------------------
	// write
	// ------------------------------------------------------------------------
	public boolean write(List list) {
		if (list == null)
			return false;
		return writeLoco(join(list, delimiter));
	}

	public boolean write(String[] array) {
		if (array == null)
			return false;
		return writeLoco(join(array, delimiter));
	}

	@Override
	public synchronized boolean write(String rec) {
		return writeLoco(rec);
	}

	// ------------------------------------------------------------------------
	// write
	// ------------------------------------------------------------------------
	private synchronized boolean writeLoco(String rec) {
		if (rec == null) {
			return true;
			// �󂯎�������R�[�h��null�Ȃ牽�������o���Ȃ��i��s�͏o�͂��Ȃ��I�j
			// �܂�A�o�͂������Ȃ����R�[�h�Ȃ�A�t�B���^�[��null��Ԃ��΂悢
		}
		if (writer == null) {
			System.out.println("#ERROR @ezWriter writer == null ?!");
			return false;
		}
		try {
			if (suppress && rec.equals(preRec)) {
				// �T�v���X�I�v�V��������Œ��O�̃��R�[�h�Ɠ������e�Ȃ珑���o���Ȃ�
			} else {
				writer.write(rec);
				writer.write(LF);
				writeCount += 1;
			}
			preRec = rec;
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}