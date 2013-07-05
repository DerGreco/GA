package runtime;

import java.io.IOException;
import java.io.PrintWriter;

public class Run {

	
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {		
		String[] command =
		    {
		        "cmd",
		    };
		String[] envp =
		    {
		        "path="+System.getenv("Path")
		    };
		System.out.println(envp[0]);
		    Process p = Runtime.getRuntime().exec(command, envp);
		    new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
		    new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
		    PrintWriter stdin = new PrintWriter(p.getOutputStream());
		    stdin.println("cd C:\\Users\\9dgonzalezg\\Desktop\\workspace\\GA\\" +
		    		"GA\\resultsMyNSGAIIExperiment\\data\\Ejecuciones\\Indicators_0\\");
		    stdin.println("rscript contrastar.r");
		    // write any other commands you want here
		    stdin.close();
		    int returnCode = p.waitFor();
		    System.out.println("Return code = " + returnCode);
	}

}
