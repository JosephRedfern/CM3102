/**
 * Created by joe on 26/10/14.
 */
public class Convolution {

    public static Image convolve(Image input, double[][] kernel){
        System.out.println("Performing convolution...") ;
        Image paddedImage = padBoundary(input, kernel.length, kernel[0].length);
        Image convolvedImage = new Image(input.depth, input.width, input.height);
        for(int y = 0; y < input.height; y++){
            for(int x = 0; x< input.width; x++){
                double convolvedValue = 0;
                int yMin = kernel.length - (int)Math.ceil(kernel.length / 2);
                int xMin = kernel[0].length - (int)Math.ceil(kernel[0].length/2);

                for(int kY = 0; kY<kernel.length; kY++){
                    for(int kX=0; kX<kernel[kY].length; kX++){
//                        System.out.printf("Convolving (%s,%s) with kernel value at (%s,%s), midpoint set to (%s, %s)%n", x, y, kX, kY, xMin, yMin);
                        convolvedValue += paddedImage.pixels[x + kX - xMin + kernel[0].length][y + kY - yMin + kernel.length] * kernel[kY][kX];
                    }
                }

                convolvedImage.pixels[x][y] = (int)convolvedValue;
            }
        }

        return convolvedImage;
    }



    public static Image padBoundary(Image input, int kernelHeight, int kernelWidth){
        Image output = new Image(input.depth, input.width + kernelWidth*2, input.height+kernelHeight*2);


        //Pad upper, lower
        for(int x = 0; x < input.width; x++){
            for(int i = 0; i < kernelHeight; i++){
                output.pixels[x+kernelWidth][i] = input.pixels[x][kernelHeight-i-1]; //shift by kernelWidth to sort alignment
            }

            for(int i = 0; i < kernelHeight; i++){
                output.pixels[x+kernelWidth][kernelHeight+input.height+i] = input.pixels[x][input.height-i-1];
            }

        }

        //Copy Image Reminder
        for(int y = 0; y < input.height; y++){
            for(int x = 0; x < input.width; x++){
                output.pixels[x+kernelWidth][y+kernelHeight] = input.pixels[x][y];
            }
        }

        for(int y = 0; y < output.height; y++){
            for(int i = 0; i < kernelHeight; i++){
                output.pixels[i][y] = output.pixels[2*kernelHeight-i][y]; //shift by kernelWidth to sort alignment
            }

            for(int i = 0; i < kernelHeight; i++){
                output.pixels[output.width-i-1][y] = output.pixels[output.width-2*kernelHeight+i][y]; //shift by kernelWidth to sort alignment
            }
        }

        output.WritePGM("reflected.pgm");
//        try {
//            Runtime.getRuntime().exec("open padded.pgm");
//        }catch(java.io.IOException e){
//
//        }
        return output;
    }
}
