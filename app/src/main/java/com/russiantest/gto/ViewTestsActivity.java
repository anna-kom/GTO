package com.russiantest.gto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ViewTestsActivity extends AppCompatActivity {

    private static DatabaseReference databaseReference;
    private static String testName;
    private static AlertDialog questionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_tests);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        LinearLayout layout = findViewById(R.id.view_linear_layout);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    View test = getLayoutInflater().inflate(R.layout.test_name_admin, null);
                    TextView text = test.findViewById(R.id.test_admin_name_text);
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

    public void viewTest(View view)
    {
        TextView textNameText = view.findViewById(R.id.test_admin_name_text);
        testName = textNameText.getText().toString();
        questionDialog = new AlertDialog.Builder(this).setView(R.layout.admin_test_options).create();
        questionDialog.show();
    }

    public void adminTestOptions(View view) {
        switch (view.getId()) {
            case R.id.admin_test_options_change:
                Intent testIntent = new Intent(this, TestAdminActivity.class);
                testIntent.putExtra(MainActivity.TEST_NAME, testName);
                startActivity(testIntent);
                break;
            case R.id.admin_test_options_delete:
                final Context currentContext = this;
                new AlertDialog.Builder(this)
                        .setMessage("Вы уверены, что хотите удалить тест?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                questionDialog.dismiss();
                                if (Objects.equals(testName, "Основной тест"))
                                    Toast.makeText(currentContext, "Не удаляйте основной тест, пожалуйста", Toast.LENGTH_SHORT).show();
                                else {
                                    databaseReference.child(testName).removeValue();
                                    Toast.makeText(currentContext, "Вы удалили " + testName, Toast.LENGTH_SHORT).show();
                                }
                                recreate();
                            }
                        })
                        .setNegativeButton("Нет", null)
                        .show();
                break;
        }
    }

    public void backToAdmin(View view)
    {
        Intent adminIntent = new Intent(this, AdminActivity.class);
        startActivity(adminIntent);
    }

    @Override
    public void onBackPressed() {
        Intent adminIntent = new Intent(this, AdminActivity.class);
        startActivity(adminIntent);
    }
}
