package com.company;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.logging.SimpleFormatter;

public class Main {
    public static final int N = 25;
//    private static int maxQueue = 0;
//    private static int loopCtr;

    public static void main(String[] args) {
        Date startDate = new Date();

        Board.setSizeLimit(N);

        int[] initalBoard = new int[1];
        initalBoard[0] = 1;
        int[] initialUsageMap = new int[0];

        Board board = new Board(initalBoard, initialUsageMap);
        PriorityQueue<Board> queue = new PriorityQueue<Board>(1, new BoardComparator());
        queue.add(board);

        int[] max = new int[N];

        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        while (null != (board = queue.poll())) {
//            loopCtr++;
            if (board.getScore() > max[board.getSize() - 1]) {
                Date date = new Date();
                System.out.println(simpleFormat.format(date) + " (" + (date.getTime() - startDate.getTime()) + ") " +  " New max[" + board.getSize() + "]: " + Arrays.toString(board.getRegions()) + " = " + board.getScore() + " QS: " + queue.size());
                max[board.getSize() - 1] = board.getScore();
            }
//            System.out.println("Processing " + Arrays.toString(board.getRegions()) + " = " + board.getScore() + " from queue");
            if (board.shouldExtend(max[board.getSize() - 1])) {
                Board[] boards = board.extend();
                for (int i = 0; i < boards.length; i++) {
//                    System.out.println("Adding " + Arrays.toString(boards[i].getRegions()) + " = " + board.getScore() + " to queue");
                    queue.add(boards[i]);
                }
            }
//            if (queue.size() > maxQueue) {
//                maxQueue = queue.size();
//                System.out.println("MaxQueueSize: " + maxQueue);
//            }
//            System.out.println("QueueSize: " + queue.size());
//            if (loopCtr % 100000 == 0) {
//                System.out.println("Progress: " + Arrays.toString(board.getRegions()) + " QS: " + queue.size());
//            }
        }
//        System.out.println("MaxQueueSize: " + maxQueue);
        Date date = new Date();
        System.out.println(simpleFormat.format(date) + " (" + (date.getTime() - startDate.getTime()) + ")" + " End");
    }
}
