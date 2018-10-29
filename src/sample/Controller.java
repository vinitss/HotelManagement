package sample;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static sample.Main.loggedIn;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller implements Initializable {
    public Button first_btn;
    public Button second_btn;
    public Button third_btn;
    /* All above buttons correspond to Home page buttons */

    /* Initializer is similar to constructor , every time this screen is loaded , its initializer is called by default */
    /* Constructor is also called every time by default , but after changing fxml values , it reverts back to default value */
    public void initialize(URL url, ResourceBundle rb)
    {
        //System.out.println("Hello World");
        if (loggedIn) {
            first_btn.setText("Profile");
            second_btn.setText("Logout");
            third_btn.setText("Room");
        } else {
            first_btn.setText("Sign Up");
            second_btn.setText("Log in ");
            third_btn.setText("Contact");
        }
    }

    /* this is for third button which is LOGIN button if employee is not logged in else LOGOUT button  */
    public void changeScreenButtonPushed(ActionEvent event) throws IOException
    {
        if (loggedIn)
        {
            loggedIn=false;
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            Scene window = (Scene) ((Node) event.getSource()).getScene();
            window.setRoot(root);
        }
        else {
            Parent root = FXMLLoader.load(getClass().getResource("sample2.fxml"));
            Scene window = (Scene) ((Node) event.getSource()).getScene();
            window.setRoot(root);
        }
    }

    /* this is for first button which is sign Up  button if employee is not logged in else profile button  */
    public void changeScreenSignUp(ActionEvent event) throws IOException
    {
        if (loggedIn)
        {
            Parent root = FXMLLoader.load(getClass().getResource("profile.fxml"));
            Scene window = (Scene) ((Node) event.getSource()).getScene();
            window.setRoot(root);
        }
        else {
            Parent root = FXMLLoader.load(getClass().getResource("signup.fxml"));
            Scene window = (Scene) ((Node) event.getSource()).getScene();
            window.setRoot(root);
        }
    }

    /* this is for third button which is contact button if employee is not logged in else room button  */
    public void changeScreenContact(ActionEvent event) throws IOException
    {
        if (loggedIn) {
            Parent root = FXMLLoader.load(getClass().getResource("room.fxml"));
            Scene window = (Scene) ((Node) event.getSource()).getScene();
            window.setRoot(root);
        }
        else{
            Parent root = FXMLLoader.load(getClass().getResource("contact.fxml"));
            Scene window = (Scene) ((Node) event.getSource()).getScene();
            window.setRoot(root);
        }
    }
}
