package io.my.bbang.commons.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DateUtil {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public List<LocalDateTime> stringListToLocalDateTimeList(List<String> list) {
        return list.stream()
                .map(this::stringToLocalDateTime)
                .collect(Collectors.toList());
    }

    public LocalDateTime stringToLocalDateTime(String date) {
        return LocalDateTime.parse(date, dateTimeFormatter);
    }

    public String localDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime.format(dateTimeFormatter);
    }

}
