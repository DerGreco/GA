package runtime;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import obsolete.NormPolynomialMutation;
import obsolete.NormSBXCrossover;

import data_structures.Matrices;

import jmetal.MyNSGAII;
import jmetal.MyProblem;
import core.Algorithm;
import core.Operator;
import core.Problem;
import core.SolutionSet;
import metaheuristics.nsgaII.NSGAII;
import operators.GreedyRestrictor;
import operators.crossover.CrossoverFactory;
import operators.crossover.SBXCrossover;
import operators.mutation.MutationFactory;
import operators.mutation.PolynomialMutation;
import operators.selection.SelectionFactory;
import qualityIndicator.QualityIndicator;
import util.Configuration;
import util.JMException;

public class Main {

	public static Logger      logger_ ;      // Logger object
	public static FileHandler fileHandler_ ; // FileHandler object
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws SecurityException 
	 * @throws ClassNotFoundException 
	 * @throws JMException 
	 */
	public static void main(String[] args) throws SecurityException, IOException, ClassNotFoundException, JMException {
		Problem   _problem   ; // The problem to solve
	    Algorithm _algorithm ; // The algorithm to use
	    Operator  _crossover ; // Crossover operator
	    Operator  _mutation  ; // Mutation operator
	    Operator  _selection ; // Selection operator
	    
	    HashMap  _parameters ; // Operator parameters
	    
	    QualityIndicator _indicators ; // Object to get quality indicators
	    
	    //System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));

	    // Logger object and file to store log messages
	    logger_      = Configuration.logger_ ;
	    fileHandler_ = new FileHandler("NSGAII_main.log"); 
	    logger_.addHandler(fileHandler_) ;
	    
	    _indicators = null ;
	    
	    _problem=new MyProblem(new Matrices(0), new GreedyRestrictor());
	    _algorithm=new MyNSGAII(_problem);
	    
	    _algorithm.setInputParameter("populationSize",100);
	    _algorithm.setInputParameter("maxEvaluations",25000);
	    
	 // Mutation and Crossover for Real codification 
	    _parameters = new HashMap() ;
	    _parameters.put("probability", 0.9) ;
	    _parameters.put("distributionIndex", 20.0) ;
	    _crossover = new SBXCrossover(_parameters);                   

	    _parameters = new HashMap() ;
	    _parameters.put("probability", 1.0/_problem.getNumberOfVariables()) ;
	    _parameters.put("distributionIndex", 20.0) ;
	    _mutation = new PolynomialMutation(_parameters); 
	    		
	    // Selection Operator 
	    _parameters = null ;
	    _selection = SelectionFactory.getSelectionOperator("BinaryTournament2", _parameters) ;
	    
	 // Add the operators to the algorithm
	    _algorithm.addOperator("crossover",_crossover);
	    _algorithm.addOperator("mutation",_mutation);
	    _algorithm.addOperator("selection",_selection);

	    // Add the indicator object to the algorithm
	    _algorithm.setInputParameter("indicators", _indicators) ;
	    
	    // Execute the Algorithm
	    long initTime = System.currentTimeMillis();
	    SolutionSet population = _algorithm.execute();
	    long estimatedTime = System.currentTimeMillis() - initTime;
	    
	    // Result messages 
	    logger_.info("Total execution time: "+estimatedTime + "ms");
	    logger_.info("Variables values have been writen to file VAR");
	    population.printVariablesToFile("VAR");    
	    logger_.info("Objectives values have been writen to file FUN");
	    population.printObjectivesToFile("FUN");
	  
	    if (_indicators != null) {
	      logger_.info("Quality indicators") ;
	      logger_.info("Hypervolume: " + _indicators.getHypervolume(population)) ;
	      logger_.info("GD         : " + _indicators.getGD(population)) ;
	      logger_.info("IGD        : " + _indicators.getIGD(population)) ;
	      logger_.info("Spread     : " + _indicators.getSpread(population)) ;
	      logger_.info("Epsilon    : " + _indicators.getEpsilon(population)) ;  
	     
	      int evaluations = ((Integer)_algorithm.getOutputParameter("evaluations")).intValue();
	      logger_.info("Speed      : " + evaluations + " evaluations") ;      
	    } // if

	}

}
