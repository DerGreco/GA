package jmetal;

import java.lang.reflect.Field;
import java.util.HashMap;

import metaheuristics.nsgaII.NSGAII;

import operators.Restrictor;
import operators.crossover.BLXAlphaCrossover;
import operators.crossover.Crossover;
import operators.crossover.CrossoverFactory;
import operators.crossover.DifferentialEvolutionCrossover;
import operators.crossover.SBXCrossover;
import operators.mutation.Mutation;
import operators.mutation.MutationFactory;
import operators.selection.Selection;
import operators.selection.SelectionFactory;
import problems.ProblemFactory;
import qualityIndicator.QualityIndicator;
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
	  //public String crossoverOperator_;
	  
	  /**
	   * Constructor
	   * @throws JMException 
	   */
	  public MyNSGAIISettings(String problem, Restrictor r) throws JMException {
	    super(problem) ;
	    
	    Object [] problemParams = {"Real"};
	    //problem_ = (new ProblemFactory()).getProblem(problemName_, problemParams);
		try {
			problem_ = new MyProblem(new Matrices(Integer.parseInt(problem)),r);
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
	    //crossoverOperator_ = "SBXCrossover";
	  }

	  
	@Override
	public Algorithm configure() throws JMException {
		Algorithm algorithm ;
	    Selection  selection ;
	    Crossover  crossover ;
	    Mutation   mutation  ;

	    HashMap  parameters ; // Operator parameters

	    QualityIndicator indicators ;
	    
	    // Creating the algorithm. There are two choices: NSGAII and its steady-
	    // state variant ssNSGAII
	    algorithm = new MyNSGAII(problem_) ;
	    //algorithm = new ssNSGAII(problem_) ;
	    
	    // Algorithm parameters
	    algorithm.setInputParameter("populationSize",populationSize_);
	    algorithm.setInputParameter("maxEvaluations",maxEvaluations_);

	    // Mutation and Crossover for Real codification
	    parameters = new HashMap() ;
	    parameters.put("probability", crossoverProbability_) ;
	    parameters.put("distributionIndex", crossoverDistributionIndex_) ;
	    crossover = crossoverOperator_;                   

	    parameters = new HashMap() ;
	    parameters.put("probability", mutationProbability_) ;
	    parameters.put("distributionIndex", mutationDistributionIndex_) ;
	    mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters);                        

	    // Selection Operator 
	    parameters = null ;
	    selection = SelectionFactory.getSelectionOperator("BinaryTournament2", parameters) ;     

	    // Add the operators to the algorithm
	    algorithm.addOperator("crossover",crossover);
	    algorithm.addOperator("mutation",mutation);
	    algorithm.addOperator("selection",selection);
	    
	    
	    // Deleted since jMetal 4.2
	   // Creating the indicator object
	   if ((paretoFrontFile_!=null) && (!paretoFrontFile_.equals(""))) {
	      indicators = new QualityIndicator(problem_, paretoFrontFile_);
	      algorithm.setInputParameter("indicators", indicators) ;  
	   } // if
	   
	    return algorithm ;
	} 

}
