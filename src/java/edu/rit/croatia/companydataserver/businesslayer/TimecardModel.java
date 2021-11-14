package edu.rit.croatia.companydataserver.businesslayer;

import com.google.gson.Gson;
import companydata.*;
import java.sql.Timestamp;
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

        validator.validateTimecardDates(startTime, endTime, empId);

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
        TimecardJson request = gson.fromJson(tc, TimecardJson.class);
        Validator validator = new Validator();
        
        Timestamp start_time = validator.getTimestamp(request.start_time);
        Timestamp end_time = validator.getTimestamp(request.end_time);
        validator.employeeExists(request.emp_id);
        validator.timecardExists(request.timecard_id);

        validator.validateTimecardDates(start_time, end_time, request.emp_id);

        if(validator.hasFailed()) return validator.errorMessage();
        
        Timecard from_request = new Timecard(request.timecard_id, start_time, end_time, request.emp_id);
        
        return gson.toJson(dl.updateTimecard(from_request));
       }

    /*
        Deletes a specific timecard
    */
      public String deleteTimecard(int timecardId){
        Validator validator = new Validator();
        validator.timecardExists(timecardId);
        
        if(validator.hasFailed()) return validator.errorMessage();
        
        dl.deleteTimecard(timecardId);
        return "{\"success:\": \"Timecard " + timecardId + " deleted.\"}";
      }
}
