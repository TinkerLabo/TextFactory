package kyPkg.util;

import java.awt.Component;
import java.awt.Cursor;

public class CursorUtil {
	// カーソルを変更（砂時計表示にする） HourGlass,hourglass
	public static void setHourglass(Component cmp, boolean flag) {
		if (cmp == null)
			return;
		if (flag) {
			cmp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		} else {
			cmp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
}
