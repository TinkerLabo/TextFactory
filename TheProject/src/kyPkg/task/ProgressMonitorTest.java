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
		ProgressMonitor pm = new ProgressMonitor(null, "���b�Z�[�W", "�m�[�g", min,
				max);
		pm.setMillisToPopup(0); // �|�b�v�A�b�v���\�������܂ł̎��Ԃ�ݒ肵�܂��B
		pm.setMillisToDecideToPopup(0); // �i�����j�^�[��\�����邩�ǂ��������肷��܂ł̑҂����Ԃ�ݒ肵�܂��B
		pm.setMaximum(max);
		for (int i = min; i < max; i++) {
			if (pm.isCanceled()) { // �I������
				pm.close();
				break;
			}
			System.out.println("���݁F" + i);
			pm.setNote("���݁F" + i);
			//-----------------------------------------------------------------
			// �����̏���
			//-----------------------------------------------------------------
			pm.setProgress(i + 1); // �v���O���X�o�[�Ɍ��ݒl���Z�b�g
		}
		pm.close();
		System.out.println("pm.close()");
		System.out.println("#testProgressMonitor end#");
	}

}
