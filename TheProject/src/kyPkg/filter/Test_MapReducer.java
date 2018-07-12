package kyPkg.filter;

import java.util.Iterator;
import java.util.List;

import globals.ResControlWeb;
import kyPkg.converter.CnvMap;
import kyPkg.etc.MapReducer;

public class Test_MapReducer {
	// #########################################################################
	public static void main(String[] argv) {
		test3();
	}

	// -------------------------------------------------------------------------
	// �e�X�g�̌��ʂ͈ȉ��̂Ƃ���ƂȂ�
	// keyb,b,this is B,20,40,60,2
	// keyc,c,this is C,10,20,30,1
	// keya,a,this is A,30,60,90,3
	// -------------------------------------------------------------------------
	public static void testX() {
		String[] rec = { "key,a,         ,10,20,30",
				"key,a,this is A,10,20,30", "key,a,         ,10,20,30",
				"key,b,this is B,10,20,30", "key,b,         ,10,20,30",
				"key,c,this is C,10,20,30" };
		// MapReducer ins = new MapReducer(
		// new int[] { 0, 1 },
		// new int[] { 1, 2 },
		// new int[] { 3, 4, 5 }, ",");
		String keyColumns= "0 ,1 " ; 
		String mapColumns= " 1, 2 " ;
		String sumColumns= " 3, 4, 5 ";
		MapReducer mapRedObj = new MapReducer("",keyColumns,mapColumns,sumColumns ,",");
		mapRedObj.init();
		for (int i = 0; i < rec.length; i++) {
			mapRedObj.execute(rec[i]);
		}
		List list = mapRedObj.getList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			System.out.println("//�@" + element);
		}
		mapRedObj.write();
	}

	public static void test1() {
		String srcFile = ResControlWeb.getD_Resources_Templates("temp01.txt");
		String outPath = ResControlWeb.getD_Resources_Templates("MapReducer.txt");
		String keyColumns = "2 ,3 "; // �}�b�s���O�Ɏg�p���鍀��
		String sumColumns = " 4, 5, 6 "; // ���������鍀��
		MapReducer closure = new MapReducer(outPath,keyColumns, sumColumns,"\t");
//		closure.setOutPath(outPath);
		new CommonClojure().incore(closure, srcFile, true);
	}

	public static void test2() {
		// ���� �C���f�b�N�X�͗납��n�߂邱�ƁI�I
		// �o�͏��͕ۏႳ��܂���i�ڒ��ڒ��ł��j
		String srcFile = "Z:/s2/rx/Idxc/Enquete/�����E���C�t�X�^�C����/2008/QTB1.txt";
		String outPath = ResControlWeb.getD_Resources_Templates("MapReducer.txt");
		String keyColumns="2"; 
		String mapColumns="4";
		String sumColumns="8,9";
		MapReducer closure = new MapReducer(outPath, keyColumns,mapColumns,sumColumns,",");
//		closure.setOutPath(outPath);
		closure.setSumFlag(false); // ���l�͉��Z���Ȃ�
		closure.setCountOption(false); // �퐔�͗v��Ȃ�
		new CommonClojure().incore(closure, srcFile, true);
		// cluster

		String element = closure.getStrElement("cluster", 0);
		int from = closure.getNumElement("cluster", 0);
		int to = closure.getNumElement("cluster", 1);
		System.out.println("cluster " + element + "from:" + from + " to:" + to);
	}

	public static void test3() {
		// ���� �C���f�b�N�X�͗납��n�߂邱�ƁI�I
		// �o�͏��͕ۏႳ��܂���i�ڒ��ڒ��ł��j
		String aliasDir = "Z:/s2/rx/Idxc/Enquete/�����E���C�t�X�^�C����/2007/";
		String pathQTB1 = aliasDir + "QTB3.txt";
		String pathQTB2 = aliasDir + "QTB3.txt";
		String[] fields = { "q1", "q2", "q47", "cluster" };

		// �ȉ�AliasRes�̕����e�X�g�E�E�E

		// ---------------------------------------------------------------------
		// QTB1 incore
		// ---------------------------------------------------------------------
		String outPath = ResControlWeb.getD_Resources_Templates("cnvQTB1.txt");
		String keyColumns="2"; 
		String mapColumns="4,7";
		String sumColumns="8,9";
		MapReducer elmQTB1 = new MapReducer(outPath,keyColumns,mapColumns,sumColumns,",");// key
//		elmQTB1.setOutPath(outPath);

		// strElem
		// ,numElm
		elmQTB1.setCondition(1, "ROOT"); // 1��ڂ�ROOT�Ƀ}�b�`������̈ӊO�͏��O
		elmQTB1.setSumFlag(false); // ���l���ڂ����Z���Ȃ�
		elmQTB1.setCountOption(false); // �퐔�͗v��Ȃ�
		new CommonClojure().incore(elmQTB1, pathQTB1, true);
		// ---------------------------------------------------------------------
		// QTB2 incore
		// ---------------------------------------------------------------------
		String outPath2 = ResControlWeb.getD_Resources_Templates("cnvQTB2.txt");
		String keyColumns2="1"; 
		String mapColumns2="3,4";
		String sumColumns2=null;
		MapReducer elmQTB2 = new MapReducer(outPath2,keyColumns2,mapColumns2,sumColumns2,",");
//		elmQTB2.setOutPath(outPath2);

		elmQTB2.setSumFlag(false); // ���l�͉��Z���Ȃ�
		elmQTB2.setModFlag(true); // �L�[���������ڂ͒ǋL����
		elmQTB2.setCountOption(false); // �퐔�͗v��Ȃ�
		new CommonClojure().incore(elmQTB2, pathQTB2, true);
		// ---------------------------------------------------------------------
		CnvMap[] cnvMap = new CnvMap[fields.length];
		int[] from = new int[fields.length];
		int[] to = new int[fields.length];
		String[] title = new String[fields.length];
		String[] type = new String[fields.length];
		// ---------------------------------------------------------------------
		// �p�s�a���fields�̈ʒu���𒲂ׂ�(���Y�p�����[�^���擾�����)
		// ---------------------------------------------------------------------
		for (int i = 0; i < fields.length; i++) {
			// QTB1
			from[i] = elmQTB1.getNumElement(fields[i], 0);
			to[i] = elmQTB1.getNumElement(fields[i], 1);
			title[i] = elmQTB1.getStrElement(fields[i], 0);
			type[i] = elmQTB1.getStrElement(fields[i], 1);
			System.out.println("field[" + i + "]:" + fields[i] + " type:"
					+ type[i] + " title:" + title[i]);
			System.out.println("from:" + from[i] + " to:" + to[i]);
			// QTB2
			String valArray = elmQTB2.getStrElement(fields[i], 0);
			String tagArray = elmQTB2.getStrElement(fields[i], 1);
			// System.out.println("valArray:"+valArray);
			// System.out.println("tagArray:"+tagArray);
			cnvMap[i] = new CnvMap(tagArray, valArray, null, "\t");
			String[] names = cnvMap[i].getTagArray();
			for (int j = 0; j < names.length; j++) {
				System.out.println("names[" + j + "]:" + names[j]);
			}
		}
	}
}
