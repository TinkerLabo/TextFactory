package kyPkg.filter;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class Common_ICmp {
	private Reader reader;
	private String inPath;
	public String getInPath() {
		return inPath;
	}
	private String delimiter = "\\t"; // ¦’ˆÓ split‚Ìˆø”‚ÍRegixII
	public Common_ICmp(String param, boolean option) {
		if (option) {
			this.inPath = param;
			try {
				this.reader = new FileReader(param);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else {
			this.reader = new StringReader(param);
		}
	}

	protected Reader getReader() {
		if (reader == null) {
			System.out.println("reader‚ªİ’è‚³‚ê‚Ä‚¢‚Ü‚¹‚ñ");
			return null;
		}
		return reader;
	}

	protected void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	protected String getDelimiter() {
		return delimiter;
	}

	public void close() {
		try {
			if (getReader() != null)
				getReader().close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
