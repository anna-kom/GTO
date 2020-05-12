package com.russiantest.gto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    private static final String LOGIN_SHARED_PREFS = "loginSharedPrefs";
    private static final String LOGGED_IN = "userIsLoggedIn";
    private static final String IS_ADMIN = "userIsAnAdmin";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_panel);
    }

    public void createTest(View view) {
        Intent formQuestionIntent = new Intent(this, BuildingTestActivity.class);
        formQuestionIntent.putExtra("newTest", true);
        startActivity(formQuestionIntent);
    }

    public void viewTests(View view)
    {
        Intent viewIntent = new Intent(this, ViewTestsActivity.class);
        startActivity(viewIntent);
    }

    public void exitAdminAccount(View view)
    {
        final Context currentContext = this;
        new AlertDialog.Builder(this)
                .setMessage("Вы уверены, что хотите выйти из аккаунта администратора?")
                // сбрасываем всю инфу о пользователе и делаем USER_EXITED_ACCOUNT true. запускаем LoginActivity
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
                        sharedPreferences.edit().putBoolean(LOGGED_IN, false).apply();
                        sharedPreferences.edit().putBoolean(IS_ADMIN, false).apply();
                        Intent exitIntent = new Intent(currentContext, LoginActivity.class);
                        startActivity(exitIntent);
                    }
                })
                .setNegativeButton("Нет", null)
                .show();
    }

    @Override
    public void onBackPressed()
    {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}
