package com.company;

import java.util.Arrays;

/**
 * Created by andero on 24.05.2014.
 */
public class Board {
    private static int sizeLimit;
    private int size;
    private int score;
    private int heuristicLimit;
    private int[] regions;
    private int[] usageMap;

    public Board(int[] regionMap, int[] usageMap) {
        regions = regionMap.clone();
        size = regionMap.length;
        this.usageMap = new int[regions[size - 1] * 3 + 2];
        System.arraycopy(usageMap, 0, this.usageMap, 0, usageMap.length);
    }

    public static void setSizeLimit(int sizeLimit) {
        Board.sizeLimit = sizeLimit;
    }

    public int getScore()
    {
        if (0 == score) {
            score = calculateScore3();
        }
        return score;
    }

    private int calculateScore3() {
//        System.out.println("--------------");
//        System.out.println(Arrays.toString(regions));
//        System.out.println(Arrays.toString(usageMap));
        int lastItem = regions[regions.length - 1];
//        System.out.println(lastItem);
        usageMap[lastItem] = 1;
        usageMap[lastItem * 2] = 2;
        usageMap[lastItem * 3] = 3;
        for (int ctr = 0; ctr < lastItem * 2 + 1; ctr++) {
            if (usageMap[ctr] == 0 || usageMap[ctr] > 2) {
                continue;
            }
            if (usageMap[ctr + lastItem] == 0 || usageMap[ctr + lastItem] > usageMap[ctr] + 1) {
                usageMap[ctr + lastItem] = usageMap[ctr] + 1;
            }
        }
//        System.out.println(Arrays.toString(usageMap));

        int max = 0;
        for (int ctr = 1; ctr < usageMap.length; ctr++) {
            if (usageMap[ctr] == 0) {
                max = ctr;
                break;
            }
        }
//        System.out.println(size + "->" + max);
        return max;
    }


    private int calculateScore2() {
        // Kuna me teame N-1 data mappi, siis uue mapi tegemiseks tuleks n+1 puhul ainult viimane lõik üle käia???
        int limit = regions[size - 1] * 3 + 1;
        int max = 0;

        int[] previousResults = new int[limit + 1];
        for (int j = 0; j < size; j++) {
            previousResults[regions[j]] = 1;
        }
        for (int target = 1; target <= limit; target++) {
            boolean matchFound = false;
            for (int fieldCtr = 0; fieldCtr < size; fieldCtr++) {
                int toFind = target - regions[fieldCtr];
                if (toFind == 0) {
                    matchFound = true;
                    break;
                }
                if (toFind < 0) {
                    break;
                }
                int dartCount = previousResults[toFind];
                if (dartCount > 0 && dartCount < 3) {
                    matchFound = true;
                    if (previousResults[target] == 0) {
                        previousResults[target] = dartCount + 1;
                    } else if (previousResults[target] > dartCount + 1) {
                        previousResults[target] = dartCount + 1;
                    }
                }
            }

            if (matchFound) {
                if (max < target) {
                    max = target;
                }
            } else {
                break;
            }
        }
        return max + 1;
    }


    private int calculateScore() {
        int[] valueMap = new int[getHeuristicLimit() + 1]; // to compensate 0 pos
        int max = 0;

        // write initial values and multiples
        for (int i = 0; i < size; i++) {
            if (regions[i] < 0) {
                return 0;
            }
            valueMap[regions[i]] = 1;
            valueMap[regions[i] * 2] = 2;
            valueMap[regions[i] * 3] = 3;
        }
        boolean changeMade;
        do {
            changeMade = false;
            for (int i = 1; i < valueMap.length; i++) {
                if (valueMap[i] < 3 && valueMap[i] > 0) {
                    for (int j = 0; j < size; j++) {
                        if (i + regions[j] < valueMap.length) {
                            if (valueMap[i + regions[j]] == 0) {
                                valueMap[i + regions[j]] = valueMap[i] + 1;
                                changeMade = true;
                            } else if (valueMap[i + regions[j]] > valueMap[i] + 1) {
                                valueMap[i + regions[j]] = valueMap[i] + 1;
                                changeMade = true;
                            }
                        }
                    }
                }
            }
        } while (changeMade);

        for (int i = 1; i < valueMap.length; i++) {
            if (valueMap[i] != 0) {
                max = i;
            } else {
                break;
            }
        }
        return max + 1;
    }

    public boolean shouldExtend(int max) {
        if (getMax() < max) {
//            System.out.println("Limiting: " + Arrays.toString(regions));
            return false;
        }
        // todo add additional conditions here?
        if (size < sizeLimit) {
            return true;
        }
        return false;
    }

    private long getMax() {
        long max = regions[regions.length - 1];
        for (int i = 0; i < (sizeLimit - size + 1); i++) {
            max = max * 3 + 1;
        }
        return max;
    }

    public Board[] extend() {
        int min = regions[regions.length - 1] + 1;
        int max = getScore();
        Board[] boards = new Board[max - min + 1];
        int[] newBoardRegions = new int[regions.length + 1];
        System.arraycopy(regions, 0, newBoardRegions, 0, regions.length);
        for (int i = min; i < max + 1; i++) {
            newBoardRegions[regions.length] = i;
            boards[i - min] = new Board(newBoardRegions, usageMap);
        }
        return boards;
    }

    public int getHeuristicLimit() {
        if (0 == heuristicLimit) {
            heuristicLimit = calculateHeuristicLimit();
        }
        return heuristicLimit;
    }

    private int calculateHeuristicLimit() {
        return 3 * findMaxRegion() + 1;
    }

    private int findMaxRegion() {
        int max = 0;
        for (int i = 0; i < size; i++) {
            if (max < regions[i]) {
                max = regions[i];
            }
        }
        return max;
    }

    public int[] getRegions() {
        return regions;
    }

    public int getSize() {
        return size;
    }
}