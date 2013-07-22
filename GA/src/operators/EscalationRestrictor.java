package operators;

import util.JMException;
import util.wrapper.XReal;
import core.Solution;

public class EscalationRestrictor implements Restrictor {

	private double _pmax=.35;
	private double _pmin=.1;
	
	@Override
	public void rearrange(Solution solution) throws JMException {
		XReal xr=new XReal(solution);
		double max, min, value;
		max=value=0;
		min=1;
		for (int i = 0; i < xr.size(); i++) {
			value=xr.getValue(i);
			if(value>max)max=value;
			else if(value!=0 && value<min)min=value;
		}
		for (int i = 0; i < xr.size(); i++) {
			value=xr.getValue(i);
			xr.setValue(i, (((value-min)/(max-min))*(_pmax-_pmin))+_pmin);
		}
		while(sum(xr)>1){
			xr.setValue(indexOfMin(xr), 0);
			if(sum(xr)<1){
				int ind=indexOfMin(xr);
				value=xr.getValue(ind);
				xr.setValue(ind, value+1-sum(xr));
			}
		}
	}

	private double sum(XReal xr) throws JMException{
		double toRet=0;
		for (int i = 0; i < xr.size(); i++) {
			toRet+=xr.getValue(i);
		}
		return toRet;
	}
	
	private int indexOfMin(XReal xr) throws JMException{
		int toRet=0;
		double value, ref;
		value=ref=1;
		for (int i = 0; i < xr.size(); i++) {
			value=xr.getValue(i);
			if(value!=0 && value<ref){
				toRet=i;
				ref=value;
			}
		}
		return toRet;
	}
	
	@Override
	public String getName() {		
		return "Escal";
	}

	@Override
	public double getMax() {		
		return _pmax;
	}

	@Override
	public double getMin() {
		return _pmin;
	}

}
