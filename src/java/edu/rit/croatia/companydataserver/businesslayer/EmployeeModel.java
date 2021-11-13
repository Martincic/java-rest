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
    public Gson gson = null;
    private Validator validator = null;

    public EmployeeModel() {
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

    public String getEmployees(String companyName) {
        List<Employee> employees = dl.getAllEmployee(companyName);
        if (employees.isEmpty()) {
            return "{\"error\": \"No employee found for company " + companyName + ".\"}";
        }
        return gson.toJson(employees);
    }

    /*
        Gets a specific employee
    */
    public String getEmployee(String empId) {
        Employee employee = dl.getEmployee(Integer.parseInt(empId));
        if (employee == null) {
            return "{\"error\": \"No employee found for id " + empId + ".\"}";
        }
        return gson.toJson(employee);
    }
    
    /*
        Create/Insert a specific employee
    */
    public String insertEmployee(String emp_name, String emp_no, String hire_date, String job, String salary, String dept_id, String mng_id) {
        java.sql.Date conv_date = java.sql.Date.valueOf(hire_date);
        Employee empObject = new Employee(emp_name, emp_no, conv_date, job, Double.parseDouble(salary), Integer.parseInt(dept_id), Integer.parseInt(mng_id));
        Employee emp = dl.insertEmployee(empObject);
        if(emp == null){
                return "{\"error\": \"Can't add new employee, employee name: " + emp_name + ", employee number: " + emp_no + ", hire date: " + hire_date + ", job: " + job  + ", salary: " + salary + ", department id: " + dept_id + ", manager id: " + mng_id + ".\"}";
            } else {

                return gson.toJson(emp);
            }
    }

    /*
        Updates a specific employee
    */
      public String updateEmployee(String employee){

      Employee emp = dl.updateEmployee(gson.fromJson(employee, Employee.class));
        if(emp == null){
                //TODO: error msgs
                return "{\"error:\": \"Can't update employee.\"}";
            } else {
                return gson.toJson(emp);
            }
   }
    /*
        Deletes a specific employee
    */
      public String deleteEmployee(int empId){
          int res = dl.deleteEmployee(empId);
          if (res==1)
            return "{\"success\": \"Employee " + empId + " deleted.\"}";
          else  
            return "{\"error\": \"Error: can't delete " + empId + ".\"}";

       }


}
