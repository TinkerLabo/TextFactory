package kyPkg.filter;

import java.util.List;

public interface Inf_oClosure {
	public abstract void setDelimiter(String delimiter);
	public abstract void setLF(String lf);

	public abstract boolean open();
	public abstract boolean open(boolean append);	//’Ç‰Á‘‚«‚İ‚·‚é‚©‚Ç‚¤‚©
	public abstract void close();

	//2014-10-16
	public void setHeader(String header) ;
	public void setHeader(List<String> headList, String delimiter) ;
	public void setHeader(String[] headArray, String delimiter) ;

	public abstract long getWriteCount();
	public abstract String getPath(); //‚Ç‚¤‚¾‚ë‚¤EE

	public abstract boolean write(List splited, int stat);
	public abstract boolean write(String[] splited, int stat);
	public abstract boolean write(String rec);
	public abstract boolean write(Inf_iClosure reader);

}