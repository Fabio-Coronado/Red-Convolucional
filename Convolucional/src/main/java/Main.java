

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;




public class Main {

    public double[][][] trainData;
    public double[][][] trainTest;
    public static ListaImagenes lista = new ListaImagenes();
    public static convolutional_utils utils = new convolutional_utils();
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Main cnn = new Main();


        
        
        //generando filtros
        double[][][] filtros1 = {
                {{-1, 0, 1}, 
                {-2, 0, 2}, 
                {-1, 0, 1}},
                
                {{-1, -2, -1}, 
                {0, 0, 0}, 
                {1, 2, 1}},
             
            
            };
        
        
        double [][][][] filtros2 = utils.filtros_aleatorios(16, 3, 3, 2);


               
        //---------------------ENTRENAMIENTO---------------------------------------
        System.out.println("Procesando imagenes para entrenamiento...");
        cnn.trainData = lista.Lista(new File("src/evaluar/DB"), 128, 128);
        //cantidad, total, division y parte
        
        //parte 1
        
    
        System.out.println("Convolucionando imagenes de entrenamiento...");
       
        //agregar argumento threads.
        //4 cores
        double [][] entrada = utils.proceso_convolucion(cnn.trainData, 4,filtros1, filtros2);
        cnn.trainData = null;
        System.gc();
        MatrizSalida operacion =  new MatrizSalida();
        double salida[][] = operacion.Salida(new File("src/evaluar/DB"));   
        int dimension2 = entrada[0].length;
        int dimension3 = salida[0].length;
        System.out.println("Dimension input "+dimension2);
        System.out.println("Cantidad de datos "+entrada.length);
        System.out.println("Dimension outnput "+dimension3);
        System.out.println("Cantidad de datos "+salida.length);
        //128 56
        int capas[] = {dimension2, 128,dimension3};
        //capas, learninrate and threads
        // 0.04
        NeuralNetworkParallel nnp = new NeuralNetworkParallel(capas, 0.08, 4);
        //NeuralNetwork nnn = new NeuralNetwork(capas, 0.01);
        
        
        


        System.out.println("Entrenando...");
        // input output , epocas , entrenamiento por epocas, dropout
        nnp.fit(entrada, salida, 1, 600000 , 0);
        entrada = null;
        salida = null;
        System.gc();
        //nnn.fit(entrada, salida, 50000);
        System.out.println("Obteniendo mejor red...");
        NeuralNetwork br = best_red(nnp, filtros1, filtros2);
        //guardar objeto
            try {
              FileOutputStream r = new FileOutputStream(new File("src/Red.txt"));
              FileOutputStream f = new FileOutputStream(new File("src/filtros.txt"));
              ObjectOutputStream or = new ObjectOutputStream(r);
              ObjectOutputStream  of = new ObjectOutputStream(f);                  

              // guardar red en el archivo
              or.writeObject(br);
              // guardar filtros en el archivo
              of.writeObject(filtros1);
              of.writeObject(filtros2);
              //of.writeObject(filtros3);
              //of.writeObject(filtros4);
              //o.write(filtros);
              //guardar filtros

              or.close();
              of.close();

              //cargando




          } catch (IOException e) {
              System.out.println("Error initializing stream");
          } 

  
    

    }
    
    //retorna la red con mejor accuracy 
    public static NeuralNetwork best_red(NeuralNetworkParallel nnp, double [][][] filtros1, double [][][][] filtros2){
        
        
        
        
        String names[] = {"beans","cake","candy","cereal","chips",
                "chocolate","coffee","corn","fish",
                "flour","honey","jam","juice","milk",
                "nuts","oil","pasta","rice","soda","spices",
                "sugar","tea","tomato_sauce","vinegar","water"};
        
        int n_redes = nnp.cores;
        
        //guardar accuracy
        
        double accuracys_red[];
        accuracys_red = new double[n_redes];
        
        for (int i = 0 ; i< n_redes; i++){
            
            
            double accuracys[];
            accuracys = new double[names.length];
            for (int j = 0 ; j< names.length; j++){
                    
                    double[][][] trainTest = lista.Lista(new File("src/validar/"+names[j]), 128, 128);
                    double [][] entradatest = utils.proceso_convolucion(trainTest,4, filtros1, filtros2);


                    List<Double>output;
                    
                    double accuracy = 0;
                    for(double d[]:entradatest){   

                        //nn.pesos contiene una lista de matrices de pesos finales. 
                        //Ej: Para acceder a la primera matriz -> nn.pesos.get(0).data
                        //nn.bias contiene una lista de matrices de bias finales. 
                        //Ej: Para acceder a la primera matriz -> nn.bias.get(0).data
                            output = nnp.Redes.get(i).predict(d);
                            //System.out.println(output.toString());

                            Double dd = Collections.max(output);
                            for(int u=0; u<names.length; u++) {
                                    if(dd==output.get(u)) {
                                            
                                            if(u==j)
                                                    accuracy++;
                                    }

                            }
                    }
                    
                    double total = accuracy/entradatest.length*100.0;
                    accuracys[j] = total;
                    
            
            }
            double suma = 0; 
            for (int z = 0 ; z< names.length; z++){
                suma = suma + accuracys[z];
            }
            
            accuracys_red[i] = suma/names.length;
            
            
            
        }
        System.out.println(Arrays.toString(accuracys_red));
        //comparando los accuracys
        double mayor = accuracys_red[0];
        int indice = 0;
         for (int k = 1 ; k< n_redes; k++){
                 if(mayor < accuracys_red[k] ){
                     mayor = accuracys_red[k];
                     indice = k;
                 }
            }
        
        System.out.println("Best red is: " + indice);
        return nnp.Redes.get(indice);
    
    }
}