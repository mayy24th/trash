package christmas.domain;

public class Benefit {
    private static final String GIFT_NAME = "샴페인 1개";
    private static final String NONE_VALUE = "없음";
    private static final int CRITERIA_FOR_PRESENT_EVENT = 120_000;
    private static final int GIFT_PRICE = 25_000;
    private static final int ZERO_PRICE = 0;
    private static final int CHRISTMAS_DAY = 25;
    private static final int BASE_PRICE = 1_000;
    private static final int DISCOUNT_RATE = 100;
    private static final int DATE_OFFSET = 1;
    private static final int WEEKEND_DISCOUNT_RATE = 2_023;

    private final int date;
    private final int discountMenus;
    private final boolean isStarDay;
    private final int totalAmount;

    public Benefit(int date, int discountMenus, boolean isStarDay, int totalAmount) {
        this.date = date;
        this.discountMenus = discountMenus;
        this.isStarDay = isStarDay;
        this.totalAmount = totalAmount;
    }

    public int getWeekendDiscount() {
        return discountMenus * WEEKEND_DISCOUNT_RATE;
    }

    public int getTotalBenefitIncludeGift() {
        return getTotalBenefit() + getGiftDiscount();
    }

    public int getTotalBenefit() {
        return getDdayDiscount() + getStarDayDiscount() + getWeekendDiscount();
    }

    public int getStarDayDiscount() {
        if (isStarDay) {
            return BASE_PRICE;
        }
        return ZERO_PRICE;
    }

    public int getDdayDiscount() {
        if (date > CHRISTMAS_DAY) {
            return ZERO_PRICE;
        }
        return BASE_PRICE + (DISCOUNT_RATE * (date - DATE_OFFSET));
    }

    public int getGiftDiscount() {
        if (totalAmount > CRITERIA_FOR_PRESENT_EVENT) {
            return GIFT_PRICE;
        }
        return ZERO_PRICE;
    }

    public String getGift() {
        if (getGiftDiscount() != 0) {
            return GIFT_NAME;
        }
        return NONE_VALUE;
    }
}
