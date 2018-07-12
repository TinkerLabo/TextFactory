package kyPkg.filter;
public class Inf_IntConv1H implements Inf_IntConverter {
	//-------------------------------------------------------------------------
	// 一時間刻みテーブルの並びを番組表型時刻並びに変更する
	//-------------------------------------------------------------------------
	@Override
	public int convertInt(int in){
		if (in < 5){
			in += 19;
		}else{
			in -= 5;
		}
		return in;
	}
}