package kyPkg.uFile;

import java.util.List;

import kyPkg.task.TaskSQL2File;
import kyPkg.uDateTime.DateCalc;
import kyPkg.util.CnvArray;

//�A���P�[�g�p�́walias.txt�x����уp�����[�^�t�@�C���wqtb1.txt�x���g���āA
//�l�������ϊ������e�L�X�g�t�@�C���������o��

public class QTB_DeNormalize extends SourceObj {
	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	// QTB�t�@�C�����x�[�X�Ƀt�B�[���h��`�A�l�˃��x���ϊ������̐������s��
	// ------------------------------------------------------------------------
	public QTB_DeNormalize(String sourceDir) {
		super();
		System.out.println("# @QTB_DeNormalize # sourceDir:" + sourceDir);
		String keyName = "���j�^�[�h�c";
		super.setSourceDir(sourceDir);
		super.setKeyName(keyName);
		super.incore(getSourceDir());
	}

	public String createCommmonSQL() {
		return "";
	}

	public String qtb2Text(String outPath, String befYmd, String aftYmd, String delimiter ) {
		if (selectedFields == null)
			selectAllFields();//�����ł��ׂẴt�B�[���h��ݒ肵�Ă���́i����ȍ~�C�ӂ̃t�B�[���h���w��ł���Ηǂ������j
		//���݂��Ȃ��t�B�[���h���w�肳�ꂽ�ꍇ�ɂ��ςȓ��������Ȃ��悤�ɂ���H�v���K�v�I�I
		String alter = getTableAlterName();
		String table = getTable();
		String key = getKey();
		String fields = getFields();
		CnvArray cnv = getConverter();
		List<String> selectedFields = getSelectedFields();
		String sql = "SELECT " + fields + " FROM " + table + " as " + alter;
		// --------------------------------------------------------------------
		// ���ԓ��L�����j�^�[�Ɍ��肷�邩�ǂ���
		// --------------------------------------------------------------------
		boolean isValidMon = true;
		String validMonCnd = "";
		if (isValidMon) {
			validMonCnd = " WHERE(" + alter + "." + "XK2" + " <= " + befYmd
					+ " ) AND (" + alter + "." + "XK3" + " >= " + aftYmd
					+ " ) AND (" + alter + "." + "XK2" + " <> 0) ";
			sql = sql + validMonCnd;
		}
//		System.out.println("############### qtb2Text sql:" + sql);
		TaskSQL2File task = new TaskSQL2File(outPath, sql);
		task.setConverter(cnv); 				// �ϊ������ݒ�
		task.setHeader(selectedFields, delimiter); 	// �w�b�_�[�t�@�C����ǉ�
		task.setDelimiter(delimiter);
		task.execute();
//		System.out.println("qtb2Text end");
		return sql;
	}

	//TODO�@�T���v���f�[�^���}�g���e�ۊǁ��Ǘ�����ׂ�
	// ------------------------------------------------------------------------
	// �၄���j�^�[�����t�@�C�����o�́A���ڂ�񐳋K�����ďo�͂���@�@�iforIizuka20141016�j
	// ------------------------------------------------------------------------
	public static void denormalizeIt() {
		String befYmd = "20170109";
		String aftYmd = "20170319";
		int nendo = DateCalc.getNendo(aftYmd);// �W�v�Ώۊ��Ԃ��@�w�N�x�x�@�����肷��
//		String qtbDir = ResControl.getQtbDir(nendo);//"c:/samples/QTB_SAMPLE";// �������t�@�C���������Ă���f�C���N�g���̃p�X
		String qtbDir = "Z:/S2/rx/enquetes/NQ/03_�����E���N���/2016/";
		String outPath = "c:/samples/QTB_SAMPLE/Attr.txt";
		QTB_DeNormalize ins = new QTB_DeNormalize(qtbDir);
		ins.loadSelectedFields("c:/selectedFields.txt"); //�o�͑Ώۍ��ڂ�ǂݍ���
		ins.qtb2Text(outPath, befYmd, aftYmd, "\t");
	}

	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] args) {
		denormalizeIt();
	}
}
