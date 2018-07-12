package kyPkg.task;

import javax.swing.SwingUtilities;

//-----------------------------------------------------------------------------
// �^�X�N���O���X���b�h�����ăR���g���[������̂Ɏg�p����
// ���ɕύX�������_�������̂ŁE�E�`���[�g���A���𗬗p���Ă���
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
	// �����𒆒f������
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
	// �X���b�h���J�n�����construct���\�b�h���Ă�
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
	// worker�̃X���b�h�����s����
	public void start() {
		Thread t = threadVar.get();
		if (t != null) {
			t.start();
		}
	}
}
