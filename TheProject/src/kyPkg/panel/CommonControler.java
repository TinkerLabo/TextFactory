package kyPkg.panel;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CommonControler extends JPanel{
	private static final long serialVersionUID = 1L;
	private JButton btnPaste; // 貼り付け
	private JButton btnClear; // クリア
	private JButton btnCopy; // コピー
	private JButton btnCut; // 切り取り
	
	private JTextArea tArea1; // テキストエリア１
	private JTextArea tArea2; // テキストエリア１

	public String gettArea1() {
		return tArea1.getText();
	}
	public void settArea1(String text) {
		tArea1.setText(text);	
	}
	public String  gettArea2() {
		return tArea2.getText();
	}
	public void settArea2(String text) {
		tArea2.setText(text);	
	}


	public CommonControler(){
		super();
		this.setLayout(null);
		this.setPreferredSize(new Dimension(600, 240));
		createControlers();
	}
	public void createControlers(){
		tArea1 = new JTextArea("");
		tArea2 = new JTextArea("");

		JScrollPane jSp1 = new JScrollPane(tArea1);    jSp1.setBounds( 0,   0, 600, 100);
		btnPaste = new JButton("Paste");	      btnPaste.setBounds(  0, 100, 100, 20);
		btnClear = new JButton("Clear");	      btnClear.setBounds(100, 100, 100, 20);

		JScrollPane jSp2 = new JScrollPane(tArea2);    jSp2.setBounds( 0, 120, 600, 100);
		btnCopy = new JButton("Copy");		      btnCopy.setBounds(   0, 220, 100, 20);
		btnCut = new JButton("Cut");		      btnCut.setBounds(  100, 220, 100, 20);

		this.add(jSp1);
		this.add(jSp2);

		this.add(btnPaste);
		this.add(btnClear);
		this.add(btnCopy);
		this.add(btnCut);

		// ---------------------------------------------------------------------
		// テキストエリア１編集
		// ---------------------------------------------------------------------
		btnPaste.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tArea1.selectAll();
				tArea1.paste();
			}
		});
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tArea1.selectAll();
				tArea1.setText("");
			}
		});
		// ---------------------------------------------------------------------
		// テキストエリア２編集
		// ---------------------------------------------------------------------
		btnCopy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tArea2.selectAll();
				tArea2.copy();
			}
		});
		btnCut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tArea2.selectAll();
				tArea2.cut();
			}
		});

	}
//	// -------------------------------------------------------------------------
//	// ルーラー表示
//	// -------------------------------------------------------------------------
//	public void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		Ruler.drawRuler(g, this.getSize().width, this.getSize().height,
//				new Color(168,120,120));
//	}
}
