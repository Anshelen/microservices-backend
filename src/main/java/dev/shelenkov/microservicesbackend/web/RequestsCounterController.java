package dev.shelenkov.microservicesbackend.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

import static net.logstash.logback.marker.Markers.append;

@RestController
@Slf4j
public class RequestsCounterController {

    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/requests")
    public Long getRequestsCount() {
        long result = counter.incrementAndGet();
        log.info(append("Request", result), "Request counter incremented");
        return result;
    }
}
