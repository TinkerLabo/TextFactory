/*
 * ListPanelx.java
 *
 * Created on 2007/08/28, 14:38
 */
package kyPkg.mySwing;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import kyPkg.gridModels.KyListModel;
import kyPkg.util.LCRendLabel;

// -----------------------------------------------------------------------------
// ListPanel <���i >
// ----------------------------------------------------------------------
// ListPanel�̎g����
// 1.���X�g���f�����쐬
// 2.���X�g���f���������ɃC���X�^���X�쐬
// 3.�N���b�N���ꂽ���̂��o�͂���I�u�W�F�N�g���w�肷��
// �����X�g���f���ɂ͐F�X�Ȍ`���Ńf�[�^�𗬂����݂₷�����Ă����B
// ----------------------------------------------------------------------
// listP_Dir = new ListPanel(new ProtLModel());
// �s�g�p��t
// ListPanel listPanel = new ListPanel();
// listPanel.addElement("UNIX,Linux,OSX,WindowsVista".split(","));
// -----------------------------------------------------------------------------


//TODO yen-mark ��\���ł���悤�ɂ��������E�E�E�悭�킩��Ȃ��Ȃ��Ă��܂����@FontUtility.setFont(new Font("���C���I", Font.PLAIN, 10));


public class ListPanel extends JPanel implements InfListPanel {
	private static final long serialVersionUID = 7965088195657670952L;
	private JList jList;
	private KyListModel listModelK;
	private boolean unique = false; // ���j�[�N�l�����ǉ��ł��Ȃ��悤�ɂ���

	public int listCount() {
		return listModelK.getSize();
	}

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	// array ���X�g�ɗ������ޔz��
	// unique true�Ȃ烊�X�g�ɗ������ޒl�����j�[�N�ɂ���
	// remove true�Ȃ�_�u���N���b�N�����ꍇ�l�����X�g����͂���
	// -------------------------------------------------------------------------
	public ListPanel(Rectangle rect, boolean unique, boolean remove) {
		this(unique, remove);
		setBounds(rect);
	}

	public ListPanel(boolean unique, boolean remove) {
		this((String[]) null, unique, remove);
	}

	public ListPanel(String[] array, boolean unique, boolean remove) {
		super();
		this.unique = unique;
		this.listModelK = new KyListModel(array);
		jList = new JList(listModelK);
		// jList.setModel(listModelK);
		// ---------------------------------------------------------------------
		// �����_���[
		// ---------------------------------------------------------------------
		// LCRendLabel listRenderer = new LCRendLabel();
		jList.setCellRenderer(new LCRendLabel());
		setLayout(new BorderLayout());
		setOpaque(false);
		add(new JScrollPane(jList), java.awt.BorderLayout.CENTER);
		addMouseListener(new ListMouseAdapter(remove));
	}

	@Override
	public void addMouseListener(MouseListener mouseListener) {
		jList.addMouseListener(mouseListener);
	}

	public int getSelectedIndex() {
		return jList.getSelectedIndex();
	}

	public Object getSelectedValue() {
		return jList.getSelectedValue();
	}

	public void setSelectedIndex(int index) {
		jList.setSelectedIndex(index);
	}

	public void setListModel(KyListModel pLModel) {
		listModelK = pLModel;
		jList.setModel(listModelK);
	}

	// --------------------------------------------------------------------------
	// ���b�p�[
	// --------------------------------------------------------------------------
	public void setListData(Object[] listData) {
		listModelK.clear();
		for (int i = 0; i < listData.length; i++) {
			listModelK.addElement(listData[i], unique);
		}
		// ���܂��ĕ`�悳��Ȃ��̂Ń��f���̍ăZ�b�g�����Ă݂�@2012-12-07
		jList.setModel(listModelK);
		jList.repaint();
		repaint();
	}

	@Override
	public void setListData(List list) {
		listModelK.clear();
		if (list == null || list.size() <= 0)
			return;
		for (Object element : list) {

			listModelK.addElement(element, unique);
		}
		// �����Ńg���u�������@2013-02-01�i�o�b�N�J���[�̃��l���������Ă����̂������ۂ��j
		// ���l��������ƕ`�悪���������Ȃ�悤���E�E�c�O�E
		// int iR, iG, iB, alpha;
		// iR = (int) Math.floor(Math.random() * 256);
		// iG = (int) Math.floor(Math.random() * 256);
		// iB = (int) Math.floor(Math.random() * 256);
		// alpha = 30;
		// Color wBackColor = new Color(iR, iG, iB, alpha);
		// jList.setBackground(wBackColor);
		// ���܂��ĕ`�悳��Ȃ��̂Ń��f���̍ăZ�b�g�����Ă݂�@2012-12-07
		jList.setModel(listModelK);
		jList.updateUI();// 20130516
	}

	// �K�v�ɂȂ�����ǉ����邱�Ƃɂ��遫
	// // ----------------------------------------------------------------------
	// public void setListMouseListener(MouseListener mouseListener) {
	// jList.addMouseListener(mouseListener);
	// }
	// // ----------------------------------------------------------------------
	// list_Src.addMouseListener(new java.awt.event.MouseAdapter() {
	// public void mouseClicked(MouseEvent e) {
	// if (e.getClickCount() == clickCount) {
	// int sIdx = ((JList) e.getSource()).getSelectedIndex();
	// Object sVal = model_Src.getValueX(sIdx);
	// model_Dst.addElement(sVal, unique);
	// model_Src.removeElementAt(sIdx);
	// }
	// }
	// });

	@Override
	public void remove(int index) {
		listModelK.remove(index); // 2010/02/08
	}

	// -------------------------------------------------------------------------
	// �A�N�Z�b�T
	// -------------------------------------------------------------------------
	public int getListSize() {
		return listModelK.size();
	}

	@Override
	public void addElement(String sVal) {
		listModelK.addElement(sVal, unique);
	}

	public void addElement(String[] array) {
		for (String element : array) {
			addElement(element);
		}
	}

	public void setElementAt(Object obj, int index) {
		if (index >= 0 && listModelK.size() >= index) {
			listModelK.setElementAt(obj, index);
		}
	}

	public void insertElementAt(Object obj, int index) {
		listModelK.insertElementAt(obj, index);
	}

	public Object getElementAt(int index) {
		if (index >= 0 && listModelK.size() >= index) {
			return listModelK.getElementAt(index);// �ǂ������~�����񂾂��Q�H
		}
		return null;
	}

	public Object getValueX(int index) {
		if (index >= 0 && listModelK.size() >= index) {
			return listModelK.getValueX(index);
		}
		return null;
	}

	public Object getValueX(int index, int seq) {
		if (index >= 0 && listModelK.size() >= index) {
			return listModelK.getValueX(index, seq);
		}
		return null;
	}

	@Override
	public void clear() {
		listModelK.clear();
	}

	@Override
	public List<Object> getList() {
		if (listModelK == null)
			return null;
		return listModelK.getData();
	}

	public void setList(List list) {
		if (listModelK != null && list != null) {
			listModelK.setDataList(list);
		}
	}

	public List getOriginalRamones() {
		List<String> list = new ArrayList();
		list.add("Joey    Ramone  (vocals; born Jeffrey Hyman    May  19,1951)");
		list.add("Johnny  Ramone  (guitar; born John    Cummings Oct  08,1951)");
		list.add("Tommy   Ramone  (drums;  born Tom     Erdelyi  Jan. 29,1952)");
		list.add("Dee Dee Ramone  (bass;   born Douglas Colvin   Sept 18,1952)");
		return list;
	}

	// -------------------------------------------------------------------------
	// �`��
	// -------------------------------------------------------------------------
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
