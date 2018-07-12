package kyPkg.task;

import static kyPkg.util.CursorUtil.setHourglass;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;

public class ProgressControl {
	private final static int ONE_SECOND = 1000; // ��ʂ��ĕ`�悷��Ԋu
	private Component parent;
	private List<JComponent> guiComps;

	private static Timer prgTimer = null; // progressBar���ĕ`�悳����^�C�}�[
	private static ProgTimerListener timerListener;
	private TaskWatcherGUI taskController;
	//	public void setTaskController(TaskWatcherGUI taskController,boolean debug) {
	//		//TODO BUGFIX 20160201
	//		if (timerListener != null)
	//			timerListener.setTaskController(taskController);
	//	}

	/**************************************************************************
	 * ProgressControl		�R���X�g���N�^
	 * @param parent			 
	 * @param guiComps			 
	 **************************************************************************/
	public ProgressControl(Component parent, List<JComponent> guiComps,TaskWatcherGUI taskController) {
		super();
		this.parent = parent;
		this.guiComps = guiComps;
		this.taskController = taskController;
	}

	public ProgressControl(Component parent, JComponent guiComponent,
			int totalStep, String info, TaskWatcherGUI taskController) {
		this(parent, Arrays.asList(new JComponent[] { guiComponent }),
				taskController);
	}

	// ------------------------------------------------------------------------
	// �Ď��J�n
	// ------------------------------------------------------------------------
	public void start(String message, String info, int totalStep) {
		System.out.println("��������������������������������������������������������");
		System.out.println("�� �@�@�@ProgressControl.start�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@��");
		System.out.println("��������������������������������������������������������");
		System.out.println(
				"--- ProgressControl.start() ---totalStep:" + totalStep);
		//		stop();//�@�Ȃ�ł��Ƃ��Ղ���񂾁H�H�H
		setHourglass(parent, true);
		enableIt(guiComps, false); // �{�^���Ȃǂ�Enabled(false)�ɂ���
		timerListener = new ProgTimerListener(parent, message, totalStep);
		if (taskController != null)
			timerListener.setTaskController(taskController);
		prgTimer = new Timer(ONE_SECOND, timerListener);
		prgTimer.start(); // �ĕ`��p�^�C�}�[���n��
	}

	public void stop(boolean debug) {
		System.out.println("��������������������������������������������������������");
		System.out.println("�� �@�@�@ProgressControl.stop�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@��");
		System.out.println("��������������������������������������������������������");
		//		System.out.println("--- ProgressControl.stop() ---");
		enableIt(guiComps, true); // �{�^���Ȃǂ�Enabled(true)�ɂ���
		setHourglass(parent, false);
		if (timerListener != null) {
			timerListener.stop();
		}
		if (prgTimer != null) {
			prgTimer.stop();
		}
	}

	public void resetProgress(int totalStep) {
		if (timerListener != null)
			timerListener.resetProgress(totalStep);
	}

	public int getTotalStep() {
		if (timerListener != null)
			return timerListener.getTotalStep();
		return -1;
	}

	public int getCurrent() {
		if (timerListener != null)
			return timerListener.getCurrent();
		return -1;
	}

	public void setCurrent(int pCurrent) {
		if (timerListener != null)
			timerListener.setCurrent(pCurrent);
	}

	public void incrementProgress() {
		if (timerListener != null)
			timerListener.incrementProgress();
	}

	private void enableIt(List<JComponent> guiComps, boolean enabled) {
		if (guiComps == null)
			return;
		for (JComponent element : guiComps) {
			if (element != null)
				element.setEnabled(enabled);
		}
	}

}
