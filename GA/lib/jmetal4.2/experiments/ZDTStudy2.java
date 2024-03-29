//  ZDTStudy.java
//
//  Authors:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//
//  Copyright (c) 2012 Antonio J. Nebro
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

package experiments;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import core.Algorithm;
import core.Problem;
import experiments.settings.AbYSS_Settings;
import experiments.settings.GDE3_Settings;
import experiments.settings.MOCell_Settings;
import experiments.settings.NSGAII_Settings;
import experiments.settings.SPEA2_Settings;
import experiments.settings.SMPSO_Settings;
import experiments.util.Friedman;
import experiments.util.RBoxplot;
import experiments.util.RWilcoxon;
import util.JMException;

/**
 * Class implementing an example experimental study. Three algorithms are 
 * compared when solving the benchmarks, and the hypervolume,
 * spread and additive epsilon indicators are used for performance assessment.
 * This experiment assumes that the reference Pareto fronts are unknown.
 */
public class ZDTStudy2 extends Experiment {

	/**
	 * Configures the algorithms in each independent run
	 * @param problemName The problem to solve
	 * @param problemIndex
	 * @throws ClassNotFoundException 
	 */
	public void algorithmSettings(String problemName, 
			int problemIndex, 
			Algorithm[] algorithm) throws ClassNotFoundException {
		try {
			int numberOfAlgorithms = algorithmNameList_.length;

			HashMap[] parameters = new HashMap[numberOfAlgorithms];

			for (int i = 0; i < numberOfAlgorithms; i++) {
				parameters[i] = new HashMap();
			} // for

			if (!(paretoFrontFile_[problemIndex] == null) && !paretoFrontFile_[problemIndex].equals("")) {
				for (int i = 0; i < numberOfAlgorithms; i++)
					parameters[i].put("frontPath_", frontPath_[problemIndex]);
				//parameters[i].put("paretoFrontFile_", paretoFrontFile_[problemIndex]);
			} // if

			algorithm[0] = new NSGAII_Settings(problemName).configure(parameters[0]);
			algorithm[1] = new SMPSO_Settings(problemName).configure(parameters[1]);
			algorithm[2] = new GDE3_Settings(problemName).configure(parameters[2]);
			algorithm[3] = new AbYSS_Settings(problemName).configure(parameters[3]);
		} catch (IllegalArgumentException ex) {
			Logger.getLogger(StandardStudy.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(StandardStudy.class.getName()).log(Level.SEVERE, null, ex);
		} catch  (JMException ex) {
			Logger.getLogger(StandardStudy.class.getName()).log(Level.SEVERE, null, ex);
		}
	} // algorithmSettings

	/**
	 * Main method
	 * @param args
	 * @throws JMException
	 * @throws IOException
	 */
	public static void main(String[] args) throws JMException, IOException {
		ZDTStudy2 exp = new ZDTStudy2();

		exp.experimentName_ = "ZDTStudy2";
		exp.algorithmNameList_ = new String[]{
				"NSGAII", "SMPSO", "GDE3", "AbySS"};
		exp.problemList_ = new String[]{"ZDT1", "ZDT2","ZDT3", "ZDT4","ZDT6"};
		exp.paretoFrontFile_ = new String[7]; // 7 problems

		exp.indicatorList_ = new String[]{"HV", "SPREAD", "EPSILON"};

		int numberOfAlgorithms = exp.algorithmNameList_.length;

		exp.experimentBaseDirectory_ = "/Users/antelverde/Softw/pruebas/jmetal/" +
				exp.experimentName_;
		exp.paretoFrontDirectory_ = ""; // This directory must be null

		exp.algorithmSettings_ = new Settings[numberOfAlgorithms];

		exp.independentRuns_ = 20;

		// Run the experiments
		int numberOfThreads ;
		exp.runExperiment(numberOfThreads = 7) ;
		
    // Generate latex tables
    exp.generateLatexTables() ;

    // Configure the R scripts to be generated
    int rows  ;
    int columns  ;
    String prefix ;
    String [] problems ;
    boolean notch ;

    // Configuring scripts for ZDT
    rows = 3 ;
    columns = 3 ;
    prefix = new String("ZDT");
    problems = new String[]{"ZDT1", "ZDT2","ZDT3", "ZDT4","ZDT6"} ;

    exp.generateRBoxplotScripts(rows, columns, problems, prefix, notch = false, exp) ;
    exp.generateRWilcoxonScripts(problems, prefix, exp) ;
	} // main
} // ZDTStudy


