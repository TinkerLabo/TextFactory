package kyPkg.util;

import java.util.Properties;
import java.util.Set;

public class ProxyTest {
	public static void main(String[] argv) {
		//�v���L�V�T�[�o�[���ݒ肳��Ă��Ȃ��ƃv���Z�V���O��LoadImage�����܂������Ȃ��̂ŁA���ϐ��̊m�F���ݒ�̐��`�������Ă݂�
		//bef�̒i�K�Ńv���L�V�̐ݒ肪�����Ă��Ȃ��̂ŁE�E�E�Esetting�]�X�ɏ�������ł����ʂȂ񂾂낤�E�E�E�E		
		Properties systemSettings = System.getProperties();
		Set keySet = systemSettings.keySet();
		System.out.println("bef-----------------------------------------------------------");
		for (Object key : keySet) {
		  System.out.println("#"+key.toString()+"=>"+systemSettings.get(key));
		}
		systemSettings.put("http.proxyHost", "agcproxy.tokyu-agc.co.jp");
		systemSettings.put("http.proxyPort", "80");
		System.setProperties(systemSettings);
		System.out.println("aft-----------------------------------------------------------");
		for (Object key : keySet) {
		  System.out.println("#"+key.toString()+"=>"+systemSettings.get(key));
		}


	}
}
