package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Lab_2 implements LabInterface {
    private FourierTransform ft = new FourierTransform();
    private Functions func = new Functions();


    public Group start() {
        Group root = new Group();
        ft.setDFTOperations();
        ft.setFFTOperations();

        root.getChildren().addAll(makeChart(870, 15, ft.getDFTFunctionValuesX(), ft.getCorrelationValues(), "Correlation"),
                makeChart(420, 15, ft.getDFTFunctionValuesX(), ft.getConvolutionValues(), "Convolution"),
                makeChart(-20, 15, func.getFunctionValuesX(), func.getFunctionYValuesY(), "Original function"));

        root.getChildren().addAll(makeChart(870, 390, ft.getDFTFunctionValuesX(), ft.getCorrelationFFTValues(), "Correlation with FFT"),
                makeChart(420, 390, ft.getDFTFunctionValuesX(), ft.getConvolutionFFTValues(), "Convolution with FFT"),
                makeChart(-20, 390, func.getFunctionValuesX(), func.getFunctionZValuesY(), "Original function"));

        Text textDFT = new Text(200, 30, "Discrete Fourier Transform");
        textDFT.setScaleX(3);
        textDFT.setScaleY(3);
        Text textFFT = new Text(300, 410, "Fast Fourier Transform with Time Decimation");
        textFFT.setScaleX(3);
        textFFT.setScaleY(3);
        root.getChildren().addAll(textDFT, textFFT);
        return root;
    }

    private LineChart<Number, Number> makeChart(int x, int y, double[] valueX, double[] valueY, String name) {
        final LineChart<Number, Number> chart =
                new LineChart<Number, Number>(new NumberAxis(), new NumberAxis());
        chart.setCreateSymbols(false);
        chart.getData().add(makeSeries(valueX, valueY, name));
        chart.setLayoutX(x);
        chart.setLayoutY(y);
        chart.setScaleY(0.85);
        chart.setScaleX(0.85);
        return chart;
    }

    public XYChart.Series makeSeries(double[] arrayX, double[] arrayY, String name) {
        XYChart.Series series = new XYChart.Series();
        series.setName(name);
        for(int i = 0; i < arrayX.length; i++)
            series.getData().add(new XYChart.Data(arrayX[i], arrayY[i]));
        return series;
    }
}

