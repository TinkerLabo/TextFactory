package kyPkg.etc;

import java.io.*;
import java.util.*;

import kyPkg.converter.ValueChecker;
import kyPkg.uFile.File2Matrix;
import kyPkg.uFile.FileUtil;
import kyPkg.util.*;

/* ----------------------------------------------------------------------------
 �s�g�p��tbean�ɕϊ��������Ȃ邩���m��Ȃ��̂ŁE�E���̌`�ԂɂȂ��Ă���
 FltKanji ftCSV = new FltKanji(); // import kyPkg.filter.*;
 ftCSV.setParmPath(wPath1);
 ftCSV.setDataPath(wPath2);
 ftCSV.setHeader(true);
 ftCSV.setPattern(true);
 String wEndMsg = ftCSV.convert();
 ------------------------------------------------------------------------------- */
public class Cnv_Kanji {
	String outFile = "CHECK.CSV";
	boolean header = false;
	boolean pattern = false;
	String parmPath = ""; // �p�����[�^�t�@�C���̃p�X
	String path = ""; // �f�[�^ �t�@�C���̃p�X
	String delimita = ","; // ��؂蕶�� �E�E�E���ۂɂ͎g���ĂȂ�
	// -------------------------------------------------------------------------
	// ���[�N�ϐ�
	// -------------------------------------------------------------------------
	RangeStk rsObj;
	StreamStk sObj2;
	int wIdx;
	int xPwk; // �J�n�ʒu�J�����g
	int wMax; // �p�����[�^�̍ő�
	String[] xCel; // ��̃R�����g
	int[] xPos; // �J�n�ʒu
	int[] xLen; // ����
	String[] xPtn; // �`�F�b�N�p�^�[��
	String[] xReg; // ���W�b�N�X

	// -------------------------------------------------------------------------
	// �A�N�Z�b�T
	// -------------------------------------------------------------------------
	public void setHeader(boolean header) {
		this.header = header;
	}

	public void setPattern(boolean pattern) {
		this.pattern = pattern;
	}

	public void setDelimita(String delimita) {
		this.delimita = delimita;
	}

	public void setParmPath(String parmPath) {
		this.parmPath = parmPath;
	}

	public void setDataPath(String dataPath) {
		this.path = dataPath;
	}

	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}

	// -------------------------------------------------------------------------
	// �p�����[�^����
	// -------------------------------------------------------------------------
	private void paramAnalyze() {
		wIdx = -1;
		rsObj = new RangeStk(); // �����W�p�����[�^�𕪐́��ۊǂ���N���X

		// ---------------------------------------------------------------------
		// parameter incore
		// ---------------------------------------------------------------------
		Vector pVec = File2Matrix.extract(parmPath);
		if (pVec == null) {
			System.out.println("parameter file Error!!" + parmPath);
			// wIdx = -1�Ȃ�G���[�̏����Ƃ����悤�I�I;
			// return "parameter file Error!!";
		}
		xPwk = 1; // �J�n�ʒu�J�����g
		wMax = pVec.size();
		xCel = new String[wMax];
		xPos = new int[wMax]; // �J�n�ʒu
		xLen = new int[wMax]; // ����
		xPtn = new String[wMax]; // �`�F�b�N�p�^�[��
		for (int i = 0; i < pVec.size(); i++) {
			Object wObj = pVec.get(i);
			if (wObj instanceof Vector) {
				if (((Vector) wObj).size() >= 3) {
					String wStr0 = "", wStr1 = " ", wStr2 = "", wStr3 = "";
					// ---------------------------------------------------------
					// �����ݒ�i�O�Ȃ疳���ƂȂ�j
					// ---------------------------------------------------------
					int wLen = 0;
					wStr2 = ((Vector) wObj).get(2).toString();
					if (wStr2.matches("\\d+"))
						wLen = Integer.parseInt(wStr2);
					if (wLen > 0) {
						// -----------------------------------------------------
						// �t�B�[���h���ݒ�
						// -----------------------------------------------------
						wStr0 = ((Vector) wObj).get(0).toString().trim();
						if (wStr0.equals(""))
							wStr0 = "Cel" + wIdx;
						// -----------------------------------------------------
						// �ʒu�ݒ�i�O�Ȃ璼�O�̍��ڂ̒���Ƃ���j
						// -----------------------------------------------------
						int wPos = 0;
						wStr1 = ((Vector) wObj).get(1).toString();
						if (wStr1.matches("\\d+"))
							wPos = Integer.parseInt(wStr1);
						if (wPos == 0)
							wPos = xPwk;
						wIdx++;
						xCel[wIdx] = wStr0;
						xPos[wIdx] = wPos - 1;
						xLen[wIdx] = wLen;
						// -----------------------------------------------------
						// �����p�^�[���ݒ� ���̏ꍇ �������� == kj
						// -----------------------------------------------------
						if (((Vector) wObj).size() >= 4) {
							wStr3 = ((Vector) wObj).get(3).toString().trim();
							xPtn[wIdx] = wStr3;
						} else {
							xPtn[wIdx] = "";
						}
						xPwk = wPos + wLen;
					}
				}
			}
		}
		wMax = wIdx;
		System.out.println("########## " + wIdx + " ############");
		for (int i = 0; i <= wMax; i++) {
			System.out.print("<<" + i + " ");
			System.out.print(" xCel:" + xCel[i]);
			System.out.print(" xPos:" + xPos[i]);
			System.out.print(" xLen:" + xLen[i]);
			System.out.println(" >>");
		}
	}

	// -------------------------------------------------------------------------
	// �f�[�^�`�F�b�N����ѕϊ�����
	// -------------------------------------------------------------------------
	private void checkNconvert() {
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(path));
			// -----------------------------------------------------------------
			// header
			// -----------------------------------------------------------------
			if (header == true) {
				for (int i = 0; i <= wMax; i++) {
					sObj2.append(outFile, xCel[i]);
				}
				sObj2.write(outFile); // full pattern write!!
			}
			// -----------------------------------------------------------------
			// �����R�����g���w�b�_�[�ɕ\��
			// -----------------------------------------------------------------
			if (pattern == true) {
				for (int i = 0; i <= wMax; i++) {
					sObj2.append(outFile, xPtn[i]);
				}
				sObj2.write(outFile); // full pattern write!!
			}
			// ------------------------------------------------------------------
			// body
			// ------------------------------------------------------------------
			sObj2.resetAll(); // ��U���ׂẴo�b�t�@�[���t���b�V���O�ׁ̈E�E�E
			String wRec = "";
			int wErrCnt = 0;
			while ((wRec = br.readLine()) != null) {
				if (!wRec.trim().equals("")) {
					wErrCnt = 0;
					String wCel = "";
					for (int i = 0; i <= wMax; i++) {
						// -----------------------------------------------------
						// ���R�[�h����pos+len���Z���Ȃ�G���[
						if (wRec.length() >= (xPos[i] + xLen[i])) {
							if (xPtn[i].equals("kj")) {
								// �w��ʒu�ɂ��锼�p����*2��S�p�X�y�[�X�ϊ�����
								wRec = ValueChecker.spKjCnv(wRec, xPos[i],
										xLen[i]);
							}
							wCel = wRec.substring(xPos[i], (xPos[i] + xLen[i]));
						} else {
							wErrCnt++;
							System.out.println("Error ���R�[�h����pos+len���Z��!!");
						}
						sObj2.append(outFile, wCel);
					}
					sObj2.write(outFile); // check�p�������o���I�I
					sObj2.resetAll(); // write!! other types
				}
			}
			br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	// -------------------------------------------------------------------------
	// �O������Ă΂��ϊ�����
	// -------------------------------------------------------------------------
	public String convert() {
		String wEndMsg = "";
		if (new File(parmPath).exists() == false) {
			wEndMsg = "Error �p�����[�^�t�@�C�������݂��Ă��܂���:" + parmPath;
			return wEndMsg;
		}
		if (new File(path).exists() == false) {
			wEndMsg = "Error �f�[�^�t�@�C�������݂��Ă��܂���:" + path;
			return wEndMsg;
		}
		String wPathx[] = path.split("\\."); // ��split �̈����� Regix
		sObj2 = new StreamStk(wPathx[0]); // �f�[�^�p�X�Ɠ��K�w�ɏo��dir�����
		sObj2.addPattern(outFile, delimita); // �o�̓t�@�C���i�g���q�ȗ��I�j
		paramAnalyze(); // �p�����[�^���͏���
		checkNconvert(); // �f�[�^����
		// ---------------------------------------------------------------------
		wEndMsg = "�ϊ��I��";
		// ---------------------------------------------------------------------
		sObj2.closeAll(); // �t�@�C�i���C�Y���ɂ����s�����
		return wEndMsg;
	}
}
