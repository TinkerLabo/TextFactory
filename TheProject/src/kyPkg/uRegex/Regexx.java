package kyPkg.uRegex;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.regex.*;
/******************************************************************************
*	�s Regexx �t 2007-05-11                        <BR>
*	���K�\���֘A�̔ėp�v���O�����Ȃ�
*	@quthor     Ken Yuasa
*	@version    Version 1.0
*	@since      SINCE java1.3
*******************************************************************************/
public class Regexx{
	
	//-------------------------------------------------------------------------
	// �啶����������B���Ɍ����������ꍇ
	// ������p�^�[�����R���p�C�������̂Œ��ӂ���
	//�s�g�p��t
	//	wFlg = Regexx.findIgnoreCase(pRegex, pCharSeq);
	//-------------------------------------------------------------------------
	public static boolean findIgnoreCase(String pRegex,String pCharSeq){
		Pattern pattern = Pattern.compile(pRegex, Pattern.CASE_INSENSITIVE);
	//	Matcher matcher = pattern.matcher(pCharSeq);
	//	boolean wFlg = matcher.find();
		boolean wFlg = pattern.matcher(pCharSeq).find();
		return wFlg;
	}
	//-------------------------------------------------------------------------
	// �啶����������B���Ƀ}�b�`���O�������ꍇ���P
	// ������p�^�[�����R���p�C�������̂Œ��ӂ���
	//�s�g�p��t
	//	wFlg = Regexx.matchesIgnoreCase(pRegex, pCharSeq);
	//-------------------------------------------------------------------------
	public static boolean matchesIgnoreCase(String pRegex,String pCharSeq){
		Pattern pattern = Pattern.compile(pRegex, Pattern.CASE_INSENSITIVE);
	//	Matcher matcher = pattern.matcher(pCharSeq);
	//	boolean wFlg = matcher.find();
		boolean wFlg = pattern.matcher(pCharSeq).matches();
		return wFlg;
	}
	//-------------------------------------------------------------------------
	// �啶����������B���Ƀ}�b�`���O�������ꍇ���Q
	// �����p�^�[����������g���ꍇ�͂����炪�x�^�[
	//�s�g�p��t
	//	Pattern ptn = Regexx.patternIgnoreCase(pRegex);
	//	wFlg = ptn.matcher(pCharSeq).find();
	//-------------------------------------------------------------------------
	public static Pattern patternIgnoreCase(String pRegex){
		return Pattern.compile(pRegex, Pattern.CASE_INSENSITIVE);
	}
	//-------------------------------------------------------------------------
	// main �e�X�g�p�f�t�h
	//-------------------------------------------------------------------------
	public static void main(String[] argv){
		JFrame fr = new JFrame("Regexx");
		fr.getContentPane().setLayout(null);
		fr.setBackground(java.awt.Color.white);

		final JTextField jTf1 = new JTextField("");
		jTf1.setBounds(10,10,200,20); fr.getContentPane().add(jTf1);

		final JTextField jTf2 = new JTextField("");
		jTf2.setBounds(10,30,500,20); fr.getContentPane().add(jTf2);

		//jTf1.setText(".*\\\\.*\\.htm"); // debug
		jTf1.setText(".*static\\s*void\\s*main.*"); // debug
		jTf2.setText("	public static void main(String[] argv){"); // debug

		final JLabel JLab1 = new JLabel("");
		JLab1.setBounds(10,50,500,20); fr.getContentPane().add(JLab1);
		JLab1.setForeground(Color.WHITE);
		JLab1.setBackground(Color.BLUE);
		JLab1.setOpaque(true);

		JButton jBt1 = new JButton("match?");
		jBt1.setBounds( 10,70,100,20); fr.getContentPane().add(jBt1);

		JButton jBt2 = new JButton("Replace");
		jBt2.setBounds(110,70,100,20); fr.getContentPane().add(jBt2);

		JButton jBt3 = new JButton("Find");
		jBt3.setBounds(210,70,100,20); fr.getContentPane().add(jBt3);

		JButton jBt4 = new JButton("EscapeSeq");
		jBt4.setBounds(310,70,100,20); fr.getContentPane().add(jBt4);

		JButton jBt5 = new JButton("MetaCharacter");
		jBt5.setBounds(410,70,100,20); fr.getContentPane().add(jBt5);

		final JTextField jTf3 = new JTextField("");
		jTf3.setBounds(10,90,500,20); fr.getContentPane().add(jTf3);

		final JComboBox jCmb1 = new JComboBox();
			jCmb1.setBounds(210,10,300,20); fr.getContentPane().add(jCmb1);
			jCmb1.addItem("[ABC]	A,B,C�̂����ꂩ1���� ");
			jCmb1.addItem("[A-Z]	A�`Z�܂ł̂����ꂩ1���� ");
			jCmb1.addItem("[A-Za-z0-9]	A�`Z, a�`z, 0-9�܂ł̂����ꂩ1����");
			jCmb1.addItem("[^ABC]	A,B.C�ȊO�̕��� ");
			jCmb1.addItem("[^A-Z]	A�`Z�ȊO�̕��� ");
			jCmb1.addItem("\\w	�p�������B[a-zA-Z0-9]�Ɠ��l ");
			jCmb1.addItem("\\W	\\w�ȊO�̕��� ");
			jCmb1.addItem("\\d	���l�����B[0-9]�Ɠ��� ");
			jCmb1.addItem("\\D	\\d�ȊO�̕��� ");
			jCmb1.addItem("\\s	�󔒕��� ");
			jCmb1.addItem("\\S	\\s�ȊO�̕���");
			jCmb1.addItem("\\n	���s���� ");

		jCmb1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				String wStr = jCmb1.getSelectedItem().toString();
				String[] arr = wStr.split("\\t");
				if (arr!= null) jTf1.setText(jTf1.getText()+arr[0]);
			}
		});
		jBt1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				JLab1.setText("");
				String pRegex   = jTf1.getText();
				String pCharSeq = jTf2.getText();
				boolean wFlg;
				//-------------------------------------------------------------
				// �W����static���\�b�h���g�����ꍇ
				//-------------------------------------------------------------
				//wFlg = Pattern.matches(pRegex, pCharSeq);
				//-------------------------------------------------------------
				// �p�^�[�����Œ肳���ꍇ�i�p�t�H�[�}���X���C�ɂ���ꍇ�j
				//-------------------------------------------------------------
				Pattern ptn = Regexx.patternIgnoreCase(pRegex);
				wFlg = ptn.matcher(pCharSeq).matches();
				JLab1.setText("matches => " + wFlg);
			}
		});
		jBt2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				JLab1.setText("");
				String pRegex   = jTf1.getText();
				String pCharSeq = jTf2.getText();
				String pReplacement = "@";
				//boolean wFlg;
				//-------------------------------------------------------------
				// �p�^�[�����Œ肳���ꍇ�i�p�t�H�[�}���X���C�ɂ���ꍇ�j
				//-------------------------------------------------------------
				Pattern ptn = Regexx.patternIgnoreCase(pRegex);
				jTf3.setText(ptn.matcher(pCharSeq).replaceAll(pReplacement));
			}
		});
		jBt3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				JLab1.setText("");
				String pRegex   = jTf1.getText();
				String pCharSeq = jTf2.getText();
				boolean wFlg;
				//-------------------------------------------------------------
				// �p�^�[�����Œ肳���ꍇ�i�p�t�H�[�}���X���C�ɂ���ꍇ�j
				//-------------------------------------------------------------
			//	Pattern ptn = Regexx.patternIgnoreCase(pRegex);
			//	wFlg = ptn.matcher(pCharSeq).find();
				wFlg = Regexx.findIgnoreCase(pRegex, pCharSeq);
				JLab1.setText("find! => " + wFlg);
			}
		});
		jBt4.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				JLab1.setText("");
				String pRegex   = jTf1.getText();
				//-------------------------------------------------------------
				// ���e�������Ɏw�肷��G�X�P�[�v�V�[�P���X�ɉ��H����
				//-------------------------------------------------------------
				pRegex = pRegex.replaceAll("\\\\","\\\\\\\\");
				pRegex = pRegex.replaceAll("\"","\\\\\"");
				pRegex = pRegex.replaceAll("\'","\\\\\'");
				pRegex = pRegex.replaceAll("\t","\\\\\t'");
				jTf3.setText(pRegex);
			}
		});
		jBt5.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				JLab1.setText("");
				String pRegex   = jTf2.getText();
				//-------------------------------------------------------------
				// �������\������regix�ɊȈՕϊ�����
				// ���ʂ���肠�邾�낤�ˁE�E�E�E
				// ���̕������regix�ł͖����Ƃ����O��E�E
				//-------------------------------------------------------------
				pRegex = pRegex.replaceAll("\\\\","\\\\");
				pRegex = pRegex.replaceAll("\"","\\\\\"");
				pRegex = pRegex.replaceAll("\'","\\\\\'");
				pRegex = pRegex.replaceAll("\t","\\\\t");
				pRegex = pRegex.replaceAll("\\.","\\\\.");
				pRegex = pRegex.replaceAll("\\s","\\\\s");
				pRegex = pRegex.replaceAll("\\*",".*");
				jTf3.setText(pRegex);
			}
		});

		fr.pack();
		fr.setSize(520,150);
		fr.setVisible(true);
	}
}
//JOptionPane jop = new JOptionPane();
//jop.showMessageDialog((Component)null,"matches:" + Pattern.matches(pRegex, pCharSeq));
//jop.showMessageDialog((Component)null,"matches:" + pCharSeq.matches(pRegex));
