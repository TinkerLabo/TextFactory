package kyPkg.frame;

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.*;

public class JProgressBarTest3 extends JFrame implements ActionListener {

	private static final String TIMER = "timer";
	private static final String STOP = "stop";
	private static final String START = "start";
	private Timer timer;
	private JButton btnStart;
	private JButton btnStop;
	private JProgressBar bar;
	private JLabel label;
	private int count;

	public static void main(String[] args) {
		JProgressBarTest3 frame = new JProgressBarTest3();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(10, 10, 300, 300);
		frame.setTitle("タイトル");
		frame.setVisible(true);
	}

	JProgressBarTest3() {
		count = 0;


		btnStart = new JButton("開始");
		btnStart.addActionListener(this);
		btnStart.setActionCommand(START);

		btnStop = new JButton("終了");
		btnStop.addActionListener(this);
		btnStop.setActionCommand(STOP);
		btnStop.setEnabled(false);

		JPanel pnlButtons = new JPanel();
		pnlButtons.add(btnStart);
		pnlButtons.add(btnStop);



		timer = new Timer(100, this);
		timer.setActionCommand(TIMER);


		JPanel labelPanel = new JPanel();
		label = new JLabel("Not Start");
		labelPanel.add(label);

		
		JPanel barPanel = new JPanel();
		bar = new JProgressBar(JProgressBar.HORIZONTAL);
		bar.setValue(0);
		barPanel.add(bar);

		
		getContentPane().add(labelPanel, BorderLayout.PAGE_START);
		getContentPane().add(barPanel, BorderLayout.CENTER);
		getContentPane().add(pnlButtons, BorderLayout.PAGE_END);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if (cmd.equals(START)) {
			btnStart.setEnabled(false);
			btnStop.setEnabled(true);

			timer.start();
		} else if (cmd.equals(STOP)) {
			btnStart.setEnabled(true);
			btnStop.setEnabled(false);

			timer.stop();
		} else if (cmd.equals(TIMER)) {
			label.setText(count + " count");

			if (count >= 100) {
				btnStart.setEnabled(true);
				btnStop.setEnabled(false);

				timer.stop();

				bar.setValue(count);

				count = 0;
			} else {
				count++;
				bar.setValue(count);
			}
		}
	}
}
