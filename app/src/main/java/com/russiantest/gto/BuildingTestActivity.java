package com.russiantest.gto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

public class BuildingTestActivity extends AppCompatActivity {

    private String testName;
    private static final String TEST_SHARED_PREFS = "testSharedPrefs";
    private static final String NUMBER_OF_QUESTIONS = "numberOfQuestions";
    private static final String TEST_NAME = "testName";
    private static final String HAS_SHOWN_TOAST_MESSAGE = "shownToast";
    private static int numberOfQuestions;
    private static int currentNumber;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addition_question);

        SharedPreferences sharedPreferences = getSharedPreferences(TEST_SHARED_PREFS, MODE_PRIVATE);
        boolean newTest = getIntent().getBooleanExtra("newTest", false);
        if (newTest)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }

        testName = sharedPreferences.getString(TEST_NAME, "");
        EditText testNameEditText = findViewById(R.id.addition_test_name);
        if (!testName.isEmpty())
            testNameEditText.setText(testName);
        testNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                testName = testNameEditText.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TEST_NAME, testName);
                editor.apply();
            }
        });

        numberOfQuestions = sharedPreferences.getInt(NUMBER_OF_QUESTIONS, 0);
        LinearLayout linearLayout = findViewById(R.id.addition_linear_layout);
        for (int i = 1; i <= numberOfQuestions; i++)
        {
            View question = getLayoutInflater().inflate(R.layout.addition_question_number, null);
            TextView text = question.findViewById(R.id.addition_question_text);
            String qText = "" + i + ". " + sharedPreferences.getString("" + i + "text", "");
            text.setText(qText);
            linearLayout.addView(question);
        }
        boolean shownToast = sharedPreferences.getBoolean(HAS_SHOWN_TOAST_MESSAGE, false);
        if (!shownToast)
        {
            Toast.makeText(this, "Нажмите на кнопку \"+\", чтобы добавить вопрос", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(HAS_SHOWN_TOAST_MESSAGE, true);
            editor.apply();
        }
        currentNumber = numberOfQuestions + 1;
    }

    public void startForm(View view) {
        final AlertDialog questionDialog = new AlertDialog.Builder(
                BuildingTestActivity.this).setView(R.layout.building_dialog).create();
        questionDialog.show();
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
        questionIntent.putExtra("number", currentNumber);
        questionIntent.putExtra("quantity", numberOfQuestions + 1);
        startActivity(questionIntent);
    }

    public void saveTest(View view)
    {
        if (testName.isEmpty())
            Toast.makeText(this, "Введите название теста", Toast.LENGTH_SHORT).show();
        else if (numberOfQuestions == 0)
            Toast.makeText(this, "Добавьте хотя бы один вопрос", Toast.LENGTH_SHORT).show();
        else if (!isInternetAvailable())
            Toast.makeText(this, "Проверьте свое подключение к интернету", Toast.LENGTH_SHORT).show();
        else {
            Context currentContext = this;
            new AlertDialog.Builder(this)
                    .setMessage("Вы уверены, что хотите сохранить тест?")
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sharedPreferences = getSharedPreferences(TEST_SHARED_PREFS, MODE_PRIVATE);
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(testName);
                            for (int i = 0; i < numberOfQuestions; i++)
                            {
                                Question question = new Question();
                                int number = i + 1;
                                String additionalTextKey  = number + "additionalText";
                                String answerKey = number + "answer";
                                String pointsKey = number + "points";
                                String option1Key = number + "option1";
                                String option2Key = number + "option2";
                                String option3Key = number + "option3";
                                String option4Key = number + "option4";
                                String textKey = number + "text";
                                String typeKey = number + "type";
                                QuestionType type;
                                switch (sharedPreferences.getInt(typeKey, 0))
                                {
                                    case 0:
                                        type = QuestionType.Standard;
                                        break;
                                    case 1:
                                        type = QuestionType.Commas;
                                        break;
                                    default:
                                        type = QuestionType.Input;
                                }
                                question.setAdditionalText(sharedPreferences.getString(additionalTextKey, ""));
                                question.setAnswer(sharedPreferences.getString(answerKey, ""));
                                question.setPoints(sharedPreferences.getInt(pointsKey, 0));
                                if (type == QuestionType.Standard || type == QuestionType.Commas)
                                {
                                    List<String> options = Arrays.asList(sharedPreferences.getString(option1Key, ""),
                                            sharedPreferences.getString(option2Key, ""),
                                            sharedPreferences.getString(option3Key, ""),
                                            sharedPreferences.getString(option4Key, ""));
                                    question.setOptions(options);
                                }
                                else
                                    question.setOptions(null);
                                question.setText(sharedPreferences.getString(textKey, ""));
                                question.setType(type);
                                databaseReference.child("" + number).setValue(question);

                            }
                            Toast.makeText(currentContext, "Тест сохранен!", Toast.LENGTH_SHORT).show();
                            Intent adminIntent = new Intent(currentContext, AdminActivity.class);
                            startActivity(adminIntent);
                        }
                    })
                    .setNegativeButton("Нет", null)
                    .show();
        }
    }

    public void showQuestion(View view)
    {
        TextView text = view.findViewById(R.id.addition_question_text);
        int number = Integer.parseInt(text.getText().toString().split("[.]")[0]);
        SharedPreferences sharedPreferences = getSharedPreferences(TEST_SHARED_PREFS, MODE_PRIVATE);
        String typeKey = number + "type";
        Intent showIntent = new Intent(this, QuestionBuildingActivity.class);
        showIntent.putExtra("existingQuestion", true);
        showIntent.putExtra("type", sharedPreferences.getInt(typeKey, 0));
        showIntent.putExtra("number", number);
        showIntent.putExtra("quantity", numberOfQuestions);
        startActivity(showIntent);
    }

    @Override
    public void onBackPressed() {
        final Context currentContext = this;
        if (!testName.isEmpty() || numberOfQuestions > 0)
            new AlertDialog.Builder(this)
                .setMessage("Вы уверены, что хотите выйти, не сохранив тест?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent adminIntent = new Intent(currentContext, AdminActivity.class);
                        startActivity(adminIntent);
                    }
                })
                .setNegativeButton("Нет", null)
                .show();
        else
        {
            Intent adminIntent = new Intent(currentContext, AdminActivity.class);
            startActivity(adminIntent);
        }
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
