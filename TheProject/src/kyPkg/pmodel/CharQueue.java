package kyPkg.pmodel;

public class CharQueue {
	private int cursor = 0;
	private char[] array = null;
	CharQueue(char[] array){
		this.array=array;
	}
	public Object peek() {//�L���[�̐擪���擾���܂����A�폜���܂���B
		return array[cursor];
	}
	public Object poll() {//�L���[�̐擪���擾����э폜���܂��B
		return array[cursor++];
	}
}
