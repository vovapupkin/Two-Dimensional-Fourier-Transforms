package sample;


import org.apache.commons.math3.complex.Complex;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Lab_3 {
    BufferedImage sourceImage =  ImageIO.read(new File("C:/Users/Admin/Desktop/DSP_labs/src/res/2020.jpg"));
    BufferedImage resultImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), sourceImage.getType());

    Lab_3() throws IOException {
        Complex[][] sourceImageValues = new Complex[sourceImage.getWidth()][sourceImage.getHeight()];
        for(int i = 0; i < sourceImage.getWidth(); i++)
            for(int j = 0; j < sourceImage.getHeight(); j++)
                sourceImageValues[i][j] = new Complex((new Color(sourceImage.getRGB(i, j))).getGreen() * Math.pow(-1, i+j), 0);
        Complex[][] res = getDFTFunctionValues(sourceImageValues, 1);
        Integer[][] imaginarySpecter = new Integer[sourceImage.getWidth()][sourceImage.getHeight()];
        for(int i = 0; i < sourceImage.getWidth(); i++)
            for(int j = 0; j < sourceImage.getHeight(); j++) {
                imaginarySpecter[i][j] = (int)(res[i][j].getImaginary() * 1E13);
                if(imaginarySpecter[i][j] < 0)
                    imaginarySpecter[i][j] = -imaginarySpecter[i][j];
                if(imaginarySpecter[i][j] > 255)
                    imaginarySpecter[i][j] = 255;
                Color color = new Color(imaginarySpecter[i][j], imaginarySpecter[i][j], imaginarySpecter[i][j]);
                resultImage.setRGB(i, j, color.getRGB());
            }
        File outputFile = new File("C:/Users/Admin/Desktop/DSP_labs/src/res/result.jpg");
        ImageIO.write(resultImage, "jpg", outputFile);
        res = getDFTFunctionValues(res, -1);
        for(int i = 0; i < sourceImage.getWidth(); i++)
            for(int j = 0; j < sourceImage.getHeight(); j++) {
                imaginarySpecter[i][j] = (int)(res[i][j].getImaginary() * 1E10);
                if(imaginarySpecter[i][j] < 0)
                    imaginarySpecter[i][j] = -imaginarySpecter[i][j];
                if(imaginarySpecter[i][j] > 255)
                    imaginarySpecter[i][j] = 255;
                Color color = new Color(imaginarySpecter[i][j], imaginarySpecter[i][j], imaginarySpecter[i][j]);
                resultImage.setRGB(i, j, color.getRGB());
            }
        outputFile = new File("C:/Users/Admin/Desktop/DSP_labs/src/res/restore.jpg");
        ImageIO.write(resultImage, "jpg", outputFile);
    }

    private Complex getSumDFT(int u, int v, Complex[][] values, int sign) {
        Complex sum = new Complex(0, 0);
        for(int i = 0; i < sourceImage.getWidth(); i++)
            for(int j = 0; j < sourceImage.getHeight(); j++)
             sum = sum.add((values[i][j]).multiply((new Complex(Math.E, 0)).pow(new Complex(0, -1 * sign * 2 * Math.PI * (u * i / sourceImage.getWidth() + v * j / sourceImage.getHeight())))));
        return sum;
    }

    public Complex[][] getDFTFunctionValues(Complex[][] results, int sign) {
        Complex[][] resValues = new Complex[sourceImage.getWidth()][sourceImage.getHeight()];
        for(int i = 0; i < sourceImage.getWidth(); i++)
            for(int j = 0; j < sourceImage.getHeight(); j++)
                resValues[i][j] = getSumDFT(i, j, results, sign);
        return resValues;
    }

    public int[][] FFT2d(Complex[][] source, int sign, int maxI, int maxJ) {
        Complex[][] result = new Complex[maxI][maxJ];
        FourierTransform transform = new FourierTransform();
        for(int i = 0; i < maxI; i++) {
            Complex[] temp = new Complex[maxJ];
            System.arraycopy(source[i], 0, temp, 0, maxJ);
            temp = transform.TimeDecimationFFT(temp, sign);
            System.arraycopy(temp, 0, result[i], 0, maxJ);
        }
        for(int j = 0; j < maxJ; j++) {
            Complex[] temp = new Complex[maxI];
            for(int i = 0; i < maxI; i++)
                temp[i] = result[i][j];
            temp = transform.TimeDecimationFFT(temp, sign);
            for(int i = 0; i < maxI; i++)
                result[i][j] = temp[i];
        }
        int[][] resultInt = new int[maxI][maxJ];
        for(int i = 0; i < maxI; i++)
            for(int j = 0; j < maxJ; j++)
                resultInt[i][j] = (int)result[i][j].getArgument();
        return  resultInt;
    }
}
