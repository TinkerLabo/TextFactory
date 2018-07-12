package kyPkg.socks;

import java.io.*;
import java.net.*;

//役に立ちそう！マルチパートのサンプル・・・2009/09/07
public class Multipart01 {
	static String BOUNDARY = "*****";

	static String LINEEND = "\r\n";

	static String TwoHyphens = "--";

	public Multipart01() throws Exception {
		// String responseFromServer = "";
		// BufferedReader br = null;
		// InputStream is = null;
		// OutputStream os = null;
		// boolean ret = false;
		// String StrMessage = "";

		String url = "http://localhost:8080/FileUpload/requestupload";

		HttpURLConnection conn = connect(url);
		sendMultipart(conn);
		// read the SERVER RESPONSE
		read_The_SERVER_RESPONSE(conn);

	}

	public static void main(String[] args) {

	}

	private HttpURLConnection connect(String urlString) {
		// open a URL connection to the Servlet
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlString);
			// Open a HTTP connection to the URL
			// ------------------ CLIENT REQUEST
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true); // Allow Inputs
			conn.setDoOutput(true); // Allow Outputs
			conn.setUseCaches(false); // Don't use a cached copy.
			conn.setRequestMethod("POST"); // Use a post method.
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + BOUNDARY);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conn;
	}

	void sendMultipart(HttpURLConnection conn) throws IOException {
		DataOutputStream out = null;
		String rootDir = globals.ResControl.getQprRootDir();
		String exsistingFileName = rootDir + "account.xls";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		try {
			out = new DataOutputStream(conn.getOutputStream());
			out.writeBytes(TwoHyphens + BOUNDARY + LINEEND);
			out.writeBytes("Content-Disposition: form-data; ");
			out.writeBytes("name=\"upload\"; ");
			out.writeBytes("filename=\"" + exsistingFileName + "\"" + LINEEND);
			out.writeBytes(LINEEND);
			// create a buffer of maximum size
			FileInputStream in = new FileInputStream(
					new File(exsistingFileName));
			bytesAvailable = in.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			// read file and write it into form...
			bytesRead = in.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				out.write(buffer, 0, bufferSize);
				bytesAvailable = in.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = in.read(buffer, 0, bufferSize);
			}

			// send multipart form data necesssary after file data...

			out.writeBytes(LINEEND);
			out.writeBytes(TwoHyphens + BOUNDARY + TwoHyphens + LINEEND);

			// close streams

			in.close();
			out.flush();
			out.close();

		} catch (MalformedURLException ex) {
			System.out.println("From ServletCom CLIENT REQUEST:" + ex);
		}

		catch (IOException ioe) {
			System.out.println("From ServletCom CLIENT REQUEST:" + ioe);
		}
	}

	void read_The_SERVER_RESPONSE(HttpURLConnection conn) throws IOException {
		// レスポンスを受信 (これをやらないと通信が完了しない)
		InputStream stream = conn.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		String responseData = null;
		while ((responseData = br.readLine()) != null) {
			System.out.print(responseData);
		}
		stream.close();
	}

}
