package kyPkg.pmodel;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

import globals.ResControlWeb;
import kyPkg.etc.AliasRes;
import kyPkg.processor.Inf_WebProcessor;
import kyPkg.uFile.FileUtil;

public class CnvAlias2Atom implements Inf_WebProcessor {
	@Override
	public void execute(Writer writer, ParameterModelInf pModel) {
		// String resDir = ResControl.getWebDir();
		String userID = pModel.get("USERID");
//		String userResPath = globals.ResControlWeb.getD_Resources_TEMP(userID);
		String reposName = pModel.get("NAME");
		String attrTitle = pModel.get("TITLE");
		String dimP1 = pModel.get("P1");
		String dimP2 = pModel.get("P2");
		String dimP3 = pModel.get("P3");
		String reposPath = ResControlWeb.getD_Resources(reposName + ".txt");
		String[] fields = pModel.getArray("FIELD");
		attrTitle = FileUtil.charsetConv(attrTitle);
		System.out.println("#<CnvAlias2Atom>####################");
		System.out.println("# userID:" + userID);
		System.out.println("#　リポジトリー名:" + reposName);
		System.out.println("#　dim1 :" + dimP1);
		System.out.println("#　dim2 :" + dimP2);
		System.out.println("#　dim3 :" + dimP3);
		System.out.println("#　リポジトリーのパス:" + reposPath);
		System.out.println("#　属性のタイトル:" + attrTitle);
//		System.out.println("#　ユーザーリソースのパス:" + userResPath);
		System.out.println("#　対象フィールド　");
		for (int i = 0; i < fields.length; i++) {
			System.out.println("#   fields[" + i + "]:" + fields[i]);
		}
		String[] array = null;
		HashMap<String, String> hmap = JsonU.file2hash(reposPath);
		if (hmap != null && (!dimP1.equals(""))) {
			String reposDir = hmap.get(dimP1);
			if (reposDir != null) {
				if (!dimP2.equals("")) {
					if (!dimP3.equals("")) {
						String aliasDir = reposDir + "/" + dimP2 + "/" + dimP3;
						String outDir = ResControlWeb.getD_Resources_ATOM(""); // 出力ディレクトリパス
						// XXX 同じファイルが存在した場合のみタイムスタンプを付加するにしようか？？
						String outName = attrTitle
								+ kyPkg.uDateTime.DateCalc.getTimeStamp();
						System.out.println("# aliasDir   :" + aliasDir);
						System.out.println("# outDir     :" + outDir);
						System.out.println("# outName    :" + outName);
						System.out.println("#20130402#checkpoint 014");

						AliasRes aliasRes = new AliasRes(aliasDir);
						aliasRes.setOptionField(",1 as count");
						aliasRes.setDelimiter("\t");
						aliasRes.saveAsAtomics(outDir + outName, fields);
					}
				}
			}
		}
		if (array == null)
			array = new String[] { "NG" };
		// return array2JSON("jsonRes", array);
		try {
			writer.write(JsonU.array2JSON("jsonRes", array));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
