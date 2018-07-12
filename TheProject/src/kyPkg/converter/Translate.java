package kyPkg.converter;

import java.util.HashMap;

//-----------------------------------------------------------------------------
// ���镶���Q��Ή����镶���Q�ɕϊ�����i�}�b�`������̂������ꍇ�̓f�t�H���g���K�p�����j
//-----------------------------------------------------------------------------
public class Translate implements Inf_Converter {

	private static final String escapeComma = "_";
	private String defaultStr;
	private HashMap<String, String> hmap;
	private String delimiter = ",";

	//-------------------------------------------------------------------------
	// �R���X�g���N�^
	//-------------------------------------------------------------------------
	public Translate(String srcStr, String dstStr, String defaultStr) {
		this(srcStr + ":" + dstStr + ":" + defaultStr);
	}
	//-------------------------------------------------------------------------
	// �R���X�g���N�^	String�ЂƂ������ɂƂ�i�hp1x,p1y,p1z:p2:p3�h�j
	//-------------------------------------------------------------------------
	public Translate(String param) {
		param = param.replaceAll(escapeComma, delimiter);
		hmap = new HashMap();
		String[] parms = param.split(":");
		if (parms.length >= 3) {
			String[] srcArray = parms[0].split(delimiter);
			String[] dstArray = parms[1].split(delimiter);
			if (parms[2].toLowerCase().equals("null"))
				parms[2] = "";
			this.defaultStr = parms[2];
			for (int i = 0; i < srcArray.length; i++) {
				if (i <= dstArray.length) {
					if (srcArray[i].toLowerCase().equals("null"))
						srcArray[i] = "";
					if (dstArray[i].toLowerCase().equals("null"))
						dstArray[i] = "";
					hmap.put(srcArray[i], dstArray[i]);
				}
			}
		} else {
			System.out.println("@Translate error!!=>param:" + param);
			System.out.println("   parms.length error!!" + parms.length);
		}
	}

	/*
	 * (�� Javadoc)
	 * 
	 * @see kyPkg.util.Inf_CellConverter#convert(java.lang.String)
	 */
	@Override
	public String convert(String cell, String[] cells) {
		String ans = hmap.get(cell);
		if (ans != null)
			return ans;
		return defaultStr;
	}

}
