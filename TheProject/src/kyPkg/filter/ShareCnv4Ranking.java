package kyPkg.filter;

import java.util.HashMap;

import kyPkg.converter.Inf_ArrayCnv;

public class ShareCnv4Ranking implements Inf_ArrayCnv {
	// -------------------------------------------------------------------------
	// ��s�ڂ��g�[�^���s�Ƃ��ăV�F�A���v�Z����
	// -------------------------------------------------------------------------
	java.text.DecimalFormat exFormat2 = new java.text.DecimalFormat("0.00");
	private long lCount = 0;//���C���J�E���g�i���E�E�����L���O�j
	private Long[] mothers = new Long[5];//�ꐔ�i�g�[�^���s�j
	private Double[] rShare = new Double[5];//�ݐσV�F�A
	private int monCount = 0;//���j�^�[�T���v����
	private HashMap<String, Long> rankMap;

	public HashMap<String, Long> getRankMap() {
		return rankMap;//�ǂ̃u�����h�����ʂȂ̂����L�^�����}�b�v
	}

	public ShareCnv4Ranking(int monCount) {
		super();
		this.monCount = monCount;
	}

	// ------------------------------------------------------------------------
	// init
	// ------------------------------------------------------------------------
	@Override
	public void init() {
		rankMap = new HashMap();
		for (int i = 0; i < rShare.length; i++) {
			rShare[i] = 0.0;
		}
	}

	// ------------------------------------------------------------------------
	// Convert
	/*
	 * <br> <br><hr>���o�̓t�@�C�� �y�T���v���z <br> <table border='1'> <tr
	 * bgcolor='DeepSkyBlue'><td>#0</td><td>#1</td><td>#2</td><td>#3</td><td>#4<
	 * /td></tr> <tr><td>
	 * Total</td><td>55131697</td><td>401063</td><td>264553274000</td><td>92</td
	 * ></tr> <tr><td>L0000:
	 * �Y�_����</td><td>8765013</td><td>70738</td><td>49544253000</td><td>92</td></
	 * tr> <tr><td>H0000:
	 * �R�[�q�[����</td><td>7215592</td><td>61627</td><td>21407190000</td><td>92</td><
	 * /tr> <tr><td>O0000:
	 * �����E���_��</td><td>8246712</td><td>58881</td><td>14874327000</td><td>92</td><
	 * /tr> <tr><td>A0000:
	 * ���{������</td><td>6258059</td><td>43529</td><td>38145280000</td><td>92</td></
	 * tr> </table>
	 */
	// ------------------------------------------------------------------------
	@Override
	public String[] convert(String[] rec, int stat) {
		String[] wRec;
		String[] oRec = new String[12];
		String brand = rec[0];//Brand
		if (lCount == 0) {
			mothers[1] = Long.valueOf(rec[1]);
			mothers[2] = Long.valueOf(rec[2]);
			mothers[3] = Long.valueOf(rec[3]);
			mothers[4] = Long.valueOf(rec[4]);
		} else {
			rankMap.put(brand, new Long(lCount));//<�u�����h�A����>�̃}�b�v
		}
		Long l1 = Long.valueOf(rec[1]);//���z
		Long l2 = Long.valueOf(rec[2]);//����
		Long l3 = Long.valueOf(rec[3]);//�e��
		Long l4 = Long.valueOf(rec[4]);//�w���Ґ�

		oRec[0] = brand;//Brand
		oRec[1] = exFormat2.format(l1 * 1.0 / l2);//���ϒP��
		oRec[2] = exFormat2.format(l4 * 100.0 / monCount);//�w����

		wRec = edit(l1, 1);//���z
		oRec[3] = wRec[0];//100�l����
		oRec[4] = wRec[1];//�V�F�A
		oRec[5] = wRec[2];//�ݐσV�F�A

		wRec = edit(l2, 2);//����
		oRec[6] = wRec[0];
		oRec[7] = wRec[1];
		oRec[8] = wRec[2];

		wRec = edit(l3, 3);//�e��
		oRec[9] = wRec[0];
		oRec[10] = wRec[1];
		oRec[11] = wRec[2];

		lCount++;
		return oRec;
	}

	String[] edit(Long chile, int idx) {
		String[] wRec = new String[3];
		//		Long chile = Long.valueOf(val);//���z
		Double share = calcShare(chile, mothers[idx]);
		if (lCount > 0)
			rShare[idx] += share;
		wRec[0] = exFormat2.format(chile * 100.0 / monCount);
		wRec[1] = exFormat2.format(share);
		wRec[2] = exFormat2.format(rShare[1]);
		return wRec;
	}

	Double calcShare(Long chile, Long mother) {
		Double share = chile * 100.0 / mother;// each values
		return share;
	}

	// ------------------------------------------------------------------------
	// fin
	// ------------------------------------------------------------------------
	@Override
	public void fin() {
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		testForHVShare();
	}

	// -------------------------------------------------------------------------
	// �P�̃e�X�g�@
	// -------------------------------------------------------------------------
	public static void testForHVShare() {
		String inPath = "C:/@qpr/home/123620000036/calc/loyHead.txt";
		String outPath = "C:/@qpr/home/123620000036/calc/HVShare.txt";
		Inf_ArrayCnv cnv = new ShareCnv4Ranking(17702);
		EzReader reader = new EzReader(inPath, cnv);
		new Common_IO(outPath, reader).execute();
	}

}
