package sample;

import connectivity.ConnectivityClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static sample.Main.employee_id;
import static sample.Main.deluxeCount;
import static sample.Main.deluxePrice;
import static sample.Main.roomSize;
import static sample.Main.standardCount;
import static sample.Main.standardPrice;
import static sample.Main.superiorCount;
import static sample.Main.superiorPrice;
import static sample.Main.roomNo;

public class RoomAvail implements Initializable {
    @FXML
    public Label deluxeRoomsCount;

    @FXML
    public Label superiorRoomsCount;

    @FXML
    public Label standardRoomsCount;

    @FXML
    public Label deluxePriceLabel;

    @FXML
    public Label superiorPriceLabel;

    @FXML
    public Label standardPriceLabel;

    @FXML
    public Label errorLabel;

    @FXML
    public Button bookButton;

    @FXML
    public ToggleButton deluxeButton;

    @FXML
    public ToggleButton superiorButton;

    @FXML
    public ToggleButton standardButton;


    public void initialize(URL url, ResourceBundle rb)
    {
        //setting values on the labels on ui
        deluxeRoomsCount.setText(deluxeCount+" rooms left.");
        superiorRoomsCount.setText(superiorCount+" rooms left.");
        standardRoomsCount.setText(standardCount+" rooms left.");
        deluxePriceLabel.setText("Rs."+deluxePrice);
        superiorPriceLabel.setText("Rs."+superiorPrice);
        standardPriceLabel.setText("Rs."+standardPrice);
        //setting values on the labels on ui
    }

    //returns the selected roomType
    public String getRoomType()
    {

        String roomType=null;

        if(standardButton.isSelected())
            roomType="standard";
        if(superiorButton.isSelected())
            roomType="superior";
        if(deluxeButton.isSelected())
            roomType="deluxe";

        return roomType;

    }

    //lets say he chooses deluxe,and rooms are available,then get the roomNo of one such deluxe room
    public int getFirstRoom(String roomType) throws SQLException {
        String sql="select room_no from room where "+
                "room_size='"+roomSize+"' and " +
                "room_type='"+roomType+"' and is_reserved = 0;";
        int roomNo=0;
        Connection connection=null;
        Statement statement=null;
        ResultSet resultSet=null;
        try {
            ConnectivityClass connectionClass = new ConnectivityClass();
            connection = connectionClass.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            //Ensure we start with first row
            resultSet.beforeFirst();
            resultSet.next();
            roomNo = resultSet.getInt(1);

            String sql2="UPDATE room set is_reserved = 1 where room_no = " + roomNo +";";
            statement.executeUpdate(sql2);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(resultSet!=null)
                resultSet.close();

            if(statement!=null)
                statement.close();

            if(connection!=null)
                connection.close();
        }

        return roomNo;

    }




    @FXML
    public void bookButtonClicked(ActionEvent event) throws IOException, SQLException {


        String roomType=getRoomType();
        //if he doesnt selects any of the three
        if(roomType==null)
        {
            errorLabel.setText("Please select one!!");
            return;
        }
        //if he doesnt selects any of the three

        //if the roomType he selects has no rooms available
        if( ((roomType.equals("deluxe")) && (deluxeCount==0)) || ((roomType.equals("superior")) && (superiorCount==0))
                || ((roomType.equals("standard")) && (standardCount==0)))
        {
            errorLabel.setText("Sorry that type of room is not available");
            return;
        }
        //if the roomType he selects has no rooms available

        roomNo=getFirstRoom(roomType);
        errorLabel.setText("");


        Parent root = FXMLLoader.load(getClass().getResource("customerdetails.fxml"));
        Scene window = (Scene)((Node)event.getSource()).getScene();
        window.setRoot(root);

    }


    public void changeScreenButtonPushed(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene window = (Scene)((Node)event.getSource()).getScene();
        window.setRoot(root);
    }
}
