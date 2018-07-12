package kyPkg.converter;

import java.util.List;

public interface Inf_LineConverter extends Inf_ArrayCnv {
	public abstract void setPrefix(String prefix);
	public abstract void setSuffix(String suffix);
	public abstract String getHeader();
	public abstract String convert2Str(String[] array, int stat);
	public abstract String convert2Str(List list, int stat);
	public abstract String convert2Str(String str);
	public abstract List convert(List<String> list, int stat);
}
