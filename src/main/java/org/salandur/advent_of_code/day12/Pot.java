package org.salandur.advent_of_code.day12;

import java.util.List;
import java.util.Optional;

public class Pot {
    final int index;
    public final boolean hasPlant;
    private Pot left;
    private Pot right;

    private Pot(boolean hasPlant, int index) {
        this.index = index;
        this.hasPlant = hasPlant;

    }

    public Pot(boolean hasPlant, int index, Optional<Pot> previousPot) {
        this(hasPlant, index);

        if (previousPot.isPresent()) {
            this.left = previousPot.get();
            previousPot.get().right = this;
        }
    }

    public Pot nextGeneration(List<Rule> rules, Optional<Pot> lastPot) {
        // System.out.printf("%2d: %s%s\n", index, StringUtils.repeat(" ", index), toPattern());
        Rule rule = rules.stream().filter(r -> r.pattern.equals(toPattern())).findFirst().orElseGet(() -> new Rule("", false));
        return new Pot(rule.willHavePlant, index, lastPot);
    }

    @Override
    public String toString() {
        return hasPlant ? "#" : ".";
    }

    public Pot left() {
        if (left == null) {
            return new Pot(false, index - 1).withRight(this);

        }
        return left;
    }

    public void setLeft(Pot left) {
        this.left = left;
        left.right = this;
    }

    public Pot withRight(Pot right) {
        this.right = right;
        return this;
    }

    public Pot right() {
        if (right == null) {
            return new Pot(false, index + 1).withLeft(this);
        }
        return right;
    }

    public void setRight(Pot right) {
        this.right = right;
        right.left = this;
    }

    public Pot withLeft(Pot left) {
        this.left = left;
        return this;
    }

    public String toPattern() {
        return new StringBuilder().
                append(leftPattern()).
                append(hasPlant ? "#" : ".").
                append(rightPattern()).
                toString();
    }

    private String leftPattern() {
        return left().left().toString() + left().toString();
    }

    private String rightPattern() {
        return right().toString() + right().right().toString();
    }
}
