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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Label;

import static sample.Main.loggedIn;
import static sample.Main.employee_id;
public class Controller2 {


    public static String USERNAME=""; // global field username (all capital) in order to quickly get username

    /* All ids from UI */
    public TextField username;
    public PasswordField password;
    public Label notice; // this is error message which is displayed for invalid data.

    // change to Home page
    public void changeScreenButtonPushed(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene window = (Scene)((Node)event.getSource()).getScene();
        window.setRoot(root);
    }

    // change to Signup page
    public void changeScreensignup(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("signup.fxml"));
        Scene window = (Scene)((Node)event.getSource()).getScene();
        window.setRoot(root);
    }
    public void test_login(ActionEvent event) throws Exception
    {
        Boolean invalid_flg=false;
        // this flag is only to indicate whether data is valid or not, (false -> means valid )
        try {
            ConnectivityClass connectionClass = new ConnectivityClass();
            Connection connection = connectionClass.getConnection();

            // if empty username,password, or password is shorter than 3 characters
             if (username.getText().equals("") | password.getText().equals("") | password.getText().length()<3)
             {
                 invalid_flg=true;
             }
             else {
                Statement statement=connection.createStatement();

                // Querying user entered data from mysql
                String sql="SELECT * FROM employee WHERE username = '"+ username.getText()+"' and password = '" + password.getText() + "' ;";
                ResultSet m_ResultSet=statement.executeQuery(sql);

                if (m_ResultSet.next()){
                    System.out.println("Logged in");

                    employee_id = Integer.parseInt(m_ResultSet.getString(7));
                    System.out.println(m_ResultSet.getString(1) + ", " + m_ResultSet.getString(2) + ", "
                            + m_ResultSet.getString(3));
                    System.out.println(employee_id);

                    /*



    boolean next() throws SQLException

    Moves the cursor froward one row from its current position. A ResultSet cursor is initially positioned before the first row; the first call to the method next makes the first row the current row; the second call makes the second row the current row, and so on.

    When a call to the next method returns false, the cursor is positioned after the last row. Any invocation of a ResultSet method which requires a current row will result in a SQLException being thrown. If the result set type is TYPE_FORWARD_ONLY, it is vendor specified whether their JDBC driver implementation will return false or throw an SQLException on a subsequent call to next.

    If an input stream is open for the current row, a call to the method next will implicitly close it. A ResultSet object's warning chain is cleared when a new row is read.

    Returns: true if the new current row is valid; false if there are no more rows Throws: SQLException - if a database access error occurs or this method is called on a closed result set

                     */


                }else {
                    invalid_flg=true;
                }
            }
            if (invalid_flg)
            {
                notice.setText("Invalid credentials");
                username.setText("");
                password.setText("");
                // reset fields in UI
            }
            else
            {
                notice.setText("");
                // Remove error message
                loggedIn=true;
                USERNAME=username.getText();

                // store username value for other controller , hence globally stored

                // GO TO home screen
                Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
                Scene window = (Scene)((Node)event.getSource()).getScene();
                window.setRoot(root);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
