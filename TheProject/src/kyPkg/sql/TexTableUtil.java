package kyPkg.sql;

import java.util.Collections;
import java.util.List;

import kyPkg.uFile.DosEmu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TexTableUtil {
//	public static Log log = LogFactory.getLog("kyPkg.sql.TexTableUtil.class");
	public static Log log = LogFactory.getLog(TContainer.APPENDA_CLASS);

	// -------------------------------------------------------------------------
	// �e�L�X�g�f�[�^�̐擪�Z�����L�[�Ƃ��ėݐύX�V����
	// �����C�A�E�g�͖��Ȃ����}�X�^�[�̃Z�����ɍ��킹����
	// ���}�X�^�[�����݂��Ȃ��ꍇ�͈�Ԑ擪�̃t�@�C���̕��̃e�[�u�������������
	// -------------------------------------------------------------------------
	// �p�����[�^�T�v
	// �၄TexTableUtil.mergeTable(outPath, dbObj, "qpr_monitor_base", pathList);
	// outPath ���ʏo�͐�f�B���N�g��
	// TexDb dbObj �f�[�^�x�[�X�C���^�t�F�[�X
	// dstTable �}�X�^�[�Ƃ���e�[�u��
	// pathList ���̓f�[�^�p�X�̃��X�g
	// -------------------------------------------------------------------------
	// public static TContener importTable(Inf_TexDb dbObj, String iTable,String
	// srcPath) {
	// TContener contener = null;
	// // -------------------------------------------------------------
	// // �t�@�C������e�L�X�g�e�[�u���𐶐�����
	// // -------------------------------------------------------------
	// String path = dbObj.getPath(srcPath);
	// contener = new TContener(iTable, path, 0);
	// String iDefs = contener.getfDefs();
	// dbObj.createTable(iTable, iDefs);
	// // -------------------------------------------------------------
	// // �t�@�C�����e�[�u���ɃA�T�C��
	// // -------------------------------------------------------------
	// log.info("�t�@�C�����e�[�u���ɃA�T�C��");
	// dbObj.assignIt(iTable, srcPath);
	// return contener;
	// }


	private static void test_mmMerge_Shop() {
		String dbDir = "c:/shops";
		Inf_TexDb dbObj = new JDBC_Sqlite("QPR", dbDir);
		dbObj.setLog(log); // for Debug
		String dataDir = dbObj.getDataDir();
		System.out.println("dataDir:" + dataDir);
		List pathList = DosEmu.dir(dataDir + "Shop*.CSV", false);
		if (pathList != null)
			Collections.sort(pathList);
		String outPath = dataDir + "Shop_out.txt";
		String oTable = "Shop_master";
		log.info("#test_mmMerge_Shop START #");
		log.info("���@�����}�X�^�[�Ƀ}�[�W");
		if (mergeShopTable(dbObj, oTable, pathList)) {
			log.info("���@�e�[�u�����t�@�C���ɃG�N�X�|�[�g����:" + outPath);
			String oKey = dbObj.getField(oTable, 0);
			dbObj.exportIt(dbObj.getPath(outPath), "\t", oTable, oKey, "*", oKey, "");
		} else {
			log.info(" �f�[�^�͑��݂��܂���ł���");
		}
		dbObj.close();
		log.info("#End test_mmMerge_Shop    #");

	}

	private static boolean mergeShopTable(Inf_TexDb dbObj, String oTable,
			List<String> pathList) {
		int oCol = 0;
		int iCol = 0;
		String sql = "";
		String iTable = "tmpMerge";
		String oKey = "";// fld0
		String iKey = "";// fld0
		String iFlds = "*";
		String oFlds = "*";
		if (pathList.size() > 0) {
			for (int i = 0; i < pathList.size(); i++) {
				String srcPath = pathList.get(i);
				log.info("�@�@�e�[�u��'" + iTable + "'�Ɂy���̓f�[�^�z�����蓖�Ă�");
				// -------------------------------------------------------------
				// �����f�[�^���C���|�[�g
				TContainer contener = dbObj.importTable(iTable, srcPath);
				String iDefs = contener.getfDefs();
				iKey = dbObj.getField(iTable, 0);// ��Ԑ擪�̃J�������h�c(�L�[)�Ƃ���
				// -------------------------------------------------------------
				// �}�X�^�[�e�[�u���Ɋւ��鏈���i���݂��Ȃ���ΐ����j
				if (i == 0) {
					if (!dbObj.isExist(oTable)) {
						log.info("�@�@�y�}�X�^�e�[�u���z" + oTable + "�𐶐�");
						sql = "CREATE TABLE " + oTable + " (" + iDefs + ")";
						dbObj.executeUpdate(sql);
					}
					oKey = dbObj.getField(oTable, 0);
					oCol = dbObj.getColumnCount(oTable);
					oFlds = dbObj.getFields(oTable);
					log.info("�@�@���}�X�^ �F " + oCol + "�J����");
					// log.info("�@�@���}�X�^ Flds:" + oFlds);
				}
				iCol = contener.getMaxCol();
				if (iCol > oCol) {
					log.fatal("�@�@��~*�@�f�[�^�̃J������ERROR�I�I �}�X�^�[�����́i" + iCol + "�J�����j");
					System.exit(999);// ��~���J�o�[���ɂ����Ȃ����H�H
				} else {
					log.info("�@�@�����́@�F " + iCol + "�J�����i�Z�������������Ƃ��̓_�~�[�inull�j�ŕ₤�j");
					iFlds = contener.getFields(oCol);
					// log.info("�@�@������Flds:" + iFlds);
					// -------------------------------------------------------------
					// �}�X�^�[�Ƀ}�[�W����
					log.info("�@�@�}�X�^�[�C���[�W��'" + iTable + "'���}�[�W");
					dbObj.tableMerge(oTable, oKey, oFlds, iTable, iKey, iFlds);
				}
				// �t�@�C�����폜
				dbObj.dropTable(iTable);
			}
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] agrv) {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("TexTableUtil");
		elapse.start();
		System.out.println("- start!�@ -");

		test_mmMerge_Shop();
		// testHsqlTexTable();
		// testSqliteTexTable();

		System.out.println("- finish! -");
		elapse.stop();
	}

	public static void testHsqlTexTable() {
		Inf_TexDb dbObj = new JDBC_HSQLDB("C:/dataBase");
		dbObj.reloadIt("QPR_MONITOR_BASE", "qpr_monitor_out0716.dat", true);
		dbObj.unloadIt("unloadIt0716.txt", "QPR_MONITOR_BASE");
	}

	public static void testSqliteTexTable() {
		Inf_TexDb dbObj = new JDBC_Sqlite("QPR", "C:/dataBase");
		dbObj.reloadIt("QPR_MONITOR_BASE", "unloadIt.txt", true);
		dbObj.unloadIt("C:/dataBase/unloadIt.txt", "QPR_MONITOR_BASE");
	}
}