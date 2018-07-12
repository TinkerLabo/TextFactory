package kyPkg.socks;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
public class HttpCon {
	String method = "";

	String agent = "";

	HttpURLConnection con = null;

	public void HttpConx(URL url) throws ProtocolException, ConnectException {
		try {
			con = (HttpURLConnection) url.openConnection();

			con.setUseCaches(false);
			con.setRequestMethod(method);

			HttpURLConnection.setFollowRedirects(true);

			con.setRequestProperty("User-Agent", agent);

			con.setInstanceFollowRedirects(true);

			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);

			con.connect();

			InputStreamReader isr = new InputStreamReader(con.getInputStream(),
					"JISAutoDetect");

			BufferedReader br = new BufferedReader(isr);

			String line = "";
			String ret = "";

			while ((line = br.readLine()) != null) {
				ret += line;
			}

			System.out.println(ret);

			br.close();
			isr.close();

		} catch (FileNotFoundException e) {
			// log.error(e);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
