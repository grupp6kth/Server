package DTO;

/**
 * This DTO is used by client to send different request to server to get some data
 * String field dataType is used to tell which type of data client require (temporary)
 * Types of requests: devices, schedule
 */
public final class GetDataRequest extends ClientServerTransferObject{
    private RequestTypes getRequestTypes;

    /**
     * Type of requests available
     */
    public enum RequestTypes{
        DEVICES, SCHEDULE
    }

    /**
     * Constructor - initializes data type
     * @param type - one of types existing in RequestTypes
     */
    public GetDataRequest(RequestTypes type){
        this.getRequestTypes = type;
    }

    /**
     * @return RequestTypes data type
     */
    public RequestTypes getType() {
        return getRequestTypes;
    }

    /**
     * Overwrites old dataType value with a new one
     * @param dataType - new RequestTypes dataType value
     */
    public void setType(RequestTypes dataType) {
        this.getRequestTypes = dataType;
    }
}