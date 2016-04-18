package DTO;

/**
 * This DTO is used by client to tell server that a device
 * shall change its status - on/off
 * Client just send this object with device id
 */
public class ControlDevice extends ClientServerTransferObject{
    private int deviceID;

    /**
     * Constructor - initializes device id
     * @param deviceID - int device id
     */
    public ControlDevice(int deviceID) {
        this.deviceID = deviceID;
    }

    /**
     *
     * @return
     */
    public int getDeviceID() {
        return deviceID;
    }

    /**
     * Overwrites old device id value wit a new value
     * @param deviceID - new int device id value
     */
    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }
}