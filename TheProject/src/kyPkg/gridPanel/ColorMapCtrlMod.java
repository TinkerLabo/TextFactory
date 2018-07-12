package kyPkg.gridPanel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class ColorMapCtrlMod {
	private List<ColorMapMod> colorMapList;
	// 行数が与えられたらその先頭値（JAN）コードを返すロジックを入れる
	private Grid grid;
	//一番先頭の値をキーとしている
	
	public void reset(){
		colorMapList.clear();
	}
	
	private String row2key(int row) {
		return grid.getString(row, 0);//janCode
	}

	// コンストラクタ　int countはカラム数？？
	public ColorMapCtrlMod(Grid grid, int count) {
		super();
		this.grid = grid;
		colorMapList = new ArrayList();
		for (int i = 0; i < count; i++) {
			colorMapList.add(new ColorMapMod());
		}
	}

	public List<ColorMapMod> getColorMapList() {
		return colorMapList;
	}

	public ColorMapMod getColorMap(int col) {
		if (colorMapList != null && colorMapList.size() > col) {
			return colorMapList.get(col);
		} else
			return null;
	}

	public Color peekColor(int row, int column) {
		ColorMapMod colorMap = getColorMap(column);
		if (colorMap == null)
			return null;
		return colorMap.peekColor(row2key(row));
	}

	// カラーマップをポップ（一個前の状態に）する
	public void popColor(String key, int column) {
		ColorMapMod colorMap = getColorMap(column);
		if (colorMap == null)
			return;
		colorMap.popColor(key);
	}

	// 直前の色を保存する（カラーマップをpushする）
	public void pushColor(String key, int column, Color color) {
		ColorMapMod colorMap = getColorMap(column);
		if (colorMap == null)
			return;
		colorMap.pushColor(key, color);
	}

	// -------------------------------------------------------------------------
	class ColorMapMod {
		private HashMap<String, Stack<Color>> colorMap;

		public ColorMapMod() {
			super();
			colorMap = new HashMap();
		}

		public void popColor(String key) {
			Stack<Color> stack = colorMap.get(key);
			try {
				if (stack != null)
					stack.pop();
			} catch (java.util.EmptyStackException e) {
				colorMap.remove(key);
			}
		}

		public Color peekColor(String key) {
			Stack<Color> stack = colorMap.get(key);
			try {
				if (stack != null) {
					return stack.peek();
				}
			} catch (java.util.EmptyStackException e) {
			}
			return null;
		}

		public void pushColor(String key, Color color) {
			Stack<Color> stack = colorMap.get(key);
			if (stack == null)
				stack = new Stack();
			stack.push(color);
			colorMap.put(key, stack);
		}
	}
}
