package kyPkg.filter;

public interface Inf_DualConverter {
	public void init();
	public abstract String convert(String[] splitedBef,String[] splitedAft,int stat);
	public void fin();
}