package kyPkg.filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import globals.ResControlWeb;
import kyPkg.etc.MapReducer;
import kyPkg.external.PoiMods;
import kyPkg.uFile.FileUtil;

//Dimennsion�W�v�̌��ʂ�Excel�o�͂���N���[�W��
public class PoiMatrixedClosure implements Inf_BaseClojure {
	private int dimX = -1; // �������������Z���C���f�b�N�X
	private int dimY = -1; // �������������Z���C���f�b�N�X
	private String[] sheetNames = null; // �����o���V�[�g�̖��O
	private int[] valueIndexs = null; // sheetnames�̒����Ɠ����łȂ���΂Ȃ�Ȃ�
	private String[] CaptionX = null; // �Ȃ��Ă��ǂ�
	private String[] CaptionY = null; // �Ȃ��Ă��ǂ�
	private String[] resource = null; // �����\�[�X
	private String NqMonPath = null; // �Ώۃ��j�^�[�i���j
	// ------------------------------------------------------------------------------
	private int baseY = 0;
	private int baseX = 0;
	private PoiMods pMod;
	private FileInputStream fis;
	private String template = ResControlWeb.getD_Resources_Templates("3-GraphTR.xls");
	private String outPath = ResControlWeb.getD_Resources_PUBLIC("workbook.xls");

	public PoiMatrixedClosure(int dimX, int dimY, String[] sheetNames,
			int[] valueIndexs, String[] CaptionX, String[] CaptionY,
			String NqMonPath) {
		this.dimX = dimX;
		this.dimY = dimY;
		this.sheetNames = sheetNames;
		this.valueIndexs = valueIndexs;
		this.CaptionX = CaptionX;
		this.CaptionY = CaptionY;
		this.NqMonPath = NqMonPath;
	}

	@Override
	public void init() {
		pMod = new PoiMods();
		fis = pMod.openTheBook(template); // �u�b�N���J��
		// �p�����[�^�̐������`�F�b�N�������ł���Ă����I
		// �e�V�[�g�ɃR�����g���ڂ���
		for (int j = 0; j < sheetNames.length; j++) {
			pMod.selectSheet(sheetNames[j]); // �V�[�g��I��
			if (CaptionX != null) {
				for (int x = 0; x < CaptionX.length; x++) {
					pMod.setCellValueX(0, x + 1, CaptionX[x]);
				}
			}
			if (CaptionY != null) {
				for (int y = 0; y < CaptionY.length; y++) {
					pMod.setCellValueX(y + 1, 0, CaptionY[y]);
				}
			}
		}
		// ���j�^�[���擾
		int mCount = lineCounter(NqMonPath);
		// ���\�[�X�f�[�^�𖄂ߍ���
		embedRes(resource, mCount);
	}

	@Override
	public void execute(String rec) {
		String[] array = rec.split("\t");
		execute(array);
	}

	@Override
	public void execute(String[] array) {
		int valueIndex = -1;
		if (dimX >= 0 && dimY >= 0) {
			for (int i = 0; i < sheetNames.length; i++) {
				pMod.selectSheet(sheetNames[i]); // �V�[�g��I��
				if (valueIndexs.length > i) {
					valueIndex = valueIndexs[i];
					int x = Integer.parseInt(array[dimX]);
					int y = Integer.parseInt(array[dimY]);
					pMod.setCellValueX(baseY + y, baseX + x, array[valueIndex]);
				}
			}
		}
	}

	@Override
	public void write() {
		pMod.saveAs(outPath);
		try {
			fis.close();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	private void embedRes(String[] res, int mCount) {
		pMod.selectSheet("resource"); // �V�[�g��I��
		pMod.setCellValueS(0, 0, "�Ty��y�vy��y��");
		pMod.selectCell(0, 1); // �Z����I��
		pMod.setCellValue(mCount);
		if (res != null) {
			for (int i = 0; i < res.length; i++) {
				String[] array = res[i].split(",");
				for (int j = 0; j < array.length; j++) {
					pMod.setCellValueX(i + 1, j, array[j]);
				}
			}
		}
	}

	public void setResource(String[] resource) {
		this.resource = resource;
	}

	public void setOutPath(String outPath) {
		this.outPath = outPath;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public void setBaseYX(int baseY, int baseX) {
		setBaseX(baseX);
		setBaseY(baseX);
	}

	public void setBaseX(int baseX) {
		if (baseX > 0)
			this.baseX = baseX - 1;
	}

	public void setBaseY(int baseY) {
		if (baseY > 0)
			this.baseY = baseY - 1;
	}

	public static void main(String[] argv) {
		test0();
	}

	private static void test0() {
		String sys100 = ResControlWeb.getD_Resources_Templates("temp01.txt");
		String sys101 = ResControlWeb.getD_Resources_Templates("MapReducer.txt");
		String keyColumns = "2 ,3 "; // �}�b�s���O�Ɏg�p���鍀��
		String sumColumns = " 4, 5, 6 "; // ���������鍀��
		MapReducer mapRed = new MapReducer(sys101, keyColumns, sumColumns, "\t");
		// mapRed.setOutPath(sys101);
		new CommonClojure().incore(mapRed, sys100, true);
		String[] res = { "�i��,�R�[���Ȃ�", "�Ώۊ���,�Q�O�O�W�N1���P������P�O���P�O��", "�V�C,�J�̂��܂�" };
		String[] top = { "�r�[��", "���A��", "���̑�" };
		String[] side = { "0:00 - 0:59", "1:00 - 1:59", "2:00 - 2:59",
				"3:00 - 3:59", "4:00 - 4:59", "5:00 - 5:59", "6:00 - 6:59",
				"7:00 - 7:59", "8:00 - 8:59", "9:00 - 9:59", "10:00 - 10:59",
				"11:00 - 11:59", "12:00 - 12:59", "13:00 - 13:59",
				"14:00 - 14:59", "15:00 - 15:59", "16:00 - 16:59",
				"17:00 - 17:59", "18:00 - 18:59", "19:00 - 19:59",
				"20:00 - 20:59", "21:00 - 21:59", "22:00 - 22:59",
				"23:00 - 23:59" };
		String xlsTmp = ResControlWeb.getD_Resources_Templates("QPRBASE01.xls");
		String sys200 = ResControlWeb.getD_Resources_Templates("Matrixed.xls");
		String sys103 = ResControlWeb.getD_Resources_Templates("nqmon.txt");
		PoiMatrixedClosure poiMatrix = new PoiMatrixedClosure(0, 1,
				new String[] { "Counts", "Prices", "NumberOfPerson" },
				new int[] { 2, 3, 4 }, top, side, sys103);
		poiMatrix.setResource(res);
		poiMatrix.setTemplate(xlsTmp);
		poiMatrix.setOutPath(sys200);
		poiMatrix.setBaseYX(2, 2);
		new CommonClojure().incore(poiMatrix, sys101, true);
	}

	public static void test1() {
		String sys100 = ResControlWeb.getD_Resources_Templates("temp01.txt");
		String sys101 = ResControlWeb.getD_Resources_Templates("MapReducer.txt");
		String keyColumns = "2 ,3 "; // �}�b�s���O�Ɏg�p���鍀��
		String sumColumns = " 4, 5, 6 "; // ���������鍀��
		MapReducer mapRed = new MapReducer(sys101, keyColumns, sumColumns, "\t");
		// mapRed.setOutPath(sys101);
		new CommonClojure().incore(mapRed, sys100, true);
	}

	public static void test2() {
		String[] res = { "�ΏۃT���v����,5678", "�i��,�R�[���Ȃ�", "�Ώۊ���,�Q�O�O�W�N1���P������P�O���P�O��",
				"�V�C,�J�̂��܂�" };
		String[] top = { "�r�[��", "���A��", "���̑�" };
		String[] side = { "0:00 - 0:59", "1:00 - 1:59", "2:00 - 2:59",
				"3:00 - 3:59", "4:00 - 4:59", "5:00 - 5:59", "6:00 - 6:59",
				"7:00 - 7:59", "8:00 - 8:59", "9:00 - 9:59", "10:00 - 10:59",
				"11:00 - 11:59", "12:00 - 12:59", "13:00 - 13:59",
				"14:00 - 14:59", "15:00 - 15:59", "16:00 - 16:59",
				"17:00 - 17:59", "18:00 - 18:59", "19:00 - 19:59",
				"20:00 - 20:59", "21:00 - 21:59", "22:00 - 22:59",
				"23:00 - 23:59" };
		String sys101 = ResControlWeb.getD_Resources_Templates("MapReducer.txt");
		String xlsTmp = ResControlWeb.getD_Resources_Templates("QPRBASE01.xls");
		String sys200 = ResControlWeb.getD_Resources_Templates("Matrixed.xls");
		String mons = globals.ResControl.getCurDir() + "nqmons.txt";

		PoiMatrixedClosure poiMatrix = new PoiMatrixedClosure(0, 1,
				new String[] { "Counts", "Prices", "NumberOfPerson" },
				new int[] { 2, 3, 4 }, top, side, mons);
		poiMatrix.setResource(res);
		poiMatrix.setTemplate(xlsTmp);
		poiMatrix.setOutPath(sys200);
		poiMatrix.setBaseYX(2, 2);
		new CommonClojure().incore(poiMatrix, sys101, true);
	}

	// -------------------------------------------------------------------------
	// �^����ꂽ�Ώۃ��j�^�[�t�@�C���̃��C�����J�E���g����i���j�E�E�����g���ł��Ȃ����낤���H�H
	// -------------------------------------------------------------------------
	private int lineCounter(String path) {
		int counter = -1; // �T���v�����Ȃ̂�int�𒴂��邱�Ƃ͂Ȃ����낤�E�E�E
		System.out.println("Flt_Mixer@incore  path:" + path);
		File file_L = new File(path);
		if (!file_L.isFile()) {
			System.out.println("#error not a File=>" + path);
			return counter;
		}
		if (!file_L.canRead()) {
			System.out.println("#error File can not read =>" + path);
			return counter;
		}
		String wRec = "";
		try {
			BufferedReader br_L = FileUtil.getBufferedReader(path);
//			BufferedReader br_L = new BufferedReader(new FileReader(path));
			while ((wRec = br_L.readLine()) != null) {
				if (!wRec.trim().equals(""))
					counter++;
			}
			br_L.close();
		} catch (IOException ie) {
			ie.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return counter;
	}

}
