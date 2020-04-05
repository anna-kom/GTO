package com.russiantest.gto;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BuildingTestActivity extends AppCompatActivity {

    private EditText testName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addition_question);

        testName = findViewById(R.id.addition_test_name);
        String name = getIntent().getStringExtra("name");
        if (!(name == null) && !name.isEmpty())
            testName.setText(name);
    }

    public void startForm(View view) {
        if (testName.getText().toString().isEmpty())
            Toast.makeText(this, "Введите название теста", Toast.LENGTH_SHORT).show();
        else {
            final AlertDialog questionDialog = new AlertDialog.Builder(
                    BuildingTestActivity.this).setView(R.layout.building_dialog).create();
            questionDialog.show();
        }
    }

    public void openQuestionBuilding(View view) {
        Intent questionIntent = new Intent(this, QuestionBuildingActivity.class);
        int type = 0;
        switch (view.getId()) {
            case R.id.question_type1_dialog:
                type = 0;
                break;
            case R.id.question_type2_dialog:
                type = 1;
                break;
            case R.id.question_type3_dialog:
                type = 2;
                break;
        }
        questionIntent.putExtra("type", type);
        questionIntent.putExtra("name", testName.getText().toString());
        startActivity(questionIntent);
    }

    public void saveTest(View view)
    {
        if (testName.getText().toString().isEmpty())
            Toast.makeText(this, "Введите название теста", Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(this, "Тест сохранен!", Toast.LENGTH_SHORT).show();
            Intent adminIntent = new Intent(this, AdminActivity.class);
            startActivity(adminIntent);
        }
    }
}
