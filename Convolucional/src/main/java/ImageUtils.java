

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class ImageUtils {
        //image to array y redimensiona
	public double[][] transformImageToArray(BufferedImage bufferedImage) throws IOException {
            
            int width          =  bufferedImage.getWidth();
            int height         =  bufferedImage.getHeight();
            double[][]image = new double[height][width ];
            //escala de grises
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width ; j++) {
                    Color color = new Color(bufferedImage.getRGB(j, i));
                    image[i][j] = (color.getRed() + color.getGreen() + color.getBlue())/765.0;
                }
            }
            return image;
    }
        
        //resize
        public BufferedImage ResizeImage(BufferedImage bufferedImage, int newW, int newH) throws IOException{
        
            Image resultingImage = bufferedImage.getScaledInstance(newW, newH, Image.SCALE_DEFAULT);
            BufferedImage outputImage = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);
            outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
             //guardar si es necesario
            
            //File outputfile = new File("red"+nameFile);
            //ImageIO.write(outputImage, "jpg", outputfile);
            return outputImage;
        }
        
        
        
        
}
