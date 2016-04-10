import javax.net.ssl.SSLSocket;
import java.io.*;
import java.util.concurrent.Executors;

public class ClientConnection {
    private SSLSocket connection;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private TransferObject response = new TransferObject("");

    /**
     * Client constructors call method for IO-stream and creates thread
     * for message receiving
     * @param connection - socket to client
     */
    public ClientConnection(SSLSocket connection) {
        this.connection = connection;
        OutputToConsole.printMessageToConsole("Client " + connection.getInetAddress().getHostName() + " is connected!");

        try{
            setupIOStreams();
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                public void run() {
                    waitForMessages();
                    closeStreamsAndConnection();
                }
            });
        }catch (Exception ex){
            OutputToConsole.printMessageToConsole(ex.getMessage());
        }
    }

    private void setupIOStreams() throws IOException{
        outputStream = new ObjectOutputStream(connection.getOutputStream());
        inputStream = new ObjectInputStream(connection.getInputStream());

    }

    private void waitForMessages(){
        while(true){
            try{
                TransferObject received = (TransferObject)inputStream.readObject();
                OutputToConsole.printMessageToConsole("Message received: " + received.getMessage());

                response.setMessage(received.getMessage());
                sendMessage();
            }catch (ClassNotFoundException CNFEx){
                CNFEx.printStackTrace();
            }catch (Exception ex){
                break;
            }
        }
    }

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

    private void closeStreamsAndConnection(){
        OutputToConsole.printMessageToConsole("Closing connection...");
        try{
            outputStream.close();
            inputStream.close();
            connection.close();
        }catch (IOException ieEx){
            ieEx.printStackTrace();
        }
    }
}