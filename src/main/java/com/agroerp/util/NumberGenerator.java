package com.agroerp.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class NumberGenerator {
    private final AtomicInteger sequence = new AtomicInteger(1000);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final int START_SEQUENCE = 1001;
    private static final int MAX_RETRIES = 100;

    public String next(String prefix) {
        return prefix + "-" + LocalDate.now().format(FORMATTER) + "-" + sequence.incrementAndGet();
    }

    public synchronized String nextFromDatabase(String prefix, Function<String, Optional<String>> latestNumber,
                                                Predicate<String> exists) {
        String datedPrefix = prefix + "-" + LocalDate.now().format(FORMATTER);
        int nextSequence = latestNumber.apply(datedPrefix)
                .map(number -> sequenceFrom(number, datedPrefix) + 1)
                .orElse(START_SEQUENCE);
        return firstAvailable(datedPrefix, nextSequence, exists);
    }

    private String firstAvailable(String datedPrefix, int nextSequence, Predicate<String> exists) {
        IntFunction<String> format = sequence -> datedPrefix + "-" + sequence;
        for (int sequence = nextSequence; sequence < nextSequence + MAX_RETRIES; sequence++) {
            String candidate = format.apply(sequence);
            if (!exists.test(candidate)) {
                return candidate;
            }
        }
        throw new IllegalStateException("Unable to allocate document number for " + datedPrefix);
    }

    private int sequenceFrom(String number, String datedPrefix) {
        Pattern pattern = Pattern.compile("^" + Pattern.quote(datedPrefix) + "-(\\d+)$");
        Matcher matcher = pattern.matcher(number);
        if (!matcher.matches()) {
            return START_SEQUENCE - 1;
        }
        return Integer.parseInt(matcher.group(1));
    }
}
