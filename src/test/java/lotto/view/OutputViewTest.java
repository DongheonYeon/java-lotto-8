package lotto.view;

import lotto.domain.Lotto;
import lotto.domain.WinningRank;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class OutputViewTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("구매한 로또 개수를 올바르게 출력해야 한다")
    void printPurchaseCount() {
        // given
        int count = 8;

        // when
        OutputView.printPurchaseCount(count);

        // then
        String output = outputStream.toString();
        assertThat(output).contains("8개를 구매했습니다.");
    }

    @Test
    @DisplayName("로또 번호를 오름차순으로 정렬하여 출력해야 한다")
    void printLottos() {
        // given
        List<Lotto> lottos = List.of(
                new Lotto(List.of(8, 21, 23, 41, 42, 43)),
                new Lotto(List.of(3, 5, 11, 16, 32, 38))
        );

        // when
        OutputView.printLottos(lottos);

        // then
        String output = outputStream.toString();
        assertThat(output).contains("[8, 21, 23, 41, 42, 43]");
        assertThat(output).contains("[3, 5, 11, 16, 32, 38]");
    }

    @Test
    @DisplayName("당첨 통계를 올바르게 출력해야 한다")
    void printWinningStatistics() {
        // given
        Map<WinningRank, Integer> statistics = Map.of(
                WinningRank.FIFTH, 1,
                WinningRank.FOURTH, 0,
                WinningRank.THIRD, 0,
                WinningRank.SECOND, 0,
                WinningRank.FIRST, 0
        );

        // when
        OutputView.printWinningStatistics(statistics);

        // then
        String output = outputStream.toString();
        assertThat(output).contains("당첨 통계");
        assertThat(output).contains("---");
        assertThat(output).contains("3개 일치 (5,000원) - 1개");
        assertThat(output).contains("4개 일치 (50,000원) - 0개");
        assertThat(output).contains("5개 일치 (1,500,000원) - 0개");
        assertThat(output).contains("5개 일치, 보너스 볼 일치 (30,000,000원) - 0개");
        assertThat(output).contains("6개 일치 (2,000,000,000원) - 0개");
    }

    @Test
    @DisplayName("수익률을 소수점 첫째 자리까지 올바르게 출력해야 한다")
    void printProfitRate() {
        // given
        double profitRate = 62.5;

        // when
        OutputView.printProfitRate(profitRate);

        // then
        String output = outputStream.toString();
        assertThat(output).contains("총 수익률은 62.5%입니다.");
    }

    @Test
    @DisplayName("수익률이 정수일 때 .0을 포함하여 출력해야 한다")
    void printProfitRateInteger() {
        // given
        double profitRate = 100.0;

        // when
        OutputView.printProfitRate(profitRate);

        // then
        String output = outputStream.toString();
        assertThat(output).contains("총 수익률은 100.0%입니다.");
    }

    @Test
    @DisplayName("에러 메시지를 [ERROR]로 시작하여 출력해야 한다")
    void printErrorMessage() {
        // given
        String errorMessage = "로또 번호는 1부터 45 사이의 숫자여야 합니다.";

        // when
        OutputView.printErrorMessage(errorMessage);

        // then
        String output = outputStream.toString();
        assertThat(output).contains("[ERROR] 로또 번호는 1부터 45 사이의 숫자여야 합니다.");
    }
}
