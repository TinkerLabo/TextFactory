package kyPkg.queue;

import java.util.Iterator;
import java.util.LinkedList;

public class Queue {
	public static void main(String[] args) {
	}
	// 先入先出法を提供する Queue インタフェース
	public static void normalQueue(String[] args) {
		java.util.Queue<String> qe = new LinkedList<String>();
		qe.add("b");
		qe.add("a");
		qe.add("c");
		qe.add("e");
		qe.add("d");
		Iterator it = qe.iterator();
		System.out.println("Initial Size of Queue :" + qe.size());

		while (it.hasNext()) {
			String iteratorValue = (String) it.next();
			System.out.println("Queue Next Value :" + iteratorValue);
		}

		// get value and does not remove element from queue
		System.out.println("Queue peek :" + qe.peek());

		// get first value and remove that object from queue
		System.out.println("Queue poll :" + qe.poll());

		System.out.println("Final Size of Queue :" + qe.size());
	}

}
