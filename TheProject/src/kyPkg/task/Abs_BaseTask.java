package kyPkg.task;

import java.util.HashMap;

import org.apache.commons.logging.Log;

/** Uses a SwingWorker to perform a time-consuming (and utterly fake) task. */
public abstract class Abs_BaseTask implements Inf_BaseTask {
	public static final int STARTED = 1;
	public static final int ABEND = 999;// ABNORMAL END
	public static final int DONE = 0;
	private int status = DONE;
	private boolean debug = true;

	private HashMap<String, Object> extra;// 処理結果を不定形で返す
	private String message = "<<< W a i t ...>>>";
	private String title = "<<title>>";
	private String taskName = "";

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	@Override
	public String getTaskName() {
		return taskName;
	}

	// --------------------------------------------------------------------------
	// コンストラクタ
	// --------------------------------------------------------------------------
	public Abs_BaseTask() {
		super();
		reset();
	}

	// --------------------------------------------------------------------------
	// status 状態設定
	// --------------------------------------------------------------------------
	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public void reset() {
		status = DONE;
	}

	@Override
	public boolean isStarted() {
		return (status == STARTED);
	}

	@Override
	public boolean isDone() {
		return (status == DONE);
	}

	@Override
	public boolean isAbend() {
		return (status == ABEND);
	}

	@Override
	public void start(String taskName, int totalStep) {
		setTaskName(taskName);
		status = STARTED;
		if (debug)
			System.out.println("status STARTED:" + taskName);
	}

	@Override
	public void stop() {
		status = DONE;
		if (debug)
			System.out.println("status STOP:" + taskName);
	}

	@Override
	public void abend() {
		status = ABEND;
		if (debug)
			System.out.println("status ABEND:" + taskName);
	}

	// --------------------------------------------------------------------------
	// 処理結果を不定形で返す
	// --------------------------------------------------------------------------
	@Override
	public void putExtra(String key, int val) {
		if (extra == null)
			extra = new HashMap();
		extra.put(key, new Integer(val));
	}

	@Override
	public void putExtra(String key, String val) {
		if (extra == null)
			extra = new HashMap();
		extra.put(key, val);
	}

	@Override
	public Integer getIntExtra(String key) {
		Object obj = extra.get(key);
		if (obj != null && obj instanceof Integer) {
			return (Integer) obj;
		}
		return null;
	}

	@Override
	public String getStringExtra(String key) {
		Object obj = extra.get(key);
		if (obj != null && obj instanceof String) {
			return (String) obj;
		}
		return null;
	}

	// --------------------------------------------------------------------------
	// ログ関連
	// --------------------------------------------------------------------------
	public void info(String message) {
		this.message = message;
		LogControl.info(message);

	}

	@Override
	public void logInfo() {
		LogControl.info(message);
	}

	@Override
	public Log getLog() {
		return LogControl.log;
	}

	// 引数は英数字のみ可
	public static void infoBanner(String message) {
		LogControl.infoBanner(message);
	}

}