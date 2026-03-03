package ru.job4j.grabber.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HabrCareerDateTimeParserTest {
    @Test
    void testDifferentOffsets() {
        assertEquals(new HabrCareerDateTimeParser().parse("2026-02-24T12:07:22+00:00"), LocalDateTime.of(2026, 2, 24, 12, 7, 22));
        assertEquals(new HabrCareerDateTimeParser().parse("2026-02-24T12:07:22+01:00"), LocalDateTime.of(2026, 2, 24, 12, 7, 22));
        assertEquals(new HabrCareerDateTimeParser().parse("2026-02-24T12:07:22+18:00"), LocalDateTime.of(2026, 2, 24, 12, 7, 22));
    }

    @Test
    void testBoundaryDates() {
        assertEquals(new HabrCareerDateTimeParser().parse("1970-01-01T00:00:00+00:00"), LocalDateTime.of(1970, 1, 1, 0, 0, 0));
        assertEquals(new HabrCareerDateTimeParser().parse("9999-12-31T23:59:59+14:00"), LocalDateTime.of(9999, 12, 31, 23, 59, 59));
        assertEquals(new HabrCareerDateTimeParser().parse("2028-02-29T12:00:00+03:00"), LocalDateTime.of(2028, 2, 29, 12, 0, 0));
    }

    @Test
    void testInvalidInput() {
        assertThrows(DateTimeParseException.class, () -> new HabrCareerDateTimeParser().parse("2026-02-30T12:00:00+03:00"));
        assertThrows(DateTimeParseException.class, () -> new HabrCareerDateTimeParser().parse("2026-13-01T12:00:00+03:00"));
    }

}