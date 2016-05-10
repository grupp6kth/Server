package model;

import DTO.*;
import com.sun.deploy.util.SessionState;
import interfaces.ChangeObserver;
import util.OutputToConsole;
import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * This object handles communication with client that just connected to server
 * This object handles IO steams: receiving and sending messages using specified
 * object type - TransferObject
 */
public class ClientConnection implements ChangeObserver{
    /*private SSLSocket connection;*/
    private Socket connection;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private TelldusAPI telldusAPI = TelldusAPI.getInstance();
    private DatabaseHandler DBHandler = DatabaseHandler.getInstance();

    /**
     * Client constructors calls method for IO-stream and message receiving
     * @param connection - SSL socket to client(changed for testing purposes to usual socket)
     */
    public ClientConnection(Socket connection) {
        try{
            this.connection = connection;
            OutputToConsole.printMessageToConsole("Client " + connection.getInetAddress().getHostName() + " is connected!");
            setupIOStreams();
            waitForMessages();
        }catch (Exception ex){
            ex.printStackTrace();
            OutputToConsole.printErrorMessageToConsole(ex.getMessage());
            closeStreamsAndConnection();
        }
    }

    /**
     * Setups output and input streams
     * In case of failure prints error message and calls clean up method to end
     * thread properly
     */
    private void setupIOStreams() throws Exception{
        try{
            outputStream = new ObjectOutputStream(connection.getOutputStream());
            outputStream.flush();

            inputStream = new ObjectInputStream(connection.getInputStream());
            OutputToConsole.printMessageToConsole("IO stream are created!");
        }catch (IOException ex){
            throw new Exception("Could not create IO streams!");
        }
    }

    /**
     * This method creates new thread for receiving messages from users
     * In case of stream ending - client disconnected - call clean up method to close thread properly
     * (This method also will call a method for handling user messages)
     * (For now it all so calls sendMessage with user input - just for testing purposes)
     */
    private void waitForMessages(){
        while(true){
            try {
                ClientServerTransferObject received = (ClientServerTransferObject) inputStream.readObject();
                System.out.println("Message received!");
                new Thread(() -> handleRequest(received)).start();
            }catch(SocketException socEx){
                OutputToConsole.printErrorMessageToConsole("Connection error!");
                break;
            }catch (ClassNotFoundException CNFEx) {
                CNFEx.printStackTrace();
                OutputToConsole.printErrorMessageToConsole("Unknown receiving object type!");
            }catch (EOFException ex){
                break;
            } catch(IOException ioEx){
                OutputToConsole.printErrorMessageToConsole("Could not receive message!");
            }
        }
        closeStreamsAndConnection();
    }

    /**
     * Handles user request in another thread
     * @param request - user request
     */
    private void handleRequest(ClientServerTransferObject request){
        switch (request.getTransferType()){
            case GET:
                if(request instanceof GetDataRequest)
                    handleDataRequests((GetDataRequest) request);
            break;

            case CHANGE_DEVICE_STATUS:
                if(request instanceof ControlDevice)
                    telldusAPI.changeDeviceStatus((ControlDevice)request);
            break;

            case ADD_NEW_DEVICE:
                if(request instanceof Device)
                    System.out.println("Add new device: \nName: " + ((Device) request).getName() +
                            "\nModel: " + ((Device) request).getModel() + "\nProtocol: " + ((Device) request).getProtocol());
            break;

            case ADD_NEW_SCHEDULED_EVENT:
                if(request instanceof ScheduledEvent)
                    //DBHandler.addNewEvent();
                    System.out.println("Adding new event!");
            break;
        }
    }

    /**
     * Handles specifically request where client requires information
     * @param request - user request of type GetDataRequest
     */
    private void handleDataRequests(GetDataRequest request){
        switch (request.getType()){
            case DEVICES:
                devicesChanged();
            break;

            case SCHEDULE:
                scheduleChanged();
            break;
        }
    }

    private Devices getFakeData(){
        ArrayList<Device> devices = new ArrayList<>();
        devices.add(new Device(1, "Lamp_1", true));
        devices.add(new Device(2, "Lamp_2", false));
        devices.add(new Device(3, "Lamp_3", false));
        devices.add(new Device(4, "Lamp_4", false));
        devices.add(new Device(5, "Lamp_5", false));
        devices.add(new Device(6, "Lamp_6", true));
        devices.add(new Device(7, "Lamp_7", false));
        devices.add(new Device(8, "Lamp_8", true));
        devices.add(new Device(9, "Lamp_9", false));
        devices.add(new Device(10, "Lamp_10", false));
        devices.add(new Device(11, "Lamp_11", true));
        devices.add(new Device(12, "Lamp_12", true));
        devices.add(new Device(13, "Lamp_13", true));
        devices.add(new Device(14, "Lamp_14", false));
        devices.add(new Device(15, "Lamp_15", false));
        devices.add(new Device(16, "Lamp_16", false));
        devices.add(new Device(17, "Lamp_17", true));

        return new Devices(devices);
    }

    /**
     * Creates new thread to send message to user - so server does not wait for
     * conformation that message was send successfully
     * In case of error prints error message
     * In both cases thread terminates
     */
    private void sendMessage(ClientServerTransferObject devices){
        try {
            outputStream.writeObject(devices);
            outputStream.flush();
            OutputToConsole.printMessageToConsole("Message sent!");
        } catch (IOException ex) {
            OutputToConsole.printErrorMessageToConsole("Failed to send message!");
        }
    }

    /**
     * This method will be used to notify all clients that device list or status have changed
     */
    @Override
    public void devicesChanged() {
        ClientServerTransferObject response = getFakeData();
        response.setTransferType(ClientServerTransferObject.TransferType.DEVICE_LIST_RESPONSE);
        sendMessage(response);
    }

    /**
     * This method will be user to notify all clients that schedule have changed: for example that
     * new event is added or some other is removed or changed
     */
    @Override
    public void scheduleChanged() {
        ClientServerTransferObject response = DBHandler.getSchedule();
        response.setTransferType(ClientServerTransferObject.TransferType.SCHEDULE_RESPONSE);
        sendMessage(response);
    }

    /**
     * Clean up method - closes IO streams and connection before ending this client thread
     */
    private void closeStreamsAndConnection(){
        OutputToConsole.printMessageToConsole("Closing connection...");
        try{
            if(outputStream != null)
                outputStream.close();
            if(inputStream != null)
                inputStream.close();
            if(connection != null)
                connection.close();
            Thread.currentThread().interrupt();
        }catch (Exception ex){
            ex.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}