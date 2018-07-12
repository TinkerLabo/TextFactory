package kyPkg.sql;

import java.util.HashMap;

public class Connectors {
	private HashMap<String, Connector> hash = null;
	private static Connectors myIns = null;

	public static Connectors getIns() {
		if (myIns == null)
			myIns = new Connectors();
		return myIns;
	}

	// �R���X�g���N�^ �@�isingleton!!�j
	private Connectors() {
		hash = new HashMap();
		Connector ins = new ServerConnecter();
		hash.put("Server", ins);
	}

	public static Connector get(String key) {
		Connector connector = getIns().gotcha(key);
		if (connector == null) {
			System.out
					.println("#ERROR!! @Connectors key:" + key + " NotFound!");
		}
		return connector;
	}

	private Connector gotcha(String key) {
		// XXX�@�Ȃ����^�C���A�E�g���Ă�����|�C�H�H�̂ŉ��}���u�E�E�E�S�R�R�l�N�V�����v�[���ɂȂ��Ă��Ȃ�
		if (key.equals("Server")) {
			hash.put("Server", new ServerConnecter());
		}

		return hash.get(key);
	}

	public static void add(String key, Connector connector) {
		System.out.println("###�@add Connectors�@###############key:" + key);
		getIns().hash.put(key, connector);
	}

	public static void remove(String key) {
		System.out.println("###�@remove Connectors�@###############key:" + key);
		Connector connector = getIns().hash.get(key);
		connector.close();
		getIns().hash.remove(key);
	}

	public static void allClose() {
		System.out.println("###�@allClose Connectors�@###############");
		java.util.Set set = getIns().hash.entrySet();
		java.util.Iterator it = set.iterator();
		while (it.hasNext()) {
			java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
			Connector connector = getIns().hash.get((String) ent.getKey());
			connector.close();
		}
	}

	// -------------------------------------------------------------------------
	// finalyze
	// -------------------------------------------------------------------------
	protected void finalyze() {
		allClose();
	}
}