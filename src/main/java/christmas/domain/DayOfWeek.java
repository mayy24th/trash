package christmas.domain;

public enum DayOfWeek {
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY;

    public boolean isWeekend() {
        return this == FRIDAY || this == SATURDAY;
    }

    public boolean isStarDay() {
        return this == SUNDAY;
    }
}
