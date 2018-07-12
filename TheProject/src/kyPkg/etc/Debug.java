package kyPkg.etc;

public class Debug {
	static String prefix="#DBG# ";
	public static void main(String[] args) {
		Debug.println("test");
	}
	public static void println(String message) {
		System.out.println(prefix + message);
	}
}
