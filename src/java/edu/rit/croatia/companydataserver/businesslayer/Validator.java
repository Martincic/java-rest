package edu.rit.croatia.companydataserver.businesslayer;

import companydata.DataLayer;
import companydata.Employee;
import companydata.Timecard;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
            this.success = false;
        }catch(java.lang.NullPointerException npe){
            addErr("Date field is required");
            this.success = false;
        }
        return null;
    }
    
    public void employeeExists(int id) {
        
        Employee employee = dl.getEmployee(id);
        if (employee == null) {
            addErr("Employee with id "+id+" was not found.");
            success = false;
        }
    }
    
    public void timecardExists(int id) {
        
        Timecard tc = dl.getTimecard(id);
        if (tc == null) {
            addErr("Timecard with id "+id+" was not found.");
            success = false;
        }
    }
    
    public void isEmpty(List<T> list) {
        if(list.isEmpty()) {
            addErr("No records exist in the database.");
            success = false;
        }
    }
    
    public void validateTimecardDates(String startdate, String enddate) {
        long start = getTimestamp(startdate).getTime();
        long end = getTimestamp(enddate).getTime();
        long miliseconds = start - end;
        int days = Math.round(miliseconds / (1000  * 60 * 60 * 24));
        
        if(days > 7) {
            addErr("Date is older then 7 days");
            success = false;
        }
    }
    
    private void addErr(String error) {
        errorMessage += "\""+error+"\",";
    }
}
