package com.example.auction.controller;

import com.example.auction.dto.*;
import com.example.auction.service.BidService;
import com.example.auction.service.LotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lot")
@RequiredArgsConstructor
public class LotController {
    private final LotService lotService;
    private final BidService bidService;

    @GetMapping("/{id}/first")
    public BidDTO getFirstBidder(@PathVariable int id){
        return bidService.getFirstBidder(id);
    }

    @GetMapping("/{id}/frequent")
    public BidDTO getMostFrequentBidder(@PathVariable int id){
        return bidService.getMostFrequentBidder(id);
    }

    @GetMapping("/{id}")
    public FullLotDTO getFullLot(@PathVariable int id){
        return lotService.getFullLot(id);
    }

    @PostMapping("/{id}/start")
    public void startLotBidding(@PathVariable int id){
        lotService.startLotBidding(id);
    }

    @PostMapping("/{id}/bid")
    public void createBid(@PathVariable int id, @RequestBody @Valid BidderDTO bidderDTO){
        bidService.createBid(id, bidderDTO);
    }

    @PostMapping("/{id}/stop")
    public void stopLotBidding(@PathVariable int id){
        lotService.stopLotBidding(id);
    }
    @PostMapping
    public LotDTO createLot(@RequestBody @Valid CreateLotDTO createLotDTO){
        return lotService.createLot(createLotDTO);
    }

    @GetMapping
    public List<LotDTO> findLots(@RequestParam(value = "status", required = false) Status status,
                                 @RequestParam(value = "page", required = false, defaultValue = "0") int page){
        return lotService.findLots(status, page);
    }
    @GetMapping("/export")
    public ResponseEntity<Resource> getCSVFile(){

        String fileName = "lots";
        Resource resource = new ByteArrayResource(lotService.getCSVFile());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ fileName)
                .header(HttpHeaders.CONTENT_TYPE, "text/csv")
                .body(resource);
    }
}