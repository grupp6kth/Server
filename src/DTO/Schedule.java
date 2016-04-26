package DTO;

import java.util.ArrayList;

/**
 * This DTO is created to send whole schedule between server and client
 */
public final class Schedule extends ClientServerTransferObject{
    private ArrayList<ScheduledEvent> schedule = new ArrayList<>();

    /**
     * Constructor sets new value of schedule when object is created
     * @param schedule - ArrayList of DTO.ScheduledEvent
     */
    public Schedule(ArrayList<ScheduledEvent> schedule) {
        this.schedule = schedule;
    }

    /**
      * @return schedule as ArrayList
     */
    public ArrayList<ScheduledEvent> getSchedule() {
        return schedule;
    }

    /**
     * Overwrites old schedule with a new one
     * @param schedule - new ArrayList schedule
     */
    public void setSchedule(ArrayList<ScheduledEvent> schedule) {
        this.schedule = schedule;
    }
}
