package com.example.auction.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LotWrongStatusException extends Exception {
    private static final Logger logger = LoggerFactory.getLogger(LotWrongStatusException.class);

    public LotWrongStatusException(String message){
        super(message);
        logger.error(message);
    }
}