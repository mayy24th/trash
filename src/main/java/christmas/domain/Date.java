package christmas.domain;

import christmas.util.ErrorCode;

public class Date {
    public static final int DATE_RANGE_START = 1;
    public static final int DATE_RANGE_END = 31;
    public static final int A_WEEK = 7;

    private final int date;
    private final DayOfWeek dayOfWeek;

    public Date(String date) {
        this.date = validateRange(validateNumeric(date));
        this.dayOfWeek = DayOfWeek.values()[this.date % A_WEEK];
    }

    public boolean isWeekend() {
        return dayOfWeek.isWeekend();
    }

    public boolean isStarDay() {
        return dayOfWeek.isStarDay() || this.date == 25;
    }

    public int getDate() {
        return date;
    }

    private int validateRange(int date) {
        if (date < DATE_RANGE_START || date > DATE_RANGE_END) {
            throw new IllegalArgumentException(ErrorCode.INVALID_DATE_VALUE.getMessage());
        }
        return date;
    }

    private int validateNumeric(String input) {
        int date;
        try {
            date = Integer.parseInt(input);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(ErrorCode.INVALID_DATE_VALUE.getMessage());
        }
        return date;
    }
}
