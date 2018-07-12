package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

/**************************************************************************
 * TeeClojure				
 * @author	ken yuasa
 * @version	1.0
 * <br>�����ΏۃJ�����̒l���Z�b�g�Ɋ܂܂�Ă��邩�ǂ����ɂ��A���R�[�h��U�蕪����B
 * <br>(totalOpt��true�Ȃ�A���Y�J�������@rec[col] = TOTAL;�@�ƂȂ�j
 **************************************************************************/
public class TeeClojure implements Inf_BaseClojure {
	private static final String TARGET = "target";
	private static final String TOTAL = "total";
	private String delimiter = "\t";
	private static final String LF = System.getProperty("line.separator");
	private String oFile_T;
	private String oFile_F;
	private int col;
	private Set<String> hashSet;
	private BufferedWriter bw_T;
	private BufferedWriter bw_F;
	private int counter_T = 0;
	private int counter_F = 0;

	private boolean otherOpt = false;
	private boolean totalOpt = false;
	private boolean targetOpt = false;

	public void setOtherOpt(boolean otherOpt) {
		this.otherOpt = otherOpt;
	}

	public void setTotalOpt(boolean totalOpt) {
		this.totalOpt = totalOpt;
	}

	public void setTargetOpt(boolean targetOpt) {
		this.targetOpt = targetOpt;
	}

	private String stat = "";

	public int getCounter1() {
		return counter_T;
	}

	public int getCounter2() {
		return counter_F;
	}

	//	String tgt_Path = calcDir + _PRF1 + "trrTrue.txt";	//target
	//	String ttl_Path = calcDir + _PRF2 + "trrFalse.txt";	//total
	/**************************************************************************
	 * TeeClojure				
	 * <br>�����ΏۃJ�����̒l���Z�b�g�Ɋ܂܂�Ă��邩�ǂ����ɂ��A���R�[�h��U�蕪����B
	 * <br>(totalOpt��true�Ȃ�A���Y�J�������@rec[col] = TOTAL;�@�ƂȂ�j
	 * @param oFile_T	�o�̓t�@�C���p�X�P�i�����ΏۃJ�����̒l�����X�g�Ɋ܂܂�Ă����ꍇ�̏o�́j	trrTrue.txt	 
	 * @param oFile_F	�o�̓t�@�C���p�X�Q�i�����ΏۃJ�����̒l�����X�g�Ɋ܂܂�Ȃ��ꍇ�̏o�́j	trrFalse.txt	 
	 * @param col		�����ΏۃJ����	 
	 * @param hashSet	�n�b�V���Z�b�g�i�����ΏۃJ�����̓��e���R���Ɉ�v���邩�ǂ����j
	 * 
	<br>
	<br><hr>���̓t�@�C���y���d�v�z
	<br>
	<table border='1'>
	<tr bgcolor='DeepSkyBlue'><td>�J����</td><td>����</td></tr>
	<tr><td> 0</td><td>Monitor ���j�^�[ID</td></tr>
	<tr><td> 1</td><td>Price �w�����i</td></tr>
	<tr><td> 2</td><td>Count �w������</td></tr>
	<tr ><td> 3</td><td>break�@�w��i�ڂ�荇�������敪</td></tr>
	<tr><td> 4</td><td>Shop1 �w����</td></tr>
	<tr><td> 5</td><td>Accept �w�����t</td></tr>
	<tr><td> 6</td><td>Flag3 �t���O�R</td></tr>
	<tr><td> 7</td><td>Flag1 �t���O�P</td></tr>
	<tr><td> 8</td><td>Flag2 �t���O�Q</td></tr>
	<tr><td> 9</td><td>Shop2 �w����(��X�敪�j</td></tr>
	<tr><td> 10</td><td>YM �N��</td></tr>
	<tr><td> 11</td><td>hh �w������</td></tr>
	<tr><td> 12</td><td>idx �w�����ԂR�O������</td></tr>
	<tr><td> 13</td><td>week �w���j��</td></tr>
	<tr><td> 14</td><td>capa�@�e��</td></tr>
	</table>
	<br>
	<br><hr>���̓t�@�C���@�y�T���v���z�@
	<br>
	<table border='1'>
	<tr bgcolor='DeepSkyBlue'><td>#0_Id</td><td>#1_Price</td><td>#2_Count</td><td>#3_Break</td><td>#4_Shop1</td><td>#5_AcceptDate</td><td>#6_Flg3</td><td>#7_Flg1</td><td>#8_Flg2</td><td>#9_Shop2</td><td>#10_ym</td><td>#11_hh</td><td>#12_Idx</td><td>#13_Week</td><td>#14_Capa</td></tr>
	<tr><td>73302423</td><td>359</td><td>1</td><td>00002: �����[����</td><td>1V</td><td>20141018</td><td> </td><td> </td><td> </td><td>1</td><td>1410</td><td>15</td><td>30</td><td>6</td><td>336000</td></tr>
	<tr><td>71205401</td><td>321</td><td>1</td><td>00002: �����[����</td><td>71</td><td>20140924</td><td> </td><td> </td><td> </td><td>7</td><td>1409</td><td>18</td><td>37</td><td>3</td><td>336000</td></tr>
	<tr><td>74136616</td><td>420</td><td>1</td><td>00004: �����ǂ�</td><td>10</td><td>20141224</td><td> </td><td> </td><td> </td><td>1</td><td>1412</td><td>13</td><td>27</td><td>3</td><td>424000</td></tr>
	<tr><td>74136616</td><td>418</td><td>1</td><td>00004: �����ǂ�</td><td>10</td><td>20141001</td><td> </td><td> </td><td> </td><td>1</td><td>1410</td><td>13</td><td>27</td><td>3</td><td>424000</td></tr>
	<tr><td>71596376</td><td>409</td><td>2</td><td>00004: �����ǂ�</td><td>10</td><td>20141219</td><td> </td><td> </td><td> </td><td>1</td><td>1412</td><td>16</td><td>33</td><td>5</td><td>424000</td></tr>
	</table>		 
	<br>
	<br><hr>�o�̓t�@�C���P�@�y�T���v���ztrrTrue.txt�@�@�w00004: �����ǂ�x���Ώۂ̏ꍇ
	<br>
	<table border='1'>
	<tr bgcolor='DeepSkyBlue'><td>#0</td><td>#1</td><td>#2</td><td>#3</td><td>#4</td><td>#5</td><td>#6</td><td>#7</td><td>#8</td><td>#9</td><td>#10</td><td>#11</td><td>#12</td><td>#13</td><td>#14</td></tr>
	<tr><td>74136616</td><td>420</td><td>1</td><td>00004: �����ǂ�</td><td>10</td><td>20141224</td><td> </td><td> </td><td> </td><td>1</td><td>1412</td><td>13</td><td>27</td><td>3</td><td>424000</td></tr>
	<tr><td>74136616</td><td>418</td><td>1</td><td>00004: �����ǂ�</td><td>10</td><td>20141001</td><td> </td><td> </td><td> </td><td>1</td><td>1410</td><td>13</td><td>27</td><td>3</td><td>424000</td></tr>
	<tr><td>71596376</td><td>409</td><td>2</td><td>00004: �����ǂ�</td><td>10</td><td>20141219</td><td> </td><td> </td><td> </td><td>1</td><td>1412</td><td>16</td><td>33</td><td>5</td><td>424000</td></tr>
	<tr><td>76835104</td><td>408</td><td>1</td><td>00004: �����ǂ�</td><td>10</td><td>20141009</td><td> </td><td> </td><td> </td><td>1</td><td>1410</td><td>11</td><td>23</td><td>4</td><td>424000</td></tr>
	<tr><td>73521373</td><td>408</td><td>1</td><td>00004: �����ǂ�</td><td>10</td><td>20140905</td><td> </td><td> </td><td> </td><td>1</td><td>1409</td><td>20</td><td>41</td><td>5</td><td>424000</td></tr>
	</table>
	<br>
	<br><hr>�o�̓t�@�C���Q�@�y�T���v���ztrrFalse.txt�@
	<br>
	<table border='1'>
	<tr bgcolor='DeepSkyBlue'><td>#0</td><td>#1</td><td>#2</td><td>#3</td><td>#4</td><td>#5</td><td>#6</td><td>#7</td><td>#8</td><td>#9</td><td>#10</td><td>#11</td><td>#12</td><td>#13</td><td>#14</td></tr>
	<tr><td>73624673</td><td>410</td><td>1</td><td>total</td><td>14</td><td>20141122</td><td> </td><td> </td><td> </td><td>1</td><td>1411</td><td>10</td><td>20</td><td>6</td><td>336000</td></tr>
	<tr><td>74077743</td><td>410</td><td>1</td><td>total</td><td>19</td><td>20141207</td><td> </td><td> </td><td> </td><td>1</td><td>1412</td><td>16</td><td>33</td><td>0</td><td>336000</td></tr>
	<tr><td>77841267</td><td>409</td><td>1</td><td>total</td><td>19</td><td>20141121</td><td> </td><td> </td><td> </td><td>1</td><td>1411</td><td>11</td><td>23</td><td>5</td><td>336000</td></tr>
	<tr><td>70278857</td><td>367</td><td>3</td><td>total</td><td>19</td><td>20141124</td><td> </td><td> </td><td> </td><td>1</td><td>1411</td><td>13</td><td>26</td><td>1</td><td>336000</td></tr>
	<tr><td>73010796</td><td>355</td><td>1</td><td>total</td><td>19</td><td>20141102</td><td> </td><td> </td><td> </td><td>1</td><td>1411</td><td>20</td><td>40</td><td>0</td><td>336000</td></tr>
	</table>
	
	 **************************************************************************/
	public TeeClojure(String oFile_T, String oFile_F, int col,
			Set<String> hashSet) {
		this.counter_T = 0;
		this.counter_F = 0;
		this.oFile_T = oFile_T;
		this.oFile_F = oFile_F;
		this.col = col;
		this.hashSet = hashSet;
		//		for (String element : hashSet) {
		//			System.out.println("��@TeeClojure set:" + element);
		//		}
	}

	public String getStat() {
		return stat;
	}

	public void write_T(String[] rec) {
		if (targetOpt)
			rec[col] = TARGET;
		String xRec = join(rec, delimiter);
		try {
			bw_T.write(xRec);
			bw_T.write(LF);
			counter_T++;
		} catch (Exception e) {
			System.out.println("#Error @TeeClojure.write_T():" + e.toString());
			e.printStackTrace();
		}
	}

	public void write_F(String[] rec) {
		if (totalOpt)
			rec[col] = TOTAL;
		String xrec = join(rec, delimiter);
		try {
			bw_F.write(xrec);
			bw_F.write(LF);
			counter_F++;
		} catch (Exception e) {
			System.out.println("#Error @TeeClojure.write_F{}:" + e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
		// open
		try {
			bw_T = new BufferedWriter(new FileWriter(oFile_T));
			bw_F = new BufferedWriter(new FileWriter(oFile_F));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	public void execute(String rec) {
		execute(rec.split(delimiter));
	}

	@Override
	public void execute(String[] rec) {
		if (rec.length > col) {
			if (hashSet.contains(rec[col])) {
				//				System.out.println("@@@TeeClojure rec[col]:" + rec[col]);
				write_T(rec);
				if (otherOpt) {
					write_F(rec);
				}
			} else {
				write_F(rec);
			}
		}
	}

	// ------------------------------------------------------------------------
	// counter1���O�Ȃ�u�ꌏ���q�b�g���Ȃ������v�Ƃ������ƂɂȂ�
	// ------------------------------------------------------------------------
	@Override
	public void write() {
		try {
			bw_T.close();
			bw_F.close();
		} catch (Exception e) {
			System.out.println("#Error @TeeClojure:" + e.toString());
		}
		stat = "";
		if (counter_T == 0) {
			stat = "#ERROR�@���Ƀ}�b�`���鍀�ڂ����݂��܂���ł����I";
		}
	}

	public static void main(String[] argv) {
		teeClojureTest();
	}

	public static void teeClojureTest() {
		String userDir = globals.ResControl.getQPRHome();
		System.out.println("userDir:" + userDir);
		String otPath = userDir + "828111000507/calc/true.txt";
		String ofPath = userDir + "828111000507/calc/false.txt";
		String inPath = userDir + "828111000507/calc/trrModItp.txt";
		int col = 3;
		HashSet<String> hashSet = new HashSet();
		hashSet.add("[40691] ���A��");
		hashSet.add("[40641] �r�[��");
		// [40691] ���A��
		TeeClojure closure = new TeeClojure(otPath, ofPath, col, hashSet);
		new CommonClojure().incore(closure, inPath, true);
	}
}
