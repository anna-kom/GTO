package com.russiantest.gto;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        Intent intent = getIntent();
        double result = intent.getDoubleExtra("result", 0);
        TextView resultText = findViewById(R.id.resilts_number);
        @SuppressLint("DefaultLocale") String finalResult = String.format("%.2f", result) + "%";
        resultText.setText(finalResult);
    }

    public void backToMainResults(View view)
    {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}
