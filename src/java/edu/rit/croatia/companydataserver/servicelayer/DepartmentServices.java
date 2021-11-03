package edu.rit.croatia.companydataserver.servicelayer;

import companydata.Department;
import edu.rit.croatia.companydataserver.businesslayer.DepartmentModel;
import javax.ws.rs.core.*;

import com.google.gson.Gson;

import javax.ws.rs.*;

/**
 * REST Web Service
 *
 * @author Kristina
 */
@Path("CompanyServices")
public class DepartmentServices {

    private DepartmentModel departmentModel = null;
    private Gson gson = null;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CompanyServices
     */
    public DepartmentServices() {
        gson = new Gson();
        departmentModel = new DepartmentModel();
    }

    @GET
    @Path("departments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDepartments(@QueryParam("company") String companyName) {
        return Response.ok(departmentModel.getDepartments(companyName)).build();
    }

    @GET
    @Path("department")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDepartment(@QueryParam("company") String companyName, @QueryParam("id") String id) {
        return Response.ok(departmentModel.getDepartment(companyName, id)).build();
    }

    @POST
    @Path("department")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertDepartment(@FormParam("company") String c,
                                     @FormParam("dept_name") String dept_name,
                                     @FormParam("dept_no") String dept_no, 
                                     @FormParam("location") String location
                                    ) {
                                         
        Department deptObject = new Department(c, dept_name, dept_no, location);
        // String inJson = Response.ok(deptObject.toString());
        departmentModel.insertDepartment(deptObject);
        return Response.ok("Department created: " + deptObject.toString()).build();
    }

    @Path("department")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDepartment(String inJson) {
        // Department dept = gson.fromJson(inJson, Department.class);
        departmentModel.updateDepartment(inJson);
        return Response.ok("Department Updated: " + inJson).build();
    }

    @Path("department")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDepartment(@QueryParam("dept_id") int id, @QueryParam("company") String companyName) {
        // return Response.ok("id is: " + id.getClass()).build();
        departmentModel.deleteDepartment(companyName, id);
        // if delete DB record successful send ok, otherwise Repsonse.Status.NO_CONTENT
        // with no msg
        return Response.ok("Department Deleted").build();
    }
}