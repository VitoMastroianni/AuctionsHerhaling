package be.pxl.auctions.service;

import be.pxl.auctions.dao.AuctionDao;
import be.pxl.auctions.model.Auction;
import be.pxl.auctions.rest.resource.AuctionCreateResource;
import be.pxl.auctions.util.exception.InvalidDateException;
import be.pxl.auctions.util.exception.RequiredFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class AuctionServiceCreateAuctionTest {
    // TODO add unit tests for all possible scenario's of the createUser method
    @Mock
    private AuctionDao auctionDao;
    @InjectMocks
    private AuctionService auctionService;
    private AuctionCreateResource auction;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        auction = new AuctionCreateResource();
        auction.setEndDate("16/06/2021");
        auction.setDescription("Shiny");
    }

    @Test
    public void ValidAuctionIsSaved() throws RequiredFieldException, InvalidDateException {
        when(auctionDao.findAuctionById(anyLong())).thenReturn(null);

        auctionService.createAuction(auction);

        verify(auctionDao).saveAuction(any(Auction.class));
    }

    @Test
    public void EmptyDescriptionThrowsRequiredFieldException() {
        auction.setEndDate(null);

        assertThrows(RequiredFieldException.class, () -> auctionService.createAuction(auction));
        verifyNoInteractions(auctionDao);
    }

    @Test
    public void EmptyEndDateThrowsRequiredFieldException() {
        auction.setDescription(null);

        assertThrows(RequiredFieldException.class, () -> auctionService.createAuction(auction));
        verifyNoInteractions(auctionDao);
    }
}
