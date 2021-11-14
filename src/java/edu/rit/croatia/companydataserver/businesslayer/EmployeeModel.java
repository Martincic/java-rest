package edu.rit.croatia.companydataserver.businesslayer;

import com.google.gson.Gson;
import companydata.*;
import java.util.List;

/**
 *
 * @author sara
 */
public class EmployeeModel {

    private DataLayer dl = null;
    public static final String COMPANY_NAME = "tm4818";
    public Gson gson = null;

    public EmployeeModel() {
        try {
            this.dl = new DataLayer("kxmzgr");
            gson = new Gson();
        } catch (Exception ex) {
            System.out.println("Problem with query: " + ex.getMessage());
        } finally {
            dl.close();
        }
    }

    public String getEmployees(String companyName) {
        Validator validator = new Validator();
        
        List<Employee> employees = dl.getAllEmployee(companyName);
        validator.isEmpty(employees, "company: " + companyName);
        
        if(validator.hasFailed()) return validator.errorMessage();

        return gson.toJson(employees);
    }

    /*
        Gets a specific employee
    */
    public String getEmployee(int empId) {
        Validator validator = new Validator();
        Employee employee = validator.employeeExists(empId);
        
        if(validator.hasFailed()) return validator.errorMessage();
        
        dl.getEmployee(empId);
        
        return gson.toJson(employee);
    }
    
    /*
        Create/Insert a specific employee

        1) dept_id must exist as a Department in your company
    */
    public String insertEmployee(String emp_name, String emp_no, String hire_date, String job, String salary, int dept_id, int mng_id) {
        Validator validator = new Validator();
        
        validator.departmentExists(COMPANY_NAME, dept_id);
        java.sql.Date conv_date = validator.validateHireDate(hire_date);
        if(mng_id != 0) validator.employeeExists(mng_id);
        
        validator.validateUniqueEmplyeeID(emp_no, "0");

        if(validator.hasFailed()) return validator.errorMessage();
        
        Employee empObject = new Employee(emp_name, emp_no, conv_date, job, Double.parseDouble(salary), dept_id, mng_id);
        Employee anotherOne = dl.insertEmployee(empObject);
        return gson.toJson(anotherOne);
    }

    /*
        Updates a specific employee
    */
      public String updateEmployee(String employee){
            EmployeeJson request = null;
            try{
                request = gson.fromJson(employee, EmployeeJson.class);
            }
            catch(com.google.gson.JsonSyntaxException mje) {
                return "{\"error\": \"Malformed JSON input. Bad request.\"}";
            }
            Validator validator = new Validator();

            validator.departmentExists(COMPANY_NAME, request.dept_id);
            java.sql.Date conv_date = validator.validateHireDate(request.hire_date);
            if(request.mng_id != 0) validator.employeeExists(request.mng_id);

            Employee emp_exists = validator.employeeExists(request.emp_id);
            if(validator.hasFailed()) return validator.errorMessage();
            
            Employee newEmp = new Employee(request.emp_id, 
                    request.emp_name, 
                    request.emp_no, 
                    conv_date, 
                    request.job, 
                    request.salary, 
                    request.dept_id, 
                    request.mng_id);
            
            Employee emp = dl.updateEmployee(newEmp);
            
            return gson.toJson(emp);
       }

    /*
        Deletes a specific employee
    */
      public String deleteEmployee(int empId){
        Validator validator = new Validator();
        validator.employeeExists(empId);
        
        if(validator.hasFailed()) return validator.errorMessage();
        
        dl.deleteEmployee(empId);
            return "{\"success\": \"Employee " + empId + " deleted.\"}";

       }


}
