package kyPkg.socks;

import java.net.*;

import static kyPkg.uFile.FileUtil.*;

import java.io.*;
import java.util.regex.*;

public class TinyURL {

	public static final String SAMPLE_URL = "http://ll.jus.or.jp/2008/info/xgihyo";

	private static final Pattern TINYURL_PAT = Pattern
			.compile("http://tinyurl\\.com/[a-z0-9-]+");

	private static final String CONVURL = "http://tinyurl.com/create.php";

	public static String tinyURL(String url, String alias) throws IOException {
		URL tinyUrl = new URL(CONVURL);
		HttpURLConnection uc = (HttpURLConnection) tinyUrl.openConnection();
		String query = "url=" + URLEncoder.encode(url, UTF_8);
		if (alias != null)
			query += "&alias=" + URLEncoder.encode(alias, UTF_8);
		byte[] ba = query.getBytes("us-ascii");
		uc.setRequestMethod("POST");
		uc.setDoOutput(true);
		uc.setFixedLengthStreamingMode(ba.length);
		uc.connect();
		try {
			OutputStream os = uc.getOutputStream();
			os.write(ba);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(uc.getInputStream(), ISO_8859_1));
			String line;
			while ((line = br.readLine()) != null) {
				Matcher m = TINYURL_PAT.matcher(line);
				if (m.find()) {
					return m.group();
				}
			}
			return null;
		} finally {
			uc.disconnect();
		}
	}

	public static String tinyURL(String url) throws IOException {
		return tinyURL(url, null);
	}

	public static void main(String[] args) throws IOException {
		System.out.println(tinyURL(SAMPLE_URL));
	}
}
