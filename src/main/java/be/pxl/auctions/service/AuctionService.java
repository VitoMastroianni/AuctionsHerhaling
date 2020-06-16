package be.pxl.auctions.service;

import be.pxl.auctions.dao.AuctionDao;
import be.pxl.auctions.dao.UserDao;
import be.pxl.auctions.dao.impl.AuctionDaoImpl;
import be.pxl.auctions.dao.impl.UserDaoImpl;
import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.Bid;
import be.pxl.auctions.model.User;
import be.pxl.auctions.rest.resource.AuctionCreateResource;
import be.pxl.auctions.rest.resource.AuctionResource;
import be.pxl.auctions.rest.resource.BidCreateResource;
import be.pxl.auctions.util.EntityManagerUtil;
import be.pxl.auctions.util.exception.*;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class AuctionService {
    private UserDao userDao;
    private AuctionDao auctionDao;

    public AuctionService() {
        EntityManager entityManager = EntityManagerUtil.createEntityManager();
        userDao = new UserDaoImpl(entityManager);
        auctionDao = new AuctionDaoImpl(entityManager);
    }

    public void doBid(long auctionId, BidCreateResource bidCreateResource) throws InvalidBidException {
            User user = userDao.findUserByEmail(bidCreateResource.getEmail());
            Auction auction = auctionDao.findAuctionById(auctionId);

            if (user == null){
                throw new InvalidBidException("User is invalid: " + bidCreateResource.getEmail());
            }
            if (auction == null){
                throw new InvalidBidException("Auction does not exist.");
            }

            if (auction.getBids().size() > 0){
                if(bidCreateResource.getPrice() < auction.findHighestBid().getAmount()){
                    throw new InvalidBidException("The current highest bid is: " + auction.findHighestBid().getAmount());
                }
                if(auction.findHighestBid().getUser() == user){
                    throw new InvalidBidException("User already has the highest bid");
                }
            }

            if(auction.isFinished()){
                throw new InvalidBidException("Auction has been finished");
            }

            Bid newBid = new Bid(user,LocalDate.now(),bidCreateResource.getPrice());
            auction.addBid(newBid);
    }

    public List<AuctionResource> findAuctions(){
        List<Auction> auctions = auctionDao.findAllAuctions();
        List<AuctionResource> auctionResourceList = new ArrayList<>();

        for (Auction auction: auctions) {
            auctionResourceList.add(mapAuction(auction));
        }

        return auctionResourceList;
    }

    public AuctionResource createAuction(AuctionCreateResource auctionCreateResource) throws RequiredFieldException, InvalidDateException {
        if (StringUtils.isBlank(auctionCreateResource.getDescription())) {
            throw new RequiredFieldException("Description");
        }
        if (StringUtils.isBlank(auctionCreateResource.getEndDate())) {
            throw new RequiredFieldException("End date");
        }

        auctionCreateResource.getEndDateAsLocalDate();

        Auction auction = mapToAuction(auctionCreateResource);

        auctionDao.saveAuction(auction);

        return mapToAuctionResource(auction);
    }

    private AuctionResource mapAuction(Auction auction){
        AuctionResource auctionResource = new AuctionResource();
        auctionResource.setId(auction.getId());
        auctionResource.setDescription(auction.getDescription());
        auctionResource.setEndDate(auction.getEndDate());
        auctionResource.setFinished(auction.isFinished());
        if(auction.getBids().size() == 0){
            auctionResource.setNumberOfBids(0);
            auctionResource.setHighestBid(0);
            auctionResource.setHighestBidBy(null);
        } else {
            auctionResource.setNumberOfBids(auction.getBids().size());
            auctionResource.setHighestBid(auction.findHighestBid().getAmount());
            auctionResource.setHighestBidBy(auction.findHighestBid().getUser().getLastName());
        }

        return auctionResource;
    }

    private AuctionResource mapToAuctionResource(Auction auction){
        AuctionResource auctionResource = new AuctionResource();
        auctionResource.setDescription(auction.getDescription());
        auctionResource.setEndDate(auction.getEndDate());

        return auctionResource;
    }

    private Auction mapToAuction(AuctionCreateResource auctionCreateResource) throws InvalidDateException {
        Auction auction = new Auction();
        auction.setDescription(auctionCreateResource.getDescription());
        auction.setEndDate(auctionCreateResource.getEndDateAsLocalDate());

        return auction;
    }
}
