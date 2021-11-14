package edu.rit.croatia.companydataserver.businesslayer;

import companydata.DataLayer;
import companydata.Employee;
import companydata.Timecard;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;

/*
    Data validator class used to build error messages
*/
public class Validator<T> {
    
    private String errorMessage = "{\"errors:\":[";
    private DataLayer dl = new DataLayer("tm4818");
    private boolean success = true;
    private Timestamp now = new Timestamp(System.currentTimeMillis());
    
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"; 
    
    public Validator() {
        try {
            this.dl = new DataLayer("kxmzgr");
        } catch (Exception ex) {
            System.out.println("Problem with query: " + ex.getMessage());
        } finally {
            dl.close();
        }
    }
    public boolean hasFailed() {
        return !this.success;
    }
    
    public String errorMessage() {
        errorMessage = errorMessage.substring(0, errorMessage.length()-1);
        errorMessage += "]}";
        return errorMessage;
    }
    
    public Timestamp getTimestamp(String timestamp) {
        try{
            return new Timestamp(new SimpleDateFormat(DATE_FORMAT).parse(timestamp).getTime());
        }catch(java.text.ParseException pe){
            addErr("Timestamp not matching desired format: " + DATE_FORMAT);
        }catch(java.lang.NullPointerException npe){
            addErr("Date field is required");
        }
        return new Timestamp(0);
    }
    
    public void employeeExists(int id) {
        
        Employee employee = dl.getEmployee(id);
        if (employee == null) {
            addErr("Employee with id "+id+" was not found.");
        }
    }
    
    public void timecardExists(int id) {
        
        Timecard tc = dl.getTimecard(id);
        if (tc == null) {
            addErr("Timecard with id "+id+" was not found.");
        }
    }
    
    public void isEmpty(List<T> list) {
        if(list.isEmpty()) {
            addErr("No records exist in the database.");
        }
    }
    
    public void validateTimecardDates(Timestamp startdate, Timestamp enddate) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        start.setTimeInMillis(startdate.getTime());
        end.setTimeInMillis(enddate.getTime());

        if(enddate.before(startdate)) {
            addErr("The start date must be before end date");
        }
        int start_day = start.get(Calendar.DAY_OF_WEEK);
        int end_day = end.get(Calendar.DAY_OF_WEEK);
        if(start_day != end_day) addErr("Start and end dates must be on the same date");
        if(start_day < 2 || start_day > 6) addErr("Start date cannot occur on Saturday or Sunday.");
        
        int start_hour = start.get(Calendar.HOUR_OF_DAY);
        int end_hour = end.get(Calendar.HOUR_OF_DAY);
        if((end_hour - start_hour) < 1) addErr("There must be at least 1 hour difference between timestamps.");
        
        long daysBetween = ChronoUnit.DAYS.between(start.toInstant(), now.toInstant());
        if(daysBetween > 7) addErr("You can't start more then 7 days ago.");
    }
    
    private void addErr(String error) {
        this.errorMessage += "\""+error+"\",";
        this.success = false;
    }
}
