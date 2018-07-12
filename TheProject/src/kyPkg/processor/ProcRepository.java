package kyPkg.processor;

//import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;

import globals.ResControl;
import globals.ResControlWeb;
import kyPkg.etc.CommonMethods;
import kyPkg.pmodel.JsonU;
import kyPkg.pmodel.ParameterModelInf;
import kyPkg.pmodel.ReportParmModel;
import kyPkg.task.Inf_ProgressTask;
import kyPkg.task.TaskWatcherNoGUI;
import kyPkg.uFile.DosEmu;
import kyPkg.uFile.FileUtil;

//updated 20130411
public class ProcRepository implements Inf_WebProcessor {
	// for directCall
	// private static List<String> getList(String userID, String reposName,
	// String dim1, String dim2, String dim3, String dim4) {
	// return asList(getArray(userID, reposName, dim1, dim2, dim3, dim4));
	// }

	@Override
	public void execute(Writer writer, ParameterModelInf pModel) {
		String[] array = getArray(pModel);
		if (array == null)
			array = new String[] { "NG" };
		try {
			writer.write(JsonU.array2JSON("jsonRes", array));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------------
	// for directCall
	// param:F:ENQ,P1:{0},P2:{1},P3:{2},NAME:REPOSITORY
	// F:ENQ,P1:ＱＰＲアンケート,P2:03_属性・性年代編,NAME:REPOSITORY
	// ------------------------------------------------------------------------
	public static List<String> getList(String param, Object[] levels) {
		String params = MessageFormat.format(param, levels);
		System.out.println("◆◆◆CHECKPOINT1◆◆◆param:" + params);
		ParameterModelInf pModel = new ReportParmModel(params);
		return java.util.Arrays.asList(getArray(pModel));
	}

	private static String[] getArray(ParameterModelInf pModel) {
		System.out.println("(7)#20130411# checkpoint2-3-0");
		String userID = pModel.get("USERID");
		String reposName = pModel.get("NAME");
		String dim1 = pModel.get("P1");
		String dim2 = pModel.get("P2");
		String dim3 = pModel.get("P3");
		String dim4 = pModel.get("P4");
		System.out.println("ENQ#20130411# userID   :" + userID);
		System.out.println("ENQ#20130411# reposName:" + reposName);
		System.out.println("ENQ#20130411# dim1     :" + dim1);
		System.out.println("ENQ#20130411# dim2     :" + dim2);
		System.out.println("ENQ#20130411# dim3     :" + dim3);
		System.out.println("ENQ#20130411# dim4     :" + dim4);
		return getArray(userID, reposName, dim1, dim2, dim3, dim4);
	}

	private static String[] getArray(String userID, String reposName,
			String dim1, String dim2, String dim3, String dim4) {

		String reposPath = ResControlWeb.getD_Resources(reposName + ".txt");
		HashMap<String, String> hmap = JsonU.file2hash(reposPath, "\t", true);
		String[] array = {};
		if (hmap != null && (!dim1.equals(""))) {
			System.out.println("#check01# dim1>>" + dim1 + "<<");
			String srcDir = hmap.get(dim1);
			System.out.println("#check01# ◆◆◆srcDir:" + srcDir);
			if (srcDir != null) {
				if (dim2.equals("")) {
					srcDir = FileUtil.getAbsPath(srcDir);
					array = DosEmu.dirList2Array(srcDir, "*");
				} else {
					if (dim3.equals("")) {
						srcDir = srcDir + "/" + dim2;
						srcDir = FileUtil.getAbsPath(srcDir);
						array = DosEmu.dirList2Array(srcDir, "*");
					} else {
						srcDir = srcDir + "/" + dim2 + "/" + dim3;
						String schemaPath = srcDir + "/Schema.ini";
						String QTB1Path = srcDir + "/QTB1.txt";
						String QTB2Path = srcDir + "/QTB2.txt";
						boolean schemaExist = new File(schemaPath).exists();
						boolean QTB1Exist = new File(QTB1Path).exists();
						boolean QTB2Exist = new File(QTB2Path).exists();
						if (schemaExist && QTB1Exist) {
//							String tempDir = kyPkg.uFile.ResControlWeb
//									.getWebUserResPath(userID);
							String   tempDir = globals.ResControl.getTempDir();
							System.out.println("#check02# ◆◆◆tempDir:"+tempDir);
							String tempPath = tempDir + "/freakoutRepo.txt";
							if (dim4.equals("")) {
								if (QTB1Exist) {
									String sql = "select Key,Nam from QTB1#TXT where Mot='ROOT'";
									Inf_ProgressTask task = CommonMethods
											.queryIsam2File(tempPath, sql,
													srcDir);
									// FIXME ここに問題ありか
									new TaskWatcherNoGUI(task).execute();
									array = JsonU.file2array(JsonU
											.file2List((tempPath)));
								}
							} else {
								String table = "QTB1#TXT";
								if (QTB2Exist)
									table = "QTB2#TXT";
								String sql = "select Key,Val,Max,Occ,Typ,Col,Len,Opt,','+Nam from "
										+ table + " where Mot='" + dim4 + "'";
								Inf_ProgressTask task = CommonMethods.queryIsam2File(
										tempPath, sql, srcDir);
								new TaskWatcherNoGUI(task).execute();
								array = JsonU.file2array(JsonU
										.file2List((tempPath)));
							}
						}
					}
				}
			}
		}
		return array;
	}

	private static void getArrayTester() {
		String userID = "";
		String reposName = "REPOSITORY";
		String dim1 = ResControl.ENQ_DEFAULT00;
		String dim2 = "";
		String dim3 = "";
		String dim4 = "";
		String[] array = getArray(userID, reposName, dim1, dim2, dim3, dim4);
		System.out.println("array.length:" + array.length);
		for (int i = 0; i < array.length; i++) {
			System.out.println(">>element :" + array[i]);
		}
	}

	public static void main(String[] argv) {
		getArrayTester();
	}
}
