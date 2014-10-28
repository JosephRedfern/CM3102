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

        double[][] twoDkernel = KernelGenerator.get2DGaussianKernel((int)sigma);
        Image twoDconvolved = Convolution.convolve(image, twoDkernel);
        twoDconvolved.WritePGM("isotropic.pgm");

//        double verticalKernel[][] = KernelGenerator.getVerticalKernel((int)sigma);
//        Image verticalConvolved = Convolution.convolve(image, verticalKernel);
//        verticalConvolved.WritePGM("vertical.pgm");
//
//        double horizontalKernel[][] = KernelGenerator.getHorizontalKernel((int)sigma);
//        Image horizontalConvolved = Convolution.convolve(image, horizontalKernel);
//        horizontalConvolved.WritePGM("horizontal.pgm");


//        try {
//            Runtime.getRuntime().exec("open convolved.pgm");
//        }catch(IOException e){
//
//        }
    }

    public AnisotropicSmoothing(String path, double sigma, String clean){

    }

}
