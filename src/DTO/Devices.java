package DTO;

import java.util.ArrayList;

/**
 * This DTO contains ArrayList of DTO.Device objects
 * and used as return object for users getAllDevices request
 */
public final class Devices extends ClientServerTransferObject{
    private ArrayList<Device> devices;

    /**
     * Constructor sets new devices value
     * @param devices - ArrayList of Device objects
     */
    public Devices(ArrayList<Device> devices) {
        this.devices = devices;
    }

    /**
     * @return ArrayList of Device objects
     */
    public final ArrayList<Device> getDeviceList() {
        return devices;
    }

    /**
     * Overwrites old devices value with a new one
     * @param devices - new ArrayList of Device objects
     */
    public final void setDevices(ArrayList<Device> devices) {
        this.devices = devices;
    }

    /**
     * @return int - number of devices
     */
    public final int getNumberOfDevices(){
        return devices.size();
    }

    /**
     * @return String representation of this object
     */
    @Override
    public final String toString(){
        StringBuilder string = new StringBuilder("");

        for (Device device : devices)
            string.append(device.toString()).append("\n");

        return string.toString();
    }
}
