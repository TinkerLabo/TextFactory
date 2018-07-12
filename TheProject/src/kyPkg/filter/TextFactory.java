package kyPkg.filter;

import java.io.File;

import kyPkg.converter.Inf_LineConverter;
import kyPkg.task.Abs_BaseTask;
import kyPkg.util.Joint;

//XXX	�o�b�`�N���Ή�������i�P�̂œ����j�E�E�E�ł���΃��O���f���悤�ɂ���
//XXX	@FirstSunday�Ƃ�������E�E�E�������̑����j���̓��t�𖄂ߍ���

//�e�L�X�g�t�@�C����ϊ�����A�ėp�v���O����
public class TextFactory extends Abs_BaseTask {
	private static String comment = "";
	private static final String ARG_ERROR = "#Error �������R�K�v�ł� =>";
	private static final String SAMPLE = "�၄java -jar TextFactory.jar outPath�@inPath�@parmPath�@";
	private static final String NOT_A_FILE = "#Error�@�t�@�C�������݂��܂���B=>";
	private Inf_LineConverter converter;
	private Inf_iClosure reader = null;
	private Inf_oClosure writer = null;
	private int skip = 0;
	private long limit = Long.MAX_VALUE;
	private boolean withHeader = false;
	private int writeCount;

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public TextFactory(String outPath, String inPath,
			Inf_LineConverter converter) {
		// System.out.println("@createTester--------------------------------------------------");
		// System.out.println("public static void tester() {");
		// System.out.println("    String outPath = \"" + outPath + "\";");
		// System.out.println("    String inPath = \"" + inPath + "\";");
		// System.out.println("    Inf_LineConverter converter = " +
		// converter.toString() + ";");
		// System.out.println("    new TextFactory(outPath,inPath,converter);");
		// System.out.println("}");
		// System.out.println("--------------------------------------------------");
		// this.reader = new EzReader(inPath);
		// this.writer = new EzWriter(outPath);
		// this.converter = converter;
		this(new EzWriter(outPath), new EzReader(inPath), converter);
	}

	//20150309 �ĉ������邽�߂ɁA���W�b�N�ύX����
	// private TextFactory(String outPath, String inPath,
	// Inf_LineConverter converter, String oEncoding, String iEncoding) {
	// EzWriter ezWriter = new EzWriter(outPath);
	// ezWriter.setCharsetName(oEncoding);
	// ezWriter.setSuppress(false);
	// this.reader = new EzReader(inPath, iEncoding);
	// this.writer = ezWriter;
	// this.converter = converter;
	// }

	public TextFactory(Inf_oClosure writer, Inf_iClosure reader,
			Inf_LineConverter converter) {
		this.reader = reader;
		this.writer = writer;
		this.converter = converter;
	}

	public int getWriteCount() {
		return writeCount;
	}

	public void setRedersDelimiter(String delimiter) {
		reader.setDelimiter(delimiter);
	}

	public void setLimit(long limit) {
		if (limit < 0) {
			this.limit = Long.MAX_VALUE;
		} else {
			this.limit = limit;
		}
	}

	public void setSkip(int skip) {
		this.skip = skip;
	}

	public void headerOption(boolean withHeader) {
		this.withHeader = withHeader;
	}

	// -------------------------------------------------------------------------
	// ���̓f�[�^�����肷�邽�߂̃t�B���^��ݒ肷��
	// "1,0,1,True,16,^2" �� 1�Z���߂́A�J�n�ʒu0�����ڂ���A����1�����A�����W�b�N�X�Ƀ}�b�`����Ȃ�A��ݒ肷��A�l�A���W�b�N�X
	// -------------------------------------------------------------------------
	public void setFilter(RegChecker filter, int stat) {
		reader.setFilter(filter, stat);
	}

	private String convert2Str(String[] array, int lineNumber) {
		// System.out.println("Write !!>>> array[234]:"+ array[234]);

		String[] debug = converter.convert(array, lineNumber);
		// System.out.println("Write !!>>> debug[234]:"+ debug[234]);

		return Joint.join(converter.convert(array, lineNumber), "\t");
	}

	// -------------------------------------------------------------------------
	// ���̓p�X�Əo�̓p�X�������ꍇ�͔O�̂��߃o�b�N�A�b�v���c���B??
	// File wFile_I = new File(inPath);
	// if (inPath.equals(outPath))
	// wFile_I.renameTo(new File(inPath + ".bak"));
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("TextFactory�@start");
		writeCount = 0;
		boolean wDirty = false;
		String[] splited;
		converter.init();
		// converter.setSuffix(LF);
		reader.open();
		writer.open();
		for (int ix = 0; ix < skip; ix++) {
			reader.readLine();// skip �w��s������ǂ݂���
		}
		if (withHeader)
			writer.write(converter.getHeader());// �R�����g�����w�b�_�[���쐬
		while ((splited = reader.readSplited()) != null) {
			if (splited.length > 0) {
				// System.out.println("Write !!>>> splited.length:"+
				// splited.length);
				wDirty = true; // �ύX���������ꍇ�t���O�𗧂Ă�

				writer.write(convert2Str(splited, writeCount));
				writeCount++;
				if (writeCount >= limit)
					break;
			}
		}
		reader.close();
		writer.close();
		if (wDirty) {
			stop();
		} else {
			abend();
		}
	}

	// �o�b�`�N���p
	public static void kickAsBatch(String[] args) {
		// XXX �w�b�_�[�̗L���̃I�v�V������ݒ肵����
		// XXX @firstsunday
		if (args.length != 3) {
			System.out.println(ARG_ERROR + SAMPLE);
			System.exit(9);
		}
		String outPath = args[0];
		String inPath = args[1];
		String parmPath = args[2];
		comment += " parmPath:" + parmPath + "\n";
		comment += " inPath  :" + inPath + "\n";
		comment += " outPath :" + outPath + "";
		// XXX �o�̓t�@�C���͑��݂��Ȃ��Ă��ǂ����A�f�B���N�g���̑��݃`�F�b�N�Ȃǂ͂��������ǂ���������Ȃ�
		// if (!new File(outPath).isFile()) {
		// System.out.println(NOT_A_FILE + " out :" + outPath);
		// System.exit(9);
		// }
		if (!new File(inPath).isFile()) {
			System.out.println(NOT_A_FILE + " in :" + inPath);
			System.exit(9);
		}
		if (!new File(parmPath).isFile()) {
			System.out.println(NOT_A_FILE + " parm :" + parmPath);
			System.exit(9);
		}
		kyPkg.converter.SubstrCnv converter = new kyPkg.converter.SubstrCnv(parmPath);
		TextFactory ins = new TextFactory(outPath, inPath, converter);
		// XXX �X�L�b�v�������Ή����������ǂ���������Ȃ�
		// setSkip(int skip)
		ins.headerOption(false);
		ins.execute();
	}

	public static void kickAss(String[] args) {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse(
				"TextFactory Elapse");
		elapse.start();
		kickAsBatch(args);
		elapse.setComment(comment);
		elapse.stop();
	}

	public static void main(String[] args) {
		if (args.length == 3) {
			String oPath = args[0];
			String iPath = args[1];
			String parmPath = args[2];
			Inf_LineConverter converter = new kyPkg.converter.SubstrCnv(parmPath);
			TextFactory insSub = new TextFactory(oPath, iPath, converter);
			insSub.headerOption(true);
			insSub.execute();
		} else {
			System.out.println("Usage .. oPath,iPath,parmPath");
		}
	}

	public static void tester01(String[] args) {
		String parmPath = "N:/PowerBX/2014/T1050_���[�O���g�@/subParm.csv";
		String outPath = "c:/ASM.TXT";
		String inPath = "N:/datas/QBR14/1050.csv";
		Inf_LineConverter converter = new kyPkg.converter.SubstrCnv(parmPath);
		TextFactory insSub = new TextFactory(outPath, inPath, converter);
		insSub.headerOption(true);
		insSub.execute();
	}
	// -------------------------------------------------------------------------
	// �p�����[�^�̉�����K�v����I�I�O�P�E�P�P
	// new Filters().fltSubstring("out","In",Vector(),"\t");
	// T:\PBrand\PowerB\2007\
	// T1010\B101\RESULT.TXT
	// T8012\B119\RESULT.TXT
	// -------------------------------------------------------------------------
}
