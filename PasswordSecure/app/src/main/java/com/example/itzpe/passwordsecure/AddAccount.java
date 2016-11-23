package com.example.itzpe.passwordsecure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;

import java.lang.Object;

public class AddAccount extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        Spinner spinner = (Spinner) findViewById(R.id.categories);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.cats, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        category = parent.getItemAtPosition(pos).toString();

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void addAccount(View v) {
        String accntName = ""; String username = ""; String password = ""; String email = ""; String notes = "";
        EditText aN = (EditText) findViewById(R.id.accountName);
        EditText un = (EditText) findViewById(R.id.username);
        EditText pw = (EditText) findViewById(R.id.password);
        EditText em = (EditText) findViewById(R.id.email);
        EditText no = (EditText) findViewById(R.id.notes);

        StringBuilder entry = new StringBuilder();
        if (aN.getText().length() > 0) { accntName = aN.getText().toString(); }
        if (un.getText().length() > 0) { username = un.getText().toString();}
        if (pw.getText().length() > 0) { password = pw.getText().toString(); }
        if (em.getText().length() > 0) { email = em.getText().toString(); }
        if (no.getText().length() > 0) { notes = no.getText().toString(); }

        entry.append(accntName); entry.append("::");
        entry.append(category); entry.append("::");
        entry.append(username); entry.append("::");
        entry.append(password); entry.append("::");
        entry.append(email); entry.append("::");
        entry.append(notes);

        
        //String test = entry.toString();
        //Log.d("Test", test, new Throwable("X"));
    }
}
