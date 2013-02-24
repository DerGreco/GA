package operators;

import data_structures.RestrictorResult;
import core.Solution;
import util.JMException;
import util.wrapper.XReal;

public class Restrictor {
	
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
		
		/*
		 * Codigo para depuracion
		 */
		XReal xr1=new XReal(solution);
		double sum=0;
		sum=33;
		//System.out.print("Rearranged chromosome: [");
		for (int i = 0; i < xr1.size(); i++) {
			sum+=xr1.getValue(i);
			//System.out.print(xr1.getValue(i)+",");
		}
		//System.out.println("]");
		//System.out.println("Normalization = "+sum);
	}		
	
	private RestrictorResult truncateOneBelowMin(boolean[] truncated, Solution solution) throws JMException {
		
		boolean changed=false;
		XReal xr=new XReal(solution);
		double excess=0;
		double value=0;
		double final_value=0;
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
			//repartir el exceso de modo proporcional entre los no truncados
			double sum=0;
			for (int i = 0; i < xr.size(); i++) {
				value=xr.getValue(i);
				if(!truncated[i]){
					sum+=value;
				}				
			}
			for (int i = 0; i < xr.size(); i++) {
				value=xr.getValue(i);
				if(!truncated[i]){
					final_value=value+(value/sum)*excess;
					//No asignar nunca Nan, habra que ver como afecta a las soluciones
					if(new Double(final_value).isNaN()){
						xr.setValue(i, 0);
					}else{
						xr.setValue(i, value);
					}					
				}				
			}
		}
		return new RestrictorResult(changed, truncated);
	}

	private RestrictorResult truncateAllAboveMax(boolean[] truncated, Solution solution) throws JMException {
		
		boolean changed=false;
		XReal xr=new XReal(solution);
		double excess=0;
		double value=0;
		double final_value=0;
		
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
			double sum=0;
			for (int i = 0; i < xr.size(); i++) {
				value=xr.getValue(i);
				if(!truncated[i]){
					sum+=value;
				}				
			}
			for (int i = 0; i < xr.size(); i++) {
				value=xr.getValue(i);
				if(!truncated[i]){
					final_value=value+(value/sum)*excess;
					//No asignar nunca Nan, habra que ver como afecta a las soluciones
					if(new Double(final_value).isNaN()){
						xr.setValue(i, 0);
					}else{
						xr.setValue(i, value);
					}	
				}				
			}
		}				
		return new RestrictorResult(changed, truncated);
	}

	private boolean[] reset(boolean[] b){
		for (int i = 0; i < b.length; i++) {
			b[i]=false;
		}
		return b;
	}
}
