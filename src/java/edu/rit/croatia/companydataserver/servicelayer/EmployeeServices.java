package edu.rit.croatia.companydataserver.servicelayer;

import companydata.Employee;
import edu.rit.croatia.companydataserver.businesslayer.EmployeeModel;
import javax.ws.rs.core.*;

import javax.ws.rs.*;

/**
 * REST Web Service
 *
 * @author Kristina
 */
@Path("CompanyServices")
public class EmployeeServices {

    private EmployeeModel employeeModel = null;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CompanyServices
     */
    public EmployeeServices() {
        employeeModel = new EmployeeModel();
    }

    @GET
    @Path("employees")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmployees(@QueryParam("company") String companyName) {
        return Response.ok(employeeModel.getEmployees(companyName)).build();
    }

    @GET
    @Path("employee")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmployee(@QueryParam("emp_id") String id) {
        return Response.ok(employeeModel.getEmployee(id)).build();
    }

    @POST
    @Path("employee")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertEmployee(@FormParam("emp_name") String emp_name,
                                     @FormParam("emp_no") String emp_no,
                                     @FormParam("hire_date") String hire_date, 
                                     @FormParam("job") String job,
                                     @FormParam("salary") String salary,
                                     @FormParam("dept_id") String dept_id,
                                     @FormParam("mng_id") String mng_id
                                    ) {
        return Response.ok(employeeModel.insertEmployee(emp_name, emp_no, hire_date, job, salary, dept_id, mng_id)).build();
    }

    @Path("employee")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEmployee(String inJson) {
        return Response.ok(employeeModel.updateEmployee(inJson)).build();
    }

    @Path("employee")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEmployee(@QueryParam("emp_id") int id) {
        // return Response.ok("id is: " + id.getClass()).build();
        
        // if delete DB record successful send ok, otherwise Repsonse.Status.NO_CONTENT
        // with no msg
        return Response.ok(employeeModel.deleteEmployee(id)).build();
    }
}