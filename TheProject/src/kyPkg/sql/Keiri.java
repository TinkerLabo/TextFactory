package kyPkg.sql;

import java.util.List;

import globals.ResControl;

public class Keiri {
	//�����}�X�^�[�iCSV�j
	private static final String TRHKCD_TXT = "trhkcd.txt";
	public static String KEIRI_JURL = "JDBC:ODBC:DRIVER={SQL Server};SERVER=agcks07;DATABASE=keiri_db1";
	public static String KEIRI_USER = "sa";
	public static String KEIRI_PASS = "";

	// XXX �E�E�E�A�N�Z�X�����̖��œǂ߂Ȃ����Ƃ�����E�E�E���̏ꍇ�ǂ����ɃL���b�V�����Ă����Ă������܂��邩�ˁH
	// �����}�X�^�[��ǂݍ���
	public static String getTorihikisaki() {
		String path = ResControl.getQPRHomeQprRes(TRHKCD_TXT);
		String regex = null;
		if (regex == null || regex.trim().equals(""))
			regex = "";
		String sql = "";
		String where = "";
		// XXX limit�Ɂ��Ƃ�����Ƃ܂����̂��A�C���W�F�N�V�����Ή��͂���̂��H�I
		if (regex.equals("")) {
		} else if (regex.matches("[0-9]*")) {
			where = "and trhkcd like '" + regex + "%'";
		} else {
			// XXX �r�[���@�ˁ@�r�|������Ȃ��ƃ_���������肷��
			where = "and kanjnm like '%" + regex + "%'";
		}
		String jURL = KEIRI_JURL;
		String user = KEIRI_USER; // ���[�U��
		String pass = KEIRI_PASS; // �p�X���[�h
		sql = "SELECT trhkcd, + '' + kanjnm FROM trms01a WHERE delflag <> 9 "
				+ where + "  ORDER BY trhkcd";
		ServerConnecter cnn = new ServerConnecter(jURL, user, pass);
		int count = cnn.query2File(path, sql);
		return path;
	}

	public static List<List> searchTRHKxxx_org(String regex) {
		List<List> matrix = null;
		if (regex == null || regex.trim().equals(""))
			regex = "";

		String sql = "";
		String where = "";

		// XXX limit�Ɂ��Ƃ�����Ƃ܂����̂��A�C���W�F�N�V�����Ή��͂���̂��H�I
		if (regex.equals("")) {
		} else if (regex.matches("[0-9]*")) {
			where = "and trhkcd like '" + regex + "%'";
		} else {
			// XXX �r�[���@�ˁ@�r�|������Ȃ��ƃ_���������肷��
			where = "and kanjnm like '%" + regex + "%'";
		}
		String jURL = KEIRI_JURL;
		String user = KEIRI_USER; // ���[�U��
		String pass = KEIRI_PASS; // �p�X���[�h
		sql = "SELECT trhkcd + ' ' + kanjnm FROM trms01a WHERE delflag <> 9 "
				+ where + "  ORDER BY trhkcd";

		ServerConnecter cnn = new ServerConnecter(jURL, user, pass);
		matrix = cnn.query2Matrix(sql);
		return matrix;
	}

	public static void testQuery2File() {
		String path = "c:/result.txt";
		String jURL = KEIRI_JURL;
		String user = KEIRI_USER; // ���[�U��
		String pass = KEIRI_PASS; // �p�X���[�h
		String sql = "SELECT trhkcd, + '' + kanjnm FROM trms01a WHERE delflag <> 9 ";
		ServerConnecter cnn = new ServerConnecter(jURL, user, pass);
		int count = cnn.query2File(path, sql);
	}

	public static void main(String[] argv) {
		Keiri.getTorihikisaki();

	}

}
