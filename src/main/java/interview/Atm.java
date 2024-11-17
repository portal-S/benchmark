package interview;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Банкомат.
 * Инициализируется набором купюр и умеет выдавать купюры для заданной суммы, либо отвечать отказом.
 * При выдаче купюры списываются с баланса банкомата.
 * Допустимые номиналы: 50₽, 100₽, 500₽, 1000₽, 5000₽.
 *
 * работает только с рублями
 *
 *
 */
public class Atm {
    //denom -> amount
    private final Sdk sdk;
    private final Map<Denomination, Integer> money;

    public Atm(Sdk sdk) {
        this.sdk = sdk;
        this.money = initMoneyInformation();
    }

    private Map<Denomination, Integer> initMoneyInformation() {
        Map<Denomination, Integer> banknotesInfo = new HashMap<>();
        for (var denomination : Denomination.values()) {
            var denomAmount = sdk.countBanknotes(denomination.numRepresentation);
            banknotesInfo.put(denomination, denomAmount);
        }
        return banknotesInfo;
    }

    public void getMoney(int sum) {
        //валидация входного числа (> 0)
        //хватает ли денег на счету
        //есть ли соответствующие купюры
        if (sum <= 0) {
            throw new RuntimeException("Requested sum must be > 0");
        }
        int curBalance = 0;

        for (var entry : money.entrySet()) {
            int slotSum = entry.getKey().numRepresentation * entry.getValue();
            curBalance += slotSum;
        }

        if (curBalance < sum) {
            throw new RuntimeException("There is not enough money in the Atm");
        }


        //TODO: revert
        var sortedDenominations = money.keySet().stream()
            .sorted(Comparator.comparing(Denomination::getNumRepresentation).reversed()).toList();

        Map<Denomination, Integer> collectedMoney = new HashMap<>();
        int leftSum = sum;

        for (int i = sortedDenominations.size() - 1; i >= 0; i--) {
            var denomination = sortedDenominations.get(i);

            if (denomination.numRepresentation > leftSum) {
                continue;
            }

            int banknoteAmount = leftSum / denomination.numRepresentation;
            banknoteAmount = Math.min(banknoteAmount, money.get(denomination));

            collectedMoney.put(denomination, banknoteAmount);

            leftSum = leftSum - (banknoteAmount * denomination.numRepresentation);

            if (leftSum == 0) break;
        }

        if (leftSum > 0) {
            throw new RuntimeException("There are not enough banknotes");
        }

        for (var collectedEntry : collectedMoney.entrySet()) {
            money.compute(collectedEntry.getKey(), (key, value) -> value - collectedEntry.getValue());
        }

        collectedMoney.forEach(
            (banknote, amount) -> sdk.moveBanknoteToDispenser(banknote.numRepresentation, amount)
        );
        sdk.openDispenser();
    }

}
