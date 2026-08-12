package com.example.skylink.services.serviceImpl;

import com.example.skylink.dtos.DnsConfigRequest;
import com.example.skylink.dtos.DnsStatsResponse;
import com.example.skylink.dtos.DnsStatusResponse;
import com.example.skylink.providers.DnsProvider;
import com.example.skylink.services.DnsCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DnsCacheServiceImpl implements DnsCacheService {

    private final DnsProvider dnsProvider;

    @Override
    public DnsStatusResponse getStatus() {
        return dnsProvider.getStatus();
    }

    @Override
    public DnsStatsResponse getStats() {
        return dnsProvider.getStats();
    }

    @Override
    public void restart() {
        dnsProvider.restart();
    }

    @Override
    public void clearCache() {
        dnsProvider.clearCache();
    }

    @Override
    public void updateConfiguration(DnsConfigRequest request) {
        dnsProvider.updateConfiguration(request);
    }

    @Override
    public String testDnsQuery(String domain) {
        return dnsProvider.testDnsQuery(domain);
    }
}