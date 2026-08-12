package com.example.skylink.providers;

import com.example.skylink.dtos.DnsConfigRequest;
import com.example.skylink.dtos.DnsStatsResponse;
import com.example.skylink.dtos.DnsStatusResponse;

public interface DnsProvider {

    DnsStatusResponse getStatus();

    DnsStatsResponse getStats();

    void restart();

    void clearCache();

    void updateConfiguration(DnsConfigRequest request);
    String testDnsQuery(String domain);
}