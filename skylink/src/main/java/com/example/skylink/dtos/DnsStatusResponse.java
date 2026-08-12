package com.example.skylink.dtos;

public record DnsStatusResponse(
        boolean running,
        String service,
        String server
) {
}