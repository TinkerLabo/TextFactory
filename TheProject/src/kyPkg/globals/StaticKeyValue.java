package kyPkg.globals;

import java.util.HashMap;

import globals.AGC_GATEWAY;
import globals.AGC_SMTP;
import globals.GWHOST;
import globals.HCS_SERVER;
import globals.MM_SERVER;
import kyPkg.uFile.YamlControl;

//委譲しなければならないメソッドがいくつかあり、冗長だ（修正漏れが起きそうで、スマートではない）
//常駐させる（サーバーメソッドから定期的に参照されるんのでstaticな扱いとしたい・・・値を管理なおかつ、緊急な変更に対応したい）
//staticなクラス
public abstract class StaticKeyValue {
	private String ymlName = "default" + "." + YamlControl.YML;
	private HashMap<String, String> ymlMap = null;

	// private StaticKeyValue getInstance();抽象メソッドにしておいて実装を強制させたい
	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public StaticKeyValue() {
		this.ymlMap = new HashMap<String, String>();
	}

	// ------------------------------------------------------------------------
	// setName　　コンストラクタのあと　super.setName(getClass().getSimpleName());
	// ------------------------------------------------------------------------
	public void setName(String ymlName) {
		this.ymlName = ymlName.trim() + "." + YamlControl.YML;
	}

	// ------------------------------------------------------------------------
	// forceDefault=>falseならymlに書かれているものを優先させる
	// ------------------------------------------------------------------------
	public void save(boolean forceDefault) {
		String curDir = globals.ResControl.getCurDir();
		String paramPath = curDir + ymlName;
		System.out.println("Save Yaml:" + paramPath);
		ymlMap = YamlControl.yaml2Map(paramPath, ymlMap, forceDefault);
	}

	public void put(String key, String val) {
		// if (key == null || val == null)
		if (key == null) // valueがnullでも可とした　、20150313
			return;
		ymlMap.put(key, val);

	}

	public String get(String key) {
		if (key == null)
			return null;
		return ymlMap.get(key);
	}

	public static void main(String[] argv) {
		test2013_0316();
	}

	// staticアクセスした場合の初期値確認
	public static void test2013_0316() {
		System.out.println("MM_SERVER.getUser:" + MM_SERVER.getUser());
		System.out.println("MM_SERVER.getPass:" + MM_SERVER.getPass());

		System.out.println("HCS_SERVER.getUser:" + HCS_SERVER.getUser());
		System.out.println("HCS_SERVER.getPass:" + HCS_SERVER.getPass());

		System.out.println("AGC_GATEWAY.getHost:" + AGC_GATEWAY.getHost());
		System.out.println("AGC_GATEWAY.getPort:" + AGC_GATEWAY.getPort());

		System.out.println("AGC_SMTP.getHost:" + AGC_SMTP.getHost());
		System.out.println("AGC_SMTP.getPort:" + AGC_SMTP.getPort());

		System.out.println("GWHOST.getHost:" + GWHOST.getHost());
		System.out.println("GWHOST.getUser:" + GWHOST.getUser());
		System.out.println("GWHOST.getPass:" + GWHOST.getPass());
	}

	// 無効値設定した場合のymlの書き出しの確認など用
	public static void test2013_0311() {
		System.out.println("pre MM_SERVER.getUser:" + MM_SERVER.getUser());
		System.out.println("pre MM_SERVER.getPass:" + MM_SERVER.getPass());
		System.out.println("pre HCS_SERVER.getUser:" + HCS_SERVER.getUser());
		System.out.println("pre HCS_SERVER.getPass:" + HCS_SERVER.getPass());
		System.out.println("pre AGC_GATEWAY.getHost:" + AGC_GATEWAY.getHost());
		System.out.println("pre AGC_GATEWAY.getPort:" + AGC_GATEWAY.getPort());

		// 半固定パラメーターはシングルトンクラスに格納する（外部パラメータ値が存在すれば変更する、無ければデフォルトを使用する）
		MM_SERVER.setUser("test MM_user");
		MM_SERVER.setPass("test 無限大");
		MM_SERVER.save();
		HCS_SERVER.setUser("test HCS_user");
		HCS_SERVER.setPass(null);
		HCS_SERVER.save();
		AGC_GATEWAY.setHost(null);
		AGC_GATEWAY.setPort(null);
		AGC_GATEWAY.save();

		System.out.println("------------------------------------------------");
		System.out.println("aft MM_SERVER.getUser:" + MM_SERVER.getUser());
		System.out.println("aft MM_SERVER.getPass:" + MM_SERVER.getPass());
		System.out.println("aft HCS_SERVER.getUser:" + HCS_SERVER.getUser());
		System.out.println("aft HCS_SERVER.getPass:" + HCS_SERVER.getPass());
		System.out.println("aft AGC_GATEWAY.getHost:" + AGC_GATEWAY.getHost());
		System.out.println("aft AGC_GATEWAY.getPort:" + AGC_GATEWAY.getPort());
	}

}