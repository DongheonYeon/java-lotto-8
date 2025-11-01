package lotto.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InputView {
    private static final String PURCHASE_AMOUNT_MESSAGE = "구입금액을 입력해 주세요.";
    private static final String WINNING_NUMBERS_MESSAGE = "\n당첨 번호를 입력해 주세요.";
    private static final String BONUS_NUMBER_MESSAGE = "\n보너스 번호를 입력해 주세요.";
    private static final String DELIMITER = ",";

    private static final String ERROR_NOT_A_NUMBER = "[ERROR] 숫자를 입력해야 합니다.";
    private static final String ERROR_NOT_DIVISIBLE_BY_THOUSAND = "[ERROR] 구입 금액은 1,000원 단위로 입력해야 합니다.";
    private static final String ERROR_INVALID_FORMAT = "[ERROR] 당첨 번호는 쉼표(,)로 구분된 6개의 숫자여야 합니다.";

    public int readPurchaseAmount() {
        System.out.println(PURCHASE_AMOUNT_MESSAGE);
        String input = Console.readLine();
        return parsePurchaseAmount(input);
    }

    public List<Integer> readWinningNumbers() {
        System.out.println(WINNING_NUMBERS_MESSAGE);
        String input = Console.readLine();
        return parseWinningNumbers(input);
    }

    public int readBonusNumber() {
        System.out.println(BONUS_NUMBER_MESSAGE);
        String input = Console.readLine();
        return parseBonusNumber(input);
    }

    private int parsePurchaseAmount(String input) {
        int amount = parseInteger(input);
        validateDivisibleByThousand(amount);
        return amount;
    }

    private List<Integer> parseWinningNumbers(String input) {
        String[] tokens = input.split(DELIMITER);
        validateWinningNumbersCount(tokens);
        return Arrays.stream(tokens)
                .map(String::trim)
                .map(this::parseInteger)
                .collect(Collectors.toList());
    }

    private int parseBonusNumber(String input) {
        return parseInteger(input);
    }

    private int parseInteger(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_NOT_A_NUMBER);
        }
    }

    private void validateDivisibleByThousand(int amount) {
        if (amount % 1000 != 0) {
            throw new IllegalArgumentException(ERROR_NOT_DIVISIBLE_BY_THOUSAND);
        }
    }

    private void validateWinningNumbersCount(String[] tokens) {
        if (tokens.length != 6) {
            throw new IllegalArgumentException(ERROR_INVALID_FORMAT);
        }
    }
}
