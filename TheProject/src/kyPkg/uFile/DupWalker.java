package kyPkg.uFile;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

//GUI�����
//��ԕʍ폜�J�E���g������
//�������̂̃t�@�C���̃����L���O�E�E�E�ǂ�ȃt�@�C���������łǂꂮ�炢�g���Ă���̂��H
//�폜�ΏۊO�Ƃ���t�@�C���̊g���q���w�肵�����E�E�E
//�������͍폜�ΏۂƂ���t�@�C���̃��W�b�N�X�H

//�����ă����N���c���H�I
//�����N�悪�Ȃ��t�@�C���͏����H
//URL�t�@�C����ҏW����H�H
//���b�Z�[�W��Ԃ��H���O���c���H�H
public class DupWalker {
	private static boolean killer = false;

	// ---------------------------------------------------------------
	// css�Ƃ��T���v���t�@�C���ȂǁA�����t�@�C�����ė��p����Ă���P�[�X�����O������@���Ȃ����낤���H�H
	// �f�B���N�g���̍\�����������̂��O���[�s���O������@�͂Ȃ����H�H
	// �@�w�肵���t�@�C�����폜�A���ʓ��Y�f�B���N�g���Ƀt�@�C�������݂��Ȃ��ꍇ�ɂ��̃f�B���N�g�����폜����
	// ---------------------------------------------------------------

	// ��t String wFinfo = FileUtil.FilelastModified("System.ini");
	public static String filelastModified(String pPath) {
		File wFile = new File(pPath);
		String wDate = DateFormat.getDateInstance()
				.format(new Date(wFile.lastModified()));
		SimpleDateFormat df1 = new SimpleDateFormat("hh:mm");
		String wTime = df1.format(new Date(wFile.lastModified()));
		return wDate + "_" + wTime;
	}

	// ��t long size = FileUtil.getFileSize("System.ini");
	public static long getFileSize(String pPath) {
		File wFile = new File(pPath);
		long size = wFile.length(); // " KByte
		return size;
	}

	// -------------------------------------------------------------------------
	// �啶����������B���Ƀ}�b�`���O�������ꍇ���Q
	// �����p�^�[����������g���ꍇ�͂����炪�x�^�[
	// �s�g�p��t
	// Pattern ptn = Regexx.patternIgnoreCase(pRegex);
	// wFlg = ptn.matcher(pCharSeq).find();
	// -------------------------------------------------------------------------
	// �啶�����������B���ȃp�^�[�����쐬
	// -------------------------------------------------------------------------
	// private static Pattern patternIgnoreCase(String pRegex) {
	// return Pattern.compile(pRegex, Pattern.CASE_INSENSITIVE);
	// }

	// -------------------------------------------------------------------------
	// �R�}���h�����s����
	// �����s�O�ɑΏۂ��m�F������@���~�����E�E�E
	// �����ʂ��o�b�t�@�[�ɒ��߂邩�ǂ��� �o�[�{�E�Y�I�v�V�����H�I
	// ���Ώۂ����b�N����Ă���A�A�N�Z�X�����Ȃǂ̃G���[�����E�E�E�Ȃǂ̖��
	// -------------------------------------------------------------------------
	private static List xCommand(String pSrc) {
		if (pSrc == null)
			return null;
		pSrc = pSrc.replaceAll("\\\\", "/");
		if (pSrc.lastIndexOf("/") == -1)
			pSrc = pSrc + "/";
		int xIdx = pSrc.lastIndexOf("/");
		String wPath1 = pSrc.substring(0, xIdx + 1);
		System.out.println("xCommand :" + wPath1);
		// Digger2 insDig = new Digger2(wPath1, "", true,killer);
		Digger2 insDig = new Digger2(wPath1, "", true, killer);
		insDig.search();
		String wSrc = "";
		Iterator it = insDig.iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			wSrc = it.next().toString();
			boolean wFlg = false;
			list.add(wSrc);
			wFlg = true;
			if (wFlg == false) {
				System.out.println("Error @xCommand :dir");
				System.out.println("wSrc :" + wSrc);
			}
		}
		return list;
	}

	public static void dupWalker(String path, boolean flag) {
		LogWriter logIns = new LogWriter("./dupWalker.log");
		logIns.startMessage("<< dupWalker Start!!>> path:" + path);

		int killCount = 0;
		int killDirCount = 0;
		killer = flag;
		// ���ʁA��ɂȂ����f�B���N�g��������������
		HashMap<String, List> hmap = new HashMap();
		List list = xCommand(path);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			String pathy = (String) iterator.next();
			String fileName = FileUtil.getFileName(pathy);
			List value = (List) hmap.get(fileName);
			if (value == null) {
				value = new ArrayList();
				hmap.put(fileName, value);
			}
			String lastMod = filelastModified(pathy);
			long size = getFileSize(pathy);
			String wkStr = lastMod + "\t" + size + "\t" + pathy;
			value.add(wkStr);
		}
		//		System.out.println("---------------------------------------------------");
		logIns.println("---------------------------------------------------");
		java.util.Set set = hmap.entrySet();
		java.util.Iterator it = set.iterator();
		while (it.hasNext()) {
			java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
			String key = (String) ent.getKey();
			List valList = (List) ent.getValue();
			// System.out.println("key:" + key + "  size:" + valList.size());
			if (valList.size() > 1) {

				//				System.out.println("size:" + valList.size() + "�@key:" + key);
				logIns.println("size:\t" + valList.size() + "\tkey:\t" + key);
				// XXX size update path-length, contents
				// �����T�C�Y�Ɠ��t�������Ȃ�[���p�X�̑����폜����
				Collections.sort(valList);
				String prePathx = "";
				String preLastMot = "";
				long preSize = -1;
				for (Iterator iterator = valList.iterator(); iterator
						.hasNext();) {
					String rec = (String) iterator.next();
					String[] splited = rec.split("\t");
					String lastMod = splited[0];
					long size = Long.parseLong(splited[1]);
					String pathx = splited[2];
					//					System.out.println("   lastMod:" + lastMod  + " pathx:" + pathx + " size:" + size);
					//					logIns.println("\t\t   lastMod:" + lastMod  + " pathx:" + pathx + " size:" + size);
					if (size == preSize) {
						if (lastMod.equals(preLastMot)) {
							// �T�C�Y�������ŕҏW���t�������Ȃ�A�Е�����
							//							System.out.println("�@�@�@�@�@=>preLastMot:" + preLastMot + " size:" + preSize + " kill:" + prePathx);
							//							logIns.println("\t\t  =>preLastMot:" + preLastMot + " size:" + preSize + " kill:" + prePathx);
							if (killer) {
								killDirCount += kyPkg.uFile.Digger2
										.chainReaction(prePathx);
							}
							killCount++;
							// ���̐e�̊K�w�𒲂ׂĂ��̔z���̃t�@�C�������O�Ȃ�΁A���̃t�H���_���폜����

						} else {
							// �T�C�Y�������œ��t���Ⴄ�ꍇ�A���e�������Ȃ�A�Е�����
							int diff = new kyPkg.tools.HexCompare(prePathx,
									pathx, 1).compare();
							if (diff == 0) {
								//								System.out.println("�@�@�@�@�@=>preLastMot:" + preLastMot + " size:" + preSize + " kill:" + prePathx);
								//								logIns.println("\t\t  =>preLastMot:" + preLastMot + " size:" + preSize + " kill:" + prePathx);
								if (killer) {
									killDirCount += kyPkg.uFile.Digger2
											.chainReaction(prePathx);
								}
								killCount++;
							}
						}
					}
					// System.out.println("path:"+pathx+" =>lastMod:"+lastMod+" size:"+size);
					prePathx = pathx;
					preLastMot = lastMod;
					preSize = size;
				}

			}
		}
		System.out
				.println("---------------------------------------------------");
		System.out.println("killCount:" + killCount);
		System.out.println("killDirCount:" + killDirCount);
		logIns.println("---------------------------------------------------");
		logIns.println("killCount   :" + killCount);
		logIns.println("killDirCount:" + killDirCount);
		logIns.close();
	}

	public static void main(String argv[]) {
		String path = "/Users/ken/debug/";
		path = "/Volumes/HD20GB/forWin/";
		dupWalker(path, true);
	}

}
