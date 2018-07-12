package kyPkg.converter;

public class ReplaceFactory implements Inf_FilterFactory {
	public static final String TITLE = "replace";
	public static final String explain = "引数１変換前の文字列（正規表現）、引数２に置き換える文字列　例>　bef:aft";
	public static final String sample = "bef:aft";
	private Inf_Converter coverter = null;
	private String param = "";

	@Override
	public Inf_Converter getConverter(String param) {
		if (coverter == null || !this.param.equals(param)) {
			coverter = new Replace(param);
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
