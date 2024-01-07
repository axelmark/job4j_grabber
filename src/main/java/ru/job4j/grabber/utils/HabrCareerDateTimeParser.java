package ru.job4j.grabber.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class HabrCareerDateTimeParser implements DateTimeParser {

    @Override
    public LocalDateTime parse(String parse) {
        Instant instant = Instant.parse(parse);
        return LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
    }
}