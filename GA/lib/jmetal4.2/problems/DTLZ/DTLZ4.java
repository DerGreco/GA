//  DTLZ4.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package problems.DTLZ;

import core.*;
import encodings.solutionType.BinaryRealSolutionType;
import encodings.solutionType.RealSolutionType;
import util.JMException;
import util.Configuration.*;

/**
 * Class representing problem DTLZ4
 */
public class DTLZ4 extends Problem{
   
  
 /**
  * Creates a default DTLZ4 problem (12 variables and 3 objectives) 
  * @param solutionType The solution type must "Real" or "BinaryReal". 
  */
  public DTLZ4(String solutionType) throws ClassNotFoundException {
    this(solutionType, 12,3);
  }
    
 /**
  * Creates a DTLZ4 problem problem instance 
  * @param numberOfVariables Number of variables
  * @param numberOfObjectives Number of objective functions
  * @param solutionType The solution type must "Real" or "BinaryReal". 
  */
  public DTLZ4(String  solutionType,
               Integer numberOfVariables,
               Integer numberOfObjectives) throws ClassNotFoundException {
    numberOfVariables_  = numberOfVariables.intValue();
    numberOfObjectives_ = numberOfObjectives.intValue();
    numberOfConstraints_= 0;
    problemName_        = "DTLZ4";
        
    lowerLimit_ = new double[numberOfVariables_];
    upperLimit_ = new double[numberOfVariables_];        
    for (int var = 0; var < numberOfVariables_; var++){
      lowerLimit_[var] = 0.0;
      upperLimit_[var] = 1.0;
    }
        
    if (solutionType.compareTo("BinaryReal") == 0)
    	solutionType_ = new BinaryRealSolutionType(this) ;
    else if (solutionType.compareTo("Real") == 0)
    	solutionType_ = new RealSolutionType(this) ;
    else {
    	System.out.println("Error: solution type " + solutionType + " invalid") ;
    	System.exit(-1) ;
    }            
  } //DTLZ4         

  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */      
  public void evaluate(Solution solution) throws JMException {
    Variable[] gen  = solution.getDecisionVariables();
  
    double [] x = new double[numberOfVariables_];
    double [] f = new double[numberOfObjectives_];
    double alpha = 100.0;
    int k = numberOfVariables_ - numberOfObjectives_ + 1;
  
    for (int i = 0; i < numberOfVariables_; i++)
      x[i] = gen[i].getValue();

    double g = 0.0;
    for (int i = numberOfVariables_ - k; i < numberOfVariables_; i++)
      g += (x[i] - 0.5)*(x[i] - 0.5);                
        
    for (int i = 0; i < numberOfObjectives_; i++)
      f[i] = 1.0 + g;
        
    for (int i = 0; i < numberOfObjectives_; i++) {
      for (int j = 0; j < numberOfObjectives_ - (i + 1); j++)            
        f[i] *= java.lang.Math.cos(java.lang.Math.pow(x[j],alpha)*(java.lang.Math.PI/2.0));                
        if (i != 0){
          int aux = numberOfObjectives_ - (i + 1);
          f[i] *= java.lang.Math.sin(java.lang.Math.pow(x[aux],alpha)*(java.lang.Math.PI/2.0));
        } //if
    } // for
        
    for (int i = 0; i < numberOfObjectives_; i++)
      solution.setObjective(i,f[i]);                
  } // evaluate 
}

