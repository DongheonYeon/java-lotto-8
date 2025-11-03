package lotto.domain;

import java.util.Arrays;

public enum WinningRank {
    FIRST(6, false, 2_000_000_000),
    SECOND(5, true, 30_000_000),
    THIRD(5, false, 1_500_000),
    FOURTH(4, false, 50_000),
    FIFTH(3, false, 5_000),
    NONE(0, false, 0);

    private final int matchCount;
    private final boolean requiresBonus;
    private final int prize;

    WinningRank(int matchCount, boolean requiresBonus, int prize) {
        this.matchCount = matchCount;
        this.requiresBonus = requiresBonus;
        this.prize = prize;
    }

    public static WinningRank valueOf(int matchCount, boolean hasBonus) {
        // 2등(5개 + 보너스)
        if (matchCount == 5 && hasBonus) {
            return SECOND;
        }

        // 나머지는 matchCount 기준으로 판별
        return findByMatchCount(matchCount);
    }

    private static WinningRank findByMatchCount(int matchCount) {
        return Arrays.stream(values())
                .filter(rank -> rank.isMatch(matchCount))
                .findFirst()
                .orElse(NONE);
    }

    private boolean isMatch(int matchCount) {
        return this.matchCount == matchCount && !this.requiresBonus;
    }

    public int getPrize() {
        return prize;
    }

    public int getMatchCount() {
        return matchCount;
    }

    public boolean isRequiresBonus() {
        return requiresBonus;
    }
}
