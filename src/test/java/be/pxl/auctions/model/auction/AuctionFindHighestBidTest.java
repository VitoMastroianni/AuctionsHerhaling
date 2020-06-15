package be.pxl.auctions.model.auction;

import be.pxl.auctions.builders.BidBuilder;
import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.Bid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AuctionFindHighestBidTest {
    private Auction auction;
    private Bid bid1;
    private Bid bid2;
    private Bid bid3;
    @BeforeEach
    public void init() {
        auction = new Auction();
        bid1 = new BidBuilder().withAmount(10).withAuction(auction).build();
        bid2 = new BidBuilder().withAmount(20).withAuction(auction).build();
        bid3 = new BidBuilder().withAmount(30).withAuction(auction).build();

    }

    @Test
    public void ReturnsHighestBidFromAuction() {
        //Arrange
        auction.addBid(bid1);
        auction.addBid(bid2);
        auction.addBid(bid3);

        //Act
        var result = auction.findHighestBid();

        //Assert
        assertEquals(bid3.getAmount(), result.getAmount());
    }

    @Test
    public void ReturnsNullIfNoBidsHaveBeenPlaced() {
        //Act
        var result = auction.findHighestBid();

        //Assert
        assertNull(result);
    }
}
