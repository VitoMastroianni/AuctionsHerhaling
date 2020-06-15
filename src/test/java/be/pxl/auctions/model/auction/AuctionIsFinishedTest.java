package be.pxl.auctions.model.auction;

import be.pxl.auctions.model.Auction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuctionIsFinishedTest {

    private Auction auction;
    @BeforeEach
    public void init() {
        auction = new Auction();
    }

    @Test
    public void returnsTrueWhenAuctionIsFinished() {
        auction.setEndDate(LocalDate.now().minusDays(1));

        boolean result = auction.isFinished();

        assertEquals(true, result);
    }

    @Test
    public void returnsFalseWhenAuctionIsNotFinished() {
        auction.setEndDate(LocalDate.now().plusDays(1));

        boolean result = auction.isFinished();

        assertEquals(false, result);
    }
}
