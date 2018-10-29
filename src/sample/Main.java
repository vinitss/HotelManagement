package sample;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.*;
import java.time.LocalDate;

public class Main extends Application {

    /* All global fields */
    public static Boolean loggedIn =false;
    public static int employee_id;
    public static int deluxeCount;
    public static int standardCount;
    public static int superiorCount;
    public static float deluxePrice;
    public static float standardPrice;
    public static float superiorPrice;
    public static String roomSize;
    public static int roomNo;
    public static String chkin;
    public static String chkout;
    public static int daysCount;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        Scene ps=new Scene(root, 1220, 875);
        primaryStage.setScene(ps);
        primaryStage.sizeToScene();
        primaryStage.show();
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());

    }


    public static void main(String[] args) {
        launch(args);
    }
}
