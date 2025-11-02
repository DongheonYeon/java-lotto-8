package lotto.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lotto.domain.Lotto;
import lotto.domain.WinningNumber;
import lotto.domain.WinningRank;
import lotto.service.LottoGenerator;
import lotto.service.LottoWinningChecker;
import lotto.service.ResultCalculator;
import lotto.view.InputView;
import lotto.view.OutputView;

public class LottoController {
    private final InputView inputView;
    private final LottoGenerator lottoGenerator;
    private final LottoWinningChecker lottoWinningChecker;
    private final ResultCalculator resultCalculator;

    public LottoController() {
        this.inputView = new InputView();
        this.lottoGenerator = new LottoGenerator();
        this.lottoWinningChecker = new LottoWinningChecker();
        this.resultCalculator = new ResultCalculator();
    }

    public void run() {
        int purchaseAmount = readPurchaseAmountWithRetry();
        List<Lotto> lottos = lottoGenerator.generateByAmount(purchaseAmount);
        OutputView.printPurchaseCount(lottos.size());
        OutputView.printLottos(lottos);

        WinningNumber winningNumber = readWinningNumberWithRetry();
        Map<WinningRank, Long> resultLong = lottoWinningChecker.countByRank(lottos, winningNumber);
        Map<WinningRank, Integer> result = convertToInteger(resultLong);

        OutputView.printWinningStatistics(result);
        long totalPrize = resultCalculator.calculateTotalPrize(result);
        double profitRate = resultCalculator.calculateProfitRate(purchaseAmount, totalPrize);
        OutputView.printProfitRate(profitRate);
    }

    private int readPurchaseAmountWithRetry() {
        while (true) {
            try {
                return inputView.readPurchaseAmount();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private WinningNumber readWinningNumberWithRetry() {
        while (true) {
            try {
                List<Integer> winningNumbers = inputView.readWinningNumbers();
                int bonusNumber = inputView.readBonusNumber();
                return new WinningNumber(winningNumbers, bonusNumber);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private Map<WinningRank, Integer> convertToInteger(Map<WinningRank, Long> longMap) {
        return longMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().intValue()
                ));
    }
}