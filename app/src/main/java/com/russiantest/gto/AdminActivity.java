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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_panel);
    }

    public void createTest(View view) {
        Intent formQuestionIntent = new Intent(this, BuildingTestActivity.class);
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
                        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(MainActivity.USER_IS_LOGGED_IN, false);
                        editor.putString(MainActivity.USER_NAME, "");
                        editor.putString(MainActivity.USER_EMAIL, "");
                        editor.putString(MainActivity.USER_PASSWORD, "");
                        editor.putBoolean(MainActivity.USER_EXITED_ACCOUNT, true);
                        editor.apply();
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
