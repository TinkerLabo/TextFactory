package kyPkg.etc;

import java.io.*;
import java.util.*;

import kyPkg.converter.ValueChecker;
import kyPkg.uFile.File2Matrix;
import kyPkg.uFile.FileUtil;
import kyPkg.util.*;

/* ----------------------------------------------------------------------------
 《使用例》beanに変換したくなるかも知れないので・・この形態になっている
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
	String parmPath = ""; // パラメータファイルのパス
	String path = ""; // データ ファイルのパス
	String delimita = ","; // 区切り文字 ・・・実際には使われてない
	// -------------------------------------------------------------------------
	// ワーク変数
	// -------------------------------------------------------------------------
	RangeStk rsObj;
	StreamStk sObj2;
	int wIdx;
	int xPwk; // 開始位置カレント
	int wMax; // パラメータの最大
	String[] xCel; // 列のコメント
	int[] xPos; // 開始位置
	int[] xLen; // 長さ
	String[] xPtn; // チェックパターン
	String[] xReg; // レジックス

	// -------------------------------------------------------------------------
	// アクセッサ
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
	// パラメータ分析
	// -------------------------------------------------------------------------
	private void paramAnalyze() {
		wIdx = -1;
		rsObj = new RangeStk(); // レンジパラメータを分析＆保管するクラス

		// ---------------------------------------------------------------------
		// parameter incore
		// ---------------------------------------------------------------------
		Vector pVec = File2Matrix.extract(parmPath);
		if (pVec == null) {
			System.out.println("parameter file Error!!" + parmPath);
			// wIdx = -1ならエラーの処理とさせよう！！;
			// return "parameter file Error!!";
		}
		xPwk = 1; // 開始位置カレント
		wMax = pVec.size();
		xCel = new String[wMax];
		xPos = new int[wMax]; // 開始位置
		xLen = new int[wMax]; // 長さ
		xPtn = new String[wMax]; // チェックパターン
		for (int i = 0; i < pVec.size(); i++) {
			Object wObj = pVec.get(i);
			if (wObj instanceof Vector) {
				if (((Vector) wObj).size() >= 3) {
					String wStr0 = "", wStr1 = " ", wStr2 = "", wStr3 = "";
					// ---------------------------------------------------------
					// 長さ設定（０なら無効となる）
					// ---------------------------------------------------------
					int wLen = 0;
					wStr2 = ((Vector) wObj).get(2).toString();
					if (wStr2.matches("\\d+"))
						wLen = Integer.parseInt(wStr2);
					if (wLen > 0) {
						// -----------------------------------------------------
						// フィールド名設定
						// -----------------------------------------------------
						wStr0 = ((Vector) wObj).get(0).toString().trim();
						if (wStr0.equals(""))
							wStr0 = "Cel" + wIdx;
						// -----------------------------------------------------
						// 位置設定（０なら直前の項目の直後とする）
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
						// 処理パターン設定 この場合 漢字処理 == kj
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
	// データチェックおよび変換処理
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
			// 検査コメントをヘッダーに表示
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
			sObj2.resetAll(); // 一旦すべてのバッファーをフラッシュ念の為・・・
			String wRec = "";
			int wErrCnt = 0;
			while ((wRec = br.readLine()) != null) {
				if (!wRec.trim().equals("")) {
					wErrCnt = 0;
					String wCel = "";
					for (int i = 0; i <= wMax; i++) {
						// -----------------------------------------------------
						// レコード長がpos+lenより短いならエラー
						if (wRec.length() >= (xPos[i] + xLen[i])) {
							if (xPtn[i].equals("kj")) {
								// 指定位置にある半角文字*2を全角スペース変換する
								wRec = ValueChecker.spKjCnv(wRec, xPos[i],
										xLen[i]);
							}
							wCel = wRec.substring(xPos[i], (xPos[i] + xLen[i]));
						} else {
							wErrCnt++;
							System.out.println("Error レコード長がpos+lenより短い!!");
						}
						sObj2.append(outFile, wCel);
					}
					sObj2.write(outFile); // check用を書き出す！！
					sObj2.resetAll(); // write!! other types
				}
			}
			br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	// -------------------------------------------------------------------------
	// 外部から呼ばれる変換処理
	// -------------------------------------------------------------------------
	public String convert() {
		String wEndMsg = "";
		if (new File(parmPath).exists() == false) {
			wEndMsg = "Error パラメータファイルが存在していません:" + parmPath;
			return wEndMsg;
		}
		if (new File(path).exists() == false) {
			wEndMsg = "Error データファイルが存在していません:" + path;
			return wEndMsg;
		}
		String wPathx[] = path.split("\\."); // ※split の引数は Regix
		sObj2 = new StreamStk(wPathx[0]); // データパスと同階層に出力dirを作る
		sObj2.addPattern(outFile, delimita); // 出力ファイル（拡張子省略可！）
		paramAnalyze(); // パラメータ分析処理
		checkNconvert(); // データ処理
		// ---------------------------------------------------------------------
		wEndMsg = "変換終了";
		// ---------------------------------------------------------------------
		sObj2.closeAll(); // ファイナライズ時にも実行される
		return wEndMsg;
	}
}
