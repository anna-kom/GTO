package com.russiantest.gto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    public static final String USER_DATA = "Userdata";  // Строка для передачи почты и пароля в MainActivity
    private static final String SHARED_PREFS = "sharedPrefs";  // Строка для обращния к SharedPreferences
    public static final String USER_EXITED_ACCOUNT = "UserExited"; // Строка для запонимания вышел пользователь из аккаунта или нет

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

    }

    public void login(View view) // при нажатии на кнопку зарегистрироваться или войти (пока одно и то же)
    {
        Button loginButton = findViewById(R.id.login_button);
        if (loginButton.getText().toString().startsWith("З")) {
            Intent regIntent = new Intent(this, RegistrationActivity.class);
            startActivity(regIntent);
        }
        EditText email_edit_text = findViewById(R.id.login_email);
        String email = email_edit_text.getText().toString();
        EditText password_edit_text = findViewById(R.id.login_password);
        String password = password_edit_text.getText().toString();

        // проверяем, можем ли мы его зарегистрировать с такими данными
        if (email.isEmpty())
            Toast.makeText(this, "Введите почту", Toast.LENGTH_SHORT).show();
        else if (password.isEmpty())
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();
        else if (!SettingsActivity.isValid(email))
            Toast.makeText(this, "Введите корректный адрес почты", Toast.LENGTH_SHORT).show();
        else {
            // запоминаем почту и пароль и передаем в MainActivity
            String user = email + " " + password;
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.putExtra(USER_DATA, user);
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(USER_EXITED_ACCOUNT, false);
            editor.apply();
            startActivity(mainIntent);
        }
    }

    public void no_account(View view) // при нажатии на кнопку "уже есть аккаунт" или "нет аккаунта". просто меняет текст кнопок на экране
    {
        TextView account = findViewById(R.id.login_no_account);
        TextView forgot_password = findViewById(R.id.login_forgot_password);
        Button loginButton = findViewById(R.id.login_button);
        if (account.getText().toString().startsWith("Н")) {
            forgot_password.setVisibility(View.GONE);
            loginButton.setText("Зарегистрироваться");
            account.setText(R.string.hasAnAccount);
        } else {
            forgot_password.setVisibility(View.VISIBLE);
            loginButton.setText("Войти");
            account.setText(R.string.noAccount);
        }
    }

    @Override
    public void onBackPressed() // чтобы при нажатии кнопки "назад" мы выходили из приложения и не зацикливались
    {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}
