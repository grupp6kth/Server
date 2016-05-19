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
        String query = "SELECT * FROM scheduler.events ORDER BY startDateTime";
        ArrayList<ScheduledEvent> schedule = new ArrayList<>();

        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while(rs.next()){
                schedule.add(new ScheduledEvent(
                        rs.getInt("deviceID"),
                        rs.getString("deviceName"),
                        rs.getString("startDateTime").substring(0,16),
                        rs.getString("endDateTime"),
                        rs.getString("newDeviceStatus")));
            }
        }catch (NullPointerException | SQLException ex){
            ex.printStackTrace();
        } finally {
            closeConnectionWithResultSet(conn, stmt, rs);
        }

        return new Schedule(schedule);
    }

    /**
     * Sets startdate to enddate and inverts status, sets enddate to null
     */
    public void modifyEvent(ScheduledEvent scheduledEvent){
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String query = "SELECT * FROM scheduler.events ORDER BY startDateTime asc limit 1";

        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            if(rs.next()){
                if (rs.getString("newDeviceStatus").equals("ON")){
                    query = "UPDATE scheduler.events SET startDateTime='" + rs.getString("endDateTime") + "', endDateTime=NULL, newDeviceStatus='OFF' WHERE (deviceID, startdateTime) = (" + scheduledEvent.getDeviceID() + ", '" + scheduledEvent.getStartDateTime() +"')";
                }
                else{
                    query = "UPDATE scheduler.events SET startDateTime='" + rs.getString("endDateTime") + "', endDateTime=NULL, newDeviceStatus='ON' WHERE (deviceID, startdateTime) = (" + scheduledEvent.getDeviceID() + ", '" + scheduledEvent.getStartDateTime() +"')";
                }
                stmt.executeUpdate(query);
            }
        }catch (NullPointerException | SQLException ex){
            ex.printStackTrace();
        } finally {
            closeConnectionWithResultSet(conn, stmt, rs);
        }

        notifyClientOnScheduleChange();
    }

    /**
     * Insert new event
     */
    public void insertEvent(ScheduledEvent scheduledEvent){
        Connection conn = getConnection();
        Statement stmt = null;
        String query;
        if (scheduledEvent.getEndDateTime() != null) {
            query = "INSERT INTO scheduler.events " + "VALUES ("
                    + scheduledEvent.getDeviceID() + ", '"
                    + scheduledEvent.getDeviceName() + "', '"
                    + scheduledEvent.getStartDateTime() + "', '"
                    + scheduledEvent.getEndDateTime() + "', '"
                    + scheduledEvent.getNewDeviceStatus() + "')";
        }
        else{
            query = "INSERT INTO scheduler.events " + "VALUES ("
                    + scheduledEvent.getDeviceID() + ", '"
                    + scheduledEvent.getDeviceName() + "', '"
                    + scheduledEvent.getStartDateTime() + "', NULL, '"
                    + scheduledEvent.getNewDeviceStatus() + "')";
        }

        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(query);

        }catch (NullPointerException | SQLException ex){
            ex.printStackTrace();
        }finally {
            closeConnection(conn, stmt);
        }

        System.out.println("New event inserted!");
        notifyClientOnScheduleChange();
    }

    /**
     * Remove event
     */
    public void removeEvent(ScheduledEvent scheduledEvent){
        Connection conn = getConnection();
        Statement stmt = null;
        String query = "DELETE FROM scheduler.events WHERE (deviceID, startDateTime) = (" + scheduledEvent.getDeviceID() + ", '" + scheduledEvent.getStartDateTime() + "')";

        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(query);

        }catch (NullPointerException | SQLException ex){
            ex.printStackTrace();
        }finally {
            closeConnection(conn, stmt);
        }

        notifyClientOnScheduleChange();
    }

    /**
     * @return earliestStartDate from events table.
     *
     * Not really req since getEarliestEvent can get this info aswell.
     */
    public String getEarliestStartDate(){
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String query = "SELECT startDateTime FROM scheduler.events ORDER BY startDateTime asc limit 1";
        String earliestStartDate = "";
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            if(rs.next()){
                earliestStartDate = rs.getString("startDateTime");
            }
            rs.close();

        }catch (NullPointerException | SQLException ex){
            ex.printStackTrace();
        }finally {
            closeConnectionWithResultSet(conn, stmt, rs);
        }

        return earliestStartDate.substring(0,16);
    }


    /**
     * @return earliestEvent by startdatetime from events table.
     */
    public ScheduledEvent getEarliestEvent(){
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String query = "SELECT * FROM scheduler.events ORDER BY startDateTime asc limit 1";
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            if(rs.next()){

                ScheduledEvent event = new ScheduledEvent(
                        rs.getInt("deviceID"),
                        rs.getString("deviceName"),
                        rs.getString("startDateTime"),
                        rs.getString("endDateTime"),
                        rs.getString("newDeviceStatus"));
                rs.close();
                return event;
            }
        }
        catch (NullPointerException | SQLException ex){
            ex.printStackTrace();
        }finally {
            closeConnectionWithResultSet(conn, stmt, rs);
        }
        //returns event with nulls if object is empty. Solution?
        //return new ScheduledEvent(404, null, null, null, null);
        return null;
    }

    /**
     * @return earliestEndDate from events table
     * substring used at end to get format yyyy-mm-dd hh-mm
     */
    public String getEarliestEndDate(){
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String query = "SELECT endDateTime FROM scheduler.events ORDER BY endDateTime asc limit 1";
        String earliestStartDate = "";
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            if(rs.next()){
                earliestStartDate = rs.getString("endDateTime");
            }
            rs.close();

        }catch (NullPointerException | SQLException ex){
            ex.printStackTrace();
        }finally {
            closeConnectionWithResultSet(conn, stmt, rs);
        }

        return earliestStartDate.substring(0,16);
    }

    /**
     * Adds new change observer that shall be notified when any changes on schedule are performed
     * @param changeObserver ChangeObserver reference
     */
    public void addScheduleChangesObserver(ChangeObserver changeObserver){
        if(changeObserver != null)
            changeObservers.add(changeObserver);
    }

    /**
     * Removes observer from list of observers
     * @param changeObserver - ChangeObserver reference
     */
    public void removeScheduleChangesObserver(ChangeObserver changeObserver){
        if(changeObserver != null)
            changeObservers.remove(changeObserver);
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
