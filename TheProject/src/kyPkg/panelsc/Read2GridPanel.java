package kyPkg.panelsc;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import kyPkg.gridModels.DefaultTableModelMod;
import kyPkg.gridPanel.TableMouseListener;
import kyPkg.gridPanel.Grid_Panel;
import kyPkg.panelMini.PnlFile;
import kyPkg.uFile.File2Matrix;

public class Read2GridPanel extends BorderPanel {
	private int col = -1;
	private String path = "";

	public String getPath() {
		return path;
	}

	public void setDefaultPath(String path) {
		pnlFile1.setText(path);
	}

	private void setPath(String path) {
		this.path = path;
	}

	public int getCol() {
		return col;
	}

	public int getRow() {
		return row;
	}

	public String getValue() {
		return value;
	}

//	private void setValue(String value) {
//		this.value = value;// xxx
//	}

	private void setCol(int col) {
		this.col = col;
	}

	private void setRow(int row) {
		this.row = row;
	}

	private int row = -1;
	private String value = "";
	private static final long serialVersionUID = 1L;
	private int depth = 100; // 検査深度
	private PnlFile pnlFile1;
	private Grid_Panel gridPanel1;
	private JTextField texRow;
	private JTextField texCol;
	private JTextField texVal;

	public Read2GridPanel() {
		super();
		
		HashMap pmap1 = new HashMap();
		pmap1.put(PnlFile.CAPTION, "Read Data");
		pmap1.put(PnlFile.OPT49ER, "false");
		pmap1.put(PnlFile.ENCODE, "false");
		pmap1.put(PnlFile.DELIM, "true");
		pmap1.put(PnlFile.DEFAULT_PATH, "");
		pmap1.put(PnlFile.CAPTION_WIDTH, "-1");
		pnlFile1 = new PnlFile(pmap1);
//		pnlFile1 = new PnlFile("Read Data", false, false, true, "", -1);
		// ---------------------------------------------------------------------
		// ACTION
		// ---------------------------------------------------------------------
		pnlFile1.setActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread th1 = new Thread() {
					@Override
					public void run() {
						String path = pnlFile1.getPath();
						String delimiter = pnlFile1.getDelimiter();
						int limit = depth;
						Vector wVec = File2Matrix.extract(path, delimiter,
								limit);
						setPath(path);
						if (wVec != null) {
							DefaultTableModelMod tModel = new DefaultTableModelMod(
									wVec);
							tModel.setUseDefaultName(false);
							gridPanel1.setDefModel(tModel);
							System.out.println("## ACTION発生！？path:" + path);
							gridPanel1.autoFit();
						}
					}
				};
				th1.start();
			}
		});
		gridPanel1 = new Grid_Panel(new DefaultTableModelMod());
		texRow = new JTextField();
		texRow.setPreferredSize(new Dimension(50, 20));
		texCol = new JTextField();
		texCol.setPreferredSize(new Dimension(50, 20));
		texVal = new JTextField();
		texVal.setPreferredSize(new Dimension(500, 20));
		JLabel labelRow = new JLabel("Row:");
		labelRow.setPreferredSize(new Dimension(50, 20));
		JLabel labelCol = new JLabel("Col:");
		labelCol.setPreferredSize(new Dimension(50, 20));
		JLabel labelVal = new JLabel("Value:");
		labelCol.setPreferredSize(new Dimension(50, 20));
		JPanel ctrlPanel = new JPanel(new FlowLayout());
		ctrlPanel.add(labelRow);
		ctrlPanel.add(texRow);
		ctrlPanel.add(labelCol);
		ctrlPanel.add(texCol);
		ctrlPanel.add(labelVal);
		ctrlPanel.add(texVal);
		this.pnlS.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.pnlN.add(pnlFile1);
		this.pnlC.add(gridPanel1);
		this.pnlS.add(ctrlPanel);
		// ----------------------------------------------------------------------
		// TablePanel
		// ----------------------------------------------------------------------
		gridPanel1.setTableMouseListener(new TableMouseListener() {
			// -------------------------------------------------------------------------
			// マウスボタンを押した
			// -------------------------------------------------------------------------
			@Override
			public void mousePressedHandle(MouseEvent evt, int row, int col) {
				setRow(row);
				setCol(col);
				texRow.setText(String.valueOf(row));
				texCol.setText(String.valueOf(col));

				String value = gridPanel1.getDataValue(row, col);
				texVal.setText(value);

				// Vector<Vector> matrix = gridPanel1.getDataVector();
				// Vector vector = matrix.get(row);
				// if (vector != null) {
				// Object obj = vector.get(col);
				// String value = String.valueOf(obj);
				// setValue(value);
				// texVal.setText(value);
				// }
			}
		});
	}
}
