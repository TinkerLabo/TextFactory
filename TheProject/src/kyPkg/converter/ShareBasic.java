package kyPkg.converter;

import java.util.HashMap;
import java.util.List;

import kyPkg.etc.CalcUtil;

abstract class ShareBasic extends DefaultConverter {
	protected int keyCell;
	protected List<Integer> shareCols;
	protected HashMap<Integer, Double> motherMap;

	// ------------------------------------------------------------------------
	// 【概要】 shareColsで示されたカラムの値と母数マップ上の値（総合計レコード）でシェア計算を行う
	// ------------------------------------------------------------------------
	// コンストラクタ
	// shareCols：シェア計算するカラムをリストで渡す（nullなら計算しない）
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
	// 合計行の処理 ： 総合計行の処理(シエア計算する行の合計をマップに格納する)
	// ------------------------------------------------------------------------
	protected String[] incoreMotherVal(String[] recs) {
		// ターゲットカラムごとの母数をマップに格納する　→　マップ＜カラム、母数＞
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
	// ターゲットカラムの値をその列の母数でシェア計算する（小数点第３位の精度）
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