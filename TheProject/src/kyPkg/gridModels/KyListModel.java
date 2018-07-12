package kyPkg.gridModels;

import java.util.List;

import static kyPkg.util.Joint.join;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.AbstractListModel; //import javax.swing.DefaultListModel;

import java.util.Vector;
import java.util.Enumeration;

//import javax.swing.event.*;
//-----------------------------------------------------------------------------
// 2007-07-10 ken yuasa 作成中なり・・・・・
//-----------------------------------------------------------------------------
//public class KyListModel extends DefaultListModel {
public class KyListModel extends AbstractListModel {
	private Vector delegate = new Vector();// オブジェクトの値を持つlist
	private int viewCol = 0;
	private final String VTAB = "" + '\u000b'; // 垂直タブ
	private String vtab = VTAB;//これを変更することは無いんじゃないか？

	// -------------------------------------------------------------------------
	// データのセットは、リストモデルごと入れ替えて実現させる・・・
	// →ＧＵＩ側の変更トリガーが良く分からなかったので・・こんどソースを見てみよう！
	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public KyListModel(String[] array) {
		this();
		if (array != null)
			modWithList(Arrays.asList(array));
	}

	public KyListModel(List list) {
		this();
		modWithList(list);
	}

	public KyListModel() {
		super();
	}

	private void modWithList(List list) {
		if (list != null && list.size() > 0) {
			for (Object object : list) {
				add(size(), object);
			}
		}
	}



	// -----------------------------------------------------------------------------
	// 2008/02/29
	// -----------------------------------------------------------------------------
	public String[] getArray() {
		List<Object> list = getData();
		String[] array = (String[]) list.toArray(new String[size()]);
		return array;
	}

	public List<Object> getData() {
		List<Object> list = new ArrayList();
		Object obj = null;
		for (int i = 0; i < size(); i++) {
			obj = getValue(i);//どっちが欲しいんだい＿？
			list.add(obj);
		}
		return list;
	}

	public List<Object> getDataList() {
		return getData();
	}

	// -----------------------------------------------------------------------------
	// リストの末尾にコンポーネントを追加する(すでに存在しない値のみ追加される)
	// -----------------------------------------------------------------------------
	public void addElement(Object obj, boolean unique) {
		if (obj == null)
			return;
		if (unique && isExitstVal(obj))
			return;
		//		System.out.println("#debug#@addElement() ---- condition:"				+ obj);
		int index = size();
		this.add(size(), obj);
		fireIntervalAdded(this, index, index);
	}

	private boolean isExitstVal(Object obj) {
		if (this.indexOf(obj) >= 0) {
			return true;
		} else {
			return false;
		}
	}

	public void setDataList(List<Object> dataList) {
		delegate.clear();//20130326
		for (Object obj : dataList) {
			this.addElement(obj);
		}
		int index1 = dataList.size() - 1;
		fireContentsChanged(this, 0, index1);
	}

	public void setView() {
		if (VTAB == null) {
			this.vtab = VTAB;
		} else {
			this.vtab = VTAB;
		}
		if (viewCol < 0) {
			this.viewCol = 0;
		} else {
			this.viewCol = 0;
		}
	}

	// 使われていないコード
	// オリジナルとロジックが違う！！
	// -----------------------------------------------------------------------------
	// 《必須》指定位置で示される値を返す
	// -----------------------------------------------------------------------------

	// -----------------------------------------------------------------------------
	// 要素を垂直タブで区切る！（垂直タブが存在しない場合は、表示と要素が一致していることとする）
	// -----------------------------------------------------------------------------
	public Object getValueX(int index, int seq) {
		if (index > this.size())
			return null;
		Object obj = this.get(index);
		// リストのマトリックスになっている場合・・・
		if (obj instanceof List) {
			List list = (List) obj;
			return join(list, "\t");
		} else if (obj instanceof String) {
			String[] splited = ((String) obj).split(vtab);
			if (splited.length > seq) {
				return splited[seq];
			} else {
				return "?!";// #ERROR
			}
		} else {
			return this.get(index);
		}
	}

	// 表示するコメント部位のみを返す
	// -----------------------------------------------------------------------------
	private Object getDisplayValue(int index) {
		if (delegate.size() < index)
			return null;

		Object obj = delegate.elementAt(index);
		// リストのマトリックスになっている場合・・・
		if (obj instanceof List) {
			List list = (List) obj;
			return join(list, "\t");
		} else if (obj instanceof String) {
			String cond = ((String) obj);
			String[] splited = cond.split(vtab);
			if (splited.length > viewCol) {
				return splited[viewCol];
			} else {
				return "?!";// #ERROR
			}
		} else {
			return obj;
		}
	}

	private Object getValue(int index) {
		Object obj = delegate.elementAt(index);
		// リストのマトリックスになっている場合・・・
		if (obj instanceof List) {
			//			System.out.println("20130326@getValue LIST!");
			List list = (List) obj;
			return join(list, "\t");
		} else if (obj instanceof String) {
			String cond = ((String) obj);
			//			System.out.println("20130326@getValue cond:"+cond);

			return cond;
			//20130326 うまく動くようにするには、こうするのかな・・・他に影響がでるかも知れない
			//			String[] splited = cond.split(vtab);
			//			if (splited.length > dataCol) {
			//				System.out.println("20130326@getValue cond:"+splited[dataCol]);
			//				return splited[dataCol];
			//			} else {
			//				System.out.println("20130326@getValue cond:"+splited[0]);
			//				return splited[0];  //20130220 でたらめなのであとで直す
			//			}
		} else {
			//			System.out.println("20130326@getValue Other!");
			return obj;
		}
	}

	// @Override

	// これを使っている部分をすべて洗い出す・・・
	// コメント部分が欲しいのか、パラメータ部分が必要なのかを洗い出す
	@Override
	public Object getElementAt(int index) {
		// return delegate.elementAt(index);
		return getDisplayValue(index);
	}

	// @Override
	// 追加@2010/08/16
	public void insertElementAt(Object obj, int index) {
		if (index < 0)
			index = 0;
		List<Object> wList = new ArrayList();
		for (int i = 0; i < index; i++) {
			Object wObj = this.get(i);
			wList.add(wObj);
		}
		wList.add(obj);
		for (int i = index; i < this.size(); i++) {
			Object wObj = this.get(i);
			wList.add(wObj);
		}
		setDataList(wList);
	}

	// @Override
	public Object getValueX(int index) {
		try {
			if (this.size() > index)
				return this.get(index);
		} catch (Exception e) {
			System.out.println("@getValueX index:" + index);
			System.out.println("@getValueX dataList.size():" + this.size());
		}
		return null;
	}

	// public Object getValueX(int index) {
	// try {
	// if (size() > index)
	// return get(index);
	// } catch (Exception e) {
	// System.out.println("@getValueX index:" + index);
	// System.out.println("@getValueX dataList.size():" + size());
	// }
	// return null;
	// }

	// original code

	/**
	 * Returns the number of components in this list.
	 * <p>
	 * This method is identical to <code>size</code>, which implements the
	 * <code>List</code> interface defined in the 1.2 Collections framework.
	 * This method exists in conjunction with <code>setSize</code> so that
	 * <code>size</code> is identifiable as a JavaBean property.
	 * 
	 * @return the number of components in this list
	 * @see #size()
	 */
	@Override
	public int getSize() {
		return delegate.size();
	}

	/**
	 * Returns the component at the specified index. <blockquote> <b>Note:</b>
	 * Although this method is not deprecated, the preferred method to use is
	 * <code>get(int)</code>, which implements the <code>List</code> interface
	 * defined in the 1.2 Collections framework. </blockquote>
	 * 
	 * @param index
	 *            an index into this list
	 * @return the component at the specified index
	 * @exception ArrayIndexOutOfBoundsException
	 *                if the <code>index</code> is negative or greater than the
	 *                current size of this list
	 * @see #get(int)
	 */
	// public Object getElementAt(int index) {
	// return delegate.elementAt(index);
	// }

	/**
	 * Copies the components of this list into the specified array. The array
	 * must be big enough to hold all the objects in this list, else an
	 * <code>IndexOutOfBoundsException</code> is thrown.
	 * 
	 * @param anArray
	 *            the array into which the components get copied
	 * @see Vector#copyInto(Object[])
	 */
	public void copyInto(Object anArray[]) {
		delegate.copyInto(anArray);
	}

	/**
	 * Trims the capacity of this list to be the list's current size.
	 * 
	 * @see Vector#trimToSize()
	 */
	public void trimToSize() {
		delegate.trimToSize();
	}

	/**
	 * Increases the capacity of this list, if necessary, to ensure that it can
	 * hold at least the number of components specified by the minimum capacity
	 * argument.
	 * 
	 * @param minCapacity
	 *            the desired minimum capacity
	 * @see Vector#ensureCapacity(int)
	 */
	public void ensureCapacity(int minCapacity) {
		delegate.ensureCapacity(minCapacity);
	}

	/**
	 * Sets the size of this list.
	 * 
	 * @param newSize
	 *            the new size of this list
	 * @see Vector#setSize(int)
	 */
	public void setSize(int newSize) {
		int oldSize = delegate.size();
		delegate.setSize(newSize);
		if (oldSize > newSize) {
			fireIntervalRemoved(this, newSize, oldSize - 1);
		} else if (oldSize < newSize) {
			fireIntervalAdded(this, oldSize, newSize - 1);
		}
	}

	/**
	 * Returns the current capacity of this list.
	 * 
	 * @return the current capacity
	 * @see Vector#capacity()
	 */
	public int capacity() {
		return delegate.capacity();
	}

	/**
	 * Returns the number of components in this list.
	 * 
	 * @return the number of components in this list
	 * @see Vector#size()
	 */
	public int size() {
		return delegate.size();
	}

	/**
	 * Tests whether this list has any components.
	 * 
	 * @return <code>true</code> if and only if this list has no components,
	 *         that is, its size is zero; <code>false</code> otherwise
	 * @see Vector#isEmpty()
	 */
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	/**
	 * Returns an enumeration of the components of this list.
	 * 
	 * @return an enumeration of the components of this list
	 * @see Vector#elements()
	 */
	public Enumeration<?> elements() {
		return delegate.elements();
	}

	/**
	 * Tests whether the specified object is a component in this list.
	 * 
	 * @param elem
	 *            an object
	 * @return <code>true</code> if the specified object is the same as a
	 *         component in this list
	 * @see Vector#contains(Object)
	 */
	public boolean contains(Object elem) {
		return delegate.contains(elem);
	}

	/**
	 * Searches for the first occurrence of <code>elem</code>.
	 * 
	 * @param elem
	 *            an object
	 * @return the index of the first occurrence of the argument in this list;
	 *         returns <code>-1</code> if the object is not found
	 * @see Vector#indexOf(Object)
	 */
	public int indexOf(Object elem) {
		return delegate.indexOf(elem);
	}

	/**
	 * Searches for the first occurrence of <code>elem</code>, beginning the
	 * search at <code>index</code>.
	 * 
	 * @param elem
	 *            an desired component
	 * @param index
	 *            the index from which to begin searching
	 * @return the index where the first occurrence of <code>elem</code> is
	 *         found after <code>index</code>; returns <code>-1</code> if the
	 *         <code>elem</code> is not found in the list
	 * @see Vector#indexOf(Object,int)
	 */
	public int indexOf(Object elem, int index) {
		return delegate.indexOf(elem, index);
	}

	/**
	 * Returns the index of the last occurrence of <code>elem</code>.
	 * 
	 * @param elem
	 *            the desired component
	 * @return the index of the last occurrence of <code>elem</code> in the
	 *         list; returns <code>-1</code> if the object is not found
	 * @see Vector#lastIndexOf(Object)
	 */
	public int lastIndexOf(Object elem) {
		return delegate.lastIndexOf(elem);
	}

	/**
	 * Searches backwards for <code>elem</code>, starting from the specified
	 * index, and returns an index to it.
	 * 
	 * @param elem
	 *            the desired component
	 * @param index
	 *            the index to start searching from
	 * @return the index of the last occurrence of the <code>elem</code> in this
	 *         list at position less than <code>index</code>; returns
	 *         <code>-1</code> if the object is not found
	 * @see Vector#lastIndexOf(Object,int)
	 */
	public int lastIndexOf(Object elem, int index) {
		return delegate.lastIndexOf(elem, index);
	}

	/**
	 * Returns the component at the specified index. Throws an
	 * <code>ArrayIndexOutOfBoundsException</code> if the index is negative or
	 * not less than the size of the list. <blockquote> <b>Note:</b> Although
	 * this method is not deprecated, the preferred method to use is
	 * <code>get(int)</code>, which implements the <code>List</code> interface
	 * defined in the 1.2 Collections framework. </blockquote>
	 * 
	 * @param index
	 *            an index into this list
	 * @return the component at the specified index
	 * @see #get(int)
	 * @see Vector#elementAt(int)
	 */
	public Object elementAt(int index) {
		return delegate.elementAt(index);
	}

	/**
	 * Returns the first component of this list. Throws a
	 * <code>NoSuchElementException</code> if this vector has no components.
	 * 
	 * @return the first component of this list
	 * @see Vector#firstElement()
	 */
	public Object firstElement() {
		return delegate.firstElement();
	}

	/**
	 * Returns the last component of the list. Throws a
	 * <code>NoSuchElementException</code> if this vector has no components.
	 * 
	 * @return the last component of the list
	 * @see Vector#lastElement()
	 */
	public Object lastElement() {
		return delegate.lastElement();
	}

	/**
	 * Sets the component at the specified <code>index</code> of this list to be
	 * the specified object. The previous component at that position is
	 * discarded.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is
	 * invalid. <blockquote> <b>Note:</b> Although this method is not
	 * deprecated, the preferred method to use is <code>set(int,Object)</code>,
	 * which implements the <code>List</code> interface defined in the 1.2
	 * Collections framework. </blockquote>
	 * 
	 * @param obj
	 *            what the component is to be set to
	 * @param index
	 *            the specified index
	 * @see #set(int,Object)
	 * @see Vector#setElementAt(Object,int)
	 */
	public void setElementAt(Object obj, int index) {
		delegate.setElementAt(obj, index);
		fireContentsChanged(this, index, index);
	}

	/**
	 * Deletes the component at the specified index.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is
	 * invalid. <blockquote> <b>Note:</b> Although this method is not
	 * deprecated, the preferred method to use is <code>remove(int)</code>,
	 * which implements the <code>List</code> interface defined in the 1.2
	 * Collections framework. </blockquote>
	 * 
	 * @param index
	 *            the index of the object to remove
	 * @see #remove(int)
	 * @see Vector#removeElementAt(int)
	 */
	public void removeElementAt(int index) {
		delegate.removeElementAt(index);
		fireIntervalRemoved(this, index, index);
	}

	/**
	 * Inserts the specified object as a component in this list at the specified
	 * <code>index</code>.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is
	 * invalid. <blockquote> <b>Note:</b> Although this method is not
	 * deprecated, the preferred method to use is <code>add(int,Object)</code>,
	 * which implements the <code>List</code> interface defined in the 1.2
	 * Collections framework. </blockquote>
	 * 
	 * @param obj
	 *            the component to insert
	 * @param index
	 *            where to insert the new component
	 * @exception ArrayIndexOutOfBoundsException
	 *                if the index was invalid
	 * @see #add(int,Object)
	 * @see Vector#insertElementAt(Object,int)
	 */
	// public void insertElementAt(Object obj, int index) {
	// delegate.insertElementAt(obj, index);
	// fireIntervalAdded(this, index, index);
	// }

	/**
	 * Adds the specified component to the end of this list.
	 * 
	 * @param obj
	 *            the component to be added
	 * @see Vector#addElement(Object)
	 */
	public void addElement(Object obj) {
		int index = delegate.size();
		delegate.addElement(obj);
		fireIntervalAdded(this, index, index);
	}

	/**
	 * Removes the first (lowest-indexed) occurrence of the argument from this
	 * list.
	 * 
	 * @param obj
	 *            the component to be removed
	 * @return <code>true</code> if the argument was a component of this list;
	 *         <code>false</code> otherwise
	 * @see Vector#removeElement(Object)
	 */
	public boolean removeElement(Object obj) {
		int index = indexOf(obj);
		boolean rv = delegate.removeElement(obj);
		if (index >= 0) {
			fireIntervalRemoved(this, index, index);
		}
		return rv;
	}

	/**
	 * Removes all components from this list and sets its size to zero.
	 * <blockquote> <b>Note:</b> Although this method is not deprecated, the
	 * preferred method to use is <code>clear</code>, which implements the
	 * <code>List</code> interface defined in the 1.2 Collections framework.
	 * </blockquote>
	 * 
	 * @see #clear()
	 * @see Vector#removeAllElements()
	 */
	public void removeAllElements() {
		int index1 = delegate.size() - 1;
		delegate.removeAllElements();
		if (index1 >= 0) {
			fireIntervalRemoved(this, 0, index1);
		}
	}

	/**
	 * Returns a string that displays and identifies this object's properties.
	 * 
	 * @return a String representation of this object
	 */
	@Override
	public String toString() {
		return delegate.toString();
	}

	/*
	 * The remaining methods are included for compatibility with the Java 2
	 * platform Vector class.
	 */

	/**
	 * Returns an array containing all of the elements in this list in the
	 * correct order.
	 * 
	 * @return an array containing the elements of the list
	 * @see Vector#toArray()
	 */
	public Object[] toArray() {
		Object[] rv = new Object[delegate.size()];
		delegate.copyInto(rv);
		return rv;
	}

	/**
	 * Returns the element at the specified position in this list.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is out
	 * of range (<code>index &lt; 0 || index &gt;= size()</code>).
	 * 
	 * @param index
	 *            index of element to return
	 */
	public Object get(int index) {
		return delegate.elementAt(index);
	}

	/**
	 * Replaces the element at the specified position in this list with the
	 * specified element.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is out
	 * of range (<code>index &lt; 0 || index &gt;= size()</code>).
	 * 
	 * @param index
	 *            index of element to replace
	 * @param element
	 *            element to be stored at the specified position
	 * @return the element previously at the specified position
	 */
	public Object set(int index, Object element) {
		Object rv = delegate.elementAt(index);
		delegate.setElementAt(element, index);
		fireContentsChanged(this, index, index);
		return rv;
	}

	/**
	 * Inserts the specified element at the specified position in this list.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is out
	 * of range (<code>index &lt; 0 || index &gt; size()</code>).
	 * 
	 * @param index
	 *            index at which the specified element is to be inserted
	 * @param element
	 *            element to be inserted
	 */
	public void add(int index, Object element) {
		delegate.insertElementAt(element, index);
		fireIntervalAdded(this, index, index);
	}

	/**
	 * Removes the element at the specified position in this list. Returns the
	 * element that was removed from the list.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is out
	 * of range (<code>index &lt; 0 || index &gt;= size()</code>).
	 * 
	 * @param index
	 *            the index of the element to removed
	 */
	public Object remove(int index) {
		Object rv = delegate.elementAt(index);
		delegate.removeElementAt(index);
		fireIntervalRemoved(this, index, index);
		return rv;
	}

	/**
	 * Removes all of the elements from this list. The list will be empty after
	 * this call returns (unless it throws an exception).
	 */
	public void clear() {
		int index1 = delegate.size() - 1;
		delegate.removeAllElements();
		if (index1 >= 0) {
			fireIntervalRemoved(this, 0, index1);
		}
	}

	/**
	 * Deletes the components at the specified range of indexes. The removal is
	 * inclusive, so specifying a range of (1,5) removes the component at index
	 * 1 and the component at index 5, as well as all components in between.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index was
	 * invalid. Throws an <code>IllegalArgumentException</code> if
	 * <code>fromIndex &gt; toIndex</code>.
	 * 
	 * @param fromIndex
	 *            the index of the lower end of the range
	 * @param toIndex
	 *            the index of the upper end of the range
	 * @see #remove(int)
	 */
	public void removeRange(int fromIndex, int toIndex) {
		if (fromIndex > toIndex) {
			throw new IllegalArgumentException("fromIndex must be <= toIndex");
		}
		for (int i = toIndex; i >= fromIndex; i--) {
			delegate.removeElementAt(i);
		}
		fireIntervalRemoved(this, fromIndex, toIndex);
	}

	/*
	 * public void addAll(Collection c) { }
	 * 
	 * public void addAll(int index, Collection c) { }
	 */
}
