package be.pxl.auctions.rest;

import be.pxl.auctions.rest.resource.*;
import be.pxl.auctions.service.AuctionService;
import be.pxl.auctions.service.UserService;
import be.pxl.auctions.util.exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Path("/auctions")
public class AuctionRest {
    private static final Logger LOGGER = LogManager.getLogger(AuctionRest.class);

    @Inject
    private AuctionService auctionService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAuctions() {
        List<AuctionResource> result = new ArrayList<>(auctionService.findAuctions());
        return Response.ok(result).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAuction(AuctionCreateResource auctionCreateResource) {
        try {
            AuctionResource auction = auctionService.createAuction(auctionCreateResource);
            return Response.created(UriBuilder.fromPath("/auctions/" + auction.getId()).build()).build();
            //return Response.ok().build();
        }
        catch (RequiredFieldException | InvalidDateException e) {
            LOGGER.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/bids")
    public Response addBid(@PathParam("id")long id, BidCreateResource bidCreateResource){
        try{
            auctionService.doBid(id, bidCreateResource);
            return Response.created(UriBuilder.fromPath("/auctions/" + bidCreateResource.getEmail()).build()).build();
        }  catch (InvalidBidException e) {
            LOGGER.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
        }
    }
}
