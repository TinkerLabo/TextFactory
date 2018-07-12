package kyPkg.vbMods;

import java.io.File;

import static kyPkg.sql.ISAM.CHARACTER_SET_OEM;
import static kyPkg.sql.ISAM.COL_NAME_HEADER_FALSE;
import static kyPkg.sql.ISAM.FORMAT_CSV_DELIMITED;
import static kyPkg.sql.ISAM.MAX_SCAN_ROWS_0;
import static kyPkg.sql.ISAM.SCHEMA_INI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import kyPkg.uFile.FileUtil;

//20110629 yuasa 
public class Schemes extends VB_Base {

	public static void main(String[] argv) {
		testSchemeRmv();
	}

	public static void testSchemeRmv() {
		String userDir = globals.ResControl.getQPRHome();
		SchemeRmv(userDir + "TEMP/T01.TXT", true);
	}

	public static int SchemeRmv(String targetPath) {
		return SchemeRmv(targetPath, true);
	}

	public static int SchemeRmv(String targetPath, boolean killOption) {
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// 概要:SchemeCpyの子プログラム;
		// 出パス上のスキーマファイルから指定されたファイルの情報のみを外す;
		// また出力ファイルが存在した場合削除する;
		// 引数: pParm :対象スキーマ名;
		// pKill :削除するかどうか;
		// 戻り値: 出力ライン数;
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// ※出力パスおよび出力先のスキーマが存在しない場合は作成されます;
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// 使用例:[ASM.TXT] QTB2.TXT;
		// ? SchemeRmv("C:\" + gUSER + "\@QBR\TEMPS\ZAP2.TXT",true);
		// ? SchemeRmv("C:\" + gUSER + "\@QBR\TEMPS\TEST1.TXT",false);
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// if (!isExists(targetPath))
		// return -1;
		targetPath = FileUtil.normarizeIt(targetPath);
		int pos = InStr_R(targetPath, "/");
		if (pos == 0)
			return -1;
		String outDir = Left(targetPath, pos);
		kyPkg.uFile.FileUtil.makedir(outDir);
		String fileName = Mid(targetPath, pos + 1);
		// System.out.println("debug SchemeRmv>> targetPath  :" + targetPath);
		// System.out.println("debug SchemeRmv>> outDir  :" + outDir);
		// System.out.println("debug SchemeRmv>> fileName:" + fileName);
		if (killOption == true) {
			kill(targetPath);
		}
		String schemaPath = outDir + SCHEMA_INI;
		String path = outDir + "Schema.OLD";
		if (!new File(schemaPath).isFile()) {
			System.out.println(
					"#info# @SchemeRmv schemaPath not Existed :" + schemaPath);
			return -1;
		}
		rename(path, schemaPath); // 入力ファイルをテンポラリに名称変更
		String keyword = ((String) "[" + fileName + "]").toUpperCase();
		// System.out.println("debug SchemeRmv>> keyword:" + keyword);

		boolean flag = false;
		int count = 0;
		try {
			String wRec;
			BufferedReader br = FileUtil.getBufferedReader(path);
			//			BufferedReader br = new BufferedReader(new FileReader(path));
			if (br == null) {
				return -1;
			}
			BufferedWriter bw = new BufferedWriter(
					new FileWriter(schemaPath, false));
			if (bw == null) {
				br.close();
				return -1;
			}
			while (br.ready()) {
				wRec = br.readLine();
				// System.out.println("debug SchemeRmv>> wRec:" + wRec);

				count = count + 1;
				if (wRec.startsWith("[")) {
					wRec = wRec.trim();
					wRec = wRec.toUpperCase();
					// System.out.println("debug SchemeRmv>> wRec#" + wRec);
					if (wRec.equals(keyword)) {
						flag = true;
					} else {
						flag = false;
					}
				}
				if (flag == false) {
					bw.write(wRec);
					bw.write(LF);
				}
			}
			br.close();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		kill(path);
		return count;
	}

	public void Schema3(String pDir) {
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// 概要 : Schema3 FOR Pyramid;
		// 引数 出力先ディレクトリパス;
		// 戻り値 :
		// 使用例 : call Schema3("c:");
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		int i, j;
		String wSeq;
		String path = pDir + "/" + SCHEMA_INI;
		SchemeRmv(path + "/" + "RESULT.TXT", true);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(path, false));
			if (bw == null)
				return;
			bw.write("[RESULT.TXT]               " + LF);
			bw.write(COL_NAME_HEADER_FALSE + LF);
			bw.write(FORMAT_CSV_DELIMITED + LF);
			bw.write(MAX_SCAN_ROWS_0 + LF);
			bw.write(CHARACTER_SET_OEM + LF);
			bw.write("Col1=" + DQ + "BCode" + DQ + " Char Width 4 " + LF);
			bw.write("Col2=" + DQ + "ID" + DQ + " Char  Width 15" + LF);
			bw.write("Col3=" + DQ + "COGNITIVE" + DQ + " Char Width 1" + LF);
			for (i = 1; i <= 25; i++) {
				wSeq = Right("00" + i, 2);
				bw.write("Col" + i + 3 + "=" + DQ + "P" + wSeq + DQ
						+ " float " + LF);
			} // Next i;
			for (j = 1; j <= 5; j++) {
				bw.write("Col" + (i + j + 2) + "=" + DQ + "bk" + j + DQ
						+ " float " + LF);
			} // Next j;
			wSeq = Right("00" + i, 2);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	public static void SchemaAx(String pDir) {
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// 概要 : SchemaAx FOR 抽出条件（条件にマッチしたID群）;
		// 引数 出力先ディレクトリパス;
		// 戻り値 :
		// 使用例 : call SchemaAx("c:");
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String path = pDir;
		SchemeRmv(path + "/" + "CALQ.TXT", true);
		SchemeRmv(path + "/" + "AX.TXT", false);
		SchemeRmv(path + "/" + "AX1.TXT", false);
		SchemeRmv(path + "/" + "AX2.TXT", false);
		path = path + "/" + SCHEMA_INI;
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
			if (bw == null)
				return;
			bw.write("[AX.TXT]               " + LF);
			bw.write(COL_NAME_HEADER_FALSE + LF);
			bw.write(FORMAT_CSV_DELIMITED + LF);
			bw.write(MAX_SCAN_ROWS_0 + LF);
			bw.write(CHARACTER_SET_OEM + LF);
			bw.write("Col1=" + DQ + "ID" + DQ + " Char Width 15 " + LF);
			bw.write("[AX1.TXT]               " + LF);
			bw.write(COL_NAME_HEADER_FALSE + LF);
			bw.write(FORMAT_CSV_DELIMITED + LF);
			bw.write(MAX_SCAN_ROWS_0 + LF);
			bw.write(CHARACTER_SET_OEM + LF);
			bw.write("Col1=" + DQ + "ID" + DQ + " Char Width 15 " + LF);
			bw.write("[AX2.TXT]               " + LF);
			bw.write(COL_NAME_HEADER_FALSE + LF);
			bw.write(FORMAT_CSV_DELIMITED + LF);
			bw.write(MAX_SCAN_ROWS_0 + LF);
			bw.write(CHARACTER_SET_OEM + LF);
			bw.write("Col1=" + DQ + "ID" + DQ + " Char Width 15 " + LF);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	public void SchemaQTB(String pDir) {
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// 概要 : SchemaQTB;
		// 引数 出力先ディレクトリパス;
		// 戻り値 :
		// 使用例 : call SchemaQTB("c:");
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String path = pDir;
		SchemeRmv(path + "/" + "QTB1.TXT", false);
		SchemeRmv(path + "/" + "QTB2.TXT", false);
		path = path + "/" + SCHEMA_INI;
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
			if (bw == null)
				return;
			bw.write("[QTB1.TXT]                  " + LF);
			bw.write(COL_NAME_HEADER_FALSE + LF);
			bw.write(FORMAT_CSV_DELIMITED + LF);
			bw.write(MAX_SCAN_ROWS_0 + LF);
			bw.write(CHARACTER_SET_OEM + LF);
			bw.write("Col1=" + DQ + "Srt" + DQ + " Char    Width 60 " + LF);
			bw.write("Col2=" + DQ + "Mot" + DQ + " Char    Width 60 " + LF);
			bw.write("Col3=" + DQ + "Key" + DQ + " Char    Width 60 " + LF);
			bw.write("Col4=" + DQ + "Val" + DQ + " Char    Width 60 " + LF);
			bw.write("Col5=" + DQ + "Nam" + DQ + " Char    Width 60 " + LF);
			bw.write("Col6=" + DQ + "Max" + DQ + " Integer  " + LF);
			bw.write("Col7=" + DQ + "Occ" + DQ + " Integer  " + LF);
			bw.write("Col8=" + DQ + "Typ" + DQ + " Char    Width 10 " + LF);
			bw.write("Col9=" + DQ + "Col" + DQ + " Integer  " + LF);
			bw.write("Col10=" + DQ + "Len" + DQ + " Integer  " + LF);
			bw.write("[QTB2.TXT]                  " + LF);
			bw.write(COL_NAME_HEADER_FALSE + LF);
			bw.write(FORMAT_CSV_DELIMITED + LF);
			bw.write(MAX_SCAN_ROWS_0 + LF);
			bw.write(CHARACTER_SET_OEM + LF);
			bw.write("Col1=" + DQ + "Srt" + DQ + " Char    Width 60 " + LF);
			bw.write("Col2=" + DQ + "Mot" + DQ + " Char    Width 60 " + LF);
			bw.write("Col3=" + DQ + "Key" + DQ + " Char    Width 60 " + LF);
			bw.write("Col4=" + DQ + "Val" + DQ + " Char    Width 60 " + LF);
			bw.write("Col5=" + DQ + "Nam" + DQ + " Char    Width 60 " + LF);
			bw.write("Col6=" + DQ + "Max" + DQ + " Integer  " + LF);
			bw.write("Col7=" + DQ + "Occ" + DQ + " Integer  " + LF);
			bw.write("Col8=" + DQ + "Typ" + DQ + " Char    Width 10 " + LF);
			bw.write("Col9=" + DQ + "Col" + DQ + " Integer  " + LF);
			bw.write("Col10=" + DQ + "Len" + DQ + " Integer  " + LF);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	public void SchemaCross(String pPth) {
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// 概要 : SchemaCross FOR Pyramid;
		// 引数 出力先ディレクトリパス;
		// 戻り値 :
		// 使用例 : call SchemaCross("c:");
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String path;
		path = pPth;
		path = path + "/" + SCHEMA_INI;
		SchemeRmv(path + "/" + "CALQ.TXT", true);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
			if (bw == null)
				return;
			bw.write("[CALQ.TXT]               " + LF);
			bw.write(COL_NAME_HEADER_FALSE + LF);
			bw.write(FORMAT_CSV_DELIMITED + LF);
			bw.write(MAX_SCAN_ROWS_0 + LF);
			bw.write(CHARACTER_SET_OEM + LF);
			bw.write("Col1=" + DQ + "ID" + DQ + " Char Width 15 " + LF);
			bw.write("Col2=" + DQ + "F01" + DQ + " Char Width 15" + LF);
			bw.write("Col3=" + DQ + "F02" + DQ + " Char Width 15" + LF);
			bw.write("Col4=" + DQ + "CNT" + DQ + " float Width 8  " + LF);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	public static void SchemaMLT(String pDir, String pTbl, String pKey) {
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// 概要 : SchemaMLT FOR MultiAnswer;
		// 引数 出力先ディレクトリパス;
		// 戻り値 :
		// 使用例 : call SchemaMLT("c:","T01AQA","AQA");
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String path = pDir;
		SchemeRmv(path + "/" + pTbl + ".TXT", true);
		path = path + "/" + SCHEMA_INI;
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
			if (bw == null)
				return;
			bw.write("[" + pTbl + ".TXT]               " + LF);
			bw.write(COL_NAME_HEADER_FALSE + LF);
			bw.write(FORMAT_CSV_DELIMITED + LF);
			bw.write(MAX_SCAN_ROWS_0 + LF);
			bw.write(CHARACTER_SET_OEM + LF);
			bw.write("Col1=" + DQ + "ID" + DQ + " Char Width 15 " + LF);
			bw.write("Col2=" + DQ + pKey + DQ + " Char Width 10 " + LF);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	public static int SchemeCpy(String expParm1, String pParm2) {
		return SchemeCpy(expParm1, pParm2, true);
	}

	public static int SchemeCpy(String expParm1, String pParm2, boolean pCopy) {
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// 概要: 入力パス上のスキーマファイルから指定されたファイルの情報のみを抜き出し出力先に追い書きする;
		// （※該当ファイルのコピーも同時に行う）;
		// 引数: xInput :入力パス;
		// xOutput :出力パス;
		// 戻り値: 出力ライン数;
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// ※出力パスおよび出力先のスキーマが存在しない場合は作成されます;
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// 使用例:[ASM.TXT] QTB2.TXT;
		// Print SchemeCpy("C:\\" + gUSER + "\\@QBR\\PowerB\\T1090" +
		// "\\QTB2.TXT", "C:\\" + gUSER + "\\@QBR\\TEMPS" + "\\ZAP2.TXT");
		// Print SchemeCpy("C:\\" + gUSER + "\\@QBR\\PowerB\\T1090" +
		// "\\ASM.TXT", "C:\\" + gUSER + "\\@QBR\\TEMPS" + "\\TEST1.TXT");
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String pParm1;
		String pPath1;
		String pPath2;
		String pName1;
		String pName2;
		int xPoz;
		pParm1 = expParm1;
		// if (IsMissing(pCopy) == true) pCopy = true;
		xPoz = InStr_R(pParm1, "\\");
		if (xPoz == 0)
			return -1;// Exit Function;
		pPath1 = Left(pParm1, xPoz - 1);
		pName1 = Mid(pParm1, xPoz + 1);
		xPoz = InStr_R(pParm2, "\\");
		if (xPoz == 0)
			return -1;// Exit Function;
		pPath2 = Left(pParm2, xPoz - 1);
		pName2 = Mid(pParm2, xPoz + 1);
		String xChr;
		boolean xFlg;
		xFlg = false;
		// Err.Clear();
		// SchemeCpy = 0;
		// **< alloc cFileIO
		// >*********************************************************************************
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String xPath_I;
		String xPath_O;
		String xName1;
		String xRec;
		int xLct;
		String path;//i_Schema
		String oPath;//o_Schema
		String i_File;
		String o_File;
		xPath_I = pPath1;
		xPath_O = pPath2;
		if (!xPath_I.endsWith("\\"))
			xPath_I = xPath_I + "\\";
		if (!xPath_O.endsWith("\\"))
			xPath_O = xPath_O + "\\";
		path = xPath_I + SCHEMA_INI;
		oPath = xPath_O + SCHEMA_INI;
		if (pName1.equals("ASM")) {
			pName1 = pName1 + ".TXT";
		}
		i_File = xPath_I + pName1;
		o_File = xPath_O + pName2;
		if (!new File(i_File).isFile()) {
			MsgBox("ERROR!! FILE NOT FOUND=>" + i_File);
			// SchemeCpy = -1;
			return -1;// Exit Function;
		}
		SchemeRmv(xPath_O + "\\" + pName2, pCopy);
		if (!new File(xPath_O).isDirectory())
			MakeDir(xPath_O, false);
		xName1 = "[" + pName1 + "]";
		xLct = 0;
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
			//			BufferedReader br = new BufferedReader(new FileReader(path));
			if (br == null)
				return -1;// Exit Function;
			BufferedWriter bw = new BufferedWriter(new FileWriter(oPath, true));
			if (bw == null) {
				br.close();
				return -1;// Exit Function;
			}
			while (br.ready()) {
				xRec = br.readLine();
				xChr = Left(xRec, 1);
				if (xChr.equals("[")) {
					if (xName1 == Trim(xRec)) {
						xFlg = true;
						xRec = "[" + pName2 + "]";
					} else {
						xFlg = false;
					}
				}
				if (xFlg == true) {
					xLct = xLct + 1;
					bw.write(xRec + LF);
				}
			} // Loop End ;
			br.close();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int rtn = 0;
		if (xLct != 0) {
			if (pCopy == true) {
				if (!new File(i_File).isFile()) {
					MsgBox("error " + i_File + "が見つかりませんでした");
					return -1;// Exit Function;
				}
				if (new File(o_File).isFile())
					Kill(o_File);
				FileCopy(i_File, o_File);
			}
			rtn = xLct;
		} else {
			rtn = -1;
		}
		return rtn;// Exit Function;
		// //---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// SchemeCpy_Err:
		// String xSelect = Err; if(false){ ;
		// } else if ( xSelect.equals(53: MsgBox("エラー 53: ファイルが見つかりません。")) ){
		// } else if ( xSelect.equals(} else {: MsgBox("on ooRDO_Err :" + Err +
		// " が発生しました" + Error(Err))) ){
		// }// end select;
		// return;// Exit Function;
	} // <function>;

}