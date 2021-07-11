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
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HelpActivity extends AppCompatActivity {
    ArrayList<Integer> ac1 = new ArrayList<>();
    ArrayList<Integer> ac2 = new ArrayList<>();
    ArrayList<Integer> ac3 = new ArrayList<>();
    ArrayList<Integer> ac4 = new ArrayList<>();
    private int fromActivity = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // get the intent extras
        Intent fromIntent = getIntent();

        ac1 = fromIntent.getIntegerArrayListExtra("ac1");
        ac2 = fromIntent.getIntegerArrayListExtra("ac2");
        ac3 = fromIntent.getIntegerArrayListExtra("ac3");
        ac4 = fromIntent.getIntegerArrayListExtra("ac4");
        fromActivity = fromIntent.getIntExtra("from", -1);

        // set the bottom nav bar
        BottomNavigationView navigation = findViewById(R.id.bottom_nav);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;

                switch (item.getItemId()) {
                    case R.id.navigation_calendar:
                        intent = new Intent(HelpActivity.this, MainActivity.class);
                        break;
                    case R.id.navigation_report:
                        intent = new Intent(HelpActivity.this, ReportActivity.class);
                        break;
                    case R.id.navigation_help:

                        break;
                }

                if (intent != null) {
                    startActivityAsIntent(intent);
                }

                return true;
            }
        });

        navigation.setSelectedItemId(R.id.navigation_help);
    }

    private void startActivityAsIntent(Intent intent) {
        intent.putIntegerArrayListExtra("ac1", ac1);
        intent.putIntegerArrayListExtra("ac2", ac2);
        intent.putIntegerArrayListExtra("ac3", ac3);
        intent.putIntegerArrayListExtra("ac4", ac4);
        intent.putExtra("from", 2);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HelpActivity.this, fromActivity == 0 ? MainActivity.class : ReportActivity.class);
        startActivityAsIntent(intent);
    }
}