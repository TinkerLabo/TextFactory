package kyPkg.util;

import java.awt.*;
import java.awt.event.*;

public class MenuTemp{
	//《使用例》	MenuTemp.menuSet(this);
	public void menuSet(Frame pThis) {
		//--------------------------------------------------------------------
		// Menu
		//--------------------------------------------------------------------
		MenuBar wMBar;
		Menu wMenu;
		MenuItem wMItm1, wMItm2;
		wMenu = new Menu("File");
		wMItm1 = new MenuItem("Open");
		wMItm2 = new MenuItem("Close");
		wMenu.add(wMItm1);
		wMenu.addSeparator();
		wMenu.add(wMItm2);
		wMBar = new MenuBar();
		wMBar.add(wMenu);
		pThis.setMenuBar(wMBar);
		wMItm2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) { System.exit(0); }
		});
		//--------------------------------------------------------------------
	}

}
