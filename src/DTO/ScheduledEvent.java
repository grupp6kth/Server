package DTO;

/**
 * This DTO represents singular event in schedule
 */
public final class ScheduledEvent extends ClientServerTransferObject{
    private int deviceID;
    private String deviceName;
    private String scheduleDate;
    private String scheduleDescription;

    /**
     * Constructor initializes values
     * @param deviceID - int device id number
     * @param deviceName - String name of the device
     * @param scheduleDate - String representation of a date when event is scheduled to happen
     * @param scheduleDescription - String representation ...
     */
    public ScheduledEvent(int deviceID, String deviceName, String scheduleDate, String scheduleDescription) {
        this.deviceID = deviceID;
        this.deviceName = deviceName;
        this.scheduleDate = scheduleDate;
        this.scheduleDescription = scheduleDescription;
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

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getScheduleDescription() {
        return scheduleDescription;
    }

    public void setScheduleDescription(String scheduleDescription) {
        this.scheduleDescription = scheduleDescription;
    }
}
