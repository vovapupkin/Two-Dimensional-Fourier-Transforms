package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Lab_1 implements LabInterface {
    private FourierTransform ft = new FourierTransform();
    private Functions func = new Functions();

    public Group start() {
        Group root = new Group();
        ft.setDFTOperations();
        ft.setFFTOperations();

        root.getChildren().addAll(makeChart(945, 0, func.getFunctionValuesX(), ft.getRestoreDFTFunctionValues(), "Restore function"),
                makeChart(605, 0, ft.getDFTFunctionValuesX(), ft.getDFTFunctionValuesPhase(), "Phase scope"),
                makeChart(270, 0, ft.getDFTFunctionValuesX(), ft.getDFTFunctionValuesAmplitude(), "Amplitude scope"),
                makeChart(-70, 0, func.getFunctionValuesX(), func.getFunctionYValuesY(), "Original function"));

        root.getChildren().addAll(makeChart(945, 400, func.getFunctionValuesX(), ft.getRestoreFFTFunctionValues(), "Restore function"),
                makeChart(605, 400, ft.getDFTFunctionValuesX(), ft.getFFTFunctionValuesPhase(), "Phase scope"),
                makeChart(270, 400, ft.getDFTFunctionValuesX(), ft.getFFTFunctionValuesAmplitude(), "Amplitude scope"),
                makeChart(-70, 400, func.getFunctionValuesX(), func.getFunctionYValuesY(), "Original function"));

        Text textDFT = new Text(200, 50, "Discrete Fourier Transform (" + ft.getDFTOperations() + ")");
        textDFT.setScaleX(3);
        textDFT.setScaleY(3);
        Text textFFT = new Text(300, 450, "Fast Fourier Transform with Time Decimation (" + ft.getFFTOperations() + ")");
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
        chart.setScaleY(0.7);
        chart.setScaleX(0.7);
        chart.setLayoutX(x);
        chart.setLayoutY(y);
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

