package kyPkg.filter;

import java.io.File;

import globals.ResControl;
import kyPkg.converter.Inf_ArrayCnv;
import kyPkg.task.Abs_BaseTask;

// 2013-07-03 yuasa
public class Common_IO extends Abs_BaseTask {
	// ------------------------------------------------------------------------
	// ���̓N���[�W��
	// ------------------------------------------------------------------------
	private Inf_iClosure reader = null;
	private Inf_oClosure writer = null;
	private String outPath = null;
	private String tmpPath = null;

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	// XXX ���o�͂������t�@�C����������E�E�E�e���|�����ɏ����o���I�����Ƀt�@�C�������l�[������
	// �e���|�����ɏ����o��
	// �o�̓t�@�C���Ɠ����t�@�C��������΁A�g���q��ύX����i.bak�Ȃǁj
	// �e���|���������l�[������
	public Common_IO(String outPath, Inf_iClosure reader) {
		this.reader = reader;
		this.outPath = outPath;
		this.tmpPath = ResControl.getTempFile();
		this.writer = new EzWriter(tmpPath);
	}

	public Common_IO(String outPath, Inf_iClosure reader, String delimiter) {
		this.reader = reader;
		this.outPath = outPath;
		this.tmpPath = ResControl.getTempFile();
		this.writer = new EzWriter(tmpPath);
		this.writer.setDelimiter(delimiter);
	}

	public Common_IO(Inf_oClosure writer, Inf_iClosure reader) {
		this.reader = reader;
		this.tmpPath = ResControl.getTempFile();
		this.writer = writer;
	}

	public Common_IO(Inf_oClosure writer, String inPath) {
		this.reader = new EzReader(inPath);
		this.tmpPath = ResControl.getTempFile();
		this.writer = writer;
	}

	// tmp�ɏo�͂����ナ�l�[������
	// original ���c���������Ƃ͂Ȃ����H�H
	public void rename(String tempPath, String outPath) {
		File orgFile = new File(outPath);
		if (orgFile.isFile())
			orgFile.delete();
		File tmpFile = new File(tempPath);
		tmpFile.renameTo(orgFile);
	}

	// -------------------------------------------------------------------------
	// ���s
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("Common_IO start");
		reader.open();
		writer.open();
		while (writer.write(reader))
			;
		reader.close();
		writer.close();
		if (outPath != null)
			rename(tmpPath, outPath);
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		tmplate01();
	}

	public static void tmplate01() {
		String inPath = "C:/Documents and Settings/Administrator.AGC/�f�X�N�g�b�v/test/merge/F_Org.txt";
		String outPath = "C:/Documents and Settings/Administrator.AGC/�f�X�N�g�b�v/test/merge/F_Org.ans";
		EzReader reader = new EzReader(inPath);
		new Common_IO(outPath, reader).execute();
	}

	public static void template02() {
		String inPath = "C:/Documents and Settings/Administrator.AGC/�f�X�N�g�b�v/test/merge/F_Org.txt";
		String outPath = "C:/Documents and Settings/Administrator.AGC/�f�X�N�g�b�v/test/merge/F_Org.ans";
		Inf_oClosure writer = new EzWriter(outPath);
		new Common_IO(writer, inPath).execute();
	}

	public static void template03() {
		String inPath = "C:/@qpr/home/123620000036/calc/loyHead.txt";
		String outPath = "C:/@qpr/home/123620000036/calc/HVShare.txt";
		Inf_ArrayCnv cnv = new Tmp_ArrayCnv();
		EzReader reader = new EzReader(inPath, cnv);
		new Common_IO(outPath, reader).execute();
	}

	public static void template04() {
		String inPath = "C:/Documents and Settings/Administrator.AGC/�f�X�N�g�b�v/test/merge/F_Org.txt";
		String outPath = "C:/Documents and Settings/Administrator.AGC/�f�X�N�g�b�v/test/merge/F_Org.ans";
		Inf_ArrayCnv cnv = new Tmp_ArrayCnv();
		Inf_oClosure writer = new EzWriter(outPath, cnv);
		new Common_IO(writer, inPath).execute();
	}

	public static void sameLogic() {
		String inPath = "";
		String outPath = "";
		Inf_iClosure reader = new EzReader(inPath);
		Inf_oClosure writer = new EzWriter(outPath);
		// Inf_oClosure writer = new EzWriter(outPath, new Flt_Template2.new
		// LocoConverter());
		reader.open();
		writer.open();
		while (writer.write(reader))
			;
		reader.close();
		writer.close();
	}

}
