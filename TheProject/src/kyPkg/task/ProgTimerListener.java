package kyPkg.task;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ProgressMonitor;

// -------------------------------------------------------------------------
// �^�C�}�[����莞�Ԃ����ɌĂ΂�āA�v���O���X�o�[�̏�Ԃ��X�V����
// -------------------------------------------------------------------------
public class ProgTimerListener implements ActionListener {
	private Component parent;
	private String message = "init ProgTimerListener";
	private int current = -1;
	private boolean canceled = false;
	private boolean odd = false;
	private int totalStep = 256;// �ő�X�e�b�v��
	private ProgressMonitor pm = null;
	private TaskWatcherGUI taskController; //�@�^�X�N�����荞�܂ꂽ�ꍇ�̏����ɑΉ����邽��

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
	 * ProgTimerListener	�R���X�g���N�^
	 * @param parent    - �_�C�A���O�{�b�N�X�̐e�R���|�[�l���g
	 * @param message   - �����I�ȃ��b�Z�[�W�B
	 * @param totalStep - �͈͂̏��
	 **************************************************************************/
	public ProgTimerListener(Component parent, String message, int totalStep) {
		super();
		if (totalStep <= 0)
			totalStep = 2048;// ���������Ă��Ȃ��Ɠ��삵�Ȃ��̂ŁE�E
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
		pm.setMillisToPopup(0); // �|�b�v�A�b�v���\�������܂ł̎��Ԃ�ݒ肵�܂��B
		pm.setMillisToDecideToPopup(0); // �i�����j�^�[��\�����邩�ǂ��������肷��܂ł̑҂����Ԃ�ݒ肵�܂��B
		pm.setProgress(1);
		//FIXME �y�v�C���I�z�_�C�A���O�������Ȃ����ۂ��������Ă��邽�߁A�����ŃN���[�Y�����Ă��邪�A�{���ԈႢ
		//	pm.close();
		return pm;
	}

	public void setMaximum(int totalStep) {
		this.totalStep = totalStep;
		pm.setMaximum(totalStep);
	}

	public void resetProgress(int totalStep) {
		System.out.println("������������������������������������������������������������");
		System.out.println("�� �@�@�@ProgressControl.resetProgress�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@�@��");
		System.out.println("������������������������������������������������������������");
		//		System.out.println("--- resetProgress ---totalStep�F" + totalStep);
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
			System.out.println("##	�������i�L�����Z���j�{�^���������ꂽ	##");
			System.exit(999);//20161019
			//TODO BUGFIX 20160201
			if (taskController != null)
				taskController.interrupt();//�@�^�X�N�����荞�܂ꂽ�ꍇ�̏����ɑΉ����邽��
			canceled = true;
		}
		//		if (current > totalStep) { 20160215
		//			canceled = true;
		//		} else 
		//			if (current < totalStep) {
		//		} else {
		//		if (current >= totalStep) {
		//			//�J�E���^�[�Ńv���O���X�o�[���I�������邱�Ƃ͂Ȃ��悤�ɂ���E�E�E20160204
		//			setTotalStep((int) (current * 1.2));
		//		}
		pm.setProgress(current);
		message = LogControl.getMessage();//�y20160202�z
		setNote(message);
		if (canceled) {
			stop();
		}
	}

	// --------------------------------------------------------------------
	// �_�C�A���O�̃��b�Z�[�W��ύX����
	// --------------------------------------------------------------------
	private void setNote(String note) {
		String wkNote = "";
		if (odd) {
			wkNote = "�������E�E�E";
			odd = false;
		} else {
			wkNote = note;
			odd = true;
		}
		pm.setNote(wkNote);
	}

	public void stop() {
		System.out.println("������������������������������������������������");
		System.out.println("�� �@�@�@ProgressControl.�� stop ���@�@�@�@�@�@�@�@�@�@�@�@�@��");
		System.out.println("������������������������������������������������");
		if (pm != null) {
			pm.setProgress(totalStep);
			pm.close();
		}
	}

	public void incrementProgress() {
		//TODO	�v�m�F	�J�E���^�[����萔�𒴂���ƃv���O���X�o�[���\������Ȃ��悤�ȋC������@�@20160215

		this.current++;
		//		System.out.println("��������incrementProgress()�������� current / totalStep=>"
		//				+ current + " / " + totalStep);
		//�C���N�������g�Ƃ������ʂ��g�[�^���𒴂��Ȃ��悤�ɒ�������E�E�E�y20160203�z
		//�J�E���^�[�Ńv���O���X�o�[���I�������邱�Ƃ͂Ȃ��悤�ɂ���E�E�E20160204
		if (current >= totalStep) {
			//�J�E���^�[�Ńv���O���X�o�[���I�������邱�Ƃ͂Ȃ��悤�ɂ���E�E�E20160204
			setMaximum((int) (current * 1.2));
		}
	}

	//	public void setMessage(String message) {
	//		this.message = message;
	//	}

}
