//  WFG4.java
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

package problems.WFG;

import core.Solution;
import core.Variable;
import util.JMException;

/**
 * This class implements the WFG4 problem
 * Reference: Simon Huband, Luigi Barone, Lyndon While, Phil Hingston
 *            A Scalable Multi-objective Test Problem Toolkit.
 *            Evolutionary Multi-Criterion Optimization: 
 *            Third International Conference, EMO 2005. 
 *            Proceedings, volume 3410 of Lecture Notes in Computer Science
 */
public class WFG4 extends WFG{
  
 /**
  * Creates a default WFG4 with 
  * 2 position-related parameter,
  * 4 distance-related parameter and
  * 2 objectives
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public WFG4(String solutionType) throws ClassNotFoundException {
    this(solutionType, 2, 4, 2) ;
  } // WFG4

 /**
  * Creates a WFG4 problem instance
  * @param k Number of position parameters
  * @param l Number of distance parameters
  * @param M Number of objective functions
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public WFG4 (String solutionType, Integer k, Integer l, Integer M) throws ClassNotFoundException {
    super(solutionType, k,l,M);
    problemName_ = "WFG4";
    
    S_ = new int[M_];
    for (int i = 0; i < M_; i++) {
      S_[i] = 2 * (i+1);
    }
    
    A_ = new int[M_-1];
    for (int i = 0; i < M_-1; i++) {
      A_[i] = 1;
    }
  } // WFG4
  
  /** 
  * Evaluates a solution 
  * @param z The solution to evaluate
  * @return double [] with the evaluation results
  */    
  public float [] evaluate(float [] z){
    float [] y;
    
    y = normalise(z);
    y = t1(y,k_);
    y = t2(y,k_,M_);
    
    float [] result = new float[M_];
    float [] x = calculate_x(y);
    for (int m = 1; m <= M_ ; m++) {
      result [m-1] = D_*x[M_-1] + S_[m-1] * (new Shapes()).concave(x,m);
    }
    
    return result;
  } // evaluate
  
  /**
   * WFG4 t1 transformation
   */  
  public float [] t1(float [] z, int k){
    float [] result = new float[z.length];
    
    for (int i = 0; i < z.length; i++) {
      result[i] = (new Transformations()).s_multi(z[i],30,10,(float)0.35);
    }
    
    return result;
  } // t1
  
  /**
  * WFG4 t2 transformation
  */  
  public float [] t2(float [] z, int k, int M){
    float [] result = new float[M];
    float [] w      = new float[z.length];
    
    for (int i = 0; i < z.length; i++) {
      w[i] = (float)1.0;
    }
            
    for (int i = 1; i <= M-1; i++) {
      int head = (i - 1)*k/(M-1) + 1;
      int tail = i * k / (M - 1);        
      float [] subZ = subVector(z,head-1,tail-1);
      float [] subW = subVector(w,head-1,tail-1);
        
      result[i-1] = (new Transformations()).r_sum(subZ,subW);
    }
      
    int head = k + 1;
    int tail = z.length;
      
    float [] subZ = subVector(z,head-1,tail-1);
    float [] subW = subVector(w,head-1,tail-1);
    result[M-1] = (new Transformations()).r_sum(subZ,subW);
      
    return result;
  } // t2
    
  /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */        
  public final void evaluate(Solution solution) throws JMException {
    float [] variables = new float[this.getNumberOfVariables()];
    Variable[] dv = solution.getDecisionVariables();
      
    for (int i = 0; i < this.getNumberOfVariables(); i++) {
      variables[i] = (float)dv[i].getValue();
    }
      
    float [] sol = evaluate(variables);
      
    for (int i = 0; i < sol.length; i++) {
      solution.setObjective(i,sol[i]);
    }
  } // evaluate
    
}
