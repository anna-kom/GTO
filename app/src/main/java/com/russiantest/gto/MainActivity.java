package com.russiantest.gto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.lang.invoke.ConstantCallSite;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String TEST_NAME = "testName";

    private static DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isInternetAvailable())
            Toast.makeText(this, "Проверьте свое подключение к интернету", Toast.LENGTH_SHORT).show();

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

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
