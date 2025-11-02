package lotto.domain;

import lotto.domain.Lotto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LottoStorageTest {
    @Test
    @DisplayName("로또 목록을 저장하고 반환해야 한다")
    void getLottos() {
        // given
        List<Lotto> lottos = List.of(
                new Lotto(List.of(1, 2, 3, 4, 5, 6)),
                new Lotto(List.of(7, 8, 9, 10, 11, 12))
        );
        LottoStorage storage = new LottoStorage(lottos);

        // when
        List<Lotto> result = storage.getLottos();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(lottos);
    }

    @Test
    @DisplayName("저장된 로또의 개수를 반환해야 한다")
    void size() {
        // given
        List<Lotto> lottos = List.of(
                new Lotto(List.of(1, 2, 3, 4, 5, 6)),
                new Lotto(List.of(7, 8, 9, 10, 11, 12)),
                new Lotto(List.of(13, 14, 15, 16, 17, 18))
        );
        LottoStorage storage = new LottoStorage(lottos);

        // when
        int size = storage.size();

        // then
        assertThat(size).isEqualTo(3);
    }

    @Test
    @DisplayName("저장된 로또 목록은 불변이어야 한다")
    void immutableLottos() {
        // given
        List<Lotto> lottos = List.of(
                new Lotto(List.of(1, 2, 3, 4, 5, 6))
        );
        LottoStorage storage = new LottoStorage(lottos);

        // when
        List<Lotto> result = storage.getLottos();

        // then
        assertThat(result).isUnmodifiable();
    }
}
