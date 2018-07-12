package kyPkg.uFile;
import java.io.IOException;
import java.io.OutputStreamWriter;
public class LogWriter {
	//TODO メッセージ出力時のタイムスタンプ、ステータスも、オプション出力したい
	static final String LF = System.getProperty("line.separator");
	private OutputStreamWriter writer;
	public void startMessage(String message){
		String today = kyPkg.uDateTime.DateCalc.getToday();
		String thisTime = kyPkg.uDateTime.DateCalc.thisTime();
		println(today + " " + thisTime + ":" + message);
	}
	public LogWriter(){
		this("");
	}
	public LogWriter(String path){
		if (path.trim().equals("")) path = "default.log";
		writer = FileUtil.getWriterEx(path); 
	}
	public void println(String line){
		try {
			if (writer != null) {
				writer.write(line, 0, line.length());
				writer.write(LF, 0, LF.length()); // 改行コード
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void close(){
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		String rootDir = globals.ResControl.getQprRootDir();
		LogWriter logIns = new LogWriter(rootDir+"test.txt");
		logIns.println("寿限無寿限無");
		logIns.println("五劫のすりきれ");
		logIns.println("海砂利水魚の水行末");
		logIns.println("雲来末、風来末");
		logIns.println("食う寝るところに住むところ");
		logIns.println("やぶらこうじのぶらこうじ");
		logIns.println("パイポパイポ、パイポのシューリンガン");
		logIns.println("シューリンガンのグーリンダイ");
		logIns.println("グーリンダイのポンポコピーのポンポコナの");
		logIns.println("長久命の長助");
		logIns.close();
	}
}
