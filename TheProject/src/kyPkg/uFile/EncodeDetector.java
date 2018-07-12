package kyPkg.uFile;

import java.util.List;

import kyPkg.sql.TContainer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.universalchardet.UniversalDetector;

//エンコードを判定するライブラリーjuniversalchardet-1.0.3.jarを配置する必要がある
//http://code.google.com/p/juniversalchardet/

public class EncodeDetector {
	public static final String APPENDA_CLASS = "kyPkg.sql";
	public static Log log = LogFactory.getLog(TContainer.APPENDA_CLASS);

	static final boolean DEBUG = false;

	public static String getEncode(String fileName) {
		String encoding = "";
		try {
			byte[] buf = new byte[4096];
			java.io.FileInputStream fis = new java.io.FileInputStream(fileName);
			// (1)Construct an instance of UniversalDetector.
			UniversalDetector detector = new UniversalDetector(null);
			// (2)Feed some data (typically several thousands bytes) to the
			// detector by calling handleData().
			int nread;
			while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
				detector.handleData(buf, 0, nread);
			}
			// (3)Notify the detector of the end of data by calling
			// UniversalDetector.dataEnd().
			detector.dataEnd();

			// (4)Get the detected encoding name by calling
			// getDetectedCharset().
			encoding = detector.getDetectedCharset();
			if (encoding == null) {
				if (DEBUG)
					System.out
							.println("No encoding detected. path:" + fileName);
				//				encoding = System.getProperty("file.encoding");// windows=>MS932
				encoding = FileUtil.getDefaultEncoding();//20161222
			}
			// (5)Don't forget to call UniversalDetector.reset() before you
			// reuse the detector instance.
			detector.reset();
			fis.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return encoding;
	}

	public static void main(String[] argv) {
		List<String> dirList = DosEmu.dir("C:/encode/*.txt");
		for (String path : dirList) {
			System.out
					.println(path + "  >>  " + EncodeDetector.getEncode(path));
		}
	}

}
