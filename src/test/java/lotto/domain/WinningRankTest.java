package lotto.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WinningRankTest {
    @DisplayName("6개 일치하면 1등을 반환한다")
    @Test
    void match6_return1() {
        assertThat(WinningRank.valueOf(6, false)).isEqualTo(WinningRank.FIRST);
    }

    @DisplayName("5개 + 보너스 일치하면 2등을 반환한다")
    @Test
    void match5_bonus_return2() {
        assertThat(WinningRank.valueOf(5, true)).isEqualTo(WinningRank.SECOND);
    }

    @DisplayName("5개만 일치하면 3등을 반환한다")
    @Test
    void match5_return3() {
        assertThat(WinningRank.valueOf(5, false)).isEqualTo(WinningRank.THIRD);
    }

    @DisplayName("4개 일치하면 4등을 반환한다")
    @Test
    void match4_return4() {
        assertThat(WinningRank.valueOf(4, false)).isEqualTo(WinningRank.FOURTH);
    }

    @DisplayName("3개 일치하면 5등을 반환한다")
    @Test
    void match3_return5() {
        assertThat(WinningRank.valueOf(3, false)).isEqualTo(WinningRank.FIFTH);
    }

    @DisplayName("그 외에는 NONE을 반환한다")
    @Test
    void otherwise_returnNone() {
        assertThat(WinningRank.valueOf(2, false)).isEqualTo(WinningRank.NONE);
        assertThat(WinningRank.valueOf(0, false)).isEqualTo(WinningRank.NONE);
    }
}
