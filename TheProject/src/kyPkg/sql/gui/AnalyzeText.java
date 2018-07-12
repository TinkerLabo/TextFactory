package kyPkg.sql.gui;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import kyPkg.uFile.FileUtil;

public class AnalyzeText {
	public static final String preFix = "fld";
	public static final String TYPE = "type";
	public static final String NAME = "name";

	// ---+---------+---------+---------+---------+---------+---------+---------+
	// �e�L�X�g�t�@�C�����e�[�u���ɃC���|�[�g����
	// ---+---------+---------+---------+---------+---------+---------+---------+
	public static HashMap<String, List<Object>> analyzeIt(String path,
			String encode, String delimiter, int checkCount,
			boolean headOption) {
		if (encode.equals("")){
//			encode = System.getProperty("file.encoding");
			encode = FileUtil.getDefaultEncoding();//20161222
		}

		int cnt = 0;
		String[] arrays;
		int[] eachMaxLen = null;
		String token = "";

		HashMap<String, List<Object>> map = new HashMap<String, List<Object>>();
		map.put(NAME, new ArrayList());
		map.put(TYPE, new ArrayList());
		String wRec = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(path), encode));
			while ((wRec = br.readLine()) != null) {
				cnt++;
				if (cnt > checkCount)
					break;
				arrays = wRec.split(delimiter);
				if (cnt == 1) {
					// �e�s�̒������܂��܂����ƍ���̂����E�E�E�E
					eachMaxLen = new int[arrays.length];
					// 1�s�ڂɃw�b�_�[��񂪂���ꍇ
					if (headOption == true) {
						StringTokenizer tok = new StringTokenizer(wRec,
								delimiter);
						while (tok.hasMoreTokens()) {
							token = tok.nextToken();
							String[] elm = token.split(" ");
							if (elm[0].startsWith("*")) { // *�Ŏn�܂鍀�ڂ́A���l�Ƃ݂Ȃ�
								map.get(NAME).add(elm[0].substring(1).trim()); // *�����̕��������o��
								map.get(TYPE).add("INTEGER");
							} else {
								map.get(NAME).add(elm[0].trim());
								if (elm.length > 1) {
									String wType = elm[1].trim().toUpperCase();
									if (wType.equals("INTEGER")) {
										map.get(TYPE).add(wType);
									} else if (wType.startsWith("VARCHAR")) {
										map.get(TYPE).add(wType);
									}
								} else {
									map.get(TYPE).add("VARCHAR");
								}
							}
						}
						// ���̃I�v�V�������w�肳�ꂽ�ꍇ�A�t�@�C���̓��e�͒������Ȃ�
						break;
					}
				}
				// �t�@�C���̒��g�𒲂ׂ�
				for (int i = 0; i < arrays.length; i++) {
					// ��Ԓ���������̒����𒲂ׂ�(�X�y�[�X�������̓��ɓ����)
					if (i < eachMaxLen.length) {
						String wk = arrays[i];
						if (isNumeric(wk.trim()) == false) {
							if (eachMaxLen[i] < wk.length())
								eachMaxLen[i] = wk.length();
						}
					}
				}
			}
			for (int seq = 0; seq < eachMaxLen.length; seq++) {
				map.get(NAME).add(preFix + seq);
				if (eachMaxLen[seq] == 0) {
					map.get(TYPE).add("INTEGER");
				} else {
					map.get(TYPE).add("VARCHAR(" + eachMaxLen[seq] + ")");
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return map;
	}

	/**
	 * ���p���������񂩂ǂ���
	 * 
	 * @param pStr
	 *            ����������
	 * @return ���p�����݂̂̕�����Ȃ�true
	 */
	private static boolean isNumeric(String pStr) {
		// System.out.println("isNumeric in :" + pStr);
		boolean wRtn = false;
		if (pStr.matches("[+-]*\\d+[.]*\\d*")) {
			// System.out.println("�`�F�b�N���ʁF���p�����݂̂̕�����ł��B");
			wRtn = true;
		} else {
			// System.out.println("�`�F�b�N���ʁF���p�����ȊO�̕������܂܂�܂��B");
			wRtn = false;
		}
		// System.out.println("isNumeric Ans:" + wRtn);
		return wRtn;
	}

}
