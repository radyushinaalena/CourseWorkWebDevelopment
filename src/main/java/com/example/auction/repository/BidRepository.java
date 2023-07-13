package com.example.auction.repository;

import com.example.auction.dto.BidDTO;
import com.example.auction.pojo.Bid;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends CrudRepository<Bid, Integer> {
    Optional<Bid> findFirstByLot_IdOrderByDateTimeAsc(int lotId);

    @Query("SELECT new com.example.auction.dto.BidDTO (b.name, max(b.dateTime)) from Bid b where b.lot.id = :id group by b.name order by count(b.name) desc limit 1")
    BidDTO maxCountBidder(int id);

    List<Bid> findAllByLot_Id(int lotId);
}