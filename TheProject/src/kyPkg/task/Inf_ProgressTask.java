package kyPkg.task;

public interface Inf_ProgressTask extends Inf_BaseTask {

	public abstract int getCurrent();

	public abstract int getTotalLen();

	public abstract void setProgress(int current, int totalLen);

	public abstract void setPrgCtrl(ProgressControl prgCtrl);

}
