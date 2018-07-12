package kyPkg.panelMini;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class PnlFont extends JPanel implements ActionListener{
	private static final long serialVersionUID = 495164841086015812L;
	private Font[]	fam;
	private JComboBox  jCbFontName ;
	private JComboBox  jCbFontStyle;
	private JComboBox  jCbFontSize ;
//	private JLabel     jTfFontInfo ;
	//-----------------------------------------------------------------------
	//　アクセッサ
	//-----------------------------------------------------------------------
	public String getFontName() {
		if (jCbFontName!=null){
			return (String)jCbFontName.getSelectedItem();
		}
		return "";
	}
	public int getFontSize() {
		if (jCbFontSize!=null){
			return Integer.parseInt((String)jCbFontSize.getSelectedItem());
		}
		return -1;
	}
	public int getFontStyle() {
		if (jCbFontStyle!=null){
			String wStyle = (String)jCbFontStyle.getSelectedItem();
			int iStyle;
			if     	(wStyle.equals("PLAIN")) 	iStyle = Font.PLAIN;
			else if	(wStyle.equals("BOLD") )	iStyle = Font.BOLD;
			else if	(wStyle.equals("ITALIC")) 	iStyle = Font.ITALIC;
			else								iStyle = Font.BOLD + Font.ITALIC;
			return iStyle;
		}
		return -1;
	}
	public String getCurrentInfo(){
		String name = getFontName();
		int style = getFontStyle();
		int size  = getFontSize();
		String wStyle="";
		if     	(style == Font.PLAIN)  wStyle = "Plain";
		else if	(style == Font.BOLD)   wStyle = "Bold";
		else if	(style == Font.ITALIC) wStyle = "Italic";
		else	                       wStyle = "BoldItalic";
		String wInfo = "【" + name + "】"+ " Style:" + wStyle + " Size:"+size ;
		return wInfo;
	}
	public Font getCurrentFont(){
		String name = getFontName();
		int style = getFontStyle();
		int size = getFontSize();
		if(!name.equals("") && style!=-1 && size!=-1){
			return new Font(name,style,size);
		}
		return null;
	}
//	public String getJTfFontInfo() {
//		return jTfFontInfo.getText();
//	}
	//-----------------------------------------------------------------------
	//　コンストラクタ
	//-----------------------------------------------------------------------
	public PnlFont(){
		super();
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		//---------------------------------------------------------------------
		// 以下フォント設定用コンポーネント
		//---------------------------------------------------------------------
		String sStyle[] = {"PLAIN","BOLD","ITALIC","BOLD + ITALIC"};
		String sPoint[] = new String[40];
		for (int i=0;i<40;i++){
			sPoint[i] = Integer.toString((i+1) * 2);
		}
		jCbFontName  = new JComboBox();		    jCbFontName.setSize(new Dimension(300,20));
		jCbFontStyle = new JComboBox(sStyle);	jCbFontStyle.setSize(new Dimension(100,20));
		jCbFontSize  = new JComboBox(sPoint);	jCbFontSize.setSize(new Dimension(100,20));
//		jTfFontInfo  = new JLabel();		    jTfFontInfo.setSize(new Dimension(200,20));
		this.setSize(600,200);
		this.add(jCbFontName);
		this.add(jCbFontStyle);
		this.add(jCbFontSize);
//		this.add(jTfFontInfo);
		//---------------------------------------------------------------------
		// Font Name Incore!!
		//---------------------------------------------------------------------
		try{
			fam = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
			//fnm = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
			//System.out.println("fnm.length:"+ fnm.length);
			//for (int i = 0; i < fnm.length  ;i++){
			//	//System.out.println("fnm["+i+"]:"+ fnm[i]);
			//	jCbFont.addItem(fnm[i]);
			//}
			//System.out.println("fam.length:"+ fam.length);
			for (int i = 0; i < fam.length  ;i++){
				jCbFontName.addItem(fam[i].getName());
				//System.out.println(fam[i].getName());
				//論理フォント名取得時はgetName、フェース名はgetFontNameを使用
			}
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
		jCbFontName.addActionListener(this);
		jCbFontStyle.addActionListener(this);
		jCbFontSize.addActionListener(this);
		//jCbFontName.setSelectedItem("ＭＳ ゴシック");
		jCbFontName.setSelectedItem("MS Gothic");
		jCbFontStyle.setSelectedIndex(0); //"PLAIN"
		jCbFontSize.setSelectedItem("16");
	}
	//---------------------------------------------------------------------
	// 変更
	//---------------------------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent e) {
//		jTfFontInfo.setText("");
//		jTfFontInfo.setText(getCurrentInfo());
	}
}