package operators;

import java.io.PrintWriter;

import jmetal.MyProblem;

import data_structures.Matrices;
import data_structures.RestrictorResult;
import core.Solution;
import util.JMException;
import util.wrapper.XReal;

public class ProportionalRestrictor implements Restrictor {
	
	private double _pmax=.35;
	private double _pmin=.1;
	private RestrictorResult _result=null;		

	public void rearrange(Solution solution) throws JMException {
		
		//Variables auxiliares componentes del RestrictorResult
		boolean changedMinAux=false;
		boolean changedMaxAux=false;
		
		boolean[] truncated=new boolean[11];		
		truncated=reset(truncated);
		
		//truncar en una primera pasada los que excedan el maximo
		_result=truncateAllAboveMax(truncated, solution);
		truncated=_result.get_truncated();
		
		//ir truncando de uno en uno los minimos, controlando los maximos hasta que no haya cambios
		do {
			_result=truncateOneBelowMin(truncated, solution);
			changedMinAux=_result.is_changed();
			truncated=_result.get_truncated();
			_result=truncateAllAboveMax(truncated, solution);
			changedMaxAux=_result.is_changed();
			truncated=_result.get_truncated();
		} while (changedMaxAux || changedMinAux);		
	}
	
	private RestrictorResult truncateOneBelowMin(boolean[] truncated, Solution solution) throws JMException {
		
		boolean changed=false;
		XReal xr=new XReal(solution);
		double excess=0;
		double value=0;
		double min_value=1000;
		int index_min=-1;
		
		for (int i = 0; i < xr.size(); i++) {
			value=xr.getValue(i);			
			if(value!=0 && value<_pmin && value<min_value){
				min_value=value;
				index_min=i;
				changed=true;
			}
		}
		if(changed){
			//truncar el minimo a cero, anotarlo y guardar el exceso
			xr.setValue(index_min, 0);
			truncated[index_min]=true;
			excess=min_value;			
			redistribute(xr, truncated, value, excess);
		}
		return new RestrictorResult(changed, truncated);
	}

	private RestrictorResult truncateAllAboveMax(boolean[] truncated, Solution solution) throws JMException {
		
		boolean changed=false;
		XReal xr=new XReal(solution);
		double[]copy=new double[xr.size()];
		double excess=0;
		double value=0;
		
		for (int i = 0; i < xr.size(); i++) {
			copy[i]=xr.getValue(i);
		}
		
		//truncar a _pmax y guardar el exceso
		for (int i = 0; i < xr.size(); i++) {
			value=xr.getValue(i);			
			if(value>_pmax){
				xr.setValue(i, _pmax);
				excess+=value-_pmax;
				truncated[i]=true;
				changed=true;
			}
		}
		
		//repartir el exceso de modo proporcional		
		if(changed){
			redistribute(xr, truncated, value, excess);
		}				
		return new RestrictorResult(changed, truncated);
	}

	private void redistribute(XReal xr, boolean[] truncated, double value, double excess ) throws JMException{
		double sum=0;
		double final_value=0;
		for (int i = 0; i < xr.size(); i++) {
			value=xr.getValue(i);
			if(!truncated[i]){
				sum+=value;
			}				
		}
		if(sum!=0){
			for (int i = 0; i < xr.size(); i++) {
				value=xr.getValue(i);
				if(!truncated[i]){
					final_value=value+(value/sum)*excess;									
					xr.setValue(i, final_value);						
				}				
			}
		}else{			
			for (int i = 0; i < xr.size(); i++) {
				value=xr.getValue(i);
				if(!truncated[i]){
					xr.setValue(i, excess);	
					break;
				}
			}					
		}		
	}	
	
	private boolean[] reset(boolean[] b){
		for (int i = 0; i < b.length; i++) {
			b[i]=false;
		}
		return b;
	}
	
	@Override
	public String getName() {		
		return "Prop";
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
