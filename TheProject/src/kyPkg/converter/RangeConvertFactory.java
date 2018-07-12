package kyPkg.converter;

public class RangeConvertFactory implements Inf_FilterFactory {
	public static final String TITLE = "RangeConvert";
	public static final String explain = "�����W�ɂ���ċ敪�R�[�h���A�T�C������A�����W�̎w��̓p�����[�^�t�@�C���Ɏw�肷��@�၄�V�ȏ�11�ȉ��Ȃ�3�h7-11	3�h";
	public static final String sample = "c:/rCnvP.txt";
	private Inf_Converter coverter = null;
	private String param = "";

	@Override
	public Inf_Converter getConverter(String param) {
		if (coverter == null || !this.param.equals(param)) {
			coverter = new RangeConvert(param);
			this.param = param;
		}
		return coverter;
	}

	@Override
	public String getExplain() {
		return explain;
	}

	@Override
	public String getSample() {
		return sample;
	}
}
