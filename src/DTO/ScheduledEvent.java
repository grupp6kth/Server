package DTO;

/**
 * This DTO represents singular event in schedule
 */
public final class ScheduledEvent extends ClientServerTransferObject{
    private int deviceID;
    private String deviceName;
    private String startDateTime;
    private String endDateTime;
    private String newDeviceStatus;

    /**
     * Constructor initializes values
     * @param deviceID - int device id number
     * @param deviceName - String name of the device
     * @param startDateTime - String representation of start date and time when event should occur
     * @param endDateTime - String representation of ending date and time when event should stop, it can be null
     * @param newDeviceStatus - String representation ...
     */
    public ScheduledEvent(int deviceID, String deviceName, String startDateTime, String endDateTime, String newDeviceStatus) {
        this.deviceID = deviceID;
        this.deviceName = deviceName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.newDeviceStatus = newDeviceStatus;
    }

    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getNewDeviceStatus() {
        return newDeviceStatus;
    }

    public void setNewDeviceStatus(String newDeviceStatus) {
        this.newDeviceStatus = newDeviceStatus;
    }
}