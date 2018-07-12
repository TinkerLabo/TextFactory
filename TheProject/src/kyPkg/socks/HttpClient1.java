package kyPkg.socks;

import java.net.*;
import java.io.*;

/**
 * Java HTTP クライアントサンプル - Socket 版 -
 *
 * @author 68user http://X68000.q-e-d.net/~68user/
 */
public class HttpClient1 {

	public static void main(String[] args) {
		String host = "www.jp.FreeBSD.org";
		int port = 80;
		String path = "/index.html";

		Socket socket;
		BufferedReader br;
		BufferedWriter bw;

		try {
			socket = new Socket(host, port);
			br = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			bw = new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream()));

			bw.write("GET " + path + " HTTP/1.0\r\n");
			bw.write("Host: " + host + ":" + port + "\r\n");
			bw.write("\r\n");
			bw.flush();

			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				System.out.println(line);
			}
			br.close();
			bw.close();
			socket.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
