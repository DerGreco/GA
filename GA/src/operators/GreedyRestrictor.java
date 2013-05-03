package operators;

import java.util.Collections;
import java.util.Vector;

import util.JMException;
import util.wrapper.XReal;
import core.Solution;
import data_structures.RestrictorResult;

public class GreedyRestrictor implements Restrictor {

	private double _pmax=.35;
	private double _pmin=.1;
	private RestrictorResult _result=null;
	
	@Override
	public void rearrange(Solution solution) throws JMException {
		
		XReal xr=new XReal(solution);
		double excess=0;
		double value=0;
		Vector<Integer> ordered_indexes=sortValues(solution);					
		for (Integer i : ordered_indexes) {
			value=xr.getValue(i);
			if(value>_pmax){
				excess+=value-_pmax;					
				xr.setValue(i, _pmax);
			}else if(_pmin<=value && value<=_pmax){
				if(value+excess<=_pmax){
					xr.setValue(i, value+excess);
					excess=0;						
				}else{
					xr.setValue(i, _pmax);
					excess-=(_pmax-value);
				}
			}else{
				xr.setValue(i, 0);
				excess+=value;
			}
		}		
	}
	
	private Vector<Integer> sortValues(Solution s) throws JMException{		
		Vector<Integer> toRet=new Vector<Integer>();
		Vector<Double> aux=new Vector<Double>();
		XReal xr=new XReal(s);
		double value=0;
		for (int i = 0; i < xr.size(); i++)	aux.add(xr.getValue(i));		
		Collections.sort(aux);
		for (Double d : aux) {
			for (int i = 0; i < xr.size(); i++) {
				if(d==xr.getValue(i))toRet.add(new Integer(i));
			}
		}
		Collections.reverse(toRet);
		return toRet;
	}
}
