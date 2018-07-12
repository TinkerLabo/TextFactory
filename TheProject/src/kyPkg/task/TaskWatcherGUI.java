package kyPkg.task;

import static kyPkg.util.CursorUtil.setHourglass;

import java.awt.Component;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

// ----+---------+---------+---------+---------+---------+---------+---------+
// repaintを行うと下記↓の３つのメソッドがコールされる
// paintComponent、paintBorder、paintChildrenの順番で呼び出され、
// 子がコンポーネント上に表示されるようにします。
// ----+---------+---------+---------+---------+---------+---------+---------+
public class TaskWatcherGUI {
	private List<Inf_ProgressTask> tasks;
	private int totalStep = 1024;
	private Component parent;
	private List<JComponent> guiComps;
	private ProgressControl prgCtrl = null; // プログレッシブコントロール
	private TaskChecker taskChecker;

	/**************************************************************************
	 * TaskWatcherGUI	コンストラクタ
	 * @param parent	ダイアログの親となるコンポーネント
	 * @param guiComps	enableをコントロールする部品を指定する
	 * @param tasks	
	 **************************************************************************/
	public TaskWatcherGUI(Component parent, List<JComponent> guiComps,
			List<Inf_ProgressTask> tasks) {
		super();
		this.parent = parent;// 
		this.guiComps = guiComps;// 
		this.tasks = tasks;
	}

	public TaskWatcherGUI(Component parent, JComponent guiComps,
			Inf_ProgressTask task) {
		this(parent, Arrays.asList(new JComponent[] { guiComps }),
				Arrays.asList(new Inf_ProgressTask[] { task }));
	}

	public TaskWatcherGUI(Component parent, JComponent guiComp,
			List<Inf_ProgressTask> tasks) {
		this(parent, Arrays.asList(new JComponent[] { guiComp }), tasks);
	}

	//for debug
	public TaskWatcherGUI(String title, List<Inf_ProgressTask> taskList) {
		this((Component) null, (List<JComponent>) null, taskList);
	}

	public void execute() {
		setHourglass(parent, true);
		//20160606プログレスバーの挙動がおかしいので要トレース・・・まいったね
		// --------------------------------------------------------------------
		// プログレスバー開始
		// --------------------------------------------------------------------
		this.prgCtrl = new ProgressControl(parent, guiComps, this);
		this.prgCtrl.start("しばらく、おまちください・・・", "start...", totalStep);
		//		this.prgCtrl.setTaskController(this);
		// --------------------------------------------------------------------
		// ジョブ監視タイマー起動
		// --------------------------------------------------------------------
		Timer timer = new Timer();
		taskChecker = new TaskChecker(parent, timer, prgCtrl, tasks);
		timer.schedule(taskChecker, 0, 20);// 0ミリ秒後に起動し、以後20ミリ秒毎に繰り返す
	};

	public void setLogWatcher(JTextComponent logWatcher) {
		taskChecker.setLogWatcher(logWatcher);
	}

	public void interrupt() {
		taskChecker.interrupt();
	}

	public void addTask(Inf_ProgressTask task) {
		this.tasks.add(task);
	}

}
