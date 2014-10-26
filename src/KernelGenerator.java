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
                kernel[y][x] = gaussianFunction(distCenter, sigma);
            }
        }

        //Need sum of kernel in order to normalise so tha sum == 1.
        //TODO: Could probably do this some fancy way that doesn't require re-summing...
        double sum = 0;

        for(int y = 0; y < kernel.length; y++) {
            for (int x = 0; x < kernel[y].length; x++){
                sum += kernel[y][x];
            }
        }

        //After finding sum of the kernel, multiply every value by its reciprocal of sum to normalise
        double normalisationConstant = 1/sum;

        for(int y=0; y<kernel.length; y++){
            for(int x=0; x<kernel[y].length; x++){
                kernel[y][x] *= normalisationConstant;
            }
        }

        return kernel;
    }

    public static double[][] get2DGaussianKernel(int sigma){
        int kernelLength = 3*sigma;
        return getGaussianKernel(kernelLength, kernelLength, sigma);
    }

    private static double gaussianFunction(double x, double sigma){
        double a = 1/(sigma * Math.sqrt(2 * Math.PI));
        double b = 0; //μ, expected value = 0?
        double c = sigma;
        double d = 0;

        return a * Math.exp(-(Math.pow(x-b, 2)/(2*Math.pow(c, 2)))) + d;
    }
}
