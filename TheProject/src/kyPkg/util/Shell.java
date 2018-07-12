package kyPkg.util;

import java.io.*;
import java.util.*;

import globals.ResControl;
import globals.ResControlWeb;
import kyPkg.uCodecs.UnicodeMod;

//---------------------------------------------------------------------------------
// ���s���ł̃V�G���R�}���h�����s����
//---------------------------------------------------------------------------------
public class Shell {
	private static boolean DEBUG = false;

	// private static boolean DEBUG = true;
	/***************************************************************************
	 * �C���i�[�N���X (XShell�Ŏg�p)�v���Z�X�̃^�C���A�E�g���Ď�����
	 **************************************************************************/
	// ***********************************************************************
	// * �s�g�p��t
	// * Process proc = Runtime.getRuntime().exec("notepad");
	// * ElapseCtrl ec = new ElapseCtrl((1000*30),proc);
	// **********************************************************************/
	public class ElapseCtrl extends Thread {
		private Process process;
		private long beginT = 0; // long�̃f�t�H���g�͉��������ˁH�H
		private long timeout = 1000 * 30; // �Ƃ肠����30�b�Ƃ���
		private long elapse = 0;
		private boolean alive = true;

		public ElapseCtrl(Process proc) {
			this(1000 * 30, proc);
		}

		public ElapseCtrl(int timeout, Process proc) {
			// ����proc���k����������ǂ�����H�H
			this.process = proc;
			this.timeout = timeout;
			this.beginT = System.currentTimeMillis();
			if (DEBUG)
				System.out.println("# this.timeout:" + this.timeout);
			this.start();
			if (DEBUG)
				System.out.println("# check point1");
		}

		// �^�C���A�E�g�O�ɏ������I������ꍇ�ɂ�����R�[������
		public long quit() {
			elapse = System.currentTimeMillis() - beginT;
			alive = false;
			// this.interrupt(); // �ǂ��炪�悢���낤���H
			return elapse;
		}

		// �^�C���A�E�g���Ď�������
		@Override
		public void run() {
			if (DEBUG)
				System.out.println("# check point2 Run!");
			while (alive) {
				elapse = System.currentTimeMillis() - beginT;
				if (DEBUG)
					System.out.println("# in Loop elapse:" + elapse);
				if (process == null)
					return;
				if (elapse > this.timeout) {
					System.out.println("# TimeOUT!! " + elapse);
					alive = false;
					process.destroy(); // �I���������Ȃ��ꍇ������
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
					return;
				}
			}
		}

		public long getElapse() {
			return elapse;
		}
	}

	/***************************************************************************
	 * (XShell�Ŏg�p)�v���Z�X�̕W���o�͂̎󂯂��� Runtime#exec(String)���\�b�h�Ńv���Z�X�𑖂点��ۂɂ���
	 * getInpuStream �� getErrorStream ��ʃX���b�h�����Ŕ񓯊��ɓǂݎ�� �i�ǂ܂Ȃ��ƊO���v���Z�X����~���ăn���O����j
	 **************************************************************************/
	class AutoReader extends Thread {
		InputStream stream;

		StringBuffer sBuff;

		AutoReader(InputStream sr) {
			stream = sr;
		}

		@Override
		public void run() {
			// System.out.println("## AutoReader ##");
			try {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(stream));
				sBuff = new StringBuffer();
				String line = null;
				while ((line = br.readLine()) != null) {
					sBuff.append(line).append("\n");
				}
				br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				System.exit(-1);
			}
		}

		public String getBuff() {
			return sBuff.toString();
		}
	}

	/***************************************************************************
	 * (XShell�Ŏg�p)�v���Z�X�̕W���o�͂̎󂯂��� ���͑҂����������̂Ɏg�p�ł��Ȃ����E�E�E �s�g�p��t AutoWriter
	 * sOtThread = new AutoWriter(proc.getOutputStream());
	 **************************************************************************/
	class AutoWriter extends Thread {
		OutputStream stream;

		BufferedWriter bw;

		String[] message = null;

		public void setMessage(String message) {
			this.setMessage(new String[] { message });
		}

		public void setMessage(String[] message) {
			this.message = message;
		}

		AutoWriter(OutputStream sr) {
			stream = sr;
			bw = new BufferedWriter(new OutputStreamWriter(stream));
		}

		@Override
		public void run() {
			// System.out.println("## AutoWriter ##");
			if (bw == null) {
				System.out.println("AutoWriter:oBw is Null");
				return;
			}
			try {
				for (int i = 0; i < message.length; i++) {
					if (DEBUG)
						System.out.println("AutoWriter�@<message>" + message[i]
								+ "</message>");
					bw.write(message[i], 0, message[i].length());
					// oBw.flush();
				}
				bw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				System.exit(-1);
			}
		}

		public void close() {
			try {
				bw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/***************************************************************************
	 * �O���R�}���h��ʃX���b�h�Ŏ��s����<br>
	 * �s�g�p��t cmd.exe ipconfig cmd.exe net use
	 **************************************************************************/
	private class XShell extends Thread {
		private Process proc; // �v���Z�X

		private AutoWriter stiWriter = null;

		private AutoReader stoReader = null;

		private AutoReader errreader = null;

		// ---------------------------------------------------------------------
		private String command = ""; // �����z��ɂ��������E�E�E�ǂ��ł��傤��

		private String stdout = ""; // �W���f�[�^�o�͂ɑ|���o���ꂽ���e

		private String stderr = ""; // �W���G���[�o�͂ɑ|���o���ꂽ���e

		// private int status;

		// ---------------------------------------------------------------------
		// �A�N�Z�b�T
		// ---------------------------------------------------------------------
		public String getStdout() {
			return stdout;
		}

		public String getCommand() {
			return command;
		}

		public void setCommand(String command) {
			this.command = command;
		}

		// ---------------------------------------------------------------------
		// �R���X�g���N�^
		// ---------------------------------------------------------------------
		// public XShell() {
		// super();
		// }
		// public XShell(String command) {
		// super();
		// this.command = command;
		// }

		public XShell(ThreadGroup group, String name) {
			super(group, name);
		}

		// public String getStderr() {
		// return stderr;
		// }
		// public int getStatus() {
		// return status;
		// }
		// //
		// ---------------------------------------------------------------------
		// // �v���Z�X�̒��f�i�ł��Ȃ����Ƃ�����炵���E�E�j
		// //
		// ---------------------------------------------------------------------
		// public void destroyProccess() {
		// if (proc != null) {
		// System.out.println("��DESTOROY");
		// proc.destroy();
		// } else {
		// System.out.println("���v���Z�X��������܂���ł���");
		// }
		// }

		// ---------------------------------------------------------------------
		@Override
		public void run() {
			try {
				// �����ŃR�}���h���󂩂ǂ����m�F�����ق����悢���H
				if (DEBUG)
					System.out.println("<<@Shell>>��command:" + command);
				// �v���Z�X���s�O�ɋ󂫃��������m�F����ق����悢���낤���H�H
				proc = Runtime.getRuntime().exec(command);
				// System.out.println("# �v���Z�X���Ď� start");
				// �v���Z�X���Ď��A�^�C���A�E�g�����ꍇ�����I��������
				ElapseCtrl ec = new ElapseCtrl((1000 * 30), proc);
				// �v���Z�X�̕W�����o�̓X�g���[�����擾
				stoReader = new AutoReader(proc.getInputStream()); // �W���o��
				errreader = new AutoReader(proc.getErrorStream()); // �W���G���[
				stiWriter = new AutoWriter(proc.getOutputStream());// �W������
				// ���͑҂���ԂɂȂ�̂����m�ł��Ȃ����H������ł��Ȃ�����������
				stiWriter.setMessage("\n");// �󑗐M
				// System.out.println("�� start() ��");
				stiWriter.start();
				stoReader.start();
				errreader.start();
				// System.out.println("�� join() ��");
				stoReader.join(); // �W�� �o�͂̓ǂݍ��݃X���b�h��҂�
				errreader.join(); // �G���[�o�͂̓ǂݍ��݃X���b�h��҂�
				stiWriter.join(); // �G���[�o�͂̓ǂݍ��݃X���b�h��҂�
				// System.out.println("��proc.waitFor before ");
				proc.waitFor(); // �v���Z�X�̏I����҂� ...
				long elapse = ec.quit(); // �Ď��v���O�������I��点��
				if (DEBUG)
					System.out.println("<<@Shell>>��elapse:" + elapse);
				ec = null;
				stdout = stoReader.getBuff();
				stderr = errreader.getBuff();
				if (DEBUG)
					System.out.println("<<@Shell>>��stdout:\n" + stdout);
				if (DEBUG)
					System.out.println("<<@Shell>>��stderr:\n" + stderr);
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}

	// ���s���������������Ȃ����H�H
	// ���� => ���� => ���� => �N�ɁH=>���[��
	public String execute(List list) {
		String[] array = (String[]) list.toArray(new String[list.size()]);
		return execute(array);
	}

	public String execute(String command, boolean option) {
		if (option) {
			// �^�����ɕύX���Ă�����s������I�v�V����
			return execute(new String[] { command.replaceAll("/", "\\\\") });

		} else {
			return execute(new String[] { command });

		}
	}

	public String execute(String command) {
		return execute(new String[] { command });
	}

	public String execute(String[] commands) {
		int count = 0;
		// -------------------------------------------------------------------------------
		// String osName = System.getProperty("os.name");
		// �n�r�̎�ʂ𔻒肷��K�v�͂��邩�H
		// ���Ƃ��΂n�r�̎�ʂɂ���āE�E
		// NT base�̏ꍇ
		// cmd.exe /c dir
		// 95 base�̏ꍇ
		// command.com /c dir
		// -------------------------------------------------------------------------------
		if (RuntimeEnv.memoryInfo() > 98) {
			System.out.println("���������A�b�v�A�b�v�ł�");
			System.out.println("�ߕ��ׂł͂Ȃ��낤���H");
		}
		// -------------------------------------------------------------------------------
		// �X���b�h�O���[�v�ɂ���Ӗ��͂���̂��H
		// -------------------------------------------------------------------------------
		// XShell ���X���b�h�O���[�v�ɂ��āA�x�N�^�[�ɓ���
		// �S������ɑ��X������郁�b�Z�[�W���Ƃ肾���B
		// -------------------------------------------------------------------------------
		ThreadGroup thGroup = new ThreadGroup("freakoutGroup");
		List jQue = new ArrayList();
		for (int k = 0; k < commands.length; k++) {
			if (!commands[k].trim().equals("")) {
				XShell xCom = new XShell(thGroup, "#" + k);

				String command = UnicodeMod.charsetConv(commands[k]);
				// if (DEBUG)
				//	System.out.println("DEBUG <<@Shell>> command:" + command);
				xCom.setCommand(command);
				xCom.start();

				// //�^�C�����O�����銴���Ȃ̂ŁE�E�E������ƐQ�����Ă݂�E�E�E?!
				// try {
				// Thread.sleep(1000);
				// } catch (Exception e) {
				// e.printStackTrace();
				// }

				jQue.add(xCom);
				try {
					xCom.join(); // �҂����킹�H�I
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
				;
			}
		}
		// -------------------------------------------------------------------------------
		// ���̎��_�ł̓V�F���v���Z�X�͑����Ă��Ȃ��A�N���X�̃A���P�[�V�����̕��ׂ��H
		// -------------------------------------------------------------------------------
		count = thGroup.activeCount();
		if (DEBUG)
			System.out.println("<<@Shell>>�S���I���܂����@���݃A�N�e�C�u�ȃX���b�h�̐�:" + count);
		// -------------------------------------------------------------------------------
		// List�������X�e�[�^�X�����o����
		// -------------------------------------------------------------------------------
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < jQue.size(); i++) {
			XShell xCom = (XShell) (jQue.get(i));
			// System.out.println("jQue #" + i + " " +xCom.getStdout());
			sb.append("�y" + xCom.getCommand() + "�z\n");
			sb.append("---------------------------------------\n");
			sb.append(xCom.getStdout());
		}
		return sb.toString();
	}

	// -------------------------------------------------------------------------
	// Shell�֘A
	// -------------------------------------------------------------------------
	// ��g�p���
	// myShell("notepad.exe");
	// java Exec cmd.exe ipconfig
	// java Exec cmd.exe net use
	// etc...
	// �A���Idos�R�}���h�͎��ۈȉ��̂悤�ɌĂяo����Ă���̂ŁE�E�E
	// NT base�̏ꍇ
	// java Exec cmd.exe /c dir
	// 95 base�̏ꍇ
	// java Exec command.com /c dir
	// ��������
	// java Exec "C:\Program Files\Microsoft Office\Office10\EXCEL.EXE"
	// -------------------------------------------------------------------------
	// �G�N�Z���Ȃ񂩂̓t���p�X�Ő؂�Ȃ��Ɠ����Ȃ��J���H�H
	// �t���p�X�Ō�������͍̂�
	// ������which���������Ēu����
	// �g���q�ɂ��N���Ƃ����̂́A�ǂ������J���N���Ȃ̂��E�E�Ewin,mac�Ȃǒ��ׂ�
	// ���W�X�g���]�X���͉��邪�A��ւ�����@�������best�Ȃ�
	// -------------------------------------------------------------------------
	public static void exec(String command) {
		exec(new String[] { command });
	}

	public static void exec(String[] commands) {
		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(commands);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = br.readLine()) != null)
				System.out.println(line);
		} catch (IndexOutOfBoundsException e) {
			System.err.println("Usage:java Exec command [args...]");
			System.exit(-1);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error:" + e);
		}
	}

	// -------------------------------------------------------------------------
	private static void TestGnuplot() {
		try {
			/*
			 * "set terminal table \n" + "set output \"" + GRAPH_FILE + "\"\n" +
			 * "plot sin(x) \n" + "quit";
			 */
			String GRAPH_FILE = "\"test.gif\"";
			String gnuplot_cmd = "set terminal gif size 300,300�@\n"
					+ "set output " + GRAPH_FILE + "�@\n"
					+ "plot x*sin(x)                �@�@�@�@�@�@�@�@\n" + "quit ";
			Process p = Runtime.getRuntime().exec("pgnuplot");
			PrintWriter gp = new PrintWriter(p.getOutputStream());
			gp.print(gnuplot_cmd);
			gp.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] argv) {
		// test0();
		// testPython();
		//		test20130314();
		test20160926();
	}

	public static void test20160926() {
		// NG 
		String command = "cmd.exe /c start c:\\xxmixit.bat";
		//		String command="cmd.exe /c c:\\xxmixit.bat";
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//		List<String> list = new ArrayList();
		//		list.add("cmd.exe date"); // NG?!
		//		list.add("cmd.exe /c dir");
		//		list.add("ipconfig");
		////		list.add("netstat -a");
		////		list.add("net use");
		////		list.add("cmd.exe tree > xtree.txt");
		////		list.add("sqlite3 QPRDB \".read sql.txt\"");
		//		new Shell().execute(list);
	}

	public static void test20130314() {
		String dir = ResControl.getUsersItpDir() + "#340018212101/";
		String dbpath = dir + "QPR.Db";
		List<String> list = new ArrayList();
		list.add("sqlite3 -separator , " + dbpath + " \".read " + dir
				+ "create.sql\"");
		list.add("sqlite3 -separator , " + dbpath + " \".read " + dir
				+ "inport.sql\"");
		new Shell().execute(list);
	}

	public static void test0() {
		String dir = ResControl.getUsersItpDir() + "504191000002/";
		String dbpath = dir + "QPR.Db";
		List<String> list = new ArrayList();
		list.add("sqlite3 -separator , " + dbpath + " \".read " + dir
				+ "create.sql\"");
		list.add("sqlite3 -separator , " + dbpath + " \".read " + dir
				+ "inport.sql\"");
		new Shell().execute(list);
	}

	public static void test0_s() {
		// �o�b�`�ł���Ă������G���[�ƂȂ����̂ŃG���R�[�h�̖��ł͂Ȃ��V���[�g�p�X�̖�肾�Ǝv��
		// java�ŃV���[�g�p�X���E�����Ƃ͉\���H�H
		String command = "";
		// String dir = "./forTest/";
		// �t�H���_�[���̊����̖��H�H�@�@unable to open database file�H
		String commonDir = ResControlWeb.getD_Resources_USER_ITP_COMMON();
		String dir = commonDir + "110098000002_�����}�s�d�S�^�O�X�O�V�ӂ��Ĉ��ރ[���[����/";
		String dbpath = dir + "QPR.Db";
		List<String> list = new ArrayList();
		// # sqlite3 -separator , DBNAME ".import FILENAME TABLENAME"
		// list.add("sqlite3 QPRDB \".read sql.txt\""});
		// list.add("sqlite3 -separator , QPRDB \".read sql.txt\"");
		command = "sqlite3 -separator , " + dbpath + " \".read " + dir
				+ "sql.txt\"";
		command = UnicodeMod.charsetConv(command);
		System.out.println(command);
		list.add(command);
		// list.add("sqlite3 -separator , " + dbpath + " \".import " + dir +
		// "JAN.txt JAN\"");
		// list.add("sqlite3 -separator , " + dbpath + " \".import " + dir +
		// "MK.txt MK\"");
		new Shell().execute(list);
	}

	public static void testx() {
		List<String> list = new ArrayList();
		list.add("cmd.exe date"); // NG?!
		list.add("cmd.exe /c dir");
		list.add("ipconfig");
		list.add("netstat -a");
		list.add("net use");
		list.add("cmd.exe tree > xtree.txt");
		list.add("sqlite3 QPRDB \".read sql.txt\"");
		new Shell().execute(list);
	}

	public static void test1() {
		Shell.TestGnuplot();
	}

	public static void test2() {
		// Shell.exec(new String[]{"cmd.exe","ipconfig"}); NG
		// Shell.exec(new String[]{"cmd.exe","netstat"}); NG
		// Shell.exec(new String[]{"cmd.exe","net","use"}); NG
		Shell.exec(new String[] { "cmd.exe", "/c", "dir" });
	}

	public static void test3() {
		try {
			Process proc = Runtime.getRuntime().exec("notepad");
			new Shell().new ElapseCtrl((1000 * 30), proc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void testPython() {
		try {
			Process proc = Runtime.getRuntime().exec("python hello.py");
			new Shell().new ElapseCtrl((1000 * 30), proc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
