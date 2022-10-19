import java.io.File;

public class MatrizSalida {
	private int cantidad = 0;
	private int contador = 0;
	private int imagenes[];
	
	public int CantidadCarpertas(File Carpeta) {
		for (File Entrada : Carpeta.listFiles()) {
	        if (Entrada.isDirectory()) {
	        	CantidadCarpertas(Entrada);
	        	cantidad++;
	        }
	    }
		imagenes = new int[cantidad];
		return cantidad;
	}
	
	public int[] CantidadImagenes(File Carpeta) {
		int con = 0;
		for (File Entrada : Carpeta.listFiles()) {
	        if (Entrada.isDirectory()) {
	        	CantidadImagenes(Entrada);
	        	contador++;
	        }
	        else {
	        	con++;
	        	imagenes[contador] = con;
	        }
	    }
		return imagenes;
	}
	
	
	public double[][] Salida(File Carpeta) {
		int total = CantidadCarpertas(Carpeta);
		int image[] = CantidadImagenes(Carpeta);
		//System.out.print(Arrays.toString(image));
		int suma = 0;
		for(int i=0 ; i<total; i++) {
			suma = suma + image[i];
		}
		
		double out[][] = new double[suma][total];
		
		int ini = 0;
		int fin = image[0];
		for(int k=0 ; k<total; k++) {
			for(int i=ini; i<fin; i++) {
				for(int j=0 ; j<total; j++) {
					if(j==k)
						out[i][j]=1;
				}
			}
			if(k<total-1) {
				ini = fin;
				fin = fin+image[k+1];
			}
		}
		return out;
	}
	
	/*
	public static void main(String[] args) {
		MatrizSalida ms = new MatrizSalida();
		double salida[][] = ms.Salida(new File("DB"));
    }
    */
}
