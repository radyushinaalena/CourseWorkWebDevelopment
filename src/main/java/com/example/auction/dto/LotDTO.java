package com.example.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LotDTO {
    private int id;
    private Status status;
    private String title;
    private String description;
    private int startPrice;
    private int bidPrice;
}