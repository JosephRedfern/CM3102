import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Notes:
 * • I couldn't get Standard Deviation calculation for an isotropic image to work quickly. Uncomment the 3 lines after
 *   line 57 to attempt it.
 * • I was unsure of the Mutual Information calculation, and ran out of time - the output comes out has being negative,
 *   which seems odd. I've taken the magnitude in an attempt to correct this.
 * • The standard deviation calculation isn't perfect, seems much darker compared to your results. This, combined with
 *   the (by default) lack of an SD map for the Isotropic image makes the combined image seem odd. However, I believe
 *   the method used to calculate the combined image to be correct.
 *
 * Created by 1106886 on 25/10/14.
 */
public class AnisotropicSmoothing {

    public static void main(String args[]){
        if(args.length >= 2) {
            String path = args[0];
            Double sigma = Double.parseDouble(args[1]);

            if(args.length >= 3){
                String clean = args[2];
                AnisotropicSmoothing application = new AnisotropicSmoothing(path, sigma, clean);
            }else{
                AnisotropicSmoothing application = new AnisotropicSmoothing(path, sigma);
            }
        }else{
            System.err.printf("Invalid number of arguments. Expected 2 or more, got %s%n", args.length);
        }
    }

    public AnisotropicSmoothing(String path, double sigma){
        Image image = new Image();
        image.ReadPGM(path);

        //SD image -> filtered image mapping for combining.
        HashMap<Image, Image> mappings = new HashMap<Image, Image>();


        double verticalKernel[][] = KernelGenerator.getVerticalKernel((int)sigma);
        Image verticalConvolved = Convolution.convolve(image, verticalKernel);
        verticalConvolved.WritePGM("vertical.pgm");
        Image verticalSD = StandardDeviation.getVerticalStandardDeviation(verticalConvolved, (int) sigma);
        verticalSD.WritePGM("verticalSD.pgm");
        mappings.put(verticalSD, verticalConvolved);

        double horizontalKernel[][] = KernelGenerator.getHorizontalKernel((int)sigma);
        Image horizontalConvolved = Convolution.convolve(image, horizontalKernel);
        horizontalConvolved.WritePGM("horizontal.pgm");
        Image horizontalSD = StandardDeviation.getHorizontalStandardDeviation(horizontalConvolved, (int)sigma);
        horizontalSD.WritePGM("horizontalSD.pgm");
        mappings.put(horizontalSD, horizontalConvolved);


        Image isotropicConvolved = Convolution.convolve(verticalConvolved, horizontalKernel);
        isotropicConvolved.WritePGM("isotropic.pgm");
//        UNCOMMENT THE BELOW 3 LINES TO ATTEMPT CALCULATION OF SD FOR ISOTROPIC IMAGE - SLOW PROCESS.

//        Image isotropicSD = StandardDeviation.getIsotropicStandardDeviation(isotropicConvolved, (int)sigma);
//        isotropicSD.WritePGM("isotropicSD.pgm");
//        mappings.put(isotropicSD, isotropicConvolved);


        double[][] fdKernel = KernelGenerator.getForwardDiagonal((int) sigma);
        Image fdConvolved = Convolution.convolve(image, fdKernel, true);
        fdConvolved.WritePGM("diagonalF.pgm");
        Image diagonalFSD = StandardDeviation.getForwardDiagonalStandardDeviation(fdConvolved, (int)sigma);
        diagonalFSD.WritePGM("diagonalFSD.pgm");
        mappings.put(diagonalFSD, fdConvolved);

        double[][] bdKernel = KernelGenerator.getBackwardDiagonal((int) sigma);
        Image bdConvolved = Convolution.convolve(image, bdKernel);
        bdConvolved.WritePGM("diagonalR.pgm");
        Image diagonalRSD = StandardDeviation.getBackwardDiagonalStandardDeviation(bdConvolved, (int)sigma);
        diagonalRSD.WritePGM("diagonalRSD.pgm");
        mappings.put(diagonalRSD, bdConvolved);

        Image combined = combine(mappings, image.depth, image.width, image.height);
        combined.WritePGM("combined.pgm");

//        try {
//            Runtime.getRuntime().exec("open vertical.pgm");
//            Thread.sleep(100);
//            Runtime.getRuntime().exec("open verticalSD.pgm");
//            Thread.sleep(100);
//            Runtime.getRuntime().exec("open combined.pgm");
//            Thread.sleep(100);
//            Runtime.getRuntime().exec("open horizontal.pgm");
//            Thread.sleep(100);
//            Runtime.getRuntime().exec("open horizontalSD.pgm");
//            Thread.sleep(100);
//            Runtime.getRuntime().exec("open diagonalR.pgm");
//            Thread.sleep(100);
//            Runtime.getRuntime().exec("open diagonalRSD.pgm");
//            Thread.sleep(100);
//            Runtime.getRuntime().exec("open reflected.pgm");
//            Thread.sleep(50);
//            Runtime.getRuntime().exec("open isotropic.pgm");
//            Thread.sleep(100);
//            Runtime.getRuntime().exec("open isotropic2.pgm");
//            Thread.sleep(100);
//            Runtime.getRuntime().exec("open isotropicSD.pgm");
//        }catch(Exception e){
//
//        }
    }

    //perform image combining based in smallest
    public static Image combine(HashMap<Image, Image> imageSDMap, int depth, int width, int height){
        Image combined = new Image(depth, width, height);

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int minSDVal = 256;
                int pixVal = 0;

                for(Map.Entry<Image, Image> entry : imageSDMap.entrySet()){
                    Image sdImage = entry.getKey();
                    Image image = entry.getValue();

                    if(sdImage.pixels[x][y] <= minSDVal){
                        pixVal = image.pixels[x][y];
                    }
                }

                combined.pixels[x][y] = pixVal;

            }
        }

        return combined;
    }

    //Perform results analysis.
    public AnisotropicSmoothing(String path, double sigma, String cleanPath){
        this(path, sigma);

        Image input = new Image();
        input.ReadPGM(path);

        Image result = new Image(); //sub-optimal.
        result.ReadPGM("combined.pgm");

        Image clean = new Image();
        clean.ReadPGM(cleanPath);

        double inputRMSE = getRMSE(input, clean);
        double inputMI = getMI(input, clean);
        System.out.printf("input image RMSE = %s%n", inputRMSE);
        System.out.printf("input image MI = %s%n", inputMI); //Unsure of MI calculation...

        double resultRMSE = getRMSE(input, result);
        double resultMI = getMI(input, result);
        System.out.printf("result\t RMSE = %s%n", resultRMSE);
        System.out.printf("result\t image MI = %s%n", resultMI);
    }

    //Get RMSE between two images
    private double getRMSE(Image image1, Image image2){
        long error = 0;

        for(int y = 0; y < image1.height; y++){
            for(int x = 0; x < image1.width; x++){
                error += Math.pow((double)(image1.pixels[x][y] - image2.pixels[x][y]), 2);
            }
        }

        return Math.sqrt(error/(image1.width * image1.height));
    }


    // Roughly based on:
    // http://stackoverflow.com/questions/23691398/mutual-information-of-two-images-matlab
    private static double getMI(Image image1, Image image2){
        double[][] jointHist = new double[256][256];

        for(int y = 0; y < image1.height; y++){
            for(int x = 0; x < image1.width; x++){
                jointHist[image1.pixels[x][y]][image2.pixels[x][y]] += 1;
            }
        }

        for(int y = 0; y < image1.height; y++){
            for(int x = 0; x < image1.width; x++){
                jointHist[image1.pixels[x][y]][image2.pixels[x][y]] /= (image1.width*image1.height);
            }
        }

        double jointEntropy = 0;
        int nonZeroCount = 0;

        for(int y = 0; y < 256; y++){
            for(int x = 0; x < 256; x++){
                if(jointHist[x][y]!=0) {
                    nonZeroCount += 1;
                    jointEntropy += jointHist[x][y] * (Math.log(jointHist[x][y]) / Math.log(2));
                }
            }
        }

        jointEntropy *= -1;

        return Math.abs(getEntropy(image1) + getEntropy(image2) - jointEntropy);


    }

    private static double getEntropy(Image input){
        double entropy = 0;
        int[] image1histogram = getHist(input);

        int nonZeroCount = 0;

        for(int n = 0; n < image1histogram.length; n++){
            if(image1histogram[n]!=0) {
                entropy += image1histogram[n] * (Math.log(image1histogram[n]) / Math.log(2));
                nonZeroCount++;
            }
        }

        if (nonZeroCount > 0) {
            for (int n = 0; n < image1histogram.length; n++) {
                entropy /= nonZeroCount;
            }
        }

        return entropy * -1;
    }

    private static int[] getHist(Image input){
        int[] histogram = new int[256];

        for(int y = 0; y < input.height; y++){
            for(int x = 0; x < input.width; x++){
                histogram[input.pixels[x][y]] += 1;
            }
        }
        return histogram;
    }

}
