package model;

import DTO.Schedule;
import DTO.ScheduledEvent;
import interfaces.ChangeObserver;

import java.sql.*;
import java.util.ArrayList;

/**
 * This singleton class handles connection, requests and responses from database
 * (FOR FUTURE: CREATE CONNECTION POOL INSTEAD)
 */
public final class DatabaseHandler {
    private static DatabaseHandler serverInstance = new DatabaseHandler();
    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false";
    private final String USER = "root";
    private final String PASS = "";
    private ArrayList<ChangeObserver> changeObservers = new ArrayList<>();

    private DatabaseHandler(){}

    /**
     * @return the only instance of this class
     */
    public static DatabaseHandler getInstance(){
        return serverInstance;
    }

    /**
     * Creates connection to database
     * @return connection to database
     */
    private Connection getConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return all scheduled events as DTO.Schedule stored in database
     */
    public Schedule getSchedule(){
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String query = "SELECT * FROM scheduler.events";
        ArrayList<ScheduledEvent> schedule = new ArrayList<>();

        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while(rs.next()){
                schedule.add(new ScheduledEvent(
                        rs.getInt("deviceID"),
                        rs.getString("deviceName"),
                        rs.getString("startDateTime"),
                        rs.getString("endDateTime"),
                        rs.getString("newDeviceStatus")));
            }
        }catch (NullPointerException | SQLException ex){
            ex.printStackTrace();
        } finally {
            closeConnectionWithResultSet(conn, stmt, rs);
        }

        //schedule.add(new ScheduledEvent(3, "UlrikasLampa", "2016-09-11 11:33", null, "ON"));
        //schedule.add(new ScheduledEvent(4, "AlexandersLampa", "2016-09-11 11:53", "2016-09-11 11:55" , "OFF"));

        return new Schedule(schedule);
    }

    /**
     * Adds new change observer that shall be notified when any changes on schedule are performed
     * @param changeObserver ChangeObserver instance
     */
    public void addDeviceChangesObserver(ChangeObserver changeObserver){
        if(changeObserver != null)
            changeObservers.add(changeObserver);
    }

    /**
     * This method shall be called from all method in this class that perform changes on devices
     */
    private void notifyClientOnScheduleChange(){
        changeObservers.forEach(ChangeObserver::scheduleChanged);
    }

    /**
     * Clean up method after query execution with result set
     * @param connection - connection to database
     * @param statement - statement that was used in querying
     * @param rs - result set
     */
    private void closeConnectionWithResultSet(Connection connection, Statement statement, ResultSet rs){
        closeConnection(connection, statement);
        try{
            if(rs != null) rs.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Clean up method after query execution
     * @param connection - connection to database
     * @param statement - statement that was used in querying
     */
    private void closeConnection(Connection connection, Statement statement){
        try {
            if (connection != null) connection.close();
            if (statement != null) statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}