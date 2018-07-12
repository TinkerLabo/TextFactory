package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import kyPkg.task.Abs_BaseTask;

// 2016-06-16 yuasa
//�w�肵���J���������K�\���Ƀ}�b�`�����f�[�^�݂̂��o�͂���
public class RegFilter extends Abs_BaseTask {
	private Inf_oClosure writer = null;
	private String delimiter = null;
	private Pattern pattern;
	private int col = 0;
	private List<String> inList;
	private long writeCount = 0;

	public long getWriteCount() {
		return writeCount;
	}

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------

	public RegFilter(String outPath, List<String> inList, int col,
			String regex) {
		this.col = col;
		this.inList = inList;
		this.pattern = Pattern.compile(regex);
		this.writer = new EzWriter(outPath);
	}

	// -------------------------------------------------------------------------
	// ���s
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("<<START>>");
		long wCnt = 0;
		writer.open();
		for (String inPath : inList) {
			loop(inPath);
		}
		writer.close();
	}

	public void loop(String inPath) {
		System.out.println("### @RegFilter:" + inPath);
		Inf_iClosure reader = new EzReader(inPath);
		reader.open();
		if (delimiter == null)
			delimiter = reader.getDelimiter();
		String[] splited = reader.readSplited();
		while (splited != null) {
			if (splited.length > col) {
				String target = splited[col];
				if (pattern.matcher(target).matches()) {
					writer.write(join(splited, delimiter));
					writeCount++;
				}
			}
			splited = reader.readSplited();
		}
		reader.close();
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		testRegFilter();
	}

	public static void testRegFilter() {
		String wkDir = "C:/samples/�����e�X�g/";
		String inPath = wkDir + "SYOMSDT_1605.TXT";
		String targetPath = wkDir + "45or49.txt";
		String regex = "^49.*|^45.*";//49�܂���45�Ŏn�܂���̂Ɍ���
		int col = 0;
		List<String> inList = new ArrayList();
		inList.add(inPath);
		new RegFilter(targetPath, inList, col, regex).execute();
	}
}
