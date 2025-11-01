package lotto.service;

import lotto.domain.Lotto;
import lotto.domain.WinningNumber;
import lotto.domain.WinningRank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class LottoWinningCheckerTest {

    private final LottoWinningChecker checker = new LottoWinningChecker();

    private Lotto ticket(int... nums) {
        return new Lotto(List.of(nums[0], nums[1], nums[2], nums[3], nums[4], nums[5]));
    }

    private WinningNumber winning(int bonus, int... nums) {
        return WinningNumber.of(List.of(nums[0], nums[1], nums[2], nums[3], nums[4], nums[5]), bonus);
    }

    @Test
    @DisplayName("6개 번호가 모두 일치하면 1등")
    void firstPrize_whenSixMatches() {
        var winning = winning(7, 1,2,3,4,5,6);
        var ticket = ticket(6,5,4,3,2,1); // 순서와 무관
        var rank = checker.evaluate(ticket, winning);
        assertThat(rank).isEqualTo(WinningRank.FIRST);
    }

    @Test
    @DisplayName("5개 번호 + 보너스 번호 일치하면 2등")
    void secondPrize_whenFivePlusBonus() {
        var winning = winning(7, 1,2,3,4,5,6);
        var ticket = ticket(1,2,3,4,5,7); // 5개 + 보너스
        var rank = checker.evaluate(ticket, winning);
        assertThat(rank).isEqualTo(WinningRank.SECOND);
    }

    @Test
    @DisplayName("5개 번호만 일치하면 3등")
    void thirdPrize_whenFiveNoBonus() {
        var winning = winning(7, 1,2,3,4,5,6);
        var ticket = ticket(1,2,3,4,5,45); // 5개 + 보너스 미일치
        var rank = checker.evaluate(ticket, winning);
        assertThat(rank).isEqualTo(WinningRank.THIRD);
    }

    @Test
    @DisplayName("4개 번호 일치하면 4등")
    void fourthPrize_whenFourMatches() {
        var winning = winning(7, 1,2,3,4,5,6);
        var ticket = ticket(1,2,3,4,45,44);
        var rank = checker.evaluate(ticket, winning);
        assertThat(rank).isEqualTo(WinningRank.FOURTH);
    }

    @Test
    @DisplayName("3개 번호 일치하면 5등")
    void fifthPrize_whenThreeMatches() {
        var winning = winning(7, 1,2,3,4,5,6);
        var ticket = ticket(1,2,3,45,44,43);
        var rank = checker.evaluate(ticket, winning);
        assertThat(rank).isEqualTo(WinningRank.FIFTH);
    }

    @Test
    @DisplayName("2개 이하 일치하면 당첨 없음")
    void none_whenLessThanThree() {
        var winning = winning(7, 1,2,3,4,5,6);
        var ticket = ticket(1,45,44,43,42,41); // 1개
        var rank = checker.evaluate(ticket, winning);
        assertThat(rank).isEqualTo(WinningRank.NONE);
    }

    @Test
    @DisplayName("보너스 번호는 5개 일치일 때만 2등 판단에 사용된다")
    void bonusOnlyMattersOnFiveMatches() {
        var winning = winning(7, 1,2,3,4,5,6);

        // 4개 + 보너스 = 4등 (보너스 무시)
        var t1 = ticket(1,2,3,4,7,45);
        assertThat(checker.evaluate(t1, winning)).isEqualTo(WinningRank.FOURTH);

        // 6개 일치 = 1등
        var t2 = ticket(1,2,3,4,5,6);
        assertThat(checker.evaluate(t2, winning)).isEqualTo(WinningRank.FIRST);
    }

    @Test
    @DisplayName("여러 장에 대해 계산한다")
    void countByRank_forManyTickets() {
        var winning = winning(7, 1,2,3,4,5,6);
        var tickets = List.of(
                ticket(1,2,3,4,5,6),   // 1등
                ticket(1,2,3,4,5,7),   // 2등
                ticket(1,2,3,4,5,45),  // 3등
                ticket(1,2,3,4,44,45), // 4등
                ticket(1,2,3,43,44,45),// 5등
                ticket(1,45,44,43,42,41) // NONE
        );

        Map<WinningRank, Long> stats = checker.countByRank(tickets, winning);

        assertThat(stats.getOrDefault(WinningRank.FIRST, 0L)).isEqualTo(1);
        assertThat(stats.getOrDefault(WinningRank.SECOND, 0L)).isEqualTo(1);
        assertThat(stats.getOrDefault(WinningRank.THIRD, 0L)).isEqualTo(1);
        assertThat(stats.getOrDefault(WinningRank.FOURTH, 0L)).isEqualTo(1);
        assertThat(stats.getOrDefault(WinningRank.FIFTH, 0L)).isEqualTo(1);
        assertThat(stats.getOrDefault(WinningRank.NONE, 0L)).isEqualTo(1);
    }

    private static TestCase c(WinningRank expected, int bonus, int[] ticket) {
        return new TestCase(expected, bonus, ticket);
    }

    static Stream<TestCase> cases() {
        // 기준 당첨: 1,2,3,4,5,6 / 보너스 7
        return Stream.of(
                c(WinningRank.FIRST,  7, new int[]{1,2,3,4,5,6}),
                c(WinningRank.SECOND, 7, new int[]{1,2,3,4,5,7}),
                c(WinningRank.THIRD,  7, new int[]{1,2,3,4,5,45}),
                c(WinningRank.FOURTH, 7, new int[]{1,2,3,4,44,45}),
                c(WinningRank.FIFTH,  7, new int[]{1,2,3,43,44,45}),
                c(WinningRank.NONE,   7, new int[]{1,45,44,43,42,41})
        );
    }

    @ParameterizedTest(name = "[{index}] 예상 등수={0}")
    @MethodSource("cases")
    void evaluate_tableDriven(TestCase tc) {
        var winning = WinningNumber.of(List.of(1,2,3,4,5,6), tc.bonus);
        var ticket  = new Lotto(List.of(tc.ticket[0], tc.ticket[1], tc.ticket[2], tc.ticket[3], tc.ticket[4], tc.ticket[5]));

        var rank = checker.evaluate(ticket, winning);

        assertThat(rank).isEqualTo(tc.expected);
    }

    private record TestCase(WinningRank expected, int bonus, int[] ticket) {}
}
