package kyPkg.frame;

import java.util.ArrayList;

import kyPkg.rez.IntRes;
import kyPkg.rez.TextRes;

public class RezObject {
	private String type;

	private TextRes source = null;

	private TextRes dest = null;

	private IntRes converter = null;

	// -------------------------------------------------------------------------
	public void setText2Source(String string) {
		String[] array = string.split("\n");
		ArrayList list = new ArrayList();
		for (int i = 0; i < array.length; i++) {
			String wStr = array[i].trim();
			if (!wStr.equals("")) {
				list.add(wStr);
			}
		}
		String[] strArray = (String[]) list.toArray(new String[list.size()]);
		source.setArray(strArray);
	}

	// -------------------------------------------------------------------------
	public void setText2Dest(String string) {
		String[] array = string.split("\n");
		ArrayList list = new ArrayList();
		for (int i = 0; i < array.length; i++) {
			String wStr = array[i].trim();
			if (!wStr.equals("")) {
				list.add(wStr);
			}
		}
		String[] strArray = (String[]) list.toArray(new String[list.size()]);
		dest.setArray(strArray);
	}

	// -------------------------------------------------------------------------
	public void setText2Converter(String string) {
		int[] intArray = null;
		try {
			String[] array = string.split("\n");
			intArray = new int[array.length];
			for (int i = 0; i < array.length; i++) {
				intArray[i] = Integer.parseInt(array[i]);
			}
		} catch (NumberFormatException e) {
			intArray = null;
		}
		converter.setConverter(intArray);
	}

	// -------------------------------------------------------------------------
	public String getConverter2Text() {
		StringBuffer buf = new StringBuffer();
		int[] array = converter.getConverter();
		for (int i = 0; i < array.length; i++) {
			buf.append(array[i]);
			buf.append("\n");
		}
		return buf.toString();
	}

	// -------------------------------------------------------------------------
	// コンストラクター
	// -------------------------------------------------------------------------
	public RezObject() {
		super();
		type = "";
		converter = new IntRes();
		source = new TextRes();
		dest = new TextRes();
	}

	public void setConverter(IntRes conv) {
		if (converter == null) {
			converter = new IntRes();
		} else {
			converter = conv;
		}
	}

	public String[] getDestArray() {
		return dest.getArray();
	}

	// 逆のことをするやつも必要 
	public String getDest2Text() {
		StringBuffer buf = new StringBuffer();
		String[] array = dest.getArray();
		for (int i = 0; i < array.length; i++) {
			buf.append(array[i]);
			buf.append("\n");
		}
		return buf.toString();
	}

	public String getSource2Text() {
		StringBuffer buf = new StringBuffer();
		String[] array = source.getArray();
		for (int i = 0; i < array.length; i++) {
			buf.append(array[i]);
			buf.append("\n");
		}
		return buf.toString();
	}

	public String[] getSourceArray() {
		return source.getArray();
	}

	public int[] getConverter() {
		if (converter != null) {
			return converter.getConverter();
		}
		return null;
	}

	// public IntRes getConverter() {
	// return converter;
	// }

	// public StringRes getDest() {
	// return dest;
	// }

	// public StringRes getSource() {
	// return source;
	// }

	public void setSourceArray(String[] array) {
		this.source.setArray(array);
	}

	public void setDestArray(String[] array) {
		this.dest.setArray(array);
	}

	public void appendDest(String str) {
		this.dest.append(str);
	}

	public int getSourceSize() {
		return this.source.getSize();
	}

	public int getDestSize() {
		return this.dest.getSize();
	}

	public void setSource(TextRes srcRes) {
		this.source = srcRes;
	}

	public void setDest(TextRes dstRes) {
		this.dest = dstRes;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
