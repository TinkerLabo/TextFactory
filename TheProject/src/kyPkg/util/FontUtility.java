package kyPkg.util;

import javax.swing.UIManager;
import java.awt.Font;

/**
 * �t�H���g�̓�����s��static���\�b�h�����N���X�ł��B
 * <p>
 */
public final class FontUtility {

	private FontUtility() {
	}

	public static void main(String[] argv) {
		FontUtility.setFont(new Font("Dialog", Font.PLAIN, 8), false);
	}

	/**
	 * �t�H���g�̓�����s���܂��B
	 * <p>
	 * ���̃��\�b�h��LookAndFeel�̃t�H���g�̐ݒ�� �w�肳�ꂽ�t�H���g�ɒu����������̂Ȃ̂ŁA
	 * LookAndFeel��ύX����ꍇ�͕ύX���s��������� ���̃��\�b�h���Ăяo���K�v������܂��B
	 * <p>
	 * 
	 * @param font
	 *            ���ꂷ��t�H���g
	 */
	public static void setFont(Font font, boolean debug) {
		if (debug == false)
			return;
		System.err.println("setFont:" + font.getFontName());
		//		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		UIManager.put("OptionPane.buttonFont", font);
		UIManager.put("OptionPane.font", font);
		UIManager.put("OptionPane.messageFont", font);
		
		UIManager.put("Button.font", font);
		UIManager.put("ToggleButton.font", font);
		UIManager.put("RadioButton.font", font);
		UIManager.put("CheckBox.font", font);
		UIManager.put("ColorChooser.font", font);
		UIManager.put("ComboBox.font", font);
		UIManager.put("Label.font", font);
		UIManager.put("List.font", font);
		UIManager.put("MenuBar.font", font);
		UIManager.put("MenuItem.font", font);
		UIManager.put("MenuItem.acceleratorFont", font);
		UIManager.put("RadioButtonMenuItem.font", font);
		UIManager.put("RadioButtonMenuItem.acceleratorFont", font);
		UIManager.put("CheckBoxMenuItem.font", font);
		UIManager.put("CheckBoxMenuItem.acceleratorFont", font);
		UIManager.put("Menu.font", font);
		UIManager.put("Menu.acceleratorFont", font);
		UIManager.put("PopupMenu.font", font);
		UIManager.put("OptionPane.font", font);
		UIManager.put("Panel.font", font);
		UIManager.put("ProgressBar.font", font);
		UIManager.put("ScrollPane.font", font);
		UIManager.put("ViewPort.font", font);
		UIManager.put("TabbedPane.font", font);
		UIManager.put("Table.font", font);
		UIManager.put("TableHeader.font", font);
		UIManager.put("TextField.font", font);
		UIManager.put("PasswordField.font", font);
		UIManager.put("TextArea.font", font);
		UIManager.put("TextPane.font", font);
		UIManager.put("EditorPane.font", font);
		UIManager.put("TitledBorder.font", font);
		UIManager.put("ToolBar.font", font);
		UIManager.put("ToolTip.font", font);
		UIManager.put("Tree.font", font);
	}
}
