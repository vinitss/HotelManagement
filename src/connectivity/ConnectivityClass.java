package connectivity;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectivityClass {
    public Connection connection;
    public  Connection getConnection(){

        /* ignore these things */
        //String dbName="javafx";  //tutorial
        //String userName="javafx";  //root

        //String dbName="id6992877_javafx";  //tutorial
        //String userName="id6992877_project";  //root


        String dbName="tutorial";
        String userName="user101";
        String password="password";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbName,userName,password);

            /*ignore these things */
            /*String host = "jdbc:mysql://"
                    + System.getenv().get("$145.14.144.34")
                    + ":"
                    + System.getenv().get("$3306");*/
            //connection= DriverManager.getConnection("jdbc:mysql://wwwjavafxcom.000webhostapp.com:3306/id6992877_javafx","id6992877_project","wolverine");
            //connection= DriverManager.getConnection("jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12254864","sql12254864","xzK1QEMsGS");



        } catch (Exception e) {
            e.printStackTrace();
        }


        return connection;
    }

}
