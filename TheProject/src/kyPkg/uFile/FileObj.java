package kyPkg.uFile;

import java.io.File;

public class FileObj {
	private boolean file;
	private boolean exists;
	private long length;
	private String iPath;
	private String parent;
	private String filename;
	private String name;
	private String ext = "";
	public FileObj(String path) {
		super();
		this.iPath = path;
		String array[];
		File iFile = new File(iPath);
		if (iFile.isFile()){
			file=true;
		}
		if (iFile.exists()){
			exists=true;
			length = iFile.length();
			parent = iFile.getParent();
			filename=iFile.getName();
			array = filename.split("\\.");
			name = array[0];
			if (array.length>=2){
				ext = "."+array[1];
			}
		}
	}
	//　拡張子
	public String getExt() {
		return ext;
	}
	//　拡張子以外の名前
	public String getName() {
		return name;
	}
	public boolean isExist() {
		return exists;
	}
	public boolean isFile() {
		return file;
	}
	public String getParent() {
		return parent;
	}
	public long getLength() {
		return length;
	}

}
