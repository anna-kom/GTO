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

    private static final String LOGIN_SHARED_PREFS = "loginSharedPrefs";
    private static final String LOGGED_IN = "userIsLoggedIn";

    private static boolean openedAdditional; // чтобы запоминать, нажал пользователь на какую-то из конопок в настройках или нет (то есть откыт ли экран настройки имени или типа того) - нужно чтобы корректно выходить отсюда

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        openedAdditional = false;
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
                        SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
                        sharedPreferences.edit().putBoolean(LOGGED_IN, false).apply();
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
}
