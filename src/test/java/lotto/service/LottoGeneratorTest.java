package lotto.service;

import lotto.domain.Lotto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static camp.nextstep.edu.missionutils.test.Assertions.assertRandomNumberInRangeTest;
import static camp.nextstep.edu.missionutils.test.Assertions.assertRandomUniqueNumbersInRangeTest;
import static org.assertj.core.api.Assertions.*;

public class LottoGeneratorTest {
    private final LottoGenerator lottoGenerator = new LottoGenerator();

    @DisplayName("구입 금액이 8000원이면 8장을 발행한다.")
    @Test
    void generateByAmount_8000() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    // when
                    List<Lotto> lottos = lottoGenerator.generateByAmount(8000);

                    // then
                    assertThat(lottos).hasSize(8);
                },
                List.of(8, 21, 23, 41, 42, 43),
                List.of(3, 5, 11, 16, 32, 38),
                List.of(7, 11, 16, 35, 36, 44),
                List.of(1, 8, 11, 31, 41, 42),
                List.of(13, 14, 16, 38, 42, 45),
                List.of(7, 11, 30, 40, 42, 43),
                List.of(2, 13, 22, 32, 38, 45),
                List.of(1, 3, 5, 14, 22, 45)
        );
    }

    @DisplayName("금액이 1000원 단위가 아니면 예외가 발생한다.")
    @Test
    void generateByAmount_NotDivisibleBy1000() {
        // when & then
        assertThatThrownBy(() -> lottoGenerator.generateByAmount(1500))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]")
                .hasMessageContaining("1,000원 단위");
    }

    @DisplayName("각 로또 번호는 1~45 범위의 중복 없는 6개 숫자로 구성된다.")
    @Test
    void eachLottoHasValidNumbers() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    // when
                    List<Lotto> lottos = lottoGenerator.generateByAmount(5000);

                    // then
                    for (Lotto lotto : lottos) {
                        List<Integer> numbers = lotto.getNumbers();

                        // 6개 확인
                        assertThat(numbers).hasSize(6);

                        // 중복 없는지 확인
                        Set<Integer> uniqueNumbers = new HashSet<>(numbers);
                        assertThat(uniqueNumbers).hasSize(6);

                        // 1~45 범위 확인
                        for (int number : numbers) {
                            assertThat(number).isBetween(1, 45);
                        }
                    }
                },
                List.of(1, 2, 3, 4, 5, 6),
                List.of(7, 8, 9, 10, 11, 12),
                List.of(13, 14, 15, 16, 17, 18),
                List.of(19, 20, 21, 22, 23, 24),
                List.of(25, 26, 27, 28, 29, 30)
        );
    }
}
