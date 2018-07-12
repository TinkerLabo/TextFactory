package kyPkg.socks;

import java.io.IOException;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.imap.IMAPClient;
import org.apache.commons.net.imap.IMAPSClient;

/**
 * This is an example program demonstrating how to use the IMAP[S]Client class.
 * This program connects to a IMAP[S] server, lists its capabilities and shows
 * the status of the inbox.
 * <p>
 * Usage: IMAPMail <imap[s] server hostname> <username> <password> [secure
 * protocol, e.g. TLS]
 * <p>
 */
public final class IMAPMail {
	public static final String DEFAULT_EMAIL = "mi-gotoh@tokyu-agc.co.jp";

	// --------------------------------------------------------------------------------
	// 20150519�@IMAP���g���ꍇ�̃e�X�g�Ȃ�
	// --------------------------------------------------------------------------------
	public static void main(String[] args) {
		String server = "awspemalst01.tokyu-agc.co.jp";
		String username = DEFAULT_EMAIL;
		String password = "ee2RkmJSIpGN3RHsnr7h";
		String[] parms = new String[] { server, username, password };
		test(parms);
	}

	// --------------------------------------------------------------------------------
	// IMAPClient�@�́@API���t�@�����X
	// http://commons.apache.org/proper/commons-net/apidocs/org/apache/commons/net/imap/IMAPClient.html
	// --------------------------------------------------------------------------------
	// ���ʂ͈ȉ��̂悤�ɂȂ���20150519
	// Connecting to server awspemalst01.tokyu-agc.co.jp on 143
	// * OK [CAPABILITY IMAP4rev1 LITERAL+] AWSPEMALST01.tokyu-agc.co.jp
	// Sendmail Mail Store IMAP4rev1 (5.6.4/mstore-5-6-SW-build-1883)
	// AAAA LOGIN *******
	// AAAA OK [CAPABILITY IMAP4rev1 LITERAL+ ACL RIGHTS=texk NAMESPACE UIDPLUS
	// IDLE UNSELECT MULTIAPPEND QUOTA] User logged in
	// AAAB CAPABILITY
	// * CAPABILITY IMAP4rev1 LITERAL+ ACL RIGHTS=texk NAMESPACE UIDPLUS IDLE
	// UNSELECT MULTIAPPEND QUOTA
	// AAAB OK Completed
	// AAAC SELECT inbox
	// * OK [UIDVALIDITY 1431602296] UIDs stay valid
	// * OK [UIDNEXT 3] Next UID
	// * 2 EXISTS
	// * 0 RECENT
	// * FLAGS (\Answered \Flagged \Draft \Deleted \Seen)
	// * OK [PERMANENTFLAGS (\Answered \Flagged \Draft \Deleted \Seen \*)]
	// Permanent flags
	// AAAC OK [READ-WRITE] Completed
	// AAAD EXAMINE inbox
	// * OK [UIDVALIDITY 1431602296] UIDs stay valid
	// * OK [UIDNEXT 3] Next UID
	// * 2 EXISTS
	// * 0 RECENT
	// * FLAGS (\Answered \Flagged \Draft \Deleted \Seen)
	// * OK [PERMANENTFLAGS ()] Permanent flags
	// AAAD OK [READ-ONLY] Completed
	// AAAE STATUS inbox (MESSAGES)
	// * STATUS inbox (MESSAGES 2)
	// AAAE OK Completed
	// AAAF LOGOUT
	// * BYE LOGOUT received
	// AAAF OK Completed
	// --------------------------------------------------------------------------------
	public static void test(String[] args) {
		if (args.length < 3) {
			System.err.println(
					"Usage: IMAPMail <imap server hostname> <username> <password> [TLS]");
			System.exit(1);
		}
		String server = args[0];
		String username = args[1];
		String password = args[2];
		String proto = (args.length > 3) ? args[3] : null;

		IMAPClient imap;

		if (proto != null) {
			System.out.println("Using secure protocol: " + proto);
			imap = new IMAPSClient(proto, true); // implicit
			// enable the next line to only check if the server certificate has
			// expired (does not check chain):
			// ((IMAPSClient)
			// imap).setTrustManager(TrustManagerUtils.getValidateServerCertificateTrustManager());
			// OR enable the next line if the server uses a self-signed
			// certificate (no checks)
			// ((IMAPSClient)
			// imap).setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());
		} else {
			imap = new IMAPClient();
		}
		System.out.println("Connecting to server " + server + " on "
				+ imap.getDefaultPort());

		// ----------------------------------------------------------------
		// We want to timeout if a response takes longer than 60 seconds
		// ----------------------------------------------------------------
		imap.setDefaultTimeout(60000);

		// ----------------------------------------------------------------
		// suppress login details
		// ----------------------------------------------------------------
		imap.addProtocolCommandListener(
				new PrintCommandListener(System.out, true));

		try {
			imap.connect(server);
		} catch (IOException e) {
			throw new RuntimeException("Could not connect to server.", e);
		}

		try {
			// ----------------------------------------------------------------
			// LOGIN ���[�U�[�� �p�X���[�h ���O�C�����s��
			// ----------------------------------------------------------------
			if (!imap.login(username, password)) {
				System.err
						.println("Could not login to server. Check password.");
				imap.disconnect();
				System.exit(3);
			}

			imap.setSoTimeout(6000);
			// ----------------------------------------------------------------
			// CAPABILITY�@�T�[�o�̋@�\�ꗗ��\������B
			// �g���R�}���h��AUTHENTICATE�̔F�ؕ��@�ւ̑Ή����m�F����̂Ɏg�p�����B
			// ----------------------------------------------------------------
			imap.capability();
			// ----------------------------------------------------------------
			// SELECT ���[���{�b�N�X�� ���[���{�b�N�X��I������B�ȍ~�̃��[������́A���̃��[���{�b�N�X���̑���ƂȂ�
			// ----------------------------------------------------------------
			imap.select("inbox");
			// ----------------------------------------------------------------
			// EXAMINE ���[���{�b�N�X�� �ǂݍ��ݐ�p��SELECT���s��
			// ----------------------------------------------------------------
			imap.examine("inbox");
			// ----------------------------------------------------------------
			// STATUS ���[���{�b�N�X�� �擾�X�e�[�^�X �w�肵�����[���{�b�N�X�̃X�e�[�^�X��\������
			// ----------------------------------------------------------------
			imap.status("inbox", new String[] { "MESSAGES" });
			// ----------------------------------------------------------------
			// LOGOUT ���O�A�E�g����
			// ----------------------------------------------------------------
			imap.logout();
			imap.disconnect();
		} catch (IOException e) {
			System.out.println(imap.getReplyString());
			e.printStackTrace();
			System.exit(10);
			return;
		}
	}

}

/* kate: indent-width 4; replace-tabs on; */
