package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import connectivity.ConnectivityClass;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class signup_controller {
    
    /* all UI field */ 
    public TextField username;
    public TextField email;
    public TextField firstname;
    public TextField lastname;
    public PasswordField password;
    public TextField contact;
    public Label notice;
    
    
    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    public void changeScreenButtonPushed(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene window = (Scene)((Node)event.getSource()).getScene();
        window.setRoot(root);
    }

    public void changeScreenlogin(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("sample2.fxml"));
        Scene window = (Scene)((Node)event.getSource()).getScene();
        window.setRoot(root);
    }

    // for signing up
    public void signup_database(ActionEvent event) throws Exception
    {
        try {
            Boolean invalid_flg=false;
            String usernameText=username.getText();
            String emailText = email.getText();
            String passwordText = password.getText();
            String contactText = contact.getText();
            String firstnameText=firstname.getText();
            String lastnameText= lastname.getText();
            if (usernameText.equals("") | emailText.equals("") | passwordText.equals("") | contactText.equals("") | passwordText.length()<3 | firstnameText.equals("") | lastnameText.equals(""))
            {
                notice.setText("Invalid credentials");
                invalid_flg=true;
            }
            else if (contactText.length()!=10 | (contactText.matches("[0-9]+")==false))
            {
                notice.setText("Invalid Contact Number");
                invalid_flg=true;
            }
            else if ( isValidEmailAddress(emailText)==false)
            {
                notice.setText("Invalid email address");
                invalid_flg=true;
            }

            ConnectivityClass connectionClass = new ConnectivityClass();
            Connection connection = connectionClass.getConnection();
            //insert only if username is unique
            if (invalid_flg==false) {
                Statement statement = connection.createStatement();
                String sql = "SELECT * FROM employee WHERE username = '" + username.getText() + "';";
                ResultSet resultSet = statement.executeQuery(sql);

                if (resultSet.next()) {
                    notice.setText("Username is taken");
                    invalid_flg = true;
                }
            }

            if (invalid_flg==false)
            {
                notice.setText("");

                Statement statement1=connection.createStatement();
                String sql1="INSERT INTO employee (username,email,contact,password,firstname,lastname) VALUES('"+ username.getText()+"','" + email.getText()+ "','" + contactText + "','" + passwordText + "','" + firstnameText +"','" +lastnameText + "');";
                statement1.executeUpdate(sql1);
                Parent root = FXMLLoader.load(getClass().getResource("sample2.fxml"));
                Scene window = (Scene)((Node)event.getSource()).getScene();
                window.setRoot(root);
            }
            else
            {
                username.setText("");
                firstname.setText("");
                lastname.setText("");
                password.setText("");
                contact.setText("");
                email.setText("");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
