package kyPkg.gridPanel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class ColorMapCtrlMod {
	private List<ColorMapMod> colorMapList;
	// �s�����^����ꂽ�炻�̐擪�l�iJAN�j�R�[�h��Ԃ����W�b�N������
	private Grid grid;
	//��Ԑ擪�̒l���L�[�Ƃ��Ă���
	
	public void reset(){
		colorMapList.clear();
	}
	
	private String row2key(int row) {
		return grid.getString(row, 0);//janCode
	}

	// �R���X�g���N�^�@int count�̓J�������H�H
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

	// �J���[�}�b�v���|�b�v�i��O�̏�ԂɁj����
	public void popColor(String key, int column) {
		ColorMapMod colorMap = getColorMap(column);
		if (colorMap == null)
			return;
		colorMap.popColor(key);
	}

	// ���O�̐F��ۑ�����i�J���[�}�b�v��push����j
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
