package kyPkg.filter;

import kyPkg.task.Abs_BaseTask;

// 2010-04-28 yuasa
public class Task_Template extends Abs_BaseTask {
	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public Task_Template() {
	}
	// -------------------------------------------------------------------------
	// ���s
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("�w�w�J�n");
	}
	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		test1();
	}
	public static void test1() {
		new Task_Template().execute();
	}
}
