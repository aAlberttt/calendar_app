package com.example.assignapp2019s1;

/**
 * @author Hao Chen
 * @since 22 May 2019
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {
    ArrayList<Integer> ac1 = new ArrayList<>();
    ArrayList<Integer> ac2 = new ArrayList<>();
    ArrayList<Integer> ac3 = new ArrayList<>();
    ArrayList<Integer> ac4 = new ArrayList<>();
    private int fromActivity = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // get the intent extras
        Intent fromIntent = getIntent();

        ac1 = fromIntent.getIntegerArrayListExtra("ac1");
        ac2 = fromIntent.getIntegerArrayListExtra("ac2");
        ac3 = fromIntent.getIntegerArrayListExtra("ac3");
        ac4 = fromIntent.getIntegerArrayListExtra("ac4");
        fromActivity = fromIntent.getIntExtra("from", -1);

        createReport();

        // set the bottom nav bar
        BottomNavigationView navigation = findViewById(R.id.bottom_nav);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;

                switch (item.getItemId()) {
                    case R.id.navigation_calendar:
                        intent = new Intent(ReportActivity.this, MainActivity.class);
                        break;
                    case R.id.navigation_report:

                        break;
                    case R.id.navigation_help:
                        intent = new Intent(ReportActivity.this, HelpActivity.class);
                        break;
                }

                if (intent != null) {
                    startActivityAsIntent(intent);
                }

                return true;
            }
        });

        navigation.setSelectedItemId(R.id.navigation_report);
    }

    private void startActivityAsIntent(Intent intent) {
        intent.putIntegerArrayListExtra("ac1", ac1);
        intent.putIntegerArrayListExtra("ac2", ac2);
        intent.putIntegerArrayListExtra("ac3", ac3);
        intent.putIntegerArrayListExtra("ac4", ac4);
        intent.putExtra("from", 1);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ReportActivity.this, fromActivity == 0 ? MainActivity.class : HelpActivity.class);
        startActivityAsIntent(intent);
    }

    private void createReport() {
        int markedDays = 0;
        double workoutHours = 0;
        double readingHours = 0;
        double holder1Hours = 0;
        double holder2Hours = 0;

        for (int i = 0; i < ac1.size(); ++i) {
            int h1 = ac1.get(i);
            int h2 = ac2.get(i);
            int h3 = ac3.get(i);
            int h4 = ac4.get(i);

            if (h1 > 0 || h2 > 0 || h3 > 0 || h4 > 0) {
                markedDays++;
            }
            workoutHours += h1 * 0.5;
            readingHours += h2 * 0.5;
            holder1Hours += h3 * 0.5;
            holder2Hours += h4 * 0.5;
        }

        TextView daysTextView = findViewById(R.id.marked_days);
        daysTextView.setText(markedDays + "");

        TextView workoutFig = findViewById(R.id.workout_figure);
        workoutFig.setText(workoutHours + " hours");

        TextView readingFig = findViewById(R.id.reading_figure);
        readingFig.setText(readingHours + " hours");

        TextView holder1Fig = findViewById(R.id.holder_1_figure);
        holder1Fig.setText(holder1Hours + " hours");

        TextView holder2Fig = findViewById(R.id.holder_2_figure);
        holder2Fig.setText(holder2Hours + " hours");
    }
}