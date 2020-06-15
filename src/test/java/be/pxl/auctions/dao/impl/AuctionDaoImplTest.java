package be.pxl.auctions.dao.impl;

import be.pxl.auctions.dao.impl.util.DaoTest;
import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuctionDaoImplTest extends DaoTest {

    private AuctionDaoImpl auctionDao = new AuctionDaoImpl(em);

    @Test
    public void AllAuctionsCanBeRetrieved() {
        Auction auction1 = new Auction();
        Auction auction2 = new Auction();

        auction1.setDescription("Auction 1");
        auction2.setDescription("Auction 2");
        auctionDao.saveAuction(auction1);
        auctionDao.saveAuction(auction2);
        clear();

        var auctionList = auctionDao.findAllAuctions();

        assertNotNull(auctionList);
        assertEquals(2, auctionList.size());
        assertEquals(auction1.getDescription(), auctionList.stream().findFirst().map(Auction::getDescription).get());
    }

    @Test
    public void AuctionCanBeSavedAndRetrievedById() {
        //Arrange
        Auction auction = new Auction();
        auction.setDescription("Test Auction");

        //Act
        long newAuctionId = auctionDao.saveAuction(auction).getId();
        clear();
        Auction retrievedAuction = auctionDao.findAuctionById(newAuctionId);

        //Assert
        assertNotNull(retrievedAuction);
        assertEquals(auction.getDescription(), retrievedAuction.getDescription());
    }


}
