package kyPkg.socks;

import java.net.*;
import java.io.*;
import java.util.*;

import kyPkg.filter.EzWriter;
import kyPkg.filter.Inf_oClosure;
import kyPkg.sql.ServerConnecter;

/**
 * Java HTTP �N���C�A���g
 * 
 * @author ken yuasa
 */
// ------------------------------------------------------------------------
// XXX �p�����[�^�͕ʂɑ��邱�Ƃ��\�Ƃ���i�p�����[�^���ʂɎw�肳��Ă���ꍇ�A���̏ꍇ�L�����N�^�Z�b�g���w�肳����H�I�j10/15
// post����p�����[�^�ɑ��o�C�g�������܂܂�Ă���ꍇ�����Ă��܂��E�E�E
// "POST" ����HTTP_CONNECTION=keep-alive�ɂȂ��Ă��܂�cqi�ւ̃p�����[�^��eof���������Ă��Ȃ�����������
// ------------------------------------------------------------------------
public class HttpClient {
	private String method = "GET";;
	private HttpAuthenticator http_authenticator = null;
	private URL urlObj = null;
	private int responseCode;
	private String responseMessage;
	private String defaultcharsetName = "JISAutoDetect";
	private String realm;
	private Map headers;
	private List<String> bodyList;
	private Inf_oClosure writer = null;
	private String params = "";

	// ------------------------------------------------------------------------
	// HttpAuthenticator
	// ------------------------------------------------------------------------
	private class HttpAuthenticator extends Authenticator {
		private String userName;
		private String password;

		public HttpAuthenticator() {
			this(null, null);
		}

		public HttpAuthenticator(String userName, String password) {
			this.userName = userName;
			this.password = password;
		}

		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(userName, password.toCharArray());
		}

		public String myGetRequestingPrompt() {
			return super.getRequestingPrompt();
		}
	}

	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	public HttpClient(String requestMethod, String url) {
		// #createTester--------------------------------------------------
		System.out.println("public static void testHttpClient() {");
		System.out.println(
				"    String requestMethod = \"" + requestMethod + "\";");
		System.out.println("    String url = \"" + url + "\";");
		System.out.println(
				"    HttpClient ins = new HttpClient(requestMethod,url);");
		System.out.println("}");
		// --------------------------------------------------
		this.method = requestMethod.toUpperCase();
		this.http_authenticator = new HttpAuthenticator();
		Authenticator.setDefault(http_authenticator);
		this.setUrl(url);
	}

	private void setUrl(String url) {
		try {
			byte[] bArray;
			try {
				bArray = url.getBytes("UTF8");
				url = new String(bArray);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			this.urlObj = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		// "http://X68000.q-e-d.net/~68user/net/sample/http-auth-digest/secret.html"
	}

	public void write(String outPath, String charsetName) {
		defaultcharsetName = charsetName;
		write(outPath);
	}

	private void write(String outPath) {
		try {
			System.out.println("<<HttpClient>> write start path:" + outPath);
			writer = new EzWriter(outPath);
			writer.open();
			connect();
			writer.close();
			System.out.println("<<HttpClient>> write end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// -------------------------------------------------------------------------
	// �ǂݏo�������G���R�[�h�^�C�v���w�肷�� "utf-8"�Ȃ�
	// US-ASCII 7 �r�b�g ASCII (ISO646-US/Unicode �����Z�b�g�� Basic Latin �u���b�N)
	// ISO-8859-1 ISO Latin Alphabet No. 1 (ISO-LATIN-1)
	// UTF-8 8 �r�b�g UCS �ϊ��`��
	// UTF-16BE 16 �r�b�g UCS �ϊ��`���A�r�b�O�G���f�B�A���o�C�g��
	// UTF-16BE 16 �r�b�g UCS �ϊ��`���A���g���G���f�B�A���o�C�g��
	// UTF-16 16 �r�b�g UCS �ϊ��`���A�I�v�V�����̃o�C�g���}�[�N�Ŏ��ʂ����o�C�g��
	// ?? ISO-2022-JP
	// -------------------------------------------------------------------------
	private List connect() throws Exception {
		return connect(defaultcharsetName);
	}

	// -------------------------------------------------------------------------
	// �၄charsetName="UTF-8"
	// -------------------------------------------------------------------------
	private List<String> connect(String charsetName) throws Exception {
		bodyList = null;
		HttpURLConnection conn;
		try {
			conn = (HttpURLConnection) urlObj.openConnection();
			// con.setUseCaches(false);
			try {
				conn.setRequestMethod(method);
			} catch (ProtocolException e) {
				e.printStackTrace();
			}
			// con.setInstanceFollowRedirects(false);
			conn.setRequestProperty("User-Agent", "tokyuAgencyBr");
			// con.setRequestProperty( "User-Agent", "Mozilla/5.0 (Windows; U;
			// Windows NT 5.1; ja; rv:1.9.0.6) Gecko/2009011913 Firefox/3.0.6"
			// );
			conn.setRequestProperty("Accept-Language",
					"ja,en-us;q=0.7,en;q=0.3");
			conn.setRequestProperty("Accept-Charset",
					"Shift_JIS,utf-8;q=0.7,*;q=0.7");
			// ------------------------------------------------------
			// ISO-2022-JP??
			// con.setRequestProperty( "Host", "tokyu-agc.co.jp" );
			// con.setRequestProperty( "Accept",
			// "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
			// );
			// con.setRequestProperty( "Accept-Encoding", "gzip,deflate" );
			// con.setRequestProperty( "Keep-Alive", "300" );
			// con.setRequestProperty( "Connection", "keep-alive" );
			// con.setConnectTimeout(5000);
			// con.setReadTimeout(5000);
			// ------------------------------------------------------
			// �p�����[�^��ʂ̃G���R�[�h�ő���i���o�C�g�������|������Ȃ��̂ŁE�E�E�j
			if (!params.equals("")) {
				conn.setDoOutput(true);
				// System.out.println("params:" + params);
				byte[] bArray = params.getBytes(charsetName);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				out.write(bArray, 0, bArray.length);
				// System.out.println("out:" + out.toString());
				out.writeTo(conn.getOutputStream());
				// int code = conn.getResponseCode();
				// System.out.println("code:" + code); // 200 �Ȃ�OK
			}
			conn.connect();
			headers = conn.getHeaderFields();
			responseCode = conn.getResponseCode();
			responseMessage = conn.getResponseMessage();
			if (http_authenticator != null) {
				realm = http_authenticator.myGetRequestingPrompt();
			}
			bodyList = new ArrayList();
			BufferedReader br = new BufferedReader(
					new InputStreamReader(conn.getInputStream(), charsetName));
			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				if (writer != null) {
					writer.write(line);
				} else {
					bodyList.add(line);
				}
			}
			br.close();
			conn.disconnect();
		} catch (FileNotFoundException e) {
			System.out.println("## FileNotFoundException:" + charsetName);
			throw e;
		} catch (Exception e) {
			throw e;
		}
		return bodyList;
	}

	public void debug() {
		System.out.println("���X�|���X�w�b�_:");
		Iterator it = headers.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			System.out.println("  " + key + ": " + headers.get(key));
		}
		System.out.println("���X�|���X �R�[�h[" + responseCode + "] " + " ���b�Z�[�W["
				+ responseMessage + "]");
		System.out.println("�v�����v�g(realm)[" + realm + "]");
		if (bodyList != null) {
			System.out.println("\n---- �{�f�B ----");
			for (Iterator iter = bodyList.iterator(); iter.hasNext();) {
				String line = (String) iter.next();
				System.out.println(line);
			}
		}
	}

	// -------------------------------------------------------------------------
	// ��ʕi�ڂ��|�X�g����
	// -------------------------------------------------------------------------
	public static void testHttpClient20150306() {
		String requestMethod = "POST";
		String params = "params=F:GENITP,USERID:ejqp7,CAT:140661,UNIT:001";
		String url = "http://" + ServerConnecter.CURRENT_SERVER
				+ "/QPRWEB/PostControlServlet?" + params;
		;
		HttpClient ins = new HttpClient(requestMethod, url);
	}

	// -------------------------------------------------------------------------
	// getItp.cgi��python�ō��ꂽTOMCAT���cgi
	// -------------------------------------------------------------------------
	public static void testHttpClient() {
		String requestMethod = "GET";
		String url = "http://" + ServerConnecter.CURRENT_SERVER
				+ "/bonzo/cgi-bin/getItp.cgi?itpCode=127720000010";
		HttpClient ins = new HttpClient(requestMethod, url);
		ins.write("c:/test.dat", "MS932");
	}

	public static void main(String[] args) {
		testHttpClient();
	}

}
