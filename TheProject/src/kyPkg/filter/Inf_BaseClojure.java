package kyPkg.filter;

public interface Inf_BaseClojure {
	public abstract void init();
	public abstract void execute(String rec);
	public abstract void execute(String[] rec );
	public abstract void write();

}