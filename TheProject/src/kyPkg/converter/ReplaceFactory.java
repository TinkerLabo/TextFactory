package kyPkg.converter;

public class ReplaceFactory implements Inf_FilterFactory {
	public static final String TITLE = "replace";
	public static final String explain = "�����P�ϊ��O�̕�����i���K�\���j�A�����Q�ɒu�������镶����@��>�@bef:aft";
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
