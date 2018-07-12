package kyPkg.converter;
import java.util.List;
public interface Inf_StatConverterM {
	public void init();
	public void fin();
	public abstract List<String> convert(String[] array, int stat);
	public abstract List<String> convert(List list, int stat);
	public List<String> convert(String str);
}