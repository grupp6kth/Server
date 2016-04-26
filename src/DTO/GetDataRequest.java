package DTO;

/**
 * This DTO is used by client to send different request to server to get some data
 * String field dataType is used to tell which type of data client require (temporary)
 * Types of requests: devices, schedule
 */
public class GetDataRequest extends ClientServerTransferObject{
    private String dataType;

    /**
     * Constructor - initializes data type
     * @param type - String type
     */
    public GetDataRequest(String type){
        this.dataType = type;
    }

    /**
     * @return String data type
     */
    public String getType() {
        return dataType;
    }

    /**
     * Overwrites old dataType value with a new one
     * @param dataType - new String dataType value
     */
    public void setType(String dataType) {
        this.dataType = dataType;
    }
}