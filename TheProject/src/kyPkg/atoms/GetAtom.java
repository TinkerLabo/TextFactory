package kyPkg.atoms;

import globals.ResControl;
import globals.ResControlWeb;
import kyPkg.pmodel.ParameterModelInf;

public class GetAtom {
	private ParameterModelInf pModel = null;

	public GetAtom(ParameterModelInf pModel) {
		this.pModel = pModel;
	}

	// -------------------------------------------------------------------------
	// GetAtom as Json
	// -------------------------------------------------------------------------
	public void writeAtom(java.io.Writer writer) {
		String whichAtom = pModel.get("ATM1");
		String atomPath = ResControlWeb.getD_Resources(whichAtom); 
		System.out.println("<@GetAtom as Json>>------------------------");
		System.out.println("	whichAtom   :" + atomPath);
		String metaPath = ResControl.D_DRV + "resources/" + atomPath +  "." + Atomics.getEXT();
		String dataPath = ResControl.D_DRV + "resources/" + atomPath + ".txt";
		new Flt_Atom2JSON(metaPath, dataPath).getJson(writer);
	}
}
