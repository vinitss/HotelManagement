package sample;

import connectivity.ConnectivityClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class GenerateBill {

    @FXML
    private TextField bookingid;

    @FXML
    private Label notice;

    public void Bill(ActionEvent event) throws IOException
    {
        Boolean invalid=false;
        if (bookingid.getText().equals(""))
            invalid=true;
        Connection connection=null;
        Statement statement=null;
        if (invalid==false) {
            try {
                String sql = "select booking_id from booking where booking_id = '" + bookingid.getText() + "';";
                ConnectivityClass connectionClass = new ConnectivityClass();
                connection = connectionClass.getConnection();
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                //Ensure we start with first row

                if (resultSet.next()) {


                    int booked = resultSet.getInt(1);

                    System.out.println(booked);
                    Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
                    Scene window = (Scene) ((Node) event.getSource()).getScene();
                    window.setRoot(root);
                } else {
                    invalid=true;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (invalid)
        {
            notice.setText("Invalid Booking ID");
        }


    }


    // change to Home page
    public void changeScreenButtonPushed(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene window = (Scene)((Node)event.getSource()).getScene();
        window.setRoot(root);
    }
}
