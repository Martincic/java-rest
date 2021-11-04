package edu.rit.croatia.companydataserver.businesslayer;

import com.google.gson.Gson;
import companydata.*;
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
    public String getEmployee(int timecard_id) {
        Timecard timecard = dl.getTimecard(timecard_id);
        if (timecard == null) {
            return "{\"error:\": \"No timecard found for timecardID: " + timecard_id + ".\"}";
        }
        return gson.toJson(timecard);
    }
    
    /*
        Create/Insert a specific timecard
    */
    public Timecard insertTimecard(Timecard timecard) {
        if(dl.insertTimecard(timecard) == null){
            return null;
        } else {
            return timecard;
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
      public int deleteTimecard(int timecard_id){
          return dl.deleteTimecard(timecard_id);
       }


}
