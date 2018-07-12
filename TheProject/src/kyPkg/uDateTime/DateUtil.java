package kyPkg.uDateTime;

import java.text.*;
import java.util.*;

//kyPkg.uDateTime.DateUtil.
public class DateUtil {
//	private static final String MONTH = "Month";
//	private static final String WEEK = "Week";
//	private static final String DATE = "Date";
//	public static String[] SLICE_TYPES = { DATE, WEEK, MONTH };

    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DAY = "day";
    public static final String WEEK = "week";
    public static String[] arrayTerm = {DAY, WEEK, MONTH, YEAR};
    public static String[] SLICE_TYPES = {DAY, WEEK, MONTH};
    public static char whichType(String xTerm){
    	if(xTerm.equals(DateUtil.YEAR)){
    		return 'y';
    	}else if(xTerm.equals(DateUtil.MONTH)){
    		return 'm';
    	}else 	if(xTerm.equals(DateUtil.WEEK)){
    		return 'w';
    	}
    	return 'd';
    }

    // =>20070115 2007�N01��15��
    // =>20070215 2007�N02��15��
    // =>20070315 2007�N03��15��
    // =>20070415 2007�N04��15��
    // =>20070515 2007�N05��15��
    // =>20070615 2007�N06��15��
    // =>20070715 2007�N07��15��
    // =>20070815 2007�N08��15��
    // =>20070915 2007�N09��15��
    // =>20071015 2007�N10��15��
    // =>20071115 2007�N11��15��
    // =>20071215 2007�N12��15��
    public static void test110715() {
        List<String> list = kyPkg.uDateTime.DateUtil.dateMapYYMM("20070115",
                "20071215", "yyyyMMdd", "yyyy�NMM��dd��");
        for (String val : list) {
            System.out.println("=>" + val);
        }
    }

    // �w�肳�ꂽ���Ԃ̃}�b�v�𐶐����郍�W�b�N�����
    // kyPkg.util.DateUtil.dateMap("20070115","20091215","yy�NMM��");
    public static List<String> dateMapYYMM(String bYmd, String aYmd,
            String pattern1, String pattern2) {
        List<String> list = new ArrayList();
        // ���������厖�Ȃ̂ŁE�E�E���X�g�A�������ǂ����Ńn�b�V���}�b�v�ɂȂ��Ă��Ȃ��ƌ����������͂�
        SimpleDateFormat df1 = new SimpleDateFormat(pattern1);
        SimpleDateFormat df2 = new SimpleDateFormat(pattern2);
        java.util.Date bDate = kyPkg.uDateTime.DateCalc.cnvYmdStr2Date(bYmd);
        java.util.Date aDate = kyPkg.uDateTime.DateCalc.cnvYmdStr2Date(aYmd);
        Calendar befCal = Calendar.getInstance();
        Calendar aftCal = Calendar.getInstance();
        befCal.setTime(bDate);
        aftCal.setTime(aDate);// �������
        aftCal.add(Calendar.DATE, 1);
        // System.out.println("befCal>>" + df2.format(befCal.getTime()));
        // System.out.println("aftCal>>" + df2.format(aftCal.getTime()));
        while (aftCal.after(befCal)) {
            String key = df1.format(befCal.getTime());
            String value = df2.format(befCal.getTime());
            list.add(key + "\t" + value);
            // System.out.println("key:" + key + "value:" + value);
            befCal.add(Calendar.MONTH, 1);
        }
        return list;
    }

    // -------------------------------------------------------------------------
    // ����t�Ƃ��̍����w�肵�āA���Y���t�����߂�
    // String theDay = DateUtil.edate1(null,"week",-1);
    // System.out.println("theDay:"+theDay);
    // -------------------------------------------------------------------------
    public static String edate1(String baseYmd, String sType, int amount) {
        return edate1(baseYmd, sType, amount, "yyyyMMdd");
    }

    public static String edate1(String baseYmd, String sType, int amount,
            String format) {
        int field = -1;
        sType = sType.toUpperCase();
        char type = sType.charAt(0);
        switch (type) {
            case 'Y':
                field = Calendar.YEAR;
                break;
            case 'M':
                field = Calendar.MONTH;
                break;
            case 'W':
                field = Calendar.DATE;
                amount = amount * 7;
                break;
            default:
                field = Calendar.DATE;
                break;
        }
        Calendar baseCal;
        if (baseYmd == null) {
            // Today
            baseCal = Calendar.getInstance(TimeZone.getTimeZone("JST"));
        } else {
            java.util.Date baseDate = kyPkg.uDateTime.DateCalc
                    .cnvYmdStr2Date(baseYmd);
            baseCal = Calendar.getInstance();
            baseCal.setTime(baseDate);
        }
        SimpleDateFormat df1 = new SimpleDateFormat(format);
        // ---------------------------------------------------------------------
        // �w��΍����A���t��␳
        // ---------------------------------------------------------------------
        baseCal.add(field, amount);
        String theDay = df1.format(baseCal.getTime());
        return theDay;
    }

    // ----------------------------------------------------------------
    // �Վ��Łi�����Łj ���t�`�F�b�N�v���O���� #ymdParse
    // �s�g�p��t System.out.println("=>"+
    // 		String actual = DateUtil.parseDate("2005�N11��4��") ;
    // ----------------------------------------------------------------
    public static String parseDate(String pYmd) {
        if (pYmd == null) {
            return "";
        }
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("JST"));
        int gYY = today.get(Calendar.YEAR);
        // int gMM = (today.get(Calendar.MONTH)+1 );
        // int gDD = today.get(Calendar.DAY_OF_MONTH);

        String wYmd = pYmd.trim();
        int iYY = 0, iMM = 0, iDD = 0;
        String sYY = "", sMM = "", sDD = "";
        // System.out.println("------------------------------------------");
        // System.out.println("#input:"+wYmd);
        try {
            String[] wArray = wYmd.split("[�N�����E�^/-]");
            // System.out.println("#wArray.length:" + wArray.length);
            // for(int i=0;i<wArray.length;i++){
            // System.out.print (" <"+i+">:"+wArray[i]);
            // }
            if (wArray.length >= 3) {
                iYY = Integer.parseInt(wArray[0]);
                iMM = Integer.parseInt(wArray[1]);
                iDD = Integer.parseInt(wArray[2]);
            } else if (wArray.length >= 2) {
                iYY = 5;
                iMM = Integer.parseInt(wArray[0]);
                iDD = Integer.parseInt(wArray[1]);
            } else if (wArray.length >= 1) {
                String wStr = wArray[0];
                if (wStr.length() >= 8) {
                    iYY = Integer.parseInt(wStr.substring(0, 0 + 4));
                    iMM = Integer.parseInt(wStr.substring(4, 4 + 2));
                    iDD = Integer.parseInt(wStr.substring(6, 6 + 2));
                } else if (wStr.length() >= 6) {
                    iYY = Integer.parseInt(wStr.substring(0, 0 + 2));
                    iMM = Integer.parseInt(wStr.substring(2, 2 + 2));
                    iDD = Integer.parseInt(wStr.substring(4, 4 + 2));
                } else if (wStr.length() >= 4) {
                    iYY = gYY;
                    iMM = Integer.parseInt(wStr.substring(2, 2 + 2));
                    iDD = Integer.parseInt(wStr.substring(4, 4 + 2));
                }
            }
        } catch (NumberFormatException ne) {
            System.out.println("#NumberFormatException");
            ne.printStackTrace();
        } catch (Exception ex) {
            System.out.println("#Exception");
            ex.printStackTrace();
        }
        if (iYY == 0) {
            iYY = 2005;
        } // ��today���N�𒸂��I
        if (iYY < 100) {
            if (iYY > 80) {
                iYY = iYY + 1900;
            } else {
                iYY = iYY + 2000;
            }
        }
        // ���N�����傫�����ǂ������E�E�E
        if (iMM < 1 | 12 < iMM) {
            iMM = 0;
        }
        if (iDD < 1 | 31 < iDD) {
            iDD = 0;
        }
        // �[�N���������邩�H�H
        if (iYY == 0 | iMM == 0 | iDD == 0) { // error
        } else {
            sYY = String.valueOf(iYY);
            sMM = String.valueOf(iMM);
            sDD = String.valueOf(iDD);
            if (sYY.length() < 4) {
                sYY = "0000".substring(0, (0 + (4 - sYY.length()))) + sYY;
            }
            if (sMM.length() < 2) {
                sMM = "00".substring(0, (0 + (2 - sMM.length()))) + sMM;
            }
            if (sDD.length() < 2) {
                sDD = "00".substring(0, (0 + (2 - sDD.length()))) + sDD;
            }
            // System.out.println("#sYY:"+sYY);
            // System.out.println("#sMM:"+sMM);
            // System.out.println("#sDD:"+sDD);
        }
        return (sYY + sMM + sDD);
    }

    // ---+---------+---------+---------+---------+---------+---------+---------+
    // �����񂪁AYYYY/MM/DD�`���̓��t�Ƃ��ĔF���ł��邩
    // @param dateStr �`�F�b�N���镶����
    // @return �F���ł���� true ��Ԃ�
    // ---+---------+---------+---------+---------+---------+---------+---------+
    public static boolean isYMD(String dateStr) {
        // �f�t�H���g�̃��P�[���ɑ΂��f�t�H���g�̓��t�t�H�[�}�b�^�����
        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();
        // ���t�t�H�[�}�b�g��ݒ�
        df.applyPattern("yyyy/MM/dd");
        // ���t�̉�͂��n�߂�
        ParsePosition pos = new ParsePosition(0);
        try {
            // dateStr �� date �^�ɕϊ�
            java.util.Date ymd = df.parse(dateStr, pos);
            // ��͂ł������H
            if (ymd != null) {
                // date�^ ymd �� String �֕ϊ�
                String Checked = df.format(ymd);
                // ������ YYYY,MM,DD �� date�^�ɕϊ����ꂽ YYYY,MM,DD ��
                // ��v���邩�H
                // dateStr="1998/02/29"�̏ꍇ�Aymd="1998/03/01"�Ƃ���
                // �F�������̂ŁA�����h�����߁B
                StringTokenizer st1 = new StringTokenizer(dateStr, "/");
                StringTokenizer st2 = new StringTokenizer(Checked, "/");
                while (st1.hasMoreTokens()) {
                    if (Integer.parseInt(st1.nextToken()) != Integer
                            .parseInt(st2.nextToken())) // parse()�ŕ␳����Ă���
                    {
                        return false;
                    }
                }
                return true;
            }
        } catch (Exception e) {
        }
        // parse()�Ɏ��s�����A��O�����������ꍇ
        return false;
    }

    // ------------------------------------------------------------------------
    // �������t��Ԃ�
    // ------------------------------------------------------------------------
    public static String firstDayOfMonth() {
        String firstDay = kyPkg.uDateTime.DateCalc.getToday().substring(0, 6)
                + "01";
        return firstDay;
    }

    // ------------------------------------------------------------------------
    // �O��������Ԃ�
    // ------------------------------------------------------------------------
    public static String firstDayOfPreMonth() {
        String firstDay = firstDayOfMonth();
        return ymdCalc(firstDay, -1, 'm');
    }
    // ------------------------------------------------------------------------
    // �O��������Ԃ�
    // ------------------------------------------------------------------------

    public static String lastDayOfPreMonth() {
        String firstDay = firstDayOfMonth();
        return ymdCalc(firstDay, -1, 'd');
    }

    // ---+---------+---------+---------+---------+---------+---------+---------+
    // java.util.Date�^�f�[�^���AYYYY/MM/DD�`���ɕϊ�����
    // �g�p�၄
    // SimpleDateFormat dformat =
    // kyPkg.util.DateUtil.getSimpleDateFormat("yyyyMMdd");
    // ---+---------+---------+---------+---------+---------+---------+---------+
    public static SimpleDateFormat getSimpleDateFormat(String pattern) {
        // �f�t�H���g�̃��P�[���̓��t�t�H�[�}�b�^�����
        SimpleDateFormat dateFormat = (SimpleDateFormat) DateFormat
                .getDateInstance();
        dateFormat.applyPattern(pattern); // ���t�t�H�[�}�b�g��ݒ�
        return dateFormat;
    }

    public static String ymdCalc(String date, int offset, char type) {
        return ymdCalc(date, offset, type, getSimpleDateFormat("yyyyMMdd"));
    }

    public static String ymdCalc(String date, int offset,
            SimpleDateFormat dateFormat) {
        return ymdCalc(date, offset, 'd', dateFormat);
    }

    private static String ymdCalc(String date, int offset, char type,
            SimpleDateFormat dateFormat) {
        int year = 0;
        int month = 0;
        int day = 0;
        if (date.matches("^\\d\\d\\d\\d\\d\\d\\d\\d.*")) {
            year = Integer.parseInt(date.substring(0, 4));
            month = Integer.parseInt(date.substring(4, 6));
            day = Integer.parseInt(date.substring(6, 8));
        } else if (date.matches("^\\d\\d\\d\\d/\\d\\d/\\d\\d.*")) {
            year = Integer.parseInt(date.substring(0, 4));
            month = Integer.parseInt(date.substring(5, 7));
            day = Integer.parseInt(date.substring(8, 10));
        } else if (date.matches("^\\d*/\\d*/\\d*\\s.*")) {
            String array[] = date.substring(0, date.indexOf(" ")).split("/");
            year = Integer.parseInt(array[0]);
            month = Integer.parseInt(array[1]);
            day = Integer.parseInt(array[2]);
        } else {
            System.out.println("ymdCalc�@NG");
            return "00000000";
        }
        if (year < 100) {
            year += 2000;
        }
        // System.out.println("year:" + year);
        // System.out.println("month:" + month);
        // System.out.println("date:" + day);
        Calendar xDay = Calendar.getInstance();
        switch (type) {
            case 'y':
            case 'Y':
                xDay.set(year, (month - 1), day);
                xDay.add(Calendar.YEAR, offset);
                return dateFormat.format(xDay.getTime());
            case 'M':
            case 'm':
                xDay.set(year, (month - 1), day);
                xDay.add(Calendar.MONTH, offset);
                return dateFormat.format(xDay.getTime());
            default:
                xDay.set(year, (month - 1), day);
                xDay.add(Calendar.DATE, offset);
                return dateFormat.format(xDay.getTime());
        }
    }

    // ------------------------------------------------------------------------
    // <<�@Slice�@>>�@���Ԃ�����Ń��X�g�Ƃ��ĕԂ�
    // �g�p�၄
    // String bef = "20140101";
    // String aft = "20141231";
    // List<String[]> termList = DateUtil.slice(bef, aft, WEEK, 1);
    // ------------------------------------------------------------------------
    public static List<String[]> slice(String bef, String aft,
            String sliceType, int sliceAmount, boolean option) {
        String theDay = bef;
        String fromYmd = "";
        String toYmd = "";
        List<String[]> termList = new ArrayList();
        if (option) {
            // option=true�Ȃ�S���ԁi�ʂ����ԁj��擪�ɒǉ�����
            termList.add(new String[]{bef, aft});
        }
        while (Integer.valueOf(theDay) <= Integer.valueOf(aft)) {
            fromYmd = theDay;
            theDay = edate1(theDay, sliceType, sliceAmount);
            toYmd = edate1(theDay, DAY, -1);
            if (Integer.valueOf(toYmd) > Integer.valueOf(aft)) {
                toYmd = aft;
            }
            termList.add(new String[]{fromYmd, toYmd});
        }
        return termList;
    }

    // ------------------------------------------------------------------------
    // ���Ԃ�����Ń��X�g�Ƃ��ĕԂ�
    // ------------------------------------------------------------------------
    public static void testSlice() {
        String bef = "20140101";
        String aft = "20141231";
        List<String[]> termList = slice(bef, aft, WEEK, 1, false);
        // for debug..
        for (String[] term : termList) {
            String befYmd = term[0];
            String aftYmd = term[1];
            System.out.println("from:" + befYmd + " to:" + aftYmd);
        }
    }

    public static void testEdate3() {
        String theDay = edate1(null, WEEK, -1);
        System.out.println("theDay:" + theDay);
    }

    public static void testEdate1() {
        String theDay = kyPkg.uDateTime.DateUtil.edate1("20110614", YEAR, -1,
                "yyyy/MM/dd");
        System.out.println("theDay:" + theDay);
    }

    public static void main(String[] argv) {
        testSlice();
        // test110715();
        // testEdate1();
        // testSubdate();
        // testGetTimeStamp();
    }

}
