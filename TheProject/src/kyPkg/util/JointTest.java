package kyPkg.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JointTest {

	@Test
	public void test() {
		testJoint();
		//		fail("Not yet implemented");
	}

	@Test
	public void testJoint() {
		//---------------------------------------------------------------------------
		List<String> list = new ArrayList();
		for (int i = 0; i <= 7; i++) {
			if (i == 3) {
				list.add(null);
			} else {
				list.add(String.valueOf(i));
			}
		}
		assertEquals("'0'-'1'-'2'-''-'4'-'5'-'6'-'7'",
				kyPkg.util.Joint.join(list, "-", "'"));
		//---------------------------------------------------------------------------
		String[] array = new String[8];
		for (int i = 0; i < array.length; i++) {
			array[i] = String.valueOf(i);
		}
		array[3] = null;
		assertEquals(kyPkg.util.Joint.join(array, "-", "'"),
				"'0'-'1'-'2'-''-'4'-'5'-'6'-'7'");
		//---------------------------------------------------------------------------
		assertEquals("'2'-''-'4'-'5'-'6'-'7'",
				kyPkg.util.Joint.join(array, "-", "'", 2));

	}

}
