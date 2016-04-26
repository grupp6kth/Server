package DTO;

/**
 * This DTO contains information about a device
 * This object contains device id, device name and device status (on/off == true/false),
 * model(for example "selflearning-switch") and protocol (for example "arctech")
 */
public class Device extends ClientServerTransferObject{
    private int id;
    private String name;
    private String protocol;
    private String model;
    private boolean status;

    /**
     * First constructor for a Device
     * @param id - int device id
     * @param name - String device name
     * @param status - byte device status: true - device off, false - device on
     */
    public Device(int id, String name, boolean status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    /**
     * Seconds constructor for a Device
     * @param name - String device name
     * @param protocol - String device protocol (for example "arctech")
     * @param model - String device model (for example "selflearning-switch")
     */
    public Device(String name, String protocol, String model) {
        this.name = name;
        this.protocol = protocol;
        this.model = model;
    }

    /**
     * Third constructor for a Device
     * @param id - int device id
     * @param name - String device name
     * @param protocol - String device protocol (for example "arctech")
     * @param model - String device model (for example "selflearning-switch")
     * @param status  - byte device status: true - device off, false - device on
     */
    public Device(int id, String name, String protocol, String model, boolean status) {
        this.id = id;
        this.name = name;
        this.protocol = protocol;
        this.model = model;
        this.status = status;
    }

    /**
     * @return int device id
     */
    public int getId() {
        return id;
    }

    /**
     * Overwrites old id value with a new one
     * @param id - new int device id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return String device name
     */
    public String getName() {
        return name;
    }

    /**
     * Overwrites old name value with a new one
     * @param name - new String device name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return byte device status (on/off)
     */
    public boolean getStatus() {
        return status;
    }

    /**
     * Overwrites old status value with a new one
     * @param status - new byte device status (on/off)
     */
    public void setStatus(boolean status) {
        this.status = status;
    }


    /**
     * @return String value of a model
     */
    public String getModel() {
        return model;
    }

    /**
     * Overwrites model value with a new one
     * @param model - String new model value
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return String value of Device protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Overwrites protocol value with a new one
     * @param protocol - String new protocol value
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * @return String representation of this DTO
     */
    public String toString(){
        return "DeviceID : " + id +
                " Device Name: " + name +
                " Model " + model +
                " Protocol: " + protocol +
                " Status: "  + status;
    }
}