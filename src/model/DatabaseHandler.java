package model;

import DTO.Schedule;
import DTO.ScheduledEvent;

import java.util.ArrayList;

/**
 * This singleton class handles connection, requests and responses from database
 */
public class DatabaseHandler {
    private static DatabaseHandler serverInstance = new DatabaseHandler();

    private DatabaseHandler(){
        //connect here
    }

    /**
     * @return the only instance of this class
     */
    public static DatabaseHandler getInstance(){
        return serverInstance;
    }

    /**
     * @return all scheduled events as DTO.Schedule
     */
    public Schedule getSchedule(){
        ArrayList<ScheduledEvent> schedule = new ArrayList<>();
        schedule.add(new ScheduledEvent(3, "UlrikasLampa", "2016-09-11 11:33", "TÄND!!!!"));
        schedule.add(new ScheduledEvent(3, "AlexandersLampa", "2016-09-11 11:53", "SLÄCK!!!!"));
        return new Schedule(schedule);
    }
}