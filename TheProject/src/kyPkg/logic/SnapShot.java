package kyPkg.logic;

import globals.ResControlWeb;
import kyPkg.filter.CommonClojure;
import kyPkg.filter.PoiClosure;
import kyPkg.pmodel.JsonObject;
import kyPkg.pmodel.ParameterModelInf;

public class SnapShot {
	private ParameterModelInf pModel = null;

	public SnapShot(ParameterModelInf pModel) {
		this.pModel = pModel;
	}
	// -------------------------------------------------------------------------
	// GetAtom as Json
	// -------------------------------------------------------------------------
	public String doIt() {
		String userID = pModel.get("USERID");
		String pdeDir = globals.ResControl.getPdeDir(userID);

		//String userResPath = qprweb.ResControl.getUserResPath(userID);
		String gridJson = pModel.getParams(); // side(tran)
		String saveAs = pModel.get("SAVEAS"); // side(tran)
		// String pdeDir = qprweb.ResControl.getPdeDir(userID);
		//String destFile = pdeDir + "/"+saveAs.trim()+".xls"; // XXX ファイル名の妥当性チェックを追加する
		String timeStamp = kyPkg.uDateTime.DateCalc.getTimeStamp();
		String destFile = pdeDir + "SnapShot" + timeStamp + ".xls";

		String[] jsonData = pModel.getArray("JSON.data"); // side(tran)
		System.out.println("<@mashUp>>----------------------------------");
		System.out.println("	userID		:" + userID);
		System.out.println("	pdeDir	:" + pdeDir);
		System.out.println("	destFile    :" + destFile);
		System.out.println("	gridJson    :" + gridJson);
		if (jsonData!=null){
			for (int i = 0; i < jsonData.length; i++) {
				System.out.println("jsonData["+i+"]:" + jsonData[i]);
			}
		}
		saveAsExcel(destFile,gridJson);
		String t_path = ResControlWeb.getD_Resources(saveAs); // Side 購買データ・・・
		System.out.println("	t_path   :" + t_path);
		//String forDebug = new Flt_Atom2JSON(t_path).getJson();
		String forDebug = "{Stat:'Easy'}";
		return forDebug;
	}
	
	public static void saveAsExcel(String destFile,String json){
//		String json = "";
//		json = "F:REP,USERID:ejqp7,REPORT:SnapShot,SAVEAS:権兵衛さん,JSON:({meta:{cells:[[{name:'className', styles:'text-align: left;', width:30}, {name:'Heavy', styles:'text-align: right;', width:5}, {name:'Middle', styles:'text-align: right;', width:5}, {name:'Light', styles:'text-align: right;', width:5}, {name:'NonUser', styles:'text-align: right;', width:5}]]}, data:[['\uFF11\uFF10\u4EE3', 3, 0, 0, 371], ['\uFF12\uFF10\u4EE3', 46, 2, 1, 1003], ['\uFF13\uFF10\u4EE3', 121, 4, 2, 1270], ['\uFF14\uFF10\u4EE3', 123, 4, 7, 993], ['\uFF15\uFF10\u4EE3', 100, 4, 1, 1152], ['\uFF16\uFF10\u4EE3', 61, 3, 2, 754]]})";
		JsonObject jsonObj = new JsonObject(json);
		System.out.println("---------------------------------------------");
		System.out.println("@Tester json=>" + json);
		System.out.println("CREATE:" + jsonObj.get("CREATE"));
		System.out.println("COMM1:" + jsonObj.get("COMM1"));
		System.out.println("COMM2:" + jsonObj.get("COMM2"));
		String created = "作成日付:" + jsonObj.get("CREATE");
		String comment1 = "HML:" + jsonObj.get("COMM1");
		String comment2 = "対象:" + jsonObj.get("COMM2");
		System.out.println("-step0--------------------------------------------");
		System.out.println("JSON.meta.cells:" + jsonObj.get("JSON.meta.cells"));

		String[] array = jsonObj.getArray("JSON.data");
		if (array!=null){
			for (int i = 0; i < array.length; i++) {
				System.out.println("data:[" + i + "]:" + array[i]);
				array[i]=array[i].replaceAll("\"", "");
			}
		}
		String[] header = jsonObj.getArray("JSON.meta.cells.0","name");
		if (header!=null){
			for (int i = 0; i < header.length; i++) {
				header[i]=header[i].replaceAll("\"", "");
				System.out.println("name:[" + i + "]:" + header[i]);
			}
		}
		System.out.println("@Tester destFile=>" + destFile);
		PoiClosure closure = new PoiClosure();
		closure.setTemplate(ResControlWeb.getD_Resources_Templates("QPRSSHOT.xls"));
		closure.setOutPath(destFile);
		closure.setSheet("data");
		closure.setBaseYX(10, 2);
		closure.setDelimiter(",");
		closure.setComm1(new String[]{comment1});
		closure.setComm2(new String[]{comment2});
		closure.setComm3(new String[]{created});
		closure.setHeader(header);
		new CommonClojure().incore(closure, array);

	}
		

}
