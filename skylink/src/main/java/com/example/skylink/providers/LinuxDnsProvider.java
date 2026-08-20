package com.example.skylink.providers;

import com.example.skylink.dtos.DnsConfigRequest;
import com.example.skylink.dtos.DnsStatsResponse;
import com.example.skylink.dtos.DnsStatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

@Component
@Profile("linux")
@Slf4j
public class LinuxDnsProvider implements DnsProvider {

    private static final String DNSMASQ_SERVICE = "dnsmasq";

    @Override
    public DnsStatusResponse getStatus() {

        try {
            String output = executeCommand(
                    "pgrep -x dnsmasq"
            );

            boolean running = !output.isBlank();

            return new DnsStatusResponse(
                    running,
                    DNSMASQ_SERVICE,
                    "127.0.0.1:5353"
            );

        } catch (Exception e) {

            log.error("Failed to get DNS status", e);

            return new DnsStatusResponse(
                    false,
                    DNSMASQ_SERVICE,
                    "127.0.0.1:5353"
            );
        }
    }

    @Override
    public DnsStatsResponse getStats() {

        /*
         * We will implement real dnsmasq statistics here later.
         *
         * For now, return safe values instead of crashing.
         */

        try {

            return new DnsStatsResponse(
                    0,
                    0,
                    0
            );

        } catch (Exception e) {

            log.error("Failed to get DNS statistics", e);

            return new DnsStatsResponse(
                    0,
                    0,
                    0
            );
        }
    }

    @Override
    public void restart() {

        try {

            executeCommand(
                    "sudo systemctl restart dnsmasq"
            );

            log.info("DNS service restarted successfully");

        } catch (Exception e) {

            log.error("Failed to restart DNS service", e);
        }
    }

    @Override
    public void clearCache() {

        try {

            /*
             * dnsmasq doesn't provide a simple universal
             * "clear cache" command.
             *
             * Restarting dnsmasq clears its in-memory cache.
             */

            executeCommand(
                    "sudo systemctl restart dnsmasq"
            );

            log.info("DNS cache cleared by restarting dnsmasq");

        } catch (Exception e) {

            log.error("Failed to clear DNS cache", e);
        }
    }

    @Override
    public void updateConfiguration(DnsConfigRequest request) {

        try {

            String upstreamDns = request.getUpstreamDns();

            if (upstreamDns == null || upstreamDns.isBlank()) {
                log.warn("Upstream DNS cannot be empty");
                return;
            }

            log.info(
                    "Requested upstream DNS change to {}",
                    upstreamDns
            );

            /*
             * We will implement safe configuration-file
             * modification here later.
             */

        } catch (Exception e) {

            log.error(
                    "Failed to update DNS configuration",
                    e
            );
        }
    }

    private String executeCommand(String command)
            throws IOException, InterruptedException {

        ProcessBuilder processBuilder = new ProcessBuilder(
                "cmd.exe",
                "/c",
                "wsl.exe",
                "-d",
                "Ubuntu-24.04",
                "--",
                "bash",
                "-c",
                command
        );

        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        String output =
                new String(process.getInputStream().readAllBytes());

        boolean finished =
                process.waitFor(5, TimeUnit.SECONDS);

        if (!finished) {
            process.destroyForcibly();
            throw new IOException("Linux command timed out");
        }

        if (process.exitValue() != 0) {
            throw new IOException(
                    "Linux command failed with exit code "
                            + process.exitValue()
                            + ". Output: "
                            + output
            );
        }

        return output.trim();
    }

    @Override
    public String testDnsQuery(String domain) {

        try {

            return executeCommand(
                    "wsl.exe -d Ubuntu-24.04 -- dig "
                            + domain
                            + " @127.0.0.1 -p 5353"
            );

        } catch (Exception e) {

            log.error("DNS query failed", e);

            return "DNS query failed: " + e.getMessage();
        }
    }
}