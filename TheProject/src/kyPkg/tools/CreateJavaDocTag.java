package kyPkg.tools;

//TODO	JavaDoc�p�̂ЂȌ^���O�����������@20151020
//-----------------------------------------------------------------------------
//�^�@�O�@��	��	��
//@author	�N���X�̍쐬�ҏ����L��
//@param	���\�b�h�̈����̐���
//@return	���\�b�h�̕Ԃ�l�̐���
//@throw	���������O�N���X���w��
//@see	����API���Q�Ƃ���ꍇ�ɋL��
//@deprecated	��������Ȃ�API�ł��邱�Ƃ�����
//@serial	���񉻂��ꂽ�t�B�[���h�̐���
//@sesrialData	���񉻂��ꂽ��Ԃł̃f�[�^�^�Ə������L��
//@since	�������ꂽ�o�[�W�������L��
//@version	�o�[�W�������L��
//-----------------------------------------------------------------------------
import static kyPkg.util.Joint.join;
import static kyPkg.util.KUtil.list2strArray;
import java.util.ArrayList;

/**************************************************************************
 * <BR>JavaDoc�p�R�����g�^�O����		20151020
 * <BR>�N���X�ɑ΂��ẴR�����g���L�q���܂��B
 * <BR>�����ł͈ȉ��̓��e���L�q���܂��B
 * <BR><UL>
 * <BR><LI>�N���X���ǂ��������Ƃ�����̂��̐���
 * <BR><LI>�C���X�^���X����蓾���Ԃɂ��Ă̏��B
 * <BR>    ��j �t�@�C�����I�[�v������Ă����ԂƃN���[�Y���ꂽ��Ԃł̐U�镑���B
 * <BR><HR>
 * <BR><HR>
 * @author ken yuasa
 * @version 1.0
 * @see "Effective Java��2��"
 **************************************************************************/
//-----------------------------------------------------------------------------
//@author		�N���X�̍쐬�ҏ����L��
//@param		���\�b�h�̈����̐���
//@return		���\�b�h�̕Ԃ�l�̐���
//@throw		���������O�N���X���w��
//@see			����API���Q�Ƃ���ꍇ�ɋL��
//@deprecated	��������Ȃ�API�ł��邱�Ƃ�����
//@serial		���񉻂��ꂽ�t�B�[���h�̐���
//@sesrialData	���񉻂��ꂽ��Ԃł̃f�[�^�^�Ə������L��
//@since		�������ꂽ�o�[�W�������L��
//@version		�o�[�W�������L��
//-----------------------------------------------------------------------------
public class CreateJavaDocTag extends Tool {
	//-----------------------------------------------------------------------------
	//	�֐��̃V�O�j�`����������������o���ăA�m�[�e�[�V�����̂ЂȌ^��������������v���O����
	//	���L�͎������������A�m�[�e�[�V�����̗�
	//-----------------------------------------------------------------------------
	//	/**
	//	 * �R���X�g���N�^ CalcLoy1
	//	 * @param mapLoy�@�o�̓p�X�P�F���j�^�[*�u�����h ���Ƃ̊e���C�����e�B�{�e���v
	//	 * @param mTotal�@�o�̓p�X�Q�F���j�^�[���Ƃ̍��v���R�[�h�i���؂�p�j
	//	 * @param gTotal�@�o�̓p�X�R�F�����v���R�[�h1���̃f�[�^�i���؂�p�j
	//	 */
	//-----------------------------------------------------------------------------
	public static String execute(String str) {
		ArrayList<String> list = new ArrayList();
		ArrayList<String> bufList = new ArrayList();
		int pos1 = str.indexOf('(');
		int pos2 = str.indexOf(')');
		if (pos1 < 0 || pos2 < 0)
			return "";
		String[] func = str.substring(0, pos1).split(" ");
		String name = func[func.length - 1];
		String ans = str.substring(pos1 + 1, pos2);
		if ((pos1 > 0) && (pos1 < pos2)) {
			//			bufList.add("// ------------------------------------------------------------------------");
			bufList.add(
					"/**************************************************************************");
			bufList.add(" * " + name + "\t\t\t\t");
			bufList.add(" * @author\tken yuasa");
			bufList.add(" * @version\t1.0");
			bufList.add(" * <ul>");
			bufList.add(" * <ll>�y�T�v�z");
			bufList.add(" * </ul>");
			String[] splited = ans.split(",");
			if (splited.length > 0) {
				for (int i = 0; i < splited.length; i++) {
					String[] tuple = splited[i].trim().split(" ");
					if (tuple.length > 1) {
						list.add(tuple[1].trim());
						bufList.add(" * @param " + tuple[1].trim() + "\t\t\t ");
					}
				}
			}
			String args = join(list2strArray(list), ",");
			bufList.add(
					" **************************************************************************/");
			//			bufList.add("// ------------------------------------------------------------------------");
		}
		String LF = "\n";
		StringBuffer buff = new StringBuffer();
		for (String val : bufList) {
			buff.append(val + LF);
		}
		return buff.toString();
	}

	public static void main(String[] argv) {
		String str = "	public HttpClient(String requestMethod, String url) {";
		str = "	public CalcLoy2X(String bodyPath, int denominator, int cutEx, int cutHi,int cutMed, String calcBase, String sharBase,HashMap<String, String> classMap, HashMap<String, String> typeMap,			String target) {";
		System.out.println(kyPkg.tools.CreateJavaDocTag.execute(str));
	}
}
