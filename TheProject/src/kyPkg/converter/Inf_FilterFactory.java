package kyPkg.converter;

public interface Inf_FilterFactory {
	public Inf_Converter getConverter(String param);
	public String getExplain() ;
	public String getSample();

}