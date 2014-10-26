/**
 * Created by joe on 26/10/14.
 */
public class Convolution {

    public static Image convolve(Image input, double[][] kernel){
        System.out.println("Performing convolution...") ;
        Image paddedImage = padBoundary(input, kernel.length, kernel[0].length);
        return paddedImage;
    }

    private static Image padBoundary(Image input, int kernelWidth, int kernelHeight){
        Image output = new Image(input.depth, input.width + kernelWidth*2, input.height+kernelHeight*2);

        //Pad Sides
        for(int y = 0; y < input.height; y++){
            for(int i = 0; i < kernelWidth; i++){
                output.pixels[i][y+kernelHeight] = input.pixels[kernelWidth-i][y];
            }

            for(int i = kernelWidth; i < kernelWidth*2; i++){
                output.pixels[i+input.width][y+kernelHeight] = input.pixels[input.width-i+kernelWidth-1][y];
            }
        }

        //Copy Image Reminder
        for(int y = 0; y < input.height; y++){
            for(int x = 0; x < input.width; x++){
                output.pixels[x+kernelWidth][y+kernelHeight] = input.pixels[x][y];
            }
        }

        //Pad Tops
        for(int x = 0; x < output.width; x++){
            for(int i = 0; i < kernelHeight; i++){
                output.pixels[x][i] = output.pixels[x][i-1+kernelHeight];
            }

            for(int i = kernelHeight; i < kernelHeight*2; i++) {
                output.pixels[x][input.width+i] = output.pixels[x][output.height-i-1];
            }
        }



        output.WritePGM("pisser.pgm");
        try {
            Runtime.getRuntime().exec("open pisser.pgm");
        }catch(java.io.IOException e){

        }
        return output;
    }
}
