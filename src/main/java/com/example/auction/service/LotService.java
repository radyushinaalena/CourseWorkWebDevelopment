package com.example.auction.service;

import com.example.auction.dto.CreateLotDTO;
import com.example.auction.dto.FullLotDTO;
import com.example.auction.dto.LotDTO;
import com.example.auction.dto.Status;

import java.util.List;

public interface LotService {
    FullLotDTO getFullLot(int id);
    void startLotBidding(int id);
    void stopLotBidding(int id);
    LotDTO createLot(CreateLotDTO createLotDTO);
    List<LotDTO> findLots(Status status, int page);
    byte[] getCSVFile();
}
