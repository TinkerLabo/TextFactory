package kyPkg.panel;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.*;
//import kyPkg.filter.Filters.ParmsObj;
import kyPkg.panelMini.*;
import kyPkg.panelsc.BorderPanel;

public class Pn_QBRconv extends BorderPanel {
	private static final long serialVersionUID = -5613170556136966301L;

	// -------------------------------------------------------------------------
	// XXX ���̃N���X�͂��͂�s�v�����m��Ȃ��E�E�E2008/10/22
	// -------------------------------------------------------------------------
	private PnlFile pnlFile;

	private JButton jBtnTest01;

	private JButton jBtnTest02;

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public Pn_QBRconv() {
		super();
		this.setOpaque(false);
		createGUI();
	}

	// -------------------------------------------------------------------------
	// GUI���쐬 ���i�Ԃ̘A�g���Ƃ��Ă����Ȃ���΂Ȃ�܂�
	// -------------------------------------------------------------------------
	void createGUI() {
		JPanel jPnl = new JPanel(new FlowLayout());
		jBtnTest01 = new JButton("�T�u�X�g�����O");
		jBtnTest01.setPreferredSize(new Dimension(150, 20));
		jBtnTest02 = new JButton("�u���[�N����  ");
		jBtnTest02.setPreferredSize(new Dimension(150, 20));
		jPnl.add(jBtnTest01);
		jPnl.add(jBtnTest02);
		
		HashMap pmap1 = new HashMap();
		pmap1.put(PnlFile.CAPTION, " ...");
		pmap1.put(PnlFile.OPT49ER, "false");
		pmap1.put(PnlFile.ENCODE, "false");
		pmap1.put(PnlFile.DELIM, "true");
		pmap1.put(PnlFile.DEFAULT_PATH, "");
		pmap1.put(PnlFile.CAPTION_WIDTH, "-1");
		pnlFile = new PnlFile(pmap1);

		
//		pnlFile = new PnlFile();
		pnlN.add(pnlFile);
		pnlS.add(jPnl);
		// ---------------------------------------------------------------------
		// test01�{�^��
		// ---------------------------------------------------------------------
		jBtnTest01.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				Old_SUBSTR_original ins = new Old_SUBSTR_original();
//				if (ins != null)
//					ins.fltSubstringProt(pnlFile.getPath(), new Vector());
			}
		});
		// ---------------------------------------------------------------------
		// test02�{�^��
		// ---------------------------------------------------------------------
		jBtnTest02.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				Old_SUBSTR_original ins = new Old_SUBSTR_original();
//				if (ins != null)
//					ins.fileBreaker(pnlFile.getPath());
			}
		});
	}
}
