package com.russiantest.gto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.UserNotAuthenticatedException;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class SettingsActivity extends AppCompatActivity {

    private static final String SHARED_PREFS = "sharedPrefs";
    public static final String USER_NAME = "Username";
    public static final String USER_PASSWORD = "UserPassword";
    public static final String USER_EMAIL = "UserEmail";
    private static final String USER_IS_LOGGED_IN = "loggedIn";
    public static final String USER_EXITED_ACCOUNT = "UserExited";

    private static boolean openedAdditional; // чтобы запоминать, нажал пользователь на какую-то из конопок в настройках или нет (то есть откыт ли экран настройки имени или типа того) - нужно чтобы корректно выходить отсюда

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        openedAdditional = false;
    }

    public void editName(View view) // при нажатии на "имя" запоминаем новое имя пользователя (пока он у меня при регистрации его не вводит)
    {
        openedAdditional = true;
        setContentView(R.layout.change_name);
        final EditText editText = findViewById(R.id.change_input);
        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String name = sharedPreferences.getString(USER_NAME, "");
        if (!name.isEmpty())
            editText.setText(name);
        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String newName = editText.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(USER_NAME, newName);
                editor.apply();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    public void openAbout(View view) // при нажатии на "об организаторах"
    {
        openedAdditional = true;
        setContentView(R.layout.about);
    }

    public void openResults(View view) // при нажатии на "как узнать результаты"
    {
        openedAdditional = true;
        setContentView(R.layout.about_results);
    }

    public void exitAccount(View view) // при нажатии на "выйти"
    {
        final Context currentContext = this;
        new AlertDialog.Builder(this)
                .setMessage("Вы уверены, что хотите выйти из аккаунта?")
                // сбрасываем всю инфу о пользователе и делаем USER_EXITED_ACCOUNT true. запускаем LoginActivity
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(USER_IS_LOGGED_IN, false);
                        editor.putString(USER_NAME, "");
                        editor.putString(USER_EMAIL, "");
                        editor.putString(USER_PASSWORD, "");
                        editor.putBoolean(USER_EXITED_ACCOUNT, true);
                        editor.apply();
                        Intent exitIntent = new Intent(currentContext, LoginActivity.class);
                        startActivity(exitIntent);
                    }
                })
                .setNegativeButton("Нет", null)
                .show();
    }

    public void backToMain(View view) // при нажатии стрелочки назад в настройках
    {
        openedAdditional = false;
        finish();
    }

    public void backToSettings(View view) // при нажатии стрелочки назад в одном из дополнительных экранов
    {
        openedAdditional = false;
        setContentView(R.layout.settings);
    }

    @Override
    public void onBackPressed() // именно при нажатии кнопки "назад". проверяем, куда выходить
    {
        if (!openedAdditional)
            finish();
        else {
            openedAdditional = false;
            setContentView(R.layout.settings);
        }
    }

    public static boolean isValid(String email) // проверка, корректный ли email ввели (это тут уже не нужно, но тут была функция editEmail)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
