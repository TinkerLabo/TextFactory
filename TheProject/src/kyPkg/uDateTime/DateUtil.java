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

    // =>20070115 2007年01月15日
    // =>20070215 2007年02月15日
    // =>20070315 2007年03月15日
    // =>20070415 2007年04月15日
    // =>20070515 2007年05月15日
    // =>20070615 2007年06月15日
    // =>20070715 2007年07月15日
    // =>20070815 2007年08月15日
    // =>20070915 2007年09月15日
    // =>20071015 2007年10月15日
    // =>20071115 2007年11月15日
    // =>20071215 2007年12月15日
    public static void test110715() {
        List<String> list = kyPkg.uDateTime.DateUtil.dateMapYYMM("20070115",
                "20071215", "yyyyMMdd", "yyyy年MM月dd日");
        for (String val : list) {
            System.out.println("=>" + val);
        }
    }

    // 指定された期間のマップを生成するロジックを作る
    // kyPkg.util.DateUtil.dateMap("20070115","20091215","yy年MM月");
    public static List<String> dateMapYYMM(String bYmd, String aYmd,
            String pattern1, String pattern2) {
        List<String> list = new ArrayList();
        // 順序性が大事なので・・・リスト、しかしどこかでハッシュマップになっていないと効率が悪いはず
        SimpleDateFormat df1 = new SimpleDateFormat(pattern1);
        SimpleDateFormat df2 = new SimpleDateFormat(pattern2);
        java.util.Date bDate = kyPkg.uDateTime.DateCalc.cnvYmdStr2Date(bYmd);
        java.util.Date aDate = kyPkg.uDateTime.DateCalc.cnvYmdStr2Date(aYmd);
        Calendar befCal = Calendar.getInstance();
        Calendar aftCal = Calendar.getInstance();
        befCal.setTime(bDate);
        aftCal.setTime(aDate);// 一日足す
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
    // 基準日付とその差を指定して、当該日付を求める
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
        // 指定偏差分、日付を補正
        // ---------------------------------------------------------------------
        baseCal.add(field, amount);
        String theDay = df1.format(baseCal.getTime());
        return theDay;
    }

    // ----------------------------------------------------------------
    // 臨時版（未完版） 日付チェックプログラム #ymdParse
    // 《使用例》 System.out.println("=>"+
    // 		String actual = DateUtil.parseDate("2005年11月4日") ;
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
            String[] wArray = wYmd.split("[年月日・／/-]");
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
        } // ※todayより年を頂く！
        if (iYY < 100) {
            if (iYY > 80) {
                iYY = iYY + 1900;
            } else {
                iYY = iYY + 2000;
            }
        }
        // 当年よりも大きいかどうかも・・・
        if (iMM < 1 | 12 < iMM) {
            iMM = 0;
        }
        if (iDD < 1 | 31 < iDD) {
            iDD = 0;
        }
        // 閏年処理をするか？？
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
    // 文字列が、YYYY/MM/DD形式の日付として認識できるか
    // @param dateStr チェックする文字列
    // @return 認識できれば true を返す
    // ---+---------+---------+---------+---------+---------+---------+---------+
    public static boolean isYMD(String dateStr) {
        // デフォルトのロケールに対しデフォルトの日付フォーマッタを入手
        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();
        // 日付フォーマットを設定
        df.applyPattern("yyyy/MM/dd");
        // 日付の解析を始める
        ParsePosition pos = new ParsePosition(0);
        try {
            // dateStr を date 型に変換
            java.util.Date ymd = df.parse(dateStr, pos);
            // 解析できたか？
            if (ymd != null) {
                // date型 ymd を String へ変換
                String Checked = df.format(ymd);
                // 引数の YYYY,MM,DD が date型に変換された YYYY,MM,DD を
                // 一致するか？
                // dateStr="1998/02/29"の場合、ymd="1998/03/01"として
                // 認識されるので、これを防ぐため。
                StringTokenizer st1 = new StringTokenizer(dateStr, "/");
                StringTokenizer st2 = new StringTokenizer(Checked, "/");
                while (st1.hasMoreTokens()) {
                    if (Integer.parseInt(st1.nextToken()) != Integer
                            .parseInt(st2.nextToken())) // parse()で補正されている
                    {
                        return false;
                    }
                }
                return true;
            }
        } catch (Exception e) {
        }
        // parse()に失敗した、例外が発生した場合
        return false;
    }

    // ------------------------------------------------------------------------
    // 月初日付を返す
    // ------------------------------------------------------------------------
    public static String firstDayOfMonth() {
        String firstDay = kyPkg.uDateTime.DateCalc.getToday().substring(0, 6)
                + "01";
        return firstDay;
    }

    // ------------------------------------------------------------------------
    // 前月初日を返す
    // ------------------------------------------------------------------------
    public static String firstDayOfPreMonth() {
        String firstDay = firstDayOfMonth();
        return ymdCalc(firstDay, -1, 'm');
    }
    // ------------------------------------------------------------------------
    // 前月末日を返す
    // ------------------------------------------------------------------------

    public static String lastDayOfPreMonth() {
        String firstDay = firstDayOfMonth();
        return ymdCalc(firstDay, -1, 'd');
    }

    // ---+---------+---------+---------+---------+---------+---------+---------+
    // java.util.Date型データを、YYYY/MM/DD形式に変換する
    // 使用例＞
    // SimpleDateFormat dformat =
    // kyPkg.util.DateUtil.getSimpleDateFormat("yyyyMMdd");
    // ---+---------+---------+---------+---------+---------+---------+---------+
    public static SimpleDateFormat getSimpleDateFormat(String pattern) {
        // デフォルトのロケールの日付フォーマッタを入手
        SimpleDateFormat dateFormat = (SimpleDateFormat) DateFormat
                .getDateInstance();
        dateFormat.applyPattern(pattern); // 日付フォーマットを設定
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
            System.out.println("ymdCalc　NG");
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
    // <<　Slice　>>　期間を刻んでリストとして返す
    // 使用例＞
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
            // option=trueなら全期間（通し期間）を先頭に追加する
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
    // 期間を刻んでリストとして返す
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
