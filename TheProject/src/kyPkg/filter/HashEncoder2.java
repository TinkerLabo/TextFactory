package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;

import kyPkg.uFile.FileUtil;
//�w�肵���Z���̒l�i�Ⴆ�΁F�敪���Ȃǁj���n�b�V���l�ɕϊ�����
public class HashEncoder2 implements Inf_BaseClojure {
	private static final String LF = System.getProperty("line.separator");
	private String dictH2I;
	private String dictI2N;
	private String outPath;
	private int col;
	private String delimiter = "\t";
	private BufferedWriter bw;
	private int counter = 0;
	private HashMap<String, String> hmap;
	private String hashCode;
	private int preHash;
	public long getCount() {
		return counter;
	}

	public HashEncoder2(String dictH2IPath,String dictI2NPath, String outPath, int col) {
		this.counter = 0;
		this.dictH2I = dictH2IPath; // Hash to Index
		this.dictI2N = dictI2NPath; // Index to dimName
		this.outPath = outPath;
		this.col = col;
	}

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

	@Override
	public void init() {
		hmap = new HashMap();
		// open
		try {
			bw = new BufferedWriter(new FileWriter(outPath));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	public void execute(String rec) {
		execute(rec.split(delimiter));
	}

	@Override
	public void execute(String[] rec) {
		if (rec.length > col) {
			int curHash = rec[col].hashCode();
			if (preHash != curHash) {
				preHash = curHash;
				hashCode = String.valueOf(preHash);
				hmap.put(hashCode, rec[col]);
			}
			rec[col] = hashCode;
			write(rec);
		}
	}

	@Override
	public void write() {
		// close
		try {
			bw.close();
		} catch (Exception e) {
			System.out.println("#Error @TeeClojure:" + e.toString());
		}
		DictionaryControl.writeDictionary2(dictH2I,dictI2N, hmap, delimiter);
	}

	public static void main(String[] argv) {
		hashEncIndexTest();
	}

	public static void hashEncIndexTest() {
		int col = 3;
		String userDir = globals.ResControl.getQPRHome();
		String inPath = userDir+"828111000507/calc/trrModItp.txt";
		String indexed = userDir+"828111000507/calc/indexed.txt";
		//�@��U�n�b�V���R�[�h�ɒu��������K�v�͂Ȃ���ˁH�I���[�ށE�E�E��肠��������Ă݂�
		String encoded = userDir+"828111000507/calc/encode.txt";
		String dirPath = FileUtil.getParent(inPath);
		String dictH2I = dirPath + "dictH2I.txt";
		String dictI2N = dirPath + "dictI2N.txt";
		//-----------------------------------------------------------
		//�敪�����n�b�V���R�[�h�ɕϊ�����E�E�E
		//�n�b�V���Q�C���f�b�N�X�ϊ������A�C���f�b�N�X�Q�敪�������̏o�͂��s��
		//XXX �����̃T�C�Y�����炩���߂킩��ƕ֗��Ȃ̂ŁA���\�b�h��ǉ�����
		//-----------------------------------------------------------
		HashEncoder2 encCloj = new HashEncoder2(dictH2I,dictI2N, encoded, col);
		new CommonClojure().incore(encCloj, inPath, true);
		//-----------------------------------------------------------
		//�n�b�V������C���f�b�N�X�ɕϊ�����
		//-----------------------------------------------------------
		HashDecoder idxCloj = new HashDecoder(indexed,dictH2I, col);
		new CommonClojure().incore(idxCloj, encoded, true);

	}
	public static void hashEncIndexNameTest() {
		//�@��U�n�b�V���R�[�h�ɒu��������K�v�͂Ȃ���ˁH�I���[�ށE�E�E
		//�@��肠��������Ă݂�
		String userDir = globals.ResControl.getQPRHome();

		String encoded = userDir+"828111000507/calc/encode.txt";
		String inPath = userDir+"828111000507/calc/trrModItp.txt";
		String dirPath = FileUtil.getParent(inPath);
		String dictH2I = dirPath + "dictH2I.txt";
		String dictI2N = dirPath + "dictI2N.txt";
		int col = 3;
		//-----------------------------------------------------------
		//�敪�����n�b�V���R�[�h�ɕϊ�����E�E�E
		//�n�b�V���Q�C���f�b�N�X�ϊ������A�C���f�b�N�X�Q�敪�������̏o�͂��s��
		//XXX �����̃T�C�Y�����炩���߂킩��ƕ֗��Ȃ̂ŁA���\�b�h��ǉ�����
		//-----------------------------------------------------------
		HashEncoder2 encCloj = new HashEncoder2(dictH2I,dictI2N, encoded, col);
		new CommonClojure().incore(encCloj, inPath, true);
		//-----------------------------------------------------------
		//�n�b�V������C���f�b�N�X�ɕϊ�����
		//-----------------------------------------------------------
		String indexed = userDir+"828111000507/calc/indexed.txt";
		HashDecoder idxCloj = new HashDecoder(indexed,dictH2I, col);
		new CommonClojure().incore(idxCloj, encoded, true);

		//-----------------------------------------------------------
		//�C���f�b�N�X���疼�̂ɕϊ�����
		//-----------------------------------------------------------
		String named = userDir+"828111000507/calc/named.txt";
		HashDecoder namCloj = new HashDecoder(named,dictI2N, col);
		new CommonClojure().incore(namCloj, indexed, true);
	}

}
