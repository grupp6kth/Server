package model;


import DTO.ControlDevice;
import DTO.Schedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class will execute scheduled events
 */
public class ScheduleExecutor {
    private TelldusAPI telldusAPI = TelldusAPI.getInstance();
    private DatabaseHandler DBHandler = DatabaseHandler.getInstance();

    public ScheduleExecutor(){
        System.out.println("Starting executor..");
        executeEvent();
    }

    /**
     * Executes event with telldusAPI and SQL queries with DBhandler.
     */
    public void executeEvent(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (eventTimePassed(DBHandler.getEarliestEvent().getStartDateTime()) && (DBHandler.getEarliestEvent().getEndDateTime() == null)) {
                    telldusAPI.changeDeviceStatus(new ControlDevice(DBHandler.getEarliestEvent().getDeviceID())); //only switches devices.
                    DBHandler.removeEvent(DBHandler.getEarliestEvent());  //remove event from DB after execution

                } else if (eventTimePassed(DBHandler.getEarliestEvent().getStartDateTime()) && (DBHandler.getEarliestEvent().getEndDateTime() != null)) {
                    telldusAPI.changeDeviceStatus(new ControlDevice(DBHandler.getEarliestEvent().getDeviceID())); //only switches devices.
                    DBHandler.modifyEvent(DBHandler.getEarliestEvent()); //modify current event
                }
            }
        }, 1000, 1000);
    }


    /**
     * @param eventTime time of event with format yyyy-MM-dd HH:mm
     * @return true if currentTime >= eventTime (If eventTime has passed), else false.
     */
    private boolean eventTimePassed (String eventTime){
        String currentTime = new SimpleDateFormat("yyyyMMddHHmm").format(Calendar.getInstance().getTime());
        eventTime = eventTime.substring(0,4).concat(eventTime.substring(5,7)).concat(eventTime.substring(8,10)).concat(eventTime.substring(11,13)).concat(eventTime.substring(14,16));

        if(Long.parseLong(currentTime) >= Long.parseLong(eventTime)){
            return true;
        }
        else {
            return false;
        }

    }

}
