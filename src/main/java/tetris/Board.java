/*
 * Copyright (c) 2008, Esko Luontola. All Rights Reserved.
 *
 * This program and its source code may be freely used for personal
 * non-commercial educational purposes. Usage as course material
 * is not allowed without prior permission.
 */

package tetris;

import java.util.Arrays;

/**
 * @author Esko Luontola
 * @since 6.6.2008
 */
public class Board implements Grid {

    private MovablePiece falling;
    private char[][] blocks;

    public Board(int rows, int columns) {
        blocks = new char[rows][columns];
        for (char[] tmp : blocks) {
            Arrays.fill(tmp, EMPTY);
        }
    }

    public void tick() {
        MovablePiece test = falling.moveDown();
        if (conflictsWithBoard(test)) {
            stopFalling();
        } else {
            falling = test;
        }
    }

    private boolean conflictsWithBoard(MovablePiece p) {
        return p.outsideBoard(this) || hitsStationaryBlock(p);
    }

    private boolean hitsStationaryBlock(MovablePiece p) {
        return blocks[p.relRow()][p.relCol()] != EMPTY;
    }


    public void drop(RotatableGrid piece) {
        if (hasFalling()) {
            throw new IllegalStateException("Another piece may not be dropped when one is already falling");
        }
        falling = new MovablePiece(piece).moveTo(0, columns() / 2 - piece.columns() / 2);
    }

    public boolean hasFalling() {
        return falling != null;
    }

    private void stopFalling() {
        assert hasFalling();
        copyToBoard(falling);
        falling = null;
    }


    private void copyToBoard(MovablePiece p) {
        for (int row = 0; row < rows(); row++) {
            for (int col = 0; col < columns(); col++) {
                if (p.isAt(row, col) && p.cellAt(row, col) != EMPTY) {
                    blocks[row][col] = p.cellAt(row, col);
                }
            }
        }
    }

    public int rows() {
        return blocks.length;
    }

    public int columns() {
        return blocks[0].length;
    }

    public char cellAt(int row, int col) {
        if (falling != null && falling.isAt(row, col)) {
            return falling.cellAt(row, col);
        } else {
            return blocks[row][col];
        }
    }

    public String toString() {
        return new GridAsciiView(this).toString();
    }
}
