package sample;

import connectivity.ConnectivityClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static sample.Main.*;

public class Customerdetails<formatter> {

    //id's of all the buttons,labels seen in UI
    @FXML
    public TextField firstName;

    @FXML
    public TextField lastName;

    @FXML
    public TextField emailAddress;

    @FXML
    public TextField phoneNumber;

    @FXML
    public Label alertMessage;
    @FXML
    public Label notice;


    //id's of all the buttons,labels seen in UI
    // only for adding timestamp in dates received , so that insert in database (database check_in datatype is datetime)
    String date = chkin;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // your template here
    java.util.Date dateStr  = formatter.parse(date);
    java.sql.Date dateDB = new java.sql.Date(dateStr.getTime());
    String date2 = chkout;
    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd"); // your template here
    java.util.Date dateStr2 = formatter2.parse(date2);
    java.sql.Date dateDB2 = new java.sql.Date(dateStr2.getTime());

    public Customerdetails() throws ParseException {
    }


    public void insertIntoBooking(int employeeId,int customerId,int roomNo) throws SQLException {
        String sql="insert into booking(employee_id,customer_id,room_No,check_in,check_out) values ("+employeeId+","+customerId+"" +
                ","+roomNo+", '"+ dateDB +"','"+ dateDB2+"');";
        Connection connection=null;
        Statement statement=null;
        try {
            ConnectivityClass connectionClass = new ConnectivityClass();
            connection = connectionClass.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(statement!=null)
                statement.close();

            if(connection!=null)
                connection.close();
        }

    }

    //when submit/book button clicked
    @FXML
    void addCustomer() throws SQLException {
        //sql part->adding into database
        Connection connection = null;
        Statement statement=null;
        ResultSet resultSet=null;
        String sqlWithLastName,sqlWithoutLastName;
        String sqlFindCustomerIdOfThisCustomer;
        int customerId;
        try {

            ConnectivityClass connectionClass = new ConnectivityClass();
            connection = connectionClass.getConnection();





            sqlWithLastName = "insert into customer(first_name,last_name,email_address,phone_number) values(?,?,?,?);";
            sqlWithoutLastName = "insert into customer(first_name,email_address,phone_number) values(?,?,?);";



            if(firstName.getText().length()==0) {
                alertMessage.setText("Please enter the first name.");
            }
            else if(emailAddress.getText().length()==0) {
                alertMessage.setText("Please enter the email address.");
            }
            else if(phoneNumber.getText().length()==0) {
                phoneNumber.setText("Please enter the phone number.");
            }
            else
            {
                PreparedStatement ps;
                if(lastName.getText().length()==0) {
                    ps=connection.prepareStatement(sqlWithoutLastName);
                    ps.setString(1,firstName.getText());
                    ps.setString(2,emailAddress.getText());
                    ps.setString(3,phoneNumber.getText());
                }
                else {
                    ps=connection.prepareStatement(sqlWithLastName);
                    ps.setString(1,firstName.getText());
                    ps.setString(2,lastName.getText());
                    ps.setString(3,emailAddress.getText());
                    ps.setString(4,phoneNumber.getText());

                }
                ps.executeUpdate();
                if (connection != null)
                    connection.close();
                sqlFindCustomerIdOfThisCustomer = "select customer_id from customer where first_name='" + firstName.getText() + "'" +
                        " and email_address='" + emailAddress.getText() + "' and phone_number='" + phoneNumber.getText() + "';";
                connection = connectionClass.getConnection();
                statement = connection.createStatement();
                resultSet = statement.executeQuery(sqlFindCustomerIdOfThisCustomer);
                //Ensure we start with first row
                resultSet.beforeFirst();
                resultSet.next();
                customerId = resultSet.getInt(1);


                insertIntoBooking(employee_id,customerId,roomNo);

                System.out.println("BOOOKED");

                String sql="select booking_id from booking where customer_id = "+ customerId +";";

                resultSet = statement.executeQuery(sql);
                //Ensure we start with first row
                resultSet.beforeFirst();
                resultSet.next();
                int bookingId = resultSet.getInt(1);

                String sql3="select price from room natural join room_price where room_no = "+ roomNo + ";";

                resultSet = statement.executeQuery(sql3);
                //Ensure we start with first row
                resultSet.beforeFirst();
                resultSet.next();
                int price = resultSet.getInt(1);
                int cost=daysCount*price;
                String sql2="insert into bill(customer_id,booking_id,employee_id,total_cost) values ("+ customerId + "," + bookingId +","+ employee_id+ ","+ cost + ")";
                statement.executeUpdate(sql2);
                notice.setText("Booking ID :" + bookingId);
                alertMessage.setText("Room No : " + roomNo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //sql part->adding into database
    }

    // HOME button clicked
    public void changeScreenButtonPushed(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene window = (Scene)((Node)event.getSource()).getScene();
        window.setRoot(root);
    }

    // Bill page
    public void changeBill(ActionEvent event) throws IOException
    {
        if (notice.getText().equals("")) {
            Parent root = FXMLLoader.load(getClass().getResource("customerdetails.fxml"));

            Scene window = (Scene) ((Node) event.getSource()).getScene();
            window.setRoot(root);

        }
        else
        {
            Parent root = FXMLLoader.load(getClass().getResource("generate_bill.fxml"));

            Scene window = (Scene) ((Node) event.getSource()).getScene();
            window.setRoot(root);
        }
    }
}
