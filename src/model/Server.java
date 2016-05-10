package model;

import util.OutputToConsole;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * SSL server (not SSL anymore)
 * This server users SSL sockets to communicate with user
 * For every user new thread i created
 */
public class Server {
    /*private SSLServerSocket server;
    private SSLSocket connection;*/
    private ServerSocket server;
    private Socket connection;
    private int port = 6789;    //default port
    private int queueLimit = 10;    //default user queue limit
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
     * Launches server - start socket creation and start method for accepting connections
     */
    public void launch(){
        createServerSocket();
        acceptConnections();
    }

    /**
     * Creates SSL Server socket (changed for testing purposes to usual server socket)
     * In case of exception prints error message and closes program
     */
    private void createServerSocket(){
        try{
            /*SSLServerSocketFactory factory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
            server = (SSLServerSocket)factory.createServerSocket(port, queueLimit);*/

            ServerSocketFactory factory = ServerSocketFactory.getDefault();
            server = factory.createServerSocket(port, queueLimit);
            //server.setNeedClientAuth(true);

            OutputToConsole.printMessageToConsole("Server started!");
        }catch(IOException ex){
            ex.printStackTrace();
            OutputToConsole.printErrorMessageToConsole("Server could not start!");
            System.exit(0);
        }
    }

    /**
     * Accept connecting clients
     * If fails - prints error and wait for a new one
     */
    private void acceptConnections(){
        OutputToConsole.printMessageToConsole("Waiting for connections...");

        while(true){
            try {
                /*connection = (SSLSocket)server.accept();*/
                connection = server.accept();
                createNewClientConnection();
            } catch (IOException ex) {
                OutputToConsole.printErrorMessageToConsole("Could not accept client!");
            }
        }
    }

    /**
     * Creates new thread that will handle connected client
     */
    private void createNewClientConnection(){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            public void run() {
                new ClientConnection(connection);
            }
        });
    }
}