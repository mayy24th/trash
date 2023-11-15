package christmas.controller;

import christmas.domain.Badge;
import christmas.domain.Date;
import christmas.domain.Order;
import christmas.view.InputView;
import christmas.view.OutputView;

public class Controller {
    private static final OutputView outputView = new OutputView();
    private static final InputView inputView = new InputView();
    private static final int ZERO_PRICE = 0;

    private final Date date;
    private final Order order;

    public Controller() {
        outputView.printGreetingMessage();
        this.date = inputView.readVisitDate();
        this.order = inputView.readOrderInput(date);
        run();
    }

    private void run() {
        // 이벤트 안내 및 입력 부
        outputView.printBenefitGuideMessage(date.getDate());

        // 주문 메뉴 출력
        outputView.printOrderMenu(order.getOrderList());

        // 할인 전 총주문 금액
        outputView.printTotalOrderAmountBeforeDiscount(order.getTotalAmount());

        // 증정 메뉴
        outputView.printGiftMenu(order.getGift());

        // 혜택 내역
        outputView.printBenefitDetails(order.getTotalBenefitIncludeGift());
        benefitDetails();

        // 총 혜택 금액
        outputView.printTotalBenefitAmount(order.getTotalBenefitIncludeGift());

        // 할인 후 예상 결제 금액
        outputView.printAmountAfterDiscount(order.getAmountAfterDiscount());

        // 12월 이벤트 뱃지
        outputView.printEventBadge(Badge.findBadge(order.getTotalBenefitIncludeGift()).getName());
    }

    private void benefitDetails() {
        if (order.getDdayDiscount() != ZERO_PRICE) {
            outputView.printDdayDiscount(order.getDdayDiscount());
        }
        if (order.getWeekendDiscount() != ZERO_PRICE) {
            outputView.printWeekendDiscount(order.getWeekendDiscount(), date.isWeekend());
        }
        if (date.isStarDay()) {
            outputView.printStarDayDiscount(order.getStarDayDiscount());
        }
        if (order.getGiftDiscount() != 0) {
            outputView.printGiftDiscount();
        }
    }

}
