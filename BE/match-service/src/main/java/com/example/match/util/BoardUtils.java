package com.example.match.util;

public class BoardUtils {
    private static final String[][] INITIAL_BOARD = {
            {"r", "h", "e", "a", "k", "a", "e", "h", "r"},
            {"", "", "", "", "", "", "", "", ""},
            {"", "c", "", "", "", "", "", "c", ""},
            {"p", "", "p", "", "p", "", "p", "", "p"},
            {"", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"P", "", "P", "", "P", "", "P", "", "P"},
            {"", "C", "", "", "", "", "", "C", ""},
            {"", "", "", "", "", "", "", "", ""},
            {"R", "H", "E", "A", "K", "A", "E", "H", "R"}
    };

    public static String[][] getInitialBoardState() {
        // Return initial board state
        return INITIAL_BOARD;
    }
}
