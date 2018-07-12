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
	public final static int ONE_SECOND = 1000; // 画面を再描画する間隔
	// -------------------------------------------------------------------------
	// Local変数
	// -------------------------------------------------------------------------
	private PnlTimeTable timeTable;
	private PnlSlider pSliderMin;
	private PnlSlider pSliderSec;
	private JButton jBtTimer; // タイマーボタン
	private JLabel jLaStat;
 	private JTextArea txtInfo;
 	private boolean activeSw = false;
	private boolean wakuUpSw = false;
	private int delay; // タイマーのインターバル milliseconds指定なのだ
	private javax.swing.Timer scTimer; // スケジューラーのタイマー
	private int tCounter;
	//private JCheckBox jCb_xxx[];
	// -------------------------------------------------------------------------
	// 《ＧＵＩ関連》
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
		jBtTimer = new JButton("実行");		jBtTimer.setPreferredSize(new Dimension( 100, 20));

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
		// タイムテーブル
		// ---------------------------------------------------------------------
		timeTable = new PnlTimeTable();
//		timeTable.setPreferredSize(new Dimension(100, 300));
		timeTable.setBounds(400, 0, 200, 100);
		pnlMission.add(timeTable);

		// ---------------------------------------------------------------------
		// 時計パネル 飾り
		// ---------------------------------------------------------------------
		JP_Clock objClock = new JP_Clock();
		Color xColor = new Color(128, 150, 161);
		objClock.setBackColor(xColor);
		objClock.initGui(100, 100, this, null);
//		objClock.initGui(100, 100,  null);
		objClock.resizeMe(100, 100); // これをしないとバグる！！
		objClock.setBounds(0, 0, 100, 100);
		pnlMission.add(objClock);

		// ---------------------------------------------------------------------
		// スライダーパネル Minute
		// ---------------------------------------------------------------------
		pSliderMin =new PnlSlider("Min", 0,120,0,40,20);	pSliderMin.setBounds(100,  0, 250, 50);		
		pSliderSec =new PnlSlider("Sec", 0,59,30,10,5);		pSliderSec.setBounds(100, 50, 250, 50);		
		pnlMission.add(pSliderMin);
		pnlMission.add(pSliderSec);

		// ---------------------------------------------------------------------
		// ※タイマーボタン
		// ---------------------------------------------------------------------
		jBtTimer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (activeSw == true) {
					jBtTimer.setText("実行");
					activeSw = false;
					scTimer.stop();
					jLaStat.setForeground(Color.BLACK);
					jLaStat.setBackground(Color.WHITE);
					pSliderMin.setEnabled(true);
					pSliderSec.setEnabled(true);
				} else {
					pSliderMin.setEnabled(false);
					pSliderSec.setEnabled(false);
					jBtTimer.setText("停止");
					activeSw = true;
					// ---------------------------------------------------------
					// スライダーより取得する
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
	// タイマーで実行させるアクション
	// -----------------------------------------------------------------------------------
	void timerAction() {
		txtInfo.setText("");
		if (wakuUpSw == true) {
			txtInfo.setText("実行中");
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
			 * String wMsg = new RTmod().xShellMulti(xArray); //■ 複数コマンド実行
			 * jTa2.setText(wMsg); } }; th1.start(); tCounter = delay; }
			 */
		}
	}
	// -----------------------------------------------------------------------------------
	/** ActionListenerインターフェースのメソッドを定義 */
	@Override
	public void actionPerformed(ActionEvent evt) {
		// JButton button = (JButton)evt.getSource( );
		// if (button == jBtTimer ){
		// taskStart( );
		// }
	}
	// -----------------------------------------------------------------------------------
	/** コンストラクタ */
	public PnlMission2() {
		super();
		createGUI(); // GUI部作成
		new Thread() { // 状態監視用スレッド
			@Override
			public void run() {
				// 処理！;
				while (true) {
					try {
						Thread.sleep(1000);// 1秒
						// -----------------------------------------------------
						// 現在の時刻
						Calendar date = Calendar.getInstance(TimeZone
								.getTimeZone("JST"));
						int hour = date.get(Calendar.HOUR_OF_DAY); // 時間
						// 取得(24H)
						// int hour = date.get(Calendar.HOUR); // 時間 取得
						// int minute = date.get(Calendar.MINUTE); // 分 取得
						// int second = date.get(Calendar.SECOND); // 秒 取得
						// -----------------------------------------------------
						// System.out.println("hour :"+hour);
						// System.out.println("minute:"+minute);
						// System.out.println("second:"+second);
					//  if (jCb_xxx[hour].isSelected()) {
						if(timeTable.getHoge(hour)){
							if (activeSw == true) {
								jLaStat.setText(Integer.toString(tCounter));
								// System.out.println("おはよう");
								wakuUpSw = true;
								tCounter--;
							} else {
								jLaStat.setText("停止中");
							}
						} else {
							jLaStat.setText("Sleep");
							// System.out.println("おやすみ");
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
//	// ルーラー表示
//	// -------------------------------------------------------------------------
//	public void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		Ruler.drawRuler(g, this.getSize().width, this.getSize().height,
//				new Color(155, 187, 244));
//	}
}
