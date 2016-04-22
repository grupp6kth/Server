import DTO.Device;
import model.ExecuteShellCommand;
import model.Server;

import java.util.ArrayList;

/**
 * Start testing SSL server
 */
public class Startup {
    public static void main(String[] args){
        System.setProperty("javax.net.ssl.keyStore", "keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");   //just for testing
        updateDeviceList();
        new Server(5821, 10).launch();
    }

    private static void updateDeviceList() {
        ExecuteShellCommand exe = new ExecuteShellCommand();
        String deviceListRaw = exe.executeCommand("tdtool --list-devices") + " "; //tailing space for string manipulation
        // Test code for debugging
        deviceListRaw = "type=device     id=1    name=Example-device     lastsentcommand=ON type=device     id=2    name=Uttag      lastsentcommand=OFF ";

        ArrayList<Device> listOfDevices = new ArrayList<Device>();

        while(deviceListRaw.length() > 40) {
            //Get ID
            int idStart = deviceListRaw.indexOf("id");
            deviceListRaw= deviceListRaw.substring(idStart + 3);
            int idEnd = deviceListRaw.indexOf(" ");
            int id = Integer.parseInt(deviceListRaw.substring(0, idEnd));

            // Get name
            int nameStart = deviceListRaw.indexOf("name") + 5;
            deviceListRaw = deviceListRaw.substring(nameStart);
            int nameEnd = deviceListRaw.indexOf(" ");
            String name = deviceListRaw.substring(0, nameEnd);

            // Get lastsentcommand
            int lastSentStart = deviceListRaw.indexOf("lastsent") + 16;
            deviceListRaw= deviceListRaw.substring(lastSentStart);
            int lastSentEnd = deviceListRaw.indexOf(" ");
            String lastSent = deviceListRaw.substring(0, lastSentEnd);
            boolean status = false;
            if(lastSent.equals("ON"))
            {
                status = true;
            }

            // Create device object and add to list of devices
            Device device = new Device(id, name, status);
            listOfDevices.add(device);
        }
    }

}