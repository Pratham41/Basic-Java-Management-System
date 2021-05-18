package classes;
import java.sql.*;
public class Connect {
    
static PreparedStatement pst;
static String myDriver = "com.mysql.jdbc.Driver";
    
static String url = "jdbc:mysql://host/databasename?autoReconnect=true&useSSL=false";
static Connection con ;
static Statement st;
    
    
}
