package christmas.domain;

import christmas.util.ErrorCode;
import java.util.Arrays;

public enum Menu {
    MUSHROOM_SOUP("양송이수프", 6_000, "APPETIZER"),
    TAPAS("타파스", 5_500, "APPETIZER"),
    CAESAR_SALAD("시저샐러드", 8_000, "APPETIZER"),
    T_BONE_STEAK("티본스테이크", 55_000, "MAIN"),
    BBQ_RIBS("바비큐립", 54_000, "MAIN"),
    SEAFOOD_PASTA("해산물파스타", 35_000, "MAIN"),
    CHRISTMAS_PASTA("크리스마스파스타", 25_000, "MAIN"),
    CHOCO_CAKE("초코케이크", 15_000, "DESSERT"),
    ICE_CREAM("아이스크림", 5_000, "DESSERT"),
    ZERO_COLA("제로콜라", 3_000, "BEVERAGE"),
    RED_WINE("레드와인", 60_000, "BEVERAGE"),
    CHAMPAGNE("샴페인", 25_000, "BEVERAGE");

    private String name;
    private int price;
    private String category;

    Menu(String name, int price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public static Menu findMenu(String name) {
        return Arrays.stream(Menu.values())
                .filter(menu -> menu.name.equals(name))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.INVALID_ORDER_VALUE.getMessage()));
    }
}

