package com.example.auction.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetNotFoundException extends Exception {
    private static final Logger logger = LoggerFactory.getLogger(BetNotFoundException.class);

    public BetNotFoundException(String message) {
        super(message);
        logger.error(message);
    }
}