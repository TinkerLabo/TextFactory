package kyPkg.filter;

import kyPkg.task.Abs_ProgressTask;
import kyPkg.task.Inf_ProgressTask;

public class TemplateOF_ProgressTask extends Abs_ProgressTask {
	// -------------------------------------------------------------------------
	// 2012-02-09 yuasa �i�R�s�[���Đ��`�Ƃ��Ďg�p����j
	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public TemplateOF_ProgressTask() {
		super();
	}

	// -------------------------------------------------------------------------
	// ���s����
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.start("TemplateOF_ProgressTask",2048);
		System.out.println("This is ....");
		super.stop();// ����I��
	}

	public static void main(String[] args) {
		tester();
	}
	// -------------------------------------------------------------------------
	// �g�p�၄
	// -------------------------------------------------------------------------
	public static void tester() {
		Inf_ProgressTask task = new TemplateOF_ProgressTask();
		task.execute();
	}

}
