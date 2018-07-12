package kyPkg.socks;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Java HTTP クライアントサンプル - HttpURLConnection 版 -
 *
 * @author 68user http://X68000.q-e-d.net/~68user/
 */
public class HttpClient2 {
    public static void main(String[] args)
        throws MalformedURLException, ProtocolException, IOException {

        URL url = new URL("http://www.debian.org/");

        HttpURLConnection urlconn = (HttpURLConnection)url.openConnection();
        urlconn.setRequestMethod("GET");
        urlconn.setInstanceFollowRedirects(false);
        urlconn.setRequestProperty("Accept-Language", "ja;q=0.7,en;q=0.3");

        urlconn.connect();

        Map headers = urlconn.getHeaderFields();
        Iterator it = headers.keySet().iterator();
        System.out.println("レスポンスヘッダ:");
        while (it.hasNext()){
            String key= (String)it.next();
            System.out.println("  " + key + ": " + headers.get(key));
        }

        System.out.println("レスポンスコード[" + urlconn.getResponseCode() + "] " +
                           "レスポンスメッセージ[" + urlconn.getResponseMessage() + "]");
        System.out.println("\n---- ボディ ----");

        BufferedReader reader =
            new BufferedReader(new InputStreamReader(urlconn.getInputStream()));

        while (true){
            String line = reader.readLine();
            if ( line == null ){
                break;
            }
            System.out.println(line);
        }

        reader.close();
        urlconn.disconnect();
    }
}

