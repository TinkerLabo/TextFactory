package kyPkg.filter;

import java.util.HashMap;

public class TestCelConv {
	//�R���X�g���N�^�͈����Ȃ��E�E�E���\�b�h�ɃV�O�j�`����String,String[]�Ƃ���E�E�E��
	public TestCelConv() {
		System.out.println("�R���X�g���N�^");
	}
	public String convert(HashMap<String, String> infoMap) {
		String val = "Hello";
		System.out.println("@Convert HashMap<String, String>"+val);
		return val;
	}
	public String convert( String[] parms) {
		System.out.println("@Convert String[] ");
		return "";
	}
	public String convert(String cel, String[] parms) {
		String val = cel.toUpperCase();
		System.out.println("�Z���̒l�ƃp�����[�^�������ł��郁�\�b�h���Ă΂�܂���");
		return val;
	}
}
