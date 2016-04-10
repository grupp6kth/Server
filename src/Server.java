import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Executors;

/**
 * This class is a SSL server object
 * Creates thread for each client
 */
public class Server {
    private SSLServerSocket server;
    private SSLSocket connection;
    private int port = 6789;    //default port
    private int queueLimit = 50;    //default user queue limit
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    /**
     * Default constructor - default port and queue limit will be used
     */
    public Server(){}

    /**
     * Second constructor - uses user entered port number, but default queue
     * @param port - integer port number
     */
    public Server(int port){
        if(port > 5000)
            this.port = port;
    }

    /**
     * Third constructor - uses user entered port and queue limit
     * @param port - integer port number
     * @param queueLimit - integer client queue limit
     */
    public Server(int port, int queueLimit){
        if(port > 5000)
            this.port = port;

        if(queueLimit > 1)
            this.queueLimit = queueLimit;
    }

    /**
     * Launch server
     */
    public void launch(){
        try{
            createServerSocket();
            acceptConnections();
        }catch (Exception ex){
            OutputToConsole.printErrorMessageToConsole(ex.getMessage());
        }
    }

    private void createServerSocket() throws Exception{
        try{
            SSLServerSocketFactory factory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
            server = (SSLServerSocket)factory.createServerSocket(port, queueLimit);

            OutputToConsole.printMessageToConsole("Server started!");
        }catch(IOException ex){
            ex.printStackTrace();
            OutputToConsole.printErrorMessageToConsole("Server could not start!");
        }
    }

    private void acceptConnections() throws IOException{
        OutputToConsole.printMessageToConsole("Waiting for connections...");

        while(true){
            connection = (SSLSocket)server.accept();
            createNewClientConnection();
        }
    }

    private void createNewClientConnection(){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            public void run() {
                new ClientConnection(connection);
            }
        });
    }
}