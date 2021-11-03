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
            return "{\"error:\": \"No employee found for company " + companyName + ".\"}";
        }
        return gson.toJson(employees);
    }

    /*
        Gets a specific employee
    */
    public String getEmployee(String empId) {
        Employee employee = dl.getEmployee(Integer.parseInt(empId));
        if (employee == null) {
            return "{\"error:\": \"No employee found for id " + empId + ".\"}";
        }
        return gson.toJson(employee);
    }
    
    /*
        Create/Insert a specific employee
    */
    public Employee insertEmployee(Employee emp) {
        if(dl.insertEmployee(emp) == null){
            return null;
        } else {
            return emp;
        }
    }

    /*
        Updates a specific employee
    */
      public String updateEmployee(String emp){
          Employee employee = gson.fromJson(emp, Employee.class);
          dl.updateEmployee(employee);
          return emp;
       }

    /*
        Updates a specific employee
    */
      public int deleteEmployee(int empId){
          return dl.deleteEmployee(empId);
       }


}
