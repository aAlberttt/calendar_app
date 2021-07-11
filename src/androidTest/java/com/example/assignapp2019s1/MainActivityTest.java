package com.example.assignapp2019s1;

import android.view.View;
import android.widget.Button;

import androidx.test.runner.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Haoyang Chen
 * @since 23 May 2019
 */


@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    // rule used to ensure that the activity is launched
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private MainActivity mainActivity;


    // before testing, we get the main activity first
    @Before
    public void setUp() {
        mainActivity = mainActivityActivityTestRule.getActivity();
    }

    // test whether the main activity launches correctly
    // we test whether the calendar area is correctly launched
    // if it is, then the main activity is correctly launched
    @Test
    public void testLaunch() {
        View view = mainActivity.findViewById(R.id.calendar_area);
        assertNotNull(view);
    }

    @Test
    public void testInitialState() {
        Grid grid = mainActivity.getCurrentSelectedGrid();
        assertNull(grid);

        Button bar = mainActivity.getCurrentSelectedStatusBar();
        assertNull(bar);

        assertEquals(-1, mainActivity.getCurrentSelectedActivityIndex());

        assertFalse(mainActivity.getStatusAreaUp());
    }

    @Test
    public void testButtonGridBinding() {
        Button button1 = mainActivity.findViewById(R.id.button03);
        Grid grid1 = mainActivity.getGridByLoc(0, 3);
        assertEquals(button1, grid1.getButton());

        Button button2 = mainActivity.findViewById(R.id.button22);
        Grid grid2 = mainActivity.getGridByLoc(2, 2);
        assertEquals(button2, grid2.getButton());

        Grid grid3 = mainActivity.getGridByLoc(4, 6);
        assertNull(grid3.getButton());
    }

    @Test
    public void testGridButtonOnClick() {
        onView(withId(R.id.button24)).perform(click());
        assertEquals(mainActivity.getGridByLoc(2, 4), mainActivity.getCurrentSelectedGrid());
        assertTrue(mainActivity.getStatusAreaUp());

        // TODO
        onView(withId(R.id.container)).perform(click());
        assertNull(mainActivity.getCurrentSelectedGrid());
        assertFalse(mainActivity.getStatusAreaUp());
    }

    @Test
    public void testStatusBarOnClick() {
        onView(withId(R.id.button24)).perform(click());
        onView(withId(R.id.color_bar_1)).perform(click());
        assertEquals(mainActivity.findViewById(R.id.color_bar_1), mainActivity.getCurrentSelectedStatusBar());
        assertEquals(1, mainActivity.getCurrentSelectedActivityIndex());
    }

    @Test
    public void testAddButtonOnClick() {
        onView(withId(R.id.button15)).perform(click());
        onView(withId(R.id.color_bar_1)).perform(click());
        onView(withId(R.id.add_button)).perform(click());
        assertEquals(1, mainActivity.getGridByLoc(1, 5).getColorCount());
        assertEquals(1, ((NonEmptyGrid) mainActivity.getGridByLoc(1, 5)).getActivityLevelByIndex(1));
    }

    @Test
    public void testRemoveButtonOnClick() {
        onView(withId(R.id.button33)).perform(click());
        onView(withId(R.id.color_bar_2)).perform(click());
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.remove_button)).perform(click());
        assertEquals(1, ((NonEmptyGrid) mainActivity.getGridByLoc(3, 3)).getActivityLevelByIndex(2));
        onView(withId(R.id.remove_button)).perform(click());
        assertEquals(0, mainActivity.getGridByLoc(3, 3).getColorCount());
    }

    @Test
    public void testClearButtonOnClick() {
        onView(withId(R.id.button40)).perform(click());
        onView(withId(R.id.color_bar_3)).perform(click());
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.color_bar_2)).perform(click());
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.clear_button)).perform(click());
        assertEquals(EmptyGrid.class, mainActivity.getGridByLoc(4, 0).getClass());
        assertEquals(0, mainActivity.getGridByLoc(4, 0).getColorCount());
    }

    @After
    public void tearDown() {
        mainActivity = null;
    }
}