package com.example.skylink.dtos;

public record DnsStatsResponse(
        long totalQueries,
        long cacheHits,
        long cacheMisses
) {
}