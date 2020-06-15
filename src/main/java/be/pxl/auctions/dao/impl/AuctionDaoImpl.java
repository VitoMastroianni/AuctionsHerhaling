package be.pxl.auctions.dao.impl;

import be.pxl.auctions.dao.AuctionDao;
import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class AuctionDaoImpl implements AuctionDao {
    private static final Logger LOGGER = LogManager.getLogger(UserDaoImpl.class);

    private EntityManager entityManager;

    public AuctionDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Auction saveAuction(Auction auction) {
        LOGGER.info("Saving Auction [" + auction.getDescription() + "]");
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(auction);
        transaction.commit();
        return auction;
    }

    @Override
    public Auction findAuctionById(long auction) {
        TypedQuery<Auction> userQuery = entityManager.createNamedQuery("findAuctionById", Auction.class);
        userQuery.setParameter("id", auction);
        try {
            return userQuery.getSingleResult();
        } catch (NoResultException e) {
            LOGGER.info("No auction found with id [" + auction + "]");
            return null;
        }
    }

    @Override
    public List<Auction> findAllAuctions() {
        TypedQuery<Auction> findAllQuery = entityManager.createNamedQuery("findAllAuctions", Auction.class);
        return findAllQuery.getResultList();
    }
}
