package kyPkg.filter;

import kyPkg.task.Abs_BaseTask;

// 2010-04-28 yuasa
public class Task_Template extends Abs_BaseTask {
	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Task_Template() {
	}
	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("ＸＸ開始");
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
