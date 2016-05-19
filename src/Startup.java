import model.ScheduleExecutor;
import model.Server;
import model.TelldusAPI;
import model.ExecuteShellCommand;
import java.util.concurrent.Executors;
import DTO.Device;
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

	//ExecuteShellCommand command = new ExecuteShellCommand();
	//command.appendToEndOfFile("/etc/tellstick2.conf", "hej hej");

	}
}
