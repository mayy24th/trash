package christmas;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumingThat;

import camp.nextstep.edu.missionutils.test.NsTest;
import christmas.domain.Badge;
import christmas.domain.Date;
import christmas.domain.Order;
import christmas.util.ErrorCode;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class ApplicationTest extends NsTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    @ParameterizedTest
    @CsvSource(value = {"'26', '타파스-1,제로콜라-1', '없음'", "'8', '티본스테이크-1,바비큐립-1,초코케이크-2,제로콜라-1', '산타'",
            "'25', '아이스크림-1', '별'", "'10', '바비큐립-4', '산타'"})
    @DisplayName("총 혜택금액에 따라 이벤트 배지가 부여된다.")
    void 이벤트_배지_부여_테스트(String inputDate, String inputOrder, String expectBadge) {
        // given
        var date = new Date(inputDate);
        var order = new Order(date, inputOrder);
        var badge = Badge.findBadge(order.getTotalBenefitIncludeGift());

        // when then
        assert Objects.equals(badge.getName(), expectBadge);
    }

    @ParameterizedTest
    @CsvSource(value = {"'3', '티본스테이크-2,레드와인-1', 25000", "'25', '티본스테이크-1,바비큐립-1,제로콜라-1', 0"})
    @DisplayName("할인 전 총주문 금액이 12만원 이상일 경우 샴페인 1개가 증정된다.")
    void 증정_이벤트_테스트(String inputDate, String inputOrder, int expectDiscount) {
        // given
        var date = new Date(inputDate);
        var order = new Order(date, inputOrder);
        var status = "False";

        // when
        if (order.getTotalAmount() >= 120_000) {
            status = "True";
        }

        // then
        assumingThat(status.equalsIgnoreCase("true"), () -> {
            assertThat(order.getGift()).contains("샴페인");
            assertThat(order.getGiftDiscount()).isEqualTo(expectDiscount);
        });
        assumingThat(status.equalsIgnoreCase("false"), () -> {
            assertThat(order.getGift()).contains("없음");
            assertThat(order.getGiftDiscount()).isEqualTo(expectDiscount);
        });
    }

    @ParameterizedTest
    @CsvSource(value = {"'1', '초코케이크-3,제로콜라-1', 0", "'8', '티본스테이크-1,바비큐립-1,제로콜라-1', 4046"})
    @DisplayName("주말에는 메인 메뉴가 개당 2_023원씩 할인된다.")
    void 주말_할인_테스트(String inputDate, String inputOrder, int expectDiscount) {
        // given
        var date = new Date(inputDate);
        var order = new Order(date, inputOrder);

        // when then
        assertThat(date.isWeekend()).isTrue();
        assertThat(order.getWeekendDiscount()).isEqualTo(expectDiscount);
    }

    @ParameterizedTest
    @CsvSource(value = {"'4', '초코케이크-3,제로콜라-1', 6069", "'25', '티본스테이크-1,바비큐립-1,제로콜라-1', 0"})
    @DisplayName("평일에는 디저트 메뉴가 개당 2_023원씩 할인된다.")
    void 평일_할인_테스트(String inputDate, String inputOrder, int expectDiscount) {
        // given
        var date = new Date(inputDate);
        var order = new Order(date, inputOrder);

        // when then
        assertThat(date.isWeekend()).isFalse();
        assertThat(order.getWeekendDiscount()).isEqualTo(expectDiscount);
    }

    @ParameterizedTest
    @CsvSource(value = {"'15', '타파스-1,제로콜라-1', 2400", "'25', '티본스테이크-1,바비큐립-1,초코케이크-2,제로콜라-1', 3400"})
    @DisplayName("크리스마스 디데이 할인은 1일 1_000원을 시작으로 25일 3_4000원까지 적용되며 매일 100원씩 증가한다.")
    void 크리스마스_디데이_할인_테스트(String inputDate, String inputOrder, int expectDiscount) {
        // given
        var date = new Date(inputDate);
        var order = new Order(date, inputOrder);

        // when then
        assertThat(order.getDdayDiscount()).isEqualTo(expectDiscount);
    }

    @ParameterizedTest
    @CsvSource(value = {"'1', '시저샐러드-7,양송이수프-15'", "'3', '해산물파스타-21'"})
    @DisplayName("한번에 20개가 넘는 주문의 경우, 에러가 발생한다.")
    void 총_주문수_20개_초과_테스트(String inputDate, String inputOrder) {
        // given
        var date = new Date(inputDate);

        // when then
        assertThrows(IllegalArgumentException.class, () -> new Order(date, inputOrder),
                ErrorCode.INVALID_ORDER_VALUE.getMessage());
    }

    @ParameterizedTest
    @CsvSource(value = {"'1', '시저샐러드-5,양송이수프-15'", "'3', '해산물파스타-20'"})
    @DisplayName("메뉴는 한번에 20개 까지만 주문 가능하다.")
    void 총_주문수_20개_이하_테스트(String inputDate, String inputOrder) {
        // given
        var date = new Date(inputDate);
        var order = new Order(date, inputOrder);

        // when then
        assertThat(order.getTotalOrderCount()).isLessThan(21);
    }

    @ParameterizedTest
    @CsvSource(value = {"'1', '제로콜라-10'", "'3', '레드와인-1,샴페인-5'"})
    @DisplayName("음료의 주문만 주어지는 경우, 에러가 발생한다.")
    void 음료메뉴만_입력_테스트(String inputDate, String inputOrder) {
        // given
        var date = new Date(inputDate);

        // when then
        assertThrows(IllegalArgumentException.class, () -> new Order(date, inputOrder),
                ErrorCode.INVALID_ORDER_VALUE.getMessage());
    }

    @ParameterizedTest
    @CsvSource(value = {"'1', '타파스-1,타파스-3'", "'3', '티본스테이크-1,바비큐립-1,초코케이크-2,바비큐립-1'"})
    @DisplayName("중복된 메뉴 입력의 경우, 에러가 발생한다.")
    void 중복된_메뉴_입력_테스트(String inputDate, String inputOrder) {
        // given
        var date = new Date(inputDate);

        // when then
        assertThrows(IllegalArgumentException.class, () -> new Order(date, inputOrder),
                ErrorCode.INVALID_ORDER_VALUE.getMessage());
    }

    @ParameterizedTest
    @CsvSource(value = {"'1', '타파스-1,,제로콜라-1'", "'3', '티본스테이크-1,바비큐 립-1,초코케이크-2,제로콜라-1'"})
    @DisplayName("잘못된 형식의 입력일 경우, 에러가 발생한다.")
    void 잘못된_형식의_메뉴_입력_테스트(String inputDate, String inputOrder) {
        // given
        var date = new Date(inputDate);

        // when then
        assertThrows(IllegalArgumentException.class, () -> new Order(date, inputOrder),
                ErrorCode.INVALID_ORDER_VALUE.getMessage());
    }

    @ParameterizedTest
    @CsvSource(value = {"'1', '타파스-0,제로콜라-1'", "'3', '티본스테이크--1,바비큐립-1,초코케이크-2,제로콜라-1'"})
    @DisplayName("메뉴의 주문수가 0 이하일 경우, 에러가 발생한다.")
    void 주문_메뉴숫자_0이하_입력_테스트(String inputDate, String inputOrder) {
        // given
        var date = new Date(inputDate);

        // when then
        assertThrows(IllegalArgumentException.class, () -> new Order(date, inputOrder),
                ErrorCode.INVALID_ORDER_VALUE.getMessage());
    }

    @ParameterizedTest
    @CsvSource(value = {"'1', '타파스-1,제로사이다-1'", "'3', '티본스테이크-1,바비큐립-1,치즈케이크-2,제로콜라-1'"})
    @DisplayName("없는 메뉴 주문의 경우, 에러가 발생한다.")
    void 없는_메뉴_주문_입력_테스트(String inputDate, String inputOrder) {
        // given
        var date = new Date(inputDate);

        // when then
        assertThrows(IllegalArgumentException.class, () -> new Order(date, inputOrder),
                ErrorCode.INVALID_ORDER_VALUE.getMessage());
    }

    @ParameterizedTest
    @CsvSource(value = {"'26', '타파스-1,제로콜라-1', 2", "'3', '티본스테이크-1,바비큐립-1,초코케이크-2,제로콜라-1', 5"})
    @DisplayName("정확한 입력 형식이 주어지는 경우 오더 객체가 생성된다.")
    void 유효한_주문_입력_테스트(String inputDate, String inputOrder, int expectOrderCount) {
        // given
        var date = new Date(inputDate);
        var order = new Order(date, inputOrder);

        // when then
        assert order.getTotalOrderCount() == expectOrderCount;
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "0", "32", " 1"})
    @DisplayName("1 ~ 31 사이의 값이 아니면 날짜 객체 생성시, 에러가 발생한다")
    void 유효하지_않은_날짜_입력_테스트(String givenValue) {
        // when then
        assertThrows(IllegalArgumentException.class, () -> new Date(givenValue),
                ErrorCode.INVALID_DATE_VALUE.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3", "4", "5"})
    @DisplayName("1 ~ 31 사이의 값으로 날짜 객체가 생성된다.")
    void 유효한_날짜_입력_테스트(String givenValue) {
        // when
        var sut = new Date(givenValue);

        // then
        assert sut.getDate() == Integer.parseInt(givenValue);
    }

    @Test
    void 모든_타이틀_출력() {
        assertSimpleTest(() -> {
            run("3", "티본스테이크-1,바비큐립-1,초코케이크-2,제로콜라-1");
            assertThat(output()).contains(
                    "<주문 메뉴>",
                    "<할인 전 총주문 금액>",
                    "<증정 메뉴>",
                    "<혜택 내역>",
                    "<총혜택 금액>",
                    "<할인 후 예상 결제 금액>",
                    "<12월 이벤트 배지>"
            );
        });
    }

    @Test
    void 혜택_내역_없음_출력() {
        assertSimpleTest(() -> {
            run("26", "타파스-1,제로콜라-1");
            assertThat(output()).contains("<혜택 내역>" + LINE_SEPARATOR + "없음");
        });
    }

    @Test
    void 날짜_예외_테스트() {
        assertSimpleTest(() -> {
            runException("a");
            assertThat(output()).contains("[ERROR] 유효하지 않은 날짜입니다. 다시 입력해 주세요.");
        });
    }

    @Test
    void 주문_예외_테스트() {
        assertSimpleTest(() -> {
            runException("3", "제로콜라-a");
            assertThat(output()).contains("[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요.");
        });
    }

    @Override
    protected void runMain() {
        Application.main(new String[]{});
    }
}
