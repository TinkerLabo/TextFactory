package kyPkg.socks;

import java.io.*;
import java.net.*;
import java.util.List;

//���ɗ����Ȃ�������02�E�E�E2009/09/07
public class TestMulipart {
	private String BOUNDARY = "|||BOUNDARY|||";

	private List<TransportFile> fileList = null;

	private List<Parameter> paramList = null;

	/**
	 * multipart�f�[�^�o�́B
	 * 
	 * @param con
	 *            HTTP�R�l�N�V����
	 * @throws IOException
	 */
	void sendMultipart(HttpURLConnection con) throws IOException {

		// multipart/form-data�`��
		con.setRequestProperty("Content-type",
				"multipart/form-data;  boundary=" + BOUNDARY);

		// �f�[�^�X�g���[���I�[�v��
		DataOutputStream out = new DataOutputStream(con.getOutputStream());

		// MultiPart�̃f�[�^�쐬
		// ��������
		for (int i = 0; i < this.paramList.size(); i++) {
			Parameter param = this.paramList.get(i);

			out.writeBytes("--" + BOUNDARY + "\r\n");
			out.writeBytes("Content-Disposition: form-data; ");
			out.writeBytes("name=\"" + param.getName() + "\"\r\n");
			out.writeBytes("\r\n");

			byte[] byteBuf = param.getValue().getBytes();
			for (int j = 0; j < byteBuf.length; j++) {
				out.writeByte(byteBuf[j]);
			}
			out.writeBytes("\r\n");
		}

		// �t�@�C������
		for (int i = 0; i < this.fileList.size(); i++) {
			TransportFile file = this.fileList.get(i);
			String exsistingFileName = file.getFileName();
			out.writeBytes("--" + BOUNDARY + "\r\n");
			out.writeBytes("Content-Disposition: form-data; ");
			out.writeBytes("name=\"" + file.getParamName() + "\"; ");
			out.writeBytes("filename=\"" + exsistingFileName + "\"\r\n");
			out.writeBytes("Content-Type: application/octet-stream\r\n");
			out.writeBytes("\r\n");
			int fileInt = 0;
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(file.getFile()));
			while ((fileInt = in.read()) != -1) {
				out.writeByte(fileInt);
			}
			in.close();
			out.writeBytes("\r\n");
		}

		// ���X�g
		out.writeBytes("--" + BOUNDARY + "--");
		out.flush();
		out.close();
	}

	class Parameter {
		private String name = "";

		private String value;

		String getName() {
			return name;
		}

		String getValue() {
			return value;
		}

	}

	class TransportFile {
		private File file;

		private String paramName = "";

		private String fileName = "";

		File getFile() {
			return file;
		}

		String getFileName() {
			return fileName;
		}

		String getParamName() {
			return paramName;
		}
	}

}
