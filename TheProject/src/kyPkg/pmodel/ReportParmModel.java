package kyPkg.pmodel;

public class ReportParmModel extends ParameterModelJSON {
	public ReportParmModel(String params) {
		super(params);
	}

	@Override
	public void checkIt() {
		chkUCase("USERID", true);
		chkExists("ITPCODE");
		chkYMD("BYMD1"); // 必須項目の確認　あれば０　なければ１を返すように変更する　エラー値を足し上げてValidation値とする
		chkYMD("AYMD1");
	}

}
