package com.russiantest.gto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static final String LOGIN_SHARED_PREFS = "loginSharedPrefs";
    private static final String LOGGED_IN = "userIsLoggedIn";
    private static final String IS_ADMIN = "userIsAnAdmin";
    private Context currCnx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
        boolean loggedIn = sharedPreferences.getBoolean(LOGGED_IN, false);
        if (loggedIn)
        {
            boolean isAdmin = sharedPreferences.getBoolean(IS_ADMIN, false);
            if (isAdmin)
            {
                Intent adminIntent = new Intent(this, AdminActivity.class);
                startActivity(adminIntent);
            }
            else {
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
            }
        }

        setContentView(R.layout.login_activity);
        currCnx = this;

    }

    public void login(View view) // при нажатии на кнопку зарегистрироваться или войти (пока одно и то же)
    {
        EditText emailEditText = findViewById(R.id.login_email);
        String email = emailEditText.getText().toString();
        EditText passwordEditText = findViewById(R.id.login_password);
        String password = passwordEditText.getText().toString();

        // проверяем, можем ли мы его зарегистрировать с такими данными
        if (email.isEmpty())
            Toast.makeText(this, "Введите email", Toast.LENGTH_SHORT).show();
        else if (password.isEmpty())
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();
        else if (Objects.equals(email, "admin") && Objects.equals(password, "admin"))
        {
            SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(LOGGED_IN, true).apply();
            sharedPreferences.edit().putBoolean(IS_ADMIN, true).apply();
            Intent adminIntent = new Intent(this, AdminActivity.class);
            startActivity(adminIntent);
        }
        else if (!isValid(email))
            Toast.makeText(this, "Введите корректный email", Toast.LENGTH_SHORT).show();
        else if (!isInternetAvailable())
            Toast.makeText(this, "Проверьте свое подключение к интернету", Toast.LENGTH_SHORT).show();
        else {
            currCnx = this; JSONObject obj = null;
            String json = "{\n" +
                    "  \"email\": \"" + email + "\",\n" +
                    "  \"password\": \"" + password + "\"\n" +
                    "}";

            try {
                obj = new JSONObject(json);
                Log.d("My App", obj.toString());

            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
            }

            String url = "https://gto.kohea.tel/api/auth/v1/user/authenticate/";
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(currCnx, response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    if (error instanceof ParseError)
                    {
                        SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
                        sharedPreferences.edit().putBoolean(LOGGED_IN, true).apply();
                        Intent mainIntent = new Intent(currCnx, MainActivity.class);
                        startActivity(mainIntent);
                        //Toast.makeText(currCnx, "Auth not successful", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        emailEditText.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                        passwordEditText.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                        TextView wrongEmail = findViewById(R.id.login_wrong_email);
                        wrongEmail.setVisibility(View.VISIBLE);
                        TextView wrongPassword = findViewById(R.id.login_wrong_password);
                        wrongPassword.setVisibility(View.VISIBLE);
                        //Toast.makeText(currCnx, "Auth successful", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json-patch+json");
                    headers.put("accept", "text/plain");
                    return headers;
                }
            };
            Volley.newRequestQueue(this).add(postRequest);
        }
    }


    public void no_account(View view) // при нажатии на кнопку "нет аккаунта"
    {
        Intent registerIntent = new Intent(this, RegistrationActivity.class);
        startActivity(registerIntent);
    }

    @Override
    public void onBackPressed() // чтобы при нажатии кнопки "назад" мы выходили из приложения и не зацикливались
    {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
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
