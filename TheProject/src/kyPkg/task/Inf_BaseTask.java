package kyPkg.task;

import org.apache.commons.logging.Log;

public interface Inf_BaseTask {
	public abstract void setTaskName(String taskName);

	public abstract String getTaskName();

	public abstract void execute(); // 外部からコールされるトリガー

	public abstract void setMessage(String message); // 状態メッセージ

	public abstract String getMessage(); // 状態メッセージ

	public abstract void stop();// 終了状態

	public abstract boolean isStarted(); // 実行中かどうか

	public abstract boolean isDone();// 実行完了したか

	public abstract boolean isAbend();// エラー終了した

	public abstract int getStatus();

	public abstract void reset();// 初期状態

	public abstract void start(String taskName, int totalStep);// 開始状態

	public abstract void abend();// エラー終了

	// ------------------------------------------------------------------------
	// log
	// ------------------------------------------------------------------------
	public void logInfo(); // log出力

	public Log getLog(); // log出力

	// ------------------------------------------------------------------------
	// 処理結果などをhashmapなど受け渡す場合に使う
	// ------------------------------------------------------------------------
	public abstract String getStringExtra(String key);

	public abstract Integer getIntExtra(String key);

	public abstract void putExtra(String key, String val);

	public abstract void putExtra(String key, int val);

}
