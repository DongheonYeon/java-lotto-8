package lotto.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import camp.nextstep.edu.missionutils.Console;
import java.io.ByteArrayInputStream;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class InputViewTest {

    private final InputView inputView = new InputView();

    @AfterEach
    void closeConsole() {
        Console.close();
    }

    private void provideInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    @DisplayName("구입 금액을 정상적으로 입력받는다")
    @Test
    void readPurchaseAmount_ValidInput() {
        // given
        provideInput("8000");

        // when
        int amount = inputView.readPurchaseAmount();

        // then
        assertThat(amount).isEqualTo(8000);
    }

    @DisplayName("구입 금액이 1000원 단위가 아니면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(strings = {"500", "1500", "8300", "999"})
    void readPurchaseAmount_NotDivisibleByThousand(String input) {
        // given
        provideInput(input);

        // when & then
        assertThatThrownBy(inputView::readPurchaseAmount)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]")
                .hasMessageContaining("구입 금액은 1,000원 단위로 입력해야 합니다");
    }

    @DisplayName("구입 금액이 숫자가 아니면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(strings = {"abc", "1000원"})
    void readPurchaseAmount_NotNumber(String input) {
        // given
        provideInput(input);

        // when & then
        assertThatThrownBy(inputView::readPurchaseAmount)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]")
                .hasMessageContaining("숫자를 입력해야 합니다");
    }

    @DisplayName("당첨 번호를 정상적으로 입력받는다")
    @Test
    void readWinningNumbers_ValidInput() {
        // given
        provideInput("1,2,3,4,5,6");

        // when
        List<Integer> numbers = inputView.readWinningNumbers();

        // then
        assertThat(numbers).containsExactly(1, 2, 3, 4, 5, 6);
    }

    @DisplayName("당첨 번호를 공백과 함께 입력해도 정상 처리된다")
    @Test
    void readWinningNumbers_WithSpaces() {
        // given
        provideInput("1, 2, 3, 4, 5, 6");

        // when
        List<Integer> numbers = inputView.readWinningNumbers();

        // then
        assertThat(numbers).containsExactly(1, 2, 3, 4, 5, 6);
    }

    @DisplayName("당첨 번호가 6개가 아니면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(strings = {"1,2,3,4,5", "1,2,3,4,5,6,7", "1,2,3", "1"})
    void readWinningNumbers_InvalidCount(String input) {
        // given
        provideInput(input);

        // when & then
        assertThatThrownBy(inputView::readWinningNumbers)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]")
                .hasMessageContaining("당첨 번호는 쉼표(,)로 구분된 6개의 숫자여야 합니다.");
    }

    @DisplayName("당첨 번호에 숫자가 아닌 값이 포함되면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(strings = {"1,2,3,4,5,a", "1,2,,4,5,6"})
    void readWinningNumbers_NotNumber(String input) {
        // given
        provideInput(input);

        // when & then
        assertThatThrownBy(inputView::readWinningNumbers)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]")
                .hasMessageContaining("숫자를 입력해야 합니다");
    }

    @DisplayName("보너스 번호를 정상적으로 입력받는다")
    @Test
    void readBonusNumber_ValidInput() {
        // given
        provideInput("7");

        // when
        int bonusNumber = inputView.readBonusNumber();

        // then
        assertThat(bonusNumber).isEqualTo(7);
    }

    @DisplayName("보너스 번호가 숫자가 아니면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(strings = {"abc"})
    void readBonusNumber_NotNumber(String input) {
        // given
        provideInput(input);

        // when & then
        assertThatThrownBy(inputView::readBonusNumber)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]")
                .hasMessageContaining("숫자를 입력해야 합니다");
    }

    @DisplayName("보너스 번호에 공백이 있어도 정상 처리된다")
    @Test
    void readBonusNumber_WithSpaces() {
        // given
        provideInput("  7  ");

        // when
        int bonusNumber = inputView.readBonusNumber();

        // then
        assertThat(bonusNumber).isEqualTo(7);
    }
}
