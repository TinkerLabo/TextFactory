package kyPkg.uFile;

import java.io.File;
//############################################################################
//Inf_OnMatch�t�@�C�������}�b�`�������ɏ���������N���X�̃C���^�t�F�[�X
//############################################################################
public interface Inf_OnMatch {
	public abstract void init() ;

	public abstract void fin();

	public abstract void onMatch(File fileObj);

	public abstract void forDebug(String val);
}