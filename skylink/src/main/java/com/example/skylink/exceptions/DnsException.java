package com.example.skylink.exceptions;

public class DnsException extends RuntimeException {

    public DnsException(String message) {
        super(message);
    }

    public DnsException(String message, Throwable cause) {
        super(message, cause);
    }
}