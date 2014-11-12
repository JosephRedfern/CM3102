/**
 * Created by joe on 10/11/14.
 */
public class StandardDeviation {
    public static Image getStandardDeviation(Image i, int sigma){
        Image paddedImage = Convolution.padBoundary(i, 3*sigma, 3*sigma);
        Image sdImage = new Image(i.depth, i.width, i.height);

        for(int y = sigma*3; y <paddedImage.height - sigma*3; y++){
            for(int x = sigma*3; x < paddedImage.width - sigma*3; x++){
                double xbar = getWeightedAverage(paddedImage, sigma, x, y) /  (sigma*3*sigma*3);
                double runningTotal = 0;

                for(int yk = -3 * sigma; yk < 3 * sigma; yk++){
                    for(int xk = -3 * sigma; xk < 3 * sigma; xk++){
                        int xi = paddedImage.pixels[x+xk][y+yk];
                        double distCenter = Math.sqrt(Math.pow(xk, 2) + Math.pow(yk, 2));
                        double gaussianWeighting = KernelGenerator.gaussianFunction(distCenter, sigma);
                        runningTotal += gaussianWeighting * Math.pow((xi - xbar), 2);
                    }
                }

                sdImage.pixels[x-(sigma*3)][y-(sigma*3)] = (int)Math.sqrt(runningTotal);

            }
        }
        return sdImage;
    }

    private static double getWeightedAverage(Image i, int sigma, int px, int py){
        double runningTotal = 0;
        int kernelSize = sigma * 3;

        for(int y = -3*sigma; y < 3*sigma; y++){
            for(int x = -3*sigma; x < 3*sigma; x++){
                double distCenter = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
                double normalWeighting = KernelGenerator.gaussianFunction(distCenter, sigma);
                runningTotal += (i.pixels[px+x][py+y] * normalWeighting);
            }
        }

        return runningTotal/9;

    }
}
