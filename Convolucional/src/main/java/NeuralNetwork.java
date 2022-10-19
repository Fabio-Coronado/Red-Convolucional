import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {

    ArrayList<Matrix> pesos = new ArrayList<Matrix>();
    ArrayList<Matrix> bias = new ArrayList<Matrix>();

    double l_rate;
    private final int[] capas;

    public NeuralNetwork(int[] capas, double l_rate) {
        this.capas = capas;
        this.l_rate = l_rate;
        for (int i = 0; i < capas.length - 1; i++) {
            //bias y pesos
            pesos.add(new Matrix(capas[i + 1], capas[i]));
            bias.add(new Matrix(capas[i + 1], 1));

        }

    }

    public List<Double> predict(double[] X) {
        Matrix aux = Matrix.fromArray(X);
        for (int i = 0; i < this.capas.length - 1; i++) {
            

            Matrix aux1 = Matrix.multiply(this.pesos.get(i), aux);
            aux1.add(this.bias.get(i));
            aux1.sigmoid();
            aux = aux1;

        }
        return aux.toArray();

    }

    public void fit(double[][] X, double[][] Y, int epochs) {
        for (int i = 0; i < epochs; i++) {
            int sampleN = (int) (Math.random() * X.length);
            this.train(X[sampleN], Y[sampleN]);
        }
    }

    public void train(double[] X, double[] Y) {

        Matrix aux = Matrix.fromArray(X);
        ArrayList<Matrix> conections = new ArrayList<Matrix>();
        conections.add(aux);
        //forward

        for (int i = 0; i < this.capas.length - 1; i++) {
            
            Matrix aux1 = Matrix.multiply(this.pesos.get(i), aux);
            aux1.add(this.bias.get(i));
            aux1.sigmoid();
            //conecciones
            conections.add(aux1);
            aux = aux1;

        }

        //backpropagation
        Matrix target = Matrix.fromArray(Y);
        Matrix aux_error = Matrix.subtract(target, conections.get(this.capas.length - 1));

        for (int i = this.capas.length - 2; i >= 0; i--) {

            if (i == this.capas.length - 2) {

                Matrix aux_gradient = conections.get(i + 1).dsigmoid();
                aux_gradient.multiply(aux_error);
                aux_gradient.multiply(this.l_rate);
             
                Matrix aux_T = Matrix.transpose(conections.get(i));
                Matrix aux_delta = Matrix.multiply(aux_gradient, aux_T);
                
                //actualizando pesos
                this.pesos.get(i).add(aux_delta);
                this.bias.get(i).add(aux_gradient);
            
            } else {

                Matrix aux1_T = Matrix.transpose(pesos.get(i + 1));;
                Matrix aux1_error = Matrix.multiply(aux1_T, aux_error);

                Matrix aux1_gradient = conections.get(i + 1).dsigmoid();
                aux1_gradient.multiply(aux1_error);
                aux1_gradient.multiply(this.l_rate);

                Matrix aux2_T = Matrix.transpose(conections.get(i));
                Matrix aux1_delta = Matrix.multiply(aux1_gradient, aux2_T);
                
                //actualizando pesos
                this.pesos.get(i).add(aux1_delta);
                this.bias.get(i).add(aux1_gradient);
           
                aux_error = aux1_error;
             

            }
            //Matrix 

        }

    }
    
  
 
}