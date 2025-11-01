package lotto.service;

import camp.nextstep.edu.missionutils.Randoms;
import lotto.domain.Lotto;

import java.util.ArrayList;
import java.util.List;

public class LottoGenerator {
    private static final int MIN_LOTTO_NUMBER = 1;
    private static final int MAX_LOTTO_NUMBER = 45;
    private static final int SINGLE_LOTTO_SIZE = 6;
    private static final int LOTTO_PRICE = 1000;
    private static final String ERROR_NOT_DIVISIBLE_BY_THOUSAND = "[ERROR] 구입 금액은 1,000원 단위여야 합니다.";

    public List<Lotto> generateByAmount(int amount) {
        validateAmount(amount);
        int count = amount / LOTTO_PRICE;
        return generate(count);
    }

    public List<Lotto> generate(int count) {
        List<Lotto> lottos = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            lottos.add(generateLotto());
        }
        return lottos;
    }

    private void validateAmount(int amount) {
        if (amount % LOTTO_PRICE != 0) {
            throw new IllegalArgumentException(ERROR_NOT_DIVISIBLE_BY_THOUSAND);
        }
    }

    private Lotto generateLotto() {
        List<Integer> numbers = Randoms.pickUniqueNumbersInRange(
                MIN_LOTTO_NUMBER,
                MAX_LOTTO_NUMBER,
                SINGLE_LOTTO_SIZE
        );
        return new Lotto(numbers);
    }
}
