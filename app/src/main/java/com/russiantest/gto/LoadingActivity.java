package com.russiantest.gto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LoadingActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;  // сколько отображается экран с логотипом в начале

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);

        // чтобы при загрузке отображался логотип, я потом запускалась LoginActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginIntent = new Intent(LoadingActivity.this, LoginActivity.class);
                LoadingActivity.this.startActivity(loginIntent);
                LoadingActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
