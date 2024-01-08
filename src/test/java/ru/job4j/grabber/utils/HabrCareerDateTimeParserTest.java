package ru.job4j.grabber.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class HabrCareerDateTimeParserTest {
    @Test
    public void getTimeParser() {
        HabrCareerDateTimeParser timeParser = new HabrCareerDateTimeParser();
        LocalDateTime dateTime = timeParser.parse("2024-01-05T12:27:41+03:00");
        assertThat(dateTime).isEqualTo("2024-01-05T09:27:41");
    }
}