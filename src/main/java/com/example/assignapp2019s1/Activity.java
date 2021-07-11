package com.example.assignapp2019s1;

/**
 * Daily Activity
 *
 * @author Zhongrui Chen
 * @since 11 May 2019
 */

public class Activity {
    private int index; // refers to the activity type
    private int level; // color intensity
    private int color;

    public Activity(int index) {
        this.index = index;
        this.level = 0;
        this.color = ActivityDict.getActivityColorByIndexAndLevel(index, level);
    }

    public Activity(int index, int level) {
        this.index = index;
        this.level = level;
        this.color = ActivityDict.getActivityColorByIndexAndLevel(index, level);
    }

    public int getColor() {
        return color;
    }

    int getIndex() {
        return index;
    }

    int getLevel() {
        return level;
    }

    void levelUp() {
        level++;
        color = ActivityDict.getActivityColorByIndexAndLevel(index, level);
    }

    void levelDown() {
        level--;
        color = ActivityDict.getActivityColorByIndexAndLevel(index, level);
    }
}
