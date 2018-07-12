package kyPkg.reflect;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.commons.logging.Log;

import kyPkg.uFile.YamlControl;

public class Reflector {
	private Object instance;
	private Class classObj;
	public static final String CLASS = "Class";
	public static final String METHOD = "Method";

	// XXX　2010-08-03　ミッションがちゃんと動くか・・・メモリリークしないか確認しておく！！

	// public Reflector(String path, HashMap<String, String> hmap, boolean
	// force) {
	// pMap = qpr.ssAna.YmlControl.resControl(path, hmap, force);
	// String className;
	// if (pMap != null) {
	// className = pMap.get("Class");
	// methodName = pMap.get("Method");
	// } else {
	// className = null;
	// methodName = null;
	// }
	// getInstance(className);
	// }
	
	public static void kickout(HashMap<String, String> pMap) {
		if (pMap == null)
			return;
		String className = pMap.get(CLASS);
		String methodName = pMap.get(METHOD);
		kyPkg.reflect.Reflector reflector = new kyPkg.reflect.Reflector(
				className);
		reflector.invoke(methodName, new Object[] { pMap });
	}
	
	
	public Reflector(String className) {
		if (className == null)
			return;
		System.out.println("  className:" + className);
		try {
			classObj = Class.forName(className);
			instance = classObj.newInstance();

			// メソッド名一覧・・・
			Method[] mArray = classObj.getDeclaredMethods();
			for (int i = 0; i < mArray.length; i++) {
				System.out.println("Method:" + mArray[i].getName());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public void invoke(String methodName, Object param) {
	// Object[] methodP = new Object[] { param };
	// invoke(methodName, methodP);
	// }
	public void invoke(String methodName, Object[] methodP) {
		if (methodName == null)
			return;
		System.out.println("<<Reflector invokeIt>>");
		System.out.println("  methodName:" + methodName);
		try {
			// パラメータから引数のシグニチャのパラメータを作る
			Class[] signature = new Class[methodP.length];
			for (int i = 0; i < methodP.length; i++) {
				signature[i] = methodP[i].getClass();
			}
			Method method = classObj.getMethod(methodName, (Class[]) signature);// シグニチャにマッチするメソッドを取得
			if (method != null) {
				// Object rtn =
				method.invoke(instance, methodP);// メソッドを実行
				// System.out.println("rtn=>" + rtn);

			} else {
				System.out.println("ERROR @invokeIt method'" + methodName
						+ "' not found!");
			}
		} catch (Exception e) {
			// NoSuchMethodException
			e.printStackTrace();
		}
	}

	// public void invokeIt(String className, String methodName, String[] array)
	// {
	// System.out.println("<<Reflector invokeIt>>");
	// System.out.println("  className:" + className);
	// System.out.println("  methodName:" + methodName);
	// try {
	// Class classObj = Class.forName(className);
	// instance = classObj.newInstance();
	// Object[] methodP = new Object[] { array };
	// // Class[] signature = new Class[] { String[].class };
	// Class[] signature = new Class[] { methodP.getClass() };
	// Method method = classObj.getMethod(methodName, (Class[]) signature);//
	// シグニチャにマッチするメソッドを取得
	// if (method != null) {
	// // Object rtn =
	// method.invoke(classObj.newInstance(), (Object[]) methodP);// メソッドを実行
	// // System.out.println("rtn=>" + rtn);
	//
	// } else {
	// System.out.println("ERROR @invokeIt method'" + methodName
	// + "' not found!");
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }



	private static void testReflector02() {
		// XXX コンストラクタは引数なし・・・メソッドにシグニチャはString,String[]とする・・・か
		// XXX だとして、プラグイン定義ファイルはどのような形式にするか
		// XXX 洒落くさい・・・プラグインフォルダを用意して、クラスファイルだけ放り込む形がよいのではないだろうか？
		// XXX クラス内に・・・プラグインの名前およびパラメータに関するコメントを持たせる
		// XXX クラスパスをどうやって通すのか？
		// XXX 考えがまとまるまで、プラグインについてはペンディングとしようと思う @2010/08/11
		String curDir = globals.ResControl.getCurDir();
		String paramPath = curDir+"plugin."+YamlControl.YML;
		HashMap<String, String> pMap = new HashMap<String, String>();
		pMap.put("Class", "kyPkg.filter.TestCelConv");
		pMap.put("Method", "convert");
		pMap = kyPkg.uFile.YamlControl.yaml2Map(paramPath, pMap, true);
		if (pMap != null) {
			String className = pMap.get("Class");
			String methodName = pMap.get("Method");
			kyPkg.reflect.Reflector reflector = new kyPkg.reflect.Reflector(
					className);
			String cell = "this is cell ";
			Object signature = pMap;
			signature = new String[0]; // for Debug
			// (signatureに指定する型によって呼び出すメソッドをコントロールできるということ・・・)
			reflector.invoke(methodName, new Object[] { cell, signature });
		}
	}

	public static void main(String args[]) {
		testReflector02();
	}

	// -------------------------------------------------------------------------
	// for Test
	// String baseYMD = GetStat();
	// String current = timeStamp();
	// String wChkNum = kyPkg.util.EnqChk.fixNum(256,7);
	// message("　status :"+baseYMD);
	// message("　timeStamp:"+current);
	// message("　wChkNum :"+wChkNum);
	// message("on TransferMM Tester START");
	// 20071101000000
	// String baseYMD = GetStat();
	// mmMerge_Trn(baseYMD) ;
	// mmMerge_Trn("c:\\NQDATA.dat",".\\Database\\data\\","20071101000000"); //
	// for Test
	// message("on TransferMM Tester END");
	// -------------------------------------------------------------------------
	public static boolean getStat(Log log, HashMap<String, String> infoMap,
			String keyWord) {
		String stat = "";
		stat = infoMap.get(keyWord);
		if (stat != null && stat.toUpperCase().startsWith("T")) {
			if (log != null) {
				log
						.info("---------------------------------------------------------");
				log.info(keyWord);
				log
						.info("---------------------------------------------------------");
			}
			return true;
		} else {
			return false;
		}
	}

}