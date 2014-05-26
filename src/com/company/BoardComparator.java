package com.company;

import java.util.Comparator;

/**
 * Created by andero on 24.05.2014.
 */
public class BoardComparator implements Comparator<Board> {

    @Override
    public int compare(Board o1, Board  o2) {
        if (o1.getScore() > o2.getScore()) {
            return -1;
        }
        if (o1.getScore() < o2.getScore()) {
            return 1;
        }
        return 0;
    }
}
