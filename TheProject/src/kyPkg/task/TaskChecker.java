package kyPkg.task;

import static kyPkg.util.CursorUtil.setHourglass;

import java.awt.Component;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.text.JTextComponent;

import kyPkg.uFile.ListArrayUtil;
import kyPkg.util.Msgbox;

// -------------------------------------------------------------------------
// �^�C�}�[����R�[�������^�X�N�Ǘ�
// -------------------------------------------------------------------------
public class TaskChecker extends TimerTask {
	private int taskIndex = -1;
	private Inf_ProgressTask task = null;

	private Timer timer;
	private List<Inf_ProgressTask> taskList;
	private ProgressControl prgCtrl;
	private JTextComponent logWatcher;
	private Component parentCmp;

	public TaskChecker(Component parentCmp, Timer timer,
			ProgressControl prgCtrl, List<Inf_ProgressTask> taskList) {
		super();
		this.parentCmp = parentCmp;
		this.timer = timer;
		this.prgCtrl = prgCtrl;
		this.taskList = taskList;
		nextJob();
	}

	@Override
	public void run() {
//		System.out.println("������ TaskChecker run()������");

		if (task != null) {
//			System.out.println("������ TaskChecker ������:"+ task.getMessage());
			// �J�n���Ԃ���̂��������������Ƃ��莞�ԓ��ɏ������I���Ȃ��ꍇ���̎|�̃_�C�A���O ��\�����Ē��f���������I�I";
			// ���̎��Ԃ��o���܂����������𒆒f���܂����H
			// yes�������O�����܂����H�@cpu�̏�Ԃ́H
			//			String info = task.getMessage();
//			if (prgCtrl != null) {
//				prgCtrl.setMessage( task.getMessage());
//			}
			if (task.isDone()) {
				task.stop();
				nextJob();
			} else if (task.isAbend()) {
				cancelIt();
				new Msgbox(null).warn("�G���[�ɂ�菈���𒆒f���܂�");
			} else {
				// System.out.println("##### is DONE??? false #######");
			}
		} else {
			nextJob();
		}
		// ���O����\������
		if (logWatcher != null) {
			logWatcher.setText(logDump());
		}
	}

	// --------------------------------------------------------------------
	// �ʃX���b�h�Ŏ��s���Ă�����̂��܂��ʂ̃X���b�h�ŊĎ�������
	// �ʏ���s���̃��O�̊m�F������Ă݂����H
	// ���O�o�͂�ʃX���b�h�Ń��O�����Ď��������E�E�E�t���ɕ\���������@������tai���I�Ȃ���
	// �t�@�C�����t���ɓǂ݂ށH
	// --------------------------------------------------------------------
	private void nextJob() {
		taskIndex++;
//		System.out.println("task#"+taskIndex);
		if (taskList != null && taskIndex < taskList.size()) {
			task = taskList.get(taskIndex);
			if (task != null) {
				task.setPrgCtrl(prgCtrl); // task������v���O���X�o�[�𑀍�ł���悤�ɂ���
				task.logInfo(); // ���O�ɊJ�n���������o��
				task.execute(); // �� ���ۂ̏����̃X���b�h���J�n

				if (prgCtrl != null) {
					prgCtrl.resetProgress(task.getTotalLen());
				}
			}
		} else {
			if (prgCtrl != null) {
				prgCtrl.stop(true);
			}
			cancelIt();
		}
	}

	public void setLogWatcher(JTextComponent logWatcher) {
		this.logWatcher = logWatcher;
	}

	private void cancelIt() {
		setHourglass(parentCmp, false);
		if (this.timer != null)
			timer.cancel();
		if (this.task != null)
			this.task.stop();
		this.taskList = null;
	}

	public void interrupt() {
		cancelIt();
		new Msgbox(null).warn("CANCEL�ɂ�菈���𒆒f���܂�");
	}

	public static String logDump() {
		StringBuffer buf = new StringBuffer();
		// System.out.println("<<<logDump>>> start ");
		List list = ListArrayUtil.file2List("c:/ssaAKA.log");
		// System.out.println("<<<logDump>>> list.size():"+list.size());
		// Collections.reverse(list);
		if (list == null) {
			System.out.println("?ERROR?@logDump");
			return "";
		}
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			buf.append(element);
			buf.append("\n");
		}
		return buf.toString();
	}
}
