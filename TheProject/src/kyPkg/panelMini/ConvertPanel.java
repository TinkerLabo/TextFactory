package kyPkg.panelMini;

import javax.swing.*;

import java.util.*;

public class ConvertPanel extends JScrollPane {
	private ArrayList arrayLeft;

	private ArrayList arrayRight;

	private static final long serialVersionUID = 1L;

	private JPanel newLeftComponent;

	private JPanel newRightComponent;

	private JSplitPane splitpane;

	private JTextField[] texLeft;

	private JComboBox[] texRight;

	private int maxSize;

	private int[] converter;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public ConvertPanel() {
		super();
		newLeftComponent = new JPanel();
		newRightComponent = new JPanel();
		splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				newLeftComponent, newRightComponent);
		setViewportView(splitpane);
		this
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}

	public void setStrArray(String[] left, String[] right) {
		setLeft(left);
		setRight(right);
	}

	public void setConverter(int[] converter) {
		if (converter == null)
			return;
		this.converter = converter;
	}
	public void setLeft(String[] array) {
		if (array != null) {
			List list = Arrays.asList(array);
			setArrayLeft(new ArrayList(list));
		} else {
			setArrayLeft(new ArrayList());
		}
	}
	public void setRight(String[] array) {
		if (array != null) {
			List list = Arrays.asList(array);
			setArrayRight(new ArrayList(list));
		} else {
			setArrayRight(new ArrayList());
		}
	}

	public void setArrays(ArrayList left, ArrayList right) {
		setArrayLeft(left);
		setArrayRight(right);
	}

	public void setArrayLeft(ArrayList parrayLeft) {
		this.arrayLeft = parrayLeft;
	}

	public void setArrayRight(ArrayList parrayRight) {
		this.arrayRight = parrayRight;
	}

	// -------------------------------------------------------------------------
	// 設置した後に行う??
	// -------------------------------------------------------------------------
	public void populate() {
		newLeftComponent = new JPanel();
		newRightComponent = new JPanel();
		splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				newLeftComponent, newRightComponent);
		setViewportView(splitpane);
		newLeftComponent.setLayout(new BoxLayout(newLeftComponent,
				BoxLayout.Y_AXIS));
		newRightComponent.setLayout(new BoxLayout(newRightComponent,
				BoxLayout.Y_AXIS));
		splitpane.setDividerLocation((this.getWidth() / 2));
		if (arrayLeft != null) {

			maxSize = arrayLeft.size();

			texLeft = new JTextField[maxSize];
			for (int i = 0; i < texLeft.length; i++) {
				String element = "";
				if (arrayLeft != null) {
					element = (String) arrayLeft.get(i);
				}
				texLeft[i] = new JTextField(element);
				texLeft[i].setEditable(false);
				newLeftComponent.add(texLeft[i]);
			}
			Object[] elements = null;
			if (arrayRight != null) {
				elements = arrayRight.toArray(new Object[arrayRight.size()]);
				texRight = new JComboBox[maxSize];
				for (int i = 0; i < texRight.length; i++) {
					if (elements != null) {
						texRight[i] = new JComboBox(elements);
						if (i < arrayRight.size()) {
							texRight[i].setSelectedIndex(i);
						} else {
							texRight[i].setSelectedIndex(arrayRight.size() - 1);
						}
					} else {
						texRight[i] = new JComboBox();
					}
					newRightComponent.add(texRight[i]);
				}
				applyConverter(); 
			}
		}
	}

	// -------------------------------------------------------------------------
	// other or cutoff の処理はレンジ幅でやっつける・・・
	// -------------------------------------------------------------------------
	public int[] getConverter() {
		converter = new int[texRight.length];
		for (int i = 0; i < converter.length; i++) {
			converter[i] = texRight[i].getSelectedIndex();
		}
		return converter;
	}

	// -------------------------------------------------------------------------
	public void applyConverter() {
		try {
			for (int i = 0; i < texRight.length; i++) {
				if (i < converter.length) {
					if (texRight[i].getModel().getSize() > converter[i]) {
						texRight[i].setSelectedIndex(converter[i]);
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
}
