package runtime;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class Results {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public void results(String[] f) throws IOException {		
		String [] indicators={"EPSILON","IGD","SPREAD","HV"};
		String [] operators={"BLX","SBX","DEX"};
		int runs=15;
		double aux=0;
		String str="";
		BufferedReader br=null;
		Vector<PrintWriter> pw=new Vector<PrintWriter>();
		DescriptiveStatistics d=new DescriptiveStatistics();
		for (int i = 0; i < operators.length; i++) {
			for (int j = 0; j < indicators.length; j++) {
				pw.add(new PrintWriter("C:\\Users\\9dgonzalezg\\Desktop\\workspace\\GA\\GA" +
						"\\resultsMyNSGAIIExperiment\\data\\"+new String(f[i])
						+"_"+indicators[j]+".res"));
				for(int k=0; k<runs; k++){
					br=new BufferedReader(new FileReader("C:\\Users\\9dgonzalezg" +
							"\\Desktop\\workspace\\GA\\GA\\resultsMyNSGAIIExperiment\\" +
							"data\\"+f[i]+"\\"+new Integer(k).toString()+"\\"+
							indicators[j]));
					str=br.readLine();
					while (str!=null) {
						aux=Double.parseDouble(str);
						d.addValue(aux);
						str=br.readLine();
					}	
					br.close();
					pw.get(j).println(d.getMean());
					d.clear();
				}
				pw.get(j).close();
			}
			pw.clear();			
		}
	}
}
