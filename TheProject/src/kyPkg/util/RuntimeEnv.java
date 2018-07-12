package kyPkg.util;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;

public class RuntimeEnv {
	public static final boolean DEBUG = true;

	public static void main(String[] argv) {
		facade();
	}

	// -------------------------------------------------------------------------
	// ���[�U�̃A�J�E���g��(���[�U�[ID)���擾����
	//	String gJUser = kyPkg.util.RuntimeEnv.getUserID();
	// -------------------------------------------------------------------------
	public static String getUserID() {
		return System.getProperty("user.name").toUpperCase();
	}

	// -------------------------------------------------------------------------
	// �R���s���[�^�[���iks6vxx�j���擾����
	//	String hostname = kyPkg.util.RuntimeEnv.getHostName();
	// -------------------------------------------------------------------------
	public static String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "UnknownHost";
	}

	// -------------------------------------------------------------------------
	//	���ϐ����E���i�R���s���[�^�[���iks6vxx�j���擾�@�������j
	// -------------------------------------------------------------------------
	public static void testHostName() {
		String hostname = System.getenv("HOSTNAME");
		System.out.println("hostname:" + hostname);
	}

	// -------------------------------------------------------------------------
	//	�m�F�p
	// -------------------------------------------------------------------------
	public static void enumProperties() {
		Properties properties = System.getProperties();
		properties.list(System.out);
	}

	// -------------------------------------------------------------------------
	// ���֘A�̃t�@�T�[�h
	// RTmod.facade();
	// -------------------------------------------------------------------------
	public static void facade() {
		if (DEBUG)
			System.out.println("<Debug Mode>");
		kyPkg.util.RuntimeEnv.envinfo();
		kyPkg.util.RuntimeEnv.memoryInfo();
		kyPkg.util.RuntimeEnv.enumCLASSPATH();
		kyPkg.util.RuntimeEnv.enumPATH();
		kyPkg.util.RuntimeEnv.envInfoEnum();
	}

	// -------------------------------------------------------------------------
	// �������̎g�p���[�g ��������int�ŕԂ�
	// -------------------------------------------------------------------------
	public static int memoryInfo() {
		java.text.DecimalFormat exFormat2 = new java.text.DecimalFormat("0.00");



		int iRate = 0;
		float rate = 0.0f;
		try {
			Runtime runtime = Runtime.getRuntime();
			long total = runtime.totalMemory();
			long free = runtime.freeMemory();
			rate = (float) free / (float) total;
			iRate = (int) (rate * 100.0);
			if (iRate < 20) {
				Double share =rate * 100.0;
				String xShare= String.valueOf(exFormat2.format(share));
				System.err.println("FreeMemory " + free + " / " + total
						+ " (Free/Total)Bytes => " + xShare + "%");
			}
		} catch (Exception e) {
			System.err.println("Error");
		}
		return iRate;
	}

	// -------------------------------------------------------------------------
	// �p�X���ϐ��ꗗ
	// -------------------------------------------------------------------------
	private static void enumPATH() {
		System.out.println("���C�u�����p�X:");
		TreeSet ts = getEnv2TreeSet("java.library.path");
		Iterator it = ts.iterator();
		while (it.hasNext())
			System.out.println("=>" + it.next());
	}

	// -------------------------------------------------------------------------
	// �N���X�p�X�ꗗ
	// -------------------------------------------------------------------------
	private static void enumCLASSPATH() {
		System.out.println("Java �N���X�p�X:");
		TreeSet ts = getEnv2TreeSet("java.class.path");
		Iterator it = ts.iterator();
		while (it.hasNext())
			System.out.println("=>" + it.next());
	}

	// -------------------------------------------------------------------------
	// �p�X�A�N���X�p�X��TreeSet���i���j�[�N��sort�������̂�TreeSet���g�p�j
	// -------------------------------------------------------------------------
	// TreeSet ts = getEnv2TreeSet("java.library.path");
	// TreeSet ts = getEnv2TreeSet("java.class.path");
	// -------------------------------------------------------------------------
	private static TreeSet getEnv2TreeSet(String type) {
		String delim = System.getProperty("path.separator");
		String envString = System.getProperty(type);
		String[] array = envString.split(delim);
		TreeSet ts = new TreeSet();
		for (int i = 0; i < array.length; i++) {
			ts.add(array[i]);
		}
		return ts;
	}

	// -------------------------------------------------------------------------
	// ���ϐ��Ȃǃ`�F�b�N
	// �v���b�g�t�H�[���̃f�t�H���g�̕����Z�b�g ?? CP932
	// -------------------------------------------------------------------------
	private static void envinfo() {
		System.out.println("���z�}�V���̃f�t�H���g�̕����Z�b�g =>"
				+ java.nio.charset.Charset.defaultCharset());
		System.out.println("�n�r��                             =>"
				+ System.getProperty("os.name"));
		System.out.println("�t�@�C����؂蕶�� (UNIX �ł� '/') =>"
				+ System.getProperty("file.separator"));
		System.out.println("�p�X��؂蕶��     (UNIX �ł� ':') =>"
				+ System.getProperty("path.separator"));
		System.out.println("Java ���z�}�V���̎d�l�o�[�W����    =>"
				+ System.getProperty("java.vm.specification.version"));
		System.out.println("Runtime�̃o�[�W����                =>"
				+ System.getProperty("java.version"));
		System.out.println(
				"Java �̃C���X�g�[����f�B���N�g��  =>" + System.getProperty("java.home"));
		System.out.println(
				"���[�U�̃z�[���f�B���N�g��         =>" + System.getProperty("user.home"));
		System.out.println(
				"���[�U�̌��݂̍�ƃf�B���N�g��     =>" + System.getProperty("user.dir"));
		System.out.println("�f�t�H���g�ꎞ�t�@�C���̃p�X       =>"
				+ System.getProperty("java.io.tmpdir"));
				/*
				 * try{ System.out.println("Java �N���X�p�X    =>" +
				 * System.getProperty("java.class.path") ); System.out.println(
				 * "�ꎞ�t�@�C���̃p�X =>" + System.getProperty("java.io.tmpdir") );
				 * System.out.println("�g��Dir�̃p�X      =>" +
				 * System.getProperty("java.ext.dirs") ); System.out.println(
				 * "�n�r�o�[�W����     =>" + System.getProperty("os.version") );
				 * System.out.println("�s      ��؂蕶�� =>" +
				 * System.getProperty("line.separator") System.out.println(
				 * "���C�u�����̃��[�h���Ɍ�������p�X�̃��X�g =>" +
				 * System.getProperty("java.library.path") ); }catch(Exception
				 * ee){ ee.printStackTrace(); }
				 */

		// System.out.println("�s��؂蕶�� ex.CRLF =>" +
		// System.getProperty("line.separator") );
		// System.out.println("�� Runtime�̎d�l�o�[�W����=>" +
		// System.getProperty("java.specification.version") );
		// System.out.println("JIT �R���p�C���̖��O "+
		// System.getProperty("java.compiler") );
		// System.out.println("Java �x���_�[�� URL "+
		// System.getProperty("java.vendor.url") );
		// System.out.println("���z�}�V���̎d�l�x���_�[ "+
		// System.getProperty("java.vm.specification.vendor") );
		// System.out.println("���z�}�V���̎d�l�� "+
		// System.getProperty("java.vm.specification.name") );
		// System.out.println("���z�}�V���̎����o�[�W����"+
		// System.getProperty("java.vm.version") );
		// System.out.println("���z�}�V���̎����x���_�[ "+
		// System.getProperty("java.vm.vendor") );
		// System.out.println("���z�}�V���̎����� "+ System.getProperty("java.vm.name")
		// );
		// System.out.println("Runtime�̃x���_�[ "+ System.getProperty("java.vendor")
		// );
		// System.out.println("Runtime�̎d�l�̃x���_�[ "+
		// System.getProperty("java.specification.vendor") );
		// System.out.println("Runtime�̎d�l�� "+
		// System.getProperty("java.specification.name") );
		// System.out.println("�N���X�̌`���̃o�[�W���� "+
		// System.getProperty("java.class.version") );
		// System.out.println("�g���f�B���N�g���̃p�X "+
		// System.getProperty("java.ext.dirs") );
		// System.out.println("OS�̃o�[�W���� "+ System.getProperty("os.version") );
		// System.out.println("OS�̃A�[�L�e�N�`�� "+ System.getProperty("os.arch") );
		// System.out.println("Byte .MAX_VALUE =>"+Byte.MAX_VALUE);
		// System.out.println("Short .MAX_VALUE =>"+Short.MAX_VALUE);
		// System.out.println("Character.MAX_VALUE =>"+Character.MAX_VALUE);
		// System.out.println("Integer .MAX_VALUE =>"+Integer.MAX_VALUE);
		// System.out.println("Long .MAX_VALUE =>"+Long.MAX_VALUE);
	}

	// -------------------------------------------------------------------------
	// �v���p�e�B�i���ϐ��Ȃǁj�̖��O�ƒl�̈ꗗ
	// System.out.println("�����s���̏�� \n"+envInfo());
	// -------------------------------------------------------------------------
	private static String envInfoEnum() {
		StringBuffer sBuf = new StringBuffer();
		Properties env = System.getProperties(); // �V�X�e���̃v���p�e�B�擾
		Iterator names = (Iterator) env.propertyNames(); // ���O�̃��X�g�擾
		while (names.hasNext()) {
			String name = names.next().toString();
			String value = env.getProperty(name);
			sBuf.append(name + '\t' + ":" + value + "\n");
			// System.out.println( name + '\t' + ":" + value + "\n");
		}
		return sBuf.toString();
	}

}
