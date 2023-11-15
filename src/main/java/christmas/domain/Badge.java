package christmas.domain;

import java.util.Arrays;

public enum Badge {
    SANTA("산타", 20_000),
    TREE("트리", 10_000),
    STAR("별", 5_000),
    NONE("없음", 0);

    private final String name;
    private final int baseBenefitAmount;

    Badge(String name, int baseBenefitAmount) {
        this.name = name;
        this.baseBenefitAmount = baseBenefitAmount;
    }

    public static Badge findBadge(int baseBenefitAmount) {
        return Arrays.stream(Badge.values())
                .filter(badge -> badge.baseBenefitAmount <= baseBenefitAmount)
                .findFirst()
                .orElse(NONE);
    }

    public String getName() {
        return name;
    }
}
