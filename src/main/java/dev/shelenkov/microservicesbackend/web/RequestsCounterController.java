package dev.shelenkov.microservicesbackend.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class RequestsCounterController {

    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/requests")
    public Long getRequestsCount() {
        return counter.incrementAndGet();
    }
}
