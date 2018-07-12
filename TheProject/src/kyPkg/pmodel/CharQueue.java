package kyPkg.pmodel;

public class CharQueue {
	private int cursor = 0;
	private char[] array = null;
	CharQueue(char[] array){
		this.array=array;
	}
	public Object peek() {//キューの先頭を取得しますが、削除しません。
		return array[cursor];
	}
	public Object poll() {//キューの先頭を取得および削除します。
		return array[cursor++];
	}
}
