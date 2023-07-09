package com.example.auction.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LotNotFoundException extends Exception {
    private static final Logger logger = LoggerFactory.getLogger(LotNotFoundException.class);

    public LotNotFoundException(String message) {
        super(message);
        logger.error(message);
    }
}