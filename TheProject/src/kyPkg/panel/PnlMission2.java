package kyPkg.panel;
import kyPkg.panelMini.*;
import kyPkg.jobs.*;
import kyPkg.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
// ============================================================================
public class PnlMission2 extends JP_Ancestor implements ActionListener {
	private static final long serialVersionUID = -8224092981175154740L;
	public final static int ONE_SECOND = 1000; // ��ʂ��ĕ`�悷��Ԋu
	// -------------------------------------------------------------------------
	// Local�ϐ�
	// -------------------------------------------------------------------------
	private PnlTimeTable timeTable;
	private PnlSlider pSliderMin;
	private PnlSlider pSliderSec;
	private JButton jBtTimer; // �^�C�}�[�{�^��
	private JLabel jLaStat;
 	private JTextArea txtInfo;
 	private boolean activeSw = false;
	private boolean wakuUpSw = false;
	private int delay; // �^�C�}�[�̃C���^�[�o�� milliseconds�w��Ȃ̂�
	private javax.swing.Timer scTimer; // �X�P�W���[���[�̃^�C�}�[
	private int tCounter;
	//private JCheckBox jCb_xxx[];
	// -------------------------------------------------------------------------
	// �s�f�t�h�֘A�t
	// -------------------------------------------------------------------------
	void createGUI() {
		
		JPanel pnlMission = new JPanel() {
			private static final long serialVersionUID = 1L;
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Ruler.drawRuler(g, this.getSize().width, this.getSize().height,
						new Color(155, 187, 244));
			}
		};
		pnlMission.setLayout(null);
		pnlMission.setPreferredSize(new Dimension(640, 150));

		jLaStat = new JLabel("Status");		jLaStat.setPreferredSize(new Dimension(100, 20));
		jLaStat.setOpaque(true);
		jLaStat.setBackground(Color.WHITE);
		jLaStat.setHorizontalAlignment(SwingConstants.CENTER);
		jBtTimer = new JButton("���s");		jBtTimer.setPreferredSize(new Dimension( 100, 20));

		txtInfo = new JTextArea("");
		JScrollPane jSp2 = new JScrollPane(txtInfo);
		jSp2.setPreferredSize(new Dimension(250, 200));

		JPanel pnlBtns = new JPanel(new FlowLayout());
		pnlBtns.add(jLaStat,BorderLayout.SOUTH );
		pnlBtns.add(jBtTimer,BorderLayout.SOUTH );
		this.add(pnlBtns,BorderLayout.NORTH);
		this.add(pnlMission, BorderLayout.CENTER);
		this.add(jSp2,BorderLayout.SOUTH);

		// ---------------------------------------------------------------------
		// �^�C���e�[�u��
		// ---------------------------------------------------------------------
		timeTable = new PnlTimeTable();
//		timeTable.setPreferredSize(new Dimension(100, 300));
		timeTable.setBounds(400, 0, 200, 100);
		pnlMission.add(timeTable);

		// ---------------------------------------------------------------------
		// ���v�p�l�� ����
		// ---------------------------------------------------------------------
		JP_Clock objClock = new JP_Clock();
		Color xColor = new Color(128, 150, 161);
		objClock.setBackColor(xColor);
		objClock.initGui(100, 100, this, null);
//		objClock.initGui(100, 100,  null);
		objClock.resizeMe(100, 100); // ��������Ȃ��ƃo�O��I�I
		objClock.setBounds(0, 0, 100, 100);
		pnlMission.add(objClock);

		// ---------------------------------------------------------------------
		// �X���C�_�[�p�l�� Minute
		// ---------------------------------------------------------------------
		pSliderMin =new PnlSlider("Min", 0,120,0,40,20);	pSliderMin.setBounds(100,  0, 250, 50);		
		pSliderSec =new PnlSlider("Sec", 0,59,30,10,5);		pSliderSec.setBounds(100, 50, 250, 50);		
		pnlMission.add(pSliderMin);
		pnlMission.add(pSliderSec);

		// ---------------------------------------------------------------------
		// ���^�C�}�[�{�^��
		// ---------------------------------------------------------------------
		jBtTimer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (activeSw == true) {
					jBtTimer.setText("���s");
					activeSw = false;
					scTimer.stop();
					jLaStat.setForeground(Color.BLACK);
					jLaStat.setBackground(Color.WHITE);
					pSliderMin.setEnabled(true);
					pSliderSec.setEnabled(true);
				} else {
					pSliderMin.setEnabled(false);
					pSliderSec.setEnabled(false);
					jBtTimer.setText("��~");
					activeSw = true;
					// ---------------------------------------------------------
					// �X���C�_�[���擾����
					// ---------------------------------------------------------
					int wMin = pSliderMin.getValue();
					int wSec = pSliderSec.getValue();
					delay = (wMin * 60 + wSec);
					tCounter = delay;
					System.out.println("wMin:" + wMin);
					System.out.println("wSec:" + wSec);
					System.out.println("delay:" + delay);
					scTimer = new javax.swing.Timer((delay * 1000),
							new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent evt) {
									System.out.println("...Perform a task...");
									timerAction();
								}
							});
					scTimer.start();
				}
			}
		});

	}

	// -----------------------------------------------------------------------------------
	// �^�C�}�[�Ŏ��s������A�N�V����
	// -----------------------------------------------------------------------------------
	void timerAction() {
		txtInfo.setText("");
		if (wakuUpSw == true) {
			txtInfo.setText("���s��");
			Thread th1 = new Thread() {
				@Override
				public void run() {
					// Mission01.mission();
					Mission02.mission();
					txtInfo.setText("");
				}
			};
			th1.start();
			tCounter = delay;
			/*
			 * if(!wReadPath.equals("")){ Thread th1 = new Thread(){ public void
			 * run(){ String[] xArray = new FileUtil().file2StrArray(wReadPath);
			 * String wMsg = new RTmod().xShellMulti(xArray); //�� �����R�}���h���s
			 * jTa2.setText(wMsg); } }; th1.start(); tCounter = delay; }
			 */
		}
	}
	// -----------------------------------------------------------------------------------
	/** ActionListener�C���^�[�t�F�[�X�̃��\�b�h���` */
	@Override
	public void actionPerformed(ActionEvent evt) {
		// JButton button = (JButton)evt.getSource( );
		// if (button == jBtTimer ){
		// taskStart( );
		// }
	}
	// -----------------------------------------------------------------------------------
	/** �R���X�g���N�^ */
	public PnlMission2() {
		super();
		createGUI(); // GUI���쐬
		new Thread() { // ��ԊĎ��p�X���b�h
			@Override
			public void run() {
				// �����I;
				while (true) {
					try {
						Thread.sleep(1000);// 1�b
						// -----------------------------------------------------
						// ���݂̎���
						Calendar date = Calendar.getInstance(TimeZone
								.getTimeZone("JST"));
						int hour = date.get(Calendar.HOUR_OF_DAY); // ����
						// �擾(24H)
						// int hour = date.get(Calendar.HOUR); // ���� �擾
						// int minute = date.get(Calendar.MINUTE); // �� �擾
						// int second = date.get(Calendar.SECOND); // �b �擾
						// -----------------------------------------------------
						// System.out.println("hour :"+hour);
						// System.out.println("minute:"+minute);
						// System.out.println("second:"+second);
					//  if (jCb_xxx[hour].isSelected()) {
						if(timeTable.getHoge(hour)){
							if (activeSw == true) {
								jLaStat.setText(Integer.toString(tCounter));
								// System.out.println("���͂悤");
								wakuUpSw = true;
								tCounter--;
							} else {
								jLaStat.setText("��~��");
							}
						} else {
							jLaStat.setText("Sleep");
							// System.out.println("���₷��");
							wakuUpSw = false;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
//	// -------------------------------------------------------------------------
//	// ���[���[�\��
//	// -------------------------------------------------------------------------
//	public void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		Ruler.drawRuler(g, this.getSize().width, this.getSize().height,
//				new Color(155, 187, 244));
//	}
}
