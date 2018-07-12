package kyPkg.task;

import java.util.List;

import kyPkg.uFile.ListArrayUtil;
import kyPkg.util.Banner;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogControl {
	public static final String APPENDA_CLASS = "LogControl";
	public static Log log = LogFactory.getLog(LogControl.APPENDA_CLASS);
	public static String message = "";

	public static String getMessage() {
		return message;
	}

	public static void info(String message) {
		LogControl.message = message;//@@@@@@
		System.out.println("■■■ info ■■■ :" + message);
		log.info(message);
	}

	public static void info(List<String> messages) {
		for (String message : messages) {
			log.info(message);
		}
	}

	// 引数は英数字のみ（エラーなどを目立たせるために使用する）
	public static void infoBanner(String message) {
		List<String> messages = Banner.banner(message);
		for (String msg : messages) {
			log.info(msg);
		}
	}

	public static Log getLog() {
		return log;
	}

	// ---------------------------------------------------------------
	// 例＞　mailIt(Transfers2MM.DEFAULT_EMAIL;);
	// ---------------------------------------------------------------
	public static void mailIt(String mailFrm,String toEmail, int stat) {
		String subject = "Regular処理が実行されました（ver1.0）";
		if (stat == 0) {
			subject += " => OK";
		} else {
			subject += " => ## NG!! ## (" + stat + ")";
		}

		List logList = ListArrayUtil.file2List("Ssa.log");
		 
		new kyPkg.socks.SMTPmini(mailFrm, toEmail, subject, logList);
	}

}
