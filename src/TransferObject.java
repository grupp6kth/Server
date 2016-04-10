import java.io.Serializable;

public class TransferObject implements Serializable {
    private String message;

    public TransferObject(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}