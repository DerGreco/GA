package jmetal;

import operators.Normalizer;
import operators.Restrictor;
import data_structures.Matrices;
import core.Problem;
import core.Solution;
import encodings.solutionType.ArrayRealSolutionType;
import util.JMException;
import util.wrapper.XReal;

public class MyProblem extends Problem {
	
	Matrices _m=null;	

	public MyProblem(Matrices m) throws ClassNotFoundException{
		solutionType_=new ArrayRealSolutionType(this); //can throw the above exception
		numberOfVariables_=11;
		numberOfConstraints_=0;
		numberOfObjectives_=2;
		problemName_="CarteraInversion";	
		double [] zeros=new double[11];
		double [] ones=new double[11];
		for (int i = 0; i < zeros.length; i++) {
			zeros[i]=0;
		}
		for (int i = 0; i < ones.length; i++) {
			ones[i]=0.4;
		}
		lowerLimit_=zeros;
		upperLimit_=ones;
		_m=m;
	}
	
	
	@Override
	public void evaluate(Solution solution) throws JMException {
		
		double [] f=new double[numberOfObjectives_];
		
		double g=evalRend(solution);
		double h=evalRiesgo(solution);
		
		//System.out.println("Riesgo: "+h);
		
		f[0]=-g; //con el menos solo aqui ejecuta bien alguna vez, las otras falla tanto en mergeHi como en Lo
		f[1]=h;
		
		solution.setObjective(0, f[0]); //con el menos solo aqui ejecuta bien alguna vez, las otras falla tanto en mergeHi como en Lo
		solution.setObjective(1, f[1]); //con los dos en positivo (o en negativo) tambien falla alguna vez.
	}
	
	public void evaluateConstraints (Solution s) throws JMException{
		Normalizer norm=new Normalizer();
		Restrictor rest=new Restrictor();
		norm.norm(s);
		rest.rearrange(s);
	}


	/**
	 * Copia rapida del metodo expuesto por CLM
	 * @param s
	 * @return
	 * @throws JMException
	 */
	private double evalRiesgo(Solution s) throws JMException {
		double tr=0;
		XReal xr=new XReal(s);
		double [] vectorDesviaciones = new double [numberOfVariables_];
		for (int i=0 ; i<numberOfVariables_ ; i++){			
			vectorDesviaciones [i] = Math.sqrt(_m.get_covarianza().getEntry(i,i));
		}
		for (int i=0 ; i<numberOfVariables_ ; i++){
			for(int j=0; j<numberOfVariables_; j++){
				if(i == j){
					tr += _m.get_covarianza().getEntry(i, i)*Math.pow(xr.getValue(j),2);
				}else {
				tr += vectorDesviaciones[i]*vectorDesviaciones[j]*xr.getValue(i)*xr.getValue(j)*_m.get_correlacion().getEntry(i, j);
				}
			}
		}
		return tr;
	}


	private double evalRend(Solution s) throws JMException {
		double tr=0;
		double[] rm=_m.get_rendimientoMedio();
		XReal xr=new XReal(s);
		for (int i = 0; i < rm.length; i++) {
			tr+=rm[i]*xr.getValue(i);
		}
		return tr;
	}

}
