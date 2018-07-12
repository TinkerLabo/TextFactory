package kyPkg.socks;

import java.io.*;
import java.net.*;
import java.util.*;
import static kyPkg.uFile.FileUtil.*;

public class MultipartSendai {
	// Multipart01　と　MultipartSendaiを合わせて使いやすいクラスを作っておく、これを指定品目のアップロードなどに利用する予定2009/09/07
	// 複数ファイルのアップロードにも対応したい！！
	private String boundary;

	private String text;

	private String exsistingFileName;

	// 一旦テンポラリに掃き出すと遅いんだろうか？？＞サイズは確認できるだろう
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
		text = "テキスト";
		String rootDir = globals.ResControl.getQprRootDir();
		exsistingFileName = rootDir + "files/file.zip";
		String url = "http://example.com/upload.do";

		// ダミーに書き込んで、データ量を調べる
		DummyOutputStream dummy = new DummyOutputStream();
		sendMultipart(dummy);

		int contentLength = dummy.getSize();

		HttpURLConnection conn = connect(url, contentLength);

		// 実際にデータを送信する
		OutputStream out = conn.getOutputStream();
		sendMultipart(out);

		read_The_SERVER_RESPONSE(conn);

		conn.disconnect();

	}

	private HttpURLConnection connect(String url, int contentLength) {
		// http://java.sun.com/j2se/1.5.0/ja/docs/ja/api/java/net/HttpURLConnection.html#setFixedLengthStreamingMode%28int%29
		// 接続
		// URL urlObj = new URL("http://example.com/upload.do"); // 送信先
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
		} // 送信先
		catch (IOException e) {
			e.printStackTrace();
		}
		return conn;
	}

	private void sendMultipart(OutputStream out) throws IOException {
		File file = new File(exsistingFileName);
		String charset = defaultEncoding2;
		// テキストフィールド送信
		out.write(("--" + boundary + "\r\n").getBytes(charset));
		out.write(("Content-Disposition: form-data; name=\"text\"\r\n")
				.getBytes(charset));
		out.write(("Content-Type: text/plain; charset=Shift_JIS\r\n\r\n")
				.getBytes(charset));

		// ココで何をやっているのか解らない・・・・？！これは必要なのだろうか？？？謎だ（取っ払って動作確認してみる）
		out.write((text).getBytes(charset));
		out.write(("\r\n").getBytes(charset));

		// ファイルフィールド送信
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
		// 送信終わり
		out.write(("--" + boundary + "--").getBytes(charset));
		try {
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.close();
	}

	void read_The_SERVER_RESPONSE(HttpURLConnection conn) throws IOException {
		// レスポンスを受信 (これをやらないと通信が完了しない)
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
