package com.example.itzpe.passwordsecure;


import android.content.ContextWrapper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.Scanner;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.encryption.pbe.PBEByteEncryptor;
import org.w3c.dom.Text;


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

        TextView op = (TextView) findViewById(R.id.optionalMessage);
        TextView er = (TextView) findViewById(R.id.errorMessage);
        TextView success = (TextView) findViewById(R.id.success);
        TextView repeat = (TextView) findViewById(R.id.repeatName);

        op.setVisibility(View.VISIBLE);
        er.setVisibility(View.INVISIBLE);
        success.setVisibility(View.INVISIBLE);
        repeat.setVisibility(View.INVISIBLE);
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
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("seniorproj");
        encryptor.initialize();


        // setting all of the strings to write
        if (aN.getText().length() > 0) { accntName = aN.getText().toString(); }
        if (un.getText().length() > 0) { username = un.getText().toString(); }
        String passwordencrypted = "";
        if (pw.getText().length() > 0) {
            password = pw.getText().toString();
            passwordencrypted = encryptor.encrypt(password);
        }
        String emailencrypted = "";
        if (em.getText().length() > 0) {
            email = em.getText().toString();
            emailencrypted = encryptor.encrypt(email);
        }
        String notesencrypted = "";
        if (no.getText().length() > 0) {
            notes = no.getText().toString();
            notesencrypted = encryptor.encrypt(notes);
        }
        if (accntName.length() > 0 && password.length() > 0) {

            // putting all the strings into one big string
            entry.append(accntName); entry.append("::");
            entry.append(category); entry.append("::");
            entry.append(username); entry.append("::");
            entry.append(passwordencrypted); entry.append("::");
            entry.append(emailencrypted); entry.append("::");
            entry.append(notesencrypted);

            // write output
            String write = entry.toString();
            Log.d("Write", write, new Throwable("X"));

            // write to file
            String file_name = "accounts.txt";
            if (!checkForDuplicate(accntName, category)) {
                try {
                    Context context = getApplicationContext();
                    FileOutputStream fout = context.openFileOutput(file_name, MODE_APPEND);
                    fout.write(write.getBytes());
                    fout.write('\n');
                    fout.close();

                    TextView op = (TextView) findViewById(R.id.optionalMessage);
                    TextView er = (TextView) findViewById(R.id.errorMessage);
                    TextView success = (TextView) findViewById(R.id.success);

                    success.setVisibility(View.VISIBLE);
                    op.setVisibility(View.INVISIBLE);
                    er.setVisibility(View.INVISIBLE);

                }   catch (FileNotFoundException e)  {
                    e.printStackTrace();
                }   catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // duplicate account name
            else {
                TextView op = (TextView) findViewById(R.id.optionalMessage);
                //TextView er = (TextView) findViewById(R.id.errorMessage);
                TextView success = (TextView) findViewById(R.id.success);
                //TextView repeat = (TextView) findViewById(R.id.repeatName);

                op.setVisibility(View.VISIBLE);
                //er.setVisibility(View.INVISIBLE);
                success.setVisibility(View.INVISIBLE);
                //repeat.setVisibility(View.VISIBLE);
                Context ctx = getApplicationContext();
                Toast toast = Toast.makeText(ctx, "An account already exists with that name and category.", Toast.LENGTH_LONG);
                toast.show();

            }



            // read from file
            String temp = "";

            try {
                Context context = getApplicationContext();
                FileInputStream fin = context.openFileInput(file_name);
                BufferedReader r = new BufferedReader(new InputStreamReader(fin));
                int x;
                while ((x=fin.read()) != -1) {
                    temp = temp + Character.toString((char)x);
                }
                fin.close();

            }   catch (FileNotFoundException e) {
                e.printStackTrace();
            }   catch (IOException e) {
                e.printStackTrace();
            }

            StringBuilder test2 = new StringBuilder();
            //test2.append(password); test2.append("::");
            //test2.append(email); test2.append("::");
            //test2.append(notes); test2.append("::");

            //test2.append(file_name.length());



            //String blah = test2.toString();
            Log.d("Read", temp, new Throwable("X"));

        }
        else {
            //TextView optional = (TextView) findViewById(R.id.optionalMessage);
            //optional.setVisibility(View.INVISIBLE);

            //TextView error = (TextView) findViewById(R.id.errorMessage);
            //error.setVisibility(View.VISIBLE);
            Context ctx = getApplicationContext();
            Toast toast = Toast.makeText(ctx, "Must fill in Account Name, Category, and Password", Toast.LENGTH_LONG);
            toast.show();
        }






    }

    public void generateRandomPassword(View v) {

        // generates a random password using characters from the characters String
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
        String pwd = RandomStringUtils.random( 15, 0, 0, false, false, characters.toCharArray(), new SecureRandom() );

        EditText et = (EditText) findViewById(R.id.password);
        et.setText(pwd);
    }

    public boolean checkForDuplicate(String name, String category) {
        // read file and put it into wholestring
        String wholestring = "";
        try {
            Context context = getApplicationContext();
            FileInputStream fin = context.openFileInput("accounts.txt");
            BufferedReader r = new BufferedReader(new InputStreamReader(fin));
            int x;
            while ((x=fin.read()) != -1) {
                wholestring = wholestring + Character.toString((char)x);
            }
            fin.close();

        }   catch (FileNotFoundException e) {
            e.printStackTrace();
        }   catch (IOException e) {
            e.printStackTrace();
        }

        String[] lines = wholestring.split("\n");                                                   // splits the whole file into each individual account
        for (int i = 0; i < lines.length; i++) {
            String[] fields = lines[i].split("::");                                                 // splits each line into an array of different fields (account name, category, etc...)
            if (fields[0].equals(name) && fields[1].equals(category)) return true;                  // returns true if there is already an account made with the given account name in the given category
        }
        return false;
    }

}
