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

    public TimecardModel() {
        try {
            this.dl = new DataLayer("kxmzgr");
            gson = new Gson();
        } catch (Exception ex) {
            System.out.println("Problem with query: " + ex.getMessage());
        } finally {
            dl.close();
        }
    }

    public String getTimecards(int emp_id) {
        Validator validator = new Validator();
        validator.employeeExists(emp_id);
        
        List<Timecard> timecards = dl.getAllTimecard(emp_id);
        validator.isEmpty(timecards);
        
        if(validator.hasFailed()) return validator.errorMessage();
        
        if (timecards.isEmpty()) {
            return "{\"error:\": \"No timecards found for employee " + emp_id + ".\"}";
        }
        return gson.toJson(timecards);
    }

    /*
        Gets a specific timecard
    */
    public String getTimecard(int timecard_id) {
        Validator validator = new Validator();
        validator.timecardExists(timecard_id);
        
        if(validator.hasFailed()) return validator.errorMessage();
        
        Timecard timecard = dl.getTimecard(timecard_id);
        return gson.toJson(timecard);
    }
    
    /*
        Create/Insert a specific timecard
    */
    public String insertTimecard(String start_time, String end_time, int empId) {
        Validator validator = new Validator();
        
        Timestamp startTime = validator.getTimestamp(start_time);
        Timestamp endTime = validator.getTimestamp(end_time);
        validator.employeeExists(empId);
        validator.validateTimecardDates(start_time, end_time);
        
        if(validator.hasFailed()) return validator.errorMessage();
        
        Timecard timecard = new Timecard(startTime, endTime, empId);
        Timecard tc = dl.insertTimecard(timecard);
        if(tc == null){
            return validator.errorMessage();
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
        Validator validator = new Validator();
        validator.timecardExists(timecardId);
        
        if(validator.hasFailed()) return validator.errorMessage();
        
          int res = dl.deleteTimecard(timecardId);
          if (res==1)
            return "{\"success:\": \"Timecard " + timecardId + " deleted.\"}";
          else  
            return validator.errorMessage();
      }


}
