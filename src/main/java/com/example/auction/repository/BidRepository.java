package com.example.auction.repository;

import com.example.auction.dto.BidDTO;
import com.example.auction.pojo.Bid;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends CrudRepository<Bid, Integer> {
    Optional<Bid> findFirstByLot_IdOrderByDateTimeAsc(int lotId);

    @Query("SELECT b.name, max(b.dateTime) FROM Bid b WHERE b.lot.id = :id GROUP BY b.name HAVING count(b.name) =" +
            "(SELECT count(bi.name) FROM Bid bi WHERE bi.lot.id = :id GROUP BY bi.name ORDER BY count(bi.name) DESC LIMIT 1)")
    BidDTO maxCountBidder(int id);

    List<Bid> findAllByLot_Id(int lotId);
}