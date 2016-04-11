import javax.net.ssl.SSLSocket;
import java.io.*;
import java.util.concurrent.Executors;

/**
 * This object handles communication with client that just connected to server
 * This object handles IO steams: receiving and sending messages using specified
 * object type - TransferObject
 */
public class ClientConnection {
    private SSLSocket connection;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private TransferObject response = new TransferObject("");

    /**
     * Client constructors calls method for IO-stream and message receiving
     * @param connection - SSL socket to client
     */
    public ClientConnection(SSLSocket connection) {
        this.connection = connection;
        OutputToConsole.printMessageToConsole("Client " + connection.getInetAddress().getHostName() + " is connected!");
        setupIOStreams();
        waitForMessages();
    }

    /**
     * Setups output and input streams
     * In case of failure prints error message and calls clean up method to end
     * thread properly
     */
    private void setupIOStreams(){
        try{
            outputStream = new ObjectOutputStream(connection.getOutputStream());
            outputStream.flush();

            inputStream = new ObjectInputStream(connection.getInputStream());
            OutputToConsole.printMessageToConsole("IO stream are created!");
        }catch (IOException ex){
            OutputToConsole.printErrorMessageToConsole("Could not create IO streams!");
            closeStreamsAndConnection();
        }
    }

    /**
     * This method creates new thread for receiving messages from users
     * In case of stream ending - client disconnected - call clean up method to close thread properly
     * (This method also will call a method for handling user messages)
     * (For now it all so calls sendMessage with user input - just for testing purposes)
     */
    private void waitForMessages(){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            public void run() {
            while(true){
                try{
                    TransferObject received = (TransferObject)inputStream.readObject();
                    OutputToConsole.printMessageToConsole("Message received: " + received.getMessage());

                    response.setMessage(received.getMessage());
                    sendMessage();
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
        });

    }

    /**
     * Creates new thread to send message to user - so server does not wait for
     * conformation that message was send successfully
     * In case of error prints error message
     * In both cases thread terminates
     */
    private void sendMessage(){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            public void run() {
                try {
                    outputStream.writeObject(response);
                    outputStream.flush();
                    OutputToConsole.printMessageToConsole("Message sent!");
                } catch (IOException ex) {
                    OutputToConsole.printErrorMessageToConsole("Failed to send message!");
                }
            }
        });
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