package kyPkg.converter;

public class TranslateFactory implements Inf_FilterFactory {
	public static String TITLE = "translate";
	private String explain = "ˆø”‚P•ÏŠ·‘O‚Ì•¶ŽšŒQAˆø”‚Q‚É‚»‚ê‚¼‚ê‘Î‰ž‚·‚é•¶ŽšŒQAˆø”‚R‚ÉƒfƒtƒHƒ‹ƒg•¶Žš@—á>@a,b,c:1,2,3:null a=>1,b=>2";
	private String sample = "0,1,2:,a,b:null";
	private Inf_Converter coverter = null;
	private String param = "";

	@Override
	public Inf_Converter getConverter(String param) {
		if (coverter == null || !this.param.equals(param)) {
			coverter = new Translate(param);
			this.param = param;
		}
		return coverter;
	}

	@Override
	public String getExplain() {
		return explain;
	}

	@Override
	public String getSample() {
		return sample;
	}
}
