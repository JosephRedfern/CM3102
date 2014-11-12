/**
 * Created by joe on 10/11/14.
 */
public class StandardDeviation {
//    public static Image getIsotropicStandardDeviation(Image i, int sigma){
//        Image paddedImage = Convolution.padBoundary(i, 3*sigma, 3*sigma);
//        Image sdImage = new Image(i.depth, i.width, i.height);
//
//        for(int y = sigma*3; y <paddedImage.height - sigma*3; y++){
//            for(int x = sigma*3; x < paddedImage.width - sigma*3; x++){
//                double xbar = getWeightedAverage(paddedImage, sigma, x, y);
//                double runningTotal = 0;
//
//                for(int yk = -3 * sigma; yk < 3 * sigma; yk++){
//                    for(int xk = -3 * sigma; xk < 3 * sigma; xk++){
//                        int xi = paddedImage.pixels[x+xk][y+yk];
//                        double distCenter = Math.sqrt(Math.pow(xk, 2) + Math.pow(yk, 2));
//                        double gaussianWeighting = KernelGenerator.gaussianFunction(distCenter, sigma);
//                        runningTotal += gaussianWeighting * Math.pow((xi - xbar), 2);
//                    }
//                }
//
//                sdImage.pixels[x-(sigma*3)][y-(sigma*3)] = (int)Math.sqrt(runningTotal);
//
//            }
//        }
//        return sdImage;
//    }


    public static Image getHorizontalStandardDeviation(Image i, int sigma){
        Image paddedImage = Convolution.padBoundary(i, 1, (6*sigma)+1);
        System.out.printf("input image size %sx%s, padded size %sx%s%n", i.width, i.height, paddedImage.width, paddedImage.height);
        Image sdImage = new Image(i.depth, i.width, i.height);

        for(int y = 0; y <paddedImage.height; y++){
            for(int x = sigma*3; x < paddedImage.width - sigma*3; x++){
                double xbar = getHorizontalWeightedAverage(paddedImage, sigma, x, y);
                double runningTotal = 0;

                for(int xk = -3 * sigma; xk < 3 * sigma; xk++){
                    int xi = paddedImage.pixels[x+xk][y];
                    double gaussianWeighting = KernelGenerator.gaussianFunction(xk, sigma);
                    runningTotal += gaussianWeighting * Math.pow((xi - xbar), 2);
                }

                sdImage.pixels[x-(sigma*3)][y] = (int) Math.sqrt(runningTotal);
            }
        }
        return sdImage;
    }

    private static double getHorizontalWeightedAverage(Image i, int sigma, int px, int py){
        double runningTotal = 0;

        for(int x = -3*sigma; x < 3*sigma; x++){
            double normalWeighting = KernelGenerator.gaussianFunction(x, sigma);
            runningTotal += (i.pixels[px+x][py] * normalWeighting);
        }

        return runningTotal;

    }

    public static Image getVerticalStandardDeviation(Image i, int sigma){
        Image paddedImage = Convolution.padBoundary(i, (6*sigma)+1, 1);
        System.out.printf("input image size %sx%s, padded size %sx%s%n", i.width, i.height, paddedImage.width, paddedImage.height);
        Image sdImage = new Image(i.depth, i.width, i.height);

        for(int y = sigma*3; y <paddedImage.height - sigma*3; y++){
            for(int x = 0; x < paddedImage.width; x++){
                double xbar = getVerticalWeightedAverage(paddedImage, sigma, x, y);
                double runningTotal = 0;

                for(int yk = -3 * sigma; yk < 3 * sigma; yk++){
                    int xi = paddedImage.pixels[x][y+yk];
                    double gaussianWeighting = KernelGenerator.gaussianFunction(yk, sigma);
                    runningTotal += gaussianWeighting * Math.pow((xi - xbar), 2);
                }

                sdImage.pixels[x][y-(sigma*3)] = (int) Math.sqrt(runningTotal);
            }
        }
        return sdImage;
    }

    private static double getVerticalWeightedAverage(Image i, int sigma, int px, int py){
        double runningTotal = 0;
        double normalWeightingTotal = 0;

        for(int y = -3*sigma; y < 3*sigma; y++){
            double normalWeighting = KernelGenerator.gaussianFunction(y, sigma);
            normalWeightingTotal += normalWeighting;
            runningTotal += (i.pixels[px][py+y] * normalWeighting);
        }

        return runningTotal/normalWeightingTotal;

    }

    public static Image getBackwardDiagonalStandardDeviation(Image i, int sigma){
        Image paddedImage = Convolution.padBoundary(i, (6*sigma)+1, (6*sigma)+1);
        System.out.printf("input image size %sx%s, padded size %sx%s%n", i.width, i.height, paddedImage.width, paddedImage.height);
        Image sdImage = new Image(i.depth, i.width, i.height);

        for(int y = sigma*3; y <paddedImage.height - sigma*3; y++){
            for(int x = sigma*3; x < paddedImage.width - sigma*3; x++){
                double xbar = getBackwardDiagonalWeightedAverage(paddedImage, sigma, x, y);
                double runningTotal = 0;

                for(int n = -3 * sigma; n < 3 * sigma; n++){
                    int xi = paddedImage.pixels[x+n][y+n];

                    double dist = Math.abs(Math.sqrt(2) * n);

                    double gaussianWeighting = KernelGenerator.gaussianFunction(dist, sigma);
                    runningTotal += gaussianWeighting * Math.pow((xi - xbar), 2);
                }

                sdImage.pixels[x-(sigma*3)][y-(sigma*3)] = (int) Math.sqrt(runningTotal);
            }
        }
        return sdImage;
    }

    private static double getBackwardDiagonalWeightedAverage(Image i, int sigma, int px, int py){
        double runningTotal = 0;
        double normalWeightingTotal = 0;

        for(int n = -3*sigma; n < 3*sigma; n++){
            double normalWeighting = KernelGenerator.gaussianFunction(Math.abs(Math.sqrt(2) * n), sigma);
            normalWeightingTotal += normalWeighting;
            runningTotal += (i.pixels[px+n][py+n] * normalWeighting);
        }

        return runningTotal/normalWeightingTotal;

    }

    public static Image getForwardDiagonalStandardDeviation(Image i, int sigma){
        Image paddedImage = Convolution.padBoundary(i, (6*sigma)+1, (6*sigma)+1);
        System.out.printf("input image size %sx%s, padded size %sx%s%n", i.width, i.height, paddedImage.width, paddedImage.height);
        Image sdImage = new Image(i.depth, i.width, i.height);

        for(int y = sigma*3; y <paddedImage.height - sigma*3; y++){
            for(int x = sigma*3; x < paddedImage.width - sigma*3; x++){
                double xbar = getBackwardDiagonalWeightedAverage(paddedImage, sigma, x, y);
                double runningTotal = 0;

                for(int n = -3 * sigma; n < 3 * sigma; n++){
                    int xi = paddedImage.pixels[x+n][y+n];

                    double dist = Math.abs(Math.sqrt(2) * n);

                    double gaussianWeighting = KernelGenerator.gaussianFunction(dist, sigma);
                    runningTotal += gaussianWeighting * Math.pow((xi - xbar), 2);
                }

                sdImage.pixels[x-(sigma*3)][y-(sigma*3)] = (int) Math.sqrt(runningTotal);
            }
        }
        return sdImage;
    }

    private static double getForwardDiagonalWeightedAverage(Image i, int sigma, int px, int py){
        double runningTotal = 0;
        double normalWeightingTotal = 0;

        for(int n = -3*sigma; n < 3*sigma; n++){
            double normalWeighting = KernelGenerator.gaussianFunction(Math.abs(Math.sqrt(2) * n), sigma);
            normalWeightingTotal += normalWeighting;
            runningTotal += (i.pixels[3*sigma-n-1+px][py+n] * normalWeighting);
        }

        return runningTotal/normalWeightingTotal;

    }

}
