package be.pxl.auctions.model;

import javax.persistence.*;
import javax.validation.valueextraction.UnwrapByDefault;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name = "findAuctionById", query = "SELECT a FROM Auction a WHERE a.id=:id"),
        @NamedQuery(name = "findAllAuctions", query = "SELECT a FROM Auction a")
})
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;
    private LocalDate endDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "auction")
    public List<Bid> bids = new ArrayList<>();

    public Auction() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void addBid(Bid bid) {
        bids.add(bid);
        bid.setAuction(this);
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public boolean isFinished(){
        return endDate.isBefore(LocalDate.now());
    }

    public Bid findHighestBid(){
        if(bids.size() > 0){
            var bid = Collections.max(bids, Comparator.comparingDouble(Bid::getAmount));
            return bid;
        }
        return null;
    }

}
