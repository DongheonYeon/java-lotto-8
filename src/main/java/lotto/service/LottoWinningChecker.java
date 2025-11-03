package lotto.service;

import lotto.domain.Lotto;
import lotto.domain.WinningNumber;
import lotto.domain.WinningRank;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class LottoWinningChecker {
    // 로또 하나 판정
    public WinningRank evaluate(Lotto ticket, WinningNumber winning) {
        List<Integer> t = ticket.getNumbers();
        List<Integer> w = winning.getWinningNumbers();

        int match = (int) t.stream().filter(w::contains).count();
        boolean bonus = t.contains(winning.getBonusNumber());

        return WinningRank.valueOf(match, bonus);
    }

    // 여러개 계산
    public Map<WinningRank, Long> countByRank(List<Lotto> tickets, WinningNumber winning) {
        return tickets.stream().map(t -> evaluate(t, winning))
            .collect(Collectors.groupingBy(r -> r, Collectors.counting()));
    }
}
