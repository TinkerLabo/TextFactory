package kyPkg.util;

import java.util.List;
import javax.swing.JComboBox;

public class SwingUtil {
	// sortしたくならないかな・・・
	// リストの内容をコンボボックスに流し込む
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

	// XXX　一番若い値、または一番大きな値をデフォルトにしたいとかないか？？
	// XXX　ソートして先頭の値、お尻の値を拾うなど・・・かな

	// デフォルトを最初に追加してしまうVersion
	public static void load2ComboBoxMod(JComboBox comboBox, List<String> list,
			String defaultVal, boolean mod) {
		comboBox.removeAllItems();
		// デフォルト値の強制追加指定があり・・・リスト中にデフォルト値が存在しない場合
//		if (mod == true && defaultVal != null && comboBox.getItemCount() == 0)
//			comboBox.addItem(defaultVal);
		if (defaultVal != null)
			comboBox.addItem(defaultVal);
		// 他のリストの値を追加する
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

		// 最初の値が入る時点で・・・イベントが発生してしまうので・・・
		// デフォルト値を設定する場合は先頭にした方がいい？？もしくは先頭が入った時点で下位のイベントを走らせない方法があるとよい
		// もしくはセレクトのイベントが判定できれば良い・・・・現状ではcomboBoxChangedで動いている

		// 追加する前に存在チェックを行い・・・存在すれば・・・または強制ならば先頭に追加する
		// 通常追加する場合にデフォルト値なら無視するという形に書き換える？？？？？？

		if (defaultVal != null && mod == true)
			comboBox.addItem(defaultVal);// 追加する場合は先頭になる
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

	// デフォルトをセレクトする旧版(デフォルト値が存在しない場合もある)
	public static void load2ComboBox_original(JComboBox comboBox,
			List<String> list, String defaultVal) {
		comboBox.removeAllItems();
		int select = 0;
		for (String element : list) {
			comboBox.addItem(element);
			if (element.equals(defaultVal)) // セレクトさせたい項目名"属性・性年代編"
				select = comboBox.getItemCount() - 1;
		}
		if (comboBox.getItemCount() > select)
			comboBox.setSelectedIndex(select);
	}
}
