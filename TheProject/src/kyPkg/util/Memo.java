package kyPkg.util;

public class Memo {
	public void enumMap() {
		java.util.HashMap hmap = new java.util.HashMap();
		hmap.put("key1", "Val1");
		hmap.put("key2", "Val2");
		hmap.put("key3", "Val3");
		hmap.put("key3", "Val3XX");
		hmap.put("key4", "Val4");
		hmap.put("key5", "Val5");
		hmap.put("key6", "Val6");
		hmap.put(null, "null");
		System.out.println("size:" + hmap.size());
		System.out.println("containsKey(null):" + hmap.containsKey(null));
		hmap.remove(null);
		System.out.println("---------------------------------------------------");
		java.util.Set set = hmap.entrySet(); // ’¼Úiterator‚ğŒÄ‚×‚È‚¢‚Ì‚Åˆê’USET‚ğæ“¾‚·‚é
		java.util.Iterator it = set.iterator();
		while (it.hasNext()) {
			java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
			String key = (String) ent.getKey();
			String val = (String) ent.getValue();
			System.out.println("Memo key:" + key + "  val:" + val);
		}
		System.out
				.println("---------------------------------------------------");
		System.out.println("get(\"key3\"):" + hmap.get("key3"));
	}
	public static void main(String[] argv) {
		tester();
	}
	public static void tester() {
		java.util.HashMap channelMap = new java.util.HashMap();
		Integer[] intObj = null;
		intObj= new Integer[3];
		for (int i = 0; i < intObj.length; i++) {
			  intObj[i]= new Integer(i);
		}
		channelMap.put("key1", intObj);
		channelMap.put("key2", "Val2");
		channelMap.put("key3", intObj);
		channelMap.put("key3", "Val3XX");
		channelMap.put("key4", "Val4");
		channelMap.put("key5", "Val5");
		channelMap.put("key6", "Val6");
		String ans = enumMap2(channelMap);
		System.out.println("ans:"+ans);
	}
	public static String enumMap2(java.util.HashMap hashMap) {
		StringBuffer buff = new StringBuffer();
		java.util.Set set = hashMap.entrySet(); 
		java.util.Iterator it = set.iterator();
		while (it.hasNext()) {
			java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
			Object key = ent.getKey();
			Object val = ent.getValue();
			buff.append("{");
			buff.append(key.toString());
			buff.append(":");
			if (val instanceof Integer[]) {
				Integer[] intObj = (Integer[])val;
				buff.append(intObj[0].toString());
				for (int i = 1; i < intObj.length; i++) {
					buff.append(",");
					buff.append(intObj[i].toString());
				}
			}else{
				buff.append(val.toString());
			}
			buff.append("}");
		}
		return buff.toString();
	}
}
