package kyPkg.filter;

import java.io.*;

public class Flt_Base_Classic extends Abs_FileFilter implements Inf_FileConverter {
	public Flt_Base_Classic(){
		super(true);
	}
	// -------------------------------------------------------------------------
	// �t�B���^�[�����E�E�E���̃v���O�����͐��`�ł��E�E�E�@2008-07-29
	// -------------------------------------------------------------------------
	@Override
	public void fileConvert(String outPath, String inPath) {
		super.open_I(inPath);
		super.open_O(outPath,false);
		super.setDelimiter("\t");
		execute();
	}
	// -------------------------------------------------------------------------
	// filterT �t�B���^�[�v���O����
	// �w�肳�ꂽ�X�g���[����ǂݍ���ŁA�X�g���[���֏����o��
	// �� boolean swt = filterT( new StringReader(jTa1.getText()) );
	// -------------------------------------------------------------------------
	@Override
	public boolean execute() {
		String delimiter= super.getDelimiter();
		Reader reader = super.getReader();
		Writer writer = super.getWriter();
		try {
			StringBuffer buff = new StringBuffer();
			BufferedReader br = new BufferedReader(reader);
			String wRec = "";
			while ((wRec = br.readLine()) != null) {
				if (!wRec.trim().equals("")) {
					String[] array = wRec.split(delimiter);
					if (array.length>0){
						buff.append(array[0]);
						buff.append(System.getProperty("line.separator"));
					}
					writer.write(buff.toString());
					buff.delete(0, buff.length());
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return true;
	}
}
