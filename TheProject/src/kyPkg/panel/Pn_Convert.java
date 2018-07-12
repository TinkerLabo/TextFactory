package kyPkg.panel;

import kyPkg.panelMini.*;
import kyPkg.panelsc.BorderPanel;
import kyPkg.task.*;
import kyPkg.uFile.ListArrayUtil;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.*;

//-----------------------------------------------------------------------------
/**
 * �s�v���O���X�o�[�̗��p���`�t ���ȉ��̓�̃N���X�Ɉˑ����Ă��܂� kyPkg.util.TskXXX kyPkg.util.SwingWorker
 * 
 * ��ʃf�[�^��ǂݍ��񂾏ꍇ�n���O�A�b�v���鋰�ꂪ���邪�A�\���ł��Ȃ����E�E
 */
public class Pn_Convert extends BorderPanel implements ActionListener {

	private static final String PREFIX = ".*\\.";

	private static final String PARAMETER = "�O���p�����[�^";

	private static final String CONVERT = "�ϊ��������s";

	private static final long serialVersionUID = 8693278004908452341L;

	// -------------------------------------------------------------------------
	// �v���O���X�o�[�̃^�C�g��
	// -------------------------------------------------------------------------
	private String gParmPath;

	private TskXXX task; // ���ۂɏ������s���N���X

	// -------------------------------------------------------------------------
	// Local�ϐ�
	// -------------------------------------------------------------------------
	private PnlFile pnlFile;

	private CnvPanel wCvP[];

	private String wBef[];

	private String wAft[];

	private String wPath;

	private JButton wBtFuji; // �����{�^��

	private JButton wBtKick; // ���s�{�^��

	private JComboBox comboExt; // �Ώۃt�@�C���̊g���q

	private JCheckBox checkSubDir; // �T�u�f�B���N�g���ȉ����������邩�ǂ���

	private JTextArea jTxaMsg; // ���ʃ��b�Z�[�W�\���p�e�L�X�g�G���A

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public Pn_Convert() {
		super();
		wCvP = new CnvPanel[50];
		wBef = new String[50];
		wAft = new String[50];
		createGUI(); // GUI���쐬
	}

	// -------------------------------------------------------------------------
	// createGUI
	// -------------------------------------------------------------------------
	private void createGUI() {

		HashMap pmap = new HashMap();
		pmap.put(PnlFile.CAPTION, "targetPath:");
		pmap.put(PnlFile.OPT49ER, "true");
		pmap.put(PnlFile.ENCODE, "false");
		pmap.put(PnlFile.DELIM, "true");
		pmap.put(PnlFile.DEFAULT_PATH, "");
		pmap.put(PnlFile.CAPTION_WIDTH, "-1");
		pnlFile = new PnlFile(pmap);

		//		pnlFile = new PnlFile("targetPath:", true, false, true, "", -1);
		// ---------------------------------------------------------------------
		// �g���q�R���{�{�b�N�X
		// ---------------------------------------------------------------------
		JLabel wLabel01 = new JLabel("�Ώۊg���q�F");
		wLabel01.setSize(new Dimension(120, 20));
		comboExt = new JComboBox();
		comboExt.setSize(new Dimension(100, 20));
		comboExt.setEditable(true);
		comboExt.addItem(PREFIX + "csv");
		comboExt.addItem(PREFIX + "txt");
		comboExt.addItem(PREFIX + "sql");
		comboExt.addItem(PREFIX + "prn");
		comboExt.addItem(PREFIX + "htm");
		comboExt.addItem(PREFIX + "html");
		comboExt.addItem(PREFIX + "java");
		comboExt.addItem(PREFIX + "bas");
		comboExt.addItem(PREFIX + "frm");
		comboExt.addItem(PREFIX + "c");
		comboExt.addItem(PREFIX + "cpp");
		comboExt.addItem(PREFIX + ".*");
		// ---------------------------------------------------------------------
		// �`�F�b�N�{�b�N�X
		// ---------------------------------------------------------------------
		checkSubDir = new JCheckBox("�T�u�t�H���_����������", true);
		checkSubDir.setSize(new Dimension(200, 20));
		checkSubDir.setOpaque(false);
		wBtKick = new JButton(CONVERT);
		wBtKick.setSize(new Dimension(200, 20));
		wBtFuji = new JButton(PARAMETER);
		wBtFuji.setSize(new Dimension(200, 20));
		// ---------------------------------------------------------------------
		// �ϊ����� �a���������� �� �`��������
		// ---------------------------------------------------------------------
		JPanel innerPanel = new JPanel();
		for (int i = 0; i < wCvP.length; i++) {
			wCvP[i] = new CnvPanel();
			wCvP[i].setBounds(10, (20 * i), 690, 20);
			innerPanel.add(wCvP[i]);
		}
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(innerPanel);
		// ---------------------------------------------------------------------
		// ���b�Z�[�W�G���A
		// ---------------------------------------------------------------------
		jTxaMsg = new JTextArea();
		jTxaMsg.setSize(new Dimension(100, 20));
		pnlN.add(pnlFile);
		JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelBtn.add(wLabel01);
		panelBtn.add(comboExt);
		panelBtn.add(checkSubDir);
		panelBtn.add(wBtKick);
		panelBtn.add(wBtFuji);
		pnlN.add(panelBtn);
		pnlC.add(scrollPane);
		pnlS.add(jTxaMsg);
		// ---------------------------------------------------------------------
		// ���s�{�^��
		// ---------------------------------------------------------------------
		wBtKick.addActionListener(this);
		// ---------------------------------------------------------------------
		// �����{�^��
		// ---------------------------------------------------------------------
		wBtFuji.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String curDir = globals.ResControl.getCurDir();
				readCnvParam(curDir);
			}
		});
	}

	// -------------------------------------------------------------------------
	// �R���o�[�g�\���t�@�C������ǂݍ��ށi������Łj
	// -------------------------------------------------------------------------
	private void readCnvParam(String pParm) {
		JFileChooser fc = new JFileChooser(pParm);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			gParmPath = fc.getSelectedFile().toString();
		}
		System.out.println("fujiipath:" + gParmPath);
		Thread th1 = new Thread() {
			@Override
			public void run() {
				int k = 0;
				String[] array = ListArrayUtil.file2Array(gParmPath);
				for (int i = 0; i < array.length; i++) {
					String[] zArray = array[i].split("\t");
					if (zArray.length >= 2) {
						wCvP[k].setBef(zArray[0]);
						wCvP[k].setAft(zArray[1]);
						k++;
					}
				}
			}
		};
		th1.start();
	};

	// -------------------------------------------------------------------------
	// ���s�{�^���̏���
	// �v���O���X���j�^�[���Z�b�g�A�b�v�� �������̃X���b�h���J�n
	// -------------------------------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent evt) {
		// �����������[�`���̏��������� ���s�O�ɏ����Ɋ�Â��ď���������I�I�I
		// �����ȉ���ύX����i�����ɂ������͈قȂ�j
		// ---------------------------------------------------------------------
		jTxaMsg.setText("");
		boolean wFlg = checkSubDir.isSelected(); // �T�u�f�B���N�g���ȉ����������邩�ǂ���
		String wRgx = (String) (comboExt.getSelectedItem());
		// wRgx = ".*\\."+wRgx;

		for (int k = 0; k < wCvP.length; k++) {
			wBef[k] = wCvP[k].getBef();
			wAft[k] = wCvP[k].getAft();
		}
		wPath = pnlFile.getPath(); // �����Ώۃp�X�A
		// �����Ώۃp�X�A��������A�ϊ��敶����A�Ώۃt�@�C���^�C�v�A�T�u�f�B���N�g������
		task = new TskXXX(wPath, wBef, wAft, wRgx, wFlg); // ���������ύX����

		task.initProgressKit(Pn_Convert.this, wBtKick, "�������E�E�E", 100);
		task.execute(); // ���������N������
	}

	public static void main(String[] argv) {
		standAlone();
	}

	public static void standAlone() {
		JFrame frame = new JFrame();
		frame.setBounds(50, 50, 800, 350);
		frame.getContentPane().add(new Pn_Convert());
		frame.setVisible(true);
	}

}
