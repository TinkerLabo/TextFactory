package kyPkg.sql.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class JComboBoxIconMod extends JComboBox {
	public static JComboBox sampleJComboBox1() {
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("Elephant", "Elephant.png");
		hmap.put("Hippo", "Hippo.png");
		hmap.put("Lion", "Lion.png");
		hmap.put("Ostrich", "Ostrich.png");
		JComboBox combo = new JComboBoxIconMod(hmap);
		return combo;
	}

	public static JComboBox sampleJComboBox2() {
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("LEFT", "LEFT.png");
		hmap.put("INNER", "INNER.png");
		hmap.put("RIGHT", "RIGHT.png");
		// 表示順のコントロール
		List<String> keyList = new ArrayList();
		keyList.add("INNER");
		keyList.add("LEFT");
		keyList.add("RIGHT");
		JComboBox combo = new kyPkg.sql.gui.JComboBoxIconMod(hmap, keyList);
		return combo;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame() {
			{
				JPanel panel = new JPanel();
				// JComboBox combo = sampleJComboBox1();
				JComboBox combo = sampleJComboBox2();
				panel.add(combo);
				// combo.setSelectedIndex(0);
				getContentPane().add(panel, BorderLayout.CENTER);
			}
		};
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(10, 10, 300, 200);
		frame.setTitle("comboTest");
		frame.setVisible(true);
	}

	// 順序指定しなくてもいい場合
	JComboBoxIconMod(HashMap<String, String> hmap) {
		this(hmap, null);
	}

	// 順序指定したい場合：keyListを指定する
	JComboBoxIconMod(HashMap<String, String> hmap, List<String> keyList) {
		super();
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		Set<String> keySet = hmap.keySet();
		if (keyList == null) {
			keyList = new ArrayList(keySet);
			Collections.sort(keyList);
		}
		for (String key : keyList) {
			model.addElement(new ComboLabel(key, hmap.get(key)));
		}
		setModel(model);
		setRenderer(new MyCellRenderer());
	}

	class ComboLabel {
		final static String imageRoot = "./img/";
		String text;
		Icon icon;

		// コンストラクタ
		ComboLabel(String text, String fileName) {
			// System.out.println("@ComboLabel text =>" + text);
			// System.out.println("@ComboLabel file =>" + fileName);
			this.text = text;
			kyPkg.uFile.FileUtil.mkdir(imageRoot);
			// todo　当該ファイルがなかったらどうする？？
			String path = imageRoot + fileName;
			if (new File(path).exists()) {
				this.icon = new ImageIcon(path);
			} else {
				System.out.println("#ERROR @ComboLabel file not exist =>"
						+ path);
				this.icon = null;
			}
		}
		@Override
		public String toString() {
			return text;
		}

		public String getText() {
			return text;
		}

		public Icon getIcon() {
			return icon;
		}
	}

	class MyCellRenderer extends JLabel implements ListCellRenderer {
		Color deepBlue = new Color(51, 0, 153); // ぐんじょういろ
		Color lightBlue = new Color(204, 255, 255); // みずいろ
		Color white = Color.WHITE; // 白

		MyCellRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			ComboLabel data = (ComboLabel) value;
			setText(data.getText());
			setIcon(data.getIcon());
			if (isSelected) {
				setForeground(deepBlue);// 文字の色
				setBackground(lightBlue);
			} else {
				setForeground(deepBlue);
				setBackground(white);
			}
			return this;
		}
	}
}
