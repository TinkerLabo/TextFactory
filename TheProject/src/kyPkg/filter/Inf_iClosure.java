package kyPkg.filter;
public interface Inf_iClosure {
	public abstract boolean notEOF();
	public abstract int getStat();
	public abstract void open();
	public abstract String readLine();
	public abstract String[] readSplited();
	public abstract String[] getSplited();
	public abstract String  getCurrent();
	public abstract void close();
	//  option ---
	public abstract void setFilter(RegChecker paramObj,int level);
	public abstract void setDelimiter(String delimiter);
	public abstract String getDelimiter();

}