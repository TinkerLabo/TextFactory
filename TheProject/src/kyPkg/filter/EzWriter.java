package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.util.ArrayList;
import java.util.List;

import kyPkg.converter.Inf_ArrayCnv;
import kyPkg.uFile.FileUtil;

public class EzWriter extends EzWriterBase {
	private Inf_ArrayCnv arrayConv;
	private String[] header = null;

	//20160421 Header
	public void setHeader(String[] header) {
		this.header = header;
	}

	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	public EzWriter(String path) {
		//		super(path);//20170425
		this(path, "");
	}

	public EzWriter(String path, String charsetName) {
		super(path);
		setCharsetName(charsetName);
	}

	public EzWriter(String path, Inf_ArrayCnv converter) {
		//		super(path);//20170425
		this(path, "");
		setConverter(converter);
	}

	public void setConverter(Inf_ArrayCnv converter) {
		this.arrayConv = converter;
	}

	// ------------------------------------------------------------------------
	// open
	// ------------------------------------------------------------------------
	@Override
	public boolean open(boolean append) {
		if (arrayConv != null)
			arrayConv.init();
		boolean stat = super.open(append);
		if (stat)
			super.write(header);//20160421 
		return stat;
	}

	// ------------------------------------------------------------------------
	// close
	// ------------------------------------------------------------------------
	@Override
	public synchronized void close() {
		if (arrayConv != null)
			arrayConv.fin();
		super.close();
	}

	// ------------------------------------------------------------------------
	// write
	// ------------------------------------------------------------------------
	@Override
	public boolean write(Inf_iClosure reader) {
		return write(reader.readSplited(), (int) writeCount);
	}

	@Override
	public boolean write(List list, int stat) {
		if (list == null)
			return false;
		if (arrayConv != null) {
			// �����łǂ̂��炢�p�t�H�[�}���X��������̂��A�C�ɂȂ�E�E�E�ǂ̂��炢�̕p�x�ł�����ʂ�̂��H
			String[] array = (String[]) list.toArray(new String[list.size()]);
			return super.write(join(arrayConv.convert(array, stat), delimiter));
		} else {
			return super.write(list);
		}
	}

	@Override
	public boolean write(String[] array, int stat) {
		if (array == null)
			return false;
		if (arrayConv != null) {
			return super.write(join(arrayConv.convert(array, stat), delimiter));
		} else {
			return super.write(array);
		}
	}

	@Override
	public synchronized boolean write(String rec) {
		if (arrayConv != null) {
			return write(rec.split(inDelimiter, -1), 0);
		} else {
			return super.write(rec);
		}
	}

	// ------------------------------------------------------------------------
	// ���X�g����t�@�C���ɏ����o��
	// EzWriter.list2File(outPath, list);
	// ------------------------------------------------------------------------
	public static boolean list2File(String outPath, List<String> list) {
		return list2File(outPath, list, false, "");
	}

	// ------------------------------------------------------------------------
	// �ǉ��������݂Ȃ�append��true�ɂ���
	// �G���R�[�h���w�肵�����ꍇ�ɂ�charSetName��"UTF-8"�Ȃǂ��w�肷��
	// ------------------------------------------------------------------------
	public static boolean list2File(String outPath, List<String> list,
			boolean append, String charsetName) {
		FileUtil.makeParents(outPath);
		EzWriter writer = new EzWriter(outPath);
		writer.setCharsetName(charsetName);
		writer.setSuppress(false);
		writer.open(append);
		for (String element : list) {
			writer.write(element);
		}
		writer.close();
		return true;
	}

	public static void main(String[] argv) {
		list2File_test1();
	}

	// ------------------------------------------------------------------------
	// �l�X�ȕ����R�[�h�Ńt�@�C���������o���Ă݂�e�X�g
	// ------------------------------------------------------------------------
	public static void list2File_test1() {
		List<String> list = getSampleDatas();
		List<String> charsetList = FileUtil.getCharsetNames();
		charsetList.remove("JISAutoDetect");
		for (String charSetName : charsetList) {
			String rootDir = globals.ResControl.getQprRootDir();
			String outPath = rootDir + "enc_" + charSetName + ".txt";
			System.out.println("outPath=>" + outPath);
			boolean append = false;
			EzWriter.list2File(outPath, list, append, charSetName);
		}
	}

	// ------------------------------------------------------------------------
	// isam���Ƒ��̂����ł͓����Ă���Ȃ��̂ŗv�C�����ȁE�E�E�E��������̓N���X�W�v�p�̂��̂Ȃ̂Ŗڂ��Ԃ�
	// ------------------------------------------------------------------------
	public static void list2File_test2() {
		String cnn = "DRIVER={Microsoft Text Driver (*.txt; *.csv)};DefaultDir=$;DriverId=27;FIL=text;MaxBufferSize=16384;PageTimeout=5;";
		List list = new ArrayList();
		list.add("connect\t" + cnn);
		list.add("table\tASM#TXT");
		list.add("field\t");
		list.add("key\tID");
		list.add("Cond\t");
		String rootDir = globals.ResControl.getQprRootDir();
		String oPath = rootDir + "alias.txt";
		EzWriter.list2File(oPath, list);
	}

	public static List<String> getSampleDatas() {
		List sampleData = new ArrayList();
		sampleData.add("�����\���F���Q��ޗ����J��E ���ΐ��؋��y�� ���ɉF�]��");
		sampleData.add("�T�U�V�W�X�Y�Z�[�\�]�_�`�a�b�c�d�{�|�}�~���������������������");
		sampleData.add("�@��ˑ����� �����������������������������~�@�A�B�C�D�����������������������������������߁燔��");
		sampleData.add("�������������������������������������������������������ÃăŃƃǃȃɃʃ˃̃̓΃σЃу҃ӃԃՃ�");
		sampleData.add("���������������������������������ĂƂȂɂʂ˂̂͂Ђӂւق܂݂ނ߂���������");
		sampleData.add("�A�C�E�G�I�L�N�P�R�T�V�X�Z�\�^�`�c�e�g�i�j�k�l�m�n�q�t�w�z�}�~������������������");
		sampleData.add("�������������������������������������ܦ�");
		sampleData.add("�P�Q�R�S�T�U�V�W�X�O");
		sampleData.add("1234567890-^\\@[;:],./!\"#$%&'()=~|`{+*}<>?_");
		sampleData.add("�`�a�b�c�d�e�f�g�h�i�j�k�l�m�n�o�p�q�r�s�t�u�v�w�x�y");
		sampleData.add("����������������������������������������������������");
		sampleData.add("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		sampleData.add("abcdefghijklmnopqrstuvwxyz");
		sampleData
				.add("E 04-YYYY2=�`�O�X�O�U�Q�T�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@   000000000000    ");
		return sampleData;
	}
}
