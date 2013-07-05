package jmetal;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import operators.crossover.BLXAlphaCrossover;
import operators.crossover.Crossover;
import operators.crossover.DifferentialEvolutionCrossover;
import operators.crossover.SBXCrossover;

import util.JMException;
import core.Algorithm;
import experiments.Experiment;
import experiments.NSGAIIStudy;
import experiments.Settings;
import experiments.settings.NSGAII_Settings;

public class MyNSGAIIExperiment extends Experiment {

	@Override
	public void algorithmSettings(String problemName, int problemId, Algorithm[] algorithm) throws ClassNotFoundException {
		try {
		      int numberOfAlgorithms = algorithmNameList_.length;

		      HashMap[] parameters = new HashMap[numberOfAlgorithms];

		      for (int i = 0; i < numberOfAlgorithms; i++) {
		        parameters[i] = new HashMap();
		      } // for
		      
		      //Crossover c0=new SBXCrossover(new HashMap<String, Object>());
		      //Crossover c1=new BLXAlphaCrossover(new HashMap<String, Object>());
		      //Crossover c2=new DifferentialEvolutionCrossover(new HashMap<String, Object>());
		      
		      String c0="SBXCrossover";		      		      	     
		      String c1="BLXAlphaCrossover";

		      parameters[0].put("crossoverOperator_", c0);
		      parameters[1].put("crossoverOperator_", c1);		      	     

		      if ((!paretoFrontFile_[problemIndex].equals("")) || 
		      		(paretoFrontFile_[problemIndex] == null)) {
		        for (int i = 0; i < numberOfAlgorithms; i++)
		          parameters[i].put("paretoFrontFile_",  paretoFrontFile_[problemIndex]);
		      } // if
		 
		      Settings s=null;
		      Algorithm a=null;
		      for (int i = 0; i < numberOfAlgorithms; i++){
		    	s = new MyNSGAIISettings(problemName);
		    	a = s.configure(parameters[i]);
		        algorithm[i] = a;
		      }
		      
		    } catch (IllegalArgumentException ex) {
		      Logger.getLogger(NSGAIIStudy.class.getName()).log(Level.SEVERE, null, ex);
		    } catch (IllegalAccessException ex) {
		      Logger.getLogger(NSGAIIStudy.class.getName()).log(Level.SEVERE, null, ex);
		    } catch (JMException ex) {
		      Logger.getLogger(NSGAIIStudy.class.getName()).log(Level.SEVERE, null, ex);
		    }
	}
	
	public static void main(String[] args) throws JMException, IOException {
	    MyNSGAIIExperiment exp = new MyNSGAIIExperiment() ; // exp = experiment
	    
	    exp.experimentName_  = "MyNSGAIIExperiment" ;
	    exp.algorithmNameList_   = new String[] {
	      "NSGAII+SBX",
	      "NSGAII+BLX"
	      } ;
	    exp.problemList_     = new String[] {
	      "0","1","2","3","4","5","6","7","8","9","10","11","12","13","14"} ;
	    exp.paretoFrontFile_ = new String[] {
	      "","","","","","","","","","","","","","",""} ;
	    exp.indicatorList_   = new String[] {"HV", "SPREAD", "IGD", "EPSILON"} ;
	    
	    int numberOfAlgorithms = exp.algorithmNameList_.length ;

	    //Para casa
	    
	    /*
	    exp.experimentBaseDirectory_ = "C:\\Users\\Dani\\git\\GA\\GA\\results" +
	                                   exp.experimentName_;
	    exp.paretoFrontDirectory_ = "C:\\Users\\Dani\\git\\GA\\GA\\resultsMyNSGAIIExperiment\\referenceFronts";
	    */
	    //Para curro
	    
	    exp.experimentBaseDirectory_ = "C:\\Users\\9dgonzalezg\\Desktop\\workspace\\GA\\GA\\results" +
	    								exp.experimentName_;
	    exp.paretoFrontDirectory_ = "C:\\Users\\9dgonzalezg\\Desktop\\workspace\\GA\\GA\\resultsMyNSGAIIExperiment\\referenceFronts";	    
	    	    
	    
	    //exp.algorithmSettings_ = new Settings[numberOfAlgorithms] ;
	    
	    exp.independentRuns_ = 20 ;
	    
	    // Run the experiments
	    int numberOfThreads ;
	    exp.runExperiment() ;
	    
	    // Generate latex tables (comment this sentence is not desired)
	    exp.generateLatexTables() ;
	    
	    // Configure the R scripts to be generated
	    int rows  ;
	    int columns  ;
	    String prefix ;
	    String [] problems ;

	    rows = 2 ;
	    columns = 3 ;
	    prefix = new String("Problems");
	    problems = new String[]{"MyProblem"} ;

	    boolean notch ;
	    exp.generateRBoxplotScripts(rows, columns, problems, prefix, notch = true, exp) ;
	    exp.generateRWilcoxonScripts(problems, prefix, exp) ;  
	  } // main

}
