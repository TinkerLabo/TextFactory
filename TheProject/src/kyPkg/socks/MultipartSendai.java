package kyPkg.socks;

import java.io.*;
import java.net.*;
import java.util.*;
import static kyPkg.uFile.FileUtil.*;

public class MultipartSendai {
	// Multipart01�@�Ɓ@MultipartSendai�����킹�Ďg���₷���N���X������Ă����A������w��i�ڂ̃A�b�v���[�h�Ȃǂɗ��p����\��2009/09/07
	// �����t�@�C���̃A�b�v���[�h�ɂ��Ή��������I�I
	private String boundary;

	private String text;

	private String exsistingFileName;

	// ��U�e���|�����ɑ|���o���ƒx���񂾂낤���H�H���T�C�Y�͊m�F�ł��邾�낤
	// http://d.hatena.ne.jp/nacookan/20080108/1199774995
	public class DummyOutputStream extends OutputStream {
		private int size = 0;

		@Override
		public void write(int b) throws IOException {
			size += 1;
		}

		@Override
		public void write(byte[] bytes) throws IOException {
			size += bytes.length;
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			size += len;
		}

		public int getSize() {
			return this.size;
		}

		@Override
		public void flush() {
		}

		@Override
		public void close() {
		}
	}

	private String generateBoundary() {
		String chars = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_";
		Random rand = new Random();
		String boundary = "";
		for (int i = 0; i < 40; i++) {
			int r = rand.nextInt(chars.length());
			boundary += chars.substring(r, r + 1);
		}
		return boundary;
	}

	public MultipartSendai() {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init() throws IOException {
		boundary = generateBoundary();
		text = "�e�L�X�g";
		String rootDir = globals.ResControl.getQprRootDir();
		exsistingFileName = rootDir + "files/file.zip";
		String url = "http://example.com/upload.do";

		// �_�~�[�ɏ�������ŁA�f�[�^�ʂ𒲂ׂ�
		DummyOutputStream dummy = new DummyOutputStream();
		sendMultipart(dummy);

		int contentLength = dummy.getSize();

		HttpURLConnection conn = connect(url, contentLength);

		// ���ۂɃf�[�^�𑗐M����
		OutputStream out = conn.getOutputStream();
		sendMultipart(out);

		read_The_SERVER_RESPONSE(conn);

		conn.disconnect();

	}

	private HttpURLConnection connect(String url, int contentLength) {
		// http://java.sun.com/j2se/1.5.0/ja/docs/ja/api/java/net/HttpURLConnection.html#setFixedLengthStreamingMode%28int%29
		// �ڑ�
		// URL urlObj = new URL("http://example.com/upload.do"); // ���M��
		HttpURLConnection conn = null;
		try {
			URL urlObj = new URL(url);
			conn = (HttpURLConnection) urlObj.openConnection();
			conn.setFixedLengthStreamingMode(contentLength);
			// conn.setDoInput(true); // Allow Inputs
			conn.setDoOutput(true); // Allow Outputs
			// conn.setUseCaches(false); // Don't use a cached copy.
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + boundary);
			conn.connect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} // ���M��
		catch (IOException e) {
			e.printStackTrace();
		}
		return conn;
	}

	private void sendMultipart(OutputStream out) throws IOException {
		File file = new File(exsistingFileName);
		String charset = defaultEncoding2;
		// �e�L�X�g�t�B�[���h���M
		out.write(("--" + boundary + "\r\n").getBytes(charset));
		out.write(("Content-Disposition: form-data; name=\"text\"\r\n")
				.getBytes(charset));
		out.write(("Content-Type: text/plain; charset=Shift_JIS\r\n\r\n")
				.getBytes(charset));

		// �R�R�ŉ�������Ă���̂�����Ȃ��E�E�E�E�H�I����͕K�v�Ȃ̂��낤���H�H�H�䂾�i��������ē���m�F���Ă݂�j
		out.write((text).getBytes(charset));
		out.write(("\r\n").getBytes(charset));

		// �t�@�C���t�B�[���h���M
		out.write(("--" + boundary + "\r\n").getBytes(charset));
		out.write(("Content-Disposition: form-data; name=\"file\"; filename=\"")
				.getBytes(charset));
		out.write((file.getName()).getBytes(charset));
		out.write(("\"\r\n").getBytes(charset));
		out.write(("Content-Type: application/octet-stream\r\n\r\n")
				.getBytes(charset));
		InputStream in = new FileInputStream(file);
		byte[] bytes = new byte[1024];
		while (true) {
			int ret = in.read(bytes);
			if (ret == 0)
				break;
			out.write(bytes, 0, ret);
			out.flush();
		}
		out.write(("\r\n").getBytes(charset));
		in.close();
		// ���M�I���
		out.write(("--" + boundary + "--").getBytes(charset));
		try {
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.close();
	}

	void read_The_SERVER_RESPONSE(HttpURLConnection conn) throws IOException {
		// ���X�|���X����M (��������Ȃ��ƒʐM���������Ȃ�)
		InputStream stream = conn.getInputStream();
		BufferedReader br = new BufferedReader(
				new InputStreamReader(stream));
		String responseData = null;
		while ((responseData = br.readLine()) != null) {
			System.out.print(responseData);
		}
		stream.close();
	}

}
