package com.russiantest.gto;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
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
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class RegistrationActivity extends AppCompatActivity {

    EditText name, email, birthDate, password, organization, city, specialization;
    Calendar dateAndTime = Calendar.getInstance();
    String userBirthDate;
    CheckBox checkBox;
    Button button;

    boolean nChanged, eChanged, bChanged, pChanged, oChanged, cChanged;
    boolean checkBoxChecked;

    private static final String LOGIN_SHARED_PREFS = "loginSharedPrefs";
    private static final String LOGGED_IN = "userIsLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        birthDate = findViewById(R.id.birth_date);
        password = findViewById(R.id.password);
        city = findViewById(R.id.city);
        organization = findViewById(R.id.organization);
        specialization = findViewById(R.id.specialization);
        checkBox = findViewById(R.id.agreement);
        button = findViewById(R.id.register_button);

        birthDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    setDate(v);
                }
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!name.getText().toString().isEmpty())
                {
                    nChanged = true;
                    if (eChanged && bChanged && pChanged && oChanged && cChanged && checkBoxChecked)
                        makeButtonOrange();
                }
                else
                    makeButtonWhite();
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!email.getText().toString().isEmpty())
                {
                    eChanged = true;
                    if (nChanged && bChanged && pChanged && oChanged && cChanged && checkBoxChecked)
                        makeButtonOrange();
                }
                else
                    makeButtonWhite();
            }
        });
        birthDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!birthDate.getText().toString().isEmpty())
                {
                    bChanged = true;
                    if (eChanged && nChanged && pChanged && oChanged && cChanged && checkBoxChecked)
                        makeButtonOrange();
                }
                else
                    makeButtonWhite();
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!password.getText().toString().isEmpty())
                {
                    pChanged = true;
                    if (eChanged && bChanged && nChanged && oChanged && cChanged && checkBoxChecked)
                        makeButtonOrange();
                }
                else
                    makeButtonWhite();
            }
        });
        city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!city.getText().toString().isEmpty())
                {
                    cChanged = true;
                    if (eChanged && bChanged && pChanged && oChanged && nChanged && checkBoxChecked)
                        makeButtonOrange();
                }
                else
                    makeButtonWhite();
            }
        });
        organization.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!organization.getText().toString().isEmpty())
                {
                    oChanged = true;
                    if (eChanged && bChanged && pChanged && nChanged && cChanged && checkBoxChecked)
                        makeButtonOrange();
                }
                else
                    makeButtonWhite();
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    checkBoxChecked = true;
                    if (nChanged && eChanged && bChanged && pChanged && oChanged && cChanged)
                        makeButtonOrange();
                }
                else
                    makeButtonWhite();
            }
        });
    }

    public void register(View view) {// если всё введено переходим к главной активности
        Context currCnx = this;
        if (validateForm())
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date date = null;
            try {
                date = format.parse(userBirthDate);
                User user = new User(name.getText().toString(), email.getText().toString(),
                        password.getText().toString(), date, city.getText().toString(),
                        organization.getText().toString(), specialization.getText().toString());
                Gson gson = new Gson();
                String json = gson.toJson(user);
                JSONObject obj = null;
                try {
                    obj = new JSONObject(json);
                    String url = "https://gto.kohea.tel/api/auth/v1/user/register/";
                    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, obj,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    //Toast.makeText(currCnx, response.toString(), Toast.LENGTH_SHORT).show();
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
                            }
                            else
                            {
                                email.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                                TextView emailExists = findViewById(R.id.registration_email_exists);
                                emailExists.setVisibility(View.VISIBLE);
                                Toast.makeText(currCnx, "Данный email уже существует", Toast.LENGTH_SHORT).show();
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

                } catch (Throwable t) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean validateForm() { // проверяет все обязательные поля введены(возмодны более продвинутые проверки позже)
        String emailText = email.getText().toString();
        if (name.getText().toString().isEmpty()) {
            Toast.makeText(this, "Введите ФИО", Toast.LENGTH_SHORT).show();
            return false;
        } else if (emailText.isEmpty()) {
            Toast.makeText(this, "Введите email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValid(emailText)) {
            Toast.makeText(this, "Введите корректный email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (birthDate.getText().toString().isEmpty()) {
            Toast.makeText(this, "Введите дату рождения", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isCorrectYear(dateAndTime)) {
            Toast.makeText(this, "Выбранный год не соответствует требованиям", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.getText().toString().isEmpty()) {
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();
            return false;
        } else if (city.getText().toString().isEmpty()) {
            Toast.makeText(this, "Введите город", Toast.LENGTH_SHORT).show();
            return false;
        } else if (organization.getText().toString().isEmpty()) {
            Toast.makeText(this, "Введите название организации", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!checkBox.isChecked()) {
            Toast.makeText(this, "Подтвердите, что вы даете согласие на обработку персональных данных", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isInternetAvailable()) {
            Toast.makeText(this, "Проверьте свое подключение к интернету", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void setDate(View v) { //показывает диалог выбора даты
        new DatePickerDialog(RegistrationActivity.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() { // создание слушателя для события смены даты
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDate();
        }
    };

    private void setInitialDate() { //устанавливает дату рождения
        birthDate.setText(DateUtils.formatDateTime(RegistrationActivity.this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_YEAR));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            userBirthDate = format.format(dateAndTime.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }
        email.requestFocus();
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

    public void hasAnAccount(View view)
    {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
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

    private boolean isCorrectYear(Calendar calendar) {
        Calendar now = Calendar.getInstance();
        int currentYear = now.get(Calendar.YEAR);
        int chosenYear = calendar.get(Calendar.YEAR);
        if ((currentYear - chosenYear) > 100)
            return false;
        if ((currentYear - chosenYear) < 14)
            return false;
        return true;
    }
}
