package be.pxl.auctions.builders;

import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.Bid;

import java.util.Random;

public class BidBuilder {
    private double amount;
    private Auction auction;

    public BidBuilder withAuction(Auction auction){
        this.auction = auction;
        return this;
    }

    public BidBuilder withAmount(double amount){
        this.amount = amount;
        return this;
    }
    public Bid build(){
        Bid bid = new Bid();
        bid.setAmount(this.amount);
        bid.setAuction(this.auction);
        return bid;
    }


}
