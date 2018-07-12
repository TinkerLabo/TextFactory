package kyPkg.task;

import static kyPkg.util.CursorUtil.setHourglass;

import java.awt.Component;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

// ----+---------+---------+---------+---------+---------+---------+---------+
// repaint���s���Ɖ��L���̂R�̃��\�b�h���R�[�������
// paintComponent�ApaintBorder�ApaintChildren�̏��ԂŌĂяo����A
// �q���R���|�[�l���g��ɕ\�������悤�ɂ��܂��B
// ----+---------+---------+---------+---------+---------+---------+---------+
public class TaskWatcherGUI {
	private List<Inf_ProgressTask> tasks;
	private int totalStep = 1024;
	private Component parent;
	private List<JComponent> guiComps;
	private ProgressControl prgCtrl = null; // �v���O���b�V�u�R���g���[��
	private TaskChecker taskChecker;

	/**************************************************************************
	 * TaskWatcherGUI	�R���X�g���N�^
	 * @param parent	�_�C�A���O�̐e�ƂȂ�R���|�[�l���g
	 * @param guiComps	enable���R���g���[�����镔�i���w�肷��
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
		//20160606�v���O���X�o�[�̋��������������̂ŗv�g���[�X�E�E�E�܂�������
		// --------------------------------------------------------------------
		// �v���O���X�o�[�J�n
		// --------------------------------------------------------------------
		this.prgCtrl = new ProgressControl(parent, guiComps, this);
		this.prgCtrl.start("���΂炭�A���܂����������E�E�E", "start...", totalStep);
		//		this.prgCtrl.setTaskController(this);
		// --------------------------------------------------------------------
		// �W���u�Ď��^�C�}�[�N��
		// --------------------------------------------------------------------
		Timer timer = new Timer();
		taskChecker = new TaskChecker(parent, timer, prgCtrl, tasks);
		timer.schedule(taskChecker, 0, 20);// 0�~���b��ɋN�����A�Ȍ�20�~���b���ɌJ��Ԃ�
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
