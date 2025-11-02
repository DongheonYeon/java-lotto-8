package lotto.service;

import lotto.domain.Lotto;
import lotto.domain.WinningNumber;
import lotto.domain.WinningRank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class ResultCalculatorTest {
    @Test
    @DisplayName("당첨 통계를 계산해야 한다")
    void calculateStatistics() {
        // given
        WinningNumber winningNumber = new WinningNumber(
                List.of(1, 2, 3, 4, 5, 6),
                7
        );
        List<Lotto> lottos = List.of(
                new Lotto(List.of(1, 2, 3, 4, 5, 6)),  // 1등: 6개 일치
                new Lotto(List.of(1, 2, 3, 4, 5, 7)),  // 2등: 5개 + 보너스
                new Lotto(List.of(1, 2, 3, 4, 5, 8)),  // 3등: 5개 일치
                new Lotto(List.of(1, 2, 3, 4, 9, 10)), // 4등: 4개 일치
                new Lotto(List.of(1, 2, 3, 11, 12, 13)) // 5등: 3개 일치
        );
        ResultCalculator calculator = new ResultCalculator();

        // when
        Map<WinningRank, Integer> statistics = calculator.calculateStatistics(lottos, winningNumber);

        // then
        assertThat(statistics.get(WinningRank.FIRST)).isEqualTo(1);
        assertThat(statistics.get(WinningRank.SECOND)).isEqualTo(1);
        assertThat(statistics.get(WinningRank.THIRD)).isEqualTo(1);
        assertThat(statistics.get(WinningRank.FOURTH)).isEqualTo(1);
        assertThat(statistics.get(WinningRank.FIFTH)).isEqualTo(1);
    }

    @Test
    @DisplayName("당첨되지 않은 로또는 통계 계산되지 않아야 한다")
    void calculateStatisticsWithNoWinning() {
        // given
        WinningNumber winningNumber = new WinningNumber(
                List.of(1, 2, 3, 4, 5, 6),
                7
        );
        List<Lotto> lottos = List.of(
                new Lotto(List.of(8, 9, 10, 11, 12, 13)),  // 당첨 안됨
                new Lotto(List.of(14, 15, 16, 17, 18, 19)) // 당첨 안됨
        );
        ResultCalculator calculator = new ResultCalculator();

        // when
        Map<WinningRank, Integer> statistics = calculator.calculateStatistics(lottos, winningNumber);

        // then
        assertThat(statistics.get(WinningRank.FIRST)).isEqualTo(0);
        assertThat(statistics.get(WinningRank.SECOND)).isEqualTo(0);
        assertThat(statistics.get(WinningRank.THIRD)).isEqualTo(0);
        assertThat(statistics.get(WinningRank.FOURTH)).isEqualTo(0);
        assertThat(statistics.get(WinningRank.FIFTH)).isEqualTo(0);
    }

    @Test
    @DisplayName("같은 등수가 여러 개 있을 때 올바르게 계산해야 한다")
    void calculateStatisticsWithMultipleSameRank() {
        // given
        WinningNumber winningNumber = new WinningNumber(
                List.of(1, 2, 3, 4, 5, 6),
                7
        );
        List<Lotto> lottos = List.of(
                new Lotto(List.of(1, 2, 3, 11, 12, 13)), // 5등: 3개 일치
                new Lotto(List.of(1, 2, 3, 14, 15, 16)), // 5등: 3개 일치
                new Lotto(List.of(1, 2, 3, 17, 18, 19))  // 5등: 3개 일치
        );
        ResultCalculator calculator = new ResultCalculator();

        // when
        Map<WinningRank, Integer> statistics = calculator.calculateStatistics(lottos, winningNumber);

        // then
        assertThat(statistics.get(WinningRank.FIFTH)).isEqualTo(3);
    }

    @Test
    @DisplayName("총 당첨 금액을 올바르게 계산해야 한다")
    void calculateTotalPrize() {
        // given
        Map<WinningRank, Integer> statistics = Map.of(
                WinningRank.FIFTH, 1,  // 5000원
                WinningRank.FOURTH, 0,
                WinningRank.THIRD, 0,
                WinningRank.SECOND, 0,
                WinningRank.FIRST, 0
        );
        ResultCalculator calculator = new ResultCalculator();

        // when
        long totalPrize = calculator.calculateTotalPrize(statistics);

        // then
        assertThat(totalPrize).isEqualTo(5000);
    }

    @Test
    @DisplayName("여러 등수의 총 당첨 금액을 올바르게 계산해야 한다")
    void calculateTotalPrizeWithMultipleRanks() {
        // given
        Map<WinningRank, Integer> statistics = Map.of(
                WinningRank.FIFTH, 2,   // 5000원 * 2 = 10000원
                WinningRank.FOURTH, 1,  // 50000원 * 1 = 50000원
                WinningRank.THIRD, 0,
                WinningRank.SECOND, 0,
                WinningRank.FIRST, 0
        );
        ResultCalculator calculator = new ResultCalculator();

        // when
        long totalPrize = calculator.calculateTotalPrize(statistics);

        // then
        assertThat(totalPrize).isEqualTo(60000);
    }

    @Test
    @DisplayName("수익률을 올바르게 계산해야 한다")
    void calculateProfitRate() {
        // given
        int purchaseAmount = 8000;
        long totalPrize = 5000;
        ResultCalculator calculator = new ResultCalculator();

        // when
        double profitRate = calculator.calculateProfitRate(purchaseAmount, totalPrize);

        // then
        assertThat(profitRate).isCloseTo(62.5, within(0.01));
    }

    @Test
    @DisplayName("수익률 계산 시 반올림이 올바르게 적용되어야 한다")
    void calculateProfitRateWithRounding() {
        // given
        int purchaseAmount = 14000;
        long totalPrize = 7250;
        ResultCalculator calculator = new ResultCalculator();

        // when
        double profitRate = calculator.calculateProfitRate(purchaseAmount, totalPrize);

        // then
        // 7250 / 14000 * 100 = 51.785... → 반올림하면 51.8
        assertThat(profitRate).isCloseTo(51.8, within(0.01));
    }
}
