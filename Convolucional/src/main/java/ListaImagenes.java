
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ListaImagenes {
	
	int total;
	int index;
	double [][][] lista;
	
	public double[][][] Lista(File Carpeta, int dim1, int dim2) {
		total = 0;
		index = 0;
		TotalImagenes(Carpeta);
		lista = new double[total][dim1][dim2];
		CarpetaDeImagenes(Carpeta, dim1, dim2);
		return lista;
	}
	
	public void CarpetaDeImagenes(File Carpeta, int dim1, int dim2) {		
		double[][] imagen = new double[dim1][dim2];
		BufferedImage b_imagen = null;
		String SubCarpeta = "";
		
	    for (File Entrada : Carpeta.listFiles()) {
	        if (Entrada.isDirectory()) {
	        	CarpetaDeImagenes(Entrada, dim1, dim2);
	        	SubCarpeta = Entrada.getName();
	        } else {
	        	try {
	        		b_imagen = ImageIO.read(new File(Carpeta +"/"+SubCarpeta+"/"+ Entrada.getName()));
	        		ImageUtils nuevo = new ImageUtils();
                                        ////
					imagen = nuevo.transformImageToArray(b_imagen);
					
					for(int i=0; i<dim1; i++) {
						for(int j=0; j<dim2; j++) {
							lista[index][i][j]= imagen[i][j];
						}
				    }
					index++;
				} catch (IOException e) {System.out.print("ERROR");}
	        }
	    }  
	}
        
        public double[][] Salida(int cantidad, int total) {
		double salida[][] = new double[cantidad*total][total];
		
		int j = 0;
		for(int i=0; i<cantidad*total; i++) {
	    	salida[i][j]=1;
	    	if((i+1)%cantidad==0 && i!=0) {
	    		j++;
	    	}
	    }
		return salida;
	}
	
	public void TotalImagenes(File Carpeta) {
		for (File Entrada : Carpeta.listFiles()) {
	        if (Entrada.isDirectory())
	        	TotalImagenes(Entrada);
	        else
	        	total++;
	    }   
	}
	

}
