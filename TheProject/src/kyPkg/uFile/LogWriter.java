package kyPkg.uFile;
import java.io.IOException;
import java.io.OutputStreamWriter;
public class LogWriter {
	//TODO ���b�Z�[�W�o�͎��̃^�C���X�^���v�A�X�e�[�^�X���A�I�v�V�����o�͂�����
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
				writer.write(LF, 0, LF.length()); // ���s�R�[�h
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
		logIns.println("������������");
		logIns.println("�܍��̂��肫��");
		logIns.println("�C���������̐��s��");
		logIns.println("�_�����A������");
		logIns.println("�H���Q��Ƃ���ɏZ�ނƂ���");
		logIns.println("��Ԃ炱�����̂Ԃ炱����");
		logIns.println("�p�C�|�p�C�|�A�p�C�|�̃V���[�����K��");
		logIns.println("�V���[�����K���̃O�[�����_�C");
		logIns.println("�O�[�����_�C�̃|���|�R�s�[�̃|���|�R�i��");
		logIns.println("���v���̒���");
		logIns.close();
	}
}
