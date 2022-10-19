import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class NeuralNetwork implements Serializable {
    
    private static final long serialVersionUID = 1L;
    Matrix[] pesos;
    Matrix[] bias;

    double l_rate;
    private final int[] capas;

    public NeuralNetwork(int[] capas, double l_rate) {
        pesos = new Matrix[capas.length-1];
        bias = new Matrix[capas.length-1];
        this.capas = capas;
        this.l_rate = l_rate;
        for (int i = 0; i < capas.length - 1; i++) {
            //bias y pesos
            pesos[i] = new Matrix(capas[i + 1], capas[i]);
            bias[i] = new Matrix(capas[i + 1], 1);

        }

    }

    public List<Double> predict(double[] X) {
        Matrix aux = Matrix.fromArray(X);
        for (int i = 0; i < this.capas.length - 1; i++) {
            
            //System.out.println((i+1)+" ");
            Matrix aux1 = Matrix.multiply(pesos[i], aux);
            aux1.add(bias[i]);
            aux1.sigmoid();
            aux = aux1;

        }
        return aux.toArray();

    }

    public void fit(double[][] X, double[][] Y, int epochs, double dropout) {
        for (int i = 0; i < epochs; i++) {
            System.out.println("...entrenamiento "+(i+1 )+" de "+ epochs);
            int sampleN = (int) (Math.random() * X.length);
            this.train(X[sampleN], Y[sampleN], dropout);
        }
    }

    public void train(double[] X, double[] Y, double dropout) {
        
        Matrix aux = Matrix.fromArray(X);
        ArrayList<Matrix> conections = new ArrayList<Matrix>();
        conections.add(aux);
        
        if (dropout != 0){
        //save pesos and bias
        Matrix[] pesosbefore = new Matrix[capas.length-1];
        Matrix[] biasbefore = new Matrix[capas.length-1];
        Matrix[] valorespesos = new Matrix[capas.length-1];
        Matrix[] valoresbias = new Matrix[capas.length-1];
        
            for (int i = 0; i < this.capas.length - 1; i++){

                pesosbefore[i] = new Matrix(pesos[i].rows, pesos[i].cols);
                biasbefore[i] = new Matrix(bias[i].rows, bias[i].cols);
                valorespesos[i] = new Matrix(pesos[i].rows, pesos[i].cols);
                valoresbias[i] = new Matrix(bias[i].rows, bias[i].cols);
                pesosbefore[i].copy(pesos[i]);
                biasbefore[i].copy(bias[i]);


            }
        }
            //crear matriz 1 y 0 donde 1 significa que cambia y 0 que no cambia 
            //dropout
            //ArrayList<Matrix []> valores = values(valorespesos, valoresbias, dropout);
           

            //forward

            for (int i = 0; i < this.capas.length - 1; i++) {

                Matrix aux1 = Matrix.multiply(pesos[i], aux);
                aux1.add(bias[i]);
                aux1.sigmoid();
                //conecciones
                conections.add(aux1);
                aux = aux1;

            }
        
        //backpropagation
        Matrix target = Matrix.fromArray(Y);
        Matrix aux_error = Matrix.subtract(target, conections.get(capas.length - 1));

        for (int i = capas.length - 2; i >= 0; i--) {

            if (i == capas.length - 2) {

                Matrix aux_gradient = conections.get(i + 1).dsigmoid();
                aux_gradient.multiply(aux_error);
                aux_gradient.multiply(l_rate);
             
                Matrix aux_T = Matrix.transpose(conections.get(i));
                Matrix aux_delta = Matrix.multiply(aux_gradient, aux_T);
                
                //actualizando pesos
                pesos[i].add(aux_delta);
                bias[i].add(aux_gradient);
            
            } else {

                Matrix aux1_T = Matrix.transpose(pesos[i + 1]);
                Matrix aux1_error = Matrix.multiply(aux1_T, aux_error);

                Matrix aux1_gradient = conections.get(i + 1).dsigmoid();
                aux1_gradient.multiply(aux1_error);
                aux1_gradient.multiply(l_rate);

                Matrix aux2_T = Matrix.transpose(conections.get(i));
                Matrix aux1_delta = Matrix.multiply(aux1_gradient, aux2_T);
                
                //actualizando pesos
                pesos[i].add(aux1_delta);
                bias[i].add(aux1_gradient);
           
                aux_error = aux1_error;
             

            }
            //Matrix 

        }
        
        //no actualizar el dropout %
        
        //pesos
        /*
        if (dropout != 0 ){
            for(int i=0;i<valores.get(0).length;i++)
               {
                       for(int j=0;j<valores.get(0)[i].data.length;j++)
                       {
                               for(int k=0;k<valores.get(0)[i].data[0].length;k++)
                               {
                                   if (valores.get(0)[i].data[j][k] == 0)
                                       pesos[i].data[j][k]= pesosbefore[i].data[j][k];

                               }
                       }
               }

            //bias

            for(int i=0;i<valores.get(1).length;i++)
               {
                       for(int j=0;j<valores.get(1)[i].data.length;j++)
                       {
                               for(int k=0;k<valores.get(1)[i].data[0].length;k++)
                               {
                                   if (valores.get(1)[i].data[j][k] == 0)
                                       bias[i].data[j][k]= biasbefore[i].data[j][k];

                               }
                       }
               }

        }*/
    }
    
    public ArrayList<Matrix []> values( Matrix [] valores, Matrix [] valores2, double porcentaje){
            ArrayList<Matrix []> lista = new ArrayList<Matrix[]>();;
           for(int i=0;i<valores.length;i++)
           {
                   for(int j=0;j<valores[i].data.length;j++)
                   {
                           for(int k=0;k<valores[i].data[0].length;k++)
                           {
                               double random_value = Math.random();
                               if (random_value < porcentaje)
                                   valores[i].data[j][k]=0;
                               else{
                                   valores[i].data[j][k]=1;
                               }
                           }
                   }
           }
           for(int i=0;i<valores2.length;i++)
           {
                   for(int j=0;j<valores2[i].data.length;j++)
                   {
                           for(int k=0;k<valores2[i].data[0].length;k++)
                           {
                               double random_value = Math.random();
                               if (random_value < porcentaje)
                                   valores2[i].data[j][k]=0;
                               else{
                                   valores2[i].data[j][k]=1;
                               }
                           }
                   }
           }
           lista.add(valores);
           lista.add(valores2);
           return lista;


    }
    
    
  
 
}