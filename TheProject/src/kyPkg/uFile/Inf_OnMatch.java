package kyPkg.uFile;

import java.io.File;
//############################################################################
//Inf_OnMatchファイル名がマッチした時に処理をするクラスのインタフェース
//############################################################################
public interface Inf_OnMatch {
	public abstract void init() ;

	public abstract void fin();

	public abstract void onMatch(File fileObj);

	public abstract void forDebug(String val);
}