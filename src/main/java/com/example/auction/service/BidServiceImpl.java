package com.example.auction.service;

import com.example.auction.dto.BidDTO;
import com.example.auction.dto.BidderDTO;
import com.example.auction.dto.Status;
import com.example.auction.exception.BetNotFoundException;
import com.example.auction.exception.LotNotFoundException;
import com.example.auction.exception.LotWrongStatusException;
import com.example.auction.mapper.BidMapper;
import com.example.auction.pojo.Bid;
import com.example.auction.pojo.Lot;
import com.example.auction.repository.BidRepository;
import com.example.auction.repository.LotRepository;
import lombok.Data;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Data
@Service
public class BidServiceImpl implements BidService {
    private final BidRepository bidRepository;
    private final LotRepository lotRepository;
    private final BidMapper bidMapper;
    private static final Logger logger = LoggerFactory.getLogger(BidServiceImpl.class);


    @SneakyThrows
    @Override
    public BidDTO getFirstBidder(int id){
        logger.debug("Получение информации о первом ставившем на лот с id: {} " , id );
        if(lotRepository.existsById(id)) {
            logger.debug("Получена информацию о первом ставившем на лот с id: {}" , id );
            return bidRepository.findFirstByLot_IdOrderByDateTimeAsc(id)
                    .map(bidMapper::toDto)
                    .orElseThrow(()->new BetNotFoundException("Лот с id: " + id +" не найден"));

        }else{
            logger.debug("Лот с id : {} найден", id);
            throw new LotNotFoundException("Лот с id: " + id +" не найден");
        }
    }

    @SneakyThrows
    @Override
    public BidDTO getMostFrequentBidder(int id){
        logger.debug("Получение имени максимально ставившего раз по лоту с id : {} ", id);
        if(lotRepository.existsById(id)){
            logger.debug("Получено имя максимально ставившего раз по лоту с id : {} ", id);
            return bidRepository.maxCountBidder(id);
        }else{
            logger.debug("Лот с id : {} найден", id);
            throw new LotNotFoundException("Лот с id: " + id +" не найден");
        }
    }

    @SneakyThrows
    @Override
    public void createBid(int id, BidderDTO bidderDTO) {
        logger.debug("Создание новой ставки по лоту с id : {} ", id);
        Lot lot = lotRepository.findById(id).orElseThrow(()-> new LotNotFoundException("Лот с id: " + id +" не найден"));
        if(lot.getStatus()== Status.CREATED||lot.getStatus()==Status.STOPPED){
            logger.debug("Лот с id : {} находится в неправильном статусе", id);
            throw new LotWrongStatusException("Лот с id: " + id +" находится в неправильном статусе");
        }
        Bid bid = new Bid();
        bid.setName(bidderDTO.getName());
        bid.setLot(lot);
        bidRepository.save(bid);
        logger.debug("Создана новая ставка по лоту с id : {} ", id);
    }
}