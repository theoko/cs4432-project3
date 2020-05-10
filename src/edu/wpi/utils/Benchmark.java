package edu.wpi.utils;

import java.time.Duration;
import java.time.Instant;

public class Benchmark {
    Instant start;

    public Benchmark() {
        start = Instant.now();
    }

    public long getTimeElapsed() {
        Instant finish = Instant.now();

        long timeElapsed = Duration.between(start, finish).toMillis();  //in millis
        return timeElapsed;
    }
}
