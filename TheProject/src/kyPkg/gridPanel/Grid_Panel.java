package kyPkg.gridPanel;

import kyPkg.gridModels.DefaultTableModelMod;
import kyPkg.gridUtil.TModelUtil;
import kyPkg.util.GridUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.awt.*;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

//------------------------------------------------------------------------------
// TablePanel <部品 >
// イベントの取り回しを担当
//------------------------------------------------------------------------------
// ※　ソースデータ設定（張り付ける値）
// ※　ドラッグドロップ
// ※　Ｕｎｄｏ
// ※レコードの追加、削除
// ※一括追加・・・
// ※クリア
// ※アイコンの処理は外部に・・・
// ※コンストラクタ・・・
// 大量データを対象にする場合はどうか・・・
// ＤＢとのコネクションも考えてみる
//------------------------------------------------------------------------------
public class Grid_Panel extends JPanel {
	private static final long serialVersionUID = -6539486059219321035L;
	private int colWidth = 21;//autoFit時の基本カラム幅
	private Grid grid;
	private JScrollPane scrollPane;
 
	protected JLabel labInfo;// 何件見つかりました等の表示に使用

	public void setLabInfo(JLabel labInfo) {
		this.labInfo = labInfo;
	}

	public JLabel getLabInfo() {
		return labInfo;
	}

	private HashMap<Integer, Set<String>> limiter = new HashMap();// 表示行数を限定する

	public HashMap<Integer, Set<String>> getLimiter() {
		return limiter;
	}

	public void extractIt(HashMap<Integer, Set<String>> limiter) {
		// Vector<Vector> dataVector =
		// GridUtil.cnvTModel2Vector(this.getModel());// ???
		this.limiter = limiter;
		int cnt = grid.getModel().getColumnCount();
		Vector colNames = new Vector();
		for (int i = 0; i < cnt; i++) {
			colNames.add(grid.getModel().getColumnName(i));
		}
		int count=TModelUtil.extractIt(grid, limiter, colNames, grid.getDataVector());
		setInfo();
		repaint();
	}

	// --------------------------------------------------------------------------
	// コンストラクタ
	// --------------------------------------------------------------------------
	public Grid_Panel() {
		this(null);
	}

	public Grid_Panel(DefaultTableModelMod tableModel) {
		super(new BorderLayout());
		this.setOpaque(false);
		grid = new Grid(false);
		scrollPane = new JScrollPane(grid);
		this.add("Center", scrollPane);
		if (tableModel != null)
			setDefModel(tableModel);
		showGrid();
	}

	public void showGrid() {
		scrollPane.setViewportView(grid);
	}

	public List<String> getListByCol(int col) {
		return GridUtil.cnvTModel2ListByCol(grid.getModel(), 0);
	}

	// 20120905　yuasa
	public int tModel2File(String path) {
		return TModelUtil.tModel2File(path, grid.getModel(), "\t", null);
	}

	public int saveAsFile(String path, int col) {
		List colList = new ArrayList();
		colList.add(col);
		return TModelUtil.tModel2File(path, grid.getModel(), "\t", colList);
	}

	// public List getDataList() {
	// TableModel tableModel = grid.getModel();
	// if (tableModel != null) {
	// if (tableModel instanceof DefaultTableModel)
	// return ((DefaultTableModel) tableModel).getDataVector();xxxx
	// }
	// return null;
	// }

	public Vector<Vector> getDataVector() {
		return grid.getDataVector();
	}

	public String getDataValue(int row, int col) {
		Vector<Vector> matrix = grid.getDataVector();
		Vector vector = matrix.get(row);
		if (vector != null) {
			Object obj = vector.get(col);
			return String.valueOf(obj);
		}
		return "";
	}

	public void removeTableMouseListener(MouseListener mouseListener) {
		grid.getTableHeader().removeMouseListener(mouseListener);
		grid.removeMouseListener(mouseListener);
	}

	public void setTableMouseListener(MouseListener mouseListener) {
		if (mouseListener == null)
			return;
		createColorMap();
		grid.getTableHeader().addMouseListener(mouseListener);
		grid.addMouseListener(mouseListener);
	}

	public ColorMapCtrlMod createColorMap() {
		return grid.createColorMap(10);// ok
	}

	public void autoFit() {

		grid.autoFit(colWidth, getDataVector());
	}

	// --------------------------------------------------------------------------
	// ユーザが列ヘッダをドラッグして列の順序を変えられるかどうかを設定します。
	// --------------------------------------------------------------------------
	protected void setReorderingAllowed(boolean flag) {
		JTableHeader jTHead = grid.getTableHeader();
		jTHead.setReorderingAllowed(flag); // false:列の順序を変えられない
	}

	// --------------------------------------------------------------------------
	// ラッパー
	// --------------------------------------------------------------------------
	// テーブルのサイズ変更時にテーブルの自動サイズ変更モードを設定します。
	// --------------------------------------------------------------------------
	protected void setAutoResizeMode() {
		grid.setAutoResizeMode(Grid.AUTO_RESIZE_OFF);
	}

	// tableHeader を返します。
	public JTableHeader getTableHeader() {
		return grid.getTableHeader();
	}

	// --------------------------------------------------------------------------
	// 選択解除
	// --------------------------------------------------------------------------
	// public void clearSelection() {
	// grid.clearSelection();// アクティブ範囲を解除（その部分のリペイントもされるが・・）
	// grid.repaint();// 確実にリペイントする（すっきりする）
	// }

	// --------------------------------------------------------------------------
	// 描画
	// --------------------------------------------------------------------------
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	// --------------------------------------------------------------------------
	// リソース（アイコン、画像など）を読み込みます
	// --------------------------------------------------------------------------
	ImageIcon createImageIcon(String path, String description) {
		ClassLoader ld = this.getClass().getClassLoader();
		java.net.URL imgURL = ld.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		}
		System.err.println("Couldn't find file: " + path);
		return null;
	}

	// ほかのコンポーネントを表示する
	public void setCmponent(Component cmp) {
		scrollPane.setViewportView(cmp);
	}

	public void enableIt(boolean flag) {
	}

	public void setInfo() {
		if (labInfo == null)
			return;
//		int dataCount = grid.getRowDataCount();
		int dataCount = grid.getRowCount();
		System.out.println("################### setInfo() #################################"+dataCount);
		if (dataCount == 0) {
			labInfo.setText("Data Not Found!               ");
			labInfo.setForeground(Color.RED);
		} else {
			labInfo.setText("RowCount : " + dataCount + "               ");
			labInfo.setForeground(Color.BLUE);
		}
	}

	// wrapper
	public void fixFit(int width) {
		grid.fixFit(width);
	}

	// wrapper
	public int getColumnCount() {
		return grid.getColumnCount();
	}

	// wrapper
	public void resetColWidth(String colWidth) {
		grid.resetColWidth(colWidth);
	}

	// wrapper
	@Override
	public void removeAll() {
		grid.removeAll();
	}

	public Grid getGrid() {
		return grid;
	}

	public TableModel getTableModel() {
		return grid.getModel();
	}

	public void setDefModel(DefaultTableModelMod tableModel) {
		grid.setDefModel(tableModel);
	}

}
