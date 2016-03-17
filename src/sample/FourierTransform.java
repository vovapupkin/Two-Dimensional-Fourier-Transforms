package sample;

import org.apache.commons.math3.complex.Complex;

public class FourierTransform {
    public final static int N = 64;
    private int DFTOperations = 0;
    private int FFTOperations = 0;
    private Functions func = new Functions();


    public void setDFTOperations() {
        DFTOperations = 0;
    }

    public void setFFTOperations() {
        FFTOperations = 0;
    }

    public int getDFTOperations() {
        return DFTOperations;
    }

    public int getFFTOperations() {
        return  FFTOperations;
    }

    public double[] getFFTFunctionValuesAmplitude() {
        FFTOperations = 0;
        double[] resValues = new double[N];
        Complex[] values = TimeDecimationFFT(func.getFunctionYComplexValuesY(), 1);
        for(int k = 0; k < N; k++) {
            values[k] = values[k];
            resValues[k] = values[k].abs();
        }
        return resValues;
    }

    public double[] getFFTFunctionValuesPhase() {
        double[] resValues = new double[N];
        Complex[] values = TimeDecimationFFT(func.getFunctionYComplexValuesY(), 1);
        for(int k = 0; k < N; k++) {
            values[k] = values[k].divide(N);
            resValues[k] = values[k].getArgument() / N;
        }
        return resValues;
    }

    public double[] getRestoreFFTFunctionValues() {
        double[] resValues = new double[N];
        Complex[] reverseValues =  TimeDecimationFFT(TimeDecimationFFT(func.getFunctionYComplexValuesY(), 1), -1);
        for(int k = 0; k < reverseValues.length; k++)
            resValues[k] = reverseValues[k].getReal();
        return resValues;
    }

    public Complex[] TimeDecimationFFT(Complex[] a, int dir) {
        int N = a.length;
        if (N == 1) return a;

        Complex[] a1 = new Complex[N / 2 + N % 2];
        Complex[] a2 = new Complex[N / 2];

        for (int i = 0; i < N; i++) {
            if (i % 2 == 0)
                a1[i / 2] = a[i];
            else
                a2[i / 2] = a[i];
        }

        Complex[] b1 = TimeDecimationFFT(a1, dir);
        Complex[] b2 = TimeDecimationFFT(a2, dir);

        Complex wN = new Complex(Math.cos(2 * Math.PI / N), -dir * Math.sin(2 * Math.PI / N));
        Complex w = new Complex(1, 0);
        Complex[] y = new Complex[N];


        Complex tmp;
        if(dir == 1)
            for(int j = 0; j < N / 2; j ++) {
                tmp = b2[j].multiply(w);
                y[j] = (b1[j].add(tmp)).divide(2);
                y[j + N / 2] = (b1[j].subtract(tmp)).divide(2);
                w = w.multiply(wN);

                FFTOperations++;
            }
        else if(dir == -1)
            for (int j = 0; j < N / 2; j++) {
                tmp = b2[j].multiply(w);
                y[j] = b1[j].add(tmp);
                y[j + N / 2] = b1[j].subtract(tmp);
                w = w.multiply(wN);

                FFTOperations++;
            }
        return y;
    }
    
    public double[] getDFTFunctionValuesAmplitude() {
        double[] resValues = new double[N];
        for(int k=0; k < N; k++) {
            Complex res = getSumDFT(k,  func.getFunctionYComplexValuesY(), 1);
            resValues[k] = res.abs() / N;
        }
        return resValues;
    }

    public double[] getDFTFunctionValuesPhase() {
        DFTOperations = 0;
        double[] resValues = new double[N];
        for(int k=0; k < N; k++) {
            Complex res = getSumDFT(k, func.getFunctionYComplexValuesY(), 1);
            resValues[k] = res.getArgument() / N;
        }
        return resValues;
    }

    private Complex[] getDFTFunctionComplexValuesY() {
        Complex[] resValues = new Complex[N];
        for(int k=0; k < N; k++) {
            Complex res = getSumDFT(k, func.getFunctionYComplexValuesY(), 1);
            resValues[k] = res.divide(N);
        }
        return resValues;
    }

    public double[] getDFTFunctionValuesX() {
        double[] resValues = new double[FourierTransform.N];
        for(int k=0; k < FourierTransform.N; k++)
            resValues[k] = k;
        return resValues;
    }

    public double[] getRestoreDFTFunctionValues() {
        double[] resValues = new double[N];
        for(int k = 0; k < N; k++) {
            Complex res = getSumDFT(k, getDFTFunctionComplexValuesY(), -1);
            resValues[k] = res.getReal();
        }
        return resValues;
    }

    private Complex getSumDFT(int k, Complex[] valuesY, int sign) {
        Complex sum = new Complex(0, 0);
        for(int i = 0; i < valuesY.length; i++) {
            sum = sum.add((valuesY[i]).multiply((new Complex(Math.E, 0)).pow(new Complex(0, -1 * sign * k * i * 2 * Math.PI / N))));
            DFTOperations++;
        }
        return sum;
    }

    private Complex[] ConvolutionCorrelation(Complex[] valuesY, Complex[] valuesZ, int op) {
        if (valuesY.length != valuesZ.length)
            return new Complex[] { };

        int N = valuesY.length;
        Complex[] result = new Complex[N];

        for(int i = 0; i < valuesY.length; i++)
            result[i] = new Complex(0, 0);

        for (int m = 0; m < N; m++) {
            for (int h = 0; h < N; h++) {
                if (op == 1) {
                    if (m - h >= 0)
                        result[m] = result[m].add(valuesY[h].multiply(valuesZ[m - h]));
                    else
                        result[m] = result[m].add(valuesY[h].multiply(valuesZ[m - h + N]));
                }
                else if (op == 0) {
                    if (m + h < N)
                        result[m] = result[m].add(valuesY[h].multiply(valuesZ[m + h]));
                    else
                        result[m] = result[m].add(valuesY[h].multiply(valuesZ[m + h - N]));
                }
            }
            result[m] = result[m].divide(N);
        }
        return result;
    }

    private Complex[] ConvolutionCorrelationFFT(Complex[] valuesY, Complex[] valuesZ, int op) {
        if (valuesY.length != valuesZ.length)
            return new Complex[] { };

        Complex[] transformedValuesY = TimeDecimationFFT(valuesY, 1);
        Complex[] transformedValuesZ = TimeDecimationFFT(valuesZ, 1);
        if (op == 0) {
            for(int i = 0; i < valuesY.length; i++)
                transformedValuesY[i] = transformedValuesY[i].conjugate();
        }
        for(int i = 0; i < transformedValuesY.length; i++)
            transformedValuesY[i] = transformedValuesY[i].multiply(transformedValuesZ[i]);
        return TimeDecimationFFT(transformedValuesY, -1);
    }

    public double[] getConvolutionValues() {
        Complex[] values = ConvolutionCorrelation(func.getFunctionYComplexValuesY(), func.getFunctionZComplexValuesY(), 1);
        double[] resValues = new double[values.length];
        for(int i = 0; i < values.length; i++)
            resValues[i] = values[i].getReal();
        return  resValues;
    }

    public double[] getCorrelationValues() {
        Complex[] values = ConvolutionCorrelation(func.getFunctionYComplexValuesY(), func.getFunctionZComplexValuesY(), 0);
        double[] resValues = new double[values.length];
        for(int i = 0; i < values.length; i++)
            resValues[i] = values[i].getReal();
        return  resValues;
    }

    public double[] getConvolutionFFTValues() {
        Complex[] values = ConvolutionCorrelationFFT(func.getFunctionYComplexValuesY(), func.getFunctionZComplexValuesY(), 1);
        double[] resValues = new double[values.length];
        for(int i = 0; i < values.length; i++)
            resValues[i] = values[i].getReal();
        return  resValues;
    }

    public double[] getCorrelationFFTValues() {
        Complex[] values = ConvolutionCorrelationFFT(func.getFunctionYComplexValuesY(), func.getFunctionZComplexValuesY(), 0);
        double[] resValues = new double[values.length];
        for(int i = 0; i < values.length; i++)
            resValues[i] = values[i].getReal();
        return  resValues;
    }
}
