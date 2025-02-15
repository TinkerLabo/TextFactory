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
	private String delimiter = "\\t"; // ※注意 splitの引数はRegix！！
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
			System.out.println("readerが設定されていません");
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
