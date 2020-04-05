package com.russiantest.gto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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
import java.util.Objects;

public class TestAdminActivity extends AppCompatActivity {

    private static ArrayList<Question> questions; // список всех вопросов текущего теста
    private static int currentQuestion; // номер текущего вопроса (нумерация с 0)
    private static int noOfQuestions;
    private static String testName;

    private static DatabaseReference databaseReference;

    private static boolean question1Changed;
    private static boolean question2Changed;
    private static boolean question3Changed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        testName = intent.getStringExtra(MainActivity.TEST_NAME);
        questions = new ArrayList<>();
        currentQuestion = 0;

        databaseReference = FirebaseDatabase.getInstance().getReference().child(testName);
        noOfQuestions = 0;
        loadTest();

        // чтобы можно было свайпать и перемещаться между вопросами
        findViewById(android.R.id.content).getRootView().setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                if (currentQuestion < noOfQuestions - 1)
                    loadQuestion(questions.get(++currentQuestion));
            }

            @Override
            public void onSwipeRight() {
                if (currentQuestion > 0)
                    loadQuestion(questions.get(--currentQuestion));
            }
        });

        Toast.makeText(this, "Листайте влево/вправо, чтобы перемещаться между вопросами", Toast.LENGTH_LONG).show();
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
                    noOfQuestions++;
                }
                loadQuestion(questions.get(0));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void loadQuestion(Question question) //  загружает на экран новый вопрос
    {
        QuestionType type = question.getType();
        if (type == QuestionType.Standard) //  если это первый тип, загружаем xml файл для вопроса первого типа
        {
            setContentView(R.layout.view_question1);
            TextView number = findViewById(R.id.view_question1_number);
            int currentNumber = currentQuestion + 1;
            number.setText("" + currentNumber);
            TextView numberOfQuestions = findViewById(R.id.view_question1_quantity);
            numberOfQuestions.setText("\\" + noOfQuestions);
            EditText text = findViewById(R.id.view_question1_text);
            text.setText(question.getText());
            EditText o1 = findViewById(R.id.view_question1_option1);
            EditText o2 = findViewById(R.id.view_question1_option2);
            EditText o3 = findViewById(R.id.view_question1_option3);
            EditText o4 = findViewById(R.id.view_question1_option4);
            List<String> options = question.getOptions();
            o1.setText(options.get(0));
            o2.setText(options.get(1));
            o3.setText(options.get(2));
            o4.setText(options.get(3));
            EditText answer = findViewById(R.id.view_question1_correct_answer);
            answer.setText(question.getAnswer());

            text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    question1Changed = true;
                }
            });
            o1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    question1Changed = true;
                }
            });
            o2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    question1Changed = true;
                }
            });
            o3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    question1Changed = true;
                }
            });
            o4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    question1Changed = true;
                }
            });
            answer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    question1Changed = true;
                }
            });
            return;
        }

        if (type == QuestionType.Commas) // все точно так же, как и в предыдущем, только для другого xml файла
        {
            setContentView(R.layout.view_question2);
            TextView number = findViewById(R.id.view_question2_number);
            int currentNumber = currentQuestion + 1;
            number.setText("" + currentNumber);
            TextView numberOfQuestions = findViewById(R.id.view_question2_quantity);
            numberOfQuestions.setText("\\" + noOfQuestions);
            EditText text = findViewById(R.id.view_question2_text);
            text.setText(question.getText());
            EditText additionalText = findViewById(R.id.view_question2_sentence);
            additionalText.setText(question.getAdditionalText());
            EditText o1 = findViewById(R.id.view_question2_option1);
            EditText o2 = findViewById(R.id.view_question2_option2);
            EditText o3 = findViewById(R.id.view_question2_option3);
            EditText o4 = findViewById(R.id.view_question2_option4);
            List<String> options = question.getOptions();
            o1.setText(options.get(0));
            o2.setText(options.get(1));
            o3.setText(options.get(2));
            o4.setText(options.get(3));
            EditText answer = findViewById(R.id.view_question2_correct_answer);
            answer.setText(question.getAnswer());

            text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    question2Changed = true;
                }
            });
            additionalText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    question2Changed = true;
                }
            });
            o1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    question2Changed = true;
                }
            });
            o2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    question2Changed = true;
                }
            });
            o3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    question2Changed = true;
                }
            });
            o4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    question2Changed = true;
                }
            });
            answer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    question2Changed = true;
                }
            });
            return;
        }

        if (type == QuestionType.Input) //  тоже точно так же
        {
            setContentView(R.layout.view_question3);
            TextView number = findViewById(R.id.view_question3_number);
            int currentNumber = currentQuestion + 1;
            number.setText("" + currentNumber);
            TextView numberOfQuestions = findViewById(R.id.view_question3_quantity);
            numberOfQuestions.setText("\\" + noOfQuestions);
            EditText text = findViewById(R.id.view_question3_text);
            text.setText(question.getText());
            EditText additionalText = findViewById(R.id.view_question3_sentence);
            additionalText.setText(question.getAdditionalText());
            EditText answer = findViewById(R.id.view_question3_correct_answer);
            answer.setText(question.getAnswer());

            text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    question3Changed = true;
                }
            });
            additionalText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    question3Changed = true;
                }
            });
            answer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    question3Changed = true;
                }
            });
        }
    }

    public void changeQuestionType1(View view)
    {
        if (question1Changed) {
            int currNumber = currentQuestion + 1;
            String number = "" + currNumber;
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
                    List<String> options = Arrays.asList(o1, o2, o3, o4);
                    EditText answerEditText = findViewById(R.id.view_question1_correct_answer);
                    String answer = answerEditText.getText().toString();
                    if (!answer.isEmpty()) {
                        Question question = new Question();
                        question.setAdditionalText("");
                        question.setAnswer(answer);
                        question.setNeedsInput(false);
                        question.setOptions(options);
                        question.setText(text);
                        question.setType(QuestionType.Standard);
                        databaseReference.child(number).setValue(question);
                        Toast.makeText(this, "Вопрос изменен!", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "Введите ответ", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, "Введите все варианты ответа", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Введите текст вопроса", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Нет изменений", Toast.LENGTH_SHORT).show();
    }

    public void changeQuestionType2(View view)
    {
        if (question2Changed) {
            int currNumber = currentQuestion + 1;
            String number = "" + currNumber;
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
                        List<String> options = Arrays.asList(o1, o2, o3, o4);
                        EditText answerEditText = findViewById(R.id.view_question2_correct_answer);
                        String answer = answerEditText.getText().toString();
                        if (!answer.isEmpty()) {
                            Question question = new Question();
                            question.setAdditionalText(additionalText);
                            question.setAnswer(answer);
                            question.setNeedsInput(false);
                            question.setOptions(options);
                            question.setText(text);
                            question.setType(QuestionType.Commas);
                            databaseReference.child(number).setValue(question);
                            Toast.makeText(this, "Вопрос изменен!", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(this, "Введите ответ", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "Введите все варианты ответа", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, "Введите предложение вопроса", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Введите текст вопроса", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Нет изменений", Toast.LENGTH_SHORT).show();
    }

    public void changeQuestionType3(View view)
    {
        if (question3Changed) {
            int currNumber = currentQuestion + 1;
            String number = "" + currNumber;
            EditText textEditText = findViewById(R.id.view_question3_text);
            String text = textEditText.getText().toString();
            if (!text.isEmpty()) {
                EditText additionalTextEditText = findViewById(R.id.view_question3_sentence);
                String additionalText = additionalTextEditText.getText().toString();
                if (!additionalText.isEmpty()) {
                    EditText answerEditText = findViewById(R.id.view_question3_correct_answer);
                    String answer = answerEditText.getText().toString();
                    if (!answer.isEmpty()) {
                        Question question = new Question();
                        question.setAdditionalText(additionalText);
                        question.setAnswer(answer);
                        question.setNeedsInput(true);
                        question.setOptions(null);
                        question.setText(text);
                        question.setType(QuestionType.Input);
                        databaseReference.child(number).setValue(question);
                        Toast.makeText(this, "Вопрос изменен!", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "Введите ответ", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, "Введите предложение вопроса", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Введите текст вопроса", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Нет изменений", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed()
    {
        Intent finishIntent = new Intent(this, ViewTestsActivity.class);
        startActivity(finishIntent);
    }
}
