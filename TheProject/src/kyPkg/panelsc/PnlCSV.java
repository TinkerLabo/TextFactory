package kyPkg.panelsc;

import java.io.*;
import java.util.HashMap;

import kyPkg.uFile.FileUtil;
import kyPkg.util.*;
import kyPkg.etc.Cnv_Kanji;
import kyPkg.panelMini.PnlFile;
import kyPkg.task.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

// ============================================================================
/**
 * �s�v���O���X�o�[�̗��p���`�t ���ȉ��̓�̃N���X�Ɉˑ����Ă��܂� kyPkg.util.TskXXX kyPkg.util.SwingWorker
 */
public class PnlCSV extends BorderPanel implements ActionListener {
	private static final long serialVersionUID = 6067181851105602663L;
	private Inf_ProgressTask task; // ���ۂɏ������s���N���X

	// -------------------------------------------------------------------------
	// Local�ϐ�
	// -------------------------------------------------------------------------
	private PnlFile pnlFile1;

	private PnlFile pnlFile2;

	private JButton jBtStart; // ���s�{�^��

	private JButton jBtCsvCnv; // Csv�ϊ�

	private JButton V_Btn1;

	private JButton R_Btn1;

	private JButton C_Btn2;

	private JButton X_Btn2;

	private JComboBox jCmbDlmO; // ��؂蕶���R���{

	private JCheckBox jCBxHeader;

	private JTextArea jTa1;

	private JTextArea jTa2;

	// -------------------------------------------------------------------------
	// ���������ύX���遡
	// -------------------------------------------------------------------------
	/** �s�f�t�h�֘A�t �����Ə������������C������I */
	void createGUI() {

		HashMap pmap1 = new HashMap();
		pmap1.put(PnlFile.CAPTION, "Read Parm");
		pmap1.put(PnlFile.OPT49ER, "false");
		pmap1.put(PnlFile.ENCODE, "false");
		pmap1.put(PnlFile.DELIM, "true");
		pmap1.put(PnlFile.DEFAULT_PATH, "");
		pmap1.put(PnlFile.CAPTION_WIDTH, "-1");
		pnlFile1 = new PnlFile(pmap1);

		HashMap pmap2 = new HashMap();
		pmap2.put(PnlFile.CAPTION, "Read Data");
		pmap2.put(PnlFile.OPT49ER, "false");
		pmap2.put(PnlFile.ENCODE, "false");
		pmap2.put(PnlFile.DELIM, "true");
		pmap2.put(PnlFile.DEFAULT_PATH, "");
		pmap2.put(PnlFile.CAPTION_WIDTH, "-1");
		pnlFile2 = new PnlFile(pmap2);

		//		pnlFile1 = new PnlFile("Read Parm", false, false, true, "", -1);
		//		pnlFile2 = new PnlFile("Read Data", false, false, true, "", -1);
		this.pnlN.add(pnlFile1, BorderLayout.NORTH);
		this.pnlN.add(pnlFile2, BorderLayout.NORTH);
		// ---------------------------------------------------------------------
		// ACTION
		// ---------------------------------------------------------------------
		pnlFile1.setActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread th1 = new Thread() {
					@Override
					public void run() {
						String wPath = pnlFile1.getPath();
						jTa1.setText(FileUtil.file2String(wPath));
					}
				};
				th1.start();
			}
		});
		// ---------------------------------------------------------------------
		// ACTION
		// ---------------------------------------------------------------------
		pnlFile2.setActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread th1 = new Thread() {
					@Override
					public void run() {
						String wPath = pnlFile1.getPath();
						jTa2.setText(FileUtil.file2String(wPath));
					}
				};
				th1.start();
			}
		});
		this.setPreferredSize(new Dimension(600, 430));
		// ---------------------------------------------------------------------
		JButton jBtWrite = new JButton("Write It");
		jBtWrite.setBounds(0, 400, 100, 20);
		// ---------------------------------------------------------------------
		jTa1 = new JTextArea("");
		jTa2 = new JTextArea("");
		JScrollPane jSp1 = new JScrollPane(jTa1);
		jSp1.setBounds(0, 50, 600, 150);
		JScrollPane jSp2 = new JScrollPane(jTa2);
		jSp2.setBounds(0, 260, 600, 140);
		V_Btn1 = new JButton("Paste");
		V_Btn1.setBounds(0, 200, 100, 20);
		R_Btn1 = new JButton("Clear");
		R_Btn1.setBounds(100, 200, 100, 20);
		C_Btn2 = new JButton("Copy");
		C_Btn2.setBounds(100, 400, 100, 20);
		X_Btn2 = new JButton("Cut");
		X_Btn2.setBounds(200, 400, 100, 20);
		jBtStart = new JButton("���s");
		jBtStart.setBounds(400, 400, 100, 20);
		jBtCsvCnv = new JButton("Csv�ϊ�");
		jBtCsvCnv.setBounds(400, 200, 100, 20);
		jCmbDlmO = new JComboBox();
		jCmbDlmO.setBounds(200, 200, 80, 20);
		jCmbDlmO.addItem("Tab");
		jCmbDlmO.addItem("Comma");
		jCmbDlmO.addItem("NOTHING");
		jCmbDlmO.addItem("DEBUG");
		jCmbDlmO.addItem("SPACE");
		jCBxHeader = new JCheckBox("�w�b�_�[����", true);
		jCBxHeader.setBounds(300, 200, 80, 20);
		// ---------------------------------------------------------------------
		BorderPanel pnlXB1 = new BorderPanel();
		BorderPanel pnlXB2 = new BorderPanel();
		pnlXB1.pnlS.setLayout(new FlowLayout());
		pnlXB2.pnlS.setLayout(new FlowLayout());
		pnlXB1.pnlS.add(jBtWrite);
		pnlXB1.pnlC.add(jSp1);
		pnlXB2.pnlC.add(jSp2);
		pnlXB1.pnlS.add(V_Btn1);
		pnlXB1.pnlS.add(R_Btn1);
		pnlXB2.pnlS.add(C_Btn2);
		pnlXB2.pnlS.add(X_Btn2);
		pnlXB1.pnlS.add(jBtStart);
		pnlXB1.pnlS.add(jBtCsvCnv);
		pnlXB1.pnlS.add(jCmbDlmO);
		pnlXB1.pnlS.add(jCBxHeader);
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlXB1,
				pnlXB2);
		splitPane.setDividerLocation(200);
		JPanel basePnl = new JPanel() {
			private static final long serialVersionUID = 6114884479112031169L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Ruler.drawRuler(g, this.getSize().width, this.getSize().height,
						new Color(155, 187, 244));
			}
		};
		basePnl.setLayout(new BorderLayout());
		this.add(basePnl, BorderLayout.CENTER);
		this.setOpaque(true);
		basePnl.add(splitPane);

		// ---------------------------------------------------------------------
		// Write It����
		// ---------------------------------------------------------------------
		jBtWrite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(".");
				if (fc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
					return;
				String wPath = fc.getSelectedFile().toString();

				if (new File(wPath).exists()) {
					int result = JOptionPane.showConfirmDialog((Component) null,
							"���łɓ����̃t�@�C�������݂��܂��A�㏑�����܂����H", "�m�F!",
							JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.NO_OPTION)
						return;
				}

				if (wPath.indexOf(".") == 0) {
					wPath = wPath.trim() + ".txt";
				}
				FileUtil.string2file_(wPath, jTa2.getText());
			}
		});
		// ---------------------------------------------------------------------
		// CSV�ϊ�����
		// ---------------------------------------------------------------------
		jBtCsvCnv.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String wPath1 = pnlFile1.getPath();
				String wPath2 = pnlFile2.getPath();
				if (new File(wPath1).exists() == false) {
					String wErr = "�p�����[�^�t�@�C�������݂��Ă��܂���";
					JOptionPane.showMessageDialog((Component) null, wErr,
							"Message...", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (new File(wPath2).exists() == false) {
					String wErr = "�f�[�^�t�@�C�������݂��Ă��܂���";
					JOptionPane.showMessageDialog((Component) null, wErr,
							"Message...", JOptionPane.ERROR_MESSAGE);
					return;
				}
				String wExt = "";
				String wDlm = "";
				switch (jCmbDlmO.getSelectedIndex()) {
				case 0:
					wExt = ".prn";
					wDlm = "\t";
					break; // Tab
				case 1:
					wExt = ".csv";
					wDlm = ",";
					break; // Comma
				case 2:
					wExt = ".dat";
					wDlm = "";
					break; // NOTHING
				case 3:
					wExt = ".txt";
					wDlm = ",_";
					break; // DEBUG
				case 4:
					wExt = ".txt";
					wDlm = " ";
					break; // SPACE
				}
				// ---------------------------------------------------------
				// �o�̓t�@�C�����ݒ�
				// ---------------------------------------------------------
				String wFileNm = FileUtil.getFirstName2(wPath2) + wExt;
				// ---------------------------------------------------------
				// �ϊ��t�B���^�[�I�u�W�F�N�g�쐬
				// ---------------------------------------------------------
				Cnv_Kanji ftCSV = new Cnv_Kanji(); // import kyPkg.filter.*;
				ftCSV.setParmPath(wPath1);
				ftCSV.setDataPath(wPath2);
				ftCSV.setOutFile(wFileNm);
				if (jCBxHeader.isSelected()) {
					ftCSV.setHeader(true);
					ftCSV.setPattern(true);
				}
				ftCSV.setDelimita(wDlm);
				String wEndMsg = ftCSV.convert();
				JOptionPane.showMessageDialog((Component) null, wEndMsg,
						"Message...", JOptionPane.INFORMATION_MESSAGE);
				ftCSV = null;
			}
		});
		// ---------------------------------------------------------------------
		// �e�L�X�g�G���A�P�ҏW
		// ---------------------------------------------------------------------
		V_Btn1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jTa1.selectAll();
				jTa1.paste();
			}
		});
		R_Btn1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jTa1.selectAll();
				jTa1.setText("");
			}
		});
		// ---------------------------------------------------------------------
		// �e�L�X�g�G���A�Q�ҏW
		// ---------------------------------------------------------------------
		C_Btn2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jTa2.selectAll();
				jTa2.copy();
			}
		});
		X_Btn2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jTa2.selectAll();
				jTa2.cut();
			}
		});
		// ---------------------------------------------------------------------
		// ���s�{�^��
		// ---------------------------------------------------------------------
		jBtStart.addActionListener(this);
	}

	// -------------------------------------------------------------------------
	/** ActionListener�C���^�[�t�F�[�X�̃��\�b�h���` */
	@Override
	public void actionPerformed(ActionEvent evt) {
		JButton button = (JButton) evt.getSource();
		if (button == jBtStart) {
			hitjBtStart();
		}
	}

	// -------------------------------------------------------------------------
	/**
	 * ���s�{�^���������ꂽ�� �v���O���X���j�^�[���Z�b�g�A�b�v�� �������̃X���b�h���J�n
	 */
	private void hitjBtStart() {
		// ---------------------------------------------------------------------
		// ���������� ���s�O�ɏ����Ɋ�Â��ď���������I�I�I
		// ---------------------------------------------------------------------
		String wPath1 = pnlFile1.getPath();
		task = new TskXXX(new String[] { ".", wPath1, "" });
		task.execute(); // ���������N������
	}

	// -------------------------------------------------------------------------
	/** �R���X�g���N�^ */
	public PnlCSV() {
		super();
		createGUI(); // GUI���쐬
	}

	//	// -------------------------------------------------------------------------
	//	/** �v���O���X�o�[�̏�Ԃ��X�V����^�C�}�[�N���X */
	//	class TimerListener implements ActionListener {
	//		public void actionPerformed(ActionEvent evt) {
	//			progressMonitor.setProgress(task.getCurrent());
	//			String s = task.getMessage();
	//			if (s != null) {
	//				progressMonitor.setNote(s);
	//			}
	//			if (progressMonitor.isCanceled() || task.isDone()) {
	//				progressMonitor.close();
	//				task.stop();
	//				Toolkit.getDefaultToolkit().beep();
	//				// if (task.isDone()) { }
	//				if (jBtStart != null)
	//					jBtStart.setEnabled(true);
	//			}
	//		}
	//	}

	// -------------------------------------------------------------------------
	// ���[���[�\��
	// -------------------------------------------------------------------------
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Ruler.drawRuler(g, this.getSize().width, this.getSize().height,
				new Color(155, 187, 244));
	}
}
