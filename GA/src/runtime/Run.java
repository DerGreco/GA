package runtime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.math3.genetics.TournamentSelection;

import operators.GreedyRestrictor;
import operators.ProportionalRestrictor;
import util.JMException;

import jmetal.MyNSGAIIExperiment;

public class Run {
		
	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws JMException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException, JMException {
		Results res=new Results();		
		MyNSGAIIExperiment exp=new MyNSGAIIExperiment(new ProportionalRestrictor());		
		String[] folders=new String[2];
		folders=exp.experiment();	
		res.results(folders);
		exp=new MyNSGAIIExperiment(new GreedyRestrictor());
		folders=exp.experiment();
		res.results(folders);
		
		String[] command ={"cmd"};
		String[] envp = {"path="+System.getenv("Path")};		
	    Process p = Runtime.getRuntime().exec(command, envp);	    
	    new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
	    new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
	    PrintWriter stdin = new PrintWriter(p.getOutputStream());	    
	    stdin.println("cd C:\\Users\\9dgonzalezg\\Desktop\\workspace\\GA\\" +
	    		"GA\\resultsMyNSGAIIExperiment\\data\\");	    
	    stdin.println("rscript contrastarEPSILON.r");	    
	    stdin.println("rscript contrastarIGD.r");		    
	    stdin.println("rscript contrastarSPREAD.r");	    
	    stdin.println("rscript contrastarHV.r");	    
	    stdin.close();
	    int returnCode = p.waitFor();
	    System.out.println("Return code = " + returnCode);
	}
		
}
