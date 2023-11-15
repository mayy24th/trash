package christmas.view;

import java.text.DecimalFormat;
import java.util.HashMap;

public class OutputView {
    DecimalFormat decimalFormat = new DecimalFormat("#,###");

    public void printGreetingMessage() {
        System.out.println("안녕하세요! 우테코 식당 12월 이벤트 플래너입니다.");
    }

    public void printBenefitGuideMessage(int date) {
        System.out.println("12월 " + date + "일에 우테코가 식당에서 받을 이벤트 혜택 미리 보기!");
        System.out.println();
    }

    public void printOrderMenu(HashMap<String, Integer> orderList) {
        System.out.println("<주문 메뉴>");
        orderList.forEach((key, value) -> {
            System.out.println(key + " " + value + "개");
        });
        System.out.println();
    }

    public void printTotalOrderAmountBeforeDiscount(int totalAmount) {
        System.out.println("<할인 전 총주문 금액>");
        System.out.println(decimalFormat.format(totalAmount) + "원");
        System.out.println();
    }

    public void printGiftMenu(String gift) {
        System.out.println("<증정 메뉴>");
        System.out.println(gift);
        System.out.println();
    }

    public void printBenefitDetails(int totalBenefit) {
        System.out.println("<혜택 내역>");
        if (totalBenefit == 0) {
            System.out.println("없음");
            System.out.println();
        }
    }

    public void printDdayDiscount(int dDayDiscount) {
        System.out.println("크리스마스 디데이 할인: -" + decimalFormat.format(dDayDiscount) + "원");
    }

    public void printWeekendDiscount(int weekendDiscount, boolean isWeekend) {
        String weekdayOrWeekend = "평일 ";
        if (isWeekend) {
            weekdayOrWeekend = "주말 ";
        }
        System.out.println(weekdayOrWeekend + "할인: -" + decimalFormat.format(weekendDiscount) + "원");
    }

    public void printStarDayDiscount(int starDayDiscount) {
        System.out.println("특별 할인: -" + starDayDiscount + "원");
    }

    public void printGiftDiscount() {
        System.out.println("증정 이벤트: -25,000원");
        System.out.println();
    }

    public void printTotalBenefitAmount(int totalBenefit) {
        System.out.println("<총혜택 금액>");
        System.out.println("-" + decimalFormat.format(totalBenefit) + "원");
        System.out.println();
    }

    public void printAmountAfterDiscount(int AmountAfterDiscount) {
        System.out.println("<할인 후 예상 결제 금액>");
        System.out.println(decimalFormat.format(AmountAfterDiscount) + "원");
        System.out.println();
    }

    public void printEventBadge(String Badge) {
        System.out.println("<12월 이벤트 배지>");
        System.out.println(Badge);
    }

}
