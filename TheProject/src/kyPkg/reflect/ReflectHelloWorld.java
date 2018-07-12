package kyPkg.reflect;

import java.lang.reflect.*;

public class ReflectHelloWorld {
	public static void main(String[] args) {
		try {
			int i, j;

			// �N���X�̎擾
			Class cls = Class.forName("java.lang.String");

			// �t�B�[���h�̕���
			Field[] fieldList = cls.getFields();
			for (i = 0; i < fieldList.length; i++) {
				Field fld = fieldList[i];
				// �C���q��\��
				System.out.print(Modifier.toString(fld.getModifiers()));
				// �^��\��
				System.out.print(" " + fld.getType().getName());
				// �t�B�[���h����\��
				System.out.println(" " + fld.getName() + ";");
			}
			System.out.println("");

			// �R���X�g���N�^�̕���
			Constructor[] ctorList = cls.getConstructors();
			for (i = 0; i < ctorList.length; i++) {
				Constructor ct = ctorList[i];
				// �C���q��\��
				System.out.print(Modifier.toString(ct.getModifiers()));
				// �N���X��(�R���X�g���N�^��)��\��
				System.out.print(" " + ct.getDeclaringClass().getName());
				// �����̌^��\��
				Class[] cparamList = ct.getParameterTypes();
				System.out.print("(");
				for (j = 0; j < cparamList.length; j++) {
					System.out.print(" " + cparamList[j].getName());
				}
				System.out.println(");");
			}
			System.out.println("");

			// ���\�b�h�̕���
			Method[] methList = cls.getMethods();
			for (i = 0; i < methList.length; i++) {
				Method m = methList[i];
				// �C���q��\��
				System.out.print(Modifier.toString(m.getModifiers()));
				// �߂�l�̌^��\��
				System.out.print(" " + m.getReturnType().getName());
				// ���\�b�h����\��
				System.out.print(" " + m.getName());
				// �����̌^��\��
				Class[] mparamList = m.getParameterTypes();
				System.out.print("(");
				for (j = 0; j < mparamList.length; j++) {
					System.out.print(" " + mparamList[j].getName());
				}
				System.out.println(");");
			}
			System.out.println("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}