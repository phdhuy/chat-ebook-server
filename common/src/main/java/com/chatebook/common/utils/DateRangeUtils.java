package com.chatebook.common.utils;

import java.time.LocalDateTime;

public class DateRangeUtils {
  public record DateRange(LocalDateTime start, LocalDateTime end) {}

  public static DateRange getDateRange(Integer day, Integer month, Integer year) {
    if (day != null && (month == null || year == null)) {
      throw new IllegalArgumentException(
          "If day is specified, month and year must also be specified");
    }
    if (month != null && year == null) {
      throw new IllegalArgumentException("If month is specified, year must also be specified");
    }

    LocalDateTime now = LocalDateTime.now();
    int currentYear = now.getYear();
    int currentMonth = now.getMonthValue();

    LocalDateTime start;
    LocalDateTime end;

    if (day != null) {
      start = LocalDateTime.of(year, month, day, 0, 0, 0);
      end = start.plusDays(1).minusSeconds(1);
    } else if (month != null) {
      start = LocalDateTime.of(year, month, 1, 0, 0, 0);
      end = start.plusMonths(1).minusSeconds(1);
    } else if (year != null) {
      start = LocalDateTime.of(year, 1, 1, 0, 0, 0);
      end = start.plusYears(1).minusSeconds(1);
    } else {
      start = LocalDateTime.of(currentYear, currentMonth, 1, 0, 0, 0);
      end = start.plusMonths(1).minusSeconds(1);
    }
    return new DateRange(start, end);
  }
}
