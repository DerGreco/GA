package operators;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
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
			//Si excede el maximo truncar al maximo y guardar el exceso
			if(value>_pmax){
				excess+=value-_pmax;					
				xr.setValue(i, _pmax);
			}else if(value<_pmin){
				//Si no llega al minimo truncar a cero y guardar el exceso		
				xr.setValue(i, 0);
				excess+=value;
			}				
		}
		for (Integer i : ordered_indexes) {
			value=xr.getValue(i);			
			//Si es un valor intermedio rellenar hasta el maximo, o hasta donde permita el exceso
			if(_pmin<=value && value<=_pmax){
				if(value+excess<=_pmax){
					xr.setValue(i, value+excess);
					excess=0;						
				}else{
					xr.setValue(i, _pmax);
					excess-=(_pmax-value);
				}
			}
		}
		//Si al terminar el exceso es distinto de cero, ir rellenando en orden		
		boolean reversed=false;
		if(excess>0){
			for (Integer i : ordered_indexes) {
				value=xr.getValue(i);
				if(value<_pmax){
					if(value+excess>_pmax){
						xr.setValue(i, _pmax);
						excess-=(_pmax-value);
					}else if(value+excess>=_pmin && value+excess<=_pmax){
						xr.setValue(i, value+excess);
						excess=0;						
					}else if(value==0 && excess<_pmin && excess>0){
						if(!reversed){
							Collections.reverse(ordered_indexes);
							reversed=true;
						}
						redistribute(ordered_indexes, solution);
					}					
				}				
			}
		}		
	}
	
	private Vector<Integer> sortValues(Solution s) throws JMException{	
		SortedSet<Integer> orden=new TreeSet<Integer>();
		Vector<Integer> toRet=new Vector<Integer>();
		Vector<Double> aux=new Vector<Double>();
		XReal xr=new XReal(s);
		double value=0;
		for (int i = 0; i < xr.size(); i++)	aux.add(xr.getValue(i));		
		Collections.sort(aux);
		for (Double d : aux) {
			for (int i = 0; i < xr.size(); i++) {
				if(d==xr.getValue(i))orden.add(new Integer(i));
			}
		}
		for (Integer i : orden) {
			toRet.add(i);
		}
		Collections.reverse(toRet);		
		return toRet;
	}

	private void redistribute(Vector<Integer> reverseOrder, Solution s) throws JMException{
		
		XReal xr=new XReal(s);
		int index_to_min=-1;
		double value=0;
		double to_min=0;
		
		for (Integer i : reverseOrder) {
			value=xr.getValue(i);
			if(value==0)index_to_min=i;
			else{				
				if(value-to_min>=_pmin){
					xr.setValue(index_to_min, _pmin);
					xr.setValue(i, value-to_min);
				}else{
					xr.setValue(i, _pmin);
					to_min=value-_pmin;
				}
			}
		}
	}

	@Override
	public String getName() {		
		return "Greedy";
	}
	
}
