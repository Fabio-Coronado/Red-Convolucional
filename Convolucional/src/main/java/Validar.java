import java.io.*;
import java.util.Collections;
import java.util.List;

public class Validar {
public static double[][][] trainTest;
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
    	ListaImagenes lista = new ListaImagenes();
        convolutional_utils utils = new convolutional_utils();

        double[][][] filtros1;
        double[][][][] filtros2;
        //cargando objeto que contiene la red entrenada
        FileInputStream fir = new FileInputStream(new File("src/Red.txt"));
        FileInputStream fif = new FileInputStream(new File("src/Filtros.txt"));
        ObjectInputStream oir = new ObjectInputStream(fir);
        ObjectInputStream oif = new ObjectInputStream(fif);
        //cargando filtros
        NeuralNetwork nnp = (NeuralNetwork) oir.readObject();
        filtros1 = (double [][][]) oif.readObject();
        filtros2 = (double [][][][]) oif.readObject();
        //filtros3 = (double [][][][]) oif.readObject();
        //filtros4 = (double [][][][]) oif.readObject();
        oir.close();
        fir.close();
        
        String names[] = {"beans","cake","candy","cereal","chips",
    			"chocolate","coffee","corn","fish",
    			"flour","honey","jam","juice","milk",
    			"nuts","oil","pasta","rice","soda","spices",
    			"sugar","tea","tomato_sauce","vinegar","water"};
        
        int pos = 9;
        
        System.out.println("Procesando imagenes para validacion...");
        //trainTest = lista.Lista(new File("src/validar/"+names[pos]), 128, 128);
        trainTest = lista.Lista(new File("src/validar/validar"), 128, 128);
        System.out.println("Convolucionando imagenes para validacion...");
        
        double [][] entradatest = utils.proceso_convolucion(trainTest,4, filtros1, filtros2);
        
        
        List<Double>output;
        System.out.println("Validando...");
        double accuracy = 0;
        for(double d[]:entradatest){   

            //nn.pesos contiene una lista de matrices de pesos finales. 
            //Ej: Para acceder a la primera matriz -> nn.pesos.get(0).data
            //nn.bias contiene una lista de matrices de bias finales. 
            //Ej: Para acceder a la primera matriz -> nn.bias.get(0).data
                output = nnp.predict(d);
                System.out.println(output.toString());
                
                Double dd = Collections.max(output);
                for(int u=0; u<names.length; u++) {
                	if(dd==output.get(u)) {
                		System.out.println("Indice: "+u+" - "+"Producto: "+names[u]);
                		if(u==pos)
                			accuracy++;
                	}
                		
                }
        }
        
        System.out.println("Accuracy: "+accuracy/entradatest.length*100.0+"%");
	
    }
}
