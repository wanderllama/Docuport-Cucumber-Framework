package jw.demo.enums;

import java.time.Duration;

public enum WaitTime {

    EXTRA_SHORT(2500),
    SHORT(5000),
    REGULAR(10000),
    LONG(15000),
    EXTRA_LONG(30000),
    thisAmount(0);

    private final Integer millis;

    WaitTime(int milliseconds) {
        millis = milliseconds;
    }

    public Duration waitTime() {
        return Duration.ofMillis(millis);
    }

    public int amountOfSeconds() {
        return millis == 0 ? 0 : (millis / 1000);
    }

    public Duration ofMillis(int millis) {
        return Duration.ofMillis(millis);
    }

}
