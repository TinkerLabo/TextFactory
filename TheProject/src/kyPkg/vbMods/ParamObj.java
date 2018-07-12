package kyPkg.vbMods;

import java.util.HashMap;
import java.util.List;

public class ParamObj {
	public HashMap<String, List> hash;
	public void push(String key,List value) {
		hash.put(key, value);
	}
	public List get(String key) {
		return hash.get(key);
	}
	public ParamObj() {
		super();
		hash = new HashMap();
	}

}
// Option Explicit
// Public hash As Object
// '---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
// ' Dim objP As ParamObj
// ' Set objP = New ParamObj
// '---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
// Public Sub Class_Initialize()
// 'コンストラクタ
// Set hash = CreateObject("Scripting.Dictionary")
// End Sub
// Public Sub Class_Terminate()
// 'デストラクタ
// End Sub
