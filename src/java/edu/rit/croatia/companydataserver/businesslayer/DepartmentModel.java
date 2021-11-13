package edu.rit.croatia.companydataserver.businesslayer;

import com.google.gson.Gson;
import companydata.*;
import java.util.List;

/**
 *
 * @author Kristina
 */
public class DepartmentModel {

    private DataLayer dl = null;
    public Gson gson = null;
    private Validator validator = null;

    public DepartmentModel() {
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

    public String getDepartments(String companyName) {
        List<Department> departments = dl.getAllDepartment(companyName);
        if (departments.isEmpty()) {
            return "{\"error:\": \"No department found for company " + companyName + ".\"}";
        }
        return gson.toJson(departments);
    }

    /*
        Gets a specific department
    */
    public String getDepartment(String companyName, String deptId) {
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
     
        Department deptObject = new Department(c, dept_name, dept_no, location);
        Department dept = dl.insertDepartment(deptObject); 
        if(dept == null){
                return "{\"error:\": \"Can't add new department, department name: " + dept_name + ", department number: " + dept_no + ", location: " + location + ".\"}";
            } else {
                return "{ "
                        + "\"company\": " + "\"" + c + "\""
                        + ", \"dept_id\":" + dept.getId()
                        + ", \"dept_name\":" + "\"" + dept_name + "\"" 
                        + ", \"dept_no\":" + "\"" + dept_no + "\"" 
                        + ", \"location\":" + "\"" + location + "\""  + " }";
            }
    }

/*
    Updates a specific department
*/
  public String updateDepartment(String dept){
      Department department = gson.fromJson(dept, Department.class);
      dl.updateDepartment(department);
      return dept;
   }

/*
    Deletes a specific department
*/
  public String deleteDepartment(String comp, int dept_id){
        int res = dl.deleteDepartment(comp, dept_id);
        if (res==1)
          return "{\"success:\": \"Department " + dept_id + " from " + comp +" deleted.\"}";
        else  
          return "{\"error:\": \"Error: can't delete " + dept_id + " from " + comp + ".\"}";

     }
    // @Override
    // public String toString() {
    //     // area = Math.PI * this.radius * this.radius;
    //     String str = "{\"id\":\"" + this.id + "\",\"radius\":\"" + this.radius + "\", \"area\":\"" + this.area + "\"}";
    //     return str;
    // }

}
