package runtime;

import data_structures.Matrices;

public class Pruebas {

	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		
		Matrices m=new Matrices();
		for (double d : m.get_rendimientoMedio()) {
			System.out.println(d);
		}
		
	}
}
