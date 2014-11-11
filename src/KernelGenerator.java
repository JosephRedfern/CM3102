import java.util.Arrays;

/**
 * Created by joe on 25/10/14.
 */
public class KernelGenerator {
    public static double[][] getGaussianKernel(int width, int height, double sigma){
        double[][] kernel = new double[width][height];

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++){
                double distCenter = Math.sqrt( Math.pow((width/2 - x),2) + Math.pow((height/2 - y),2)); //normalise X and Y values around origin, compute distance
                kernel[x][y] = gaussianFunction(distCenter, sigma);
            }
        }

        return normalise(kernel, 1);
    }

    private static double[][] normalise(double[][] input, int target){
        //find sum
        double sum = 0;

        for(int x = 0; x<input.length; x++){
            for(int y = 0; y<input[0].length; y++){
                sum+=input[x][y];
            }
        }

        double multiplier = target/sum;

        for(int x = 0; x < input.length; x++){
            for(int y = 0; y < input[0].length; y++){
                input[x][y] *= multiplier;
            }
        }

        return input;

    }

    public static double[][] getBackwardDiagonal(int sigma){
        int length =  1 + (sigma * 6);
        double[][] kernel = new double[length][length];

        for(int i = 0; i < length; i++){

            double distCenter = Math.sqrt( Math.pow((length/2 - i),2) + Math.pow((length/2 - i),2)); //normalise X and Y values around origin, compute distance
            kernel[i][i] = gaussianFunction(distCenter, sigma);
        }

        return normalise(kernel, 1);
    }

    public static double[][] getForwardDiagonal(int sigma){
        int length =  1 + (sigma * 6);
        double[][] kernel = new double[length][length];

        for(int i = 0; i < length; i++){

            double distCenter = Math.sqrt( Math.pow((length/2 - i),2) + Math.pow((length/2 - i),2)); //normalise X and Y values around origin, compute distance
            kernel[i][length-i-1] = gaussianFunction(distCenter, sigma);
        }

        return normalise(kernel, 1);
    }

    public static double[][] get2DGaussianKernel(int sigma){
        int kernelLength = 6*sigma + 1;
        return getGaussianKernel(kernelLength, kernelLength, sigma);
    }

    public static double[][] getVerticalKernel(int sigma){
        int kernelHeight = 6*sigma + 1;
        return getGaussianKernel(kernelHeight, 1, sigma);
    }


    public static double[][] getHorizontalKernel(int sigma){
        int kernelWidth = 6*sigma + 1;
        return getGaussianKernel(1, kernelWidth, sigma);
    }

    public static double gaussianFunction(double x, double sigma){
        double a = 1/(sigma * Math.sqrt(2 * Math.PI));
        double b = 0; //μ, expected value = 0?
        double c = sigma;
        double d = 0;

        return a * Math.exp(-(Math.pow(x-b, 2)/(2*Math.pow(c, 2)))) + d;
    }
}
