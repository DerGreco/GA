package test;

import static org.junit.Assert.*;
import jmetal.MyProblem;

import operators.EscalationRestrictor;

import org.junit.Test;

import util.JMException;
import util.wrapper.XReal;

import core.Problem;
import core.Solution;
import core.Variable;
import encodings.solutionType.ArrayRealSolutionType;
import encodings.variable.ArrayReal;
import encodings.variable.Real;

public class EscalationRestrictorTest {

	@Test
	public void testRearrange() throws JMException, ClassNotFoundException {
		int j=0;
		do{
			EscalationRestrictor er=new EscalationRestrictor();
			Double[]d=
					/*
					{0.11401324638136141, 0.2270247562583415, 				
					0.0833082994939674, 0.18314894719813243, 
					0.012122363286617466, 0.15211618764903748, 
					0.20142252469829655, 0.2575168947234156, 
					0.25737151602135294, 0.054154859454614804, 
					0.04480820524418921};
					*/
					/*
				{0.0, 0.30622740340426435, 0.0, 0.0, 0.35, 0.34043100991136577, 0.0, 0.0, 0.0, 0.35, 0.0};
				*/
					/*
				{0.0, 0.0, 0.34446608714123944, 0.0, 0.0, 0.29944898656249874, 0.0, 0.0, 0.018982846143424464, 0.0, 0.018529123141117154};
				*/
				{0.0, 0.4, 0.0, 0.4, 0.4, 0.0, 0.0, 0.4, 0.0, 0.0, 0.0};
			Variable[]variables=new ArrayReal[1];
			Solution s=new Solution();
			ArrayReal ar=new ArrayReal();
			ar.array_=d;
			ar.size_=11;
			variables[0]=ar;
			s.setDecisionVariables(variables);
			s.setType(new ArrayRealSolutionType(new MyProblem()));
			er.rearrange(s);
			
			XReal xr=new XReal(s);
			double sum, max, min, value;
			sum=max=min=value=0;		
			min=er.getMin();
			max=er.getMax();
			for (int i = 0; i < xr.size(); i++) {
				value=xr.getValue(i);
				if((value!=0 && value<min) || value>max){
					System.out.println("Falla el restrictor, limites");
				}
				sum+=value;
			}
			if(sum<0.99999999 || sum>1.00000001){
				System.out.println("Falla el restrictor, normalizacion");
			}
			j++;
		}while(j<1000);
	}

}
