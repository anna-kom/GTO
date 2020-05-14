package com.russiantest.gto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ChangeQuestionActivity extends AppCompatActivity {

    private static final String TEST_SHARED_PREFS = "changeTestSharedPrefs";
    private static final String NUMBER_OF_QUESTIONS = "numberOfQuestions";
    private static final String TEST_CHANGED = "testChanged";
    private static SharedPreferences sharedPreferences;
    private static int currentNumber;
    private static int quantity;
    private static boolean questionChanged;
    private static Button button;

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            questionChanged = true; makeButtonOrange();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        sharedPreferences = getSharedPreferences(TEST_SHARED_PREFS, MODE_PRIVATE);
        int type = intent.getIntExtra("type", 0);
        currentNumber = intent.getIntExtra("number", 1);
        quantity = sharedPreferences.getInt(NUMBER_OF_QUESTIONS, 1);
        questionChanged = false;
        String additionalTextKey = currentNumber + "additionalText";
        String answerKey = currentNumber + "answer";
        String pointsKey = currentNumber + "points";
        String option1Key = currentNumber + "option1";
        String option2Key = currentNumber + "option2";
        String option3Key = currentNumber + "option3";
        String option4Key = currentNumber + "option4";
        String textKey = currentNumber + "text";
        TextView number, questionQuantity;
        if (type == 0) {
            setContentView(R.layout.view_question1);
            number = findViewById(R.id.view_question1_number);
            number.setText("" + currentNumber);
            questionQuantity = findViewById(R.id.view_question1_quantity);
            questionQuantity.setText("/" + quantity);
            button = findViewById(R.id.view_question1_save_button);
            EditText answerEditText = findViewById(R.id.view_question1_correct_answer);
            EditText pointsEditText = findViewById(R.id.view_question1_points);
            EditText option1EditText = findViewById(R.id.view_question1_option1);
            EditText option2EditText = findViewById(R.id.view_question1_option2);
            EditText option3EditText = findViewById(R.id.view_question1_option3);
            EditText option4EditText = findViewById(R.id.view_question1_option4);
            EditText textEditText = findViewById(R.id.view_question1_text);
            answerEditText.setText(sharedPreferences.getString(answerKey, ""));
            pointsEditText.setText("" + sharedPreferences.getInt(pointsKey, 0));
            option1EditText.setText(sharedPreferences.getString(option1Key, ""));
            option2EditText.setText(sharedPreferences.getString(option2Key, ""));
            option3EditText.setText(sharedPreferences.getString(option3Key, ""));
            option4EditText.setText(sharedPreferences.getString(option4Key, ""));
            textEditText.setText(sharedPreferences.getString(textKey, ""));
            answerEditText.addTextChangedListener(textWatcher);
            pointsEditText.addTextChangedListener(textWatcher);
            option1EditText.addTextChangedListener(textWatcher);
            option2EditText.addTextChangedListener(textWatcher);
            option3EditText.addTextChangedListener(textWatcher);
            option4EditText.addTextChangedListener(textWatcher);
            textEditText.addTextChangedListener(textWatcher);
        }
        else if (type == 1) {
            setContentView(R.layout.view_question2);
            number = findViewById(R.id.view_question2_number);
            number.setText("" + currentNumber);
            questionQuantity = findViewById(R.id.view_question2_quantity);
            questionQuantity.setText("/" + quantity);
            button = findViewById(R.id.view_question2_save_button);
            EditText additionalTextEditText = findViewById(R.id.view_question2_sentence);
            additionalTextEditText.setText(sharedPreferences.getString(additionalTextKey, ""));
            EditText answerEditText = findViewById(R.id.view_question2_correct_answer);
            answerEditText.setText(sharedPreferences.getString(answerKey, ""));
            EditText pointsEditText = findViewById(R.id.view_question2_points);
            pointsEditText.setText("" + sharedPreferences.getInt(pointsKey, 0));
            EditText option1EditText = findViewById(R.id.view_question2_option1);
            option1EditText.setText(sharedPreferences.getString(option1Key, ""));
            EditText option2EditText = findViewById(R.id.view_question2_option2);
            option2EditText.setText(sharedPreferences.getString(option2Key, ""));
            EditText option3EditText = findViewById(R.id.view_question2_option3);
            option3EditText.setText(sharedPreferences.getString(option3Key, ""));
            EditText option4EditText = findViewById(R.id.view_question2_option4);
            option4EditText.setText(sharedPreferences.getString(option4Key, ""));
            EditText textEditText = findViewById(R.id.view_question2_text);
            textEditText.setText(sharedPreferences.getString(textKey, ""));
            additionalTextEditText.addTextChangedListener(textWatcher);
            answerEditText.addTextChangedListener(textWatcher);
            pointsEditText.addTextChangedListener(textWatcher);
            option1EditText.addTextChangedListener(textWatcher);
            option2EditText.addTextChangedListener(textWatcher);
            option3EditText.addTextChangedListener(textWatcher);
            option4EditText.addTextChangedListener(textWatcher);
            textEditText.addTextChangedListener(textWatcher);
        }
        else {
            setContentView(R.layout.view_question3);
            number = findViewById(R.id.view_question3_number);
            number.setText("" + currentNumber);
            questionQuantity = findViewById(R.id.view_question3_quantity);
            questionQuantity.setText("/" + quantity);
            button = findViewById(R.id.view_question3_save_button);
            EditText additionalTextEditText = findViewById(R.id.view_question3_sentence);
            additionalTextEditText.setText(sharedPreferences.getString(additionalTextKey, ""));
            EditText answerEditText = findViewById(R.id.view_question3_correct_answer);
            answerEditText.setText(sharedPreferences.getString(answerKey, ""));
            EditText pointsEditText = findViewById(R.id.view_question3_points);
            pointsEditText.setText("" + sharedPreferences.getInt(pointsKey, 0));
            EditText textEditText = findViewById(R.id.view_question3_text);
            textEditText.setText(sharedPreferences.getString(textKey, ""));
            additionalTextEditText.addTextChangedListener(textWatcher);
            answerEditText.addTextChangedListener(textWatcher);
            pointsEditText.addTextChangedListener(textWatcher);
            textEditText.addTextChangedListener(textWatcher);
        }
    }

    public void changeQuestion1(View view)
    {
        if (questionChanged) {
            EditText pointsEditText = findViewById(R.id.view_question1_points);
            String points = pointsEditText.getText().toString();
            if (!points.isEmpty()) {
                EditText textEditText = findViewById(R.id.view_question1_text);
                String text = textEditText.getText().toString();
                if (!text.isEmpty()) {
                    EditText o1EditText = findViewById(R.id.view_question1_option1);
                    EditText o2EditText = findViewById(R.id.view_question1_option2);
                    EditText o3EditText = findViewById(R.id.view_question1_option3);
                    EditText o4EditText = findViewById(R.id.view_question1_option4);
                    String o1 = o1EditText.getText().toString();
                    String o2 = o2EditText.getText().toString();
                    String o3 = o3EditText.getText().toString();
                    String o4 = o4EditText.getText().toString();
                    if (!o1.isEmpty() && !o2.isEmpty() && !o3.isEmpty() && !o4.isEmpty()) {
                        EditText answerEditText = findViewById(R.id.view_question1_correct_answer);
                        String answer = answerEditText.getText().toString();
                        if (!answer.isEmpty()) {
                            if (isCorrectAnswer(answer)) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                String additionalTextKey = currentNumber + "additionalText";
                                editor.putString(additionalTextKey, "");
                                String answerKey = currentNumber + "answer";
                                editor.putString(answerKey, answer);
                                String pointsKey = currentNumber + "points";
                                editor.putInt(pointsKey, Integer.parseInt(points));
                                String option1Key = currentNumber + "option1";
                                editor.putString(option1Key, o1);
                                String option2Key = currentNumber + "option2";
                                editor.putString(option2Key, o2);
                                String option3Key = currentNumber + "option3";
                                editor.putString(option3Key, o3);
                                String option4Key = currentNumber + "option4";
                                editor.putString(option4Key, o4);
                                String textKey = currentNumber + "text";
                                editor.putString(textKey, text);
                                editor.putBoolean(TEST_CHANGED, true);
                                editor.apply();
                                Toast.makeText(this, "Вопрос изменен!", Toast.LENGTH_SHORT).show();
                                Intent changeTestIntent = new Intent(this, ChangeTestActivity.class);
                                startActivity(changeTestIntent);
                            } else
                                Toast.makeText(this, "Ответ должен быть цифрой от 1 до 4", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(this, "Введите ответ", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "Введите все варианты ответа", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, "Введите текст вопроса", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Введите баллы", Toast.LENGTH_SHORT).show();
        }
    }

    public void changeQuestion2(View view)
    {
        if (questionChanged) {
            EditText pointsEditText = findViewById(R.id.view_question2_points);
            String points = pointsEditText.getText().toString();
            if (!points.isEmpty()) {
                EditText textEditText = findViewById(R.id.view_question2_text);
                String text = textEditText.getText().toString();
                if (!text.isEmpty()) {
                    EditText additionalTextEditText = findViewById(R.id.view_question2_sentence);
                    String additionalText = additionalTextEditText.getText().toString();
                    if (!additionalText.isEmpty()) {
                        EditText o1EditText = findViewById(R.id.view_question2_option1);
                        EditText o2EditText = findViewById(R.id.view_question2_option2);
                        EditText o3EditText = findViewById(R.id.view_question2_option3);
                        EditText o4EditText = findViewById(R.id.view_question2_option4);
                        String o1 = o1EditText.getText().toString();
                        String o2 = o2EditText.getText().toString();
                        String o3 = o3EditText.getText().toString();
                        String o4 = o4EditText.getText().toString();
                        if (!o1.isEmpty() && !o2.isEmpty() && !o3.isEmpty() && !o4.isEmpty()) {
                            EditText answerEditText = findViewById(R.id.view_question2_correct_answer);
                            String answer = answerEditText.getText().toString();
                            if (!answer.isEmpty()) {
                                if (isCorrectAnswer(answer)) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    String additionalTextKey = currentNumber + "additionalText";
                                    editor.putString(additionalTextKey, additionalText);
                                    String answerKey = currentNumber + "answer";
                                    editor.putString(answerKey, answer);
                                    String pointsKey = currentNumber + "points";
                                    editor.putInt(pointsKey, Integer.parseInt(points));
                                    String option1Key = currentNumber + "option1";
                                    editor.putString(option1Key, o1);
                                    String option2Key = currentNumber + "option2";
                                    editor.putString(option2Key, o2);
                                    String option3Key = currentNumber + "option3";
                                    editor.putString(option3Key, o3);
                                    String option4Key = currentNumber + "option4";
                                    editor.putString(option4Key, o4);
                                    String textKey = currentNumber + "text";
                                    editor.putString(textKey, text);
                                    editor.putBoolean(TEST_CHANGED, true);
                                    editor.apply();
                                    Toast.makeText(this, "Вопрос изменен!", Toast.LENGTH_SHORT).show();
                                    Intent changeTestIntent = new Intent(this, ChangeTestActivity.class);
                                    startActivity(changeTestIntent);
                                } else
                                    Toast.makeText(this, "Ответ должен быть цифрой от 1 до 4", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(this, "Введите ответ", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(this, "Введите все варианты ответа", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "Введите предложение для вопроса", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, "Введите текст вопроса", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Введите баллы", Toast.LENGTH_SHORT).show();
        }
    }

    public void changeQuestion3(View view)
    {
        if (questionChanged) {
            EditText pointsEditText = findViewById(R.id.view_question3_points);
            String points = pointsEditText.getText().toString();
            if (!points.isEmpty()) {
                EditText textEditText = findViewById(R.id.view_question3_text);
                String text = textEditText.getText().toString();
                if (!text.isEmpty()) {
                    EditText additionalTextEditText = findViewById(R.id.view_question3_sentence);
                    String additionalText = additionalTextEditText.getText().toString();
                    if (!additionalText.isEmpty()) {
                        EditText answerEditText = findViewById(R.id.view_question3_correct_answer);
                        String answer = answerEditText.getText().toString();
                        if (!answer.isEmpty()) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            String additionalTextKey = currentNumber + "additionalText";
                            editor.putString(additionalTextKey, additionalText);
                            String answerKey = currentNumber + "answer";
                            editor.putString(answerKey, answer);
                            String pointsKey = currentNumber + "points";
                            editor.putInt(pointsKey, Integer.parseInt(points));
                            String textKey = currentNumber + "text";
                            editor.putString(textKey, text);
                            editor.putBoolean(TEST_CHANGED, true);
                            editor.apply();
                            Toast.makeText(this, "Вопрос изменен!", Toast.LENGTH_SHORT).show();
                            Intent changeTestIntent = new Intent(this, ChangeTestActivity.class);
                            startActivity(changeTestIntent);
                        } else
                            Toast.makeText(this, "Введите ответ", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "Введите предложение для вопроса", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, "Введите текст вопроса", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Введите баллы", Toast.LENGTH_SHORT).show();
        }
    }

    public void makeButtonOrange()
    {
        button.setBackgroundResource(R.drawable.orange_solid_rounded_button);
        button.setTextColor(Color.parseColor("#FFFFFF"));
    }

    @Override
    public void onBackPressed() {
        if (questionChanged)
        {
            final Context currentContext = this;
            new AlertDialog.Builder(this)
                    .setMessage("Вы уверены, что хотите выйти, не сохранив изменения?")
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent changeTestIntent = new Intent(currentContext, ChangeTestActivity.class);
                            startActivity(changeTestIntent);
                        }
                    })
                    .setNegativeButton("Нет", null)
                    .show();
        }
        else
        {
            Intent changeTestIntent = new Intent(this, ChangeTestActivity.class);
            startActivity(changeTestIntent);
        }
    }

    private boolean isCorrectAnswer(String answer) {
        String answerRegex = "\\b[1-4]\\b";
        Pattern pat = Pattern.compile(answerRegex);
        if (answer == null)
            return false;
        return pat.matcher(answer).matches();
    }
}
