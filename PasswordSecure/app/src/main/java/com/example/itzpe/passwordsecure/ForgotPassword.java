package com.example.itzpe.passwordsecure;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;


public class ForgotPassword extends AppCompatActivity implements OnItemSelectedListener {

    int question = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.questions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        question = pos;
        StringBuilder sb = new StringBuilder();
        sb.append(pos);
        Log.d("debug", sb.toString(), new Throwable("x"));
    }

    public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
    }


    public void resetPassword(View v) {
        EditText ans = (EditText) findViewById(R.id.answer);
        SharedPreferences settings = getSharedPreferences("Settings", 0);
        if (ans.getText().length() != 0) {
            String value = ans.getText().toString();
            String answer = "";
            if (question == 0) { answer = settings.getString("Question1", ""); }
            else if (question == 1) { answer = settings.getString("Question2", ""); }
            else if (question == 2) { answer = settings.getString("Question3", ""); }
            if (value.equals(answer)) {
                Intent fpc = new Intent(this, ForcedPassChange.class);
                startActivity(fpc);
                this.finish();
            }
        }




    }


}
