package kyPkg.task;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class KickCallBack extends Abs_ProgressTask {
	private List<Inf_CallBack> list;
	@Override
	public void execute() {
		super.start("KickCallBack",2048);
		if (isStarted()) {
			final SwingWrk worker = new SwingWrk() {
				@Override
				public Object construct() {
					return new ActualTask(); // ���ۂ̏���
				}
			};
			worker.start();
		}
		super.stop();// ����I��
	};

	class ActualTask {
		ActualTask() {
			for (Iterator it = list.iterator(); it.hasNext();) {
				Inf_CallBack callBack = (Inf_CallBack) it.next();
				callBack.callBack();
			}
			setCurrent(250);
			stop();// ����I��
 
		}
	}
	public KickCallBack(List callbackList) {
		super();
		this.list = callbackList;
		super.setTitle("�R�[���o�b�N����");
	}

	public KickCallBack(Inf_CallBack callback) {
		this( Arrays.asList(new Inf_CallBack[]{callback}));
	}

}
