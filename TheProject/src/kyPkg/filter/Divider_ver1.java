package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.io.BufferedWriter;
import java.io.FileWriter;

import kyPkg.uFile.DosEmu;
//���C�^�[���ЂƂ����g��Ȃ��o�[�W����
//---------------------------------------------------------------
// �w�肵���Z���̒l�iex�FID�j���ƂɃt�@�C���������s���E�E�E����max5000id���炢�Ƃ��Ăǂ�Ȃ��񂩁H
//---------------------------------------------------------------
public class Divider_ver1 implements Inf_BaseClojure {
	private static final String LF = System.getProperty("line.separator");
	private String preKey="";
	private String outDir;
	private int col;
	private String delimiter = "\t";
	private BufferedWriter bw;
	private int counter = 0;
	public long getCount() {
		return counter;
	}

	//---------------------------------------------------------------
	// �w��J�����̒l�����X�g�Ɋ܂܂�Ă����ꍇ���̃t�@�C���ɏo�͂����A����ȊO�͂��ɏo�͂����
	//---------------------------------------------------------------
	public Divider_ver1(String ofFile, int col) {
		this.counter = 0;
		this.outDir = ofFile;
		this.col = col;
	}
	//---------------------------------------------------------------
	// open
	//---------------------------------------------------------------
	private void open(String outPath) {
		//�ǉ����[�h�ŊJ��
		boolean append = true;
		try {
			bw = new BufferedWriter(new FileWriter(outPath,append));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	//---------------------------------------------------------------
	// write
	//---------------------------------------------------------------
	public void write(String[] rec) {
		String xrec = join(rec, delimiter);
		try {
			bw.write(xrec);
			bw.write(LF);
			counter++;
		} catch (Exception e) {
			System.out.println("#Error @TeeClojure.write1:" + e.toString());
			e.printStackTrace();
		}
	}
	//---------------------------------------------------------------
	// close
	//---------------------------------------------------------------
	private void close() {
		try {
			bw.close();
		} catch (Exception e) {
			System.out.println("#Error @TeeClojure:" + e.toString());
		}
	}
	@Override
	public void init() {
		outDir = kyPkg.uFile.FileUtil.makedir(outDir);
		// ���Y�f�B���N�g���ȉ��̃t�@�C�����폜����
		DosEmu.del(outDir + "*.txt");
	}
	@Override
	public void execute(String rec) {
		execute(rec.split(delimiter));
	}
	@Override
	public void execute(String[] rec) {
		if (rec.length > col) {
			if (!rec[col].equals(preKey)){
				//�@Break�����E�E�E
				if (!preKey.equals("") ) close();
				preKey = rec[col].trim();
				if (!preKey.equals("") ) open(outDir+preKey+".txt");
			}
			write(rec);
		}
	}
	@Override
	public void write() {
		if (!preKey.equals("") ) close();
	}
	public static void main(String[] argv) {
		//XXX �t�@�C���|�C���^�[�v�[�������E�E�E�L���[�̐��𒲐�����
		//�������̏�Ԃ��ώ@����
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("DividerVer1");
		elapse.start();
		tester();
		elapse.stop();
	}
	public static void tester() {
		String userDir = globals.ResControl.getQPRHome();
		String outDir = userDir+"828111000507/calc/chile";
		String inPath = userDir+"828111000507/calc/trrModItp.txt";
		int col = 0;
		Divider_ver1 closure = new Divider_ver1(outDir, col);
		new CommonClojure().incore(closure, inPath, true);
	}
}
