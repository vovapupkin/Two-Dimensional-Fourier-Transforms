package sample;

import javafx.application.Application;
import javafx.beans.binding.StringBinding;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        try {
            Lab_3_1 lab_3_1 = new Lab_3_1();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setTitle("DSP");
        stage.setFullScreen(true);
        Group root = new Group();
        Scene scene = new Scene(root, 400, 250, Color.WHITE);

        TabPane tabPane = new TabPane();

        Lab_1 lab1 = new Lab_1();
        Lab_2 lab2 = new Lab_2();
        tabPane.getTabs().addAll(makeTab("DFT & FFT", lab1), makeTab("Convolution & Correlation", lab2));

        BorderPane borderPane = new BorderPane();
        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());

        borderPane.setCenter(tabPane);
        root.getChildren().add(borderPane);
        stage.setScene(scene);
        stage.show();

    }

    private Tab makeTab(String string, LabInterface lab) {
        Tab tab = new Tab();
        tab.setText(string);
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(lab.start());
        tab.setContent(hbox);
        return tab;

    }

    public static void main(String[] args) {
        launch(args);
    }
}

