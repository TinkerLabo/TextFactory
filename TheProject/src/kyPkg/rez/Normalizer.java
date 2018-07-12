package kyPkg.rez;

public class Normalizer {
	public static void main(String[] var) {
		testRemoveInjection();
	}

	public static void testRemoveInjection() {
		String condition = "hello;insert into 'Freak' master;";
		condition = kyPkg.rez.Normalizer.removeInjection(condition);
		System.out.println(condition);
	}

	// XXX ��肠��������������Ȃ̂ŁE�E�E���܂茟�؂��Ă��܂���
	// SQL�C���W�F�N�V�����΍􏈗��H�I
	// �G�������Ă����炻��ȍ~�̕����͏���etc
	public static String removeInjection(String condition) {
		// 20140716 trim���Ȃ��I�I�敪�R�[�h�ɃX�y�[�X���܂܂�Ă���ꍇ�ɍ���i�����ł��Ȃ��Ȃ�j
		// condition = condition.trim();
		String[] NGStr = { ";", "'", "\"" };
		int endIndex = 0;
		for (int i = 0; i < NGStr.length; i++) {
			endIndex = condition.indexOf(NGStr[i]);
			if (endIndex > 0) {
				condition = condition.substring(0, endIndex);
			}
		}
		return condition;
	}
}
