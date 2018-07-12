package kyPkg.converter;

import kyPkg.filter.Flt_Venn;
import kyPkg.filter.Inf_DualConverter;

//-------------------------------------------------------------------------
// IDÇÃÇ›ÇèoóÕÇ∑ÇÈ
//-------------------------------------------------------------------------
public class DCnvKeyOnly implements Inf_DualConverter {
	@Override
	public String convert(String[] array_R, String[] array_L, int stat) {
		String rtn = "";
		switch ((int) stat) {
		case Flt_Venn.RIGHT_ONLY:
			rtn = array_R[0];// + "<R>";
			break;
		case Flt_Venn.INNER_JOIN:
			rtn = array_L[0];// + "<I>";
			break;
		case Flt_Venn.LEFT_ONLY:
			rtn = array_L[0];// + "<L>";
			break;
		default:
			rtn = array_L[0];// + "<L>";
		}
		return rtn;
	}

	@Override
	public void init() {
	};

	@Override
	public void fin() {
	};
}