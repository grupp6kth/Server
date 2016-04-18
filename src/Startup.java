import model.Server;

/**
 * Start testing SSL server
 */
public class Startup {
    public static void main(String[] args){
        System.setProperty("javax.net.ssl.keyStore", "keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");   //just for testing

        new Server(5821, 10).launch();
    }
}
