package com.russiantest.gto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class QuestionBuildingActivity extends AppCompatActivity {

    private static String testName;
    private static DatabaseReference databaseReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int type = getIntent().getExtras().getInt("type");
        testName = getIntent().getStringExtra("name");
        switch (type) {
            case 0:
                setContentView(R.layout.form_question_type1);
                break;
            case 1:
                setContentView(R.layout.form_question_type2);
                break;
            case 2:
                setContentView(R.layout.form_question_type3);
                break;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void addQuestionType1(View view)
    {
        EditText numberEditText = findViewById(R.id.question_type1_number);
        String number = numberEditText.getText().toString();
        if (!number.isEmpty()) {
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
                    List<String> options = Arrays.asList(o1, o2, o3, o4);
                    EditText answerEditText = findViewById(R.id.question_type1_correct_answer);
                    String answer = answerEditText.getText().toString();
                    if (!answer.isEmpty()) {
                        Question question = new Question();
                        question.setAdditionalText("");
                        question.setAnswer(answer);
                        question.setNeedsInput(false);
                        question.setOptions(options);
                        question.setText(text);
                        question.setType(QuestionType.Standard);
                        databaseReference.child(testName).child(number).setValue(question);
                        Toast.makeText(this, "Вопрос добавлен!", Toast.LENGTH_SHORT).show();
                        Intent buildingTestIntent = new Intent(this, BuildingTestActivity.class);
                        buildingTestIntent.putExtra("name", testName);
                        startActivity(buildingTestIntent);
                    }
                    else
                        Toast.makeText(this, "Введите ответ", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(this, "Введите все варианты ответа", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Введите текст вопроса", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Введите номер вопроса", Toast.LENGTH_SHORT).show();
    }

    public void addQuestionType2(View view)
    {
        EditText numberEditText = findViewById(R.id.question_type2_number);
        String number = numberEditText.getText().toString();
        if (!number.isEmpty()) {
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
                        List<String> options = Arrays.asList(o1, o2, o3, o4);
                        EditText answerEditText = findViewById(R.id.question_type2_correct_answer);
                        String answer = answerEditText.getText().toString();
                        if (!answer.isEmpty()) {
                            Question question = new Question();
                            question.setAdditionalText(additionalText);
                            question.setAnswer(answer);
                            question.setNeedsInput(false);
                            question.setOptions(options);
                            question.setText(text);
                            question.setType(QuestionType.Commas);
                            databaseReference.child(testName).child(number).setValue(question);
                            Toast.makeText(this, "Вопрос добавлен!", Toast.LENGTH_SHORT).show();
                            Intent buildingTestIntent = new Intent(this, BuildingTestActivity.class);
                            buildingTestIntent.putExtra("name", testName);
                            startActivity(buildingTestIntent);
                        } else
                            Toast.makeText(this, "Введите ответ", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "Введите все варианты ответа", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, "Введите предложение для вопроса", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Введите текст вопроса", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Введите номер вопроса", Toast.LENGTH_SHORT).show();
    }

    public void addQuestionType3(View view)
    {
        EditText numberEditText = findViewById(R.id.question_type3_number);
        String number = numberEditText.getText().toString();
        if (!number.isEmpty()) {
            EditText textEditText = findViewById(R.id.question_type3_text);
            String text = textEditText.getText().toString();
            if (!text.isEmpty()) {
                EditText additionalTextEditText = findViewById(R.id.question_type3_sentence);
                String additionalText = additionalTextEditText.getText().toString();
                if (!additionalText.isEmpty()) {
                    EditText answerEditText = findViewById(R.id.question_type3_correct_answer);
                    String answer = answerEditText.getText().toString();
                    if (!answer.isEmpty()) {
                        Question question = new Question();
                        question.setAdditionalText(additionalText);
                        question.setAnswer(answer);
                        question.setNeedsInput(true);
                        question.setOptions(null);
                        question.setText(text);
                        question.setType(QuestionType.Input);
                        databaseReference.child(testName).child(number).setValue(question);
                        Toast.makeText(this, "Вопрос добавлен!", Toast.LENGTH_SHORT).show();
                        Intent buildingTestIntent = new Intent(this, BuildingTestActivity.class);
                        buildingTestIntent.putExtra("name", testName);
                        startActivity(buildingTestIntent);
                    } else
                        Toast.makeText(this, "Введите ответ", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, "Введите предложение для вопроса", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Введите текст вопроса", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Введите номер вопроса", Toast.LENGTH_SHORT).show();
    }
}
