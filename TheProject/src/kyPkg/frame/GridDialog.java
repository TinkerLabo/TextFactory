package kyPkg.frame;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import kyPkg.gridModels.DefaultTableModelMod;
import kyPkg.gridPanel.Grid_Panel;
public class GridDialog extends JDialog {
	private static final long serialVersionUID = 8689974610721637381L;
	private JDialog thisFrame;
	private Container container;
	private Grid_Panel resultGrid;
	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------
	public GridDialog(String title, int width, int height) {
		super((Frame) null, title, true);
		this.thisFrame = this;
		this.container = this.getContentPane();
		setSize(width, height);
		centering();
		container.setLayout(new BorderLayout());
		JButton btnOK = new JButton("Close");
		btnOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				thisFrame.setVisible(false);
			}
		});
		resultGrid = new Grid_Panel();
		container.add(resultGrid, BorderLayout.CENTER);
		container.add(btnOK, BorderLayout.SOUTH);
	}
	// ---------------------------------------------------------------------
	// ウィンドウを中央に配置
	// ---------------------------------------------------------------------
	private void centering() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension thisSize = thisFrame.getSize();
		if (thisSize.height > screenSize.height)
			thisSize.height = screenSize.height;
		if (thisSize.width > screenSize.width)
			thisSize.width = screenSize.width;
		this.setLocation((screenSize.width - thisSize.width) / 2,
				(screenSize.height - thisSize.height) / 2);
	}
	public void showGrid(String path, String[] headArray, String colwidth) {
		List<String> janheadList = Arrays.asList(headArray);
		
		Vector<Vector> matrix = kyPkg.uFile.MatrixUtil.file2VectorMatrixPlus(path, 256);
		
		resultGrid.setDefModel(new DefaultTableModelMod(matrix, janheadList));
		resultGrid.resetColWidth(colwidth);
		thisFrame.setVisible(true);
	}
	// -------------------------------------------------------------------------
	// for Test
	// -------------------------------------------------------------------------
	public static void tester() {
		final JPanel testP = new JPanel(new BorderLayout());
		final JButton btnSrc = new JButton("GO");
		testP.add(btnSrc, BorderLayout.SOUTH);
		btnSrc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				GridDialog gridDialog = new GridDialog("Grid", 600, 480);
				String path = globals.ResControl.getCurDir()+"MapReduce3.txt";
				String[] headArray = "Jan,JanName,code,name,価格,容量".split(",");
				gridDialog.showGrid(path, headArray, "100,300,50,300,80,80");
			}
		});
		System.setErr(System.out);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new BaseFrame(testP, 640, 480);
			}
		});
	}
	public static void main(String[] argv) {
		tester();
	}
}
