package com.javamentor.qa.platform.models.dto.enums;

import java.time.LocalDateTime;

public enum Period {
    ALL,
    YEAR,
    MONTH,
    WEEK;
    public LocalDateTime getTrancedDate() {
        LocalDateTime res;
        switch (this) {
            case WEEK:
                res = LocalDateTime.now().minusWeeks(1);
                break;
            case MONTH:
                res = LocalDateTime.now().minusMonths(1);
                break;
            case YEAR:
                res = LocalDateTime.now().minusYears(1);;
                break;
            default:
                res = LocalDateTime.of(1900,01,01, 0,0);
        }
        return res;
    }

}
