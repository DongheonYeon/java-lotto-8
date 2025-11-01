package lotto.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class WinningNumberTest {
    // 1. 생성자 테스트
    @DisplayName("당첨 번호와 보너스 번호로 WinningNumber를 생성한다.")
    @Test
    void createWinningNumber() {
        // given
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 7;

        // when
        WinningNumber winningNumber = new WinningNumber(winningNumbers, bonusNumber);

        // then
        assertThat(winningNumber).isNotNull();
    }

    @DisplayName("당첨 번호가 6개가 아니면 예외가 발생한다.")
    @Test
    void createWinningNumber_InvalidSize() {
        // given
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5);
        int bonusNumber = 7;

        // when & then
        assertThatThrownBy(() -> new WinningNumber(winningNumbers, bonusNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]")
                .hasMessageContaining("6개");
    }

    @DisplayName("당첨 번호에 중복이 있으면 예외가 발생한다.")
    @Test
    void createWinningNumber_DuplicatedWinningNumbers() {
        // given
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 5);
        int bonusNumber = 7;

        // when & then
        assertThatThrownBy(() -> new WinningNumber(winningNumbers, bonusNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]")
                .hasMessageContaining("중복");
    }

    @DisplayName("당첨 번호가 1~45 범위를 벗어나면 예외가 발생한다.")
    @Test
    void createWinningNumber_WinningNumberOutOfRange() {
        // given
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 46);
        int bonusNumber = 7;

        // when & then
        assertThatThrownBy(() -> new WinningNumber(winningNumbers, bonusNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]")
                .hasMessageContaining("범위");
    }

    @DisplayName("보너스 번호가 당첨 번호와 중복되면 예외가 발생한다.")
    @Test
    void createWinningNumber_BonusNumberDuplicated() {
        // given
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 6;

        // when & then
        assertThatThrownBy(() -> new WinningNumber(winningNumbers, bonusNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]")
                .hasMessageContaining("중복");
    }

    @DisplayName("보너스 번호가 1~45 범위를 벗어나면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, 46, 47, 100})
    void createWinningNumber_BonusNumberutOfRange(int invalidBonus) {
        // given
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);

        // when & then
        assertThatThrownBy(() -> new WinningNumber(winningNumbers, invalidBonus))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]")
                .hasMessageContaining("범위");
    }

    @DisplayName("경계값 테스트: 보너스 번호 1과 45는 정상적으로 생성된다.")
    @Test
    void createWinningNumber_BonusNumberBoundaryValues() {
        // 보너스 번호 1
        WinningNumber winningNumber1 = new WinningNumber(List.of(2, 3, 4, 5, 6, 7), 1);
        assertThat(winningNumber1).isNotNull();

        // 보너스 번호 45
        WinningNumber winningNumber2 = new WinningNumber(List.of(1, 2, 3, 4, 5, 6), 45);
        assertThat(winningNumber2).isNotNull();
    }

    // 2. getWinningNumbers() 테스트
    @DisplayName("당첨 번호 목록을 조회한다.")
    @Test
    void getWinningNumbers() {
        // given
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        WinningNumber winningNumber = new WinningNumber(winningNumbers, 7);

        // when
        List<Integer> result = winningNumber.getWinningNumbers();

        // then
        assertThat(result).containsExactly(1, 2, 3, 4, 5, 6);
    }

    @DisplayName("조회한 당첨 번호 목록은 불변이다.")
    @Test
    void getWinningNumbers_Immutable() {
        // given
        WinningNumber winningNumber = new WinningNumber(List.of(1, 2, 3, 4, 5, 6), 7);
        List<Integer> numbers = winningNumber.getWinningNumbers();

        // when & then
        assertThatThrownBy(() -> numbers.add(8))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    // 3. getBonusNumber() 테스트
    @DisplayName("보너스 번호를 조회한다.")
    @Test
    void getBonusNumber() {
        // given
        WinningNumber winningNumber = new WinningNumber(List.of(1, 2, 3, 4, 5, 6), 7);

        // when
        int bonusNumber = winningNumber.getBonusNumber();

        // then
        assertThat(bonusNumber).isEqualTo(7);
    }

    @DisplayName("다양한 보너스 번호를 조회한다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 7, 45})
    void getBonusNumber_Various(int bonus) {
        // given
        WinningNumber winningNumber = new WinningNumber(List.of(10, 11, 12, 13, 14, 15), bonus);

        // when
        int bonusNumber = winningNumber.getBonusNumber();

        // then
        assertThat(bonusNumber).isEqualTo(bonus);
    }
}
