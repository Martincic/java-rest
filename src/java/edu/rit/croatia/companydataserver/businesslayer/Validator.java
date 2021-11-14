package edu.rit.croatia.companydataserver.businesslayer;

import companydata.DataLayer;
import companydata.Department;
import companydata.Employee;
import companydata.Timecard;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
    private String DATE_FORMAT_SIMPLE = "yyyy-MM-dd"; 
    
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
    
    public Employee employeeExists(int id) {
        Employee emp = dl.getEmployee(id);
            
        if (emp == null) {
            addErr("Employee with id "+id+" was not found.");
            return null;
        }
        else return emp;
    }
    
    public Timecard timecardExists(int id) {
        Timecard tc = dl.getTimecard(id);
        if (tc == null) {
            addErr("Timecard with id "+id+" was not found.");
            return null;
        }
        else return tc;
    }
    
    public Department departmentExists(String company, int id) {
        Department dept = dl.getDepartment(company, id);
        
        if (dept == null) {
            addErr("Department with id "+id+" was not found for company: " + company);
            return null;
        }
        else return dept;
    }
    
    public void isEmpty(List<T> list, String reason) {
        if(list.isEmpty()) {
            addErr("No records exist in the database for " + reason);
        }
    }

    public java.sql.Date validateHireDate(String hire_date){
        java.sql.Date d = null;
        long parsed = 0L;
        try{
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_SIMPLE);
            parsed = format.parse(hire_date).getTime();
            d = new java.sql.Date(parsed); 
        }
        catch(ParseException pfe) {
            pfe.printStackTrace();
            addErr("Hire date does not follow the format: "+DATE_FORMAT_SIMPLE);
        }
        Calendar hire_cal = Calendar.getInstance();
        Calendar now_cal = Calendar.getInstance();
        hire_cal.setTime(d);
        
        if(hire_cal.after(now_cal)) addErr("Hire date must be in the past.");
        
        int start_day = hire_cal.get(Calendar.DAY_OF_WEEK);
        if(start_day < 2 || start_day > 6) {
            addErr("Start date cannot occur on Saturday or Sunday.");
        }

        return d;
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
    
    public void validateUniqueDeptNo(String company, String dept_no, int current) {
        ArrayList<String> dept_nos = new ArrayList();
        try{
            for(Department dept: dl.getAllDepartment(company)) {
                
                if(dept.getId() != current) dept_nos.add(dept.getDeptNo());
            }
        }
        catch(NumberFormatException nfe){ addErr("Department ID has to be integer."); }
        
        if(dept_nos.contains(dept_no)) addErr("Department with number: "+dept_no+" already exists for company: "+company);
    }
    
    public void validateUniqueEmplyeeID(String emp_no, String current) {
        ArrayList<String> emp_nos = new ArrayList();
        for(Employee emp: dl.getAllEmployee(EmployeeModel.COMPANY_NAME)) {
            
            if(emp.getEmpNo() != current) emp_nos.add(emp.getEmpNo());
        }
        
        if(emp_nos.contains(emp_no)) addErr("Employee with id:" +emp_no+" already exists.");
    }
    
    private void addErr(String error) {
        this.errorMessage += "\""+error+"\",";
        this.success = false;
    }
}
 