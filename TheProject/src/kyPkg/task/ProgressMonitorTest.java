package kyPkg.task;

import java.awt.Color;

import javax.swing.ProgressMonitor;
import javax.swing.UIManager;

public class ProgressMonitorTest {

	public ProgressMonitorTest() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] argv) {
		testProgressMonitor();
	}

	public static void testProgressMonitor() {
		 UIManager.put("ProgressBar.background", Color.LIGHT_GRAY);
		 UIManager.put("ProgressBar.foreground", Color.gray);
		 UIManager.put("ProgressBar.selectionBackground", Color.red);
		 UIManager.put("ProgressBar.selectionForeground", Color.green);
		System.out.println("#testProgressMonitor start#");
		int min = 0;
		int max = 500 * 10000;
		max=Integer.MAX_VALUE;
		ProgressMonitor pm = new ProgressMonitor(null, "メッセージ", "ノート", min,
				max);
		pm.setMillisToPopup(0); // ポップアップが表示されるまでの時間を設定します。
		pm.setMillisToDecideToPopup(0); // 進捗モニターを表示するかどうかを決定するまでの待ち時間を設定します。
		pm.setMaximum(max);
		for (int i = min; i < max; i++) {
			if (pm.isCanceled()) { // 終了判定
				pm.close();
				break;
			}
			System.out.println("現在：" + i);
			pm.setNote("現在：" + i);
			//-----------------------------------------------------------------
			// 何かの処理
			//-----------------------------------------------------------------
			pm.setProgress(i + 1); // プログレスバーに現在値をセット
		}
		pm.close();
		System.out.println("pm.close()");
		System.out.println("#testProgressMonitor end#");
	}

}
