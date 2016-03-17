package sample;

import org.apache.commons.math3.complex.Complex;

public class Functions {

    private double period = Math.PI * 2/3 ;
    private double step = period/FourierTransform.N;

    public static double functionY(double x) {
        return Math.sin(6 * x) + Math.cos(3 * x);
    }
    public static double functionZ(double x) {
        return Math.cos(7 * x);
    }

    public double[] getFunctionYValuesY() {
        double[] values = new double[FourierTransform.N];
        for(double i = 0, j = 0; j < FourierTransform.N; i += step, j++)
            values[(int) j] = functionY(i);
        return values;
    }

    public double[] getFunctionValuesX() {
        double[] values = new double[FourierTransform.N];
        for(double i = 0, j = 0; j < FourierTransform.N; i += step, j++)
            values[(int) j] = i;
        return values;
    }

    public Complex[] getFunctionYComplexValuesY() {
        Complex[] values = new Complex[FourierTransform.N];
        for(double i = 0, j = 0; j < FourierTransform.N; i += step, j++)
            values[(int) j] = new Complex(functionY(i), 0);
        return values;
    }

    public double[] getFunctionZValuesY() {
        double[] values = new double[FourierTransform.N];
        for(double i = 0, j = 0; j < FourierTransform.N; i += step, j++)
            values[(int) j] = functionZ(i);
        return values;
    }

    public Complex[] getFunctionZComplexValuesY() {
        Complex[] values = new Complex[FourierTransform.N];
        for(double i = 0, j = 0; j < FourierTransform.N; i += step, j++)
            values[(int) j] = new Complex(functionZ(i), 0);
        return values;
    }

}
