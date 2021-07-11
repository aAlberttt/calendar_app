package com.example.assignapp2019s1;

/**
 * A class that consists of several static methods.
 * Used for retrieving activity type and corresponding color on different levels.
 *
 * @author Zhongrui Chen
 * @since 17 May 2019
 */

import java.util.ArrayList;

public class ActivityDict {
    private static ArrayList<String> activityTypes = new ArrayList<>();
    private static ArrayList<ArrayList<Integer>> activityColorList = new ArrayList<>();

    public static int size() {
        return activityTypes.size();
    }

    public static void addActivity(String activityType, ArrayList<Integer> colors) {
        activityTypes.add(activityType);
        activityColorList.add(colors);
    }

    public static int getActivityColorByIndexAndLevel(int index, int level) {
        return activityColorList.get(index).get(level);
    }
}