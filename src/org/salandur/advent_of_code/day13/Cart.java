package org.salandur.advent_of_code.day13;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Arrays;
import java.util.List;

public class Cart implements Comparable<Cart> {
    static final char LEFT = '<';
    static final char RIGHT = '>';
    static final char UP = '^';
    static final char DOWN = 'v';
    static final char STRAIGHT = '|';

    static final List<Character> CART_DIRECTIONS = Arrays.asList(LEFT, RIGHT, UP, DOWN);

    private char direction;
    private int x;
    private int y;
    // order is LEFT, STRAIGHT, RIGHT
    private char nextTurn = LEFT;
    private boolean isCrashed = false;

    public Cart(char direction, int x, int y) {
        this.direction = direction;
        this.x = x;
        this.y = y;
    }


    public boolean isOn(int x, int y) {
        return this.x == x && this.y == y;
    }

    void move() {
        if (direction == LEFT)
            x--;
        else if (direction == RIGHT)
            x++;
        else if (direction == UP)
            y--;
        else if (direction == DOWN)
            y++;
    }

    public boolean collidesWith(Cart other) {
        return this.x == other.x && this.y == other.y;
    }

    public boolean isNotCrashed() {
         return !isCrashed;
    }

    public void setIsCrashed() {
        this.isCrashed = true;
    }

    char getDirection() {
        return direction;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("direction", direction)
                .append("x", x)
                .append("y", y)
                .toString();
    }

    public void turnIfNeeded(Character trackSection) {
        if (trackSection == '/') {
            turn(UP, DOWN, RIGHT, LEFT);
        } else if (trackSection == '\\') {
            turn(DOWN, UP, LEFT, RIGHT);
        } else if (trackSection == '+') {
            if (nextTurn == LEFT) {
                turn(UP, DOWN, LEFT, RIGHT);
                nextTurn = STRAIGHT;
            } else if (nextTurn == STRAIGHT) {
                nextTurn = RIGHT;
            } else if (nextTurn == RIGHT) {
                turn(DOWN, UP, RIGHT, LEFT);
                nextTurn = LEFT;
            }
        }
    }

    private void turn(char fromRight, char fromLeft, char fromUp, char fromDown) {
        if (direction == UP)
            direction = fromUp;
        else if (direction == DOWN)
            direction = fromDown;
        else if (direction == RIGHT)
            direction = fromRight;
        else // direction == LEFT
            direction = fromLeft;
    }


    @Override
    public int compareTo(Cart o) {
        return new CompareToBuilder().append(x, o.x).append(y, o.y).toComparison();
    }
}
