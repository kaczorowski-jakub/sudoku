package com.os.sudoku;

import com.os.sudoku.model.Grid;

public class Main {

    public static void main(String[] args) {
        Grid g = new Grid(9);
        g.setValue(6,4,7);
        g.printSquare(6);
    }

}
