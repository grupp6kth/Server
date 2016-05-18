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

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            public void run() {
                System.out.println("Starting");
                new ScheduleExecutor();
            }
        });

        new Server(5821, 10).launch();

    }
}