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
		/*
		MyNSGAIIExperiment exp=new MyNSGAIIExperiment(new ProportionalRestrictor());
		Results res=new Results();
		String[] folders=new String[2];
		folders=exp.experiment();
		res.results(folders);
		exp=new MyNSGAIIExperiment(new GreedyRestrictor());
		folders=exp.experiment();
		res.results(folders);
		*/
		String[] command ={"cmd"};
		String[] envp = {"path="+System.getenv("Path")};		
	    Process p = Runtime.getRuntime().exec(command, envp);
	    new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
	    new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
	    PrintWriter stdin = new PrintWriter(p.getOutputStream());
	    String[] ind={"EPSILON","IGD","SPREAD","HV"};
	    stdin.println("cd C:\\Users\\9dgonzalezg\\Desktop\\workspace\\GA\\" +
	    		"GA\\resultsMyNSGAIIExperiment\\data\\");
	    for (int i = 0; i < ind.length; i++) {
	    	new Run().touchContrastar(ind[i]);
		    stdin.println("rscript contrastar.r");
		    
		}	    
	    stdin.close();
	    int returnCode = p.waitFor();
	    System.out.println("Return code = " + returnCode);
	}
	
	private void touchContrastar(String s) throws IOException{
		BufferedReader br=new BufferedReader(new FileReader(
				"C:\\Users\\9dgonzalezg\\Desktop\\workspace\\GA\\" +
	    		"GA\\resultsMyNSGAIIExperiment\\data\\contrastar.r"));
		PrintWriter pw=new PrintWriter("C:\\Users\\9dgonzalezg\\Desktop\\workspace\\GA\\" +
	    		"GA\\resultsMyNSGAIIExperiment\\data\\contrastarAux.r");
		String str=br.readLine();
		while(str!=null){
			if(str.contains("fuentesdatos<-list.files(pattern=")){
				pw.println("fuentesdatos<-list.files(pattern=\""+s+".res\")");
			}else pw.println(str);
			str=br.readLine();
		}
		br.close();
		pw.close();
		new File("C:\\Users\\9dgonzalezg\\Desktop\\workspace\\GA\\" +
	    		"GA\\resultsMyNSGAIIExperiment\\data\\contrastar.r").delete();
		new File("C:\\Users\\9dgonzalezg\\Desktop\\workspace\\GA\\" +
	    		"GA\\resultsMyNSGAIIExperiment\\data\\contrastarAux.r").renameTo(
	    				new File("C:\\Users\\9dgonzalezg\\Desktop\\workspace\\GA\\" +
	    			    		"GA\\resultsMyNSGAIIExperiment\\data\\contrastar.r"));
	}

}
