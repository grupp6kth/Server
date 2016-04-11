import java.io.Serializable;

/**
 * This simple DTO is used as communications object for server-client communication
 */
public class TransferObject implements Serializable {
    private String message;

    /**
     * Constructor initializes variables on creation
     * @param message - String variable that contains a message
     */
    public TransferObject(String message) {
        this.message = message;
    }

    /**
     * Retrieves message
     * @return String message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets new message - overwrites old message
     * @param message - String message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}