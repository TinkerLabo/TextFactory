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
 �o�c�e�o�͊֘A�N���X
 @quthor	 Ken Yuasa
 @version	Version.1.0
 */
public class Csv2pdf {
	/*-------------------------------------------------------------------------
	 * ���C���i�P�̃e�X�g�p�j
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
		vector2pdf(row, outPath, "�@�h�@�L�@���@���@���@�g�@�́@�^�@�C�@�g�@���@");
		System.out.println("\n-----------------------------------------");
	}

	/***************************************************************************
	 * �s tmdl2pdf �t <BR>
	 * �e�[�u�����f�����̃f�[�^���o�c�e�t�@�C���ɏ����o��
	 * 
	 * @quthor Ken Yuasa
	 * @version Version 1.0
	 * @parameter pOutPath �t�@�C���̏o�͐�p�X
	 * @parameter pTmdl ���̓f�[�^�i�e�[�u�����f���j
	 * @parameter pHead �v�w�b�_�[���ǂ��� �s�g�p��t tmdl2pdf(pOutPath,TableModel
	 *            pTmdl,true,"�����Ɓ[");
	 ***************************************************************************/
	public int tmdl2pdf(String pOutPath, TableModel pTmdl, boolean headOption,
			String title, int colWidths[]) {
		System.out.println("�� tmdl2pdf ��������������������������������������");
		if (title.trim().equals(""))
			title = "�^�C�g�����ݒ�";
		boolean pVertical = false;
		Document document; // �c���������������I�u�W�F�N�g
		if (pVertical == true) {
			document = new Document(PageSize.A4); // A4�c
		} else {
			document = new Document(PageSize.A4.rotate()); // A4��
		}
		int wCnt = 0;
		int columnCount = pTmdl.getColumnCount(); // �J������
		int wRow = pTmdl.getRowCount(); // �s��
		System.out.println("�� columnCount:" + columnCount);
		System.out.println("�� wRow:" + wRow);
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
			// PdfWriter����
			// -----------------------------------------------------------------
			PdfWriter.getInstance(document, new FileOutputStream(pOutPath));
			// -----------------------------------------------------------------
			// �t�H���g�̐ݒ�@�@�@�@�@��iTextAsian.jar �𕹗p���Ȃ��ƃG���[�ɂȂ�܂�
			// -----------------------------------------------------------------
			// BaseFont bf1 = BaseFont.createFont("HeiseiMin-W3",
			// "UniJIS-UCS2-HW-H",false); // ����
			BaseFont bf2 = BaseFont.createFont("HeiseiKakuGo-W5",
					"UniJIS-UCS2-H", false); // �S�V�b�N
			// Font fMin24 = new Font(bf1, 24);
			// Font fGot9 = new Font(bf2, 4);
			// Font fGot7 = new Font(bf2, 4);
			Font fGot4 = new Font(bf2, 4);
			Font fGot12U = new Font(bf2, 12, Font.UNDERLINE | Font.ITALIC
					| Font.BOLD);
			// -----------------------------------------------------------------
			// WindowsMS�S�V�b�N
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
			HeaderFooter footer = new HeaderFooter(new Phrase("���}�G�[�W�F���V�[",
					fGot4), false);
			footer.setBorder(Rectangle.NO_BORDER);
			footer.setAlignment(Element.ALIGN_CENTER);
			document.setFooter(footer);
			// -----------------------------------------------------------------
			// �h�L�������g��OPEN
			// -----------------------------------------------------------------
			document.open();
			// -----------------------------------------------------------------
			// �^�C�g��(��ԍŏ��̃p���O���t)���A���_�[���C�������Ă݂悤�I
			// -----------------------------------------------------------------
			Paragraph para = new Paragraph(title, fGot12U);
			para.setAlignment(Element.ALIGN_CENTER);
			// para.setGrayFill(0.9f);
			document.add(para);
			// -----------------------------------------------------------------
			// �e�[�u���w�b�_�[�����i�e�[�u���E�I�u�W�F�N�g�̐����j
			// �w�b�_�[��̕������ɂ���ė�͕������ݒ�E�E�E
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
			// �񕝂����Ŏw�肷�邽�߂̌v�Z
			for (int j = 0; j < columnCount; j++) {
				colWidths[j] = (colWidths[j] * 100) / total;
			}
			// -----------------------------------------------------------------
			// ���񕝂̓p�[�Z���g�Ŏw�肷��
			// -----------------------------------------------------------------
			Table aTable = new Table(columnCount);
			aTable.setDefaultHorizontalAlignment(Element.ALIGN_LEFT);
			// -------------------------------------------------------------
			// ���������Ȃ�Element.ALIGN_CENTER
			// aTable.setDefaultVerticalAlignment(Element.ALIGN_MIDDLE);
			// -------------------------------------------------------------
			aTable.setPadding(2);
			aTable.setWidths(colWidths); // �e�J�����̕���ݒ� �i�p�[�Z���g�j
			aTable.setWidth(100); // �e�[�u���S�̂̑傫����ݒ�i�p�[�Z���g�j
			aTable.endHeaders(); // �ł��܂������Ă��J��������\������
			// for(int j = 0;j<wCol;j++){
			// System.out.println("��#2 pTmdl.getColumnName(j):"+pTmdl.getColumnName(j));
			// aTable.addCell(pTmdl.getColumnName(j)); // �J������
			// }
			for (int j = 0; j < columnCount; j++) {
				Cell cellX = new Cell(new Phrase(pTmdl.getColumnName(j), fGot4));
				aTable.addCell(cellX); // �J������
				// aTable.addCell((String)col.get(j)); // �J������
			}
			String wStr = "";
			for (int i = 0; i < wRow; i++) {
				for (int j = 0; j < columnCount; j++) {
					Object obj = pTmdl.getValueAt(i, j);
					if (obj != null) {
						wStr = obj.toString();
					} else {
						wStr = "�@";
					}
					Cell cell2 = new Cell(new Phrase(wStr, fGot4));
//					if (j > 0)
//						cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
					if ((i % 2) == 0)
						cell2.setGrayFill(0.9f); // �w�i���O���[�œh��Ԃ�
					aTable.addCell(cell2);
				}
			}
			// -----------------------------------------------------------------
			// �h�L�������g�Ƀe�[�u����ǉ�����I�I
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
		document.close(); /* �h�L�������g�̂b�k�n�r�d */
		return wCnt;
	} // tmdl2pdf

	/*-------------------------------------------------------------------------
	 * �t�@�C�����u�����������ɕϊ�����
	 * @quthor	 Ken Yuasa
	 * @version	Version.1.0
	 * @parameter	pInput ���̓f�[�^�̃p�X
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
	 * �u������������PDF�ɕϊ�����
	 * @quthor	 Ken Yuasa
	 * @version	Version.1.0
	 * @parameter	pInput ���̓f�[�^�̃p�X
	 * Csv2pdf.vector2pdf(wVec,"c:/test.pdf","�^�C�g��")
	-------------------------------------------------------------------------*/
	public static void vector2pdf(Vector row, String pOutPath, String pTitle) {
		boolean pVertical = true;
		Document document; // �c���������������I�u�W�F�N�g
		if (pVertical == true) {
			document = new Document(PageSize.A4); // A4�c
		} else {
			document = new Document(PageSize.A4.rotate()); // A4���́A
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
			// ���o�����v�����������E�I�u�W�F�N�g�̐���
			// -----------------------------------------------------------------
			PdfWriter.getInstance(document, new FileOutputStream(pOutPath));
			// -----------------------------------------------------------------
			// �t�H���g�̐ݒ�@�@�@�@�@��iTextAsian.jar �𕹗p���Ȃ��ƃG���[�ɂȂ�܂�
			// -----------------------------------------------------------------
			// BaseFont bf1 = BaseFont.createFont("HeiseiMin-W3",
			// "UniJIS-UCS2-HW-H",false); // ����
			BaseFont bf2 = BaseFont.createFont("HeiseiKakuGo-W5",
					"UniJIS-UCS2-H", false); // �S�V�b�N
			// Font fMin24 = new Font(bf1, 7);
			Font fGot9 = new Font(bf2, 7);
			Font fGot7 = new Font(bf2, 7);
			Font fGot12U = new Font(bf2, 12, Font.UNDERLINE | Font.ITALIC
					| Font.BOLD);
			// -----------------------------------------------------------------
			// WindowsMS�S�V�b�N
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
			HeaderFooter footer = new HeaderFooter(new Phrase("���}�G�[�W�F���V�[",
					fGot7), false);
			footer.setBorder(Rectangle.NO_BORDER);
			footer.setAlignment(Element.ALIGN_CENTER);
			document.setFooter(footer);
			// -----------------------------------------------------------------
			// �h�L�������g��OPEN
			// -----------------------------------------------------------------
			document.open();
			// -----------------------------------------------------------------
			// �^�C�g��(��ԍŏ��̃p���O���t)���A���_�[���C�������Ă݂悤�I
			// -----------------------------------------------------------------
			Paragraph para = new Paragraph(pTitle, fGot12U);
			para.setAlignment(Element.ALIGN_CENTER);
			// para.setGrayFill(0.9f);
			document.add(para);
			// -----------------------------------------------------------------
			// �e�[�u���w�b�_�[�����i�e�[�u���E�I�u�W�F�N�g�̐����j
			// -----------------------------------------------------------------
			Vector col = (Vector) row.get(0);
			int width[] = new int[col.size()]; // int width[] = { 10, 30, 60 };
			int ttlWid = 0;
			// -----------------------------------------------------------------
			// �w�b�_�[��̕������ɂ���ė�͕������ݒ�E�E�E
			// -----------------------------------------------------------------
			for (int j = 0; j < col.size(); j++) {
				if (col.get(j) instanceof String) {
					width[j] = ((String) col.get(j)).length();
				} else {
					width[j] = 10; // ���ɂ������Ă����E�E�i�ǁ`���ˁH�H�j
				}
				if (j == 0)
					width[0] = 50;
				ttlWid = ttlWid + width[j];
			}
			// �񕝂����Ŏw�肷�邽�߂̌v�Z
			for (int j = 0; j < width.length; j++) {
				width[j] = (width[j] * 100) / ttlWid;
			}
			// -----------------------------------------------------------------
			// ��width�̓p�[�Z���g�Ŏw�肷��
			// -----------------------------------------------------------------
			Table aTable;
			if (row.get(0) instanceof Vector) {
				col = (Vector) row.get(0);
				aTable = new Table(col.size());
				aTable.setDefaultHorizontalAlignment(Element.ALIGN_LEFT);
				// -------------------------------------------------------------
				// ���������Ȃ�Element.ALIGN_CENTER
				// aTable.setDefaultVerticalAlignment(Element.ALIGN_MIDDLE);
				// -------------------------------------------------------------
				aTable.setPadding(2);
				aTable.setWidths(width); // �e�J�����̕���ݒ� �i�p�[�Z���g�j
				aTable.setWidth(100); // �e�[�u���S�̂̑傫����ݒ� �i�p�[�Z���g�j
				aTable.endHeaders(); // �ł��܂������Ă��J��������\������
				for (int j = 0; j < col.size(); j++) {
					Cell cellX = new Cell(
							new Phrase((String) col.get(j), fGot9));
					aTable.addCell(cellX); // �J������
					// aTable.addCell((String)col.get(j)); // �J������
				}
			} else {
				// �G���[����
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
							cell2.setGrayFill(0.9f); // �w�i���O���[�œh��Ԃ�
						aTable.addCell(cell2);
					}
				}
			}
			// -----------------------------------------------------------------
			// �h�L�������g�Ƀe�[�u����ǉ�����I�I
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
		document.close(); /* �h�L�������g�̂b�k�n�r�d */
	} // End of vector2pdf

	// -------------------------------------------------------------------------
	// Image��PDF�ɕϊ�����
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
			document = new Document(PageSize.A4); // A4�c
		} else {
			document = new Document(PageSize.A4.rotate()); // A4���́A
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
