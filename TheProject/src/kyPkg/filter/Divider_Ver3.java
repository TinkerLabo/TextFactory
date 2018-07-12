package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import kyPkg.uFile.DosEmu;

//�n�b�V���}�b�v�Ƀ��C�^�[�I�u�W�F�N�g���i�[����o�[�W�����i�o�͌�����c���ł���j
//�o�͐悪������Ɓi�I�u�W�F�N�g��������A�t�@�C���|�C���^����������p�ӂ���̂Łj�q�[�v������Ȃ��Ȃ鋰�ꂠ��
//---------------------------------------------------------------
// �w�肵���Z���̒l�iex�FID�j���ƂɃt�@�C���������s���E�E�E����max5000id���炢�Ƃ��Ăǂ�Ȃ��񂩁H
//---------------------------------------------------------------
public class Divider_Ver3 implements Inf_BaseClojure {
	class WriterObj {
		private String path = "";

		public String getPath() {
			return path;
		}

		private BufferedWriter writer;
		private int count;

		public int getCount() {
			return count;
		}

		WriterObj(String path) {
			this.path = path;
			this.count = 0;
			this.writer = open(path);
		}

		// ---------------------------------------------------------------
		// open
		// ---------------------------------------------------------------
		private BufferedWriter open(String outPath) {
			// �ǉ����[�h�ŊJ��
			BufferedWriter bw = null;
			boolean append = true;
			try {
				bw = new BufferedWriter(new FileWriter(outPath, append));
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
			return bw;
		}

		// ---------------------------------------------------------------
		// write
		// ---------------------------------------------------------------
		public void write(String[] rec) {
			String xrec = join(rec, delimiter);
			try {
				writer.write(xrec);
				writer.write(LF);
				count++;
			} catch (Exception e) {
				System.out.println("#Error @TeeClojure.write1:" + e.toString());
				e.printStackTrace();
			}
		}

		// ---------------------------------------------------------------
		// close
		// ---------------------------------------------------------------
		public void close() {
			try {
				writer.close();
			} catch (Exception e) {
				System.out.println("#Error @TeeClojure:" + e.toString());
			}
		}
	}

	private static final String LF = System.getProperty("line.separator");
	private boolean append;
	private String outDir;
	private int[] col;
	private int maxCol;
	private String delimiter = "\t";
	private HashMap<String, WriterObj> hmap;
	private List pathList;

	public List getPathList() {
		return pathList;
	}

	// ---------------------------------------------------------------
	// �w��J�����̒l�����X�g�Ɋ܂܂�Ă����ꍇ���̃t�@�C���ɏo�͂����A����ȊO�͂��ɏo�͂����
	// ---------------------------------------------------------------
	public Divider_Ver3(String ofFile, int[] col,boolean append) {
		this.outDir = ofFile;
		this.col = col;
		this.append = append;
		maxCol = -1;
		for (int i = 0; i < col.length; i++) {
			if (maxCol < col[i])
				maxCol = col[i];
		}
	}

	@Override
	public void init() {
		hmap = new HashMap<String, WriterObj>();
		outDir = kyPkg.uFile.FileUtil.makedir(outDir);
		if(this.append==false){
			// ���Y�f�B���N�g���ȉ��̃t�@�C�����폜����
			DosEmu.del(outDir + "*.txt");
		}
	}

	@Override
	public void execute(String rec) {
		execute(rec.split(delimiter));
	}

	@Override
	public void execute(String[] rec) {
		if (rec.length > maxCol) {
			StringBuffer buff = new StringBuffer();
			buff.delete(0, buff.length());
			for (int i = 0; i < col.length; i++) {
				buff.append(rec[col[i]]);
			}
			String curKey = buff.toString();
			if (!curKey.equals("")) {
				WriterObj writerObj = hmap.get(curKey);
				if (writerObj == null) {
					writerObj = new WriterObj(outDir + curKey + ".txt");
					hmap.put(curKey, writerObj);
				}
				if (writerObj != null)
					writerObj.write(rec);
			}
		}
	}

	@Override
	public void write() {
		pathList = new ArrayList();
		java.util.Set set = hmap.entrySet();
		java.util.Iterator it = set.iterator();
		while (it.hasNext()) {
			java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
			WriterObj writerObj = (WriterObj) ent.getValue();
			if (writerObj.getCount() >= 1) {
				pathList.add(writerObj.getPath());
			}
			writerObj.close();
		}
		System.out.println("count:" + pathList.size());
	}

	public static void main(String[] argv) {
		// XXX �t�@�C���|�C���^�[�v�[�������E�E�E�L���[�̐��𒲐�����
		// �������̏�Ԃ��ώ@����
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("Divider_Ver3");
		elapse.start();
//		jicfsDivide();
		makerDivide();
		elapse.stop();
	}

	public static void makerDivide() {
		
		//default�ł�append�ɂ͂Ȃ��Ă��Ȃ��l�q
		
		// 2010/04/02 JICFS�@�}�[�J�[�}�X�^�[����
		String rootDir = globals.ResControl.getQprRootDir();
		String inPath = rootDir+"JicfsChile/ED1.txt";
		String outDir = rootDir+"JicfsChile/maker/";

		int[] divCol = new int[] { 0, 1,3 };

		Divider_Ver3 closure = new Divider_Ver3(outDir, divCol,true);
		new CommonClojure().incore(closure, inPath, true);
		List list = closure.getPathList();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			String wkPath = (String) iterator.next();
			System.out.println("wkPath:" + wkPath);
		}
	}

	
	public static void jicfsDivide() {
		// 2010/04/02 JICFS�@�A�C�e���}�X�^�[�@�t�H�[�}�b�g�ύX�֘A
		//DIS0006020100201184346STD
		String rootDir = globals.ResControl.getQprRootDir();
		String userDir = globals.ResControl.getQPRHome();
		String inPath = userDir+"DIS0006020100224183900STD.dat";
		String outDir = rootDir+"JicfsChile";
		
		int[] divCol = new int[] { 0, 1 };
		Divider_Ver3 closure = new Divider_Ver3(outDir, divCol,true);
		new CommonClojure().incore(closure, inPath, true);
		List list = closure.getPathList();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			String wkPath = (String) iterator.next();
			System.out.println("wkPath:" + wkPath);
		}
	}
}
