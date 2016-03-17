package sample;

import org.apache.commons.math3.complex.Complex;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Lab_3_1 {
    BufferedImage sourceImage =  ImageIO.read(new File("C:/Users/Admin/Desktop/DSP_labs/src/res/5050.jpg"));
    BufferedImage recoveredImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), sourceImage.getType());
    BufferedImage magnitudeImageRed = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), sourceImage.getType());
    BufferedImage phaseImageRed = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), sourceImage.getType());
    BufferedImage magnitudeImageGreen = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), sourceImage.getType());
    BufferedImage phaseImageGreen = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), sourceImage.getType());
    BufferedImage magnitudeImageBlue = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), sourceImage.getType());
    BufferedImage phaseImageBlue = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), sourceImage.getType());

    Lab_3_1() throws IOException {
        Complex[][] convertedRed = DDFTRed();
        Complex[][] convertedGreen = DDFTGreen();
        Complex[][] convertedBlue = DDFTBlue();
        setMagnitudeImageRed(convertedRed);
        setPhaseImageRed(convertedRed);
        setMagnitudeImageGreen(convertedGreen);
        setPhaseImageGreen(convertedGreen);
        setMagnitudeImageBlue(convertedBlue);
        setPhaseImageBlue(convertedBlue);
        ODDFT(getComplexDataRed(), getComplexDataGreen(), getComplexDataBlue());
        File outputFile = new File("C:/Users/Admin/Desktop/DSP_labs/src/res/restore.jpg");
        ImageIO.write(recoveredImage, "jpg", outputFile);
        outputFile = new File("C:/Users/Admin/Desktop/DSP_labs/src/res/magnitudeRed.jpg");
        ImageIO.write(magnitudeImageRed, "jpg", outputFile);
        outputFile = new File("C:/Users/Admin/Desktop/DSP_labs/src/res/phaseRed.jpg");
        ImageIO.write(phaseImageRed, "jpg", outputFile);
        outputFile = new File("C:/Users/Admin/Desktop/DSP_labs/src/res/magnitudeGreen.jpg");
        ImageIO.write(magnitudeImageGreen, "jpg", outputFile);
        outputFile = new File("C:/Users/Admin/Desktop/DSP_labs/src/res/phaseGreen.jpg");
        ImageIO.write(phaseImageGreen, "jpg", outputFile);
        outputFile = new File("C:/Users/Admin/Desktop/DSP_labs/src/res/magnitudeBlue.jpg");
        ImageIO.write(magnitudeImageBlue, "jpg", outputFile);
        outputFile = new File("C:/Users/Admin/Desktop/DSP_labs/src/res/phaseBlue.jpg");
        ImageIO.write(phaseImageBlue, "jpg", outputFile);
    }

    Complex[][] DDFTRed() {
        if (sourceImage == null)
            return null;
        int N = sourceImage.getHeight();
        int M = sourceImage.getWidth();
        Complex[][] redArray = new Complex[M][N];
        for (int u = 0; u < M; u++)
        {
            for (int v = 0; v < N; v++)
            {
                Complex newPixel = new Complex(0,0);
                for (int x = 0; x < M; x++)
                {
                    for (int y = 0; y < N; y++)
                    {
                        Complex redPixelValue = new Complex(new Color(sourceImage.getRGB(x, y)).getRed());
                        newPixel = newPixel.add(redPixelValue.multiply(((x + y) % 2 == 0)? 1 : -1).multiply(getExponent(x, y, M, N, u, v, false)));
                    }
                }

                newPixel = newPixel.divide(M * N).multiply(FilterRed(u,v, M, N));
                redArray[u][v] = newPixel;
            }
        }
        return redArray;
    }

    Complex[][] DDFTGreen() {
        if (sourceImage == null)
            return null;
        int N = sourceImage.getHeight();
        int M = sourceImage.getWidth();
        Complex[][] redArray = new Complex[M][N];
        for (int u = 0; u < M; u++)
        {
            for (int v = 0; v < N; v++)
            {
                Complex newPixel = new Complex(0,0);
                for (int x = 0; x < M; x++)
                {
                    for (int y = 0; y < N; y++)
                    {
                        Complex redPixelValue = new Complex(new Color(sourceImage.getRGB(x, y)).getGreen());
                        newPixel = newPixel.add(redPixelValue.multiply(((x + y) % 2 == 0)? 1 : -1).multiply(getExponent(x, y, M, N, u, v, false)));
                    }
                }

                newPixel = newPixel.divide(M * N).multiply(FilterGreen(u,v, M, N));
                redArray[u][v] = newPixel;
            }
        }
        return redArray;
    }

    Complex[][] DDFTBlue() {
        if (sourceImage == null)
            return null;
        int N = sourceImage.getHeight();
        int M = sourceImage.getWidth();
        Complex[][] redArray = new Complex[M][N];
        for (int u = 0; u < M; u++)
        {
            for (int v = 0; v < N; v++)
            {
                Complex newPixel = new Complex(0,0);
                for (int x = 0; x < M; x++)
                {
                    for (int y = 0; y < N; y++)
                    {
                        Complex redPixelValue = new Complex(new Color(sourceImage.getRGB(x, y)).getBlue());
                        newPixel = newPixel.add(redPixelValue.multiply(((x + y) % 2 == 0)? 1 : -1).multiply(getExponent(x, y, M, N, u, v, false)));
                    }
                }

                newPixel = newPixel.divide(M * N).multiply(FilterBlue(u,v, M, N));
                redArray[u][v] = newPixel;
            }
        }
        return redArray;
    }

    double FilterRed(int u, int v, int M, int N) {
        return (u == M / 2 && v == N / 2) ? 1 : 1;
    }
    double FilterGreen(int u, int v, int M, int N) {
        return (u == M / 2 && v == N / 2) ? 1 : 1;
    }
    double FilterBlue(int u, int v, int M, int N) {
        return (u == M / 2 && v == N / 2) ? 1 : 1;
    }

    Complex getExponent(double x, double y, double m, double n, double u, double v, Boolean plus) {
        int mult = (plus == true) ? 1 : -1;
        return new Complex(Math.cos(mult * 2 * Math.PI * (u * x / m + v * y / n)), Math.sin(mult * 2 * Math.PI * (u * x / m + v * y / n)));
    }

    void ODDFT(Complex[][] arrayRed, Complex[][] arrayGreen, Complex[][] arrayBlue)
    {
        if (sourceImage == null)
            return;
        int N = sourceImage.getHeight();
        int M = sourceImage.getWidth();
        for (int u = 0; u < M; u++)
            for (int v = 0; v < N; v++) {
                Complex newPixel = new Complex(0,0);
                for (int x = 0; x < M; x++)
                    for (int y = 0; y < N; y++)
                        newPixel = newPixel.add( arrayRed[x][y].multiply(getExponent(x, y, M, N, u, v, true)));
                int r = (byte)(newPixel.getReal() * (((u + v) % 2 == 0) ? 1 : -1)) & 0xFF;
                newPixel = new Complex(0,0);
                for (int x = 0; x < M; x++)
                    for (int y = 0; y < N; y++)
                        newPixel = newPixel.add( arrayGreen[x][y].multiply(getExponent(x, y, M, N, u, v, true)));
                int g = (byte)(newPixel.getReal() * (((u + v) % 2 == 0) ? 1 : -1)) & 0xFF;
                newPixel = new Complex(0,0);
                for (int x = 0; x < M; x++)
                    for (int y = 0; y < N; y++)
                        newPixel = newPixel.add( arrayBlue[x][y].multiply(getExponent(x, y, M, N, u, v, true)));
                int b = (byte)(newPixel.getReal() * (((u + v) % 2 == 0) ? 1 : -1)) & 0xFF;
                recoveredImage.setRGB(u, v, new Color(r, g, b).getRGB());
            }
    }

    void setMagnitudeImageRed(Complex[][] ArrayDDFT) {
        int M = sourceImage.getWidth();
        int N = sourceImage.getHeight();
        if (sourceImage == null)
            return;
        for (int u = 0; u < M; u++)
            for (int v = 0; v < N; v++) {
                Double pixelMagnitudeDouble = ArrayDDFT[u][v].abs();
                Long pixelMagnitude = Double.doubleToLongBits(pixelMagnitudeDouble);
                Color pixel = new Color(
                        (byte)(pixelMagnitude >> 56) & 0xFF,
                        (byte)(pixelMagnitude >> 48) & 0xFF,
                        (byte)(pixelMagnitude >> 40) & 0xFF);
                magnitudeImageRed.setRGB(u, v, pixel.getRGB());
            }
    }

    void setMagnitudeImageGreen(Complex[][] ArrayDDFT) {
        int M = sourceImage.getWidth();
        int N = sourceImage.getHeight();
        if (sourceImage == null)
            return;
        for (int u = 0; u < M; u++)
            for (int v = 0; v < N; v++) {
                Double pixelMagnitudeDouble = ArrayDDFT[u][v].abs();
                Long pixelMagnitude = Double.doubleToLongBits(pixelMagnitudeDouble);
                Color pixel = new Color(
                        (byte)(pixelMagnitude >> 56) & 0xFF,
                        (byte)(pixelMagnitude >> 48) & 0xFF,
                        (byte)(pixelMagnitude >> 40) & 0xFF);
                magnitudeImageGreen.setRGB(u, v, pixel.getRGB());
            }
    }

    void setMagnitudeImageBlue(Complex[][] ArrayDDFT) {
        int M = sourceImage.getWidth();
        int N = sourceImage.getHeight();
        if (sourceImage == null)
            return;
        for (int u = 0; u < M; u++)
            for (int v = 0; v < N; v++) {
                Double pixelMagnitudeDouble = ArrayDDFT[u][v].abs();
                Long pixelMagnitude = Double.doubleToLongBits(pixelMagnitudeDouble);
                Color pixel = new Color(
                        (byte)(pixelMagnitude >> 56) & 0xFF,
                        (byte)(pixelMagnitude >> 48) & 0xFF,
                        (byte)(pixelMagnitude >> 40) & 0xFF);
                magnitudeImageBlue.setRGB(u, v, pixel.getRGB());
            }
    }

    void setPhaseImageRed(Complex[][] ArrayDDFT) {
        int M = sourceImage.getWidth();
        int N = sourceImage.getHeight();
        if (sourceImage == null)
            return;
        for (int u = 0; u < M; u++)
            for (int v = 0; v < N; v++) {
                Double pixelMagnitudeDouble = ArrayDDFT[u][v].getArgument();
                Long pixelMagnitude = Double.doubleToLongBits(pixelMagnitudeDouble);
                Color pixel = new Color(
                        (byte)(pixelMagnitude >> 56) & 0xFF,
                        (byte)(pixelMagnitude >> 48) & 0xFF,
                        (byte)(pixelMagnitude >> 40) & 0xFF);
                phaseImageRed.setRGB(u, v, pixel.getRGB());
            }
    }

    void setPhaseImageGreen(Complex[][] ArrayDDFT) {
        int M = sourceImage.getWidth();
        int N = sourceImage.getHeight();
        if (sourceImage == null)
            return;
        for (int u = 0; u < M; u++)
            for (int v = 0; v < N; v++) {
                Double pixelMagnitudeDouble = ArrayDDFT[u][v].getArgument();
                Long pixelMagnitude = Double.doubleToLongBits(pixelMagnitudeDouble);
                Color pixel = new Color(
                        (byte)(pixelMagnitude >> 56) & 0xFF,
                        (byte)(pixelMagnitude >> 48) & 0xFF,
                        (byte)(pixelMagnitude >> 40) & 0xFF);
                phaseImageGreen.setRGB(u, v, pixel.getRGB());
            }
    }

    void setPhaseImageBlue(Complex[][] ArrayDDFT) {
        int M = sourceImage.getWidth();
        int N = sourceImage.getHeight();
        if (sourceImage == null)
            return;
        for (int u = 0; u < M; u++)
            for (int v = 0; v < N; v++) {
                Double pixelMagnitudeDouble = ArrayDDFT[u][v].getArgument();
                Long pixelMagnitude = Double.doubleToLongBits(pixelMagnitudeDouble);
                Color pixel = new Color(
                        (byte)(pixelMagnitude >> 56) & 0xFF,
                        (byte)(pixelMagnitude >> 48) & 0xFF,
                        (byte)(pixelMagnitude >> 40) & 0xFF);
                phaseImageBlue.setRGB(u, v, pixel.getRGB());
            }
    }

    Complex[][] getComplexDataRed() {
        int N = sourceImage.getHeight();
        int M = sourceImage.getWidth();
        Complex[][] result = new Complex[M][N];
        for (int u = 0; u < M; u++)
            for (int v = 0; v < N; v++) {
                Long magnitudePixelLong = Integer.toUnsignedLong(magnitudeImageRed.getRGB(u, v));
                Long phasePixelLong = Integer.toUnsignedLong(phaseImageRed.getRGB(u, v));
                byte[] bytes = new byte[8];
                ByteBuffer.wrap(bytes).putLong(magnitudePixelLong);
                for(int i = 0; i < 3; i++)
                    bytes[i] = bytes[i + 5];
                bytes[4] = 0;
                Double magnitudePixelDouble = ByteBuffer.wrap(bytes).getDouble();
                bytes = new byte[8];
                ByteBuffer.wrap(bytes).putLong(phasePixelLong);
                for(int i = 0; i < 3; i++)
                    bytes[i] = bytes[i + 5];
                bytes[4] = 0;
                Double phasePixelDouble = ByteBuffer.wrap(bytes).getDouble();
                result[u][v] = new Complex(magnitudePixelDouble * Math.cos(phasePixelDouble), magnitudePixelDouble * Math.sin(phasePixelDouble));
            }
        return result;
    }

    Complex[][] getComplexDataGreen() {
        int N = sourceImage.getHeight();
        int M = sourceImage.getWidth();
        Complex[][] result = new Complex[M][N];
        for (int u = 0; u < M; u++)
            for (int v = 0; v < N; v++) {
                Long magnitudePixelLong = Integer.toUnsignedLong(magnitudeImageGreen.getRGB(u, v));
                Long phasePixelLong = Integer.toUnsignedLong(phaseImageGreen.getRGB(u, v));
                byte[] bytes = new byte[8];
                ByteBuffer.wrap(bytes).putLong(magnitudePixelLong);
                for(int i = 0; i < 3; i++)
                    bytes[i] = bytes[i + 5];
                bytes[4] = 0;
                Double magnitudePixelDouble = ByteBuffer.wrap(bytes).getDouble();
                bytes = new byte[8];
                ByteBuffer.wrap(bytes).putLong(phasePixelLong);
                for(int i = 0; i < 3; i++)
                    bytes[i] = bytes[i + 5];
                bytes[4] = 0;
                Double phasePixelDouble = ByteBuffer.wrap(bytes).getDouble();
                result[u][v] = new Complex(magnitudePixelDouble * Math.cos(phasePixelDouble), magnitudePixelDouble * Math.sin(phasePixelDouble));
            }
        return result;
    }

    Complex[][] getComplexDataBlue() {
        int N = sourceImage.getHeight();
        int M = sourceImage.getWidth();
        Complex[][] result = new Complex[M][N];
        for (int u = 0; u < M; u++)
            for (int v = 0; v < N; v++) {
                Long magnitudePixelLong = Integer.toUnsignedLong(magnitudeImageBlue.getRGB(u, v));
                Long phasePixelLong = Integer.toUnsignedLong(phaseImageBlue.getRGB(u, v));
                byte[] bytes = new byte[8];
                ByteBuffer.wrap(bytes).putLong(magnitudePixelLong);
                for(int i = 0; i < 3; i++)
                    bytes[i] = bytes[i+5];
                bytes[4] = 0;
                Double magnitudePixelDouble = ByteBuffer.wrap(bytes).getDouble();
                bytes = new byte[8];
                ByteBuffer.wrap(bytes).putLong(phasePixelLong);
                for(int i = 0; i < 3; i++)
                    bytes[i] = bytes[i+5];
                bytes[4] = 0;
                Double phasePixelDouble = ByteBuffer.wrap(bytes).getDouble();
                result[u][v] = new Complex(magnitudePixelDouble * Math.cos(phasePixelDouble), magnitudePixelDouble * Math.sin(phasePixelDouble));
            }
        return result;
    }
}
