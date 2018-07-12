package kyPkg.task;

import static kyPkg.util.CursorUtil.setHourglass;

import java.awt.Component;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.text.JTextComponent;

import kyPkg.uFile.ListArrayUtil;
import kyPkg.util.Msgbox;

// -------------------------------------------------------------------------
// タイマーからコールされるタスク管理
// -------------------------------------------------------------------------
public class TaskChecker extends TimerTask {
	private int taskIndex = -1;
	private Inf_ProgressTask task = null;

	private Timer timer;
	private List<Inf_ProgressTask> taskList;
	private ProgressControl prgCtrl;
	private JTextComponent logWatcher;
	private Component parentCmp;

	public TaskChecker(Component parentCmp, Timer timer,
			ProgressControl prgCtrl, List<Inf_ProgressTask> taskList) {
		super();
		this.parentCmp = parentCmp;
		this.timer = timer;
		this.prgCtrl = prgCtrl;
		this.taskList = taskList;
		nextJob();
	}

	@Override
	public void run() {
//		System.out.println("■■■ TaskChecker run()■■■");

		if (task != null) {
//			System.out.println("■■■ TaskChecker ■■■:"+ task.getMessage());
			// 開始時間からのｅｌａｐｓｅをとり一定時間内に処理が終わらない場合その旨のダイアログ を表示して中断させたい！！";
			// 一定の時間が経ちましたが処理を中断しますか？
			// yes＝＞ログを見ますか？　cpuの状態は？
			//			String info = task.getMessage();
//			if (prgCtrl != null) {
//				prgCtrl.setMessage( task.getMessage());
//			}
			if (task.isDone()) {
				task.stop();
				nextJob();
			} else if (task.isAbend()) {
				cancelIt();
				new Msgbox(null).warn("エラーにより処理を中断します");
			} else {
				// System.out.println("##### is DONE??? false #######");
			}
		} else {
			nextJob();
		}
		// ログ情報を表示する
		if (logWatcher != null) {
			logWatcher.setText(logDump());
		}
	}

	// --------------------------------------------------------------------
	// 別スレッドで実行しているものをまた別のスレッドで監視したい
	// 通常実行時のログの確認もやってみたい？
	// ログ出力を別スレッドでログ情報を監視したい・・・逆順に表示したい　いわゆるtaiｌ的なこと
	// ファイルを逆順に読みむ？
	// --------------------------------------------------------------------
	private void nextJob() {
		taskIndex++;
//		System.out.println("task#"+taskIndex);
		if (taskList != null && taskIndex < taskList.size()) {
			task = taskList.get(taskIndex);
			if (task != null) {
				task.setPrgCtrl(prgCtrl); // task側からプログレスバーを操作できるようにする
				task.logInfo(); // ログに開始情報を書き出す
				task.execute(); // ■ 実際の処理のスレッドを開始

				if (prgCtrl != null) {
					prgCtrl.resetProgress(task.getTotalLen());
				}
			}
		} else {
			if (prgCtrl != null) {
				prgCtrl.stop(true);
			}
			cancelIt();
		}
	}

	public void setLogWatcher(JTextComponent logWatcher) {
		this.logWatcher = logWatcher;
	}

	private void cancelIt() {
		setHourglass(parentCmp, false);
		if (this.timer != null)
			timer.cancel();
		if (this.task != null)
			this.task.stop();
		this.taskList = null;
	}

	public void interrupt() {
		cancelIt();
		new Msgbox(null).warn("CANCELにより処理を中断します");
	}

	public static String logDump() {
		StringBuffer buf = new StringBuffer();
		// System.out.println("<<<logDump>>> start ");
		List list = ListArrayUtil.file2List("c:/ssaAKA.log");
		// System.out.println("<<<logDump>>> list.size():"+list.size());
		// Collections.reverse(list);
		if (list == null) {
			System.out.println("?ERROR?@logDump");
			return "";
		}
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			buf.append(element);
			buf.append("\n");
		}
		return buf.toString();
	}
}
