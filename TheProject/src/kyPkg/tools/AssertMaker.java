package kyPkg.tools;

public class AssertMaker extends Tool {

	public static String execute(String expression) {
		String prefix = "System.out.println(\"assertEquals(";
		String suffix = "+\");\");";
		String ans = prefix + cnvEsc(expression) + ",\"+" + expression + suffix;
		return ans;
	}

}
