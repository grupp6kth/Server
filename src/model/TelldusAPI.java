package model;

import DTO.ControlDevice;
import DTO.Device;
import DTO.Devices;
import interfaces.ChangeObserver;

import java.util.ArrayList;

public class TelldusAPI {
    private Devices deviceList;
    private static TelldusAPI instance = null;
    private ArrayList<ChangeObserver> changeObservers = new ArrayList<>();

    private TelldusAPI() {
        // To prevent instantiation
    }

    public static TelldusAPI getInstance() {
        if (instance == null)
            instance = new TelldusAPI();

        return instance;
    }

    /**
     * Adds new change observer that shall be notified when any changes on devices are performed
     * @param changeObserver ChangeObserver instance
     */
    public void addDeviceChangesObserver(ChangeObserver changeObserver){
        if(changeObserver != null)
            changeObservers.add(changeObserver);
    }

    /**
     * This method shall be called from all method in this class that perform changes on devices
     */
    private void notifyClientOnDeviceChange(){
        changeObservers.forEach(ChangeObserver::devicesChanged);
    }

    public Devices updateDeviceList() {
        ExecuteShellCommand exe = new ExecuteShellCommand();
        String deviceListRaw = exe.executeCommand("tdtool --list-devices") + " "; //tailing space to find End of line
        //String deviceListRaw = "type=device     id=1    name=Example-device     lastsentcommand=ON type=device     id=2    name=Uttag      lastsentcommand=OFF ";

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
        deviceList = new Devices(listOfDevices);
        return deviceList;
    }

    public void changeDeviceStatus(ControlDevice request) {
        int deviceId = request.getDeviceID();
        System.out.println("Switching device with id: " + deviceId);
        Device device = deviceList.getDeviceList().get(deviceId-1);
        String newStatus;
        if(device.getStatus())
            newStatus = "off";
        else
            newStatus = "on";

        String command = "tdtool --" + newStatus + " " + deviceId;

        ExecuteShellCommand shell = new ExecuteShellCommand();
        shell.executeCommand(command);
    }

    public void learnDevice(Device device) {
        updateDeviceList();
        int newDeviceId = deviceList.getNumberOfDevices() + 1;
        device.setId(newDeviceId);

        String command = "echo 'device{\n   id=" + device.getId() +
        "\n   name=\"" + device.getName() + "\"\n   protocol=\"" + device.getProtocol() + "\"\n   model=\"" + device.getModel() +
        "\"\n   parameters{\n      house=\"A\"\n      unit=\"" + device.getId() + "\"\n   }\n}' >> /etc/tellstick.conf";

        ExecuteShellCommand shell = new ExecuteShellCommand();
        shell.executeCommand(command);
        System.out.println("Tellstick.conf updated with new device: " + device.getId());

        command = "tdtool -e " + device.getId();
        shell.executeCommand(command);
        System.out.println("Tellstick has synchronized with device: " + device.getId());
    }
}