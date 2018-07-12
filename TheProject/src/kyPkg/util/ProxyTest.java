package kyPkg.util;

import java.util.Properties;
import java.util.Set;

public class ProxyTest {
	public static void main(String[] argv) {
		//プロキシサーバーが設定されていないとプロセシングのLoadImageがうまく動かないので、環境変数の確認＆設定の雛形を書いてみる
		//befの段階でプロキシの設定が効いていないので・・・・setting云々に書き込んでも無駄なんだろう・・・・		
		Properties systemSettings = System.getProperties();
		Set keySet = systemSettings.keySet();
		System.out.println("bef-----------------------------------------------------------");
		for (Object key : keySet) {
		  System.out.println("#"+key.toString()+"=>"+systemSettings.get(key));
		}
		systemSettings.put("http.proxyHost", "agcproxy.tokyu-agc.co.jp");
		systemSettings.put("http.proxyPort", "80");
		System.setProperties(systemSettings);
		System.out.println("aft-----------------------------------------------------------");
		for (Object key : keySet) {
		  System.out.println("#"+key.toString()+"=>"+systemSettings.get(key));
		}


	}
}
