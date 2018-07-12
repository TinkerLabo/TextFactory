package kyPkg.Sorts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import globals.ResControl;
import kyPkg.filter.EzReader;
import kyPkg.filter.EzWriter;
import kyPkg.filter.Inf_iClosure;
import kyPkg.filter.Inf_oClosure;
import kyPkg.filter.Reader2List;
import kyPkg.task.Abs_BaseTask;

// ----------------------------------------------------------------------------
//�@�\�[�g�p�����[�^�̃J�����w���0���͂��߂�i1�ł͂Ȃ��I�I�j
// ----------------------------------------------------------------------------
public class IncoreSort extends Abs_BaseTask {
	private ArrayComparator multiComparator = null;
	private String delimiter = null;
	private List<Inf_iClosure> readerList;
	private Inf_oClosure writer = null;
	private long limit = Long.MAX_VALUE;
	private long wCount = 0;

	public void setLimit(long limit) {
		this.limit = limit;
	}

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public IncoreSort(String inPath) {
		this("0,String,asc", inPath, inPath);
	}

	private IncoreSort(String sortParam, String ioPath) {
		this(sortParam, ioPath, ioPath);
	}

	public IncoreSort(String sortParam, String outPath, String inPath) {
		this(sortParam, new EzWriter(outPath), new EzReader(inPath));
	}

	public IncoreSort(String sortParam, Inf_oClosure writer,
			Inf_iClosure reader) {
		List<Inf_iClosure> readers = new ArrayList();
		readers.add(reader);
		incore(writer, readers, sortParam);
		wCount = 0;
	}

	private void incore(Inf_oClosure writer, List<Inf_iClosure> readerList,
			String sortParam) {
		this.multiComparator = new ArrayComparator(sortParam);
		this.readerList = readerList;
		this.writer = writer;
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
		delimiter = preSort.getDelimiter();// ��ԍŌ�ɓǂݍ��񂾃f�[�^�̋�؂蕶���ɂȂ��Ă���
		// ---------------------------------------------------------------------
		// sort
		// ---------------------------------------------------------------------
		Collections.sort(arrayList, multiComparator);
		// ---------------------------------------------------------------------
		// output
		// ---------------------------------------------------------------------
		// �d���f�[�^�̍폜�����邩�ǂ����H
		// ---------------------------------------------------------------------
		//		System.out.println("#IncoreSort# output");
		writer.setDelimiter(delimiter);
		writer.open();
		if (arrayList.size() > 0) {
			for (String[] array : arrayList) {
				if (wCount++ > limit)
					break;//20161110 limit�����ǉ�
				//				 System.out.println("@ IncoreSort [0]:" + array[0] +
				//				 " [1]:" + array[1]+ " [2]:" + array[2]);
				writer.write(array, 1);
			}
		}
		writer.close();
	}

	// -------------------------------------------------------------------------
	// test
	// -------------------------------------------------------------------------
	public static void test1() {
		new kyPkg.Sorts.IncoreSort(ResControl.D_DAT + "NQFACE.DAT")
				.execute();
		new kyPkg.Sorts.IncoreSort(ResControl.D_DAT + "NQFACE.DAT")
				.execute();
	}

	public static void test2() {
		String sortParam = "0,String,A,1,int,D";
		String outPath = ResControl.D_DAT + "sorted.txt";
		new kyPkg.Sorts.IncoreSort(sortParam, outPath).execute();
	}

	public static void test3() {
		String userDir = globals.ResControl.getQPRHome();
		String inPath = userDir + "MapReduce1.txt";
		String outPath = userDir + "xxxsortTest.txt";
		String sortParam = "2,int,A,3,int,D";
		new kyPkg.Sorts.IncoreSort(sortParam, outPath, inPath).execute();
	}

	public static void tester0203() {
		String userDir = globals.ResControl.getQPRHome();
		String inPath = userDir + "148247000005/MapReduce1.txt";
		String outPath = userDir + "148247000005/sortOut.txt";
		String sortParam = "1,String,A,0,String,A,2,int,A";
		new kyPkg.Sorts.IncoreSort(sortParam, outPath, inPath).execute();
	}

	public static void tester2011_1111() {
		String inPath = "C:/ejqp7/862573000026/tran/20110801_20111031.trn";
		String outPath = "C:/ejqp7/862573000026/tran/20110801_20111031.dbg";
		String sortParam = "0,String,A,1,int,D";
		new kyPkg.Sorts.IncoreSort(sortParam, outPath, inPath).execute();
	}

	public static void test0601() {
		// �ʂ����ĉ������炢�Ȃ���Ȃ��\�[�g�ł���̂��E�E�E
		String inPath = ResControl.D_QPR + "TESTDAT/Area3.txt";
		String outPath = ResControl.D_QPR + "TESTDAT/Area3X.txt";
		String sortParam = "3,String,A";// �S�Z���ځAJANCODE����
		new kyPkg.Sorts.IncoreSort(sortParam, outPath, inPath).execute();
	}

	public static void testIncoreSort() {
		String sortParam = "1,Double,desc";
		String outPath = "C:/@qpr/home/123620000049/calc/#004_sorted0.txt";
		String inPath = "C:/@qpr/home/123620000049/calc/#003_MapReduce2.txt";
		new IncoreSort(sortParam, outPath, inPath).execute();
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		//		tester2011_1111();
		testIncoreSort();
	}

}
