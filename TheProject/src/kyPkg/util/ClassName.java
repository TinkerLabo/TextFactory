package kyPkg.util;

public class ClassName {
	// ���Y�^�E�E�E�N���X�����E�������ꍇ
	public ClassName() {
		super();
		// �E�N���X����擾������@
		// getClass().getName(); // �p�b�P�[�W�����܂ރN���X��
		// getClass().getSimpleName() // �N���X���̂�
		//
		// �E�X�^�b�N�g���[�X����擾������@
		// Thread.currentThread().getStackTrace()[1].getMethodName(); // ���\�b�h��
		// Thread.currentThread().getStackTrace()[1].getClassName(); //
		// �p�b�P�[�W�����܂ރN���X��
		// Thread.currentThread().getStackTrace()[1].getFileName(); //
		// �g���q(.java)�t���t�@�C����
		// Thread.currentThread().getStackTrace()[1].getLineNumber(); // �s��

		String name = getClass().getName();
		String simpleName = getClass().getSimpleName();
//		System.out.println("name:"+name);
//		System.out.println("simpleName:"+simpleName);

	}

}
