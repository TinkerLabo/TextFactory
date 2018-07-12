package kyPkg.processor;
import java.io.Writer;

import kyPkg.pmodel.ParameterModelInf;
public interface Inf_WebProcessor {
	public  void execute(Writer writer,ParameterModelInf pModel);
}