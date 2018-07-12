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
					return new ActualTask(); // 実際の処理
				}
			};
			worker.start();
		}
		super.stop();// 正常終了
	};

	class ActualTask {
		ActualTask() {
			for (Iterator it = list.iterator(); it.hasNext();) {
				Inf_CallBack callBack = (Inf_CallBack) it.next();
				callBack.callBack();
			}
			setCurrent(250);
			stop();// 正常終了
 
		}
	}
	public KickCallBack(List callbackList) {
		super();
		this.list = callbackList;
		super.setTitle("コールバック処理");
	}

	public KickCallBack(Inf_CallBack callback) {
		this( Arrays.asList(new Inf_CallBack[]{callback}));
	}

}
