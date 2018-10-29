package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import connectivity.ConnectivityClass;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import static sample.Main.loggedIn;

import static sample.Controller2.USERNAME;



public class Profile implements Initializable{
    
    /* All UI fields */
    
    // user -> employee name
    // username ->  Page contains heading of username ,  this is to store that heading
    // Both will contain same content
    
    
    public TextField user;
    public TextField firstname;
    public TextField lastname;
    public Label username; 
    public PasswordField password;
    public TextField contact;
    public TextField email;
    public Label notice;
    
    /* in UI password field is not shown , hence storing it , so that when employee wants same old password , we can use this to query in database */ 
    String password_which_is_not_shown;  // storing password initially from table
    String ID; // id of employee stored locally
    
    public void initialize(URL url, ResourceBundle rb)
    {
        username.setText(USERNAME);
        user.setText(USERNAME);

        try{

            ConnectivityClass connectionClass = new ConnectivityClass();
            Connection connection = connectionClass.getConnection();
            Statement statement=connection.createStatement();
            String sql="SELECT * FROM employee WHERE username = '"+ username.getText()+ "' ;";
            ResultSet m_ResultSet=statement.executeQuery(sql);
            if (m_ResultSet.next()) {
                contact.setText(m_ResultSet.getString(3));
                email.setText(m_ResultSet.getString(2));
                password_which_is_not_shown = m_ResultSet.getString(4);
                firstname.setText(m_ResultSet.getString(5));
                lastname.setText(m_ResultSet.getString(6));
                ID =m_ResultSet.getString(7);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    
    // this function only checks if email is valid or not
    // dont know how it works , just copy pasted
    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    
    //Home page button
    public void changeScreenButtonPushed(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene window = (Scene)((Node)event.getSource()).getScene();
        window.setRoot(root);
    }

    
    // Edit button which is clicked calls this function
    public void edit_changes(ActionEvent event) throws IOException
    {
        try {
            Boolean invalid_flg=false;
            String userText =user.getText();
            String emailText = email.getText();
            String passwordText = password.getText();
            String firstnameText=firstname.getText();
            String lastnameText = lastname.getText();

            if (passwordText.length()==0)
            {
                passwordText=password_which_is_not_shown;
            }
            String contactText = contact.getText();

            // empty field not allowed
            if (userText.equals("") | emailText.equals("") | passwordText.equals("") | contactText.equals("") | passwordText.length()<3  | firstnameText.equals("") | lastnameText.equals(""))
            {
                notice.setText("Invalid credentials");
                invalid_flg=true;
            }

            // contact number must be 10 in length and contain numbers
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
            if (invalid_flg==false & user.getText().equals(USERNAME)==false) {
                Statement statement = connection.createStatement();
                String sql = "SELECT * FROM employee WHERE username = '" + user.getText() + "';";
                ResultSet resultSet = statement.executeQuery(sql);

                if (resultSet.next()) {
                    notice.setText("Username is taken");
                    invalid_flg = true;
                }
            }

            if (invalid_flg==false)
            {
                notice.setText("");
                Statement statement=connection.createStatement();

                String upsql="UPDATE employee SET email = '" + email.getText() + "', contact = '"+ contactText + "', password = '" + passwordText  + "', firstname = '" +firstnameText + "', lastname = '" + lastnameText + "', username = '" + user.getText() +"' where employee_id = " + ID;
                statement.executeUpdate(upsql);
                USERNAME=userText;
                Parent root = FXMLLoader.load(getClass().getResource("profile.fxml"));
                Scene window = (Scene)((Node)event.getSource()).getScene();
                window.setRoot(root);
            }
            else
            {

                // if invalid credentials then refill old data in all TextField
                username.setText(USERNAME);
                user.setText(USERNAME);
                try{


                    Statement statement=connection.createStatement();
                    String sql="SELECT * FROM employee WHERE username = '"+ username.getText()+ "' ;";
                    ResultSet m_ResultSet=statement.executeQuery(sql);
                    if (m_ResultSet.next()) {
                        contact.setText(m_ResultSet.getString(3));
                        email.setText(m_ResultSet.getString(2));
                        password_which_is_not_shown= m_ResultSet.getString(4);
                        firstname.setText(m_ResultSet.getString(5));
                        lastname.setText(m_ResultSet.getString(6));
                        ID=m_ResultSet.getString(7);
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }
}
