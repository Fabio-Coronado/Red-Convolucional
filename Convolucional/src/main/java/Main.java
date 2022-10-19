

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {

    public double[][][] trainData;
    public double[][][] trainTest;
    
    public static void main(String[] args) throws IOException {
        Main cnn = new Main();


        ListaImagenes lista = new ListaImagenes();
        convolutional_utils utils = new convolutional_utils();
        
        System.out.println("Procesando imagenes");
        //lista de imagenes 
        cnn.trainData = lista.Lista(new File("src/evaluar/train"), 256, 256);
        cnn.trainTest = lista.Lista(new File("src/evaluar/test"), 256, 256);;
        
        
        
        double[][][] filtros = {
                {{-1, -1, -1}, 
                {2, 2, 2}, 
                {-1, -1, -1}},
                
                {{-1, 2, -1}, 
                {-1, 2, -1}, 
                {-1, 2, -1}},
                
                {{-1, -1, 2}, 
                {-1, 2, -1}, 
                {2, -1, -1}},
                
                {{2, -1, -1}, 
                {-1, 2, -1}, 
                {-1, -1, 2}},
            
            };
            
        double [][][][] filtros2 = utils.filtros_aleatorios(3, 3, 3, 2);
        double [][][][] filtros3 = utils.filtros_aleatorios(3, 3, 3, 2);

        
        //funcion que genera clasificacion
        double salida[][] = lista.Salida(cnn.trainData.length, 3);
        

        //Convolucion
        System.out.println("Convolucionando imagenes");
        double [][] entrada = utils.proceso_convolucion(cnn.trainData, filtros, filtros2, filtros3);
        double [][] entradatest = utils.proceso_convolucion(cnn.trainTest, filtros, filtros2, filtros3);

        int dimension2 = entrada[0].length;

                int capas[] = {dimension2, 200,100, 3};
        
        
        
                NeuralNetwork nn = new NeuralNetwork(capas, 0.003);
		
		
		List<Double>output;
                System.out.println("Entrenando");
		// input output y training
		nn.fit(entrada, salida, 50000);
		
                //verificar
               
                //validar
                System.out.println("Validando");
		for(double d[]:entradatest)
                    
		{   
                    
                    //nn.pesos contiene una lista de matrices de pesos finales. 
                    //Ej: Para acceder a la primera matriz -> nn.pesos.get(0).data
                    //nn.bias contiene una lista de matrices de bias finales. 
                    //Ej: Para acceder a la primera matriz -> nn.bias.get(0).data
                    
			output = nn.predict(d);
			System.out.println(output.toString());
		}		

       

    }
}