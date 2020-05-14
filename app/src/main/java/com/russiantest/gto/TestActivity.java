package com.russiantest.gto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TestActivity extends AppCompatActivity {

    private static ArrayList<Question> questions; // список всех вопросов текущего теста
    private static int currentQuestion; // номер текущего вопроса (нумерация с 0)
    private static int noOfQuestions;
    private static String[] answers; // массив ответов на каждый вопрос
    private static ArrayList<String> correctAnswers;
    private static CountDownTimer timer; // чтобы запускать таймер на тест
    private static String timeLeft;
    private static QuestionType currentType;

    private static DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String testName = intent.getStringExtra(MainActivity.TEST_NAME);

        if (!isInternetAvailable())
            Toast.makeText(this, "Проверьте свое подключение к интернету", Toast.LENGTH_SHORT).show();

        questions = new ArrayList<>();
        correctAnswers = new ArrayList<>();
        currentQuestion = 0;

        databaseReference = FirebaseDatabase.getInstance().getReference().child(testName);
        noOfQuestions = 0;
        loadTest();
    }

    public void startTimer()
    {
        // запускаем таймер на тест
        final Context currentContext = this;
        timer = new CountDownTimer(1800000, 1000) {
            public void onTick(long millisUntilFinished) {
                TextView time;
                @SuppressLint("DefaultLocale") String tl = String.format("%02d:%02d",
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                switch (currentType)
                {
                    case Standard:
                        time = findViewById(R.id.question1_timer);
                        break;
                    case Commas:
                        time = findViewById(R.id.question2_timer);
                        break;
                    default:
                        time = findViewById(R.id.question3_timer);
                }
                timeLeft = tl;
                time.setText(tl);
            }

            public void onFinish()
            {
                Intent finishIntent = new Intent(currentContext, ResultsActivity.class);
                String result = testResults();
                finishIntent.putExtra("result", result);
                startActivity(finishIntent);
            }
        }.start();
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
                    correctAnswers.add(Objects.requireNonNull(question).getAnswer());
                    noOfQuestions++;
                }
                answers = new String[noOfQuestions];
                currentType = questions.get(0).getType();
                loadQuestion();
                startTimer();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    public void loadQuestion() //  загружает на экран новый вопрос
    {
        if (currentType == QuestionType.Standard) //  если это первый тип, загружаем xml файл для вопроса первого типа
        {
            setContentView(R.layout.test_question1);
            if (currentQuestion == 0)
            {
                ImageView left = findViewById(R.id.question1_left);
                left.setVisibility(View.GONE);
            }
            if (currentQuestion == (noOfQuestions - 1))
            {
                ImageView right = findViewById(R.id.question1_right);
                right.setVisibility(View.GONE);
            }
            TextView number = findViewById(R.id.question1_number);
            int currentNumber = currentQuestion + 1;
            number.setText("" + currentNumber);
            TextView tl = findViewById(R.id.question1_timer);
            tl.setText(timeLeft);
            TextView numberOfQuestions = findViewById(R.id.question1_quantity);
            numberOfQuestions.setText("/" + noOfQuestions);

            String currentAnswer = answers[currentQuestion];
            TextView o1 = findViewById(R.id.question1_option1);
            TextView o2 = findViewById(R.id.question1_option2);
            TextView o3 = findViewById(R.id.question1_option3);
            TextView o4 = findViewById(R.id.question1_option4);
            if (currentAnswer != null && !currentAnswer.isEmpty()) {
                int ans = Integer.parseInt(currentAnswer);
                switch (ans) {
                    case (1):
                        o1.setBackgroundResource(R.drawable.blue_solid_rounded_button);
                        o1.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                    case (2):
                        o2.setBackgroundResource(R.drawable.blue_solid_rounded_button);
                        o2.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                    case (3):
                        o3.setBackgroundResource(R.drawable.blue_solid_rounded_button);
                        o3.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                    case (4):
                        o4.setBackgroundResource(R.drawable.blue_solid_rounded_button);
                        o4.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                }
            }
            // текст самого вопроса и варианты ответов
            TextView text = findViewById(R.id.question1_text);
            text.setText(questions.get(currentQuestion).getText());
            List<String> options = questions.get(currentQuestion).getOptions();
            o1.setText(options.get(0));
            o2.setText(options.get(1));
            o3.setText(options.get(2));
            o4.setText(options.get(3));
        }

        else if (currentType == QuestionType.Commas) // все точно так же, как и в предыдущем, только для другого xml файла
        {
            setContentView(R.layout.test_question2);
            if (currentQuestion == 0)
            {
                ImageView left = findViewById(R.id.question2_left);
                left.setVisibility(View.GONE);
            }
            if (currentQuestion == (noOfQuestions - 1))
            {
                ImageView right = findViewById(R.id.question2_right);
                right.setVisibility(View.GONE);
            }
            TextView number = findViewById(R.id.question2_number);
            int currentNumber = currentQuestion + 1;
            number.setText("" + currentNumber);
            TextView numberOfQuestions = findViewById(R.id.question2_quantity);
            numberOfQuestions.setText("/" + noOfQuestions);
            TextView tl = findViewById(R.id.question2_timer);
            tl.setText(timeLeft);

            String currentAnswer = answers[currentQuestion];
            TextView o1 = findViewById(R.id.question2_option1);
            TextView o2 = findViewById(R.id.question2_option2);
            TextView o3 = findViewById(R.id.question2_option3);
            TextView o4 = findViewById(R.id.question2_option4);
            if (currentAnswer != null && !currentAnswer.isEmpty()) {
                int ans = Integer.parseInt(currentAnswer);
                switch (ans) {
                    case (1):
                        o1.setBackgroundResource(R.drawable.blue_solid_rounded_button);
                        o1.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                    case (2):
                        o2.setBackgroundResource(R.drawable.blue_solid_rounded_button);
                        o2.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                    case (3):
                        o3.setBackgroundResource(R.drawable.blue_solid_rounded_button);
                        o3.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                    case (4):
                        o4.setBackgroundResource(R.drawable.blue_solid_rounded_button);
                        o4.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                }
            }
            TextView text = findViewById(R.id.question2_text);
            text.setText(questions.get(currentQuestion).getText());
            TextView additionalText = findViewById(R.id.question2_additional_text);
            additionalText.setText(questions.get(currentQuestion).getAdditionalText());
            List<String> options = questions.get(currentQuestion).getOptions();
            o1.setText(options.get(0));
            o2.setText(options.get(1));
            o3.setText(options.get(2));
            o4.setText(options.get(3));
        }

        else if (currentType == QuestionType.Input) //  тоже точно так же
        {
            setContentView(R.layout.test_question3);
            if (currentQuestion == 0)
            {
                ImageView left = findViewById(R.id.question3_left);
                left.setVisibility(View.GONE);
            }
            if (currentQuestion == (noOfQuestions - 1))
            {
                ImageView right = findViewById(R.id.question3_right);
                right.setVisibility(View.GONE);
            }
            TextView number = findViewById(R.id.question3_number);
            int currentNumber = currentQuestion + 1;
            number.setText("" + currentNumber);
            TextView numberOfQuestions = findViewById(R.id.question3_quantity);
            numberOfQuestions.setText("/" + noOfQuestions);
            TextView tl = findViewById(R.id.question3_timer);
            tl.setText(timeLeft);

            String currentAnswer = answers[currentQuestion];
            EditText answer = findViewById(R.id.question3_answer);
            if (currentAnswer != null && !currentAnswer.isEmpty()) {
                answer.setText(currentAnswer);
            }
            TextView text = findViewById(R.id.question3_text);
            text.setText(questions.get(currentQuestion).getText());
            TextView additionalText = findViewById(R.id.question3_additional_text);
            additionalText.setText(questions.get(currentQuestion).getAdditionalText());
            // добавляем это чтобы следить что тестируемый печатает и запоминать новый ответ
            answer.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    if (!answer.getText().toString().isEmpty())
                        answers[currentQuestion] = answer.getText().toString();
                    else
                        answers[currentQuestion] = "";
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });
        }
    }

    public void nextQuestion(View view)
    {
        if (currentQuestion < noOfQuestions - 1)
        {
            ++currentQuestion;
            currentType = questions.get(currentQuestion).getType();
            loadQuestion();
        }
    }

    public void prevQuestion(View view)
    {
        if (currentQuestion > 0)
        {
            --currentQuestion;
            currentType = questions.get(currentQuestion).getType();
            loadQuestion();
        }
    }

    public void answer1(View view) // при нажатии на один из вариантов ответа для вопроса первого типа
    {
        TextView option = (TextView) view;
        // если нажали на вариант, который уже был выбран, просто отменяем его выбор
        if (Objects.equals(option.getBackground().getConstantState(),
                getResources().getDrawable(R.drawable.blue_solid_rounded_button).getConstantState())) {
            option.setBackgroundResource(R.drawable.blue_rounded_button);
            option.setTextColor(getResources().getColor(R.color.colorPrimary));
            answers[currentQuestion] = "";
        } else {
            // тут сначала делаем все варианты ответа "невыбранными", чтобы нельзя было выбрать 2 одновременно, и выбираем нужный
            TextView o1 = findViewById(R.id.question1_option1);
            if (o1.getText().toString().equals(option.getText().toString()))
                answers[currentQuestion] = "1";
            o1.setBackgroundResource(R.drawable.blue_rounded_button);
            o1.setTextColor(getResources().getColor(R.color.colorPrimary));
            TextView o2 = findViewById(R.id.question1_option2);
            if (o2.getText().toString().equals(option.getText().toString()))
                answers[currentQuestion] = "2";
            o2.setBackgroundResource(R.drawable.blue_rounded_button);
            o2.setTextColor(getResources().getColor(R.color.colorPrimary));
            TextView o3 = findViewById(R.id.question1_option3);
            if (o3.getText().toString().equals(option.getText().toString()))
                answers[currentQuestion] = "3";
            o3.setBackgroundResource(R.drawable.blue_rounded_button);
            o3.setTextColor(getResources().getColor(R.color.colorPrimary));
            TextView o4 = findViewById(R.id.question1_option4);
            if (o4.getText().toString().equals(option.getText().toString()))
                answers[currentQuestion] = "4";
            o4.setBackgroundResource(R.drawable.blue_rounded_button);
            o4.setTextColor(getResources().getColor(R.color.colorPrimary));

            option.setBackgroundResource(R.drawable.blue_solid_rounded_button);
            option.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    public void answer2(View view) // при нажатии на один из вариантов ответа для вопроса второго типа
    {
        TextView option = (TextView) view;
        if (Objects.equals(option.getBackground().getConstantState(),
                getResources().getDrawable(R.drawable.blue_solid_rounded_button).getConstantState())) {
            option.setBackgroundResource(R.drawable.blue_rounded_button);
            option.setTextColor(getResources().getColor(R.color.colorPrimary));
            answers[currentQuestion] = "";
        } else {
            TextView o1 = findViewById(R.id.question2_option1);
            if (o1.getText().toString().equals(option.getText().toString()))
                answers[currentQuestion] = "1";
            o1.setBackgroundResource(R.drawable.blue_rounded_button);
            o1.setTextColor(getResources().getColor(R.color.colorPrimary));
            TextView o2 = findViewById(R.id.question2_option2);
            if (o2.getText().toString().equals(option.getText().toString()))
                answers[currentQuestion] = "2";
            o2.setBackgroundResource(R.drawable.blue_rounded_button);
            o2.setTextColor(getResources().getColor(R.color.colorPrimary));
            TextView o3 = findViewById(R.id.question2_option3);
            if (o3.getText().toString().equals(option.getText().toString()))
                answers[currentQuestion] = "3";
            o3.setBackgroundResource(R.drawable.blue_rounded_button);
            o3.setTextColor(getResources().getColor(R.color.colorPrimary));
            TextView o4 = findViewById(R.id.question2_option4);
            if (o4.getText().toString().equals(option.getText().toString()))
                answers[currentQuestion] = "4";
            o4.setBackgroundResource(R.drawable.blue_rounded_button);
            o4.setTextColor(getResources().getColor(R.color.colorPrimary));

            option.setBackgroundResource(R.drawable.blue_solid_rounded_button);
            option.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    public void finishTest(View view) //  при нажатии на кнопку "завершить тест"
    {
        final Context currentContext = this;
        // спрашиваем тестируемого, уверен он или нет, и если уверен то сбрасываем таймер и завершаем тест
        new AlertDialog.Builder(this)
                .setMessage("Вы уверены, что хотите завершить тест?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        timer.cancel();
                        Intent finishIntent = new Intent(currentContext, ResultsActivity.class);
                        String result = testResults();
                        finishIntent.putExtra("result", result);
                        startActivity(finishIntent);
                    }
                })
                .setNegativeButton("Нет", null)
                .show();
    }

    @Override
    public void onBackPressed() // чтобы при нажатии кнопки "назад" мы делали то же самое, что и при попытке завершить тест
    {
        final Context currentContext = this;
        new AlertDialog.Builder(this)
                .setMessage("Вы уверены, что хотите завершить тест?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        timer.cancel();
                        Intent finishIntent = new Intent(currentContext, ResultsActivity.class);
                        String result = testResults();
                        finishIntent.putExtra("result", result);
                        startActivity(finishIntent);
                    }
                })
                .setNegativeButton("Нет", null)
                .show();
    }

    private String testResults()
    {
        int points = 0;
        int totalPoints = 0;
        for (int i = 0; i < noOfQuestions; i++) {
            if (Objects.equals(answers[i], correctAnswers.get(i)))
                points += questions.get(i).getPoints();
            totalPoints += questions.get(i).getPoints();
        }
        return points + "/" + totalPoints;
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void selectQuestion(View view)
    {
        MyAdapter adapter = new MyAdapter(this, R.layout.select_question, answers);
        new AlertDialog.Builder(this)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentQuestion = which;
                        currentType = questions.get(which).getType();
                        loadQuestion();
                    }
                }).create().show();
    }

    public class MyAdapter extends ArrayAdapter {

        public MyAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.select_question, parent, false);
            TextView text = layout.findViewById(R.id.spinner_question_text);
            int currIndex = position + 1;
            String questionText = "" + currIndex + ". " + questions.get(position).getText();
            text.setText(questionText);
            ImageView img = layout.findViewById(R.id.spinner_question_checkmark);
            String currAnswer = answers[position];
            if (currAnswer != null && !currAnswer.isEmpty())
                img.setVisibility(View.VISIBLE);
            return layout;
        }

        @Override
        public int getCount() {
            return questions.size();
        }

        // It gets a View that displays in the drop down popup the data at the specified position
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        // It gets a View that displays the data at the specified position
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
    }
}
