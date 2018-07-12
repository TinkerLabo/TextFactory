package kyPkg.sql;

import java.util.List;

public interface Inf_Closure {
	public abstract void init();
	public abstract void execute(List<Object> list);
	public abstract void fin();
}
