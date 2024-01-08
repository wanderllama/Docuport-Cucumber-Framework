package jw.demo.enums;

import java.time.Duration;

public enum Wait {

    TWO(2000),
    FIVE(5000),
    TEN(10000),
    FIFTEEN(15000),
    THIRTY(30000),
    FOR(0);

    private final Integer millis;

    Wait(int milliseconds) {
        millis = milliseconds;
    }

    public Duration secondsDuration() {
        return Duration.ofMillis(millis);
    }

    public int seconds() {
        return millis == 0 ? 0 : (millis / 1000);
    }

    public Duration millisDuration(int millis) {
        return Duration.ofMillis(millis);
    }

    public Duration secondsDuration(int seconds) {
        return Duration.ofSeconds(seconds);
    }

}
