package kyPkg.filter;

import globals.ResControlWeb;

public class Basic_IO {
	private Inf_iClosure reader;
	private Inf_oClosure writer;

	// TODO �t�@�C�������݂��Ȃ��ꍇ�Ƀ_�C�A���O�ł��̎|�\�������ق����ǂ���������Ȃ�
	public Basic_IO(String outPath, String inPath) {
		System.out.println("debug #Basic_IO#");
		System.out.println("debug inPath :" + inPath);
		System.out.println("debug outPath:" + outPath);
		reader = new EzReader(inPath);
		writer = new EzWriter(outPath);
	}

	public Basic_IO(Inf_oClosure writer, Inf_iClosure reader) {
		this.reader = reader;
		this.writer = writer;
	}

	public void execute() {
		reader.open();
		writer.open();
		while (writer.write(reader))
			;
		reader.close();
		writer.close();
	}

	public static void main(String[] argv) {
		test("127720000010");
	}

	public static void test(String itpCode) {
		// ���Ĉ�ʃ��[�U�����̃h���C�u���Q�Ƃł���悤�ɂ��Ȃ���΂��܂������Ȃ��̂�resources��S���[�U�Q�Ɖ\�Ƃ����E�E�ǂݍ��݌����̂�
		String iPath = ResControlWeb.getItpPath_remote(itpCode);
		String oPath = "c:/test127720000010.txt";
		System.out.println("#test#");
		new Basic_IO(oPath, iPath).execute();
	}

}
