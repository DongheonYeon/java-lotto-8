package lotto.view;

import lotto.domain.Lotto;
import lotto.domain.WinningRank;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class OutputView {

    private static final String PURCHASE_COUNT_FORMAT = "\n%d개를 구매했습니다.";
    private static final String WINNING_STATISTICS_HEADER = "\n당첨 통계";
    private static final String WINNING_STATISTICS_DIVIDER = "---";
    private static final String PROFIT_RATE_FORMAT = "총 수익률은 %s%%입니다.";
    private static final String ERROR_PREFIX = "[ERROR] ";
    private static final DecimalFormat PRIZE_FORMATTER = new DecimalFormat("#,###");
    private static final DecimalFormat RATE_FORMATTER = new DecimalFormat("#,##0.0");


    // printPurchaseCount
    public static void printPurchaseCount(int count) {
        System.out.println(String.format(PURCHASE_COUNT_FORMAT, count));
    }

    // printLottos
    public static void printLottos(List<Lotto> lottos) {
        for (Lotto lotto : lottos) {
            System.out.println(lotto);
        }
    }

    // printWinningStatistics
    public static void printWinningStatistics(Map<WinningRank, Integer> statistics) {
        System.out.println(WINNING_STATISTICS_HEADER);
        System.out.println(WINNING_STATISTICS_DIVIDER);
        printStatisticsInOrder(statistics);
    }

    private static void printStatisticsInOrder(Map<WinningRank, Integer> statistics) {
        printRankStatistic(WinningRank.FIFTH, statistics);
        printRankStatistic(WinningRank.FOURTH, statistics);
        printRankStatistic(WinningRank.THIRD, statistics);
        printRankStatistic(WinningRank.SECOND, statistics);
        printRankStatistic(WinningRank.FIRST, statistics);
    }

    private static void printRankStatistic(WinningRank rank, Map<WinningRank, Integer> statistics) {
        int count = statistics.getOrDefault(rank, 0);
        String matchDescription = (rank == WinningRank.SECOND)
                ? "5개 일치, 보너스 볼 일치"
                : rank.getMatchCount() + "개 일치";
        String formattedPrize = PRIZE_FORMATTER.format(rank.getPrize());
        System.out.println(String.format("%s (%s원) - %d개", matchDescription, formattedPrize, count));
    }

    // printProfitRate
    public static void printProfitRate(double profitRate) {
        String formattedRate = RATE_FORMATTER.format(profitRate);
        System.out.println(String.format(PROFIT_RATE_FORMAT, formattedRate));
    }

    // printErrorMessage
    public static void printErrorMessage(String message) {
        System.out.println(ERROR_PREFIX + message);
    }
}
