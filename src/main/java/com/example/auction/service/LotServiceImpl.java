package com.example.auction.service;

import com.example.auction.dto.CreateLotDTO;
import com.example.auction.dto.FullLotDTO;
import com.example.auction.dto.LotDTO;
import com.example.auction.dto.Status;
import com.example.auction.exception.LotNotFoundException;
import com.example.auction.mapper.BidMapper;
import com.example.auction.mapper.LotMapper;
import com.example.auction.pojo.Bid;
import com.example.auction.pojo.Lot;
import com.example.auction.repository.BidRepository;
import com.example.auction.repository.LotRepository;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.emitter.EmitterException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Data
@Service
public class LotServiceImpl implements LotService {
    private final LotRepository lotRepository;
    private final BidRepository bidRepository;
    private final LotMapper lotMapper;
    private final BidMapper bidMapper;

    private static final Logger logger = LoggerFactory.getLogger(LotServiceImpl.class);

    @SneakyThrows
    @Override
    public FullLotDTO getFullLot(int id) {
        logger.debug("Поиск лота по id: {}", id);
        Lot lot = lotRepository.findById(id).orElseThrow(() -> new LotNotFoundException("Лот с id: " + id + " не найден"));
        List<Bid> bidList = bidRepository.findAllByLot_Id(id);
        Bid lastBid = bidList.stream().max(Comparator.comparing(Bid::getDateTime)).orElse(new Bid());
        int currentPrice = bidList.size() * lot.getBidPrice() + lot.getStartPrice();
        logger.debug("Лот с id : {} найден", id);
        return lotMapper.toFullLotDTO(lot, currentPrice, bidMapper.toDto(lastBid));
    }

    @SneakyThrows
    @Override
    public void startLotBidding(int id) {
        logger.debug("Поиск лота по id: {} для начала торгов", id);
        Lot lot = lotRepository.findById(id).orElseThrow(() -> new LotNotFoundException("Лот с id: " + id + " не найден"));
        lot.setStatus(Status.STARTED);
        lotRepository.save(lot);
        logger.debug("Торги по лоту с id: {} начаты", id);
    }

    @SneakyThrows
    @Override
    public void stopLotBidding(int id) {
        logger.debug("Поиск лота по id: {} для остановки торгов", id);
        Lot lot = lotRepository.findById(id).orElseThrow(() -> new LotNotFoundException("Лот с id: " + id + " не найден"));
        lot.setStatus(Status.STOPPED);
        lotRepository.save(lot);
        logger.debug("Торги по лоту с id: {} остановлены", id);
    }

    @Override
    public LotDTO createLot(CreateLotDTO createLotDTO) {
        logger.debug("Создан новый лот {}", createLotDTO.getTitle());
        return lotMapper.toLotDTO(lotRepository.save(lotMapper.toLot(createLotDTO)));
    }

    @Override
    public List<LotDTO> findLots(@Nullable Status status, int page) {
        PageRequest pageRequest;
        Page<Lot> lotPage;
        logger.debug("Поиск всех лотов со статусом {}", status);
        if (!(status == null)) {
            pageRequest = PageRequest.of(page, 10);
            lotPage = lotRepository.findAllByStatus(status, pageRequest);
            logger.debug("Найдены лоты со статусом {}", status);
            return lotPage.stream().map(lotMapper::toLotDTO).toList();
        } else {
            pageRequest = PageRequest.of(page, 10);
            lotPage = lotRepository.findAll(pageRequest);
            logger.debug("Лоты найдены");
            return lotPage.stream().map(lotMapper::toLotDTO).toList();
        }
    }

    @Override
    public byte[] getCSVFile() {
        lotRepository.getAllLotsForCsv().forEach(x -> System.out.println(Arrays.toString(x)));
        logger.debug("Начало экпорта лотов в файл с расширением csv");

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader("id", "title", "status", "lastBidder", "currentPrice").build();

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8), csvFormat)) {
            for (Object[] obj : lotRepository.getAllLotsForCsv()) {
                printer.printRecord(obj);
            }
            printer.flush();
            logger.debug("Лоты экспортированы в файл с расширением csv");
            return byteArrayOutputStream.toByteArray();
        } catch (IOException exception) {
            logger.debug("Ошибка экспорта лотов в файл с расширением csv");
            exception.printStackTrace();
        }
        return null;
    }
}
