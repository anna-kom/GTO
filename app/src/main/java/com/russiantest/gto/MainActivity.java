package com.russiantest.gto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.invoke.ConstantCallSite;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs"; // Строка для обращния к SharedPreferences
    public static final String USER_IS_LOGGED_IN = "loggedIn"; // Строка для запоминания вошел пользователь в аккаунт или нет
    public static final String USER_DATA = "Userdata"; // Строка для передачи почты и пароля
    public static final String USER_NAME = "Username"; // Строка для запоминания имени пользователя
    public static final String USER_PASSWORD = "UserPassword"; // Строка для запоминания пароля пользователя
    public static final String USER_EMAIL = "UserEmail"; // Строка для запоминания почты
    public static final String USER_EXITED_ACCOUNT = "UserExited"; // Строка для запонимания вышел пользователь из аккаунта или нет
    public static final String TEST_NAME = "testName";

    private static DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // проверяем, вошел ли пользователь через LoginActivity
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Intent thisIntent = getIntent();
        String user = thisIntent.getStringExtra(USER_DATA);
        if (user != null) // сработает только если мы пришли из LoginActivity, а дальше запоминаем данные пользователя
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(USER_IS_LOGGED_IN, true);
            editor.putString(USER_EMAIL, user.split(" ")[0]);
            editor.putString(USER_PASSWORD, user.split(" ")[1]);
            editor.apply();
        }

        // проверяем, что пользователь вошел в акаунт и не выходил из него
        boolean isLoggedIn = sharedPreferences.getBoolean(USER_IS_LOGGED_IN, false);
        boolean exited = sharedPreferences.getBoolean(USER_EXITED_ACCOUNT, false);
        if (!isLoggedIn || exited) // если пользователь не вошел, запускаем LoginActivity
        {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }

        String email = sharedPreferences.getString(USER_EMAIL, "");
        String password = sharedPreferences.getString(USER_PASSWORD, "");
        if (check_admin(email, password))
        {
            Intent adminIntent = new Intent(this, AdminActivity.class);
            startActivity(adminIntent);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
        LinearLayout layout = findViewById(R.id.main_linear_layout);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    View test = getLayoutInflater().inflate(R.layout.test_name, null);
                    TextView text = test.findViewById(R.id.test_name_text);
                    String testName = snapshot.getKey();
                    text.setText(testName);
                    layout.addView(test);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void startTest(View view)
    {
        TextView testName = view.findViewById(R.id.test_name_text);
        String name = testName.getText().toString();
        Intent testIntent = new Intent(this, TestActivity.class);
        testIntent.putExtra(TEST_NAME, name);
        startActivity(testIntent);
    }

    public void openSettings(View view) // при нажатии на кнопочку настроек
    {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    @Override
    public void onBackPressed() // чтобы при нажатии кнопки "назад" мы выходили из приложения и не зацикливались
    {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public boolean check_admin(String email, String password) {
        return email.equals("admin@mail.com") && password.equals("admin");
    }
}
