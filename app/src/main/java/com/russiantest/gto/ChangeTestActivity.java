package com.russiantest.gto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChangeTestActivity extends AppCompatActivity {

    private static String testName;
    private static final String TEST_SHARED_PREFS = "changeTestSharedPrefs";
    private static final String NUMBER_OF_QUESTIONS = "numberOfQuestions";
    private static final String TEST_NAME = "testName";
    private static final String INITIAL_TEST_NAME = "initialTestName";
    private static SharedPreferences sharedPreferences;
    private static int numberOfQuestions;

    private static ArrayList<Question> questions;
    private static DatabaseReference databaseReference;
    private static final String TEST_CHANGED = "testChanged";
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addition_question);
        button = findViewById(R.id.save_button);
        button.setText("Сохранить изменения");
        button.setOnClickListener(this::changeTest);
        makeButtonWhite();
        LinearLayout plusLayout = findViewById(R.id.addition_plus_layout);
        plusLayout.setVisibility(View.GONE);

        if (!isInternetAvailable())
            Toast.makeText(this, "Проверьте свое подключение к интернету", Toast.LENGTH_SHORT).show();

        sharedPreferences = getSharedPreferences(TEST_SHARED_PREFS, MODE_PRIVATE);
        boolean newTest = getIntent().getBooleanExtra("newTest", false);
        if (newTest)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            String initialTestName = getIntent().getStringExtra(TEST_NAME);
            editor = sharedPreferences.edit();
            editor.putString(INITIAL_TEST_NAME, initialTestName);
            editor.putString(TEST_NAME, initialTestName);
            editor.apply();
            assert initialTestName != null;
            questions = new ArrayList<>();
            databaseReference = FirebaseDatabase.getInstance().getReference().child(initialTestName);
            numberOfQuestions = 0;
            loadTest();
        }
        else
            loadScreen();
    }

    public void loadScreen() {
        EditText testNameEditText = findViewById(R.id.addition_test_name);
        testName = sharedPreferences.getString(TEST_NAME, "");
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
                editor.putBoolean(TEST_CHANGED, true);
                editor.apply();
                makeButtonOrange();
            }
        });

        numberOfQuestions = sharedPreferences.getInt(NUMBER_OF_QUESTIONS, 0);
        LinearLayout linearLayout = findViewById(R.id.addition_linear_layout);
        for (int i = 1; i <= numberOfQuestions; i++)
        {
            View question = getLayoutInflater().inflate(R.layout.addition_question_number, null);
            question.setOnClickListener(this::showQuestionToChange);
            TextView text = question.findViewById(R.id.addition_question_text);
            String qText = "" + i + ". " + sharedPreferences.getString("" + i + "text", "");
            text.setText(qText);
            linearLayout.addView(question);
        }

        boolean changed = sharedPreferences.getBoolean(TEST_CHANGED, false);
        if (changed)
            makeButtonOrange();
    }

    public void loadTest()
    {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Question question = snapshot.getValue(Question.class);
                    questions.add(question);
                    numberOfQuestions++;
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(NUMBER_OF_QUESTIONS, numberOfQuestions);
                for (int i = 1; i <= numberOfQuestions; i++)
                {
                    Question question = questions.get(i - 1);
                    String additionalTextKey = i + "additionalText";
                    String answerKey = i + "answer";
                    String pointsKey = i + "points";
                    String option1Key = i + "option1";
                    String option2Key = i + "option2";
                    String option3Key = i + "option3";
                    String option4Key = i + "option4";
                    String textKey = i + "text";
                    String typeKey = i + "type";
                    if (question.getType() == QuestionType.Standard)
                    {
                        editor.putString(additionalTextKey, "");
                        editor.putString(answerKey, question.getAnswer());
                        editor.putInt(pointsKey, question.getPoints());
                        editor.putString(option1Key, question.getOptions().get(0));
                        editor.putString(option2Key, question.getOptions().get(1));
                        editor.putString(option3Key, question.getOptions().get(2));
                        editor.putString(option4Key, question.getOptions().get(2));
                        editor.putString(textKey, question.getText());
                        editor.putInt(typeKey, 0);
                        editor.apply();
                    }
                    else if (question.getType() == QuestionType.Commas) {
                        editor.putString(additionalTextKey, question.getAdditionalText());
                        editor.putString(answerKey, question.getAnswer());
                        editor.putInt(pointsKey, question.getPoints());
                        editor.putString(option1Key, question.getOptions().get(0));
                        editor.putString(option2Key, question.getOptions().get(1));
                        editor.putString(option3Key, question.getOptions().get(2));
                        editor.putString(option4Key, question.getOptions().get(2));
                        editor.putString(textKey, question.getText());
                        editor.putInt(typeKey, 1);
                        editor.apply();
                    }
                    else if (question.getType() == QuestionType.Input) {
                        editor.putString(additionalTextKey, question.getAdditionalText());
                        editor.putString(answerKey, question.getAnswer());
                        editor.putInt(pointsKey, question.getPoints());
                        editor.putString(option1Key, "");
                        editor.putString(option2Key, "");
                        editor.putString(option3Key, "");
                        editor.putString(option4Key, "");
                        editor.putString(textKey, question.getText());
                        editor.putInt(typeKey, 2);
                        editor.apply();
                    }
                }
                loadScreen();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void changeTest(View view)
    {
        boolean changed = sharedPreferences.getBoolean(TEST_CHANGED, false);
        if (changed) {
            if (testName.isEmpty())
                Toast.makeText(this, "Введите название теста", Toast.LENGTH_SHORT).show();
            else if (!isInternetAvailable())
                Toast.makeText(this, "Проверьте свое подключение к интернету", Toast.LENGTH_SHORT).show();
            else {
                Context currentContext = this;
                new AlertDialog.Builder(this)
                        .setMessage("Вы уверены, что хотите сохранить изменения?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String initialTestName = sharedPreferences.getString(INITIAL_TEST_NAME, "");
                                FirebaseDatabase.getInstance().getReference().child(initialTestName).removeValue();
                                databaseReference = FirebaseDatabase.getInstance().getReference().child(testName);
                                for (int i = 0; i < numberOfQuestions; i++) {
                                    Question question = new Question();
                                    int number = i + 1;
                                    String additionalTextKey = number + "additionalText";
                                    String answerKey = number + "answer";
                                    String pointsKey = number + "points";
                                    String option1Key = number + "option1";
                                    String option2Key = number + "option2";
                                    String option3Key = number + "option3";
                                    String option4Key = number + "option4";
                                    String textKey = number + "text";
                                    String typeKey = number + "type";
                                    QuestionType type;
                                    switch (sharedPreferences.getInt(typeKey, 0)) {
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
                                    if (type == QuestionType.Standard || type == QuestionType.Commas) {
                                        List<String> options = Arrays.asList(sharedPreferences.getString(option1Key, ""),
                                                sharedPreferences.getString(option2Key, ""),
                                                sharedPreferences.getString(option3Key, ""),
                                                sharedPreferences.getString(option4Key, ""));
                                        question.setOptions(options);
                                    } else
                                        question.setOptions(null);
                                    question.setText(sharedPreferences.getString(textKey, ""));
                                    question.setType(type);
                                    databaseReference.child("" + number).setValue(question);

                                }
                                Toast.makeText(currentContext, "Тест изменен!", Toast.LENGTH_SHORT).show();
                                Intent adminIntent = new Intent(currentContext, ViewTestsActivity.class);
                                startActivity(adminIntent);
                            }
                        })
                        .setNegativeButton("Нет", null)
                        .show();
            }
        }
    }

    public void showQuestionToChange(View view)
    {
        TextView text = view.findViewById(R.id.addition_question_text);
        int number = Integer.parseInt(text.getText().toString().split("[.]")[0]);
        String typeKey = number + "type";
        Intent showIntent = new Intent(this, ChangeQuestionActivity.class);
        showIntent.putExtra("type", sharedPreferences.getInt(typeKey, 0));
        showIntent.putExtra("number", number);
        startActivity(showIntent);
    }

    @Override
    public void onBackPressed() {
        final Context currentContext = this;
        boolean changed = sharedPreferences.getBoolean(TEST_CHANGED, false);
        if (changed)
            new AlertDialog.Builder(this)
                    .setMessage("Вы уверены, что хотите выйти, не сохранив изменения?")
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent adminIntent = new Intent(currentContext, ViewTestsActivity.class);
                            startActivity(adminIntent);
                        }
                    })
                    .setNegativeButton("Нет", null)
                    .show();
        else
        {
            Intent adminIntent = new Intent(currentContext, ViewTestsActivity.class);
            startActivity(adminIntent);
        }
    }

    public void makeButtonOrange()
    {
        button.setBackgroundResource(R.drawable.orange_solid_rounded_button);
        button.setTextColor(Color.parseColor("#FFFFFF"));
    }

    public void makeButtonWhite()
    {
        button.setBackgroundResource(R.drawable.orange_rounded_button);
        button.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
