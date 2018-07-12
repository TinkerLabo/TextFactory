package kyPkg.task;

import org.apache.commons.logging.Log;

public interface Inf_BaseTask {
	public abstract void setTaskName(String taskName);

	public abstract String getTaskName();

	public abstract void execute(); // �O������R�[�������g���K�[

	public abstract void setMessage(String message); // ��ԃ��b�Z�[�W

	public abstract String getMessage(); // ��ԃ��b�Z�[�W

	public abstract void stop();// �I�����

	public abstract boolean isStarted(); // ���s�����ǂ���

	public abstract boolean isDone();// ���s����������

	public abstract boolean isAbend();// �G���[�I������

	public abstract int getStatus();

	public abstract void reset();// �������

	public abstract void start(String taskName, int totalStep);// �J�n���

	public abstract void abend();// �G���[�I��

	// ------------------------------------------------------------------------
	// log
	// ------------------------------------------------------------------------
	public void logInfo(); // log�o��

	public Log getLog(); // log�o��

	// ------------------------------------------------------------------------
	// �������ʂȂǂ�hashmap�Ȃǎ󂯓n���ꍇ�Ɏg��
	// ------------------------------------------------------------------------
	public abstract String getStringExtra(String key);

	public abstract Integer getIntExtra(String key);

	public abstract void putExtra(String key, String val);

	public abstract void putExtra(String key, int val);

}
