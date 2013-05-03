package obsolete;

import java.util.HashMap;

import core.Solution;
import operators.Normalizer;
import operators.ProportionalRestrictor;
import operators.crossover.SBXCrossover;
import util.JMException;
import util.wrapper.XReal;

public class NormSBXCrossover extends SBXCrossover {
	
	ProportionalRestrictor rest=new ProportionalRestrictor();
	Normalizer norm=new Normalizer();
	
	public NormSBXCrossover(HashMap<String, Object> parameters) {
		super(parameters);
	}
	
	public Object execute(Object object) throws JMException {
		Solution [] offSpring;
		offSpring=(Solution[])super.execute(object);
		norm.norm(offSpring[0]);		
		rest.rearrange(offSpring[0]);		
		norm.norm(offSpring[1]);
		rest.rearrange(offSpring[1]);		
		return offSpring;
	}	
}
