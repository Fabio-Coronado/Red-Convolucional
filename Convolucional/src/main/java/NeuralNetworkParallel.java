
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;


public class NeuralNetworkParallel implements Serializable {
    private static final long serialVersionUID = 1L;
    int cores;
    List<NeuralNetwork> Redes = new ArrayList<NeuralNetwork>();
    public NeuralNetworkParallel(int[] capas, double l_rate, int cores){
        this.cores = cores;
        for (int i = 0; i < cores; i++) {
            NeuralNetwork nn = new NeuralNetwork(capas, l_rate);
            Redes.add(nn);
        }

    }
    
    public void fit(double[][] X, double[][] Y, int epochs, int trainingperepoch, double dropout) {
        
        for(int r = 0; r< epochs;r++){
        //Matrix[][] aux2;
        System.out.println("epoca "+(r+1 )+" de "+ epochs);
        //llenando los pesos- Redes iguales
        Matrix[][] aux1pesos = new Matrix[cores][Redes.get(0).pesos.length];
        Matrix[][] aux1bias = new Matrix[cores][Redes.get(0).bias.length];
        for (int i = 0; i < cores; i++){
            

            for(int j = 0; j< Redes.get(i).pesos.length; j++){
                
                aux1pesos[i][j] = new Matrix(Redes.get(i).pesos[j].rows, Redes.get(i).pesos[j].cols);
                //copiando a 
                aux1pesos[i][j].copy(Redes.get(i).pesos[j]);
                
            }
            
            for(int j = 0; j< Redes.get(i).bias.length; j++){
                aux1bias[i][j] = new Matrix(Redes.get(i).bias[j].rows, Redes.get(i).bias[j].cols);
                aux1bias[i][j].copy(Redes.get(i).bias[j]);
                
            }

        }
     

        //entrenar paralelo
        try {
            
            Thread[] Ts = new Thread[cores]; // treads creados
            
            for(int w=0;w<cores;w++)
                {
                    
                    networkworkercore wc = new networkworkercore(Redes, w, trainingperepoch, X, Y, dropout);
                    Ts[w] = new Thread(wc);
                    Ts[w].start(); // iniciando thread
                }
            for(int w=0;w<cores;w++) Ts[w].join(); // esperar que cada uno finalize
        } catch (InterruptedException ie) {System.out.println(ie); System.exit(1);}
        

        
        
        Matrix[][] aux2pesos = new Matrix[cores][Redes.get(0).pesos.length];
        Matrix[][] tendenciapesos = new Matrix[cores][Redes.get(0).pesos.length];
        Matrix[][] aux2bias = new Matrix[cores][Redes.get(0).bias.length];       
        Matrix[][] tendenciabias = new Matrix[cores][Redes.get(0).bias.length];
        for (int i = 0; i < cores; i++){
            

            for(int j = 0; j< Redes.get(i).pesos.length; j++){
                
                aux2pesos[i][j] = new Matrix(Redes.get(i).pesos[j].rows, Redes.get(i).pesos[j].cols);
                tendenciapesos[i][j] = new Matrix(Redes.get(i).pesos[j].rows, Redes.get(i).pesos[j].cols);
                //copiando a 
                aux2pesos[i][j].copy(Redes.get(i).pesos[j]);
                tendenciapesos[i][j].copy(Redes.get(i).pesos[j]);
                
            }
            
            for(int j = 0; j< Redes.get(i).bias.length; j++){
                aux2bias[i][j] = new Matrix(Redes.get(i).bias[j].rows, Redes.get(i).bias[j].cols);
                tendenciabias[i][j] = new Matrix(Redes.get(i).bias[j].rows, Redes.get(i).bias[j].cols);
                aux2bias[i][j].copy(Redes.get(i).bias[j]);
                tendenciabias[i][j].copy(Redes.get(i).bias[j]);
                
            }

        }
     
        
    

        
                for (int j = 0; j < aux2pesos[0].length; j++){
                    //recorre matrices
                    for (int k = 0; k < aux2pesos[0][j].rows; k++){
                        for (int l = 0; l < aux2pesos[0][j].cols; l++){
                            //double valor = aux1pesos[0][j].data[k][l];
                             //System.out.println("inicio recorrido");
                            for (int i = 0; i < cores; i++){
                                
                            double valor = aux2pesos[i][j].data[k][l] - aux1pesos[i][j].data[k][l];
                            tendenciapesos[i][j].data[k][l] = valor;

                            }
   
                        }
                    }
                }
        
        
    
        
            //comparaciones
            //double [] valores =new double[cores]; 
            
                //recorre capas
                for (int j = 0; j < aux2bias[0].length; j++){
                    //recorre matrices
                    for (int k = 0; k < aux2bias[0][j].rows; k++){
                        for (int l = 0; l < aux2bias[0][j].cols; l++){   
                            for (int i = 0; i < cores; i++){
                            tendenciabias[i][j].data[k][l] = aux2bias[i][j].data[k][l] - aux1bias[i][j].data[k][l];
                            //double val = tendenciabias.get(i).get(j).data[k][l]; 
                            //System.out.println(val);
                            }
                        }
                    }
                }
        
        
        //recorrer las tendencias y verificarlas
        
        //pesos
            //comparaciones
            //double [] valores =new double[cores]; 
            //primera posicion
            int i = 0;
                //recorre capas
                for (int j = 0; j < tendenciapesos[i].length; j++){
                    //recorre matrices
                    for (int k = 0; k < tendenciapesos[i][j].rows; k++){
                        for (int l = 0; l < tendenciapesos[i][j].cols; l++){
                            //primer elemento
                            //double valor = tendenciabias.get(i).get(j).data[k][l];
                            //int positivos = 1;
                            //todos positivos
                            double [] valorestendencia = new double[cores]; 
                            double [] valorestraining = new double[cores];
                            for(int m = 0; m < tendenciapesos.length; m ++){
                                valorestendencia[m] = tendenciapesos[m][j].data[k][l];
                            }
                            
                            for(int m = 0; m < tendenciapesos.length; m ++){
                                valorestraining[m] = aux2pesos[m][j].data[k][l];
                            }
                            
                            //System.out.println("Ingresados");
                            //System.out.println(Arrays.toString(valores));
                            double [] valores2 = valores_retornados(valorestendencia, valorestraining);
                            //System.out.println("Retornados");
                            //System.out.println(Arrays.toString(valores2));
                            //System.out.println(Arrays.toString(valores2));
                            for(int m = 0; m < cores; m ++){
                                //asignando nuevos valores
                                Redes.get(m).pesos[j].data[k][l] = valores2[m];
                            }
                            
                            //asignar a nuevos pesos
                            
                            
                        }
                    }
                }
        
                //bias
                
                i = 0;
                //recorre capas
                for (int j = 0; j < tendenciabias[i].length; j++){
                    //recorre matrices
                    for (int k = 0; k < tendenciabias[i][j].rows; k++){
                        for (int l = 0; l < tendenciabias[i][j].cols; l++){
                            
                            double [] valorestendencia = new double[cores]; 
                            double [] valorestraining = new double[cores]; 
                            for(int m = 0; m < tendenciabias.length; m ++){
                                valorestendencia[m] = tendenciabias[m][j].data[k][l];
                            }
                            for(int m = 0; m < tendenciabias.length; m ++){
                                valorestraining[m] = aux2bias[m][j].data[k][l];
                            }
                            
                            double [] valores2 = valores_retornados(valorestendencia, valorestraining);
                            for(int m = 0; m < cores; m ++){
                                //asignando nuevos valores?
                                Redes.get(m).bias[j].data[k][l] = valores2[m];
                                
                            }
                            
                            
                            
                            
                        }
                    }
                }
        
        
        }
    
    }
    
    public List<Double> predict(double[] X){
            //validar con la primera red
            return Redes.get(cores-1).predict(X);

        
    
    }
    

    
    static double randomDouble(double min, double max) {
    if (min >= max) {
        return min;
    }
       // Random r = new Random();
        return Math.random() * (max - min) + min;
    }
    
    //verifica la tendencia y retorna valores adeacuados
    static double [] valores_retornados(double [] valorestendencia, double [] valorestraining) {
        

        double valor = valorestendencia[0];
        double [] valores2 = new double[valorestendencia.length]; 
        if(valor>0){
            for(int i = 1; i< valorestendencia.length; i++ ){
                if(valorestendencia[i]<0){
                    double minimo_valor = minimo(valorestraining);
                    double promedio = prom(valorestraining);
                    for(int j = 0; j< valorestraining.length; j++ ){
                        valores2[j] = randomDouble(minimo_valor,promedio);
                    }

                    return valores2;
                }
                
                if(valorestendencia[i]==0){
                    for(int j = 0; j< valorestraining.length; j++ ){
                        valores2[j] = valorestraining[i];
                    }
                    return valores2;
                }
                
                if(i == valorestendencia.length-1){
                    double maximo_valor = maximo(valorestraining);
                    for(int j = 0; j< valorestendencia.length; j++ ){
                        valores2[j] = maximo_valor+ 0.01*(double)j*maximo_valor;
                    }

                    return valores2;

                }
                
            }
        
        }else if(valor<0){
            
            for(int i = 1; i< valorestendencia.length; i++ ){
                if(valorestendencia[i]>0){
                    double minimo_valor = minimo(valorestraining);
                    double promedio = prom(valorestraining);
                    for(int j = 0; j< valorestendencia.length; j++ ){
                        valores2[j] = randomDouble(minimo_valor,promedio);
                    }
                      return valores2;

                }
                 
                if(valorestendencia[i]==0){
                    for(int j = 0; j< valorestraining.length; j++ ){
                        valores2[j] = valorestraining[i];
                    }
                    return valores2;
                }

                if(i == valorestendencia.length-1){
                    double minimo_valor = minimo(valorestraining);
                    //todos minimo
                    for(int j = 0; j< valorestendencia.length; j++ ){
                        valores2[j] = minimo_valor - 0.01*(double)j*minimo_valor ;
                    }

                    return valores2;

                }
                
        }
        
        } 
        
        for(int j = 0; j< valorestraining.length; j++ ){
                        valores2[j] = valorestraining[0];
        }
        
        /*
        else if (valor == 0){
            
            return valorestraining;
        }*/
        return valores2;
        
    }
    
    static double maximo(double [] valores) {
        double mayor = valores[0];
        for(int i = 1; i< valores.length; i++ ){
            if(valores[i]>mayor ){
                mayor = valores[i];

            }
        }
             
        return mayor;
    }
    
    static double minimo(double [] valores) {
        double menor = valores[0];
             for(int i = 1; i< valores.length; i++ ){
                 if(valores[i] < menor ){
                     menor = valores[i];
                     
                 }
             }
             
        return menor;
    }
    
    static double prom(double [] valores) {
        double suma = 0;
             for(int i = 0; i< valores.length; i++ ){
                 suma = valores[i] + suma;
             }
             
        return suma/(double)valores.length;
    }

    
}

class networkworkercore implements Runnable
{
   List<NeuralNetwork> Redes;  // pointer to external matrices, AXB = R
    double dropout;
    int n;
    int trainingperepoch;
    double[][] X;
    double[][] Y;
    public networkworkercore(List<NeuralNetwork> Redes, int n, int trainingperepoch, double[][] X, double[][] Y, double dropout){
        this.n = n;
        this.Redes = Redes;
        this.trainingperepoch = trainingperepoch;
        this.X = X;
        this.Y = Y;
        this.dropout = dropout;
    }

    @Override
    public void run()
    {
        
	Redes.get(n).fit(X, Y, trainingperepoch, dropout);
         
    }
}




