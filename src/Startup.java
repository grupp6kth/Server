import model.ScheduleExecutor;
import model.Server;

import java.util.concurrent.Executors;

/**
 * Start testing SSL server
 */
public class Startup {
    public static void main(String[] args){
        //System.setProperty("javax.net.ssl.keyStore", "keystore.jks");
        //System.setProperty("javax.net.ssl.keyStorePassword", "password");   //just for testing

        new Server(5821, 10).launch();

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            public void run() {
                new ScheduleExecutor();
            }
        });
    }
}