package kyPkg.filter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kyPkg.uFile.FileUtil;

/**************************************************************************
 * Col2Dictionary�@�w�肵���Z���̒l���C���f�b�N�X�����ɕύX����				
 * @author	ken yuasa
 * @version	1.0
 **************************************************************************/
@SuppressWarnings("unused")
public class Col2Dictionary implements Inf_BaseClojure {
	private String dictDimName;
	private int col;
	private String delimiter = "\t";
	private HashSet<String> keySet;
	private Set<String> dummySet = null;

	//	private List<String> pref=null;
	public void setDummySet(Set<String> dummySet) {
		this.dummySet = dummySet;
	}

	private int dimCount;

	public int getDimCount() {
		return dimCount;
	}

	/**************************************************************************
	 * Col2Dictionary�@�R���X�g���N�^
	 * @param dictDimNamePath			 
	 * @param col			 
	 **************************************************************************/
	public Col2Dictionary(String dictDimNamePath, int col) {
		this.dictDimName = dictDimNamePath; // Index to dimName
		this.col = col;
	}

	@Override
	public void init() {
		keySet = new HashSet();
	}

	@Override
	public void execute(String rec) {
		execute(rec.split(delimiter));
	}

	@Override
	public void execute(String[] rec) {
		if (rec.length > col) {
			keySet.add(rec[col]);
		}
	}

	@Override
	public void write() {
		if (dummySet != null)
			keySet.addAll(dummySet); //TODO ��������Ԓ��O�ɕs�����̃L�[����Set�ɒǉ����Ă��܂��΃_�~�[�����ɂȂ�Ȃ����ȁE�E�E�E�Ɗ���
		//		String[] prefix=new String[]{ModsCommon.NOT_BUY};//"��w��"��擪�ɒǉ�
		String[] prefix = null;
		dimCount = DictionaryControl.writeSortedDict2File(dictDimName, keySet,
				prefix);
	}

	//-----------------------------------------------------------
	//�w��J��������C���f�b�N�X�������쐬����
	//20151120 ��w����擪�ɑ}������H
	//-----------------------------------------------------------
	public static int convert(String outPath, String dictPath, String inPath,
			int col, Set<String> dummySet) {
		Col2Dictionary clojure = new Col2Dictionary(dictPath, col);
		clojure.setDummySet(dummySet);

		new CommonClojure().incore(clojure, inPath, true);
		int dimCount = clojure.getDimCount();
		if (dimCount > 0) {
			//-----------------------------------------------------------
			//�C���f�b�N�X�����ɂ�薼�̂��C���f�b�N�X�ɒu��������
			//-----------------------------------------------------------
			HashDecoder idxCloj = new HashDecoder(outPath, dictPath, col, -1);
			new CommonClojure().incore(idxCloj, inPath, true);
		}
		return dimCount;
	}

	public static void main(String[] argv) {
		tester();
	}

	public static void tester() {
		int col = 3;
		String userDir = globals.ResControl.getQPRHome();
		String inPath = userDir + "828111000507/calc/trrModItp.txt";
		String outPath = userDir + "828111000507/calc/indexed.txt";
		String dirPath = FileUtil.getParent(inPath) + "/";
		String dictPath = dirPath + "dimName.txt";
		int dimCount = convert(outPath, dictPath, inPath, col, null);
		System.out.println("dimCount:" + dimCount);
		//-----------------------------------------------------------
		//�C���f�b�N�X����敪���̂ɕϊ�����
		//-----------------------------------------------------------
		String recover = userDir + "828111000507/calc/recover.txt";
		HashDecoder nameCloj = new HashDecoder(recover, dictPath, col, -2);
		new CommonClojure().incore(nameCloj, outPath, true);
	}

}
