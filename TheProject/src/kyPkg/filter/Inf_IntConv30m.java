package kyPkg.filter;
public class Inf_IntConv30m implements Inf_IntConverter {
	//-------------------------------------------------------------------------
	// ３０分刻みテーブルの並びを番組表型時刻並びに変更する
	//-------------------------------------------------------------------------
	@Override
	public int convertInt(int in){
		if (in < 10){
			in += 38;
		}else{
			in -= 10;
		}
		return in;
	}
}
