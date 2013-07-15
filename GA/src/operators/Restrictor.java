package operators;

import util.JMException;
import core.Solution;

public interface Restrictor {

	public void rearrange(Solution solution) throws JMException;
	
	public String getName();
}
