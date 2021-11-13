package edu.rit.croatia.companydataserver.servicelayer;

import javax.ws.rs.core.*;

import com.google.gson.Gson;
import edu.rit.croatia.companydataserver.businesslayer.TimecardModel;

import javax.ws.rs.*;

/**
 * REST Web Service
 *
 * @author sara
 */
@Path("CompanyServices")
public class TimecardServices {

    private TimecardModel timecardModel = null;
    private Gson gson = null;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TimecardServices
     */
    public TimecardServices() {
        gson = new Gson();
        timecardModel = new TimecardModel();
    }

    @GET
    @Path("timecards")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTimecards(@QueryParam("emp_id") int empId) {
        return Response.ok(timecardModel.getTimecards(empId)).build();
    }

    @GET
    @Path("timecard")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTimecard(@QueryParam("timecard_id") int timecardId) {
        return Response.ok(timecardModel.getTimecard(timecardId)).build();
    }

    @POST
    @Path("timecard")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertTimecard(@FormParam("emp_id") int empId,
                                     @FormParam("start_time") String start_time,
                                     @FormParam("end_time") String end_time
                                    ) {
        return Response.ok(timecardModel.insertTimecard(start_time, end_time, empId)).build();
    }

    @Path("timecard")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTimecard(String inJson) {
        return Response.ok(timecardModel.updateTimecard(inJson)).build();
    }

    @Path("timecard")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDepartment(@QueryParam("timecard_id") int timecardId) {
        return Response.ok(timecardModel.deleteTimecard(timecardId)).build();
    }
}