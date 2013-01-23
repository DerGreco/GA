package obsolete;

import java.util.HashMap;

import core.Solution;
import operators.Normalizer;
import operators.Restrictor;
import operators.mutation.PolynomialMutation;
import util.JMException;
import util.wrapper.XReal;

public class NormPolynomialMutation extends PolynomialMutation {
	
	Restrictor rest=new Restrictor();
	Normalizer norm=new Normalizer();

	public NormPolynomialMutation(HashMap<String, Object> parameters) {
		super(parameters);
	}
	
	public Object execute(Object object) throws JMException {
		Solution mutated;
		mutated=(Solution)super.execute(object);
		norm.norm(mutated);
		boolean ret;		
		rest.rearrange(mutated);		
		return mutated;
	}		
}
