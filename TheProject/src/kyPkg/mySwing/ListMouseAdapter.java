package kyPkg.mySwing;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JList;
import javax.swing.ListModel;

import kyPkg.gridModels.KyListModel;

//*****************************************************************************
// �C�x���g�̃n���h�����I�u�W�F�N�g�����瑀�삷�邽�߂̃C���^�[�t�F�[�X
//*****************************************************************************
public class ListMouseAdapter implements MouseListener {
	private boolean remove;
	private Object currentObj;
	public Object getCurrentObj() {
		return currentObj;
	}
	public ListMouseAdapter(boolean remove) {
		super();
		this.remove = remove;
	}
	// -------------------------------------------------------------------------
	// �}�E�X�N���b�N�i�E�{�^���j
	// -------------------------------------------------------------------------
	public  void clicked(Object obj){
		currentObj = obj;
	};
	// -------------------------------------------------------------------------
	// �}�E�X�_�u���N���b�N�i�E�{�^���j
	// -------------------------------------------------------------------------
	public void doubleClicked(Object obj) {
	}
	@Override
	public void mouseClicked(MouseEvent evt) {
		Object evtSource = evt.getSource();
		if (evtSource instanceof JList) {
			JList jList = (JList) evtSource;
			if (evt.getClickCount() == 1) {
				Object selectedValue = jList.getSelectedValue();
				clicked(selectedValue);
			}
			if (evt.getClickCount() == 2) {
				if (remove) {
					ListModel listModel = (ListModel) jList.getModel();
					if(listModel instanceof KyListModel){
						((KyListModel) listModel).removeElementAt(jList.getSelectedIndex());
					}
				}else{
					Object selectedValue = jList.getSelectedValue();
					doubleClicked(selectedValue);
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}
