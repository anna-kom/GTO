package com.russiantest.gto;

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

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class QuestionBuildingActivity extends AppCompatActivity {

    private static final String TEST_SHARED_PREFS = "testSharedPrefs";
    private static final String NUMBER_OF_QUESTIONS = "numberOfQuestions";
    private static int currentNumber;
    private static int quantity;
    private static boolean existingQuestion;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int type = intent.getIntExtra("type", 0);
        currentNumber = intent.getIntExtra("number", 1);
        quantity = intent.getIntExtra("quantity", 1);
        existingQuestion = intent.getBooleanExtra("existingQuestion", false);
        questionChanged = false;
        TextView number, questionQuantity;
        if (type == 0) {
            setContentView(R.layout.form_question_type1);
            number = findViewById(R.id.question_type1_number);
            number.setText("" + currentNumber);
            questionQuantity = findViewById(R.id.question_type1_quantity);
            questionQuantity.setText("/" + quantity);
            button = findViewById(R.id.save_button);
            if (existingQuestion) {
                button.setText("Изменить вопрос");
                makeButtonWhite();
                SharedPreferences sharedPreferences = getSharedPreferences(TEST_SHARED_PREFS, MODE_PRIVATE);
                String answerKey = currentNumber + "answer";
                String pointsKey = currentNumber + "points";
                String option1Key = currentNumber + "option1";
                String option2Key = currentNumber + "option2";
                String option3Key = currentNumber + "option3";
                String option4Key = currentNumber + "option4";
                String textKey = currentNumber + "text";
                EditText answerEditText = findViewById(R.id.question_type1_correct_answer);
                EditText pointsEditText = findViewById(R.id.question_type1_points);
                EditText option1EditText = findViewById(R.id.question_type1_option1);
                EditText option2EditText = findViewById(R.id.question_type1_option2);
                EditText option3EditText = findViewById(R.id.question_type1_option3);
                EditText option4EditText = findViewById(R.id.question_type1_option4);
                EditText textEditText = findViewById(R.id.question_type1_text);
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
        }
        else if (type == 1) {
            setContentView(R.layout.form_question_type2);
            number = findViewById(R.id.question_type2_number);
            number.setText("" + currentNumber);
            questionQuantity = findViewById(R.id.question_type2_quantity);
            questionQuantity.setText("/" + quantity);
            button = findViewById(R.id.save_button);
            if (existingQuestion) {
                button.setText("Изменить вопрос");
                makeButtonWhite();
                SharedPreferences sharedPreferences = getSharedPreferences(TEST_SHARED_PREFS, MODE_PRIVATE);
                String additionalTextKey = currentNumber + "additionalText";
                String answerKey = currentNumber + "answer";
                String pointsKey = currentNumber + "points";
                String option1Key = currentNumber + "option1";
                String option2Key = currentNumber + "option2";
                String option3Key = currentNumber + "option3";
                String option4Key = currentNumber + "option4";
                String textKey = currentNumber + "text";
                EditText additionalTextEditText = findViewById(R.id.question_type2_sentence);
                additionalTextEditText.setText(sharedPreferences.getString(additionalTextKey, ""));
                EditText answerEditText = findViewById(R.id.question_type2_correct_answer);
                answerEditText.setText(sharedPreferences.getString(answerKey, ""));
                EditText pointsEditText = findViewById(R.id.question_type2_points);
                pointsEditText.setText("" + sharedPreferences.getInt(pointsKey, 0));
                EditText option1EditText = findViewById(R.id.question_type2_option1);
                option1EditText.setText(sharedPreferences.getString(option1Key, ""));
                EditText option2EditText = findViewById(R.id.question_type2_option2);
                option2EditText.setText(sharedPreferences.getString(option2Key, ""));
                EditText option3EditText = findViewById(R.id.question_type2_option3);
                option3EditText.setText(sharedPreferences.getString(option3Key, ""));
                EditText option4EditText = findViewById(R.id.question_type2_option4);
                option4EditText.setText(sharedPreferences.getString(option4Key, ""));
                EditText textEditText = findViewById(R.id.question_type2_text);
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
        }
        else {
            setContentView(R.layout.form_question_type3);
            number = findViewById(R.id.question_type3_number);
            number.setText("" + currentNumber);
            questionQuantity = findViewById(R.id.question_type3_quantity);
            questionQuantity.setText("/" + quantity);
            button = findViewById(R.id.save_button);
            if (existingQuestion) {
                button.setText("Изменить вопрос");
                makeButtonWhite();
                SharedPreferences sharedPreferences = getSharedPreferences(TEST_SHARED_PREFS, MODE_PRIVATE);
                String additionalTextKey = currentNumber + "additionalText";
                String answerKey = currentNumber + "answer";
                String pointsKey = currentNumber + "points";
                String textKey = currentNumber + "text";
                EditText additionalTextEditText = findViewById(R.id.question_type3_sentence);
                additionalTextEditText.setText(sharedPreferences.getString(additionalTextKey, ""));
                EditText answerEditText = findViewById(R.id.question_type3_correct_answer);
                answerEditText.setText(sharedPreferences.getString(answerKey, ""));
                EditText pointsEditText = findViewById(R.id.question_type3_points);
                pointsEditText.setText("" + sharedPreferences.getInt(pointsKey, 0));
                EditText textEditText = findViewById(R.id.question_type3_text);
                textEditText.setText(sharedPreferences.getString(textKey, ""));
                additionalTextEditText.addTextChangedListener(textWatcher);
                answerEditText.addTextChangedListener(textWatcher);
                pointsEditText.addTextChangedListener(textWatcher);
                textEditText.addTextChangedListener(textWatcher);
            }
        }
    }

    public void addQuestionType1(View view)
    {
        if (!existingQuestion || questionChanged) {
            EditText pointsEditText = findViewById(R.id.question_type1_points);
            String points = pointsEditText.getText().toString();
            if (!points.isEmpty()) {
                EditText textEditText = findViewById(R.id.question_type1_text);
                String text = textEditText.getText().toString();
                if (!text.isEmpty()) {
                    EditText o1EditText = findViewById(R.id.question_type1_option1);
                    EditText o2EditText = findViewById(R.id.question_type1_option2);
                    EditText o3EditText = findViewById(R.id.question_type1_option3);
                    EditText o4EditText = findViewById(R.id.question_type1_option4);
                    String o1 = o1EditText.getText().toString();
                    String o2 = o2EditText.getText().toString();
                    String o3 = o3EditText.getText().toString();
                    String o4 = o4EditText.getText().toString();
                    if (!o1.isEmpty() && !o2.isEmpty() && !o3.isEmpty() && !o4.isEmpty()) {
                        EditText answerEditText = findViewById(R.id.question_type1_correct_answer);
                        String answer = answerEditText.getText().toString();
                        if (!answer.isEmpty()) {
                            if (isCorrectAnswer(answer)) {
                                SharedPreferences sharedPreferences = getSharedPreferences(TEST_SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt(NUMBER_OF_QUESTIONS, quantity);
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
                                String typeKey = currentNumber + "type";
                                editor.putInt(typeKey, 0);
                                editor.apply();
                                if (existingQuestion)
                                    Toast.makeText(this, "Вопрос изменен!", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(this, "Вопрос добавлен!", Toast.LENGTH_SHORT).show();
                                Intent buildingTestIntent = new Intent(this, BuildingTestActivity.class);
                                startActivity(buildingTestIntent);
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

    public void addQuestionType2(View view)
    {
        if (!existingQuestion || questionChanged) {
            EditText pointsEditText = findViewById(R.id.question_type2_points);
            String points = pointsEditText.getText().toString();
            if (!points.isEmpty()) {
                EditText textEditText = findViewById(R.id.question_type2_text);
                String text = textEditText.getText().toString();
                if (!text.isEmpty()) {
                    EditText additionalTextEditText = findViewById(R.id.question_type2_sentence);
                    String additionalText = additionalTextEditText.getText().toString();
                    if (!additionalText.isEmpty()) {
                        EditText o1EditText = findViewById(R.id.question_type2_option1);
                        EditText o2EditText = findViewById(R.id.question_type2_option2);
                        EditText o3EditText = findViewById(R.id.question_type2_option3);
                        EditText o4EditText = findViewById(R.id.question_type2_option4);
                        String o1 = o1EditText.getText().toString();
                        String o2 = o2EditText.getText().toString();
                        String o3 = o3EditText.getText().toString();
                        String o4 = o4EditText.getText().toString();
                        if (!o1.isEmpty() && !o2.isEmpty() && !o3.isEmpty() && !o4.isEmpty()) {
                            EditText answerEditText = findViewById(R.id.question_type2_correct_answer);
                            String answer = answerEditText.getText().toString();
                            if (!answer.isEmpty()) {
                                if (isCorrectAnswer(answer)) {
                                    SharedPreferences sharedPreferences = getSharedPreferences(TEST_SHARED_PREFS, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt(NUMBER_OF_QUESTIONS, quantity);
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
                                    String typeKey = currentNumber + "type";
                                    editor.putInt(typeKey, 1);
                                    editor.apply();
                                    if (existingQuestion)
                                        Toast.makeText(this, "Вопрос изменен!", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(this, "Вопрос добавлен!", Toast.LENGTH_SHORT).show();
                                    Intent buildingTestIntent = new Intent(this, BuildingTestActivity.class);
                                    startActivity(buildingTestIntent);
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

    public void addQuestionType3(View view)
    {
        if (!existingQuestion || questionChanged) {
            EditText pointsEditText = findViewById(R.id.question_type3_points);
            String points = pointsEditText.getText().toString();
            if (!points.isEmpty()) {
                EditText textEditText = findViewById(R.id.question_type3_text);
                String text = textEditText.getText().toString();
                if (!text.isEmpty()) {
                    EditText additionalTextEditText = findViewById(R.id.question_type3_sentence);
                    String additionalText = additionalTextEditText.getText().toString();
                    if (!additionalText.isEmpty()) {
                        EditText answerEditText = findViewById(R.id.question_type3_correct_answer);
                        String answer = answerEditText.getText().toString();
                        if (!answer.isEmpty()) {
                            SharedPreferences sharedPreferences = getSharedPreferences(TEST_SHARED_PREFS, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt(NUMBER_OF_QUESTIONS, quantity);
                            String additionalTextKey = currentNumber + "additionalText";
                            editor.putString(additionalTextKey, additionalText);
                            String answerKey = currentNumber + "answer";
                            editor.putString(answerKey, answer);
                            String pointsKey = currentNumber + "points";
                            editor.putInt(pointsKey, Integer.parseInt(points));
                            String option1Key = currentNumber + "option1";
                            editor.putString(option1Key, "");
                            String option2Key = currentNumber + "option2";
                            editor.putString(option2Key, "");
                            String option3Key = currentNumber + "option3";
                            editor.putString(option3Key, "");
                            String option4Key = currentNumber + "option4";
                            editor.putString(option4Key, "");
                            String textKey = currentNumber + "text";
                            editor.putString(textKey, text);
                            String typeKey = currentNumber + "type";
                            editor.putInt(typeKey, 2);
                            editor.apply();
                            if (existingQuestion)
                                Toast.makeText(this, "Вопрос изменен!", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(this, "Вопрос добавлен!", Toast.LENGTH_SHORT).show();
                            Intent buildingTestIntent = new Intent(this, BuildingTestActivity.class);
                            startActivity(buildingTestIntent);
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

    public void makeButtonWhite()
    {
        button.setBackgroundResource(R.drawable.orange_rounded_button);
        button.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void onBackPressed() {
        if (existingQuestion)
        {
            if (questionChanged)
            {
                final Context currentContext = this;
                new AlertDialog.Builder(this)
                        .setMessage("Вы уверены, что хотите выйти, не сохранив изменения?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent buildingTestIntent = new Intent(currentContext, BuildingTestActivity.class);
                                startActivity(buildingTestIntent);
                            }
                        })
                        .setNegativeButton("Нет", null)
                        .show();
            }
            else
            {
                Intent buildingTestIntent = new Intent(this, BuildingTestActivity.class);
                startActivity(buildingTestIntent);
            }
        }
        else {
            final Context currentContext = this;
            new AlertDialog.Builder(this)
                    .setMessage("Вы уверены, что хотите выйти, не сохранив вопрос?")
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent buildingTestIntent = new Intent(currentContext, BuildingTestActivity.class);
                            startActivity(buildingTestIntent);
                        }
                    })
                    .setNegativeButton("Нет", null)
                    .show();
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
