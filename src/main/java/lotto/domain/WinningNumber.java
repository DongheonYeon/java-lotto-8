package lotto.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class WinningNumber {
    private static final int SINGLE_LOTTO_SIZE = 6;
    private static final int MIN_LOTTO_NUMBER = 1;
    private static final int MAX_LOTTO_NUMBER = 45;

    private final List<Integer> winningNumbers;
    private final int bonusNumber;

    public WinningNumber(List<Integer> winningNumbers, int bonusNumber) {
        validateNotNull(winningNumbers);
        validateSize(winningNumbers);
        validateNoNullElements(winningNumbers);
        validateRange(winningNumbers);
        validateNoDuplicate(winningNumbers);
        validateBonusRange(bonusNumber);
        validateBonusNotDuplicated(winningNumbers, bonusNumber);

        // 오름차순 정렬된 불변 리스트로 보관
        this.winningNumbers = Collections.unmodifiableList(
                winningNumbers.stream()
                        .sorted()
                        .collect(Collectors.toList())
        );
        this.bonusNumber = bonusNumber;
    }
    
    // 정적 팩토리 메서드 방식
    public static WinningNumber of(List<Integer> numbers, int bonus) {
        return new WinningNumber(numbers, bonus);
    }
    
    public List<Integer> getWinningNumbers() {
        return winningNumbers;
    }

    public int getBonusNumber() {
        return bonusNumber;
    }

    // ======= validators =======
    private void validateNotNull(List<Integer> numbers) {
        if (numbers == null) {
            throw new IllegalArgumentException("[ERROR] 당첨 번호는 null일 수 없습니다.");
        }
    }

    private void validateSize(List<Integer> numbers) {
        if (numbers.size() != SINGLE_LOTTO_SIZE) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 6개여야 합니다.");
        }
    }

    private void validateNoNullElements(List<Integer> numbers) {
        if (numbers.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("[ERROR] 로또 번호에 null이 포함될 수 없습니다.");
        }
    }

    private void validateRange(List<Integer> numbers) {
        boolean out = numbers.stream().anyMatch(n -> n < MIN_LOTTO_NUMBER || n > MAX_LOTTO_NUMBER);
        if (out) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 1부터 45 사이의 범위여야 합니다.");
        }
    }

    private void validateNoDuplicate(List<Integer> numbers) {
        if (new HashSet<>(numbers).size() != SINGLE_LOTTO_SIZE) {
            throw new IllegalArgumentException("[ERROR] 당첨 번호에 중복이 존재합니다.");
        }
    }

    private void validateBonusRange(int bonus) {
        if (bonus < MIN_LOTTO_NUMBER || bonus > MAX_LOTTO_NUMBER) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호는 1부터 45 사이의 범위여야 합니다.");
        }
    }

    private void validateBonusNotDuplicated(List<Integer> numbers, int bonus) {
        if (numbers.contains(bonus)) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호가 당첨 번호와 중복됩니다.");
        }
    }
}
