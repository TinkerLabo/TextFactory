package kyPkg.converter;

public class DictconvertFactory implements Inf_FilterFactory {
	public static final String TITLE = "dictconvert";
	public static final String explain = "é´èëïœä∑Å@ó·>Å@C:/@QPR/home/qpr/res/catTree.txt";
	public static final String sample = "C:/@QPR/home/qpr/res/channel.dic";
	private Inf_Converter coverter = null;
	private String param = "";

	@Override
	public Inf_Converter getConverter(String param) {
		if (coverter == null || !this.param.equals(param)) {
			coverter = new DictConvert(param);
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
