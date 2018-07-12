package kyPkg.filter;

import java.util.List;

public abstract class OutClosureBase implements Inf_oClosure {

	@Override
	public boolean write(List splited, int stat) {
		System.out
				.println("#ERROR! 実装されていないメソッドを実行しています => write(List splited, int stat)");
		return false;
	}

	@Override
	public boolean write(String[] splited, int stat) {
		System.out
				.println("#ERROR! 実装されていないメソッドを実行しています => write(String[] splited, int stat)");
		return false;
	}

	@Override
	public boolean write(String rec) {
		System.out.println("#ERROR! 実装されていないメソッドを実行しています => write(String rec)");
		return true;
	}

	@Override
	public boolean write(Inf_iClosure reader) {
		System.out
				.println("#ERROR! 実装されていないメソッドを実行しています => write(Inf_iClosure reader)");
		return true;
	}

	@Override
	public void setHeader(String header) {
	}

	@Override
	public void setHeader(List<String> headList, String delimiter) {
	}

	@Override
	public void setHeader(String[] headArray, String delimiter) {
	}
}
