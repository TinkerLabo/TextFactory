package kyPkg.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import globals.ResControlWeb;
import kyPkg.converter.CnvDictionary;
import kyPkg.converter.Inf_StrConverter;
import kyPkg.filter.EzWriter;
import kyPkg.uCodecs.CharConv;
import kyPkg.uFile.HashMapUtil;
public class ChannelCnv {
	private static final String TAB = "\t";

	//	// <<�w���揈��>> �X�R�[�h�ϊ��e�[�u���C���R�A�����E�E�E2011/02/09
	//	System.out.println("## INCORE ##" + resPath);
	//	if (new File(resPath).isFile() == false) {
	//		System.out.println("#ERROR file not found:" + resPath);
	//		System.exit(999);
	//	}

	private static final String CHANNEL_CNV_TXT = "channelCnv.txt";

	public static CnvDictionary setUpConverter() {
		String resPath = ResControlWeb.getD_Resources_QPR_RES()
				+ CHANNEL_CNV_TXT;
		System.out.println("## INCORE ## �ϊ��p�����[�^�t�@�C�� " + resPath);
		if (new File(resPath).isFile() == false) {
			System.out.println("#ERROR file not found:" + resPath);
			System.exit(999);
		}
		int keyCol = 1;
		int valCol = 0;
		CnvDictionary dict = new CnvDictionary(resPath, keyCol, valCol);
		// ������Ȃ������Ƃ��̏����E�E�E
		Inf_StrConverter converter = new Inf_StrConverter() {
			@Override
			public String convert(String val) {
				if (val.matches("4[456789].*")) {
					// System.out.println("44? Type�S�ݓX�Ɋ��荞��ŕt�Ԃ���Ă���n���X�[�p�[�Ƃ݂Ȃ�");
					return "19";// ���ɒn���X�[�p�[��19�Ƃ����R�[�h�Ƃ���
				} else {
					// System.out.println("otherType ��X�敪���������āE�E�E���̑�����");
					return val.charAt(0) + "9";
				}
			}
		};
		dict.setConverter(converter);
		return dict;
	}

	public static void testConverter() {
		// �X�R�[�h�ϊ������̃v���g�^�C�v�E�E�E2011/02/09
		CnvDictionary dict = setUpConverter();
		// �ʏ킱�̂悤�Ɏg�p����
		String ans = dict.getValue("440");
		System.out.println("ans of 440 =>" + ans);
		System.out
				.println("�L�[���̂ݎ��o���Ă݂ă`�F�b�N����-----------------------------------");
		List keys = dict.getKeyList();
		keys.add("439");
		keys.add("440");
		keys.add("451");
		for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			int seq = dict.getDestPos(key); // ���Ԗڂɂ����邩
			String val = dict.getValue(key); // �ύX��̒l�͉���
			System.out.println("key:" + key + " => seq:" + seq + " val:" + val);
		}
	}

	public static void test110228() {
		// �X�R�[�h�ϊ������̃v���g�^�C�v�E�E�E2011/02/09
		CnvDictionary dict = setUpConverter();
		// ����A�ǉ������R�[�h���ׂĂ��e�X�g
		System.out.println("�M�t�g�Z�b�g�@�@�@�@�@�@�@�@�@001=>" + dict.getValue("001"));
		System.out.println("�i�i�i�p�`���R���j�@�@�@�@�@�@002=>" + dict.getValue("002"));
		System.out.println("���̑������i�@�@�@�@�@�@�@�@�@003=>" + dict.getValue("003"));
		System.out.println("���S���[�[���@�@�@�@�@�@�@�@�@149=>" + dict.getValue("149"));
		System.out.println("�t�W�X�[�p�[�@�@�@�@�@�@�@�@�@150=>" + dict.getValue("150"));
		System.out.println("�I�m�����@�@�@�@�@�@�@�@�@�@�@151=>" + dict.getValue("151"));
		System.out.println("�g���C�A���@�@�@�@�@�@�@�@�@�@152=>" + dict.getValue("152"));
		System.out.println("�_�C�C�`�@�@�@�@�@�@�@�@�@�@�@153=>" + dict.getValue("153"));
		System.out.println("�`�R�[�v�i�_���j�@�@�@�@�@�@�@154=>" + dict.getValue("154"));
		System.out.println("�r�b�O�n�E�X�@�@�@�@�@�@�@�@�@155=>" + dict.getValue("155"));
		System.out.println("���Ƃ��@�@�@�@�@�@�@�@�@�@�@�@156=>" + dict.getValue("156"));
		System.out.println("�W���C�X�@�@�@�@�@�@�@�@�@�@�@157=>" + dict.getValue("157"));
		System.out.println("�x�j�[�}�[�g�A�J�u�Z���^�[�@�@158=>" + dict.getValue("158"));
		System.out.println("�}���g�@�@�@�@�@�@�@�@�@�@�@�@159=>" + dict.getValue("159"));
		System.out.println("���}�U���@�@�@�@�@�@�@�@�@�@�@160=>" + dict.getValue("160"));
		System.out.println("���j�o�[�X�@�@�@�@�@�@�@�@�@�@161=>" + dict.getValue("161"));
		System.out.println("���I���E�h�[���@�@�@�@�@�@�@�@162=>" + dict.getValue("162"));
		System.out.println("�E�W�G�X�[�p�[�@�@�@�@�@�@�@�@163=>" + dict.getValue("163"));
		System.out.println("�}�G�_�@�@�@�@�@�@�@�@�@�@�@�@164=>" + dict.getValue("164"));
		System.out.println("�J�X�~�n�@�@�@�@�@�@�@�@�@�@�@165=>" + dict.getValue("165"));
		System.out.println("�G�R�X�O���[�v�@�@�@�@�@�@�@�@166=>" + dict.getValue("166"));
		System.out.println("�I�[�^�j�@�@�@�@�@�@�@�@�@�@�@167=>" + dict.getValue("167"));
		System.out.println("�Ƃ肹��@�@�@�@�@�@�@�@�@�@�@168=>" + dict.getValue("168"));
		System.out.println("�t���b�Z�C�@�@�@�@�@�@�@�@�@�@169=>" + dict.getValue("169"));
		System.out.println("�x���N�@�@�@�@�@�@�@�@�@�@�@�@170=>" + dict.getValue("170"));
		System.out.println("���M�A�i���X�@�@�@�@�@�@�@�@�@171=>" + dict.getValue("171"));
		System.out.println("�E�I���N�@�@�@�@�@�@�@�@�@�@�@172=>" + dict.getValue("172"));
		System.out.println("�A���r�X�@�@�@�@�@�@�@�@�@�@�@173=>" + dict.getValue("173"));
		System.out.println("�c�����@�@�@�@�@�@�@�@�@�@�@�@174=>" + dict.getValue("174"));
		System.out.println("�}�c���@�@�@�@�@�@�@�@�@�@�@�@175=>" + dict.getValue("175"));
		System.out.println("��㉮�V���b�v�@�@�@�@�@�@�@�@176=>" + dict.getValue("176"));
		System.out.println("�I�M�m�@�@�@�@�@�@�@�@�@�@�@�@177=>" + dict.getValue("177"));
		System.out.println("�A�b�v�������h�@�@�@�@�@�@�@�@178=>" + dict.getValue("178"));
		System.out.println("�}���G�[�@�@�@�@�@�@�@�@�@�@�@179=>" + dict.getValue("179"));
		System.out.println("�J�l�X�G�@�@�@�@�@�@�@�@�@�@�@180=>" + dict.getValue("180"));
		System.out.println("�����ĂX�g�A�@�@�@�@�@�@�@�@181=>" + dict.getValue("181"));
		System.out.println("�i�t�R�@�@�@�@�@�@�@�@�@�@�@�@182=>" + dict.getValue("182"));
		System.out.println("���S�X�g�A�@�@�@�@�@�@�@�@�@�@183=>" + dict.getValue("183"));
		System.out.println("�A�I�L�X�[�p�[�@�@�@�@�@�@�@�@184=>" + dict.getValue("184"));
		System.out.println("�O�S�@�@�@�@�@�@�@�@�@�@�@�@�@185=>" + dict.getValue("185"));
		System.out.println("�ꍆ�ځ@�@�@�@�@�@�@�@�@�@�@�@186=>" + dict.getValue("186"));
		System.out.println("�h�~�[�@�@�@�@�@�@�@�@�@�@�@�@187=>" + dict.getValue("187"));
		System.out.println("���C�X�g�A�A���V�d���@�@�@�@�@188=>" + dict.getValue("188"));
		System.out.println("�X�[�p�[�T���V�@�@�@�@�@�@�@�@189=>" + dict.getValue("189"));
		System.out.println("�}���A�C�@�@�@�@�@�@�@�@�@�@�@190=>" + dict.getValue("190"));
		System.out.println("�����@�@�@�@�@�@�@�@�@�@�@�@�@191=>" + dict.getValue("191"));
		System.out.println("�t���X�R�@�@�@�@�@�@�@�@�@�@�@192=>" + dict.getValue("192"));
		System.out.println("�}���C�O���[�v�@�@�@�@�@�@�@�@193=>" + dict.getValue("193"));
		System.out.println("���A�[�Y�A�ۘa�@�@�@�@�@�@�@�@194=>" + dict.getValue("194"));
		System.out.println("�V�����n�@�@�@�@�@�@�@�@�@�@�@195=>" + dict.getValue("195"));
		System.out.println("�t�W�n�@�@�@�@�@�@�@�@�@�@�@�@196=>" + dict.getValue("196"));
		System.out.println("�f�B�I�A���E���[�@�@�@�@�@�@�@197=>" + dict.getValue("197"));
		System.out.println("�j�V�i�A�t�[�h�o�X�P�b�g�@�@�@198=>" + dict.getValue("198"));
		System.out.println("�X���[�G�t�@�@�@�@�@�@�@�@�@�@225=>" + dict.getValue("225"));
		System.out.println("�X�p�[�@�@�@�@�@�@�@�@�@�@�@�@226=>" + dict.getValue("226"));
		System.out.println("�Z�[�u�I���@�@�@�@�@�@�@�@�@�@227=>" + dict.getValue("227"));
		System.out.println("�g���C�A���f�B�X�J�E���g�R����228=>" + dict.getValue("228"));
		System.out.println("�ߗ��i�X�@�@�@�@�@�@�@�@�@�@�@313=>" + dict.getValue("313"));
		System.out.println("�򉤓��@�@�@�@�@�@�@�@�@�@�@�@540=>" + dict.getValue("540"));
		System.out.println("�A�T�q�@�@�@�@�@�@�@�@�@�@�@�@541=>" + dict.getValue("541"));
		System.out.println("�}���G�h���b�O�@�@�@�@�@�@�@�@542=>" + dict.getValue("542"));
		System.out.println("�h���b�O�Ă炵�܁@�@�@�@�@�@�@543=>" + dict.getValue("543"));
		System.out.println("�A�I�L�@�@�@�@�@�@�@�@�@�@�@�@544=>" + dict.getValue("544"));
		System.out.println("�R�_�}�@�@�@�@�@�@�@�@�@�@�@�@545=>" + dict.getValue("545"));
		System.out.println("�Q���L�[�@�@�@�@�@�@�@�@�@�@�@546=>" + dict.getValue("546"));
		System.out.println("�A�����J���h���b�O�@�@�@�@�@�@547=>" + dict.getValue("547"));
		System.out.println("�V���m�h���b�O�@�@�@�@�@�@�@�@548=>" + dict.getValue("548"));
		System.out.println("�h���b�O�t�W�C�@�@�@�@�@�@�@�@549=>" + dict.getValue("549"));
		System.out.println("�E�H���c�@�@�@�@�@�@�@�@�@�@�@550=>" + dict.getValue("550"));
		System.out.println("�Ђ܂��@�@�@�@�@�@�@�@�@�@�@551=>" + dict.getValue("551"));
		System.out.println("�U�O�U�O�@�@�@�@�@�@�@�@�@�@�@552=>" + dict.getValue("552"));
		System.out.println("�E�F���l�X�@�@�@�@�@�@�@�@�@�@553=>" + dict.getValue("553"));
		System.out.println("������̃��u�@�@�@�@�@�@�@�@�@554=>" + dict.getValue("554"));
		System.out.println("���f�B��ǁ@�@�@�@�@�@�@�@�@�@555=>" + dict.getValue("555"));
		System.out.println("���f�B�R21�@�@�@�@�@�@�@�@�@�@556=>" + dict.getValue("556"));
		System.out.println("�}�b�N�@�@�@�@�@�@�@�@�@�@�@�@557=>" + dict.getValue("557"));
		System.out.println("�����ߖ�ǁ@�@�@�@�@�@�@�@�@�@558=>" + dict.getValue("558"));
		System.out.println("�E��i�I�t�B�X�j�̎��̋@�@�@�@610=>" + dict.getValue("610"));
		System.out.println("�E��i�H��E�H������j�̎��̔�611=>" + dict.getValue("611"));
		System.out.println("�w�Z���̎��̋@�@�@�@�@�@�@�@�@612=>" + dict.getValue("612"));
		System.out.println("�p�`���R�X���̎��̋@�@�@�@�@�@613=>" + dict.getValue("613"));
		System.out.println("��y�E���W���[�{�݁i�p�`���R��614=>" + dict.getValue("614"));
		System.out.println("�����X���̎��̋@�@�@�@�@�@�@�@615=>" + dict.getValue("615"));
		System.out.println("�a�@���̎��̋@�@�@�@�@�@�@�@�@616=>" + dict.getValue("616"));
		System.out.println("�T�[�r�X�G���A�̎��̋@�@�@�@�@617=>" + dict.getValue("617"));
		System.out.println("�Z��X�̓��H�����̎��̋@�@�@�@618=>" + dict.getValue("618"));
		System.out.println("���X�X�̓��H�����̎��̋@�@�@�@619=>" + dict.getValue("619"));
		System.out.println("�����E�������H�̓��H�e�����̂�620=>" + dict.getValue("620"));
		System.out.println("�T���f�[�@�@�@�@�@�@�@�@�@�@�@941=>" + dict.getValue("941"));
		System.out.println("�_�C���[�G�C�g�@�@�@�@�@�@�@�@942=>" + dict.getValue("942"));
		System.out.println("�W���C�t�����}�V���@�@�@�@�@�@943=>" + dict.getValue("943"));
		System.out.println("�Z�L�`���[�@�@�@�@�@�@�@�@�@�@944=>" + dict.getValue("944"));
		System.out.println("���T�V�@�@�@�@�@�@�@�@�@�@�@�@945=>" + dict.getValue("945"));
		System.out.println("�Ȕ��@�@�@�@�@�@�@�@�@�@�@�@�@946=>" + dict.getValue("946"));
		System.out.println("�o�k�`�m�s�@�@�@�@�@�@�@�@�@�@947=>" + dict.getValue("947"));
		System.out.println("�݂�@�@�@�@�@�@�@�@�@�@�@�@948=>" + dict.getValue("948"));
		System.out.println("�A���n�f�B�I�@�@�@�@�@�@�@�@�@949=>" + dict.getValue("949"));
		System.out.println("�W���p���@�@�@�@�@�@�@�@�@�@�@950=>" + dict.getValue("950"));
		System.out.println("�W�����e���h�[�@�@�@�@�@�@�@�@951=>" + dict.getValue("951"));
		System.out.println("�^�C���@�@�@�@�@�@�@�@�@�@�@�@952=>" + dict.getValue("952"));
		System.out.println("�z�[���Z���^�[���Ȃ��@�@�@�@�@953=>" + dict.getValue("953"));
		System.out.println("���[�z�[�@�@�@�@�@�@�@�@�@�@�@954=>" + dict.getValue("954"));
		System.out.println("�i���o�@�@�@�@�@�@�@�@�@�@�@�@955=>" + dict.getValue("955"));
		System.out.println("�~�X�^�[�}�b�N�X�@�@�@�@�@�@�@956=>" + dict.getValue("956"));
		System.out.println("�}���j�@�@�@�@�@�@�@�@�@�@�@�@957=>" + dict.getValue("957"));
		System.out.println("�����W���C�@�@�@�@�@�@�@�@�@�@958=>" + dict.getValue("958"));
		System.out.println("�_�C���b�N�X�@�@�@�@�@�@�@�@�@959=>" + dict.getValue("959"));
		System.out.println("�z�[�����C�h�@�@�@�@�@�@�@�@�@960=>" + dict.getValue("960"));
		System.out.println("�X�[�p�[�L�b�h�@�@�@�@�@�@�@�@961=>" + dict.getValue("961"));
		System.out.println("�g�h�@�q���Z�@�@�@�@�@�@�@�@�@962=>" + dict.getValue("962"));
		System.out.println("���[�\���X�g�A�P�O�O�@�@�@�@�@963=>" + dict.getValue("963"));
		System.out.println("�r�g�n�o�X�X�@�@�@�@�@�@�@�@�@964=>" + dict.getValue("964"));
		System.out.println("�r�b�N���b�L�[�@�@�@�@�@�@�@�@965=>" + dict.getValue("965"));
		System.out.println("�_�C�[���@�@�@�@�@�@�@�@�@�@�@966=>" + dict.getValue("966"));
		System.out.println("���т���@�@�@�@�@�@�@�@�@�@�@967=>" + dict.getValue("967"));
		System.out.println("�e�L�T�X�@�@�@�@�@�@�@�@�@�@�@968=>" + dict.getValue("968"));
		System.out.println("���b�L�[�@�@�@�@�@�@�@�@�@�@�@969=>" + dict.getValue("969"));
		System.out.println("���K�@�@�@�@�@�@�@�@�@�@�@�@�@970=>" + dict.getValue("970"));
		System.out.println("���O���@�@�@�@�@�@�@�@�@�@�@971=>" + dict.getValue("971"));
		System.out.println("�Z�v�E�h�[���@�@�@�@�@�@�@�@�@972=>" + dict.getValue("972"));
		System.out.println("�W���p���J�@�@�@�@�@�@�@�@�@�@973=>" + dict.getValue("973"));
		System.out.println("���̃f�p�[�g���V�_�@�@�@�@�@�@974=>" + dict.getValue("974"));
		System.out.println("�q�[���[�@�@�@�@�@�@�@�@�@�@�@975=>" + dict.getValue("975"));
		System.out.println("�������@�@�@�@�@�@�@�@�@�@�@�@976=>" + dict.getValue("976"));
		System.out.println("���̂s�n�o�@�@�@�@�@�@�@�@�@�@977=>" + dict.getValue("977"));
		System.out.println("�s�`�j�`�f�h�@�@�@�@�@�@�@�@�@978=>" + dict.getValue("978"));
		System.out.println("�˓c��́@�@�@�@�@�@�@�@�@�@�@979=>" + dict.getValue("979"));
		System.out.println("���J�[���[���h�؁@�@�@�@�@�@�@980=>" + dict.getValue("980"));
		System.out.println("���X�u���@�@�@�@�@�@�@�@�@�@�@981=>" + dict.getValue("981"));
		System.out.println("����r�b�N�@�@�@�@�@�@�@�@�@�@982=>" + dict.getValue("982"));
		System.out.println("���J�[�}�E���e���@�@�@�@�@�@�@983=>" + dict.getValue("983"));
		System.out.println("���̂������@�@�@�@�@�@�@�@�@�@984=>" + dict.getValue("984"));
		System.out.println("�r�`�j�d�s��}���V�F�@�@�@�@�@985=>" + dict.getValue("985"));
		System.out.println("���̂��񂾁@�@�@�@�@�@�@�@�@�@986=>" + dict.getValue("986"));
		System.out.println("�S�����@�@�@�@�@�@�@�@�@�@�@�@987=>" + dict.getValue("987"));
		System.out.println("�f�C�E�����N�@�@�@�@�@�@�@�@�@988=>" + dict.getValue("988"));
		System.out.println("���̌��@�@�@�@�@�@�@�@�@�@�@989=>" + dict.getValue("989"));
		System.out.println("�t�����e�B�A�@�@�@�@�@�@�@�@�@990=>" + dict.getValue("990"));
		System.out.println("�W���@�@�@�@�@�@�@�@�@�@�@�@�@991=>" + dict.getValue("991"));
		System.out.println("���J�I�[�@�@�@�@�@�@�@�@�@�@�@992=>" + dict.getValue("992"));
		System.out.println("�A���[���J�[�@�@�@�@�@�@�@�@�@993=>" + dict.getValue("993"));
		System.out.println("������@�@�@�@�@�@�@�@�@�@�@�@994=>" + dict.getValue("994"));
		System.out.println("���̃L���R�[�@�@�@�@�@�@�@�@�@995=>" + dict.getValue("995"));
		System.out.println("���񂭂�ӂ���@�@�@�@�@�@�@�@996=>" + dict.getValue("996"));
		System.out.println("�x���[�}�b�`�@�@�@�@�@�@�@�@�@997=>" + dict.getValue("997"));
		System.out.println("�т������X�@�@�@�@�@�@�@�@�@998=>" + dict.getValue("998"));

		System.out.println("���n���[�Y�@�@�@�@�@�@�@�@�@�@440=>" + dict.getValue("440"));
		System.out.println("���t���X�^�@�@�@�@�@�@�@�@�@�@441=>" + dict.getValue("441"));
		System.out.println("���}���i�J�n�@�@�@�@�@�@�@�@�@442=>" + dict.getValue("442"));
		System.out.println("���݂��܂�A���F���f�@�@�@�@�@443=>" + dict.getValue("443"));
		System.out.println("���ۍ��@�@�@�@�@�@�@�@�@�@�@�@444=>" + dict.getValue("444"));
		System.out.println("�����y�A�}���V�F�[�@�@�@�@�@�@445=>" + dict.getValue("445"));
		System.out.println("���G�u���B�@�@�@�@�@�@�@�@�@�@446=>" + dict.getValue("446"));
		System.out.println("�����b�h�L���x�c�@�@�@�@�@�@�@447=>" + dict.getValue("447"));
		System.out.println("���܂邫�O���[�v�@�@�@�@�@�@�@448=>" + dict.getValue("448"));
		System.out.println("���I�����[�����@�@�@�@�@�@�@�@449=>" + dict.getValue("449"));
		System.out.println("���}�}�C�O���[�v�@�@�@�@�@�@�@450=>" + dict.getValue("450"));
		System.out.println("���L���[�G�C�@�@�@�@�@�@�@�@�@451=>" + dict.getValue("451"));
		System.out.println("���T�j�[�}�[�g�@�@�@�@�@�@�@�@452=>" + dict.getValue("452"));
		System.out.println("���T���V���C���@�@�@�@�@�@�@�@453=>" + dict.getValue("453"));
		System.out.println("���Z�u���@�@�@�@�@�@�@�@�@�@�@454=>" + dict.getValue("454"));
		System.out.println("���Z�u���X�^�[�@�@�@�@�@�@�@�@455=>" + dict.getValue("455"));
		System.out.println("���}�����V�Z���^�[�@�@�@�@�@�@456=>" + dict.getValue("456"));
		System.out.println("���G���i�@�@�@�@�@�@�@�@�@�@�@457=>" + dict.getValue("457"));
		System.out.println("�����~�G�[���@�@�@�@�@�@�@�@�@458=>" + dict.getValue("458"));
		System.out.println("���}���~���X�g�A�@�@�@�@�@�@�@459=>" + dict.getValue("459"));
	}

	public static void test20120302() {
		//		// �X�R�[�h�ϊ������̃v���g�^�C�v�E�E�E2012/03/02
		//		String resPath = ChannelCnv.getParam();// �ϊ��p�����[�^�t�@�C��
		//�������猩���ꍇ�̃p�X
		//		String resPath = ResControl.T_RES + "qpr/src/channelCnv.txt";
		CnvDictionary dict = setUpConverter();

		// ����A�ǉ������R�[�h���ׂĂ��e�X�g
		System.out.println("���B�h�t�����X�@�@�@�@�@�@�@�@321=>" + dict.getValue("321"));
		System.out.println("�T���G�g���[���@�@�@�@�@�@�@�@322=>" + dict.getValue("322"));
		System.out.println("�n�[�X�u���E���@�@�@�@�@�@�@�@323=>" + dict.getValue("323"));
		System.out.println("���g���}�[���C�h�@�@�@�@�@�@�@324=>" + dict.getValue("324"));
		System.out.println("�����^�{�[�@�@�@�@�@�@�@�@�@�@325=>" + dict.getValue("325"));
		System.out.println("�h���N�@�@�@�@�@�@�@�@�@�@�@�@326=>" + dict.getValue("326"));
		System.out.println("�A���f���Z���@�@�@�@�@�@�@�@�@327=>" + dict.getValue("327"));
		System.out.println("�|���p�h�E���@�@�@�@�@�@�@�@�@328=>" + dict.getValue("328"));
		System.out.println("�J���e�{�[���@�@�@�@�@�@�@�@�@329=>" + dict.getValue("329"));
		System.out.println("�T���W�F���}���@�@�@�@�@�@�@�@330=>" + dict.getValue("330"));
		System.out.println("�z�����@�@�@�@�@�@�@�@�@�@�@�@331=>" + dict.getValue("331"));
		System.out.println("�_�ˉ��L�b�`���E�_�ˉ��x�[�J��332=>" + dict.getValue("332"));
		System.out.println("�T���}���N�@�@�@�@�@�@�@�@�@�@333=>" + dict.getValue("333"));
		System.out.println("�x�[�O�����x�[�O���@�@�@�@�@�@334=>" + dict.getValue("334"));
		System.out.println("�T�������[�@�@�@�@�@�@�@�@�@�@335=>" + dict.getValue("335"));
		System.out.println("�p���E�َq�X�@���̑��@�@�@�@�@307=>" + dict.getValue("307"));
	}

	//---------------------------------------------------------------------
	//�@�}�N���~���̃}�X�^�[�ɑ��݂��邪�A�ϊ��e�[�u���ɑ��݂��Ȃ����̂�􂢏o���@�y20160915�z
	// �ǉ�����ϊ��f�[�^�̐��`�𐶐�����
	//---------------------------------------------------------------------
	//���`�F�b�N�Ɏg�p����}�N���~���Œ�`���Ă���i�R�[�h�A���O��TAB�ŋ�؂����e�L�X�g�t�@�C���̗�j
	//	"C:/test/MM_Code_NAME.txt""C:/test/MM_Code_NAME.txt"
	//	184	�A�I�L�X�[�p�[
	//	185	�O�S
	//	186	�ꍆ��
	//	187	�h�~�[
	//	188	���C�X�g�A�A���V�d��
	//	189	�X�[�p�[�T���V
	//	190	�}���A�C
	//	191	����
	//---------------------------------------------------------------------
	public static void checkChannelCode() {
		CnvDictionary cnvDict = ChannelCnv.setUpConverter();
		String cnvPath = ResControlWeb.getD_Resources_QPR_RES()
				+ CHANNEL_CNV_TXT;
		HashMap<String, String> cnvMap = HashMapUtil
				.file2HashMap(cnvPath, 1, 2);
		//---------------------------------------------------------------------
		String mmPath = "C:/test/MM_Code_NAME.txt";
		HashMap<String, String> mmMap = HashMapUtil.file2HashMap(mmPath, 0, 1);
		//---------------------------------------------------------------------
		System.out.println("#################################################");
		System.out.println("## �ϊ��e�[�u���ɑ��݂��Ȃ��`���l���R�[�h");
		System.out.println("## channel.dic���Q�Ƃ��āA�g�p���Ă��Ȃ��Z�k�R�[�h�����蓖�Ă�");
		System.out.println("## channelCnv.txt�����channel.dic�ɒǉ�����");
		System.out.println("#################################################");
		System.out.println("## ���݂��Ȃ��X�R�[�h�i�����ӁA2���X�R�[�h�̊��蓖�Ăɒ��ӂ���j  ");
		System.out.println("#################################################");
		List<String> result = new ArrayList();
		if (mmMap != null) {
			List<String> keyList = new ArrayList(mmMap.keySet());
			Collections.sort(keyList);
			for (String mmCode : keyList) {
				if (!cnvMap.containsKey(mmCode)) {
					String val = mmMap.get(mmCode);
					// -----------------------------------------------------
					// �w����ϊ����� (11/03/07�`)
					String code2 = cnvDict.getValue(mmCode);
					String name1 = CharConv.cnvWide(val, 15);
					String name2 = CharConv.cnvWide(val, 5);
					String rec = code2 + TAB + mmCode + TAB + name1 + TAB
							+ name2 + "@";
					result.add(rec);
					System.out.println(rec);
				}
			}
		}
		System.out.println("#################################################");
		String oPath =  "c:/�ǉ�����X�R�[�h���`.txt";
		EzWriter.list2File(oPath, result);
	}

	public static void main(String[] argv) {
		//		testConverter() ;
		//		 test110228();
		//		test20120302();
		kyPkg.util.ChannelCnv.checkChannelCode();
	}
}
