package kyPkg.task;

import java.util.Arrays;
import java.util.List;

//処理を順序だてて行う仕掛け（※注意！！マルチスレッド化されるわけではない）
//マルチスレッド化するには、個々の処理のexecuteで別スレッドにする仕掛けが必要
public class TaskWatcherNoGUI {
	public final static int TIMEOUT = 30 * 10; // ■ 再描画間隔・・30分
	public final static int ONE_SECOND = 1000 * 1; // ■ 再描画間隔・・1秒
	private int taskIndex = 0;
	private List<Inf_ProgressTask> taskList;
	private Inf_ProgressTask currentTask; // ■ 実際の処理

	// FIXME ,boolean debug

	// コンストラクタ
	// TaskWatcherGUI との切り替え互換用
	public TaskWatcherNoGUI(Object dummy1, Object dummy2, List<Inf_ProgressTask> tasks) {
		this.taskList = tasks;
	}

	// コンストラクタ
	public TaskWatcherNoGUI(List<Inf_ProgressTask> tasks) {
		this.taskList = tasks;
	}

	public TaskWatcherNoGUI(Inf_ProgressTask task) {
		this(Arrays.asList(new Inf_ProgressTask[] { task }));
	}

	public void execute() {
		currentTask = taskList.get(taskIndex);
		currentTask.execute();// ■ 実際の処理のスレッドを開始
		int chkTimeOut = 0;
		while (true) {
			chkTimeOut++;
			if (chkTimeOut > TIMEOUT) {
				System.out.println("@TaskWatcherNoGUI TIMEOUT!");
				break;
			}
			// System.out.println("Loopの中・・・・loopCnt=>" + chkTimeOut);
			System.out.println(">>TaskName:" + currentTask.getTaskName()
					+ " #Status:" + currentTask.getStatus());
			if (currentTask.isDone()) {
				taskIndex++;
				if (taskIndex < taskList.size()) {
					// ここで直前のタスクのステータスをみて次に行くかどうか判定する！！
					currentTask = taskList.get(taskIndex);
					currentTask.logInfo(); // ログに開始情報を書き出す
					currentTask.execute(); // ■ 実際の処理のスレッドを開始
					chkTimeOut = 0;
				} else {
					// Toolkit.getDefaultToolkit().beep();
					// System.out.println("nogui	timer.stop・・・・");
					break;
				}
			}
			try {
				Thread.sleep(ONE_SECOND);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
