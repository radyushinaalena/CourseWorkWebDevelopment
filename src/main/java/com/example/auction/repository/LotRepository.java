package com.example.auction.repository;

import com.example.auction.dto.Status;
import com.example.auction.pojo.Lot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LotRepository extends PagingAndSortingRepository<Lot, Integer>, JpaRepository<Lot, Integer> {

    Page<Lot> findAllByStatus(Status status, Pageable pageable);

    @Query(value = "SELECT l.id, l.title, l.status,last_time.name, count(b)*l.bid_price+l.start_price " +
            "FROM lots l " +
            "LEFT JOIN bids b on l.id = b.lot_id " +
            "LEFT JOIN " +
            "(SELECT b.name, b.lot_id, b.date_time FROM bids b " +
            "RIGHT JOIN " +
            "(SELECT bi.lot_id, max(bi.date_time) as time FROM bids bi GROUP BY bi.lot_id) as max_time " +
            "ON max_time.lot_id = b.lot_id " +
            "and time = b.date_time) as last_time on l.id = last_time.lot_id " +
            "GROUP BY l.id, l.title, l.status, last_time.name " +
            "ORDER BY l.id asc", nativeQuery = true)
    List<Object[]> getAllLotsForCsv();
}