package lotto.domain;

import lotto.domain.Lotto;

import java.util.Collections;
import java.util.List;

public class LottoStorage {
    private final List<Lotto> lottos;

    public LottoStorage(List<Lotto> lottos) {
        this.lottos = List.copyOf(lottos);
    }

    // getLottos
    public List<Lotto> getLottos() {
        return Collections.unmodifiableList(lottos);
    }

    // size
    public int size() {
        return lottos.size();
    }
}
