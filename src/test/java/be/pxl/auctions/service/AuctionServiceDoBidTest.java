package be.pxl.auctions.service;

import be.pxl.auctions.dao.AuctionDao;
import be.pxl.auctions.dao.UserDao;
import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.Bid;
import be.pxl.auctions.model.User;
import be.pxl.auctions.rest.resource.BidCreateResource;
import be.pxl.auctions.rest.resource.UserCreateResource;
import be.pxl.auctions.util.exception.InvalidBidException;
import be.pxl.auctions.util.exception.RequiredFieldException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuctionServiceDoBidTest {



    // TODO add unit tests for all possible scenario's of the createUser method
    @Mock
    private UserDao userDao;
    @Mock
    private AuctionDao auctionDao;
    @InjectMocks
    private AuctionService auctionService;
    private BidCreateResource bidCreateResource;
    private User user;
    private Auction auction;
    private Bid bid;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        bidCreateResource = new BidCreateResource();
        bidCreateResource.setPrice(10000);
        bidCreateResource.setEmail("mark@facebook.com");

        user = new User();
        user.setEmail(bidCreateResource.getEmail());


        bid = new Bid();
        bid.setAmount(100);

        auction = new Auction();
        auction.setEndDate(LocalDate.now().plusMonths(1));
        auction.addBid(bid);
    }

    @Test
    public void ValidBidIsSaved() throws InvalidBidException {
        when(userDao.findUserByEmail(anyString())).thenReturn(user);
        when(auctionDao.findAuctionById(anyLong())).thenReturn(auction);

        auctionService.doBid(1, bidCreateResource);

        verify(userDao, times(1)).findUserByEmail(anyString());
        verify(auctionDao, times(1)).findAuctionById(anyLong());
        assertEquals(2, auction.bids.size());
    }

    @Test
    public void InvalidUserThrowsInvalidBidException(){
        when(userDao.findUserByEmail(anyString())).thenReturn(null);

        assertThrows(InvalidBidException.class, () -> auctionService.doBid(1, bidCreateResource));
    }

    @Test
    public void InvalidAuctionThrowsInvalidBidException(){
        when(auctionDao.findAuctionById(anyLong())).thenReturn(null);

        assertThrows(InvalidBidException.class, () -> auctionService.doBid(1, bidCreateResource));
    }

    @Test
    public void LowerBidThrowsInvalidBidException(){
        bidCreateResource.setPrice(auction.findHighestBid().getAmount()-1);

        assertThrows(InvalidBidException.class, () -> auctionService.doBid(1, bidCreateResource));
    }

    @Test
    public void doBidThrowsInvalidBidExceptionWhenUserAlreadyHasTheHighestBid(){
        when(userDao.findUserByEmail(anyString())).thenReturn(user);
        when(auctionDao.findAuctionById(anyLong())).thenReturn(auction);
        auction.addBid(new Bid(user, LocalDate.now(), 1000));

        assertThrows(InvalidBidException.class, () -> auctionService.doBid(1, bidCreateResource));
    }

    @Test
    public void BidWhenAuctionIsFinishedThrowsInvalidBidException(){
        when(userDao.findUserByEmail(anyString())).thenReturn(user);
        when(auctionDao.findAuctionById(anyLong())).thenReturn(auction);
        auction.setEndDate(LocalDate.now().minusMonths(1));

        assertThrows(InvalidBidException.class, () -> auctionService.doBid(1, bidCreateResource));
    }
}
