package edu.rit.croatia.companydataserver.businesslayer;

import com.google.gson.Gson;
import companydata.*;
import java.util.List;

/**
 *
 * @author G12
 */
public class CompanyModel {

    private DataLayer dl = null;
    public Gson gson = null;
    private Validator validator = null;

    public CompanyModel() {
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

    /*
        Deletes all Departments, Employees and Timecards for a specific company
    */
      public int deleteCompany(String companyName){
          return dl.deleteCompany(companyName);
       }


}
