package kyPkg.sql;
import java.util.List;
public interface Connector {
	public abstract JDBC getConnection();
	public abstract void close();
	public abstract List query2Matrix(String Sql);
	public abstract String query2Str(String sql);

}