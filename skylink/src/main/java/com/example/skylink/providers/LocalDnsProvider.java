package com.example.skylink.providers;

import com.example.skylink.dtos.DnsConfigRequest;
import com.example.skylink.dtos.DnsStatsResponse;
import com.example.skylink.dtos.DnsStatusResponse;
import com.example.skylink.exceptions.DnsException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@Profile("local")
public class LocalDnsProvider implements DnsProvider {

    @Override
    public DnsStatusResponse getStatus() {
        return executeSafely(
                () -> new DnsStatusResponse(true, "dnsmasq", "127.0.0.1"),
                "Unable to fetch DNS status"
        );
    }

    @Override
    public DnsStatsResponse getStats() {
        return executeSafely(
                () -> new DnsStatsResponse(100, 70, 30),
                "Unable to fetch DNS stats"
        );
    }

    @Override
    public void restart() {
        executeSafely(() -> {
            System.out.println("Local DNS service restarted");
        }, "Unable to restart DNS service");
    }

    @Override
    public void clearCache() {
        executeSafely(() -> {
            System.out.println("Local DNS cache cleared");
        }, "Unable to clear DNS cache");
    }

    @Override
    public void updateConfiguration(DnsConfigRequest request) {
        executeSafely(() -> {
            if (request == null || request.getUpstreamDns() == null || request.getUpstreamDns().isBlank()) {
                throw new DnsException("Upstream DNS cannot be empty");
            }

            System.out.println("Updating upstream DNS: " + request.getUpstreamDns());
        }, "Unable to update DNS configuration");
    }

    @Override
    public String testDnsQuery(String domain) {
        return "";
    }

    private void executeSafely(Runnable action, String errorMessage) {
        try {
            action.run();
        } catch (DnsException e) {
            throw e;
        } catch (Exception e) {
            throw new DnsException(errorMessage, e);
        }
    }

    private <T> T executeSafely(Supplier<T> action, String errorMessage) {
        try {
            return action.get();
        } catch (DnsException e) {
            throw e;
        } catch (Exception e) {
            throw new DnsException(errorMessage, e);
        }
    }
}