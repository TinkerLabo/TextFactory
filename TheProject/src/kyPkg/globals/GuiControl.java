package kyPkg.globals;

import java.awt.Component;

//データベース関連の動作にGUIが絡んでいるかどうかをコントロールしている
public class GuiControl {
	private static final GuiControl instance = new GuiControl((Component) null,
			0);
	protected Component component = null;
	protected int timeOut = 0;
	private static final int DEFAULT_TIMEOUT = 240;//120;//60 * 4;//タイムアウトを設定20170731

	public GuiControl(Component component, int timeOut) {
		this.component = component;
		this.timeOut = timeOut;
	}

	private static GuiControl getInstance() {
		return instance;
	}

	public static void setComponent(Component component) {
		if (component == null)
			return;
		getInstance().component = component;
	}

	public static void setTimeOut(String timeOut) {
		if (timeOut == null)
			return;
		setTimeOut(Integer.parseInt(timeOut));
	}

	public static void setDefaultTimeOut() {
		getInstance().timeOut = DEFAULT_TIMEOUT;
	}

	private static void setTimeOut(int timeOut) {
		if (timeOut < 0)
			return;
		getInstance().timeOut = timeOut;
	}

	public static Component getComponent() {
		return getInstance().component;
	}

	public static int getTimeOut() {
		return getInstance().timeOut;
	}



}