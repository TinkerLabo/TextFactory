package kyPkg.globals;

import java.util.HashMap;

import globals.AGC_GATEWAY;
import globals.AGC_SMTP;
import globals.GWHOST;
import globals.HCS_SERVER;
import globals.MM_SERVER;
import kyPkg.uFile.YamlControl;

//�Ϗ����Ȃ���΂Ȃ�Ȃ����\�b�h������������A�璷���i�C���R�ꂪ�N�������ŁA�X�}�[�g�ł͂Ȃ��j
//�풓������i�T�[�o�[���\�b�h�������I�ɎQ�Ƃ�����̂�static�Ȉ����Ƃ������E�E�E�l���Ǘ��Ȃ����A�ً}�ȕύX�ɑΉ��������j
//static�ȃN���X
public abstract class StaticKeyValue {
	private String ymlName = "default" + "." + YamlControl.YML;
	private HashMap<String, String> ymlMap = null;

	// private StaticKeyValue getInstance();���ۃ��\�b�h�ɂ��Ă����Ď�����������������
	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	public StaticKeyValue() {
		this.ymlMap = new HashMap<String, String>();
	}

	// ------------------------------------------------------------------------
	// setName�@�@�R���X�g���N�^�̂��Ɓ@super.setName(getClass().getSimpleName());
	// ------------------------------------------------------------------------
	public void setName(String ymlName) {
		this.ymlName = ymlName.trim() + "." + YamlControl.YML;
	}

	// ------------------------------------------------------------------------
	// forceDefault=>false�Ȃ�yml�ɏ�����Ă�����̂�D�悳����
	// ------------------------------------------------------------------------
	public void save(boolean forceDefault) {
		String curDir = globals.ResControl.getCurDir();
		String paramPath = curDir + ymlName;
		System.out.println("Save Yaml:" + paramPath);
		ymlMap = YamlControl.yaml2Map(paramPath, ymlMap, forceDefault);
	}

	public void put(String key, String val) {
		// if (key == null || val == null)
		if (key == null) // value��null�ł��Ƃ����@�A20150313
			return;
		ymlMap.put(key, val);

	}

	public String get(String key) {
		if (key == null)
			return null;
		return ymlMap.get(key);
	}

	public static void main(String[] argv) {
		test2013_0316();
	}

	// static�A�N�Z�X�����ꍇ�̏����l�m�F
	public static void test2013_0316() {
		System.out.println("MM_SERVER.getUser:" + MM_SERVER.getUser());
		System.out.println("MM_SERVER.getPass:" + MM_SERVER.getPass());

		System.out.println("HCS_SERVER.getUser:" + HCS_SERVER.getUser());
		System.out.println("HCS_SERVER.getPass:" + HCS_SERVER.getPass());

		System.out.println("AGC_GATEWAY.getHost:" + AGC_GATEWAY.getHost());
		System.out.println("AGC_GATEWAY.getPort:" + AGC_GATEWAY.getPort());

		System.out.println("AGC_SMTP.getHost:" + AGC_SMTP.getHost());
		System.out.println("AGC_SMTP.getPort:" + AGC_SMTP.getPort());

		System.out.println("GWHOST.getHost:" + GWHOST.getHost());
		System.out.println("GWHOST.getUser:" + GWHOST.getUser());
		System.out.println("GWHOST.getPass:" + GWHOST.getPass());
	}

	// �����l�ݒ肵���ꍇ��yml�̏����o���̊m�F�ȂǗp
	public static void test2013_0311() {
		System.out.println("pre MM_SERVER.getUser:" + MM_SERVER.getUser());
		System.out.println("pre MM_SERVER.getPass:" + MM_SERVER.getPass());
		System.out.println("pre HCS_SERVER.getUser:" + HCS_SERVER.getUser());
		System.out.println("pre HCS_SERVER.getPass:" + HCS_SERVER.getPass());
		System.out.println("pre AGC_GATEWAY.getHost:" + AGC_GATEWAY.getHost());
		System.out.println("pre AGC_GATEWAY.getPort:" + AGC_GATEWAY.getPort());

		// ���Œ�p�����[�^�[�̓V���O���g���N���X�Ɋi�[����i�O���p�����[�^�l�����݂���ΕύX����A������΃f�t�H���g���g�p����j
		MM_SERVER.setUser("test MM_user");
		MM_SERVER.setPass("test ������");
		MM_SERVER.save();
		HCS_SERVER.setUser("test HCS_user");
		HCS_SERVER.setPass(null);
		HCS_SERVER.save();
		AGC_GATEWAY.setHost(null);
		AGC_GATEWAY.setPort(null);
		AGC_GATEWAY.save();

		System.out.println("------------------------------------------------");
		System.out.println("aft MM_SERVER.getUser:" + MM_SERVER.getUser());
		System.out.println("aft MM_SERVER.getPass:" + MM_SERVER.getPass());
		System.out.println("aft HCS_SERVER.getUser:" + HCS_SERVER.getUser());
		System.out.println("aft HCS_SERVER.getPass:" + HCS_SERVER.getPass());
		System.out.println("aft AGC_GATEWAY.getHost:" + AGC_GATEWAY.getHost());
		System.out.println("aft AGC_GATEWAY.getPort:" + AGC_GATEWAY.getPort());
	}

}