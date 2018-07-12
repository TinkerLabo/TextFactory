package kyPkg.tools;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;

public class Elapse {
	private String title = null;
	private String comment = null;
	private long current = 0;
	private long counter = 0;

	public long getCounter() {
		return counter;
	}

	private long sum = 0;
	private long prev = 0;
	private long startTime = 0;
	private long stopTime = 0;
	private long elapse = 0;
	public Log log = null;

	public Elapse(String title, Log log) {
		this(title);
		this.log = log;
		startTime = System.currentTimeMillis();
	}

	// Ç«Ç±Ç≈åƒÇÒÇ≈Ç¢ÇÈÇÃÇ©íTÇµÇ…Ç≠Ç≠Çƒç¢Ç¡ÇΩÇÃÇ≈ÅAÉ^ÉCÉgÉãÇïKê{Ç∆ÇµÇΩ
	public Elapse(String title) {
		setTitle(title);
	}

	public Elapse(String title, char filler) {
		setTitle(title);
		setFiller(filler);
	}

	private char filler = '*';
	private int width = 80;

	public void setFiller(char filler) {
		this.filler = filler;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	private String repeater(String prefix, int width) {
		StringBuffer buff = new StringBuffer();
		buff.append(prefix);
		for (int i = buff.length(); i < width; i++) {
			buff.append(filler);
		}
		return buff.toString();
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void start() {
		startTime = System.currentTimeMillis();
		prev = startTime;
	}

	public long check() {
		counter++;
		current = System.currentTimeMillis();
		long check = current - prev;
		sum += check;
		prev = current;
		return check;
	}

	public String stop() {
		stopTime = System.currentTimeMillis();
		elapse = stopTime - startTime;
		if (log == null) {
			System.out.println(getInfo(true));
		} else {
			List<String> list = getInfoList(true);
			for (String val : list) {
				log.info(val);
			}
		}
		return getInfo(false);
	}

	public static String edit(double sec) {
		DecimalFormat exFormat1 = new DecimalFormat("##.##");
		int hour = 0;
		int min = 0;
		if (sec > (60 * 60)) {
			hour = (int) sec / (60 * 60);
			sec = sec - (hour * (60 * 60));
		}
		if (sec > (60)) {
			min = (int) sec / 60;
			sec = sec - (min * 60);
		}
		if (min > 0) {
			if (hour > 0) {
				return hour + " éûä‘ " + min + " ï™ " + exFormat1.format(sec)
						+ " ïb";
			} else {
				return min + " ï™ " + exFormat1.format(sec) + " ïb";
			}
		} else {
			return exFormat1.format(sec) + " ïb";
		}
	}

	public List<String> getInfoList(boolean barOption) {
		List<String> info = new ArrayList<String>();
		String today = kyPkg.uDateTime.DateCalc.getToday("/");
		if (barOption) {
			info.add("");
			String prefix = "";
			if (title != null) {
				prefix = "<< " + title + " " + today + " >>";
			}
			String bar = repeater(prefix, width);
			info.add(bar);
		}
		if (comment != null)
			info.add(comment);
		double sec = (elapse / 1000.0);
		// info.add(" Start:" + new
		// SimpleDateFormat("HH:mm:ss.SSS").format(startTime));
		// info.add(" Stop:" + new
		// SimpleDateFormat("HH:mm:ss.SSS").format(stopTime));
		info.add(" Start  :"
				+ new SimpleDateFormat("HH:mm:ss").format(startTime));
		info.add(" Stop   :"
				+ new SimpleDateFormat("HH:mm:ss").format(stopTime));
		info.add(" Elapse :" + edit(sec));
		if (counter > 0) {
			info.add(" Counter:" + counter);
			double avg = (sum / (counter * 1000.0));
			info.add(" Average:" + edit(avg));
		}
		if (barOption) {
			String bar2 = repeater("", width);
			info.add(bar2);
		}
		return info;
	}

	public String getInfo(boolean barOption) {
		String LF = "\n";
		List<String> info = getInfoList(barOption);
		if (barOption == false)
			LF = " ";
		StringBuffer buf = new StringBuffer();
		for (String val : info) {
			buf.append(val);
			buf.append(LF);
		}
		return buf.toString();
	}

	// public String getInfo_org() {
	// String info = "";
	// String today = kyPkg.uDateTime.DateCalc.getToday("/");
	// info += "####################################\n";
	// if (title != null)
	// info += "<<" + title + ">>" + "\n";
	// if (comment != null)
	// info += "  " + comment + "\n";
	// info += " " + today + "\n";
	// info += " Start___ "
	// + new SimpleDateFormat("HH:mm:ss SSS").format(startTime) + "\n";
	// info += " Stop____ "
	// + new SimpleDateFormat("HH:mm:ss SSS").format(stopTime) + "\n";
	//
	// double sec = (elapse / 1000.0);
	// info += " Elapse__ " + edit(sec) + "\n";
	//
	// // int min = sec / 60;
	// // if(min>0){
	// // int hour = min / 60;
	// // if(hour>0){
	// // sec = sec - (min*60);
	// // info += " Elapse__ " + hour + " éûä‘ " + min + " ï™ " + sec + " ïb\n";
	// // }else{
	// // sec = sec - (min*60);
	// // info += " Elapse__ " + min + " ï™ " + sec + " ïbÅ@\n";
	// // }
	// // }else{
	// // info += " Elapse__ " + elapse / 1000.0 + " ïb\n";
	// // }
	// if (counter > 0) {
	// // info += " Sum________ " + sum + " É~Éäïb\n";
	// info += " Counter____ " + counter + " \n";
	// // info += " Average____ " + (sum / counter) + " É~Éäïb\n";
	// double avg = (sum / (counter * 1000.0));
	// info += " Average____ " + edit(avg) + "\n";
	// }
	// info += "####################################\n";
	// return info;
	// }
	// #############################################################################
	public static void main(String[] argv) {
		test();
		// String ans= Elapse.edit(60*60*2+60*3+4);
		// System.out.println("test:"+ans);
	}

	// -------------------------------------------------------------------------
	// ÅségÇ¢ï˚Åt
	// -------------------------------------------------------------------------
	public static void test() {
		String dateStr = kyPkg.uDateTime.DateCalc.today();
		System.out.println("today:" + dateStr);
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("Elapse Test", '=');
		elapse.start();
		try {
			elapse.check();
			Thread.sleep(300);
			elapse.check();
			Thread.sleep(400);
			elapse.check();
			Thread.sleep(500);
			elapse.check();
			Thread.sleep(600);
		} catch (Exception e) {
			e.printStackTrace();
		}
		elapse.stop();
	}
}
