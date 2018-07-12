package kyPkg.rez;

public class NameSet {
	private String title = "";
	private String[] tag = null;
	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	private NameSet() {
		super();
	}
	public NameSet(String title, String[] tag) {
		this();
		this.title = title;
		this.tag = tag;
	}
	public NameSet(String title, String tagStr) {
		this(title, new String[]{tagStr});
	}
	public String[] getTag() {
		return tag;
	}

	public void setTag(String[] tag) {
		this.tag = tag;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
