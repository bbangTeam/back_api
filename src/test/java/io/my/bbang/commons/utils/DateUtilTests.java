package io.my.bbang.commons.utils;

import static org.junit.jupiter.api.Assertions.*;

import io.my.bbang.commons.base.SpringBootTestBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Slf4j
class DateUtilTests {
    private DateUtil dateUtil;

    @BeforeEach
    void setUp() {
        dateUtil = new DateUtil();
    }

    @Test
    @DisplayName("string을 LocalDateTime으로 변환")
    void stringToLocalDateTimetest() {
        String beforeActual = "2021-09-03 11:10";
        LocalDateTime expected =
                LocalDateTime.of(2021, 9, 3, 11, 10);

        assertEquals(expected, dateUtil.stringToLocalDateTime(beforeActual));
    }

    @Test
    @DisplayName("string list를 LocalDateTime list로 변환")
    void stringListToLocalDateTimeListTest() {
        List<String> beforeActual = new ArrayList<>();
        List<LocalDateTime> expected = new ArrayList<>();
        String date = "2021-09-03 11:10";
        LocalDateTime localDateTime =
                LocalDateTime.of(2021, 9, 3, 11, 10);

        expected.add(localDateTime);
        beforeActual.add(date);

        assertEquals(expected,
                dateUtil.stringListToLocalDateTimeList(beforeActual));
    }

    @Test
    @DisplayName("LocalDateTime을 string 형식으로 변환")
    void localDateTimeToString() {
        String expected = "2021-09-01 11:10";
        LocalDateTime actual = LocalDateTime.of(2021, Month.SEPTEMBER, 1, 11, 10);

        assertEquals(expected, dateUtil.localDateTimeToString(actual));
    }

}
