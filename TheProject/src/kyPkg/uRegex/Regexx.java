package kyPkg.uRegex;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.regex.*;
/******************************************************************************
*	《 Regexx 》 2007-05-11                        <BR>
*	正規表現関連の汎用プログラムなど
*	@quthor     Ken Yuasa
*	@version    Version 1.0
*	@since      SINCE java1.3
*******************************************************************************/
public class Regexx{
	
	//-------------------------------------------------------------------------
	// 大文字小文字を曖昧に検索したい場合
	// ※毎回パターンがコンパイルされるので注意する
	//《使用例》
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
	// 大文字小文字を曖昧にマッチングしたい場合＃１
	// ※毎回パターンがコンパイルされるので注意する
	//《使用例》
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
	// 大文字小文字を曖昧にマッチングしたい場合＃２
	// 同じパターンを何回も使う場合はこちらがベター
	//《使用例》
	//	Pattern ptn = Regexx.patternIgnoreCase(pRegex);
	//	wFlg = ptn.matcher(pCharSeq).find();
	//-------------------------------------------------------------------------
	public static Pattern patternIgnoreCase(String pRegex){
		return Pattern.compile(pRegex, Pattern.CASE_INSENSITIVE);
	}
	//-------------------------------------------------------------------------
	// main テスト用ＧＵＩ
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
			jCmb1.addItem("[ABC]	A,B,Cのいずれか1文字 ");
			jCmb1.addItem("[A-Z]	A～Zまでのいずれか1文字 ");
			jCmb1.addItem("[A-Za-z0-9]	A～Z, a～z, 0-9までのいずれか1文字");
			jCmb1.addItem("[^ABC]	A,B.C以外の文字 ");
			jCmb1.addItem("[^A-Z]	A～Z以外の文字 ");
			jCmb1.addItem("\\w	英数文字。[a-zA-Z0-9]と同様 ");
			jCmb1.addItem("\\W	\\w以外の文字 ");
			jCmb1.addItem("\\d	数値文字。[0-9]と同等 ");
			jCmb1.addItem("\\D	\\d以外の文字 ");
			jCmb1.addItem("\\s	空白文字 ");
			jCmb1.addItem("\\S	\\s以外の文字");
			jCmb1.addItem("\\n	改行文字 ");

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
				// 標準のstaticメソッドを使った場合
				//-------------------------------------------------------------
				//wFlg = Pattern.matches(pRegex, pCharSeq);
				//-------------------------------------------------------------
				// パターンが固定される場合（パフォーマンスを気にする場合）
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
				// パターンが固定される場合（パフォーマンスを気にする場合）
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
				// パターンが固定される場合（パフォーマンスを気にする場合）
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
				// リテラル内に指定するエスケープシーケンスに加工する
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
				// 文字列を表現するregixに簡易変換する
				// 括弧も問題あるだろうね・・・・
				// 元の文字列はregixでは無いという前提・・
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
