package com.example.skylink.controllers;

import com.example.skylink.dtos.DnsConfigRequest;
import com.example.skylink.dtos.DnsStatsResponse;
import com.example.skylink.dtos.DnsStatusResponse;
import com.example.skylink.providers.DnsProvider;
import com.example.skylink.services.DnsCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dns")
@RequiredArgsConstructor
public class DnsController {

    private final DnsCacheService dnsCacheService;
    private final DnsProvider dnsProvider;

    @GetMapping("/status")
    public ResponseEntity<DnsStatusResponse> getStatus() {
        return ResponseEntity.ok(
                dnsCacheService.getStatus()
        );
    }

    @GetMapping("/stats")
    public ResponseEntity<DnsStatsResponse> getStats() {
        return ResponseEntity.ok(
                dnsCacheService.getStats()
        );
    }

    @PostMapping("/restart")
    public ResponseEntity<Void> restart() {

        dnsCacheService.restart();

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/clear")
    public ResponseEntity<Void> clearCache() {

        dnsCacheService.clearCache();

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/config")
    public ResponseEntity<Void> updateConfiguration(
            @RequestBody DnsConfigRequest request) {

        dnsCacheService.updateConfiguration(request);

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/linux-test")
    public ResponseEntity<String> linuxTest() {

        return ResponseEntity.ok(
                dnsCacheService.getStatus().toString()
        );
    }
    @GetMapping("/test-query")
    public ResponseEntity<String> testQuery(
            @RequestParam String domain) {

        return ResponseEntity.ok(
                dnsCacheService.testDnsQuery(domain)
        );
    }
}