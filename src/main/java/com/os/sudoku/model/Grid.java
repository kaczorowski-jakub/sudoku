package com.os.sudoku.model;

import java.util.ArrayList;
import java.util.List;

public class Grid {

    private final List<Bucket> rows = new ArrayList<>();
    private final List<Bucket> cols = new ArrayList<>();
    private final List<Bucket> squares = new ArrayList<>();
    private final Entry[][] entries;
    private final int squareX;
    private final int squareY;

    public Grid(final int size) {
        this.entries = new Entry[size][size];
        this.squareY = (int) Math.sqrt((double) size);
        this.squareX = size / this.squareY;
        fillGrid(size);
        createBuckets();
    }

    private void fillGrid(final int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                entries[i][j] = new Entry(size);
            }
        }
    }

    private void createBuckets() {
        for (int i = 0; i < entries.length; i++) {
            try {
                rows.get(i);
            } catch (IndexOutOfBoundsException e) {
                rows.add(i, new Bucket());
            }
            for (int j = 0; j < entries[i].length; j++) {
                try {
                    cols.get(j);
                } catch (IndexOutOfBoundsException e) {
                    cols.add(j, new Bucket());
                }
                final int squareNumber = (i / squareY * squareY) + (j / squareX);
                try {
                    squares.get(squareNumber);
                } catch (IndexOutOfBoundsException e) {
                    squares.add(squareNumber, new Bucket());
                }
                rows.get(i).addEntry(entries[i][j]);
                cols.get(j).addEntry(entries[i][j]);
                squares.get(squareNumber).addEntry(entries[i][j]);
            }
        }
    }

    public void setValue(final int val, final int x, final int y) {
        entries[x][y].setVal(val);
    }

    private void resetHints() {
        for (int i = 0; i < entries.length; i++) {
            for (int j = 0; j < entries[i].length; j++) {
                entries[i][j].fillNumbers();
            }
        }
    }

    public void computeHints() {
        resetHints();
        eliminateByBucket();
        onlyPossibleChoice();
        rejectMisfits();
    }

    private void eliminateByBucket() {
        rows.forEach(row -> row.eliminateNumbers());
        cols.forEach(col -> col.eliminateNumbers());
        squares.forEach(square -> square.eliminateNumbers());
    }

    private void onlyPossibleChoice() {
        rows.forEach(row -> row.onlyPosibleChoice());
        cols.forEach(col -> col.onlyPosibleChoice());
        squares.forEach(square -> square.onlyPosibleChoice());
    }

    private void rejectMisfits() {
        rows.forEach(row -> row.rejectMisfits());
        cols.forEach(col -> col.rejectMisfits());
        squares.forEach(square -> square.rejectMisfits());
    }

    public void solve(int x, int y) {
        if (x >= entries.length || y >= entries[x].length || isCompleted()) {
            return;
        }

        if (!entries[x][y].isEntered()) {
            for (int possibleNumber : entries[x][y].getPossibleNumbers()) {
                entries[x][y].setValHard(possibleNumber);
                if (validate()) {
                    int tx = x;
                    int ty = y;
                    if (ty < entries[tx].length - 1) {
                        ty++;
                    } else {
                        ty = 0;
                        tx++;
                    }
                    solve(tx, ty);
                    if (isCompleted()) {
                        return;
                    }
                }
                entries[x][y].setValHard(0);
            }
        } else {
            int tx = x;
            int ty = y;
            if (ty < entries[tx].length - 1) {
                ty++;
            } else {
                ty = 0;
                tx++;
            }
            solve(tx, ty);
        }
    }

    public boolean isCompleted() {
        for (Entry[] arr : entries) {
            for (Entry entry : arr) {
                if (!entry.isEntered()) {
                    return false;
                }
            }
        }
        return validate();
    }

    public boolean validate() {
        for (Bucket b : rows) {
            if (b.validate() == false) {
                return false;
            }
        }
        for (Bucket b : cols) {
            if (b.validate() == false) {
                return false;
            }
        }
        for (Bucket b : squares) {
            if (b.validate() == false) {
                return false;
            }
        }
        return true;
    }

    public int getX() {
        return entries.length;
    }

    public int getY() {
        return entries.length > 0 ? entries[0].length : 0;
    }

    public Entry getEntryAt(int x, int y) {
        return entries[x][y];
    }

    public void printRow(int x) {
        rows.get(x).printBucket();
    }

    public void printCol(int x) {
        cols.get(x).printBucket();
    }

    public void printSquare(int x) {
        squares.get(x).printBucket();
    }

    public void printGrid() {
        for (int i = 0; i < entries.length; i++) {
            for (int j = 0; j < entries[i].length; j++) {
                System.out.print(entries[i][j].getVal() + " - ");
            }
            System.out.println("\n");
        }
    }
}
