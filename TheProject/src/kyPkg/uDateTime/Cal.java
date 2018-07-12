package kyPkg.uDateTime;

import java.util.*;

public class Cal {
	
    public static final String[] WEEK_NAMES
	= {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};

    public static final String[] MONTH_NAMES
	= {"January", "February", "March", "April", "May", "June", "July",
	   "August", "September", "October", "November", "December"};
	

    public static int day_of_week(int y, int m, int d) {
	int[] b = { 2, 5, 0, 3, 5, 1, 4, 6, 2, 4, 0, 3 };
	/* week number on 0th in each month from March to Feburuay */

	/* It is acceptable that m == 13 or 14 */
	if (m < 1 || 14 < m)
	    throw new IndexOutOfBoundsException("Month: " + m);

	if (m == 1 || m == 2) {
	    m += 12;
	    y -= 1;
	}
	return (y + y/4 - y/100 + y/400 + b[m - 3] + d) % 7;
    }

    public static int days_of_month(int y, int m) {
	switch (m) {
	    case 1: case 3: case 5: case 7: case 8: case 10: case 12:
		return 31;
	    case 4: case 6: case 9: case 11:
		return 30;
	    case 2:
		if (y % 400 == 0) // leap year
		    return 29;
		else if (y % 100 == 0) // not
		    return 28;
		else if (y % 4 == 0) // leap
		    return 29;
		else		// not
		    return 28;
	    default:
		throw new IndexOutOfBoundsException("Month: " + m);
	}
    }
	
    public static void main(String[] args) {
	if (args.length == 0) {
	    GregorianCalendar cal = new GregorianCalendar();
	    printCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
	}
	else if (args.length == 2) {
	    try {
		printCalendar(parseInt(args[1]), parseMonth(args[0]));
	    }
	    catch (IllegalArgumentException e) {
		// Note: NumberFormatException is-a IllegalArgumentException
		System.err.println("Usage: java CalendarApplet month year");
		System.exit(1);
	    }
	}
	else {
	    System.err.println("Invalid arguments");
	    System.exit(1);
	}
    }

    static int parseInt(String s) throws NumberFormatException {
	return Integer.parseInt(s);
    }

    static int parseMonth(String s) throws IllegalArgumentException {
	try {
	    return Integer.parseInt(s);
	}
	catch (NumberFormatException e) {
	    for (int i = 0; i < MONTH_NAMES.length; i++) {
		if (MONTH_NAMES[i].toUpperCase().startsWith(s.toUpperCase())) {
		    return i + 1;
		}
	    }
	    throw new IllegalArgumentException(s + " as month");
	}
    }

    static void printCalendar(int y, int m) {
	System.out.print(MONTH_NAMES[m - 1]);
	System.out.print("  ");
	System.out.println(y);
	int w = day_of_week(y, m, 1);
	for (int i = 0; i < w; i++) {
	    System.out.print("   ");
	}
	for (int d = 1; d <= days_of_month(y, m); d++) {
	    System.out.print(rightfilled(d));
	    if ((w + d) % 7 == 0) {
		System.out.println();
	    }
	}
	System.out.println();
    }

    public static String rightfilled(int n) {
	String str = "  " + n;
	return str.substring(str.length() - 3, str.length());
    }
}

