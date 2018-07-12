package kyPkg.filter;

import kyPkg.task.Abs_ProgressTask;
import kyPkg.task.Inf_ProgressTask;

public class TemplateOF_ProgressTask extends Abs_ProgressTask {
	// -------------------------------------------------------------------------
	// 2012-02-09 yuasa （コピーして雛形として使用する）
	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public TemplateOF_ProgressTask() {
		super();
	}

	// -------------------------------------------------------------------------
	// 実行部分
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.start("TemplateOF_ProgressTask",2048);
		System.out.println("This is ....");
		super.stop();// 正常終了
	}

	public static void main(String[] args) {
		tester();
	}
	// -------------------------------------------------------------------------
	// 使用例＞
	// -------------------------------------------------------------------------
	public static void tester() {
		Inf_ProgressTask task = new TemplateOF_ProgressTask();
		task.execute();
	}

}
