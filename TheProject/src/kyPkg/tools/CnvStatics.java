package kyPkg.tools;

import java.util.ArrayList;
import java.util.List;

public class CnvStatics extends Tool {
	// ------------------------------------------------------------------------
	// �����̃\�[�X�𗘗p���Ē萔�𐶐����邽�߂̃c�[��
	// ������̓R�����i�F�j�ŋ�؂��Ă�����̂Ƃ���(�ȉ��̂悤�ȃX�^�C�������҂��Ă���)
	// ���[�J�[�@�@�@�@�@�@�@�@�@�@�@:MKR
	// �敪�P�@�@�@�@�@�@�@�@�@�@�@�@:KB1
	// �敪�Q�@�@�@�@�@�@�@�@�@�@�@�@:KB2
	// �敪�R�@�@�@�@�@�@�@�@�@�@�@�@:KB3
	// �敪�S�@�@�@�@�@�@�@�@�@�@�@�@:KB4
	// �敪�T�@�@�@�@�@�@�@�@�@�@�@�@:KB5
	// �敪�U�@�@�@�@�@�@�@�@�@�@�@�@:KB6
	// �A�C�e���@�@�@�@�@�@�@�@�@�@�@:ITM
	// �i�ږ� �@�@�@�@�@�@�@�@�@�@�@:HN4
	// ------------------------------------------------------------------------
	public static List<String> execute(List<String> list) {
		List<String> listA = new ArrayList();
		List<String> listB = new ArrayList();
		for (String element : list) {
			String[] array = element.split(":");
			if (array.length >= 2) {
				listA.add(array[0]);
				listB.add(array[1].trim());
			}
		}
		List<String> listRes = new ArrayList();
		for (int i = 0; i < listA.size(); i++) {
			listRes.add("public static final String " + listB.get(i) + " = \""
					+ listA.get(i) + "\";");
		}
		listRes.add("public static String[] arrayList_X = {");
		for (int i = 0; i < listA.size(); i++) {
			listRes.add("\t" + listB.get(i) + ",");
		}
		listRes.add("};");
		listRes.add("");
		listRes.add("HashMap map_X;");
		listRes.add("map_X = new HashMap();");
		for (int i = 0; i < listA.size(); i++) {
			listRes.add("map_x.put(RepDict." + listB.get(i) + ", \""
					+ listB.get(i) + "\");");
		}
		listRes.add("");
		listRes.add("//�����X�g�̏ꍇ�i�J�X�^���R�[�h�Ɏw�肷��j");
		listRes.add(
				"ListModel listModelX new javax.swing.AbstractListModel() {");
		listRes.add(
				"    public int getSize() { return RepDict.arrayList_X.length; }");
		listRes.add(
				"    public Object getElementAt(int i) { return RepDict.arrayList_X[i]; }");
		listRes.add("};");
		listRes.add("");
		listRes.add("//���R���{�{�b�N�X�̏ꍇ�i�J�X�^���R�[�h�Ɏw�肷��j");
		listRes.add(
				"ComboBoxModel cmbModelX = new javax.swing.DefaultComboBoxModel(RepDict.arrayList_X);");
		return listRes;
	}

}
