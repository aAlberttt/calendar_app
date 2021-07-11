package com.example.assignapp2019s1;

/**
 * @author Zhongrui Chen
 * @since 11 May 2019
 */
import android.widget.Button;

public class EmptyGrid extends Grid {
    private Button button;
    private int colorCount = 0;

    public EmptyGrid() {}

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

    @Override
    public void changeColorCount(int offset) {
        colorCount += offset;
    }
}
