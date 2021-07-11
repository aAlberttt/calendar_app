package com.example.assignapp2019s1;

/**
 * @author Zhongrui Chen
 * @since 11 May 2019
 */

import android.widget.Button;

public abstract class Grid {
    // a grid is associated with a button
    public abstract void setButton(Button button);

    public abstract Button getButton();

    public abstract int getColorCount();

    public abstract void changeColorCount(int offset);
}
