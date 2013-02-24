package operators;

import util.JMException;
import util.wrapper.XReal;
import core.Solution;

public class Normalizer {

	public void norm(Solution s) throws JMException {		
		XReal xr1=new XReal(s);		
		double orig[]=new double[xr1.size()];
		double sum=0;
		double tmp=0;
		for (int i = 0; i < xr1.size(); i++) {
			orig[i]=xr1.getValue(i);;
			sum+=orig[i];
		}
		for (int i = 0; i < xr1.size(); i++) {
			tmp=orig[i];
			tmp/=sum;
			xr1.setValue(i, tmp);
		}			
	}		
}
