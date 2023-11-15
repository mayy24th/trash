package christmas.domain;

import christmas.util.ErrorCode;
import christmas.util.MyPair;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Order {
    private static final int MAX_ORDER_COUNT = 20;
    private static final int MIN_SIZE = 2;
    private static final String APPETIZER = "APPETIZER";
    private static final String MAIN = "MAIN";
    private static final String DESSERT = "DESSERT";

    private final HashMap<String, Integer> orderList;
    private final Date date;
    private final Benefit benefit;

    public Order(Date date, String orderLines) {
        this.orderList = createOrderList(splitByComma(orderLines));
        validateNonBeverageOrder();
        validateTotalCount(getTotalOrderCount());
        this.date = date;
        this.benefit = new Benefit(date.getDate(), getWeekendDiscountCount(), date.isStarDay(), getTotalAmount());
    }

    public int getStarDayDiscount(){
        return benefit.getStarDayDiscount();
    }

    public int getTotalOrderCount() {
        int totalOrderCount = 0;
        for (Integer count : orderList.values()) {
            totalOrderCount += count;
        }
        return totalOrderCount;
    }

    public int getGiftDiscount() {
        return benefit.getGiftDiscount();
    }

    public int getWeekendDiscount() {
        return benefit.getWeekendDiscount();
    }

    public int getDdayDiscount() {
        return benefit.getDdayDiscount();
    }

    public int getWeekendDiscountCount() {
        if (date.isWeekend()) {
            return getCategoryOrderCount(MAIN);
        }
        return getCategoryOrderCount(DESSERT);
    }

    public int getAmountAfterDiscount() {
        return getTotalAmount() - getTotalBenefit();
    }

    public String getGift() {
        return benefit.getGift();
    }

    public int getTotalBenefitIncludeGift() {
        return benefit.getTotalBenefitIncludeGift();
    }

    public int getTotalBenefit() {
        return benefit.getTotalBenefit();
    }

    public int getTotalAmount() {
        AtomicInteger totalAmount = new AtomicInteger();
        orderList.forEach((key, value) -> {
            totalAmount.addAndGet(Menu.findMenu(key).getPrice() * value);
        });
        return totalAmount.get();
    }

    public HashMap<String, Integer> getOrderList() {
        return orderList;
    }

    private int getCategoryOrderCount(String category) {
        AtomicInteger count = new AtomicInteger(0);
        orderList.forEach((key, value) -> {
            Menu menu = Menu.findMenu(key);
            if (menu.getCategory().equals(category)) {
                count.addAndGet(value);
            }
        });
        return count.get();
    }

    private void validator(HashMap<String, Integer> orderList, MyPair<String, Integer> order) {
        validateDuplicate(orderList, order.getName());
        validateMenu(order.getName());
        validateCount(order.getCount());
    }

    private void validateNonBeverageOrder() {
        int appetizerCount = getCategoryOrderCount(APPETIZER);
        int mainCount = getCategoryOrderCount(MAIN);
        int dessertCount = getCategoryOrderCount(DESSERT);

        if (appetizerCount + mainCount + dessertCount == 0) {
            throw new IllegalArgumentException(ErrorCode.INVALID_ORDER_VALUE.getMessage());
        }
    }

    private void validateCount(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException(ErrorCode.INVALID_ORDER_VALUE.getMessage());
        }
    }

    private void validateMenu(String name) {
        Menu.findMenu(name);
    }

    private void validateTotalCount(int totalOrderCount) {
        if (totalOrderCount > MAX_ORDER_COUNT) {
            throw new IllegalArgumentException(ErrorCode.INVALID_ORDER_VALUE.getMessage());
        }
    }

    private void validateDuplicate(HashMap<String, Integer> orderList, String name) {
        if (orderList.containsKey(name)) {
            throw new IllegalArgumentException(ErrorCode.INVALID_ORDER_VALUE.getMessage());
        }
    }

    private HashMap<String, Integer> createOrderList(List<String> orderLines) {
        HashMap<String, Integer> orderList = new HashMap<>();
        MyPair<String, Integer> order;
        try {
            for (String orderLine : orderLines) {
                order = splitByBar(orderLine);
                validator(orderList, order);
                orderList.put(order.getName(), order.getCount());
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(ErrorCode.INVALID_ORDER_VALUE.getMessage());
        }

        return orderList;
    }

    private MyPair<String, Integer> splitByBar(String orderLine) {
        List<String> parts = List.of(orderLine.split("-"));
        if (parts.size() < MIN_SIZE) {
            throw new IllegalArgumentException(ErrorCode.INVALID_ORDER_VALUE.getMessage());
        }
        String name = parts.get(0);
        Integer count = Integer.parseInt(parts.get(1));
        return new MyPair<>(name, count);
    }

    private List<String> splitByComma(String order) {
        try {
            return List.of(order.split(","));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(ErrorCode.INVALID_ORDER_VALUE.getMessage());
        }
    }
}
