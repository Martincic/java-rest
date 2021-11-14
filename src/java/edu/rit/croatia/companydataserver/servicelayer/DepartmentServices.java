package edu.rit.croatia.companydataserver.servicelayer;

import companydata.Department;
import edu.rit.croatia.companydataserver.businesslayer.DepartmentModel;
import javax.ws.rs.core.*;


import javax.ws.rs.*;

/**
 * REST Web Service
 *
 * @author G12
 */
@Path("CompanyServices")
public class DepartmentServices {

    private DepartmentModel departmentModel = null;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CompanyServices
     */
    public DepartmentServices() {
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
    public Response getDepartment(@QueryParam("company") String companyName, @QueryParam("dept_id") int id) {
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
                                         
        return Response.ok(departmentModel.insertDepartment(c, dept_name, dept_no, location)).build();
    }

    @Path("department")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDepartment(String inJson) {
        // Department dept = gson.fromJson(inJson, Department.class);
        return Response.ok(departmentModel.updateDepartment(inJson)).build();
    }

    @Path("department")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDepartment(@QueryParam("dept_id") int id, @QueryParam("company") String companyName) {
        // return Response.ok("id is: " + id.getClass()).build();
        // if delete DB record successful send ok, otherwise Repsonse.Status.NO_CONTENT
        // with no msg
        return Response.ok(departmentModel.deleteDepartment(companyName, id)).build();
    }
}