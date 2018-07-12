package kyPkg.Sorts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import kyPkg.filter.EzReader;
import kyPkg.filter.EzWriter;
import kyPkg.filter.Inf_iClosure;
import kyPkg.filter.Inf_oClosure;
import kyPkg.filter.Reader2List;
import kyPkg.task.Abs_BaseTask;

// ----------------------------------------------------------------------------
//�@�\�[�g�p�����[�^�̃J�����w���0���͂��߂�i1�ł͂Ȃ��I�I�j
// ----------------------------------------------------------------------------
public class IncoreSort_Ex extends Abs_BaseTask {
	private ArrayComparator multiComparator = null;
	private String delimiter = null;
	private List<Inf_iClosure> readerList;
	private Inf_oClosure writer = null;
	boolean opt1st = false;//�P���R�[�h�ڂ�ޔ���������ꍇtrue�@�i���בւ��̑Ώۂɂ��Ȃ��j
	//	boolean opt2nd = false;//2���R�[�h�ڂ�ޔ���������ꍇtrue�@�i���בւ��̑Ώۂɂ��Ȃ��j
	boolean optLast = false;//�ŏI���R�[�h��ޔ���������ꍇtrue�@�i���בւ��̑Ώۂɂ��Ȃ��j
	boolean optLast2top = false;//�ŏI���R�[�h��ޔ����Ă����Đ擪�ɏ悹��@�i���בւ��̑Ώۂɂ��Ȃ��j
	boolean optLast2second = false;//�ŏI���R�[�h��ޔ����Ă����Đ擪�ɏ悹��@�i���בւ��̑Ώۂɂ��Ȃ��j
	private List<String> prefix = null;

	public void setPrefix(List<String> prefix) {
		this.prefix = prefix;
	}

	public void setSuffix(List<String> suffix) {
		this.suffix = suffix;
	}

	private List<String> suffix = null;
	private HashMap<String, Integer> seqMap;//20170525
	public HashMap<String, Integer> getSeqMap() {
		return seqMap;
	}

	public void setOptLast2top(boolean optLast2top) {
		this.optLast2top = optLast2top;
	}

	public void setOptLast2Second(boolean optLast2top) {
		this.optLast2second = optLast2top;
	}

	public void setOpt1st(boolean opt1st) {
		this.opt1st = opt1st;
	}
	//	public void setOpt2nd(boolean opt1st) {
	//		this.opt2nd = opt1st;
	//	}

	public void setOptLast(boolean optLast) {
		this.optLast = optLast;
	}

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------

	public IncoreSort_Ex(String inPath) {
		this("0,String,asc", inPath, inPath);
	}

	public IncoreSort_Ex(String sortParam, String ioPath) {
		this(sortParam, ioPath, ioPath);
	}

	public IncoreSort_Ex(String sortParam, String outPath, String inPath) {
		this(sortParam, new EzWriter(outPath), new EzReader(inPath));
	}

	public IncoreSort_Ex(String sortParam, Inf_oClosure writer,
			Inf_iClosure reader) {
		List<Inf_iClosure> readers = new ArrayList();
		readers.add(reader);
		incore(writer, readers, sortParam);
	}

	private void incore(Inf_oClosure writer, List<Inf_iClosure> readerList,
			String sortParam) {
		this.writer = writer;
		this.multiComparator = new ArrayComparator(sortParam);
		this.readerList = readerList;
	}

	public long getWriteCount() {
		return this.writer.getWriteCount();
	}

	// -------------------------------------------------------------------------
	// ���s
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("IncoreSort�J�n");
		// ---------------------------------------------------------------------
		// list in core �������[�I�[�o�[�̉\��������̂Œ��ӂ���i�΍�͐扄�΂��E�E�Eummmm�j
		// ---------------------------------------------------------------------
		Reader2List preSort = new Reader2List(readerList);
		List<String[]> arrayList = preSort.readers2ArrayList();// ���̓f�[�^�̂��ׂĂ����X�g�ɓǂݍ���

		String[] line1st = null;
		String[] line2nd = null;
		String[] last = null;
		if (arrayList.size() > 0)
			line1st = arrayList.get(0);//�擪�ɂ��鍇�v�s�����o���Đ擪�ɕt��������ꍇ
		if (arrayList.size() > 1)
			line2nd = arrayList.get(1);//�擪�ɂ��鍇�v�s�����o���Đ擪�ɕt��������ꍇ
		last = arrayList.get(arrayList.size() - 1);//Ex:��w�������o���čŌ���Ɏ��t����ꍇ

		if (opt1st)
			arrayList.remove(0);//�P�s�߁i���v�j����菜���āE�E�E
		//		if (opt2nd)
		//			arrayList.remove(1);//2�s�ځi��w���j����菜���āE�E�E
		//optLast=>�ŏI�s�i��w���j����菜���āE�E�E�ŏI�s�ɒǉ�
		//optLast2top=>�ŏI�s�i��w���j����菜���āE�E�E�擪�ɒǉ�
		if (optLast || optLast2top || optLast2second) {
			if (arrayList.size() > 0)
				arrayList.remove(arrayList.size() - 1);
		}

		delimiter = preSort.getDelimiter();// ��ԍŌ�ɓǂݍ��񂾃f�[�^�̋�؂蕶���ɂȂ��Ă���
		// ---------------------------------------------------------------------
		// sort
		// ---------------------------------------------------------------------
		Collections.sort(arrayList, multiComparator);

		if (optLast)
			arrayList.add(last);//�ޔ������ŏI�s�i��w���j���Ō���ɒǉ�����

		// ---------------------------------------------------------------------
		// output
		// ---------------------------------------------------------------------
		// �d���f�[�^�̍폜�����邩�ǂ����H
		// ---------------------------------------------------------------------
		writer.setDelimiter(delimiter);
		writer.open();

		// ---------------------------------------------------------------------
		//	�擪�ɕt�������s����	20150917
		// ---------------------------------------------------------------------
		if (prefix != null) {
			for (String element : prefix) {
				writer.write(element);
			}
		}

		if (optLast2top)
			writer.write(last, 1);//�ޔ������ŏI�s�i���v�j��擪�ɒǉ�����

		if (opt1st)
			writer.write(line1st, 1);//�ޔ������擪�s�i���v�j��擪�ɒǉ�����

		if (optLast2second)
			writer.write(last, 1);//�ޔ������ŏI�s�i���v�j��擪�ɒǉ�����

//		EzWriter writer1 = new EzWriter(path);
//		writer1.open();
//		writer1.close();

		seqMap = new HashMap();
		int seq = 0;
		if (arrayList.size() > 0) {
			for (String[] array : arrayList) {
//				System.err.println("#debug20170525#:" + array[0]+" seq:"+seq);
				seqMap.put(array[0], seq++);
				// System.out.println("@������IncoreSort0630 id:" + array[0] +
				// " share:" + array[1]+ " val:" + array[2]);
				writer.write(array, 1);
			}
		}

		// ---------------------------------------------------------------------
		//	�擪�ɕt�������s����
		// ---------------------------------------------------------------------
		if (suffix != null) {
			for (String element : suffix) {
				writer.write(element);
			}
		}

		writer.close();
	}

	// -------------------------------------------------------------------------
	// test
	// -------------------------------------------------------------------------
	private static void testIncoreSort() {
		boolean opt1st = true;//�P���R�[�h�ڂ�ޔ���������ꍇtrue�@�i���בւ��̑Ώۂɂ��Ȃ��j
		boolean optLast = false;//�ŏI���R�[�h��ޔ���������ꍇtrue�@�i���בւ��̑Ώۂɂ��Ȃ��j
		String sortParam = "1,Double,desc";
		String outPath = "C:/@qpr/home/123620000058/calc/#999_result.txt";
		String inPath = "C:/@qpr/home/123620000058/calc/#005_result.txt";
		IncoreSort_Ex ins = new IncoreSort_Ex(sortParam, outPath, inPath);
		ins.setOptLast(true);//�ŏI���R�[�h��ޔ���������ꍇtrue�@�i���בւ��̑Ώۂɂ��Ȃ��j
		ins.execute();
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		//		tester2011_1111();
		testIncoreSort();
	}

}
