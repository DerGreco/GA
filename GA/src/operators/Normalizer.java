package operators;

import util.JMException;
import util.wrapper.XReal;
import core.Solution;

public class Normalizer {

	public void norm(Solution s) throws JMException {
		XReal xr1=new XReal(s);		
		double sum=0;
		double tmp=0;
		for (int i = 0; i < xr1.size(); i++) {
			sum+=xr1.getValue(i);
		}
		for (int i = 0; i < xr1.size(); i++) {
			tmp=xr1.getValue(i);
			xr1.setValue(i, tmp/sum);
		}			
	}		
}
