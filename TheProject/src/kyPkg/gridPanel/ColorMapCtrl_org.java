package kyPkg.gridPanel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class ColorMapCtrl_org {
	private List<ColorMap> colorMapList;

	public ColorMapCtrl_org(int count) {
		super();
		colorMapList = new ArrayList();
		for (int i = 0; i < count; i++) {
			colorMapList.add(new ColorMap());
		}
	}

	public List<ColorMap> getColorMapList() {
		return colorMapList;
	}

	public ColorMap getColorMap(int col) {
		if (colorMapList != null && colorMapList.size() > col) {
			return colorMapList.get(col);
		} else
			return null;
	}

	public Color peekColor(int row, int column) {
		ColorMap colorMap = getColorMap(column);
		if (colorMap == null)
			return null;
		return colorMap.peekColor(row);
	}

	public void popColor(int row, int column) {
		ColorMap colorMap = getColorMap(column);
		if (colorMap == null)
			return;
		colorMap.popColor(row);
	}

	public void pushColor(Integer row, int column, Color color) {
		ColorMap colorMap = getColorMap(column);
		if (colorMap == null)
			return;
		colorMap.pushColor(row, color);
	}

	//-------------------------------------------------------------------------
	class ColorMap {
		private HashMap<Integer, Stack<Color>> colorMap;

		public ColorMap() {
			super();
			colorMap = new HashMap();
		}

		public void popColor(Integer row) {
			Stack<Color> stack = colorMap.get(row);
			try {
				if (stack != null)
					stack.pop();
			} catch (java.util.EmptyStackException e) {
				colorMap.remove(row);
			}
		}

		public Color peekColor(Integer row) {
			Stack<Color> stack = colorMap.get(row);
			try {
				if (stack != null) {
					//					System.out.println("Å†peekColor:" + row);
					return stack.peek();
				}
			} catch (java.util.EmptyStackException e) {
			}
			return null;
		}

		public void pushColor(Integer row, Color color) {
			//			System.out.println("pushColor:" + row);
			Stack<Color> stack = colorMap.get(row);
			if (stack == null)
				stack = new Stack();
			stack.push(color);
			colorMap.put(row, stack);
		}
	}
}
