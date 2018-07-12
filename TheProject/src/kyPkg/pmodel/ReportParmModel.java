package kyPkg.pmodel;

public class ReportParmModel extends ParameterModelJSON {
	public ReportParmModel(String params) {
		super(params);
	}

	@Override
	public void checkIt() {
		chkUCase("USERID", true);
		chkExists("ITPCODE");
		chkYMD("BYMD1"); // �K�{���ڂ̊m�F�@����΂O�@�Ȃ���΂P��Ԃ��悤�ɕύX����@�G���[�l�𑫂��グ��Validation�l�Ƃ���
		chkYMD("AYMD1");
	}

}
