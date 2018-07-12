package kyPkg.util;

import java.awt.datatransfer.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class TextCutPaste extends MouseAdapter {
/*
	<�g�p��>
    TextCutPaste tccp = new TextCutPaste();
    tf.addMouseListener(tccp);
    ta.addMouseListener(tccp);
*/




	protected JTextComponent textComponent;
	protected JPopupMenu popupMenu;
	protected AbstractAction cutAction;
	protected AbstractAction copyAction;
	protected AbstractAction pasteAction;

	public TextCutPaste() {
		popupMenu = new JPopupMenu();
		cutAction = new AbstractAction("Cut") {
			/**
			 * 
			 */
			private static final long serialVersionUID = -2500190822271378825L;

			@Override
			public void actionPerformed(ActionEvent event) {
				onCut();
			}
		};
		popupMenu.add(cutAction);
		copyAction = new AbstractAction("Copy") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 470512253893562250L;

			@Override
			public void actionPerformed(ActionEvent event) {
				onCopy();
			}
		};
		popupMenu.add(copyAction);
		pasteAction = new AbstractAction("Paste") {
			/**
			 * 
			 */
			private static final long serialVersionUID = -6901317914966225760L;

			@Override
			public void actionPerformed(ActionEvent event) {
				onPaste();
			}
		};
		popupMenu.add(pasteAction);
	}

	public void onCut() {
		textComponent.cut();
	}

	public void onCopy() {
		textComponent.copy();
	}

	public void onPaste() {
		textComponent.paste();
	}

	@Override
	public void mousePressed(MouseEvent event) {
		if ((event.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
			textComponent = (JTextComponent)(event.getSource());
			String selected = textComponent.getSelectedText();
			//	�e�L�X�g���I�����ꂽ�ꍇ�ɂ̂�[cut]��[copy]��L���ɂ���
			boolean canCut = ((selected != null) && (selected.length() > 0));
			cutAction.setEnabled(canCut);
			copyAction.setEnabled(canCut);

			Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
			Transferable t = cb.getContents(this);

			//	�R���|�[�l���g�Ƀt�H�[�J�X������A�N���b�v�{�[�h��
			//	�e�L�X�g�f�[�^���܂܂��ꍇ�ɂ̂�[paste]��L���ɂ���
			pasteAction.setEnabled((t.isDataFlavorSupported(DataFlavor.stringFlavor)) &&
					textComponent.hasFocus());
			popupMenu.show(textComponent, event.getX(), event.getY());
		}
	}

}
