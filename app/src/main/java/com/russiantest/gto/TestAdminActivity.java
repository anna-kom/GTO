package com.russiantest.gto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TestAdminActivity extends AppCompatActivity {

    private static ArrayList<Question> questions; // список всех вопросов текущего теста
    private static int currentQuestion; // номер текущего вопроса (нумерация с 0)
    private static int noOfQuestions;
    private static String testName;
    private static QuestionType currentType;
    private static Button button;

    private static DatabaseReference databaseReference;

    private static boolean question1Changed;
    private static boolean question2Changed;
    private static boolean question3Changed;

    TextWatcher textWatcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            question1Changed = true; makeButtonOrange();
        }
    };
    TextWatcher textWatcher2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            question2Changed = true; makeButtonOrange();
        }
    };
    TextWatcher textWatcher3 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            question3Changed = true; makeButtonOrange();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isInternetAvailable())
            Toast.makeText(this, "Проверьте свое подключение к интернету", Toast.LENGTH_SHORT).show();

        Intent intent = getIntent();
        testName = intent.getStringExtra(MainActivity.TEST_NAME);
        questions = new ArrayList<>();
        currentQuestion = 0;

        databaseReference = FirebaseDatabase.getInstance().getReference().child(testName);
        noOfQuestions = 0;
        loadTest();
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
                currentType = questions.get(0).getType();
                loadQuestion(questions.get(0));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void adminNextQuestion(View view)
    {
        if (currentQuestion < noOfQuestions - 1)
        {
            if ((currentType == QuestionType.Standard) && question1Changed ||
                    (currentType == QuestionType.Commas) && question2Changed ||
                    (currentType == QuestionType.Input) && question3Changed)
            {
                new AlertDialog.Builder(this)
                        .setMessage("Вы уверены, что хотите перейти к другому вопросу, не сохранив изменения?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                question1Changed = false;
                                question2Changed = false;
                                question3Changed = false;
                                ++currentQuestion;
                                currentType = questions.get(currentQuestion).getType();
                                loadQuestion(questions.get(currentQuestion));
                            }
                        })
                        .setNegativeButton("Нет", null)
                        .show();
            }
            else
            {
                ++currentQuestion;
                currentType = questions.get(currentQuestion).getType();
                loadQuestion(questions.get(currentQuestion));
            }
        }
    }

    public void adminPrevQuestion(View view)
    {
        if (currentQuestion > 0)
        {
            if ((currentType == QuestionType.Standard) && question1Changed ||
                    (currentType == QuestionType.Commas) && question2Changed ||
                    (currentType == QuestionType.Input) && question3Changed)
            {
                new AlertDialog.Builder(this)
                        .setMessage("Вы уверены, что хотите перейти к другому вопросу, не сохранив изменения?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                question1Changed = false;
                                question2Changed = false;
                                question3Changed = false;
                                --currentQuestion;
                                currentType = questions.get(currentQuestion).getType();
                                loadQuestion(questions.get(currentQuestion));
                            }
                        })
                        .setNegativeButton("Нет", null)
                        .show();
            }
            else
            {
                --currentQuestion;
                currentType = questions.get(currentQuestion).getType();
                loadQuestion(questions.get(currentQuestion));
            }
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

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    public void loadQuestion(Question question) //  загружает на экран новый вопрос
    {
        if (currentType == QuestionType.Standard) //  если это первый тип, загружаем xml файл для вопроса первого типа
        {
            setContentView(R.layout.view_question1);
            button = findViewById(R.id.view_question1_save_button);
            TextView number = findViewById(R.id.view_question1_number);
            int currentNumber = currentQuestion + 1;
            number.setText("" + currentNumber);
            TextView numberOfQuestions = findViewById(R.id.view_question1_quantity);
            numberOfQuestions.setText("/" + noOfQuestions);
            EditText points = findViewById(R.id.view_question1_points);
            points.setText("" + question.getPoints());
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

            points.addTextChangedListener(textWatcher1);
            text.addTextChangedListener(textWatcher1);
            o1.addTextChangedListener(textWatcher1);
            o2.addTextChangedListener(textWatcher1);
            o3.addTextChangedListener(textWatcher1);
            o4.addTextChangedListener(textWatcher1);
            answer.addTextChangedListener(textWatcher1);

            return;
        }

        if (currentType == QuestionType.Commas) // все точно так же, как и в предыдущем, только для другого xml файла
        {
            setContentView(R.layout.view_question2);
            button = findViewById(R.id.view_question2_save_button);
            TextView number = findViewById(R.id.view_question2_number);
            int currentNumber = currentQuestion + 1;
            number.setText("" + currentNumber);
            TextView numberOfQuestions = findViewById(R.id.view_question2_quantity);
            numberOfQuestions.setText("/" + noOfQuestions);
            EditText points = findViewById(R.id.view_question2_points);
            points.setText("" + question.getPoints());
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

            points.addTextChangedListener(textWatcher2);
            text.addTextChangedListener(textWatcher2);
            additionalText.addTextChangedListener(textWatcher2);
            o1.addTextChangedListener(textWatcher2);
            o2.addTextChangedListener(textWatcher2);
            o3.addTextChangedListener(textWatcher2);
            o4.addTextChangedListener(textWatcher2);
            answer.addTextChangedListener(textWatcher2);

            return;
        }

        if (currentType == QuestionType.Input) //  тоже точно так же
        {
            setContentView(R.layout.view_question3);
            button = findViewById(R.id.view_question3_save_button);
            TextView number = findViewById(R.id.view_question3_number);
            int currentNumber = currentQuestion + 1;
            number.setText("" + currentNumber);
            TextView numberOfQuestions = findViewById(R.id.view_question3_quantity);
            numberOfQuestions.setText("/" + noOfQuestions);
            EditText points = findViewById(R.id.view_question3_points);
            points.setText("" + question.getPoints());
            EditText text = findViewById(R.id.view_question3_text);
            text.setText(question.getText());
            EditText additionalText = findViewById(R.id.view_question3_sentence);
            additionalText.setText(question.getAdditionalText());
            EditText answer = findViewById(R.id.view_question3_correct_answer);
            answer.setText(question.getAnswer());

            points.addTextChangedListener(textWatcher3);
            text.addTextChangedListener(textWatcher3);
            additionalText.addTextChangedListener(textWatcher3);
            answer.addTextChangedListener(textWatcher3);
        }
    }

    public void changeQuestionType1(View view)
    {
        if (question1Changed) {
            int currNumber = currentQuestion + 1;
            String number = "" + currNumber;
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
                        List<String> options = Arrays.asList(o1, o2, o3, o4);
                        EditText answerEditText = findViewById(R.id.view_question1_correct_answer);
                        String answer = answerEditText.getText().toString();
                        if (!answer.isEmpty()) {
                            Question question = new Question();
                            question.setAdditionalText("");
                            question.setAnswer(answer);
                            question.setPoints(Integer.parseInt(points));
                            question.setOptions(options);
                            question.setText(text);
                            question.setType(QuestionType.Standard);
                            databaseReference.child(number).setValue(question);
                            questions.set(currentQuestion, question);
                            makeButtonWhite();
                            question1Changed = false;
                            Toast.makeText(this, "Вопрос изменен!", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(this, "Введите ответ", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "Введите все варианты ответа", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, "Введите текст вопроса", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Введите баллы", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Нет изменений", Toast.LENGTH_SHORT).show();
    }

    public void changeQuestionType2(View view)
    {
        if (question2Changed) {
            int currNumber = currentQuestion + 1;
            String number = "" + currNumber;
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
                            List<String> options = Arrays.asList(o1, o2, o3, o4);
                            EditText answerEditText = findViewById(R.id.view_question2_correct_answer);
                            String answer = answerEditText.getText().toString();
                            if (!answer.isEmpty()) {
                                Question question = new Question();
                                question.setAdditionalText(additionalText);
                                question.setAnswer(answer);
                                question.setPoints(Integer.parseInt(points));
                                question.setOptions(options);
                                question.setText(text);
                                question.setType(QuestionType.Commas);
                                databaseReference.child(number).setValue(question);
                                questions.set(currentQuestion, question);
                                makeButtonWhite();
                                question2Changed = false;
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
                Toast.makeText(this, "Введите баллы", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Нет изменений", Toast.LENGTH_SHORT).show();
    }

    public void changeQuestionType3(View view)
    {
        if (question3Changed) {
            int currNumber = currentQuestion + 1;
            String number = "" + currNumber;
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
                            Question question = new Question();
                            question.setAdditionalText(additionalText);
                            question.setAnswer(answer);
                            question.setPoints(Integer.parseInt(points));
                            question.setOptions(null);
                            question.setText(text);
                            question.setType(QuestionType.Input);
                            databaseReference.child(number).setValue(question);
                            questions.set(currentQuestion, question);
                            makeButtonWhite();
                            question3Changed = false;
                            Toast.makeText(this, "Вопрос изменен!", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(this, "Введите ответ", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "Введите предложение вопроса", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, "Введите текст вопроса", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Введите баллы", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Нет изменений", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed()
    {
        if ((currentType == QuestionType.Standard) && question1Changed ||
                (currentType == QuestionType.Commas) && question2Changed ||
                (currentType == QuestionType.Input) && question3Changed)
        {
            final Context currentContext = this;
            new AlertDialog.Builder(this)
                    .setMessage("Вы уверены, что хотите выйти, не сохранив изменения?")
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent finishIntent = new Intent(currentContext, ViewTestsActivity.class);
                            startActivity(finishIntent);
                        }
                    })
                    .setNegativeButton("Нет", null)
                    .show();
        }
        else
        {
            Intent finishIntent = new Intent(this, ViewTestsActivity.class);
            startActivity(finishIntent);
        }
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
