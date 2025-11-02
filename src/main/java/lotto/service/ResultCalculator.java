package lotto.service;

import lotto.domain.Lotto;
import lotto.domain.WinningNumber;
import lotto.domain.WinningRank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ResultCalculator {
    private static final int PERCENTAGE_MULTIPLIER = 100;
    private static final int PROFIT_RATE_SCALE = 1;

    // calculateStatistics
    public Map<WinningRank, Integer> calculateStatistics(List<Lotto> lottos, WinningNumber winningNumber) {
        Map<WinningRank, Integer> statistics = initializeStatistics();
        countWinningRanks(lottos, winningNumber, statistics);
        return statistics;
    }

    private Map<WinningRank, Integer> initializeStatistics() {
        Map<WinningRank, Integer> statistics = new EnumMap<>(WinningRank.class);
        for (WinningRank rank : WinningRank.values()) {
            statistics.put(rank, 0);
        }
        return statistics;
    }

    private void countWinningRanks(List<Lotto> lottos, WinningNumber winningNumber,
                                   Map<WinningRank, Integer> statistics) {
        for (Lotto lotto : lottos) {
            WinningRank rank = determineRank(lotto, winningNumber);
            incrementRankCount(statistics, rank);
        }
    }

    private WinningRank determineRank(Lotto lotto, WinningNumber winningNumber) {
        LottoWinningChecker checker = new LottoWinningChecker();
        return checker.evaluate(lotto, winningNumber);
    }

    private void incrementRankCount(Map<WinningRank, Integer> statistics, WinningRank rank) {
        if (rank != null) {
            statistics.put(rank, statistics.get(rank) + 1);
        }
    }

    // calculateTotalPrize
    public long calculateTotalPrize(Map<WinningRank, Integer> statistics) {
        long totalPrize = 0;
        for (Map.Entry<WinningRank, Integer> entry : statistics.entrySet()) {
            totalPrize += calculateRankPrize(entry);
        }
        return totalPrize;
    }

    private long calculateRankPrize(Map.Entry<WinningRank, Integer> entry) {
        WinningRank rank = entry.getKey();
        int count = entry.getValue();
        return rank.getPrize() * count;
    }

    // calculateProfitRate
    public double calculateProfitRate(int purchaseAmount, long totalPrize) {
        BigDecimal prize = BigDecimal.valueOf(totalPrize);
        BigDecimal amount = BigDecimal.valueOf(purchaseAmount);
        BigDecimal rate = prize.divide(amount, PROFIT_RATE_SCALE + 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(PERCENTAGE_MULTIPLIER));
        return rate.setScale(PROFIT_RATE_SCALE, RoundingMode.HALF_UP).doubleValue();
    }

}
