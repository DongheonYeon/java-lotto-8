package lotto.domain;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class Lotto {
    private static final int LOTTO_NUMBERS_SIZE = 6;
    private static final int MIN_LOTTO_NUMBER = 1;
    private static final int MAX_LOTTO_NUMBER = 45;
    private static final String ERROR_INVALID_SIZE = "[ERROR] 로또 번호는 6개여야 합니다.";
    private static final String ERROR_INVALID_RANGE = "[ERROR] 로또 번호는 1부터 45 사이의 숫자여야 합니다.";
    private static final String ERROR_DUPLICATED_NUMBERS = "[ERROR] 로또 번호는 중복될 수 없습니다.";

    private final List<Integer> numbers;

    public Lotto(List<Integer> numbers) {
        validate(numbers);
        this.numbers = numbers;
    }

    private void validate(List<Integer> numbers) {
        validateSize(numbers);
        validateRange(numbers);
        validateDuplicated(numbers);
    }

    private void validateSize(List<Integer> numbers) {
        if (numbers.size() != LOTTO_NUMBERS_SIZE) {
            throw new IllegalArgumentException(ERROR_INVALID_SIZE);
        }
    }

    private void validateRange(List<Integer> numbers) {
        boolean hasInvalidNumber = numbers.stream()
                .anyMatch(number -> number < MIN_LOTTO_NUMBER || number > MAX_LOTTO_NUMBER);

        if (hasInvalidNumber) {
            throw new IllegalArgumentException(ERROR_INVALID_RANGE);
        }
    }

    private void validateDuplicated(List<Integer> numbers) {
        Set<Integer> uniqueNumbers = new HashSet<>(numbers);
        if (uniqueNumbers.size() != numbers.size()) {
            throw new IllegalArgumentException(ERROR_DUPLICATED_NUMBERS);
        }
    }

    public int countMatch(List<Integer> winningNumbers) {
        return (int) numbers.stream()
                .filter(winningNumbers::contains)
                .count();
    }

    public boolean contains(int number) {
        return numbers.contains(number);
    }

    public List<Integer> getNumbers() {
        return List.copyOf(numbers);
    }

    // TODO: 추가 기능 구현
}
