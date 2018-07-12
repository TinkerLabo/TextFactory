package kyPkg.uFile;

public class YamlCtrl extends YamlControl {
	private static YamlControl ymlCtrl = null;

	public static YamlControl getInstance(String path) {
		String prevPath = "";
		if (ymlCtrl != null)
			prevPath = ymlCtrl.getYamlPath();
		if (!prevPath.equals(path)) {
//			System.out.println("########################################");
//			System.out.println("# @TranYaml.getInstance");
//			System.out.println("# bef:" + prevPath);
//			System.out.println("# aft:" + path);
//			System.out.println("########################################");
			if (ymlCtrl != null)
				ymlCtrl.saveAs(prevPath);
			ymlCtrl = new YamlControl(path);
		}
		return ymlCtrl;
	}
}
