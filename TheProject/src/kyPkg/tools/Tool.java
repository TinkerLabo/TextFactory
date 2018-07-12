package kyPkg.tools;

public class Tool {

	public static String cnvEsc(String str) {
		str = str.replaceAll("\\\\", "\\\\\\\\");
		str = str.replaceAll("\"", "\\\\\"");
		return str;
	}

}