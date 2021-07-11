package com.example.assignapp2019s1;

/**
 * @author Zhongrui Chen
 * @since 17 May 2019
 */

import android.widget.Button;
import java.util.ArrayList;

public class NonEmptyGrid extends Grid {
    private transient Button button;
    private int colorCount;
    private ArrayList<Activity> activities = new ArrayList<>();

    public NonEmptyGrid(EmptyGrid grid, Activity activity) {
        colorCount++;
        button = grid.getButton();
        activities.add(activity);
    }

    public NonEmptyGrid(Grid grid, ArrayList<Activity> activities) {
        colorCount = activities.size();
        button = grid.getButton();
        this.activities = activities;
    }

    @Override
    public void changeColorCount(int offset) {
        colorCount += offset;
    }

    public void activityLevelUp(int activityIndex) {
        for (int i = 0; i < activities.size(); ++i) {
            Activity activity = activities.get(i);
            if (activity.getIndex() == activityIndex) {
                activity.levelUp();
                break;
            }
        }
    }

    public void activityLevelDown(int activityIndex) {
        for (int i = 0; i < activities.size(); ++i) {
            Activity activity = activities.get(i);
            if (activity.getIndex() == activityIndex) {
                activity.levelDown();
                break;
            }
        }
    }

    public int getActivityLevelByIndex(int activityIndex) {
        int res = 0;
        for (int i = 0; i < activities.size(); ++i) {
            Activity activity = activities.get(i);
            if (activity.getIndex() == activityIndex) {
                res = activity.getLevel();
                break;
            }
        }
        return res;
    }

    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
        colorCount++;
    }

    public void removeActivity(int activityIndex) {
        int index = -1;

        for (int i = 0; i < activities.size(); ++i) {
            Activity activity = activities.get(i);
            if (activity.getIndex() == activityIndex) {
                index = i;
                break;
            }
        }

        activities.remove(index);
        colorCount--;
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public ArrayList<Integer> getActivityIndices() {
        ArrayList<Integer> res = new ArrayList<>();
        for (Activity activity : activities) {
            res.add(activity.getIndex());
        }
        return res;
    }

    @Override
    public void setButton(Button button) {
        this.button = button;
    }

    @Override
    public Button getButton() {
        return button;
    }

    @Override
    public int getColorCount() {
        return colorCount;
    }
}
