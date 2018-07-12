package kyPkg.atoms;

import static kyPkg.util.Joint.join;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import kyPkg.filter.EzReader;
import kyPkg.filter.EzWriter;
import kyPkg.filter.Inf_iClosure;
import kyPkg.filter.Inf_oClosure;
import kyPkg.task.Abs_BaseTask;

// 2009-06-16 yuasa
// ATOMのメタデータをJSONに変換する
public class Flt_Atom2JSON extends Abs_BaseTask {
	// ------------------------------------------------------------------------
	// 入力クロージャ
	// ------------------------------------------------------------------------
	private String json = null;
	private Inf_iClosure reader1 = null;
	private Inf_iClosure reader2 = null;
	private java.io.Writer writer = null;
	private ArrayList<String> list;

	public void getJson(java.io.Writer outWriter) {
		this.writer = outWriter;
		execute();
	}

	public void saveASText(String outPath) {
		Inf_oClosure writer = new EzWriter(outPath);
		// ---------------------------------------------------------------------
		// output
		// ---------------------------------------------------------------------
		writer.open();
		if (!json.equals("")) {
			writer.write(json);
		}
		writer.close();
	}

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Flt_Atom2JSON(String metaPath, String dataPath) {
		reader1 = new EzReader(metaPath);
		reader2 = new EzReader(dataPath);
	}

	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("ＸＸ開始");
		HashMap<String, String[]> hashMap = new HashMap();
		list = new ArrayList();
		long wCnt1 = 0;
		long wCnt2 = 0;
		reader1.open();
		String[] splited = null;
		// ---------------------------------------------------------------------
		// incore
		// ---------------------------------------------------------------------
		while (reader1.readLine() != null) {
			int wStat = reader1.getStat();
			if (wStat >= 0) {
				splited = reader1.getSplited();
				if (splited.length >= 2) {
					String key = splited[0];
					String val = splited[1];
					String[] array = val.split(",");
					for (int i = 0; i < array.length; i++) {
						array[i] = array[i].replaceAll(":", "：");
						array[i] = array[i].replaceAll("\\[", "【");
						array[i] = array[i].replaceAll("\\]", "】");
						array[i] = array[i].replaceAll("\\{", "『");
						array[i] = array[i].replaceAll("\\}", "』");
						array[i] = array[i].replaceAll("　", "");
						array[i] = "'" + array[i].trim() + "'";
					}
					hashMap.put(key, array);
				}
				wCnt1++;
			}
		}
		reader1.close();
		// System.out.println("##Incore1:" + wCnt1);
		Set collectionView = hashMap.entrySet();
		Iterator iter = collectionView.iterator();
		while (iter.hasNext()) {
			java.util.Map.Entry ent = (java.util.Map.Entry) iter.next();
			list.add((String) ent.getKey());
		}
		// System.out.println("##sort ");
		Collections.sort(list);
		StringBuffer buff = new StringBuffer();
		try {
			writer.write("{meta:{");
			for (int i = 0; i < list.size(); i++) {
				buff.delete(0, buff.length());
				if (i > 0)
					buff.append(",");
				String key = list.get(i);
				String[] array = hashMap.get(key);
				String values = join(array, ",");
				buff.append(key);
				buff.append(":[");
				buff.append(values);
				buff.append("]");
				writer.write(buff.toString());
			}
			writer.write("},data:[");
			// etc ...
			reader2.open();
			// ---------------------------------------------------------------------
			// incore
			// ---------------------------------------------------------------------
			wCnt2 = 0;
			while (reader2.readLine() != null) {
				int wStat = reader2.getStat();
				if (wStat >= 0) {
					splited = reader2.getSplited();
					String wRec = join(splited, ",", "'");
					buff.delete(0, buff.length());
					if (wCnt2 > 0)
						buff.append(",");
					buff.append("[");
					buff.append(wRec);
					buff.append("]");
					writer.write(buff.toString());
					wCnt2++;
				}
			}
			reader2.close();
			writer.write("]};");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
