package kyPkg.pmodel;

import java.util.List;

import kyPkg.converter.ValueChecker;

public class ParameterModelJSON implements ParameterModelInf {
	private JsonObject jsonObj;
	private String params;
	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] argv) {

	}

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public ParameterModelJSON(String params) {
		System.out.println("DEBUG:@ParameterModelJSON:params =>=>=> " + params);
		if (!params.startsWith("{")) {
			params = "{" + params.trim() + "}";
		}
		this.params = params;
		this.jsonObj = new JsonObject(params);
		checkIt();
	}

	@Override
	public String getParams() {
		return params;
	}

	@Override
	public String get(String wKey) {
		return jsonObj.get(wKey);
	}

	@Override
	public String[] getArray(String wKey) {
		return jsonObj.getArray(wKey);
	}

	@Override
	public List getList(String wKey) {
		return jsonObj.getList(wKey);
	}

	protected void chkExists(String wKey) {
		chkUCase(wKey, false);
	}

	protected void chkUCase(String wKey, boolean wUCase) {
		String wPre = jsonObj.get(wKey);
		String wCnv = "";
		if (wPre != null) {
			if (wUCase)
				wCnv = wPre.toUpperCase();
			if (!wPre.equals(wCnv))
				jsonObj.put(wKey, wCnv);
		}
	}

	protected void chkYMD(String wKey) {
		String wPre = jsonObj.get(wKey);
		String wCnv = "";
		if (wPre != null) {
			wCnv = ValueChecker.cnvYmd(wPre);
			if (!wPre.equals(wCnv))
				jsonObj.put(wKey, wCnv);
		}
	}

	@Override
	public void checkIt() {
	}

	@Override
	public void saveAsText(String path) {
	};
}
// public static void test(){
// String param =
// "F:REP,USERID:ejqp7,REPORT:Cross,ATM1:atom/aカンロ／キャンキャラ＋Ｆｉｔｓ＋カバヤ生キャラ(2008年1月1日から6月30日),ATM2:atom/aカンロ／キャンキャラ＋Ｆｉｔｓ＋カバヤ生キャラ(2008年1月1日から6月30日),MSK1:atom/aカンロ／キャンキャラ＋Ｆｉｔｓ＋カバヤ生キャラ(2008年1月1日から6月30日),MSK2:atom/aカンロ／キャンキャラ＋Ｆｉｔｓ＋カバヤ生キャラ(2008年1月1日から6月30日),MTYP:1,NUM1:0,NUM2:0,BASE:2,PMASK:2,WMASK:2,CB1:2,CB2:2,CB3:2,CB4:2,ATM_BYMD:2009/10/4,ATM_AYMD:2009/11/3,DIM1:[1],DIM2:[5]";
// qprweb.PModel.ParameterModelJSON pModel = new
// qprweb.PModel.ParameterModelJSON(param);
// }
