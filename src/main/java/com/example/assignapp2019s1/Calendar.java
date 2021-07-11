package com.example.assignapp2019s1;

/**
 * @author Zhongrui Chen
 * @since 11 May 2019
 */

import java.util.ArrayList;

public class Calendar {
    private int year;
    private int month;
    private int daysOfMonth;

    private ArrayList<ArrayList<Grid>> gridMatrix = new ArrayList<>();

    public Calendar(int year, int month) {
        this.year = year;
        this.month = month;
        boolean isLeapYear = (year % 4 == 0 && year % 100 != 0 || (year % 400 == 0));

        daysOfMonth = calculateDaysOfMonth(month, isLeapYear);

        for (int i = 0; i < 5; ++i) {
            ArrayList<Grid> gridRow = new ArrayList<>();
            for (int j = 0; j < 7; ++j) {
                gridRow.add(new EmptyGrid());
            }
            gridMatrix.add(gridRow);
        }
    }

    public int getDaysOfMonth() {
        return daysOfMonth;
    }

    public int getMonth() {
        return month;
    }

    public ArrayList<ArrayList<Grid>> getGridMatrix() {
        return gridMatrix;
    }

    private int calculateDaysOfMonth(int month, boolean isLeapYear) {
        if (month == 2) {
            return isLeapYear ? 29 : 28;
        } else {
            if (month <= 7) {
                return (month & 1) == 0 ? 30 : 31;
            } else {
                return (month & 1) == 0 ? 31 : 30;
            }
        }
    }
}
