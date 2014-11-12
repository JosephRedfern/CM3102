/**
 * Created by joe on 26/10/14.
 */
public class Convolution {

    public static Image convolve(Image input, double[][] kernel, boolean savePadded){
        System.out.println("Performing convolution...");

        Image paddedImage;

        if(!savePadded) {
            paddedImage = padBoundary(input, kernel.length, kernel[0].length);
        }else{
            paddedImage = padBoundary(input, kernel.length, kernel[0].length, "reflected.pgm");
        }
        Image convolvedImage = new Image(input.depth, input.width, input.height);

        for(int y = 0; y < input.height; y++){
            for(int x = 0; x< input.width; x++){
                double convolvedValue = 0;

                int yMin = kernel.length - (int)Math.ceil(kernel.length / 2);
                int xMin = kernel[0].length - (int)Math.ceil(kernel[0].length/2);

                for(int kY = 0; kY<kernel.length; kY++){
                    for(int kX=0; kX<kernel[0].length; kX++){
                        convolvedValue += paddedImage.pixels[x - kX + (kernel[0].length - 1)][y - kY + (kernel.length - 1)] * kernel[kY][kX]; //
                    }
                }

//                System.out.println(convolvedValue);

                convolvedImage.pixels[x][y] = (int)convolvedValue;
            }
        }

        return convolvedImage;
    }

    public static Image convolve(Image input, double[][] kernel){
        return convolve(input, kernel, false);
    }

    public static Image padBoundary(Image input, int kernelHeight, int kernelWidth){
        Image output = new Image(input.depth, input.width + kernelWidth - 1, input.height + kernelHeight - 1);

        //Pad upper, lower
        for(int x = 0; x < input.width; x++){
            for(int i = 0; i < (kernelHeight-1)/2; i++){
                output.pixels[x+(kernelWidth-1)/2][i] = input.pixels[x][(kernelHeight-1)/2-i]; //shift by kernelWidth to sort alignment
            }

            for(int i = 0; i < (kernelHeight-1)/2; i++){
                output.pixels[x+(kernelWidth-1)/2][(kernelHeight-1)/2+input.height+i] = input.pixels[x][input.height-i-1];
            }

        }

        //Copy Image Reminder
        for(int y = 0; y < input.height; y++){
            for(int x = 0; x < input.width; x++){
                output.pixels[x+(kernelWidth-1)/2][y+(kernelHeight-1)/2] = input.pixels[x][y];
            }
        }

        for(int y = 0; y < output.height; y++){

            for(int i = 0; i < (kernelWidth-1)/2; i++){
                output.pixels[i][y] = output.pixels[(kernelWidth-1) - i][y];
            }

            for(int i = 0; i < (kernelWidth-1)/2; i++){
//                output.pixels[input.width+i][y] = output.pixels[(output.width-1)-i][y];
                output.pixels[input.width + (kernelWidth-1)/2 + i][y] = output.pixels[input.width+((kernelWidth-1)/2) - 1 - i][y];
            }
        }

        return output;
    }

    public static Image padBoundary(Image input, int kernelHeight, int kernelWidth, String fileName){
        Image padded = padBoundary(input, kernelHeight, kernelWidth);

        if(fileName.length()>0){
            padded.WritePGM(fileName);
        }

        return padded;
    }
}
