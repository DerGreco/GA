package data_structures;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
/*
import db_management.DB_Handler;
import file_management.File_Handler;
*/

public class Matrices {
	
	private double [][] _rendimientos=null;
	private RealMatrix _covarianza=null;
	private RealMatrix _matrizRendimientos=null;
	private RealMatrix _correlacion=null;
	private double [] _rendimientoMedio=new double[11];
	
	/*
	private DB_Handler dbh=new DB_Handler();
	private File_Handler fh=new File_Handler();
	*/
	
	/**
	 * Es necesario seleccionar valores con el mismo numero de historicos, para evitar ceros 
	 * en la matriz, si un valor empezo a cotizar mas tarde y no hay datos para el.
	 * 
	 * Demasiado hardcoded (fichero y dimensiones)
	 */
	public Matrices(int offset){
		
		boolean escribirFichero=false;
		
		DescriptiveStatistics d=new DescriptiveStatistics();
		double store=0;
		double[][] cargaRend=new double[11][115];
		_rendimientos=new double[11][100];				
		
		if(escribirFichero){
			/*
			dbh.masterOpen();
			int i=0,j=0;
			for (String s : fh.get_quotes()) {
				for (KeyData k : dbh.getDataSeries(s, "MonthlyRet", "cotizacion_mensual")) {
					if(j==0){
						
					}else{
						store=k.get_data();
						d.addValue(store);
						_rendimientos[i][j-1]=store;
					}				
					j++;
				}
				_rendimientoMedio[i]=d.getMean();
				d.clear();
				i++;
				j=0;
			}
			try {
				FileOutputStream fos = new FileOutputStream("rend.dat");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(_rendimientos);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
		}else{
			try {
				FileInputStream fis = new FileInputStream("rend.dat");
				ObjectInputStream iis = new ObjectInputStream(fis);
				cargaRend = (double[][]) iis.readObject();
				/*
				for (int i = 0; i < cargaRend.length; i++) {
					for (int j = 0; j < cargaRend[i].length; j++) {
						d.addValue(cargaRend[i][j]);
					}
					_rendimientoMedio[i]=d.getMean();
					d.clear();
				}
				*/
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i = 0; i < _rendimientos.length; i++) {
			for (int j = 0; j < _rendimientos[i].length; j++) {
				_rendimientos[i][j]=cargaRend[i][j+offset];
				d.addValue(_rendimientos[i][j]);
			}
			_rendimientoMedio[i]=d.getMean();
			d.clear();
		}
		_matrizRendimientos=new Array2DRowRealMatrix(_rendimientos);
		_covarianza=new Covariance(_matrizRendimientos).getCovarianceMatrix();
		_correlacion=new PearsonsCorrelation(_rendimientos).getCorrelationMatrix();
	}

	public RealMatrix get_covarianza() {
		return _covarianza;
	}

	public RealMatrix get_correlacion() {
		return _correlacion;
	}

	public double[] get_rendimientoMedio() {
		return _rendimientoMedio;
	}		
}
