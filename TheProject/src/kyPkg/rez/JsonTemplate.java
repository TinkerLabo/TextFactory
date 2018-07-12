package kyPkg.rez;

import java.util.List;

public class JsonTemplate {

	// var json1 =
	// {meta:{cells:[[
	// {name: 'Jan ', styles: 'text-align: left;', width: 8},
	// {name: 'JanName', styles: 'text-align: left;', width:25},
	// {name: 'Code ', styles: 'text-align: left;', width: 4},
	// {name: 'Name ', styles: 'text-align: left;', width:25},
	// {name: 'Price ', styles: 'text-align: right;', width: 5},
	// {name: 'Volume ', styles: 'text-align: right;', width: 5}
	// ]]},
	// data:[
	// ['4901005252264','�O���R ����R�R�A �N���[�����b�` 4��','01010','�C�g�[���[�J��','500','0'],
	// ['4901002014803','�r�a ���������p�b�N �`���[�n�� �S�W��','00001','�c�q�x','160','0'],
	// ['4901551230075','�J�l�{�E ���x�P�̊C�N�ܖڃ`���[�n���̑f �P�V�D�Q','00001','�c�q�x','140','0'],
	// ['4901577428272','�p�o�R���u�߂ăp���b�ƁI�`���[�n���̑f �P�S���~�S','99999','���ݒ肻�̑�','250','0'],
	// ['4901665006443','�^�� �j�̃`���[�n���̑f �T�D�W���~�R','99999','���ݒ肻�̑�','160','0'],
	// ['4901990102636','�}������� �`���[�n���̑f �K�[���b�N �S�O���~�R','99999','���ݒ肻�̑�','131','0'],
	// ['4902106658610','�~�c�J�� �`���[�n���̑f ���т˂� �P�W��','00001','�c�q�x','125','0'],
	// ['4902388047225','�i�J�� �|�P�����`���[�n���̑f���ɖ��ܖ� �� �R�o','00001','�c�q�x','130','0'],
	// ]};

	public static void main(String[] argv) {
		String[] title = { "Jan", "JanName", "Code", "Name", "Price", "Volume" };
		String[] align = { "", "", "", "", "r", "r" };
		int[] width = { 8, 25, 4, 25, 5, 5 };
		String rec[] = {
				"'4901005252264','�O���R�@����R�R�A�@�N���[�����b�`�@4��','01010','�C�g�[���[�J��','500','0'",
				"'4901005252264','�O���R�@����R�R�A�@�N���[�����b�`�@4��','01010','�C�g�[���[�J��','500','0'",
				"'4901005252264','�O���R�@����R�R�A�@�N���[�����b�`�@4��','01010','�C�g�[���[�J��','500','0'",
				"'4901005252264','�O���R�@����R�R�A�@�N���[�����b�`�@4��','01010','�C�g�[���[�J��','500','0'",
				"'4901005252264','�O���R�@����R�R�A�@�N���[�����b�`�@4��','01010','�C�g�[���[�J��','500','0'",
				"'4901005252264','�O���R�@����R�R�A�@�N���[�����b�`�@4��','01010','�C�g�[���[�J��','500','0'",
				"'4901005252264','�O���R�@����R�R�A�@�N���[�����b�`�@4��','01010','�C�g�[���[�J��','500','0'",
				"'4901005252264','�O���R�@����R�R�A�@�N���[�����b�`�@4��','01010','�C�g�[���[�J��','500','0'",
				"'4901005252264','�O���R�@����R�R�A�@�N���[�����b�`�@4��','01010','�C�g�[���[�J��','500','0'",
				"'4901005252264','�O���R�@����R�R�A�@�N���[�����b�`�@4��','01010','�C�g�[���[�J��','500','0'" };

		System.out.println("=>" + kyPkg.rez.JsonTemplate.jason4Grid(title,align,width,rec));
	}
	private static String[] int2strArray(int[] array){
		String[] Sarray = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			Sarray[i] = String.valueOf(array[i]);
		}
		return Sarray;
	}
	public static String jason4Grid(String[] title, String[] align,int[] width, String rec[]) {
		return jason4Grid(title, align, int2strArray(width), rec);
	}
	public static String jason4Grid(List lTitle, List lAlign,List lWidth, String rec[]) {
	    String[] title = (String[]) lTitle.toArray(new String[lTitle.size()]);
	    String[] align = (String[]) lAlign.toArray(new String[lAlign.size()]);
	    String[] width = (String[]) lWidth.toArray(new String[lWidth.size()]);
	    return jason4Grid(title,align,width,rec);
	}
	public static String jason4Grid(String[] title, String[] align,String[] width, String rec[]) {
		String suffix = "";
		String lf = "\n";
		StringBuffer writer = new StringBuffer();
		StringBuffer buff = new StringBuffer();
		// -----------------------------------------------------------------
		// meta
		// -----------------------------------------------------------------
		if (suffix.equals(""))
			writer.append("{");
		writer.append("meta" + suffix);
		writer.append(":{cells:[[");
		writer.append(lf);
		for (int i = 0; i < title.length; i++) {
			if (i > 0) {
				writer.append(",");
				writer.append(lf);
			}
			if (align[i].toUpperCase().startsWith("R")) {
				align[i] = "right";
			} else {
				align[i] = "left";
			}
			writer.append("{name: '" + title[i] + "', styles: 'text-align: "
					+ align[i] + ";', width: " + width[i] + "}");
		}
		writer.append("]]},");
		writer.append(lf);
		writer.append("data" + suffix);
		writer.append(":[");
		writer.append(lf);
		// -----------------------------------------------------------------
		// data
		// -----------------------------------------------------------------
		for (int v = 0; v < rec.length; v++) {
			String splited[] = rec[v].split(",");
			buff.delete(0, buff.length());
			if (v > 0) {
				writer.append(",");
				writer.append(lf);
			}
			buff.append("[");
			buff.append(splited[0]);
			for (int h = 1; h < splited.length; h++) {
				buff.append(",");
				buff.append(splited[h]);
			}
			writer.append(buff.toString());
			writer.append("]");
		}
		writer.append("]");
		if (suffix.equals(""))
			writer.append("}");
		writer.append(lf);
		return writer.toString();
	}
}
