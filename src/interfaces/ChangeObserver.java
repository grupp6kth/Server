package interfaces;

/**
 * This interface is a part of observer pattern that will observe device and schedule
 * changes and methods in this interface will be used to notify client about changes
 */
public interface ChangeObserver {
    /**
     * This method will be used to notify all clients that device list or status have changed
     */
    void devicesChanged();

    /**
     * This method will be user to notify all clients that schedule have changed: for example that
     * new event is added or some other is removed or changed
     */
    void scheduleChanged();
}
