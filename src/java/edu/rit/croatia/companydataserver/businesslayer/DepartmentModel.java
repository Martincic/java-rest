package edu.rit.croatia.companydataserver.businesslayer;

import com.google.gson.Gson;
import companydata.*;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Kristina
 */
public class DepartmentModel {

    private DataLayer dl = null;
    public Gson gson = null;

    public DepartmentModel() {
        try {
            this.dl = new DataLayer("kxmzgr");
            gson = new Gson();
        } catch (Exception ex) {
            System.out.println("Problem with query: " + ex.getMessage());
        } finally {
            dl.close();
        }
    }

    public String getDepartments(String companyName) {
        Validator validator = new Validator();
        
        List<Department> departments = dl.getAllDepartment(companyName);
        validator.isEmpty(departments, "company: " + companyName);
        
        if(validator.hasFailed()) return validator.errorMessage();
        
        return gson.toJson(departments);
    }

    /*
        Gets a specific department
    */
    public String getDepartment(String companyName, String deptId) {
        Validator validator = new Validator();
        validator.departmentExists(companyName, deptId);
        
        if(validator.hasFailed()) return validator.errorMessage();
        
        Department department = dl.getDepartment(companyName, Integer.parseInt(deptId));
        if (department == null) {
            return "{\"error:\": \"No department found for company " + companyName + ".\"}";
        }
        return gson.toJson(department);
    }
    
    /*
        Create/Insert a specific department
    */
    public String insertDepartment(String c, String dept_name, String dept_no, String location) {
        Validator validator = new Validator();
        validator.validateUniqueDeptNo(c, dept_no, 0);

        if(validator.hasFailed()) return validator.errorMessage();
        
        Department deptObject = new Department(c, dept_name, dept_no, location);
        Department dept = dl.insertDepartment(deptObject); 

        return gson.toJson(dept);
    }

/*
    Updates a specific department
*/
  public String updateDepartment(String department){
      Department dept = null;
      Validator validator = new Validator();
      
      try{
        dept = gson.fromJson(department, Department.class);
      }  
      catch(com.google.gson.JsonSyntaxException mje) {
          return "{\"error:\": \"Malformed JSON input. Bad request.\"}";
      }
      
      validator.departmentExists(dept.getCompany(), String.valueOf(dept.getId()));
      validator.validateUniqueDeptNo(dept.getCompany(), String.valueOf(dept.getDeptNo()), dept.getId());
      if(validator.hasFailed()) return validator.errorMessage();
      
      return gson.toJson(dl.updateDepartment(dept));
   }

/*
    Deletes a specific department
*/
  public String deleteDepartment(String comp, int dept_id){
      Validator validator = new Validator();
      validator.departmentExists(comp, String.valueOf(dept_id));
      if(validator.hasFailed()) return validator.errorMessage();

      dl.deleteDepartment(comp, dept_id);
      return "{\"success:\": \"Department " + dept_id + " from " + comp +" deleted.\"}";
   }


}
