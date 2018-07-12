package kyPkg.converter;

public class TranslateFactory implements Inf_FilterFactory {
	public static String TITLE = "translate";
	private String explain = "�����P�ϊ��O�̕����Q�A�����Q�ɂ��ꂼ��Ή����镶���Q�A�����R�Ƀf�t�H���g�����@��>�@a,b,c:1,2,3:null a=>1,b=>2";
	private String sample = "0,1,2:,a,b:null";
	private Inf_Converter coverter = null;
	private String param = "";

	@Override
	public Inf_Converter getConverter(String param) {
		if (coverter == null || !this.param.equals(param)) {
			coverter = new Translate(param);
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
