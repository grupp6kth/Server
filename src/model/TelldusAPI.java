package model;

import DTO.ControlDevice;
import DTO.Device;
import DTO.Devices;

import java.util.ArrayList;

public class TelldusAPI {
    private Devices deviceList;

    public void updateDeviceList() {
        ExecuteShellCommand exe = new ExecuteShellCommand();
        String deviceListRaw = exe.executeCommand("tdtool --list-devices") + " "; //tailing space for string manipulation
        // Test code for debugging
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
}
