package kyPkg.etc;

import static kyPkg.sql.ISAM.CHARACTER_SET_OEM;
import static kyPkg.sql.ISAM.COL_NAME_HEADER_FALSE;
import static kyPkg.sql.ISAM.FORMAT_CSV_DELIMITED;
import static kyPkg.sql.ISAM.MAX_SCAN_ROWS_0;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class QTBwriter {
	static String LF = System.getProperty("line.separator");
	private static final java.text.DecimalFormat df2 = new java.text.DecimalFormat("00000");

	public static void main(String[] argv) {
		String[] mother = { "購買経験のあるブランド１　　　　　　　　　　　　　　　　　　",
				"購買経験のあるブランド２　　　　　　　　　　　　　　　　　　",
				"購買経験のあるブランド３　　　　　　　　　　　　　　　　　　", };
		String[] chile = { "ブランド０１　　　　　　　　　　　　　　　　　　　　　　　　",
				"ブランド０２　　　　　　　　　　　　　　　　　　　　　　　　",
				"ブランド０３　　　　　　　　　　　　　　　　　　　　　　　　",
				"ブランド０４　　　　　　　　　　　　　　　　　　　　　　　　",
				"ブランド０５　　　　　　　　　　　　　　　　　　　　　　　　",
				"ブランド０６　　　　　　　　　　　　　　　　　　　　　　　　",
				"ブランド０７　　　　　　　　　　　　　　　　　　　　　　　　",
				"ブランド０８　　　　　　　　　　　　　　　　　　　　　　　　",
				"ブランド０９　　　　　　　　　　　　　　　　　　　　　　　　", };
		String rootDir = globals.ResControl.getQprRootDir();
		String outDir = rootDir+"zapp";
		writeISAMctrl(outDir, mother, chile,",");
	}

	// -------------------------------------------------------------------------
	// write ISAM Control
	// -------------------------------------------------------------------------
	public static void writeISAMctrl(String outDir, String[] mother,	String[] chile,String delim) {
		if (outDir==null) return;
		if (mother==null) return;
		if (chile==null)  return;
		File wFile = new File(outDir);
		wFile.mkdir();
		String outPath = outDir + "/QTB1.TXT";
		String[] descripter = new String[mother.length];
		char x = 'A';
		int fullSeq = 0;
		String prefix = "EX";
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outPath));
			fullSeq = writeHEAD(bw, ",ROOT,SAMPLENUMBER,,,1,10,INTEGER",	fullSeq);
			for (int i = 0; i < mother.length; i++) {
				x++;
				// System.out.println("x:"+x);
				// System.out.println(" mother[i]:"+ mother[i]);
				descripter[i] = prefix + x;
				fullSeq = writeQTB1(bw, descripter[i], mother[i], fullSeq,
						chile.length);
				fullSeq = writeQTB2(bw, descripter[i], chile, fullSeq);
			}
			bw.close();
			witeSchemaINI(outDir, descripter, chile.length,delim);
			witeAliasTXT(outDir,"ID","XXX");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	// -------------------------------------------------------------------------
	// 00001,ROOT,SAMPLENUMBER,,,1,10,INTEGER
	// -------------------------------------------------------------------------
	public static int writeHEAD(BufferedWriter Writer, String value, int fullSeq) {
		StringBuffer buf = new StringBuffer();
		try {
			fullSeq++;
			buf.delete(0, buf.length());
			buf.append(df2.format(fullSeq));
			buf.append(value);
			Writer.write(buf.toString());
			Writer.write(LF);
			System.out.println("writeHead>" + value);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return fullSeq;
	}

	// -------------------------------------------------------------------------
	// writeQTB1
	// -------------------------------------------------------------------------
	public static int writeQTB1(BufferedWriter Writer, String descripter,
			String mother, int fullSeq, int occ) {
		StringBuffer buf = new StringBuffer();
		int seq = -1;
		try {
			fullSeq++;
			seq++;
			buf.delete(0, buf.length());
			buf.append(df2.format(fullSeq));
			buf.append(",ROOT,");
			buf.append(descripter);
			buf.append(",,");
			buf.append(mother);
			buf.append(",");
			buf.append(occ);
			buf.append(",");
			buf.append(occ);
			buf.append(",MULTI");
			Writer.write(buf.toString());
			Writer.write(LF);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return fullSeq;
	}

	// -------------------------------------------------------------------------
	// writeQTB2
	// -------------------------------------------------------------------------
	public static int writeQTB2(BufferedWriter Writer, String descripter,
			String[] chile, int fullSeq) {
		StringBuffer buf = new StringBuffer();
		int seq = 0;
		try {
			for (int i = 0; i < chile.length; i++) {
				fullSeq++;
				seq++;
				buf.delete(0, buf.length());
				buf.append(df2.format(fullSeq));
				buf.append(",");
				buf.append(descripter);
				buf.append(",");
				buf.append(descripter);
				buf.append("_" + seq);
				buf.append(",1,");
				buf.append(chile[i]);
				buf.append(",1,1,MULTI,");
				buf.append("" + seq);
				buf.append(",1,");
				buf.append(descripter);
				Writer.write(buf.toString());
				Writer.write(LF);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return fullSeq;
	}

	// -------------------------------------------------------------------------
	// witeSchemaINI
	// -------------------------------------------------------------------------
	// [ASM.TXT]
	// ColNameHeader=False
	// Format=CSVDelimited
	// MaxScanRows=0
	// CharacterSet=OEM
	// Col1="ID" Char Width 15
	// Col87="Q3_1" Char Width 1
	// Col88="Q3_2" Char Width 1
	// Col89="Q3_3" Char Width 1
	// Col90="Q3_4" Char Width 1
	// Col91="Q3_5" Char Width 1
	// Col92="Q3_6" Char Width 1
	// Col93="Q3_7" Char Width 1
	// Col94="Q3_8" Char Width 1
	// Col95="Q3_9" Char Width 1
	// -------------------------------------------------------------------------
	public static void CommonWriter(String outPath , String[] header,String[] body,String[] foot){
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outPath));
			if (header!=null){
				for (int i = 0; i < header.length; i++) {
					bw.write(header[i]);
					bw.write(LF);
				}
			}
			if (body!=null){
				for (int i = 0; i < body.length; i++) {
					bw.write(body[i]);
					bw.write(LF);
				}
			}
			if (foot!=null){
				for (int i = 0; i < foot.length; i++) {
					bw.write(foot[i]);
					bw.write(LF);
				}
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	public static void witeSchemaINI(String outDir, String[] descripter,
			int range,String delim) {
		String delimX = "CSV";
		if (delim.equals("\t")){
			 delimX = "TAB";
		}
		String outPath = outDir + "/Schema.ini";
		String[] header = { 
				"[QTB1.TXT]                    ",
				COL_NAME_HEADER_FALSE,
				FORMAT_CSV_DELIMITED,
				MAX_SCAN_ROWS_0,
				CHARACTER_SET_OEM,
				"Col1=\"Srt\" Char    Width 60   ",
				"Col2=\"Mot\" Char    Width 60   ",
				"Col3=\"Key\" Char    Width 60   ",
				"Col4=\"Val\" Char    Width 60   ",
				"Col5=\"Nam\" Char    Width 60   ",
				"Col6=\"Max\" Integer            ",
				"Col7=\"Occ\" Integer            ",
				"Col8=\"Typ\" Char    Width 10   ",
				"Col9=\"Col\" Integer            ",
				"Col10=\"Len\" Integer           ",
				"Col11=\"Opt\" Char    Width 128 ",
				"[ASM.TXT]                     ",
				COL_NAME_HEADER_FALSE,
				"Format="+delimX+"Delimited           ",
				MAX_SCAN_ROWS_0,
				CHARACTER_SET_OEM,
				"Col1=\"ID\" Char Width 15       ", 
				};
		String[] body = new String[descripter.length];
		for (int i = 0; i < descripter.length; i++) {
			body[i] = "Col" + (i + 2)+"=\""+descripter[i]+"\" Char Width  " + range;
		}

		CommonWriter(outPath ,header,body,null);

	}
	public static void witeAliasTXT(String outDir,String key,String field) {
		String outPath = outDir + "/alias.txt";
		String[] header = {
			"connect\tDRIVER={Microsoft Text Driver (*.txt; *.csv)};DefaultDir=$;DriverId=27;FIL=text;MaxBufferSize=16384;PageTimeout=5;",
			"table\tASM#TXT",
			"field\t" + field,
			"key\t"  + key,
			"Cond\t",
		};
		CommonWriter(outPath ,header,null,null);
	}

}
