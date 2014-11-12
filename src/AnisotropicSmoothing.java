import java.io.IOException;

/**
 * Created by joe on 25/10/14.
 */
public class AnisotropicSmoothing {

    public static void main(String args[]){
        if(args.length >= 2) {
            String path = args[0];
            Double sigma = Double.parseDouble(args[1]);

            System.out.printf("Running with Sigma = %s%n", sigma);

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

//        Image sdInput = StandardDeviation.getStandardDeviation(image, (int)sigma);
//        sdInput.WritePGM("sdInput.pgm");

        double verticalKernel[][] = KernelGenerator.getVerticalKernel((int)sigma);
        Image verticalConvolved = Convolution.convolve(image, verticalKernel);
        verticalConvolved.WritePGM("vertical.pgm");
//        System.out.println("Convolved, SD-ing...");
//        StandardDeviation.getStandardDeviation(verticalConvolved, (int)sigma).WritePGM("verticalSD.pgm");
//
        double horizontalKernel[][] = KernelGenerator.getHorizontalKernel((int)sigma);
        Image horizontalConvolved = Convolution.convolve(image, horizontalKernel);
        horizontalConvolved.WritePGM("horizontal.pgm");
//        System.out.println("Convolved, SD-ing...");
//        StandardDeviation.getStandardDeviation(horizontalConvolved, (int)sigma).WritePGM("hortizontalSD.pgm");

        double[][] twoDkernel = KernelGenerator.get2DGaussianKernel((int)sigma);
        Image twoDconvolved = Convolution.convolve(image, twoDkernel);
        twoDconvolved.WritePGM("isotropic.pgm");
//        System.out.println("Convolved, SD-ing...");
//        StandardDeviation.getStandardDeviation(twoDconvolved, (int)sigma).WritePGM("isotropicSD.pgm");


        double[][] fdKernel = KernelGenerator.getForwardDiagonal((int) sigma);
        Image fdConvolved = Convolution.convolve(image, fdKernel);
        fdConvolved.WritePGM("diagonalF.pgm");
//        System.out.println("Convolved, SD-ing...");
//        StandardDeviation.getStandardDeviation(fdConvolved, (int)sigma).WritePGM("diagonalFSD.pgm");
//
        double[][] bdKernel = KernelGenerator.getBackwardDiagonal((int) sigma);
        Image bdConvolved = Convolution.convolve(image, bdKernel);
        bdConvolved.WritePGM("diagonalR.pgm");
//        System.out.println("Convolved, SD-ing...");
//        StandardDeviation.getStandardDeviation(bdConvolved, (int)sigma).WritePGM("diagonalRSD.pgm");


//        try {
//            Runtime.getRuntime().exec("open vertical.pgm");
//            Runtime.getRuntime().exec("open horizontal.pgm");
//            Runtime.getRuntime().exec("open reflected.pgm");
//            Thread.sleep(50);
//            Runtime.getRuntime().exec("open isotropic.pgm");
//            Runtime.getRuntime().exec("open sdInput.pgm");
//        }catch(Exception e){
//
//        }
    }

    public AnisotropicSmoothing(String path, double sigma, String clean){

    }

}
