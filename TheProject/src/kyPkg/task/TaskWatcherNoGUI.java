package kyPkg.task;

import java.util.Arrays;
import java.util.List;

//�������������Ăčs���d�|���i�����ӁI�I�}���`�X���b�h�������킯�ł͂Ȃ��j
//�}���`�X���b�h������ɂ́A�X�̏�����execute�ŕʃX���b�h�ɂ���d�|�����K�v
public class TaskWatcherNoGUI {
	public final static int TIMEOUT = 30 * 10; // �� �ĕ`��Ԋu�E�E30��
	public final static int ONE_SECOND = 1000 * 1; // �� �ĕ`��Ԋu�E�E1�b
	private int taskIndex = 0;
	private List<Inf_ProgressTask> taskList;
	private Inf_ProgressTask currentTask; // �� ���ۂ̏���

	// FIXME ,boolean debug

	// �R���X�g���N�^
	// TaskWatcherGUI �Ƃ̐؂�ւ��݊��p
	public TaskWatcherNoGUI(Object dummy1, Object dummy2, List<Inf_ProgressTask> tasks) {
		this.taskList = tasks;
	}

	// �R���X�g���N�^
	public TaskWatcherNoGUI(List<Inf_ProgressTask> tasks) {
		this.taskList = tasks;
	}

	public TaskWatcherNoGUI(Inf_ProgressTask task) {
		this(Arrays.asList(new Inf_ProgressTask[] { task }));
	}

	public void execute() {
		currentTask = taskList.get(taskIndex);
		currentTask.execute();// �� ���ۂ̏����̃X���b�h���J�n
		int chkTimeOut = 0;
		while (true) {
			chkTimeOut++;
			if (chkTimeOut > TIMEOUT) {
				System.out.println("@TaskWatcherNoGUI TIMEOUT!");
				break;
			}
			// System.out.println("Loop�̒��E�E�E�EloopCnt=>" + chkTimeOut);
			System.out.println(">>TaskName:" + currentTask.getTaskName()
					+ " #Status:" + currentTask.getStatus());
			if (currentTask.isDone()) {
				taskIndex++;
				if (taskIndex < taskList.size()) {
					// �����Œ��O�̃^�X�N�̃X�e�[�^�X���݂Ď��ɍs�����ǂ������肷��I�I
					currentTask = taskList.get(taskIndex);
					currentTask.logInfo(); // ���O�ɊJ�n���������o��
					currentTask.execute(); // �� ���ۂ̏����̃X���b�h���J�n
					chkTimeOut = 0;
				} else {
					// Toolkit.getDefaultToolkit().beep();
					// System.out.println("nogui	timer.stop�E�E�E�E");
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
