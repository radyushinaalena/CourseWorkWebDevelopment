package com.example.auction.service;

import com.example.auction.dto.BidDTO;
import com.example.auction.dto.BidderDTO;

public interface BidService {
    BidDTO getFirstBidder(int id);
    BidDTO getMostFrequentBidder(int id);
    void createBid(int id, BidderDTO bidderDTO);
}
