package kyPkg.util;

import java.util.List;
import javax.swing.JComboBox;

public class SwingUtil {
	// sort�������Ȃ�Ȃ����ȁE�E�E
	// ���X�g�̓��e���R���{�{�b�N�X�ɗ�������
	// kyPkg.util.SwingUtil.
	public static void load2Combo(JComboBox cmbBox, List listNext) {
		if (listNext == null)
			return;
		load2ComboBox(cmbBox, listNext, null);
	}

	public static void load2ComboBoxMod(JComboBox comboBox, String[] array,
			String defaultVal, boolean mod) {
		List<String> list = java.util.Arrays.asList(array);
		load2ComboBoxMod(comboBox, list, defaultVal, mod);
	}

	// XXX�@��ԎႢ�l�A�܂��͈�ԑ傫�Ȓl���f�t�H���g�ɂ������Ƃ��Ȃ����H�H
	// XXX�@�\�[�g���Đ擪�̒l�A���K�̒l���E���ȂǁE�E�E����

	// �f�t�H���g���ŏ��ɒǉ����Ă��܂�Version
	public static void load2ComboBoxMod(JComboBox comboBox, List<String> list,
			String defaultVal, boolean mod) {
		comboBox.removeAllItems();
		// �f�t�H���g�l�̋����ǉ��w�肪����E�E�E���X�g���Ƀf�t�H���g�l�����݂��Ȃ��ꍇ
//		if (mod == true && defaultVal != null && comboBox.getItemCount() == 0)
//			comboBox.addItem(defaultVal);
		if (defaultVal != null)
			comboBox.addItem(defaultVal);
		// ���̃��X�g�̒l��ǉ�����
		if (list != null) {
			for (String element : list) {
				if (element != null && !element.equals(defaultVal))
					comboBox.addItem(element);
			}
		}
	}

	public static void load2ComboBoxMod_org(JComboBox comboBox,
			List<String> list, String defaultVal, boolean mod) {
		comboBox.removeAllItems();
		int select = 0;

		// �ŏ��̒l�����鎞�_�ŁE�E�E�C�x���g���������Ă��܂��̂ŁE�E�E
		// �f�t�H���g�l��ݒ肷��ꍇ�͐擪�ɂ������������H�H�������͐擪�����������_�ŉ��ʂ̃C�x���g�𑖂点�Ȃ����@������Ƃ悢
		// �������̓Z���N�g�̃C�x���g������ł���Ηǂ��E�E�E�E����ł�comboBoxChanged�œ����Ă���

		// �ǉ�����O�ɑ��݃`�F�b�N���s���E�E�E���݂���΁E�E�E�܂��͋����Ȃ�ΐ擪�ɒǉ�����
		// �ʏ�ǉ�����ꍇ�Ƀf�t�H���g�l�Ȃ疳������Ƃ����`�ɏ���������H�H�H�H�H�H

		if (defaultVal != null && mod == true)
			comboBox.addItem(defaultVal);// �ǉ�����ꍇ�͐擪�ɂȂ�
		if (list != null) {
			for (String element : list) {
				if (element != null && element != defaultVal) {
					System.out.println("debug add element:" + element);
					comboBox.addItem(element);
					if (element.equals(defaultVal))
						select = comboBox.getItemCount() - 1;
				}
			}
		}
		if (comboBox.getItemCount() > select)
			comboBox.setSelectedIndex(select);
		// if (comboBox.getItemCount() > 1)
		// comboBox.setSelectedIndex(0);
	}

	public static void load2ComboBox(JComboBox comboBox, List<String> list,
			String defaultVal) {
		load2ComboBoxMod(comboBox, list, defaultVal, false);
	}

	// �f�t�H���g���Z���N�g���鋌��(�f�t�H���g�l�����݂��Ȃ��ꍇ������)
	public static void load2ComboBox_original(JComboBox comboBox,
			List<String> list, String defaultVal) {
		comboBox.removeAllItems();
		int select = 0;
		for (String element : list) {
			comboBox.addItem(element);
			if (element.equals(defaultVal)) // �Z���N�g�����������ږ�"�����E���N���"
				select = comboBox.getItemCount() - 1;
		}
		if (comboBox.getItemCount() > select)
			comboBox.setSelectedIndex(select);
	}
}
