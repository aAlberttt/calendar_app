package com.example.assignapp2019s1;

/**
 * @author Zhongrui Chen
 * @since 11 May 2019
 */

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Calendar calendar;
    private ArrayList<ArrayList<Grid>> gridMatrix;
    private int fromActivity = -1;

    // members for tracking and controlling the dynamic status
    private boolean statusAreaUp = false;

    private Grid currentSelectedGrid;
    private Button currentSelectedStatusBar;
    private int currentSelectedActivityIndex = -1;

    // getters

    public Grid getGridByLoc(int i, int j) {
        return gridMatrix.get(i).get(j);
    }

    public Grid getCurrentSelectedGrid() {
        return currentSelectedGrid;
    }

    public Button getCurrentSelectedStatusBar() {
        return currentSelectedStatusBar;
    }

    public int getCurrentSelectedActivityIndex() {
        return currentSelectedActivityIndex;
    }

    public boolean getStatusAreaUp() {
        return statusAreaUp;
    }

    // final members
    final int MAX_LEVEL = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set the bottom nav bar
        BottomNavigationView navigation = findViewById(R.id.bottom_nav);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;

                switch (item.getItemId()) {
                    case R.id.navigation_calendar:
                        break;
                    case R.id.navigation_report:
                        intent = new Intent(MainActivity.this, ReportActivity.class);
                        break;
                    case R.id.navigation_help:
                        intent = new Intent(MainActivity.this, HelpActivity.class);
                        break;
                }

                if (intent != null) {
                    putActivityStatusIntoIntent(intent);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }

                return true;
            }
        });

        navigation.setSelectedItemId(R.id.navigation_calendar);

        // initial the main page using the current time
        int year = 2019;
        int month = 5;
        calendar = new Calendar(year, month);
        gridMatrix = calendar.getGridMatrix();

        // set the month view
        TextView monthTextView = findViewById(R.id.month);
        String str = Months.byNum(month) + " " + year;
        monthTextView.setText(str);

        // create grids view
        int count = calendar.getDaysOfMonth();

        for (int i = 0; i < 5; ++i) {
            ArrayList<Grid> gridRow = gridMatrix.get(i);
            for (int j = 0; j < gridRow.size(); ++j, --count) {
                int id = getResources().getIdentifier("button" + i + j, "id", this.getPackageName());
                Button button = findViewById(id);

                if (count <= 0) { // if the grid count reaches the days of the month
                    // set all following grids to be invisible
                    button.setVisibility(View.INVISIBLE);
                    continue;
                }

                EmptyGrid grid = (EmptyGrid) gridRow.get(j);
                if (button != null) {
                    button.setOnClickListener(new Button.OnClickListener() {
                        public void onClick(View view) {
                            updateSelectedGrid(getGridByButton((Button) view));
                        }
                    });
                }

                grid.setButton(button);
            }
        }

        // set built-in activities
        setBuiltInActivities();

        // set listeners of bar-buttons in the status area
        int activityCount = ActivityDict.size();

        for (int i = 0; i < activityCount; ++i) {
            int id = getResources().getIdentifier("color_bar_" + i, "id", this.getPackageName());
            final int activityIndex = i;
            Button bar = findViewById(id);

            if (bar != null) {
                bar.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View view) {
                        initSelectedActivity();
                        currentSelectedStatusBar = (Button) view;

                        // set borders
                        currentSelectedStatusBar.setBackground(getBorderedDrawable(currentSelectedStatusBar.getBackground(), false));
                        currentSelectedActivityIndex = activityIndex;
                    }
                });
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        // get the intent extras
        Intent fromIntent = getIntent();
        if (fromIntent != null) {
            repaintGridMatrixByIntent(fromIntent);
            fromActivity = fromIntent.getIntExtra("from", -1);
        }
    }

    @Override
    public void onBackPressed() {
        if (fromActivity == -1) {
            super.onBackPressed();
        } else {
            Intent intent = new Intent(MainActivity.this, (fromActivity == 1 ? ReportActivity.class : HelpActivity.class));
            putActivityStatusIntoIntent(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }

    private void repaintGridMatrixByIntent(Intent intent) {
        ArrayList<Integer> ac1 = intent.getIntegerArrayListExtra("ac1");
        ArrayList<Integer> ac2 = intent.getIntegerArrayListExtra("ac2");
        ArrayList<Integer> ac3 = intent.getIntegerArrayListExtra("ac3");
        ArrayList<Integer> ac4 = intent.getIntegerArrayListExtra("ac4");

        if (ac1 == null) {
            return;
        }

        int count = calendar.getDaysOfMonth();

        for (int i = 0; i < gridMatrix.size(); ++i) {
            ArrayList<Grid> gridRow = gridMatrix.get(i);
            for (int j = 0; j < gridRow.size(); ++j, --count) {
                Grid grid = gridRow.get(j);

                int id = getResources().getIdentifier("button" + i + j, "id", this.getPackageName());
                Button button = findViewById(id);

                if (count <= 0) { // if the grid count reaches the days of the month
                    // set all following grids to be invisible

                    continue;
                }

                if (button != null) {
                    button.setOnClickListener(new Button.OnClickListener() {
                        public void onClick(View view) {
                            updateSelectedGrid(getGridByButton((Button) view));
                        }
                    });
                }

                ArrayList<Activity> activities = new ArrayList<>();
                int ac1Level = ac1.get(i * 7 + j);
                if (ac1Level != 0) {
                    activities.add(new Activity(0, ac1Level));
                }
                int ac2Level = ac2.get(i * 7 + j);
                if (ac2Level != 0) {
                    activities.add(new Activity(1, ac2Level));
                }
                int ac3Level = ac3.get(i * 7 + j);
                if (ac3Level != 0) {
                    activities.add(new Activity(2, ac3Level));
                }
                int ac4Level = ac4.get(i * 7 + j);
                if (ac4Level != 0) {
                    activities.add(new Activity(3, ac4Level));
                }

                grid.setButton(button);

                if (activities.size() > 0) {
                    grid = new NonEmptyGrid(grid, activities);
                }

                paintGridByActivities(grid, activities);
            }
        }
    }

    private void setBuiltInActivities() {
        ArrayList<Integer> workoutActivityColors = new ArrayList<>();
        ArrayList<Integer> readingActivityColors = new ArrayList<>();
        ArrayList<Integer> holder1ActivityColors = new ArrayList<>();
        ArrayList<Integer> holder2ActivityColors = new ArrayList<>();

        workoutActivityColors.add(getColor(R.color.basic_color));
        workoutActivityColors.add(getColor(R.color.workout_level_1));
        workoutActivityColors.add(getColor(R.color.workout_level_2));
        workoutActivityColors.add(getColor(R.color.workout_level_3));
        workoutActivityColors.add(getColor(R.color.workout_level_4));

        readingActivityColors.add(getColor(R.color.basic_color));
        readingActivityColors.add(getColor(R.color.reading_level_1));
        readingActivityColors.add(getColor(R.color.reading_level_2));
        readingActivityColors.add(getColor(R.color.reading_level_3));
        readingActivityColors.add(getColor(R.color.reading_level_4));

        holder1ActivityColors.add(getColor(R.color.basic_color));
        holder1ActivityColors.add(getColor(R.color.holder_1_level_1));
        holder1ActivityColors.add(getColor(R.color.holder_1_level_2));
        holder1ActivityColors.add(getColor(R.color.holder_1_level_3));
        holder1ActivityColors.add(getColor(R.color.holder_1_level_4));

        holder2ActivityColors.add(getColor(R.color.basic_color));
        holder2ActivityColors.add(getColor(R.color.holder_2_level_1));
        holder2ActivityColors.add(getColor(R.color.holder_2_level_2));
        holder2ActivityColors.add(getColor(R.color.holder_2_level_3));
        holder2ActivityColors.add(getColor(R.color.holder_2_level_4));

        ActivityDict.addActivity(getString(R.string.workout_activity), workoutActivityColors);
        ActivityDict.addActivity(getString(R.string.reading_activity), readingActivityColors);
        ActivityDict.addActivity(getString(R.string.coding_activity), holder1ActivityColors);
        ActivityDict.addActivity(getString(R.string.social_activity), holder2ActivityColors);
    }

    private void putActivityStatusIntoIntent(Intent intent) {
        ArrayList<Integer> ac1 = new ArrayList<>();
        ArrayList<Integer> ac2 = new ArrayList<>();
        ArrayList<Integer> ac3 = new ArrayList<>();
        ArrayList<Integer> ac4 = new ArrayList<>();

        for (ArrayList<Grid> gridRow : gridMatrix) {
            for (Grid grid : gridRow) {
                if (grid.getColorCount() == 0) {
                    ac1.add(0);
                    ac2.add(0);
                    ac3.add(0);
                    ac4.add(0);
                } else {
                    ac1.add(((NonEmptyGrid) grid).getActivityLevelByIndex(0));
                    ac2.add(((NonEmptyGrid) grid).getActivityLevelByIndex(1));
                    ac3.add(((NonEmptyGrid) grid).getActivityLevelByIndex(2));
                    ac4.add(((NonEmptyGrid) grid).getActivityLevelByIndex(3));
                }
            }
        }

        intent.putIntegerArrayListExtra("ac1", ac1);
        intent.putIntegerArrayListExtra("ac2", ac2);
        intent.putIntegerArrayListExtra("ac3", ac3);
        intent.putIntegerArrayListExtra("ac4", ac4);
        intent.putExtra("from", 0);
    }

    // get other resources by buttons
    private String convertButtonIdToDate(Button button) {
        String id = getResources().getResourceName(button.getId());
        int i = Integer.parseInt(id.charAt(id.length() - 2) + "");
        int j = Integer.parseInt(id.charAt(id.length() - 1) + "");
        return (i * 7 + j + 1) + ", " + Months.byNum(calendar.getMonth());
    }

    public Grid getGridByButton(Button button) {
        String id = getResources().getResourceName(button.getId());
        int i = Integer.parseInt(id.charAt(id.length() - 2) + "");
        int j = Integer.parseInt(id.charAt(id.length() - 1) + "");
        return gridMatrix.get(i).get(j);
    }

    private void updateGridInMatrix(Button button, Grid newGrid) {
        String id = getResources().getResourceName(button.getId());
        int i_loc = Integer.parseInt(id.charAt(id.length() - 2) + "");
        int j_loc = Integer.parseInt(id.charAt(id.length() - 1) + "");
        ArrayList<Grid> gridRow = gridMatrix.get(i_loc);
        gridRow.set(j_loc, newGrid);
    }


    // Drawable Processing Methods

    private Drawable getDrawableCopy(Drawable drawable) {
        return drawable.getConstantState().newDrawable().mutate();
    }

    private GradientDrawable getColoredDrawable(Drawable drawable, int color) {
        GradientDrawable res = (GradientDrawable) getDrawableCopy(drawable);
        res.setColor(color);
        return res;
    }

    private GradientDrawable getBorderedDrawable(Drawable drawable, boolean fromAbove) {
        GradientDrawable res = (GradientDrawable) getDrawableCopy(drawable);
        int color = getColor(fromAbove ? R.color.action_area_background : R.color.calendar_area_background);
        res.setStroke(5, color);
        return res;
    }

    private GradientDrawable getUnborderedDrawable(Drawable drawable) {
        GradientDrawable res = (GradientDrawable) getDrawableCopy(drawable);
        res.setStroke(0, 0);
        return res;
    }

    // handling with layer-list

    private LayerDrawable getColoredDrawableForLayerList(Drawable drawable, ArrayList<Integer> colors) {
        LayerDrawable res = (LayerDrawable) getDrawableCopy(drawable);
        int colorCount = colors.size();
        int size = res.getNumberOfLayers();
        for (int i = 0; i < colorCount; ++i) {
            GradientDrawable tempDrawable = (GradientDrawable) res.getDrawable(i);
            res.setDrawable(i, getColoredDrawable(tempDrawable, colors.get(i)));
        }
        return res;

    }

    private LayerDrawable getBorderedDrawableForLayerList(Drawable drawable, boolean fromAbove) {
        LayerDrawable res = (LayerDrawable) getDrawableCopy(drawable);
        int size = res.getNumberOfLayers();
        GradientDrawable borderControlItem = (GradientDrawable) res.getDrawable(size - 1);
        res.setDrawable(size - 1, getBorderedDrawable(borderControlItem, fromAbove));

        return res;
    }

    private LayerDrawable getUnborderedDrawableForLayerList(Drawable drawable) {
        LayerDrawable res = (LayerDrawable) getDrawableCopy(drawable);
        int size = res.getNumberOfLayers();
        GradientDrawable borderControlItem = (GradientDrawable) res.getDrawable(size - 1);
        res.setDrawable(size - 1, getUnborderedDrawable(borderControlItem));

        return res;
    }

    private Drawable getDrawableBySize(int size) {
        int drawableId;
        if (size == 2) {
            drawableId = R.drawable.two_colors_grid;
        } else if (size == 3) {
            drawableId = R.drawable.three_colors_grid;
        } else {
            drawableId = R.drawable.four_color_grid;
        }
        return getDrawable(drawableId);
    }

    private void paintGridByActivities(Grid grid, ArrayList<Activity> activities) {
        int size = activities.size();
        Button button = grid.getButton();
        if (size > 1) {
            ArrayList<Integer> colors = new ArrayList<>();
            for (Activity activity : activities) {
                colors.add(activity.getColor());
            }
            button.setBackground(getColoredDrawableForLayerList(getDrawableBySize(size), colors));
            updateGridInMatrix(button, grid);
        } else if (size == 1) {
            Activity activity = activities.get(0);
            int color = activity.getColor();
            button.setBackground(getColoredDrawable(getDrawable(R.drawable.empty_grid), color));
            updateGridInMatrix(button, grid);
        } // else size == 0, return directly
    }

    private void paintCurrentSelectedGridByActivities(ArrayList<Activity> activities) {
        int size = activities.size();
        Button button = currentSelectedGrid.getButton();
        if (size > 1) {
            ArrayList<Integer> colors = new ArrayList<>();
            for (Activity activity : activities) {
                colors.add(activity.getColor());
            }
            button.setBackground(getBorderedDrawableForLayerList(getColoredDrawableForLayerList(getDrawableBySize(size), colors), true));
            updateGridInMatrix(button, currentSelectedGrid);
        } else if (size == 1) {
            Activity activity = activities.get(0);
            int color = activity.getColor();
            button.setBackground(getBorderedDrawable(getColoredDrawable(getDrawable(R.drawable.empty_grid), color), true));
            updateGridInMatrix(button, currentSelectedGrid);
        } // else size == 0, return directly
    }

    // button on-click listeners (helper) functions

    private void updateSelectedGrid(Grid grid) {
        initSelectedGrid();
        initSelectedActivity();

        currentSelectedGrid = grid;
        repaintColorBars();

        // update the date
        TextView dateTextView = findViewById(R.id.date);
        dateTextView.setText(convertButtonIdToDate(currentSelectedGrid.getButton()));

        // add the borders
        Button button = currentSelectedGrid.getButton();
        int colorCount = grid.getColorCount();
        if (colorCount <= 1) { // empty
            button.setBackground(getBorderedDrawable(button.getBackground(), true));
        } else {
            button.setBackground(getBorderedDrawableForLayerList(button.getBackground(), true));
        }

        if (!statusAreaUp) {
            slideUp(findViewById(R.id.action_area));
            statusAreaUp = true;
        }
    }

    // the on-click listener of the add button
    public void activityLevelUp(View view) {
        // no activity is being selected
        if (currentSelectedActivityIndex < 0) {
            return;
        }

        int colorCount = currentSelectedGrid.getColorCount();

        if (colorCount == 0) { // if the grid is empty, we are going to add the first color into it
            currentSelectedGrid = new NonEmptyGrid((EmptyGrid) currentSelectedGrid, new Activity(currentSelectedActivityIndex));
            ((NonEmptyGrid) currentSelectedGrid).activityLevelUp(currentSelectedActivityIndex);
            Button button = currentSelectedGrid.getButton();
            int color = ActivityDict.getActivityColorByIndexAndLevel(currentSelectedActivityIndex, 1);
            // change the corresponding background of the status bar
            currentSelectedStatusBar.setBackground(getColoredDrawable(currentSelectedStatusBar.getBackground(), color));
            button.setBackground(getColoredDrawable(button.getBackground(), color));
            // update grid in matrix by current selected grid
            updateGridInMatrix(button, currentSelectedGrid);
        } else { // update the activity level
            // find the existed activities
            ArrayList<Integer> existedActivityIndex = ((NonEmptyGrid) currentSelectedGrid).getActivityIndices();


            if (!existedActivityIndex.contains(currentSelectedActivityIndex)) { // add a new color
                ((NonEmptyGrid) currentSelectedGrid).addActivity(new Activity(currentSelectedActivityIndex));
                ((NonEmptyGrid) currentSelectedGrid).activityLevelUp(currentSelectedActivityIndex);
                paintCurrentSelectedGridByActivities(((NonEmptyGrid) currentSelectedGrid).getActivities());
            } else { // just level up an existed color
                int level = ((NonEmptyGrid) currentSelectedGrid).getActivityLevelByIndex(currentSelectedActivityIndex);
                if (level == MAX_LEVEL) { // the level can't increase anymore
                    return;
                }
                ((NonEmptyGrid) currentSelectedGrid).activityLevelUp(currentSelectedActivityIndex);
                paintCurrentSelectedGridByActivities(((NonEmptyGrid) currentSelectedGrid).getActivities());
            }
            repaintColorBars();
        }
    }

    // the on-click listener of the remove button
    public void activityLevelDown(View view) {
        if (currentSelectedActivityIndex < 0) {
            return;
        }

        if (currentSelectedGrid.getColorCount() == 0) { // if the grid is empty, the level can't decrease
            return;
        } else { // update the activity level
            int level = ((NonEmptyGrid) currentSelectedGrid).getActivityLevelByIndex(currentSelectedActivityIndex);
            if (level == 0) {
                return;
            } else if (level == 1) { // if level is becoming 0 from 1,

                ((NonEmptyGrid) currentSelectedGrid).removeActivity(currentSelectedActivityIndex);

                if (currentSelectedGrid.getColorCount() == 0) { // remove all colors
                    clearGrid(view);
                    return;
                }
            }
            // doesn't remove a color, just level down
            ((NonEmptyGrid) currentSelectedGrid).activityLevelDown(currentSelectedActivityIndex);
            paintCurrentSelectedGridByActivities(((NonEmptyGrid) currentSelectedGrid).getActivities());
            repaintColorBars();
        }
    }

    // the on-click listener of the clear button
    public void clearGrid(View view) {
        if (currentSelectedGrid == null) {
            return;
        }
        Button button = currentSelectedGrid.getButton();
        currentSelectedGrid = new EmptyGrid();
        button.setBackground(getBorderedDrawable(getDrawable(R.drawable.empty_grid), true));

        repaintColorBars();
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                updateSelectedGrid(getGridByButton((Button) view));
            }
        });
        currentSelectedGrid.setButton(button);
        updateGridInMatrix(button, currentSelectedGrid);
    }

    public void clickActionArea(View view) {
        initSelectedActivity();
    }

    public void clickBlankSpace(View view) {
        if (statusAreaUp) {
            slideDown(findViewById(R.id.action_area));
            statusAreaUp = false;
        }
    }


    // track-variables initialization

    private void initSelectedGrid() {
        if (currentSelectedGrid == null) {
            return;
        }

        int colorCount = currentSelectedGrid.getColorCount();
        Button button = currentSelectedGrid.getButton();
        // remove borders and set it null
        if (colorCount <= 1) {
            button.setBackground(getUnborderedDrawable(button.getBackground()));
        } else {
            button.setBackground(getUnborderedDrawableForLayerList(button.getBackground()));
        }
        currentSelectedGrid = null;
    }

    private void initSelectedActivity() {
        if (currentSelectedStatusBar == null) {
            return;
        }

        // remove borders of the bar
        currentSelectedStatusBar.setBackground(getUnborderedDrawable(currentSelectedStatusBar.getBackground()));
        currentSelectedStatusBar = null;
        currentSelectedActivityIndex = -1;
    }

    // animations

    private void repaintColorBars() {
        if (currentSelectedGrid == null) {
            return;
        }

        for (int i = 0; i < ActivityDict.size(); ++i) {
            int id = getResources().getIdentifier("color_bar_" + i, "id", this.getPackageName());
            Button bar = findViewById(id);
            if (bar == null) {
                continue;
            }

            int level = currentSelectedGrid.getColorCount() == 0 ? 0 : ((NonEmptyGrid) currentSelectedGrid).getActivityLevelByIndex(i);
            bar.setBackground(getColoredDrawable(bar.getBackground(), ActivityDict.getActivityColorByIndexAndLevel(i, level)));
        }
    }

    private void slideUp(View view) {
        view.setVisibility(View.VISIBLE);


        TranslateAnimation animate = new TranslateAnimation(0, 0, view.getHeight(), 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    private void slideDown(View view) {
        initSelectedGrid();
        initSelectedActivity();

        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // enumerate class

    private enum Months {
        JAN(1), FEB(2), MAR(3), APR(4), MAY(5), JUN(6), JUL(7), AUG(8), SEP(9), OCT(10), NOV(11), DEC(12);

        int monthNumber;

        Months(int monthNumber) {
            this.monthNumber = monthNumber;
        }

        public static Months byNum(int monthNumber) {
            Months res = null;
            for (Months m : Months.values()) {
                if (m.monthNumber == monthNumber) {
                    res = m;
                    break;
                }
            }
            return res;
        }
    }
}