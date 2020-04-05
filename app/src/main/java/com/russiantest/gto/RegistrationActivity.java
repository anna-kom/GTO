package com.russiantest.gto;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


public class RegistrationActivity extends AppCompatActivity {

    EditText surname;
    EditText name;
    EditText middleName;
    EditText email;
    EditText birthDate;
    EditText phone;
    EditText password;
    EditText organization;
    EditText city;
    EditText specialization;
    Calendar dateAndTime = Calendar.getInstance();
    /*Spinner citizenshipSpinner;
    Spinner activitySpinner;
    TextView university_text;
    TextView faculty_text;
    TextView specialization_text;
    TextView organization_text;
    TextView position_text;
    EditText university;
    EditText faculty;
    EditText specialization;
    EditText organization;
    EditText position;
    String[] plants = new String[]{
            "Выберите",
            "Гражданин РФ",
            "Иностранный гражданин"
    };
    String[] activities = new String[]{
            "Выберите",
            "Студент",
            "Работник"
    };
    */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        surname = findViewById(R.id.surname);
        name = findViewById(R.id.name);
        middleName = findViewById(R.id.middle_name);
        email = findViewById(R.id.email);
        birthDate = findViewById(R.id.birth_date);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        city = findViewById(R.id.city);
        specialization = findViewById(R.id.specialization);
        organization = findViewById(R.id.organization);
       /* citizenshipSpinner = findViewById(R.id.citizenship);
        activitySpinner = findViewById(R.id.activity);
        university_text = findViewById(R.id.university_text);
        faculty_text = findViewById(R.id.faculty_text);
        specialization_text = findViewById(R.id.specialization_text);
        organization_text = findViewById(R.id.organization_text);
        position_text = findViewById(R.id.position_text);
        university = findViewById(R.id.university);
        faculty = findViewById(R.id.faculty);
        specialization = findViewById(R.id.specialization);
        organization = findViewById(R.id.organization);
        position = findViewById(R.id.position);


        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner, plants);
        ArrayAdapter<String> actAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner, activities);
        cityAdapter.setDropDownViewResource(R.layout.custom_spinner);
        citizenshipSpinner.setAdapter(cityAdapter);

        actAdapter.setDropDownViewResource(R.layout.custom_spinner);
        activitySpinner.setAdapter(actAdapter);

        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                TextView studentsInputs[] = new TextView[]{university_text, university, faculty_text, faculty, specialization_text, specialization};
                TextView workersInputs[] = new TextView[]{organization_text, organization, position_text, position};
                if (pos == 1) {

                    setVisible(studentsInputs);
                    setInvisible(workersInputs);
                } else if (pos == 2) {
                    setVisible(workersInputs);
                    setInvisible(studentsInputs);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
*/
    }

    /* public void setVisible(TextView[] arrayList) {
         for (TextView el : arrayList)
             el.setVisibility(View.VISIBLE);
     }

     public void setInvisible(TextView[] arrayList) {
         for (TextView el : arrayList)
             el.setVisibility(View.INVISIBLE);
     }
 */
    public void register(View view) {// если всё введено переходим к главной акстивности

        Intent mainIntent = new Intent(this, MainActivity.class);
        if (validateForm())
            startActivity(mainIntent);
    }

    public boolean validateForm() { // проверяет все обязательные поля введены(возмодны более продвинутые проверки позже)
        String emailText = email.getText().toString();
        if (surname.getText().toString().isEmpty()) {
            Toast.makeText(this, "Введите фамилию", Toast.LENGTH_SHORT).show();
            return false;
        } else if (name.getText().toString().isEmpty()) {
            Toast.makeText(this, "Введите имя", Toast.LENGTH_SHORT).show();
            return false;
        } else if (emailText.isEmpty()) {
            Toast.makeText(this, "Введите почту", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!SettingsActivity.isValid(emailText)) {
            Toast.makeText(this, "Некорректный адрес", Toast.LENGTH_SHORT).show();
            return false;
        } else if (birthDate.getText().toString().isEmpty()) {
            Toast.makeText(this, "Введите дату рождения", Toast.LENGTH_SHORT).show();
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
        }
        /*else if (citizenshipSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Укажите гражданство", Toast.LENGTH_SHORT).show();
            return false;
        } else if (activitySpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Укажите род занятости", Toast.LENGTH_SHORT).show();
            return false;
        } else if (activitySpinner.getSelectedItemPosition() == 1) {
            if (university.getText().toString().isEmpty()) {
                Toast.makeText(this, "Введите университет", Toast.LENGTH_SHORT).show();
                return false;
            } else if (faculty.getText().toString().isEmpty()) {
                Toast.makeText(this, "Введите факультет", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (activitySpinner.getSelectedItemPosition() == 2)
            if (organization.getText().toString().isEmpty()) {
                Toast.makeText(this, "Введите название организации", Toast.LENGTH_SHORT).show();
                return false;
            }
            */

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
    }

    public void showAlert(View view) {
        final AlertDialog aboutDialog = new AlertDialog.Builder(
                RegistrationActivity.this).setMessage(R.string.agreement_full_text)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();

        aboutDialog.show();
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
