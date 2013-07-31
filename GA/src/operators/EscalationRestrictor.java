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
		int indexOfLastMin=-1;
		max=value=0;
		min=1;
		int count=0;
		
		for (int i = 0; i < xr.size(); i++) {
			value=xr.getValue(i);
			if(value>max)max=value;
			if(value!=0){
				count++;
				if(value<min)min=value;			
			}
		}
		
		if(max!=min){
			for (int i = 0; i < xr.size(); i++) {
				value=xr.getValue(i);
				if(value!=0){
					value=((value-min)/(max-min));				
					value=(value*(_pmax-_pmin))+_pmin;				
					xr.setValue(i, value);							
				}			
			}
		}else{
			for (int i = 0; i < xr.size(); i++) {
				value=xr.getValue(i);
				if(value!=0){
					if(1/count<=_pmax)xr.setValue(i, 1/count);
					else xr.setValue(i, _pmax);
				}
			}
		}		
		
		while(sum(xr)>1)xr.setValue(indexOfLastMin=indexOfMin(xr), 0);	
		
		value=sum(xr);
		if(value<1){
			double deficit=1-value;			
			int index=0;			
			while(deficit>0  && !new Double(deficit).isNaN()){
				index=indexOfNearestToMax(xr);
				if(index==-1){
					if(indexOfLastMin==-1){						
						deficit=insertMaxRandom(xr, deficit);
					}else{
						if(deficit>_pmax){
							xr.setValue(indexOfLastMin, _pmax);
							deficit-=_pmax;
						}else{
							xr.setValue(indexOfLastMin, deficit);
							deficit=0;
						}							
					}					
				}else{
					value=xr.getValue(index);				
					if(value+deficit<=_pmax){
						xr.setValue(index, value+deficit);
						deficit=0;
					}
					else{
						xr.setValue(index, _pmax);
						deficit-=_pmax-value;
					}	
				}						
			}			
		}		
	}
	
	private double insertMaxRandom(XReal xr, double deficit) throws JMException{
		int randomIndex=(int)Math.floor(Math.random()*xr.size());
		while(xr.getValue(randomIndex)!=0)randomIndex=(int)Math.floor(Math.random()*xr.size());
		if(deficit>_pmax){
			xr.setValue(randomIndex, _pmax);
			deficit-=_pmax;
		}else{
			xr.setValue(randomIndex, deficit);
			deficit=0;
		}		
		return deficit;
	}

	private int indexOfNearestToMax(XReal xr) throws JMException {
		int toRet=-1;
		double value, nearestValue;		
		value=nearestValue=0;
		for (int i = 0; i < xr.size(); i++) {
			value=xr.getValue(i);
			if(value!=0 && value<_pmax){
				if(value>nearestValue){
					nearestValue=value;
					toRet=i;
				}
			}
		}
		return toRet;
	}

	private double sum(XReal xr) throws JMException{
		double toRet=0;
		for (int i = 0; i < xr.size(); i++) {
			toRet+=xr.getValue(i);
		}
		return toRet;
	}
	
	private int indexOfMin(XReal xr) throws JMException{
		int toRet=-1;
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
