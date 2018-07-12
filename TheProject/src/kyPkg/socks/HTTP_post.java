package kyPkg.socks;

import java.net.*;
import java.io.*;

//ñÇ…óßÇΩÇ»Ç¢ä¥Ç∂ÅEÅEÅE2009/09/07
public class HTTP_post {
	static URL u;

	public static void main(String args[]) {
		//String s = URLEncoder.encode("A Test string to send to a servlet");
		String s = "A Test string to send to a servlet";

		try {
			HTTP_post.u = new URL("http://myhost/servlet");

			// Open the connection and prepare to POST
			URLConnection conn = u.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setAllowUserInteraction(false);

			DataOutputStream dstream = new DataOutputStream(conn.getOutputStream());

			// The POST line
			dstream.writeBytes(s);
			dstream.close();

			// Read Response
			InputStream in = conn.getInputStream();
			int x;
			while ((x = in.read()) != -1) {
				System.out.write(x);
			}
			in.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			StringBuffer buf = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				buf.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace(); // should do real exception handling
		}
	}
}
