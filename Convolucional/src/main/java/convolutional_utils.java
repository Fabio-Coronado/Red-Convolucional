

public class convolutional_utils {
    //unit16 igual a int ?
    public  double [][] conv_(double[][] matriz_imagen, double[][] filtro ){
        int tam_filtro = filtro[0].length;
        double[][] resultado = new double[matriz_imagen.length][matriz_imagen[0].length];
        
        for(int i = tam_filtro/2; i< (int)(matriz_imagen.length - tam_filtro/2.0) + 1; i++){
            for (int j = tam_filtro/2; j< (int)(matriz_imagen[0].length/1.0 - tam_filtro/2.0) + 1; j++){


                double[][] region_actual = new double[(int)Math.ceil(tam_filtro/2.0) + (int)Math.floor(tam_filtro/2.0)][(int)Math.ceil(tam_filtro/2.0) + (int)Math.floor(tam_filtro/2.0)];
                
                int r1 =0, s1;
                
                for(int r = i - (int)Math.floor(tam_filtro/2.0); r < i + (int)Math.ceil(tam_filtro/2.0); r++){
                        s1=0;
                        for (int s = j - (int)Math.floor(tam_filtro/2.0); s < j + (int)Math.ceil(tam_filtro/2.0); s++){
                            
               
                            region_actual[r1][s1] = matriz_imagen[r][s];
                            s1++;
                        }
                        r1++;
                    }

                    
                   double[][] resultado_actual = new double[region_actual.length][region_actual[0].length];
                   //Element-wise multiplication
                   double conv_suma = 0;
                   for (int m = 0; m < region_actual.length; m++){
                       for(int n = 0; n < region_actual[0].length; n++ ){
                           
                           resultado_actual[m][n] = region_actual[m][n] * filtro[m][n];
                           conv_suma = conv_suma + resultado_actual[m][n];
                    }
                   }
                   resultado[i][j] = conv_suma;                   
            }
        }
        
        double[][] resultado_final = new double[resultado.length - 2*(int)(tam_filtro/2.0)][resultado[0].length - 2*(int)(tam_filtro/2.0)];
        
                  
        int u1 =0, v1;
        int u = 0;
        int v = 0;
        
        for (u = (int)(tam_filtro/2.0); u < resultado.length - (int)(tam_filtro/2.0); u++){
            v1=0;
                       for(v = (int)(tam_filtro/2.0); v < resultado[0].length - (int)(tam_filtro/2.0); v++ ){
                           //
                           resultado_final[u1][v1] = resultado[u][v];
                           v1++;
                       }
                       u1++;
        } 

        return resultado_final;
                       
    }
    
    
    public double [][][] conv(double[][] matriz_imagen, double[][][] filtros ){
        //img dimension debe tener 1 dimension menos que el de filtros
         double[][][] mapa_caracteristicas =  new double[matriz_imagen.length - filtros[0].length + 1]
                 [matriz_imagen[0].length - filtros[0].length + 1][filtros.length];
         
         for(int i = 0; i < filtros.length; i++){
             double[][] filtro_actual = new double[filtros[0].length][filtros[0][0].length];
            for(int j = 0; j < filtros[0].length; j++){
                for(int k = 0; k < filtros[0][0].length; k++){
                    filtro_actual[j][k] = filtros[i][j][k];
                    
                }
            }
            
            //if(getNumDimensions())
            
            double [][] mapa_convolucional = conv_(matriz_imagen, filtro_actual);
            
            //System.out.println(mapa_convolucional[60][155]);
          //verificado
          
            for(int j1 = 0; j1 < mapa_convolucional.length; j1++){
                for(int k1 = 0; k1 < mapa_convolucional[0].length; k1++){
                    //System.out.println(
                    mapa_caracteristicas[j1][k1][i] = mapa_convolucional[j1][k1];
                }
            }
   
         }
         
         
         return mapa_caracteristicas; 
    }
    
    
    public double [][][] conv2(double[][][] imagenes, double[][][][] filtros ){
        //img dimension debe tener 1 dimension menos que el de filtros
         double[][][] mapa_caracteristicas =  new double[imagenes.length - filtros[0].length + 1]
                 [imagenes[0].length - filtros[0].length + 1][filtros.length];
         
         for(int i = 0; i < filtros.length; i++){         
            double[][][] filtro_actual = new double[filtros[0].length][filtros[0][0].length][filtros[0][0][0].length];
            for(int j = 0; j < filtros[0].length; j++){           
                for(int k = 0; k < filtros[0][0].length; k++){
                        for(int l = 0; l < filtros[0][0][0].length; l++){
                        filtro_actual[j][k][l] = filtros[i][j][k][l];

                    }
                    
                }
            }
            
            //posibles print de filtro actual
            //llenamos imagen
                double [][] p_imagenes = new double[imagenes.length][imagenes[0].length];
                for( int p = 0; p< imagenes.length; p ++){
                    for( int p1 = 0; p1< imagenes[0].length; p1 ++){
                        p_imagenes[p][p1] = imagenes[p][p1][0];
                    }
                }
            //llenamos filtro
             double [][] p_filtro = new double[filtro_actual.length][filtro_actual[0].length];
                for( int p2 = 0; p2< filtro_actual.length; p2 ++){
                    for( int p3 = 0; p3< filtro_actual[0].length; p3 ++){
                        p_filtro[p2][p3] = filtro_actual[p2][p3][0];
                    }
                }
            
                //inicializando mapa convolucional
            double [][] mapa_convolucional = conv_(p_imagenes, p_filtro);
            
            for(int numero_ch = 1; numero_ch < filtro_actual[0][0].length; numero_ch++ ){
                
                    //llenamos imagen
                        for( int p = 0; p< imagenes.length; p ++){
                            for( int p1 = 0; p1< imagenes[0].length; p1 ++){
                                p_imagenes[p][p1] = imagenes[p][p1][numero_ch];
                            }
                        }
                    //llenamos filtro

                        for( int p2 = 0; p2< filtro_actual.length; p2 ++){
                            for( int p3 = 0; p3< filtro_actual[0].length; p3 ++){
                                p_filtro[p2][p3] = filtro_actual[p2][p3][numero_ch];
                            }
                        }
                
                    //recalculando mapa convolucional
                    //paralelizar
                    double [][] r_mapa_convolucional = conv_(p_imagenes, p_filtro);
                    
                    
                        
                    for(int i_mp = 0; i_mp < mapa_convolucional.length; i_mp++ ){

                       for(int j_mp = 0; j_mp < mapa_convolucional[0].length; j_mp++ ){

                        mapa_convolucional[i_mp][j_mp] =  mapa_convolucional[i_mp][j_mp] + r_mapa_convolucional[i_mp][j_mp];
                       }
                    }
            }
                

          //verificado
          
            for(int j1 = 0; j1 < mapa_convolucional.length; j1++){
                for(int k1 = 0; k1 < mapa_convolucional[0].length; k1++){
                    //System.out.println(
                    mapa_caracteristicas[j1][k1][i] = mapa_convolucional[j1][k1];
                }
            }
   
         }
         
         
         return mapa_caracteristicas; 
    }
    

    public double [][][] pool(double [][][] mapa_caracteristicas, int size, int stride){
        //cambiarlo si vamos rgb
        double [][][] salida_pool = new double[(int)((mapa_caracteristicas.length - size +1)/stride + 1) ]
                [(int)((mapa_caracteristicas[0].length - size +1)/stride + 1)][mapa_caracteristicas[0][0].length];
        for(int i = 0; i < mapa_caracteristicas[0][0].length; i++){
            int r2 = 0;
            
            for(int j = 0; j < mapa_caracteristicas.length - size + 1; j = j + stride){
                int c2 = 0;
                
                for(int k = 0; k < mapa_caracteristicas[0].length - size + 1; k = k + stride){
                    //llenar la matriz
                    double [][] mapa_parte = new double[size][size];
                    int r3 = 0;
                    for(int j1 = j; j1 < j + size; j1++){
                        int c3 = 0;
                        for(int k1 = k; k1 < k + size; k1++){
                            mapa_parte[r3][c3] = mapa_caracteristicas[j1][k1][i];
                             
                            c3++;
                        }
                        r3++;
                    }
                    
                    
                    salida_pool[r2][c2][i] = maximo(mapa_parte);
                    
                    c2++;
            }
                r2++;
               
            }
        }
        return salida_pool;
    }
    
    public double maximo(double [][] matriz){
         double maxValue = matriz[0][0];
        for (int j = 0; j < matriz.length; j++) {
            for (int i = 0; i < matriz[j].length; i++) {
                if (matriz[j][i] > maxValue) {
                    maxValue = matriz[j][i];
                }
            }
        }
        return maxValue;
    }
    
    
    public double [][][] relu(double [][][] mapa_caracteristicas){
        double [][][] salida_relu = new double[mapa_caracteristicas.length]
                [mapa_caracteristicas[0].length][mapa_caracteristicas[0][0].length];
        
        for (int i = 0; i < mapa_caracteristicas[0][0].length; i++) {
            for (int j = 0; j < mapa_caracteristicas.length; j++) {
                for (int k = 0; k < mapa_caracteristicas[0].length; k++) {
                    salida_relu [j][k][i] = relu_function(mapa_caracteristicas[j][k][i]);
                
                }
            }
        }
        return salida_relu;
    }
    
    public double relu_function(double a){
        double valor = 0;
        if(a > valor){
            valor = a;
        }
        return valor;
    }
    
    
    public double [][][][] filtros_aleatorios(int a, int b, int c, int d){
         double [][][][] filtros = new double[a][b][c][d];      
         for (int i = 0 ; i< a ;i++){
              for (int j = 0 ; j< b ;j++){
                  for (int k = 0 ; k< c ;k++){
                        for (int l = 0 ; l< d ;l++){
                            filtros[i][j][k][l] = Math.random() *2 -1;
                  }         
                }         
             }        
        }
         return filtros;
    }
    
    public double [][][] filtros_aleatorios3(int a, int b, int c){
         double [][][] filtros = new double[a][b][c];      
         for (int i = 0 ; i< a ;i++){
              for (int j = 0 ; j< b ;j++){
                  for (int k = 0 ; k< c ;k++){
                        
                            filtros[i][j][k] = Math.random() *2 -1;
                          
            }         
             }        
        }
         return filtros;
    }
    
    
    
    //aplana matriz de tres dimensiones
    public double[] aplanar(double [][][] matriz){
        double [] matriz_aplanada = new double[matriz.length * matriz[0].length * matriz[0][0].length];
        int l = 0;
        for (int i = 0 ; i< matriz.length ;i++){
              for (int j = 0 ; j< matriz[0].length ;j++){
                  for (int k = 0 ; k< matriz[0][0].length ;k++){
                          matriz_aplanada[l] = matriz[i][j][k]/100.0;
                          l++;
                }         
            }        
        }
        return matriz_aplanada;
    }
    
    
    //dividir el proceso en N partes
    
    public double [][] proceso_convolucion(double [][][] imagenes, int cores, double[][][] filtros, double [][][][] filtros2){
        
        double [][] ingreso_entrenamiento = new double[imagenes.length][];
        
        
        //double [][] auxiliar = new double[imagenes[0].length][imagenes[0][0].length];
            int N = imagenes.length;
            System.out.println(N);
            try {
            
            Thread[] Ts = new Thread[cores]; // treads creados
            int Rs = N/cores; 
            for(int w=0;w<cores;w++)
                {
                    int Ra = Rs*(w+1)
                    ; // rows assigned 
                    if (w==cores-1) // ultimo thread hasta el final
                     Ra = imagenes.length;
                    
                    workercore wc = new workercore(imagenes,filtros,filtros2,ingreso_entrenamiento,Rs*w,Ra);
                    Ts[w] = new Thread(wc);
                    Ts[w].start(); // iniciando thread
                }
            for(int w=0;w<cores;w++) Ts[w].join(); // esperarar que cada uno finalize
        } catch (InterruptedException ie) {System.out.println(ie); System.exit(1);}
        
        
                
        return ingreso_entrenamiento;
        
    }
    
   
    
    
 }

class workercore implements Runnable
{
    double [][][] imagenes;  // pointer to external matrices, AXB = R
    double [][][] filtros;
    double [][] ingreso_entrenamiento;
    double[][][][] filtros2;
    double[][][][] filtros3;
    double[][][][] filtros4;
    convolutional_utils utils3 = new convolutional_utils();
    int start, N; // starting row and number of rows.
    double [][] auxiliar;
    public workercore(double [][][] imagenes,double[][][] filtros  ,double[][][][] filtros2, double [][] ingreso_entrenamiento ,int s, int n){
        this.imagenes = imagenes; 
        this.filtros = filtros; 
        this.filtros2 = filtros2;        
        //this.filtros3 = filtros3;
        //this.filtros4 = filtros4;
        this.ingreso_entrenamiento = ingreso_entrenamiento;
        start=s; 
        N=n;
        auxiliar = new double[imagenes[0].length][imagenes[0][0].length];
        
    }

    @Override
    public void run()
    {
	
         for (int i = start ; i< N ;i++){
            
            //llenando imagenes
              for (int j = 0 ; j< imagenes[0].length ;j++){
                  for (int k = 0 ; k< imagenes[0][0].length ;k++){
                          auxiliar[j][k] = imagenes[i][j][k];
                          
                }         
            }
              
              
                double [][][] array;
                
                array = utils3.conv(auxiliar, filtros);
                //System.out.println(Arrays.deepToString(array));
                double [][][] relu1;
                relu1 = utils3.relu(array);
                double [][][] pool;
                pool = utils3.pool(relu1, 2, 2);

                
                double [][][] array2;
                //System.out.println(pool[0][0].length);
                array2 = utils3.conv2(pool, filtros2);           
                double [][][] relu2;
                relu2 = utils3.relu(array2);
                double [][][] pool2;
                pool2 = utils3.pool(relu2, 2, 2);
                
                /*
                double [][][] array3;
                //System.out.println(pool2[0][0].length);
                array3 = utils3.conv2(pool2, filtros3);
                double [][][] relu3;
                relu3 = utils3.relu(array3);
                double [][][] pool3;
                pool3 = utils3.pool(relu3, 2, 2);
                */
                /*
                double [][][] array4;
                //System.out.println(pool3[0][0].length);
                array4 = utils3.conv2(pool3, filtros4);
                double [][][] relu4;
                relu4 = utils3.relu(array4);
                double [][][] pool4;
                pool4 = utils3.pool(relu4, 2, 2);
                */
                            
                double [] aplanada =  utils3.aplanar(pool2);
                ingreso_entrenamiento[i] = aplanada;
                //System.out.println(aplanada.length);
                
        }
                
    }
}//workercore

