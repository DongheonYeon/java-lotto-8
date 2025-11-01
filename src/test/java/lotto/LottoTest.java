package lotto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LottoTest {
    @Test
    void 로또_번호의_개수가_6개가_넘어가면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 6, 7)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("로또 번호에 중복된 숫자가 있으면 예외가 발생한다.")
    @Test
    void 로또_번호에_중복된_숫자가_있으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 5)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // TODO: 추가 기능 구현에 따른 테스트 코드 작성
    @DisplayName("로또 번호가 1보다 작으면 예외가 발생한다.")
    @Test
    void 로또_번호가_1보다_작으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(0, 1, 2, 3, 4, 5)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]");
    }

    @DisplayName("로또 번호가 45보다 크면 예외가 발생한다.")
    @Test
    void 로또_번호가_45보다_크면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 46)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]");
    }

    @DisplayName("당첨 번호와 일치하는 번호 개수를 반환한다.")
    @Test
    void 당첨_번호와_일치하는_번호_개수를_반환한다() {
        // given
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        List<Integer> winningNumbers = List.of(1, 2, 3, 7, 8, 9);

        // when
        int matchCount = lotto.countMatch(winningNumbers);

        // then
        assertThat(matchCount).isEqualTo(3);
    }

    @DisplayName("당첨 번호와 하나도 일치하지 않으면 0을 반환한다.")
    @Test
    void 당첨_번호와_하나도_일치하지_않으면_0을_반환한다() {
        // given
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        List<Integer> winningNumbers = List.of(7, 8, 9, 10, 11, 12);

        // when
        int matchCount = lotto.countMatch(winningNumbers);

        // then
        assertThat(matchCount).isEqualTo(0);
    }

    @DisplayName("특정 번호가 포함되어 있으면 true를 반환한다.")
    @Test
    void 특정_번호가_포함되어_있으면_true를_반환한다() {
        // given
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));

        // when & then
        assertThat(lotto.contains(1)).isTrue();
        assertThat(lotto.contains(6)).isTrue();
    }

    @DisplayName("특정 번호가 포함되어 있지 않으면 false를 반환한다.")
    @Test
    void 특정_번호가_포함되어_있지_않으면_false를_반환한다() {
        // given
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));

        // when & then
        assertThat(lotto.contains(7)).isFalse();
        assertThat(lotto.contains(45)).isFalse();
    }

    @DisplayName("getNumbers()로 로또 번호 목록을 조회한다.")
    @Test
    void getNumbers로_로또_번호_목록을_조회한다() {
        // given
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));

        // when
        List<Integer> numbers = lotto.getNumbers();

        // then
        assertThat(numbers).containsExactly(1, 2, 3, 4, 5, 6);
    }
}
