package com.os.sudoku.model;

import java.util.Arrays;

public class Entry {

    private final int[] numbers;
    private int val;

    Entry(int size) {
        this.numbers = new int[size];
        fillNumbers();
    }

    void fillNumbers() {
        for (int i = 0; i < this.numbers.length; i++) {
            this.numbers[i] = i + 1;
        }
    }

    private void eraseNumbers() {
        Arrays.fill(this.numbers, 0);
    }

    boolean isEntered() {
        return val > 0;
    }

    public int getVal() {
        return val;
    }

    void setVal(final int i) {
        this.val = i;
        if (i > 0) {
            eraseNumbers();
        } else {
            fillNumbers();
        }
    }

    void setValHard(final int val) {
        this.val = val;
    }

    void eraseNumber(final int number) {
        if (number <= this.numbers.length && number > 0) {
            this.numbers[number - 1] = 0;
        }
    }

    void setNumber(final int number) {
        eraseNumbers();
        numbers[number - 1] = number;
    }

    boolean isNumberAllowed(final int number) {
        return numbers[number-1] != 0;
    }

    public String getNumbers() {

        if (isEntered()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i : numbers) {
            if (i != 0) {
                if (sb.length() == 0) {
                    sb.append(i);
                } else {
                    sb.append(",").append(i);
                }
            }
        }
        return sb.toString();
    }

    public int[] getPossibleNumbers() {
        return Arrays.stream(this.numbers).filter(no -> no != 0).toArray();
    }

    @Override
    public String toString() {
        return val + " (" + getNumbers() + ")";
    }
}
