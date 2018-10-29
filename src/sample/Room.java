package sample;

import connectivity.ConnectivityClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import static sample.Main.employee_id;
import static sample.Main.deluxeCount;
import static sample.Main.deluxePrice;
import static sample.Main.roomSize;
import static sample.Main.standardCount;
import static sample.Main.standardPrice;
import static sample.Main.superiorCount;
import static sample.Main.superiorPrice;
import static sample.Main.chkout;
import static sample.Main.chkin;
import static sample.Main.daysCount;

public class Room  implements Initializable{

    //simply a map which maps "5000"->5000,...
    public HashMap<String,Integer> budget;

    //id's of all the buttons,labels seen in UI
    @FXML
    public ComboBox minBudget;
    @FXML
    public ComboBox maxBudget;
    @FXML
    public ToggleButton bhk1;
    @FXML
    public ToggleButton bhk2;
    @FXML
    public ToggleButton bhk3;
    @FXML
    public Label notice;

    public DatePicker checkin;
    @FXML
    public DatePicker checkout;

    //id's of all the buttons,labels seen in UI


    //employeeId of the person logged in,and who is booking
    public int employeeId;
    //employeeId of the person logged in,and who is booking

    //in ui,these pop down,when we click min Budget,max Budget
    ObservableList<String> budgetList=FXCollections.observableArrayList("5000","10000","15000","20000");
    //in ui,these pop down,when we click min Budget,max Budget


    //this function used by HomePageForReceptionController,it passes the employee_id of the person who logs in to this

    //this function used by HomePageForReceptionController,it passes the employee_id of the person who logs in to this


    Boolean reload=false;
    //first method to be called

    public void initialize(URL url, ResourceBundle rb)
    {
        employeeId=employee_id;
        minBudget.setItems(budgetList);
        maxBudget.setItems(budgetList);

        budget = new HashMap<>();
        budget.put("5000",5000);
        budget.put("10000",10000);
        budget.put("15000",15000);
        budget.put("20000",20000);

        DatePicker checkInDatePicker = checkin;
        DatePicker checkOutDatePicker = checkout;

        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (checkInDatePicker.getValue()!=null)
                                {
                                    if (item.isBefore(
                                            checkInDatePicker.getValue().plusDays(1))
                                    ) {
                                        setDisable(true);
                                        setStyle("-fx-background-color: #ffc0cb;");
                                    }
                                }
                                else
                                {
                                    if (true
                                    ) {
                                        setDisable(true);
                                        setStyle("-fx-background-color: #ffc0cb;");
                                    }
                                }

                            }
                        };
                    }
                };
        final Callback<DatePicker, DateCell> dayCellFactory2 =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item.isBefore(
                                        LocalDate.now() )
                                ) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }

                            }
                        };
                    }
                };
        checkInDatePicker.setDayCellFactory(dayCellFactory2);
        checkOutDatePicker.setDayCellFactory(dayCellFactory);

    }

    //first method to be called
    public void setBhk1()
    {
        bhk2.setSelected(false);
        bhk3.setSelected(false);
    }

    public void setBhk2()
    {
        bhk1.setSelected(false);
        bhk3.setSelected(false);
    }

    public void setBhk3()
    {
        bhk2.setSelected(false);
        bhk1.setSelected(false);
    }



    //returns the selected roomSize
    public String getRoomSize()
    {
        String roomSize=null;


        if(bhk1.isSelected())
        {
            roomSize="1";
            reload=false;
        }
        else if(bhk2.isSelected())
        {
            roomSize="2";
            reload=false;
        }
        else if(bhk3.isSelected())
        {
            roomSize="3";
            reload=false;
        }
        else
        {
            notice.setText("Select a room.");
            reload=true;

        }

        if (checkin.getValue()!=null)
        {
            chkin=checkin.getValue().toString();

            System.out.println(chkin);
        }
        else
        {
            //System.out.println("Caught");
            notice.setText("Select date ");
            reload=true;
        }




        if (checkout.getValue()!=null)
        {
            chkout=checkout.getValue().toString();
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Date date1 = myFormat.parse(chkin);
                Date date2 = myFormat.parse(chkout);
                long diff = date2.getTime() - date1.getTime();

                daysCount= (int) (diff/(1000*60*60*24));
                System.out.println(daysCount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(chkout);
        }
        else
        {
            //System.out.println("Caught");
            notice.setText("Select date ");
            reload=true;
        }
        //LocalDate chkout = checkout.getValue();

        return roomSize;

    }


    //returns the selected roomSize

    //returns the selected roomType

    //returns the selected roomType

    //returns the number of room of type specified by sql query,like 1BHK deluxe,which are not reserved
    public int countRooms(String sql) throws SQLException {
        Connection connection=null;
        Statement statement=null;
        ResultSet resultSet=null;
        int roomCount=0;
        try {
            ConnectivityClass connectionClass = new ConnectivityClass();
            connection = connectionClass.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            //Ensure we start with first row
            resultSet.beforeFirst();
            resultSet.next();
            roomCount = resultSet.getInt(1);
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

        return roomCount;

    }
    //returns the number of room of type specified by sql query,like 1BHK deluxe,which are not reserved

    //returns the price of a given type of room
    public float roomPrice(String sql) throws SQLException {
        Connection connection=null;
        Statement statement=null;
        ResultSet resultSet=null;
        float price=0;
        try {
            ConnectivityClass connectionClass = new ConnectivityClass();
            connection = connectionClass.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            //Ensure we start with first row
            resultSet.beforeFirst();
            resultSet.next();
            price = resultSet.getFloat(1);
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

        return price;

    }
    //returns the price of a given type of room


    //when searchButton clicked,we find the number of such available rooms,and their price and pass it
    //to RoomAvailabilityPageController
    @FXML
    public void searchResult(ActionEvent event) throws IOException, SQLException {




        String sqlDeluxeCount=null,sqlSuperiorCount=null,sqlStandardCount=null;
        String sqlDeluxePrice=null,sqlSuperiorPrice=null,sqlStandardPrice=null;


        roomSize=getRoomSize();

        if (reload==true)
        {
            /*
            Parent root = FXMLLoader.load(getClass().getResource("room.fxml"));
            Scene window = (Scene)((Node)event.getSource()).getScene();
            window.setRoot(root);
            */
            return ;
        }

        int mn_budget;
        int mx_budget;

        // ensure that deafult values in dropdown list is 5000 or else if unselected NullPointerException is thrown (hence, inside try-catch)
        try{
            mn_budget=budget.get(minBudget.getValue());
            //System.out.println(budget.get(minBudget.getValue()));

        }
        catch(Exception e)
        {
            System.out.println("Caught");
            mn_budget=5000;

        }
        try{

            //System.out.println(budget.get(minBudget.getValue()));
            mx_budget=budget.get(maxBudget.getValue());
        }
        catch(Exception e)
        {
            System.out.println("Caught");

            mx_budget=5000;
        }
        System.out.println(mn_budget);
        System.out.println(mx_budget);
        mn_budget=mn_budget/daysCount;
        mx_budget=mx_budget/daysCount;
        System.out.println(mx_budget);
        sqlDeluxeCount="select count(*) from room natural join room_price where " +
                "price>="+mn_budget+" and " +
                "price<="+mx_budget+" and room_size='"+roomSize+"' and " +
                "room_type='deluxe' and is_reserved = 0;";
        sqlSuperiorCount="select count(*) from room natural join room_price where " +
                "price>="+mn_budget+" and " +
                "price<="+mx_budget+" and room_size='"+roomSize+"' and " +
                "room_type='superior' and is_reserved = 0;";
        sqlStandardCount="select count(*) from room natural join room_price where " +
                "price>="+mn_budget+" and " +
                "price<="+mx_budget+" and room_size='"+roomSize+"' and " +
                "room_type='standard' and is_reserved = 0;";

        sqlDeluxePrice="select price from room_price where room_size='"+roomSize+"' and " +
                "room_type='deluxe'";
        sqlSuperiorPrice="select price from room_price where room_size='"+roomSize+"' and " +
                "room_type='superior'";
        sqlStandardPrice="select price from room_price where room_size='"+roomSize+"' and " +
                "room_type='standard'";


        deluxeCount=countRooms(sqlDeluxeCount);
        superiorCount=countRooms(sqlSuperiorCount);
        standardCount=countRooms(sqlStandardCount);

        deluxePrice=roomPrice(sqlDeluxePrice);
        superiorPrice=roomPrice(sqlSuperiorPrice);
        standardPrice=roomPrice(sqlStandardPrice);

        //System.out.println(deluxePrice);

        Parent root = FXMLLoader.load(getClass().getResource("room_avail.fxml"));
        Scene window = (Scene)((Node)event.getSource()).getScene();
        window.setRoot(root);




    }

    //home page
    public void changeScreenButtonPushed(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene window = (Scene)((Node)event.getSource()).getScene();
        window.setRoot(root);
    }

    // Bill page
    public void changeBill(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("generate_bill.fxml"));
        Scene window = (Scene)((Node)event.getSource()).getScene();
        window.setRoot(root);
    }


}
