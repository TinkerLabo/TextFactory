package kyPkg.converter;

public class RangeConvertFactory implements Inf_FilterFactory {
	public static final String TITLE = "RangeConvert";
	public static final String explain = "レンジによって区分コードをアサインする、レンジの指定はパラメータファイルに指定する　例＞７以上11以下なら3”7-11	3”";
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
