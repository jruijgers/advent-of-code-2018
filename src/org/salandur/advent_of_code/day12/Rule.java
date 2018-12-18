package org.salandur.advent_of_code.day12;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rule {
    private static final Pattern RULE = Pattern.compile("([.#]{5}) => ([.#])");

    public static Rule from(String ruleString) {
        Matcher m = RULE.matcher(ruleString);

        if (m.matches()) {
            return new Rule(m.group(1), "#".equals(m.group(2)));
        } else {
            throw new RuntimeException("No rule match for " + ruleString);
        }
    }

    public final String pattern;
    public final boolean willHavePlant;

    public Rule(String pattern, boolean willHavePlant) {
        this.pattern = pattern;
        this.willHavePlant = willHavePlant;
    }

    @Override
    public String toString() {
        return pattern + " => " + (willHavePlant ? "#" : ".");
    }
}
