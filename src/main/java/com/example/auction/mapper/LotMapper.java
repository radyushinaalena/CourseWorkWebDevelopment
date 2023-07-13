package com.example.auction.mapper;

import com.example.auction.dto.*;
import com.example.auction.pojo.Lot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LotMapper {

    public FullLotDTO toFullLotDTO(Lot lot, int currentPrice, BidDTO lastBid) {

        FullLotDTO fullLotDTO = new FullLotDTO();
        fullLotDTO.setId(lot.getId());
        fullLotDTO.setStatus(lot.getStatus());
        fullLotDTO.setTitle(lot.getTitle());
        fullLotDTO.setDescription(lot.getDescription());
        fullLotDTO.setStartPrice(lot.getStartPrice());
        fullLotDTO.setBidPrice(lot.getBidPrice());
        fullLotDTO.setCurrentPrice(currentPrice);
        fullLotDTO.setLastBid(lastBid);
        return fullLotDTO;
    }

    public Lot toLot(CreateLotDTO createLotDTO) {
        Lot lot = new Lot();
        lot.setStatus(Status.CREATED);
        lot.setTitle(createLotDTO.getTitle());
        lot.setDescription(createLotDTO.getDescription());
        lot.setStartPrice(createLotDTO.getStartPrice());
        lot.setBidPrice(createLotDTO.getBidPrice());
        return lot;
    }

    public LotDTO toLotDTO(Lot lot) {
        LotDTO lotDTO = new LotDTO();
        lotDTO.setId(lot.getId());
        lotDTO.setStatus(lot.getStatus());
        lotDTO.setTitle(lot.getTitle());
        lotDTO.setDescription(lot.getDescription());
        lotDTO.setStartPrice(lot.getStartPrice());
        lotDTO.setBidPrice(lot.getBidPrice());
        return lotDTO;
    }
}