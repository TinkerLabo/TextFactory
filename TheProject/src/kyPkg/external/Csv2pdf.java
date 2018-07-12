package kyPkg.external;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.BaseFont;
import java.io.*;
import java.util.*;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.table.*;

//-----------------------------------------------------------------------------
//import javax.swing.table.DefaultTableModel;
//import java.util.*;
//import java.io.*;
//import java.text.*;
//-----------------------------------------------------------------------------
/*
 ＰＤＦ出力関連クラス
 @quthor	 Ken Yuasa
 @version	Version.1.0
 */
public class Csv2pdf {
	/*-------------------------------------------------------------------------
	 * メイン（単体テスト用）
	 * @quthor	 Ken Yuasa
	 * @version	Version.1.0
	-------------------------------------------------------------------------*/
	public static void main(String[] argv) {
		String inPath;
		String outPath;
		inPath = argv[0];
		outPath = "zzzz.pdf";
		System.out.println("\n-----------------------------------------");
		Vector row = file2vector(inPath);
		vector2pdf(row, outPath, "　ド　キ　ュ　メ　ン　ト　の　タ　イ　ト　ル　");
		System.out.println("\n-----------------------------------------");
	}

	/***************************************************************************
	 * 《 tmdl2pdf 》 <BR>
	 * テーブルモデル中のデータをＰＤＦファイルに書き出す
	 * 
	 * @quthor Ken Yuasa
	 * @version Version 1.0
	 * @parameter pOutPath ファイルの出力先パス
	 * @parameter pTmdl 入力データ（テーブルモデル）
	 * @parameter pHead 要ヘッダーかどうか 《使用例》 tmdl2pdf(pOutPath,TableModel
	 *            pTmdl,true,"たいとー");
	 ***************************************************************************/
	public int tmdl2pdf(String pOutPath, TableModel pTmdl, boolean headOption,
			String title, int colWidths[]) {
		System.out.println("■ tmdl2pdf ■■■■■■■■■■■■■■■■■■■");
		if (title.trim().equals(""))
			title = "タイトル未設定";
		boolean pVertical = false;
		Document document; // Ｄｏｃｕｍｅｎｔオブジェクト
		if (pVertical == true) {
			document = new Document(PageSize.A4); // A4縦
		} else {
			document = new Document(PageSize.A4.rotate()); // A4横
		}
		int wCnt = 0;
		int columnCount = pTmdl.getColumnCount(); // カラム数
		int wRow = pTmdl.getRowCount(); // 行数
		System.out.println("■ columnCount:" + columnCount);
		System.out.println("■ wRow:" + wRow);
		// String wLs = System.getProperty("line.separator");
		// StringBuffer sBuf = new StringBuffer();
		try {
			File wFile = new File(pOutPath);
			if (wFile.exists()) {
				wFile.delete();
				System.err.println(pOutPath + " is exists");
				if (wFile.exists()) {
					System.err.println(pOutPath + " Can't Delete...sorry!!");
					System.exit(0);
				}
			}
			// -----------------------------------------------------------------
			// PdfWriter生成
			// -----------------------------------------------------------------
			PdfWriter.getInstance(document, new FileOutputStream(pOutPath));
			// -----------------------------------------------------------------
			// フォントの設定　　　　　※iTextAsian.jar を併用しないとエラーになります
			// -----------------------------------------------------------------
			// BaseFont bf1 = BaseFont.createFont("HeiseiMin-W3",
			// "UniJIS-UCS2-HW-H",false); // 明朝
			BaseFont bf2 = BaseFont.createFont("HeiseiKakuGo-W5",
					"UniJIS-UCS2-H", false); // ゴシック
			// Font fMin24 = new Font(bf1, 24);
			// Font fGot9 = new Font(bf2, 4);
			// Font fGot7 = new Font(bf2, 4);
			Font fGot4 = new Font(bf2, 4);
			Font fGot12U = new Font(bf2, 12, Font.UNDERLINE | Font.ITALIC
					| Font.BOLD);
			// -----------------------------------------------------------------
			// WindowsMSゴシック
			// BaseFont bf3 = BaseFont.createFont(
			// "c:/winnt/fonts/msgothic.ttc,1", BaseFont.IDENTITY_H,
			// BaseFont.EMBEDDED);
			// -----------------------------------------------------------------
			// Header
			// -----------------------------------------------------------------
			if (headOption == true) {
				HeaderFooter header = new HeaderFooter(new Phrase("Page:",
						fGot4), true);
				header.setAlignment(Element.ALIGN_RIGHT);
				header.setBorder(Rectangle.NO_BORDER);
				document.setHeader(header);
			}
			// -----------------------------------------------------------------
			// Footer
			// -----------------------------------------------------------------
			HeaderFooter footer = new HeaderFooter(new Phrase("東急エージェンシー",
					fGot4), false);
			footer.setBorder(Rectangle.NO_BORDER);
			footer.setAlignment(Element.ALIGN_CENTER);
			document.setFooter(footer);
			// -----------------------------------------------------------------
			// ドキュメントのOPEN
			// -----------------------------------------------------------------
			document.open();
			// -----------------------------------------------------------------
			// タイトル(一番最初のパラグラフ)※アンダーラインを入れてみよう！
			// -----------------------------------------------------------------
			Paragraph para = new Paragraph(title, fGot12U);
			para.setAlignment(Element.ALIGN_CENTER);
			// para.setGrayFill(0.9f);
			document.add(para);
			// -----------------------------------------------------------------
			// テーブルヘッダー処理（テーブル・オブジェクトの生成）
			// ヘッダー列の文字数によって列は幅を仮設定・・・
			// -----------------------------------------------------------------
			// int colWidths[]
			if (colWidths == null) {
				colWidths = new int[columnCount];
				for (int j = 0; j < columnCount; j++) {
					colWidths[j] = (pTmdl.getColumnName(j)).length();
					if (j == 0)
						colWidths[0] = 50;
				}
			}

			int total = 0;
			for (int j = 0; j < columnCount; j++) {
				total = total + colWidths[j];
			}
			// 列幅を％で指定するための計算
			for (int j = 0; j < columnCount; j++) {
				colWidths[j] = (colWidths[j] * 100) / total;
			}
			// -----------------------------------------------------------------
			// ※列幅はパーセントで指定する
			// -----------------------------------------------------------------
			Table aTable = new Table(columnCount);
			aTable.setDefaultHorizontalAlignment(Element.ALIGN_LEFT);
			// -------------------------------------------------------------
			// 中央揃えならElement.ALIGN_CENTER
			// aTable.setDefaultVerticalAlignment(Element.ALIGN_MIDDLE);
			// -------------------------------------------------------------
			aTable.setPadding(2);
			aTable.setWidths(colWidths); // 各カラムの幅を設定 （パーセント）
			aTable.setWidth(100); // テーブル全体の大きさを設定（パーセント）
			aTable.endHeaders(); // 頁をまたがってもカラム名を表示する
			// for(int j = 0;j<wCol;j++){
			// System.out.println("■#2 pTmdl.getColumnName(j):"+pTmdl.getColumnName(j));
			// aTable.addCell(pTmdl.getColumnName(j)); // カラム名
			// }
			for (int j = 0; j < columnCount; j++) {
				Cell cellX = new Cell(new Phrase(pTmdl.getColumnName(j), fGot4));
				aTable.addCell(cellX); // カラム名
				// aTable.addCell((String)col.get(j)); // カラム名
			}
			String wStr = "";
			for (int i = 0; i < wRow; i++) {
				for (int j = 0; j < columnCount; j++) {
					Object obj = pTmdl.getValueAt(i, j);
					if (obj != null) {
						wStr = obj.toString();
					} else {
						wStr = "　";
					}
					Cell cell2 = new Cell(new Phrase(wStr, fGot4));
//					if (j > 0)
//						cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
					if ((i % 2) == 0)
						cell2.setGrayFill(0.9f); // 背景をグレーで塗りつぶす
					aTable.addCell(cell2);
				}
			}
			// -----------------------------------------------------------------
			// ドキュメントにテーブルを追加する！！
			// -----------------------------------------------------------------
			document.add(aTable);
		} catch (DocumentException de) {
			de.printStackTrace();
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.err.println(ioe.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		document.close(); /* ドキュメントのＣＬＯＳＥ */
		return wCnt;
	} // tmdl2pdf

	/*-------------------------------------------------------------------------
	 * ファイルをＶｅｃｔｏｒに変換する
	 * @quthor	 Ken Yuasa
	 * @version	Version.1.0
	 * @parameter	pInput 入力データのパス
	-------------------------------------------------------------------------*/
	public static Vector file2vector(String path) {
		Vector row = new Vector();
		Vector col = new Vector();
		String wRec = "";
		String wCel = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));
			while ((wRec = br.readLine()) != null) {
				System.out.println(wRec);
				StringTokenizer tok = new StringTokenizer(wRec, ",");
				col = new Vector();
				while (tok.hasMoreTokens()) {
					wCel = tok.nextToken();
					col.add(wCel);
				}
				row.add(col);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return row;
	} // End of file2vector

	/*-------------------------------------------------------------------------
	 * ＶｅｃｔｏｒをPDFに変換する
	 * @quthor	 Ken Yuasa
	 * @version	Version.1.0
	 * @parameter	pInput 入力データのパス
	 * Csv2pdf.vector2pdf(wVec,"c:/test.pdf","タイトル")
	-------------------------------------------------------------------------*/
	public static void vector2pdf(Vector row, String pOutPath, String pTitle) {
		boolean pVertical = true;
		Document document; // Ｄｏｃｕｍｅｎｔオブジェクト
		if (pVertical == true) {
			document = new Document(PageSize.A4); // A4縦
		} else {
			document = new Document(PageSize.A4.rotate()); // A4横は、
		}
		try {
			File wFile = new File(pOutPath);
			if (wFile.exists()) {
				wFile.delete();
				System.err.println(pOutPath + " is exists");
				if (wFile.exists()) {
					System.err.println(pOutPath + " Can't Delete...sorry!!");
					System.exit(0);
				}
			}
			// -----------------------------------------------------------------
			// ■ＰｄｆＷｒｉｔｅｒ・オブジェクトの生成
			// -----------------------------------------------------------------
			PdfWriter.getInstance(document, new FileOutputStream(pOutPath));
			// -----------------------------------------------------------------
			// フォントの設定　　　　　※iTextAsian.jar を併用しないとエラーになります
			// -----------------------------------------------------------------
			// BaseFont bf1 = BaseFont.createFont("HeiseiMin-W3",
			// "UniJIS-UCS2-HW-H",false); // 明朝
			BaseFont bf2 = BaseFont.createFont("HeiseiKakuGo-W5",
					"UniJIS-UCS2-H", false); // ゴシック
			// Font fMin24 = new Font(bf1, 7);
			Font fGot9 = new Font(bf2, 7);
			Font fGot7 = new Font(bf2, 7);
			Font fGot12U = new Font(bf2, 12, Font.UNDERLINE | Font.ITALIC
					| Font.BOLD);
			// -----------------------------------------------------------------
			// WindowsMSゴシック
			// BaseFont bf3 = BaseFont.createFont(
			// "c:/winnt/fonts/msgothic.ttc,1", BaseFont.IDENTITY_H,
			// BaseFont.EMBEDDED);
			// -----------------------------------------------------------------
			// Header
			// -----------------------------------------------------------------
			HeaderFooter header = new HeaderFooter(new Phrase("Page:", fGot7),
					true);
			header.setAlignment(Element.ALIGN_RIGHT);
			header.setBorder(Rectangle.NO_BORDER);
			document.setHeader(header);
			// -----------------------------------------------------------------
			// Footer
			// -----------------------------------------------------------------
			HeaderFooter footer = new HeaderFooter(new Phrase("東急エージェンシー",
					fGot7), false);
			footer.setBorder(Rectangle.NO_BORDER);
			footer.setAlignment(Element.ALIGN_CENTER);
			document.setFooter(footer);
			// -----------------------------------------------------------------
			// ドキュメントのOPEN
			// -----------------------------------------------------------------
			document.open();
			// -----------------------------------------------------------------
			// タイトル(一番最初のパラグラフ)※アンダーラインを入れてみよう！
			// -----------------------------------------------------------------
			Paragraph para = new Paragraph(pTitle, fGot12U);
			para.setAlignment(Element.ALIGN_CENTER);
			// para.setGrayFill(0.9f);
			document.add(para);
			// -----------------------------------------------------------------
			// テーブルヘッダー処理（テーブル・オブジェクトの生成）
			// -----------------------------------------------------------------
			Vector col = (Vector) row.get(0);
			int width[] = new int[col.size()]; // int width[] = { 10, 30, 60 };
			int ttlWid = 0;
			// -----------------------------------------------------------------
			// ヘッダー列の文字数によって列は幅を仮設定・・・
			// -----------------------------------------------------------------
			for (int j = 0; j < col.size(); j++) {
				if (col.get(j) instanceof String) {
					width[j] = ((String) col.get(j)).length();
				} else {
					width[j] = 10; // 仮にこうしておく・・（ど～かね？？）
				}
				if (j == 0)
					width[0] = 50;
				ttlWid = ttlWid + width[j];
			}
			// 列幅を％で指定するための計算
			for (int j = 0; j < width.length; j++) {
				width[j] = (width[j] * 100) / ttlWid;
			}
			// -----------------------------------------------------------------
			// ※widthはパーセントで指定する
			// -----------------------------------------------------------------
			Table aTable;
			if (row.get(0) instanceof Vector) {
				col = (Vector) row.get(0);
				aTable = new Table(col.size());
				aTable.setDefaultHorizontalAlignment(Element.ALIGN_LEFT);
				// -------------------------------------------------------------
				// 中央揃えならElement.ALIGN_CENTER
				// aTable.setDefaultVerticalAlignment(Element.ALIGN_MIDDLE);
				// -------------------------------------------------------------
				aTable.setPadding(2);
				aTable.setWidths(width); // 各カラムの幅を設定 （パーセント）
				aTable.setWidth(100); // テーブル全体の大きさを設定 （パーセント）
				aTable.endHeaders(); // 頁をまたがってもカラム名を表示する
				for (int j = 0; j < col.size(); j++) {
					Cell cellX = new Cell(
							new Phrase((String) col.get(j), fGot9));
					aTable.addCell(cellX); // カラム名
					// aTable.addCell((String)col.get(j)); // カラム名
				}
			} else {
				// エラー処理
				return;
			}
			for (int i = 1; i < row.size(); i++) {
				if (row.get(i) instanceof Vector) {
					col = (Vector) row.get(i);
					for (int j = 0; j < col.size(); j++) {
						String wCel = "";
						if (col.get(j) instanceof String) {
							wCel = (String) col.get(j);
						} else {
							wCel = " ";
						}
						Cell cell2 = new Cell(new Phrase(wCel, fGot9));
						if (j > 0)
							cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
						if ((i % 2) == 0)
							cell2.setGrayFill(0.9f); // 背景をグレーで塗りつぶす
						aTable.addCell(cell2);
					}
				}
			}
			// -----------------------------------------------------------------
			// ドキュメントにテーブルを追加する！！
			// -----------------------------------------------------------------
			document.add(aTable);
		} catch (DocumentException de) {
			de.printStackTrace();
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.err.println(ioe.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		document.close(); /* ドキュメントのＣＬＯＳＥ */
	} // End of vector2pdf

	// -------------------------------------------------------------------------
	// ImageをPDFに変換する
	// @quthor Ken Yuasa
	// @version Version.1.0
	// Csv2pdf.image2pdf("c:\test.pdf","watermark.jpg")
	// -------------------------------------------------------------------------
	// http://itext.ugent.be/library/api/
	// http://www.lowagie.com/iText/
	// -------------------------------------------------------------------------
	public static void image2pdf(String pOutPath, String inputImage) {
		System.out.println("@csv3pdf inPath:" + inputImage);
		System.out.println("@csv3pdf outPath:" + pOutPath);
		boolean pVertical = true;
		// ---------------------------------------------------------------------
		// step 1: creation of a document-object
		// ---------------------------------------------------------------------
		Document document;
		if (pVertical == false) {
			document = new Document(PageSize.A4); // A4縦
		} else {
			document = new Document(PageSize.A4.rotate()); // A4横は、
		}
		try {
			File wFile = new File(pOutPath);
			if (wFile.exists()) {
				wFile.delete();
				System.err.println(pOutPath + " is exists");
				if (wFile.exists()) {
					System.err.println(pOutPath + " Can't Delete...sorry!!");
					System.exit(0);
				}
			}
			// -----------------------------------------------------------------
			// we create a writer that listens to the document and directs a
			// PDF-stream to a file
			// -----------------------------------------------------------------
			PdfWriter.getInstance(document, new FileOutputStream(pOutPath));
			// step 3: we open the document
			document.open();
			// step 4:
			// document.add(new Paragraph("A picture of my dog: otsoe.jpg"));
			// Image jpg = Image.getInstance("otsoe.jpg");
			document.add(new Paragraph(inputImage));
			Image jpg = Image.getInstance(inputImage);
			jpg.setAlignment(Image.MIDDLE);
			document.add(jpg);
			// document.add(new Paragraph("getacro.gif"));
			// Image gif= Image.getInstance("getacro.gif");
			// document.add(gif);
			// document.add(new Paragraph("pngnow.png"));
			// Image png = Image.getInstance("pngnow.png");
			// document.add(png);
			// document.add(new Paragraph("iText.bmp"));
			// Image bmp = Image.getInstance("iText.bmp");
			// document.add(bmp);
			// document.add(new Paragraph("iText.wmf"));
			// Image wmf = Image.getInstance("iText.wmf");
			// document.add(wmf);
			// document.add(new Paragraph("iText.tif"));
			// Image tiff = Image.getInstance("iText.tif");
			// document.add(tiff);
		} catch (DocumentException de) {
			de.printStackTrace();
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.err.println(ioe.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}

		// we close the document
		document.close();
	}
}
