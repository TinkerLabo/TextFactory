package kyPkg.task;

import javax.swing.SwingUtilities;

//-----------------------------------------------------------------------------
// タスクを外部スレッド化してコントロールするのに使用する
// 特に変更したい点が無いので・・チュートリアルを流用している
//-----------------------------------------------------------------------------
public abstract class SwingWrk {
	private Object value; // see getValue(), setValue()

	// -------------------------------------------------------------------------
	/**
	 * Class to maintain reference to current worker thread under separate
	 * synchronization control.
	 */
	private static class ThreadVar {
		private Thread thread;
		ThreadVar(Thread t) {
			thread = t;
		}
		synchronized Thread get() {
			return thread;
		}
		synchronized void clear() {
			thread = null;
		}
	}

	private ThreadVar threadVar;

	// -------------------------------------------------------------------------
	/**
	 * Get the value produced by the worker thread, or null if it hasn't been
	 * constructed yet.
	 */
	protected synchronized Object getValue() {
		return value;
	}

	// -------------------------------------------------------------------------
	/**
	 * Set the value produced by worker thread
	 */
	private synchronized void setValue(Object x) {
		value = x;
	}

	// -------------------------------------------------------------------------
	/**
	 * Compute the value to be returned by the <code>get</code> method.
	 */
	public abstract Object construct();

	// -------------------------------------------------------------------------
	/**
	 * Called on the event dispatching thread (not on the worker thread) after
	 * the <code>construct</code> method has returned.
	 */
	public void finished() {
	}

	// -------------------------------------------------------------------------
	// 処理を中断させる
	// -------------------------------------------------------------------------
	public void interrupt() {
		Thread t = threadVar.get();
		if (t != null) {
			t.interrupt();
		}
		threadVar.clear();
	}

	// -------------------------------------------------------------------------
	/**
	 * Return the value created by the <code>construct</code> method. Returns
	 * null if either the constructing thread or the current thread was
	 * interrupted before a value was produced.
	 * 
	 * @return the value created by the <code>construct</code> method
	 */
	public Object get() {
		while (true) {
			Thread t = threadVar.get();
			if (t == null) {
				return getValue();
			}
			try {
				t.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // propagate
				return null;
			}
		}
	}

	// -------------------------------------------------------------------------
	// スレッドを開始するとconstructメソッドを呼ぶ
	public SwingWrk() {
		final Runnable doFinished = new Runnable() {
			@Override
			public void run() {
				finished();
			}
		};

		Runnable doConstruct = new Runnable() {
			@Override
			public void run() {
				try {
					setValue(construct());
				} finally {
					threadVar.clear();
				}

				SwingUtilities.invokeLater(doFinished);
			}
		};

		Thread t = new Thread(doConstruct);
		threadVar = new ThreadVar(t);
	}

	// -------------------------------------------------------------------------
	// workerのスレッドを実行する
	public void start() {
		Thread t = threadVar.get();
		if (t != null) {
			t.start();
		}
	}
}
