package kyPkg.etc;

import java.util.Stack;

public class StackCtrl {
	private Stack<Object> stack = null;
	private boolean debug = true;

	public StackCtrl() {
		super();
		stack = new Stack();
	}

	//-------------------------------------------------------------------------
	//	スタックの先頭を返す
	//-------------------------------------------------------------------------
	public Object peek() {
		Object obj = null;
		if (!stack.isEmpty()) {
			try {
				obj = stack.peek();
				System.out.println("peek:" + obj.hashCode());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return obj;
	}

	//-------------------------------------------------------------------------
	// スタックにpushする
	//-------------------------------------------------------------------------
	public void push(Object owner, String title) {
		if (stack == null)
			stack = new Stack();
//		if (owner == null)
//			return;
		stack.add(owner);
		enumStack("push :" + title);
	}

	//-------------------------------------------------------------------------
	// スタックからpopする
	//-------------------------------------------------------------------------
	public Object pop() {
		if (stack == null)
			stack = new Stack();
		if (stack.size() > 0)
			stack.pop();
		enumStack("pop");
		return peek();
	}

	private void enumStack(String msg) {
		if (!debug)
			return;
		System.out.println("◆◆◆◆#<" + msg + ">#◆◆◆◆");
		Object[] array = stack.toArray();
		for (int i = 0; i < array.length; i++) {
			System.out.println(
					"     ◆◆◆◆#enumStack#<" + i + ">:" + array[i].hashCode());
		}
	}
}
