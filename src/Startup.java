/**
 * Start testing SSL server
 */
public class Startup {
    public static void main(String[] args){
        System.setProperty("javax.net.ssl.keyStore", "keyStoreTest.jks");   //testing - change later
        System.setProperty("javax.net.ssl.keyStorePassword", "password");   //testing - change later

        new Server(5821, 100).launch();
    }
}
