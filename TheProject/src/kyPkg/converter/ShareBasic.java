package kyPkg.converter;

import java.util.HashMap;
import java.util.List;

import kyPkg.etc.CalcUtil;

abstract class ShareBasic extends DefaultConverter {
	protected int keyCell;
	protected List<Integer> shareCols;
	protected HashMap<Integer, Double> motherMap;

	// ------------------------------------------------------------------------
	// �y�T�v�z shareCols�Ŏ����ꂽ�J�����̒l�ƕꐔ�}�b�v��̒l�i�����v���R�[�h�j�ŃV�F�A�v�Z���s��
	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// shareCols�F�V�F�A�v�Z����J���������X�g�œn���inull�Ȃ�v�Z���Ȃ��j
	// ------------------------------------------------------------------------
	public ShareBasic(int keyCell, List<Integer> shareCols) {
		super();
		this.motherMap = null;
		this.keyCell = keyCell;
		this.shareCols = shareCols;
	}

	@Override
	public void init() {
		this.motherMap = null;
	}

	// ------------------------------------------------------------------------
	// ���v�s�̏��� �F �����v�s�̏���(�V�G�A�v�Z����s�̍��v���}�b�v�Ɋi�[����)
	// ------------------------------------------------------------------------
	protected String[] incoreMotherVal(String[] recs) {
		// �^�[�Q�b�g�J�������Ƃ̕ꐔ���}�b�v�Ɋi�[����@���@�}�b�v���J�����A�ꐔ��
		motherMap = new HashMap();
		if (shareCols != null && shareCols.size() > 0) {
			for (Integer colObj : shareCols) {
				int col = colObj.intValue();
				if (recs.length > col) {
					try {
						Double val = Double.parseDouble(recs[col]);
//						System.out.println("debug@incoreMotherVal key:" + col
//								+ " val:" + val);
						motherMap.put(col, val);

					} catch (Exception e) {
						System.out.println("Error @incoreMotherVal.parseDouble " + " val:" + recs[col] + "col:" + col);
						e.printStackTrace();
						// TODO: handle exception
					}

				}
				// setShare(recs, intObj);
			}
		}
		return recs;
	}

	protected String[] calcShare(String[] recs) {
		if (shareCols != null) {
			for (Integer targetCol : shareCols) {
				recs = setShare(recs, targetCol.intValue());
			}
		}
		return recs;
	}

	// ------------------------------------------------------------------------
	// �^�[�Q�b�g�J�����̒l�����̗�̕ꐔ�ŃV�F�A�v�Z����i�����_��R�ʂ̐��x�j
	// ------------------------------------------------------------------------
	protected String[] setShare(String[] recs, int shareCol) {
		Double motherObj = motherMap.get(shareCol);
		if (motherObj != null && motherObj.doubleValue() > 0) {
			try {
				double val = Double.parseDouble(recs[shareCol]);
				double mother = motherObj.doubleValue();
				double share = CalcUtil.calcShare(val, mother, 3);
				recs[shareCol] = String.valueOf(share);
			} catch (Exception e) {
				recs[shareCol] = "Share NG(1)";
			}
		} else {
			recs[shareCol] = "Share NG(2)";
		}
		return recs;
	}

}