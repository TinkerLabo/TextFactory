package kyPkg.filter;

public interface Inf_Writer {
	public abstract void open();
	public abstract void write(String rec);
	public abstract void close();
}