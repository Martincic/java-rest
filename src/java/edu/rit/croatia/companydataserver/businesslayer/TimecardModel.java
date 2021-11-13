package edu.rit.croatia.companydataserver.businesslayer;

import com.google.gson.Gson;
import companydata.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import companydata.Timecard;
import java.util.List;

/**
 *
 * @author sara
 */
public class TimecardModel {

    private DataLayer dl = null;
    public Gson gson = null;
    private Validator validator = null;

    public TimecardModel() {
        try {
            this.dl = new DataLayer("kxmzgr");
            gson = new Gson();
            validator = new Validator();
        } catch (Exception ex) {
            System.out.println("Problem with query: " + ex.getMessage());
        } finally {
            dl.close();
        }
    }

    public String getTimecards(int emp_id) {
        List<Timecard> timecards = dl.getAllTimecard(emp_id);
        if (timecards.isEmpty()) {
            return "{\"error:\": \"No timecards found for employee " + emp_id + ".\"}";
        }
        return gson.toJson(timecards);
    }

    /*
        Gets a specific timecard
    */
    public String getTimecard(int timecard_id) {
        Timecard timecard = dl.getTimecard(timecard_id);
        if (timecard == null) {
            return "{\"error:\": \"No timecard found for timecardID: " + timecard_id + ".\"}";
        }
        return gson.toJson(timecard);
    }
    
    /*
        Create/Insert a specific timecard
    */
    public String insertTimecard(String start_time, String end_time, int empId) {
        Timestamp startTime, endTime;

        try{
            startTime = new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(start_time).getTime());
            endTime = new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(end_time).getTime());  
        }catch(java.text.ParseException pe){
            return "{\"error:\": \"please write time in format yyyy-MM-dd HH:mm:ss.\"}";
        }
        
        Timecard timecard = new Timecard(startTime, endTime, empId);
        Timecard tc = dl.insertTimecard(timecard);
        if(tc == null){
            return "{\"error:\": \"Can't add new timecard for employee id: " + empId + ", start time: " + startTime + ", end time: " + endTime + ".\"}";
        } else {
            return gson.toJson(tc);
        }
    }

    /*
        Updates a specific timecard
    */
      public String updateTimecard(String tc){
          Timecard timecard = gson.fromJson(tc, Timecard.class);
          dl.updateTimecard(timecard);
          return tc;
       }

    /*
        Deletes a specific timecard
    */
      public String deleteTimecard(int timecardId){
          int res = dl.deleteTimecard(timecardId);
          if (res==1)
            return "{\"success:\": \"Timecard " + timecardId + " deleted.\"}";
          else  
            return "{\"error:\": \"can't delete timecard " + timecardId + ".\"}";
      }


}
