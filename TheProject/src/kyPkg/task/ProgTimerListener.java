package kyPkg.task;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ProgressMonitor;

// -------------------------------------------------------------------------
// タイマーより一定時間おきに呼ばれて、プログレスバーの状態を更新する
// -------------------------------------------------------------------------
public class ProgTimerListener implements ActionListener {
	private Component parent;
	private String message = "init ProgTimerListener";
	private int current = -1;
	private boolean canceled = false;
	private boolean odd = false;
	private int totalStep = 256;// 最大ステップ数
	private ProgressMonitor pm = null;
	private TaskWatcherGUI taskController; //　タスクが割り込まれた場合の処理に対応するため

	public void setTaskController(TaskWatcherGUI taskController) {
		this.taskController = taskController;
	}

	public int getTotalStep() {
		return totalStep;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	/**************************************************************************
	 * ProgTimerListener	コンストラクタ
	 * @param parent    - ダイアログボックスの親コンポーネント
	 * @param message   - 説明的なメッセージ。
	 * @param totalStep - 範囲の上限
	 **************************************************************************/
	public ProgTimerListener(Component parent, String message, int totalStep) {
		super();
		if (totalStep <= 0)
			totalStep = 2048;// 何か入っていないと動作しないので・・
		this.parent=parent;
		this.message = message;
		this.current = 0;
		this.canceled = false;
		if (pm != null) {
			System.err.println("### pm != null ###");
		}

		pm = getPM(parent, message, totalStep);

		setMaximum(totalStep);
	}

	private ProgressMonitor getPM(Component parent, String message,
			int totalStep) {
		if (pm == null) {
			int min = 0;
			pm = new ProgressMonitor(parent, message, "start", min, totalStep);
		}
		pm.setMillisToPopup(0); // ポップアップが表示されるまでの時間を設定します。
		pm.setMillisToDecideToPopup(0); // 進捗モニターを表示するかどうかを決定するまでの待ち時間を設定します。
		pm.setProgress(1);
		//FIXME 【要修正！】ダイアログが消せない現象が発生しているため、ここでクローズを入れているが、本来間違い
		//	pm.close();
		return pm;
	}

	public void setMaximum(int totalStep) {
		this.totalStep = totalStep;
		pm.setMaximum(totalStep);
	}

	public void resetProgress(int totalStep) {
		System.out.println("◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇");
		System.out.println("◇ 　　　ProgressControl.resetProgress　　　　　　　　　　　　　　　　　　◇");
		System.out.println("◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇");
		//		System.out.println("--- resetProgress ---totalStep：" + totalStep);
		if (pm != null) {
			stop();
		}
		pm = getPM(parent, message, totalStep);//20170424

//		setMaximum(totalStep);
//		pm.setProgress(0);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (pm.isCanceled()) {
			System.out.println("##	取り消し（キャンセル）ボタンがおされた	##");
			System.exit(999);//20161019
			//TODO BUGFIX 20160201
			if (taskController != null)
				taskController.interrupt();//　タスクが割り込まれた場合の処理に対応するため
			canceled = true;
		}
		//		if (current > totalStep) { 20160215
		//			canceled = true;
		//		} else 
		//			if (current < totalStep) {
		//		} else {
		//		if (current >= totalStep) {
		//			//カウンターでプログレスバーを終了させることはないようにする・・・20160204
		//			setTotalStep((int) (current * 1.2));
		//		}
		pm.setProgress(current);
		message = LogControl.getMessage();//【20160202】
		setNote(message);
		if (canceled) {
			stop();
		}
	}

	// --------------------------------------------------------------------
	// ダイアログのメッセージを変更する
	// --------------------------------------------------------------------
	private void setNote(String note) {
		String wkNote = "";
		if (odd) {
			wkNote = "処理中・・・";
			odd = false;
		} else {
			wkNote = note;
			odd = true;
		}
		pm.setNote(wkNote);
	}

	public void stop() {
		System.out.println("◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇");
		System.out.println("◇ 　　　ProgressControl.■ stop ■　　　　　　　　　　　　　◇");
		System.out.println("◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇");
		if (pm != null) {
			pm.setProgress(totalStep);
			pm.close();
		}
	}

	public void incrementProgress() {
		//TODO	要確認	カウンターが一定数を超えるとプログレスバーが表示されないような気がする　　20160215

		this.current++;
		//		System.out.println("■■■■incrementProgress()■■■■ current / totalStep=>"
		//				+ current + " / " + totalStep);
		//インクリメントとした結果がトータルを超えないように調整する・・・【20160203】
		//カウンターでプログレスバーを終了させることはないようにする・・・20160204
		if (current >= totalStep) {
			//カウンターでプログレスバーを終了させることはないようにする・・・20160204
			setMaximum((int) (current * 1.2));
		}
	}

	//	public void setMessage(String message) {
	//		this.message = message;
	//	}

}
