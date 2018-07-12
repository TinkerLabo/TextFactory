package kyPkg.socks;

import static kyPkg.uFile.ListArrayUtil.file2List;
import java.net.*;
import java.io.*;
import java.util.*;
import globals.AGC_SMTP;
import globals.ResControl;

//----------------------------------------------------------------------------------------
// �s���[�����M�v���O�����t
// �O���R�}���h�̎��s���O�����̂܂܃��[���]�����Ă݂�
// ���O�͈�U�t�@�C���ɂ��Ă���
// �Ƃ������ƂŁE�E���M��A�^�C�g���A�t�@�C�������w�肷�邱�ƂŃ��[�����M����
// �v���O�����Ƃ���
//----------------------------------------------------------------------------------------
public class SMTPmini {
	private static final String DEFAULT_EMAIL = "mi-gotoh@tokyu-agc.co.jp"; //TODO	���[�����O���X�g�̃A�J�E���g�ɂ��Ă��炦��΃x�X�g��
	private static final String ARG_ERROR = "#Error �������R�K�v�ł� =>";
	private static final String SAMPLE = "�၄java -jar SMTPmini.jar emailAddr subject MailFilePath�@";
	private Socket sock;
	private BufferedReader smtpIn;
	private PrintWriter smtpOut;

	// -----------------------------------------------------------------------------------
	// setPortAndServer �T�[�o�[������у|�[�g�ԍ����O�����i�l�s�`��yml���j
	// -----------------------------------------------------------------------------------
	// 20150309�@MTA�ύX�ɔ�����Ɓ@ SMTP_SERVER
	// ��null�Ȃ�O���t�@�C�����Q�Ƃ���A�t�@�C�������݂��Ȃ���΃f�t�H���g��ݒ肷��
	// -----------------------------------------------------------------------------------
	// �R���X�g���N�^
	// -----------------------------------------------------------------------------------
	public SMTPmini(String frm_email, String to_email, String subject,
			List msgBody) {
		// XXX �������ł���悤�ɂ�����
		this(frm_email, Arrays.asList(to_email.split(",")), subject, msgBody);
		//#createTester--------------------------------------------------
		System.out.println("public static void testSMTPmini() {");
		System.out.println("    String to_email = \"" + to_email + "\";");
		System.out.println("    String subject = \"" + subject + "\";");
		System.out.println("    List msgBody = " + msgBody + ";");
		System.out.println(
				"    SMTPmini ins = new SMTPmini(to_email,subject,msgBody);");
		System.out.println("}");
		//--------------------------------------------------
	}

	// -----------------------------------------------------------------------------------
	// ���b�Z�[�W�͊O���t�@�C�����g�p�����
	// -----------------------------------------------------------------------------------
	public SMTPmini(String frm_email, String to_email, String subject,
			String path) {
		this(frm_email, Arrays.asList(to_email.split(",")), subject,
				file2List(path));
	}

	// -----------------------------------------------------------------------------------
	// ����A�h���X ������
	// -----------------------------------------------------------------------------------
	private SMTPmini(String frm_email, List addrList, String subject,
			List msgBody) {
		if (addrList.size() > 0) {
			try {
				session(frm_email, subject, addrList, msgBody);
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}
	}

	// -----------------------------------------------------------------------------------
	// send���\�b�h ���M
	// -----------------------------------------------------------------------------------
	private void send(String command) {
		smtpOut.print(command + "\r\n"); // �R�}���h�̑��M
		smtpOut.flush();
		// System.out.println("send> " + command); // ���M���e�̕\��
	}

	// -----------------------------------------------------------------------------------
	// recv���\�b�h ��M
	// -----------------------------------------------------------------------------------
	private boolean recv(int success_code) {
		try {
			String res = smtpIn.readLine(); // �����R�[�h�̓ǂݎ��
			// System.out.println("recv> " + res);
			if (Integer.parseInt(res.substring(0, 3)) != success_code) {
				sock.close();// �R�l�N�V��������܂�
				throw new RuntimeException(res);
			}
		} catch (IOException ie) {
			ie.printStackTrace();
			System.out.println("�R�}���h��M��io�G���[�F");
			// throw new RuntimeException(res);
			return false;
		}
		return true;
	}

	// -----------------------------------------------------------------------------------
	// SMTP�̃Z�b�V����
	// -----------------------------------------------------------------------------------
	private void session(String frm_email, String subject, List<String> toAddr,
			List<String> msgBody) throws IOException {
		if (msgBody == null) {
			msgBody = new ArrayList();
			msgBody.add("#ERROR message Body is NULL...!?");
		}
		String myname = "";
		try {
			myname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException ue) {
			ue.printStackTrace();
			return;
		}
		// -------------------------------------------------------------------------------
		// CONNECT
		// -------------------------------------------------------------------------------
		sock = new Socket(AGC_SMTP.getHost(), AGC_SMTP.getPort());
		smtpIn = new BufferedReader(
				new InputStreamReader(sock.getInputStream()));
		smtpOut = new PrintWriter(sock.getOutputStream());
		if (recv(220) == false) {
			System.out.println("�ڑ��G���[");
			return;
		}
		// -------------------------------------------------------------------------------
		// HELO�R�}���h ���M���R���s���[�^���̒ʒm
		// -------------------------------------------------------------------------------
		// send("HELO HOST.COM");
		send("HELO " + myname);
		recv(250);
		// -------------------------------------------------------------------------------
		// MAIL FROM�R�}���h �ԐM��𑊎�ɒʒm
		// -------------------------------------------------------------------------------
		send("MAIL FROM:" + frm_email);
		recv(250);
		// -------------------------------------------------------------------------------
		// RCPT TO�R�}���h ���[����M�҂̎w��
		// -------------------------------------------------------------------------------
		for (Iterator iterator = toAddr.iterator(); iterator.hasNext();) {
			String mailAddr = (String) iterator.next();
			if (mailAddr == null)
				mailAddr = "";
			mailAddr = mailAddr.trim();
			if (!mailAddr.equals("")) {
				send("RCPT TO:" + mailAddr);
				recv(250);
			}
		}
		// for (int i = 0; i < toAddr.length; i++) {
		// send("RCPT TO:" + toAddr[i]);
		// recv(250);
		// }
		// -------------------------------------------------------------------------------
		// DATA�R�}���h ���[���f�[�^���M�J�n�̒ʒm
		// -------------------------------------------------------------------------------
		send("DATA");
		recv(354);
		// -------------------------------------------------------------------------------
		// Mail_Header
		// -------------------------------------------------------------------------------
		send("From: ## QPR Message ####### <" + frm_email + "> ");
		// send("To: " + to[0]);

		//2016-07-20 cc���������Ă݂�E�E�E����ł����̂��낤�� 
		send("To:" + toAddr.get(0));
		for (int i = 1; i < toAddr.size(); i++) {
			send("CC:" + toAddr.get(i));
		}

		send("Subject:" + subject);
		send("MIME-Version: 1.0");
		send("X-Mailer: P.E.A.C.E. mail ver.777 ");
		send("Content-Type: text/plain; charset=iso-2022-jp");
		send(""); // �w�b�_�̌�ɂ͋󔒂̍s���K�v
		// -------------------------------------------------------------------------------
		// Mail_Body
		// -------------------------------------------------------------------------------
		for (Iterator iterator = msgBody.iterator(); iterator.hasNext();) {
			String message = (String) iterator.next();
			send(message);
		}
		// for(int i = 0;i < msgBody.length;i++) {
		// send(msgBody[i]);
		// }
		send("\r\n.");
		recv(250); // ���[�����b�Z�[�W�̏I��
		// -------------------------------------------------------------------------------
		// QUIT�R�}���h �Z�b�V�����̏I��
		// -------------------------------------------------------------------------------
		send("QUIT");
		recv(221);
		// -------------------------------------------------------------------------------
		// �R�l�N�V�����̃N���[�Y
		// -------------------------------------------------------------------------------
		sock.close();
		System.out.println("<fin>");
	}

	// -----------------------------------------------------------------------------------
	// main���\�b�h �s���̃N���X�̎g�p��t
	// -----------------------------------------------------------------------------------
	public static void main(String[] args) {
		//		testSMTPmini1(); //for test
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("SMTPmini Elapse");
		elapse.start();
		kickAsBatch(args);
		elapse.setComment("COMMENT");
		elapse.stop();
	}

	public static void kickAsBatch(String[] args) {
		if (args.length < 3) {
			System.out.println(ARG_ERROR + SAMPLE);
			System.exit(9);
		}
		String frm_email = DEFAULT_EMAIL;
		if (args.length == 4) {
			frm_email = args[3];
		}
		new kyPkg.socks.SMTPmini(frm_email, args[0], args[1], args[2]);
	}

	// -----------------------------------------------------------------------------------
	// XXX �O���̃e�X�g�p�N���X�Ɉȉ��̃��\�b�h���ړ������Ă���
	// testSMTPmini1();
	// testSMTPmini2();
	// -----------------------------------------------------------------------------------
	public static void testSMTPmini2() {
		String mailTo = DEFAULT_EMAIL; // ���M�惁�[���A�h���X�i�Վ��j
		String mailFrm = DEFAULT_EMAIL; // ���M�惁�[���A�h���X�i�Վ��j
		String subject = "䕂̂��莆�Q�R";
		String path = ResControl.D_QPR + "LOG/ITEM_MM_clan.log";
		new kyPkg.socks.SMTPmini(mailFrm, mailTo, subject, path);
	}

	public static void testSMTPmini1() {
		String mailAddr1 = DEFAULT_EMAIL; // ���M�惁�[���A�h���X�i�Վ��j
		List<String> mailList = new ArrayList();
		mailList.add(mailAddr1);
		String subject = "䕂̂��莆�Q�R�k����������";
		String[] msgBody = { "䕂̂��莆�Q�R�@ ", "", "Hello, my love",
				"I heard a kiss from you", "Red magic satin playing near, too",
				"All through the morning rain",
				"I gaze - the sun doesn't shine -",
				"Rainbows and waterfalls run through my mind",
				"In the garden - I see west", "Purple shower, bells and tea",
				"Orange birds and river cousins dressed in green",
				"Pretty music I hear - so happy", "And loud - blue flower echo",
				"From a cherry cloud", "Feel sunshine sparkle pink and blue",
				"Playgrounds will laugh", "if you try to ask",
				"Is it cool?, is it cool?", "If you arrive and don't see me",
				"I'm going to be with my baby",
				"I am free - flying in her arms, over the sea",
				"Stained window, yellow candy screen",
				"See speakers of kite - with velvet roses diggin' freedom flight",
				"A present from you - Strawberry letter 22",
				"The music plays, I sit in for a few",
				"Ooh...ooh...ooh...ooh...ooh...",
				"A present from you - Strawberry letter 22",
				"The music plays, I sit in for a few",
				"Ooh...ooh...ooh...ooh...ooh...", "\n\n", "\t\t�a���@�V���M�[�E�I�[�e�B�X" };
		new kyPkg.socks.SMTPmini(mailAddr1, mailList, subject,
				Arrays.asList(msgBody));
	}
}
