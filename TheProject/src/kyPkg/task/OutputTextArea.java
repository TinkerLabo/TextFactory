package kyPkg.task;
import java.awt.*;
import java.io.*;

import javax.swing.JTextArea;
public class OutputTextArea extends JTextArea {
	private static final long serialVersionUID = 1L;
	private TextAreaOutputStream out;
	public OutputTextArea() throws HeadlessException {
		super();
		this.setEditable(false);
		out = new TextAreaOutputStream(this);
	}
	public void setToSystemOut() {
		System.setOut(new PrintStream(this.getOut()));
	}
	public void setToSystemErr() {
		System.setErr(new PrintStream(this.getOut()));
	}
	public TextAreaOutputStream getOut() {
		return out;
	}
	public void flush() {
		this.append(out.toString());
		out.reset();
	}

	class TextAreaOutputStream extends ByteArrayOutputStream {
		private OutputTextArea textarea;
		public TextAreaOutputStream(OutputTextArea textarea) {
			super();
			this.textarea = textarea;
		}
		@Override
		public synchronized void write(byte[] b, int off, int len) {
			super.write(b, off, len);
			textarea.flush();
		}
		@Override
		public synchronized void write(int b) {
			super.write(b);
			textarea.flush();
		}
		@Override
		public void write(byte[] b) throws IOException {
			super.write(b);
			textarea.flush();
		}
	}
}