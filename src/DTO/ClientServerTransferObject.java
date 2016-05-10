package DTO;

import java.io.Serializable;

/**
 * This object is extended by other objects for
 * communication between client and server
 */
public class ClientServerTransferObject implements Serializable {
    private TransferType transferType;

    /**
     * Types of messaging between client and server
     */
    public enum TransferType{
        GET, CHANGE_DEVICE_STATUS, ADD_NEW_DEVICE, ADD_NEW_SCHEDULED_EVENT, DEVICE_LIST_RESPONSE, SCHEDULE_RESPONSE
    }

    /**
     * @return type of this message
     */
    public final TransferType getTransferType() {
        return transferType;
    }

    /**
     * Sets type
     * @param transferType
     */
    public final void setTransferType(TransferType transferType) {
        this.transferType = transferType;
    }
}