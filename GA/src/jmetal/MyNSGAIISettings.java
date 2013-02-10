package jmetal;

import java.lang.reflect.Field;
import java.util.HashMap;

import operators.crossover.BLXAlphaCrossover;
import operators.crossover.Crossover;
import operators.crossover.CrossoverFactory;
import operators.crossover.DifferentialEvolutionCrossover;
import operators.crossover.SBXCrossover;
import operators.mutation.Mutation;
import operators.mutation.MutationFactory;
import problems.ProblemFactory;
import util.JMException;
import core.Algorithm;
import core.Operator;
import data_structures.Matrices;
import encodings.solutionType.ArrayRealSolutionType;
import encodings.solutionType.BinaryRealSolutionType;
import encodings.solutionType.BinarySolutionType;
import encodings.solutionType.RealSolutionType;
import experiments.Settings;

public class MyNSGAIISettings extends Settings {
	
	  public int populationSize_                 ; 
	  public int maxEvaluations_                 ;
	  public double mutationProbability_         ;
	  public double crossoverProbability_        ;
	  public double mutationDistributionIndex_   ;
	  public double crossoverDistributionIndex_  ;
	  public Crossover crossoverOperator_	 ;
	  
	  /**
	   * Constructor
	   * @throws JMException 
	   */
	  public MyNSGAIISettings(String problem) throws JMException {
	    super(problem) ;
	    
	    Object [] problemParams = {"Real"};
	    //problem_ = (new ProblemFactory()).getProblem(problemName_, problemParams);
		try {
			problem_ = new MyProblem(new Matrices());
		} catch (ClassNotFoundException e) {
			System.out.println("Problemas en la constuccion del problema");
			e.printStackTrace();
		}  
	    // Default settings
	    populationSize_              = 100   ; 
	    maxEvaluations_              = 25000 ;
	    mutationProbability_         = 1.0/problem_.getNumberOfVariables() ;
	    crossoverProbability_        = 0.9   ;
	    mutationDistributionIndex_   = 20.0  ;
	    crossoverDistributionIndex_  = 20.0  ;
	    crossoverOperator_ = new SBXCrossover(new HashMap<String, Object>());
	  }

	  
	@Override
	public Algorithm configure() throws JMException {
		// TODO Auto-generated method stub
		return null;
	} // NSGAII_Settings

}
