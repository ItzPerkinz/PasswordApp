package com.example.itzpe.passwordsecure;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ModifyAccount extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button doneButton;
    private Button deleteButton;
    private String[] accountInformation;                                                             // the account information
    private int line;                                                                                // the line in the file that needs to be modified or deleted
    private String categoryString = "";
    private int categoryIndex;
    private ArrayAdapter adapter;
    private int tempCat;

    private EditText accountName;
    private Spinner category;
    private EditText username;
    private EditText password;
    private EditText email;
    private EditText notes;


    private StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_account);

        doneButton = (Button) findViewById(R.id.doneButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.cats, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        accountName = (EditText) findViewById(R.id.accountName);
        category = (Spinner) findViewById(R.id.spinner2);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        notes = (EditText) findViewById(R.id.notes);

        setAccountInformationAndIndex();
        encryptor.setPassword("seniorproj");
        encryptor.initialize();

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        categoryString = parent.getItemAtPosition(pos).toString();
        tempCat = pos;

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void editInfo(View v) {

        doneButton.setClickable(true);
        doneButton.setVisibility(View.VISIBLE);

        deleteButton.setClickable(false);
        deleteButton.setVisibility(View.INVISIBLE);

        accountName.setEnabled(true);
        category.setEnabled(true);
        username.setEnabled(true);
        password.setEnabled(true);
        email.setEnabled(true);
        notes.setEnabled(true);

        Log.d("EditCalled", "The edit info method has been called");
    }

    public void deleteAccount(View v) {
        Log.d("DeleteCalled", "The delete account method has been called");
        Dialog d = confirm(this);
        d.show();



    }

    public void callDone(View v) {
        if (!checkForDuplicate(accountName.getText().toString(), categoryString, line)) { Dialog d = confirmEdit();
            d.show(); }
        // duplicate account name
        else {
            Context ctx = getApplicationContext();
            Toast toast = Toast.makeText(ctx, "An account already exists with that name and category.", Toast.LENGTH_LONG);
            toast.show();

        }

        Log.d("DoneCalled", "The done method has been called");
    }

    public Dialog confirm(Activity a) {
        boolean close = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CONFIRM");
        builder.setMessage("Are you sure you want to delete this account?");
        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // delete
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
                    finish();

                }   catch (FileNotFoundException e) {
                    e.printStackTrace();
                }   catch (IOException e) {
                    e.printStackTrace();
                }

                String[] lines = wholestring.split("\n");                                                   // splits the whole file into each individual account
                String[] deleted = new String[lines.length-1];
                lines[line] = lines[lines.length-1];
                for (int i = 0; i < deleted.length; i++) {
                    deleted[i] = lines[i];
                }
                deleted = alphabetize(deleted);

                try {
                    Context context = getApplicationContext();
                    FileOutputStream fout = context.openFileOutput("accounts.txt", MODE_PRIVATE);
                    for (int i = 0; i < deleted.length; i++) {
                        fout.write(deleted[i].getBytes());
                        fout.write('\n');
                    }
                    fout.close();
                    Toast t = Toast.makeText(context, "Account successfully deleted.", Toast.LENGTH_LONG);
                    t.show();



                }   catch (FileNotFoundException e)  {
                    e.printStackTrace();
                }   catch (IOException e) {
                    e.printStackTrace();
                }


            }


        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // cancelled
            }
        });

        return builder.create();
    }

    public Dialog confirmEdit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CONFIRM");
        builder.setMessage("Are you sure you want these changes?");
        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // edit
                doneButton.setClickable(false);
                doneButton.setVisibility(View.INVISIBLE);

                deleteButton.setClickable(true);
                deleteButton.setVisibility(View.VISIBLE);

                accountName.setEnabled(false);
                category.setEnabled(false);
                username.setEnabled(false);
                password.setEnabled(false);
                email.setEnabled(false);
                notes.setEnabled(false);

                accountInformation[0] = accountName.getText().toString();
                accountInformation[1] = categoryString;
                accountInformation[2] = username.getText().toString();
                accountInformation[3] = password.getText().toString();
                accountInformation[4] = email.getText().toString();
                accountInformation[5] = notes.getText().toString();
                categoryIndex = tempCat;

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
                StringBuilder sb = new StringBuilder();
                sb.append(accountInformation[0]); sb.append("::");
                sb.append(accountInformation[1]); sb.append("::");
                sb.append(accountInformation[2]); sb.append("::");
                if (!accountInformation[3].equals("")) { sb.append(encryptor.encrypt(accountInformation[3])); sb.append("::"); }
                else { sb.append("::");}
                if (!accountInformation[4].equals("")) { sb.append(encryptor.encrypt(accountInformation[4])); sb.append("::"); }
                else { sb.append("::"); }
                if (!accountInformation[5].equals("")) { sb.append(encryptor.encrypt(accountInformation[5])); }
                else { sb.append(""); }
                String newLine = sb.toString();
                lines[line] = newLine;

                try {
                    Context context = getApplicationContext();
                    FileOutputStream fout = context.openFileOutput("accounts.txt", MODE_PRIVATE);
                    for (int i = 0; i < lines.length; i++) {
                        fout.write(lines[i].getBytes());
                        fout.write('\n');
                    }
                    fout.close();

                    }   catch (FileNotFoundException e)  {
                        e.printStackTrace();
                    }   catch (IOException e) {
                        e.printStackTrace();
                    }


            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // cancelled
                accountName.setText(accountInformation[0]);
                category.setSelection(categoryIndex);
                username.setText(accountInformation[2]);
                password.setText(accountInformation[3]);
                email.setText(accountInformation[4]);
                notes.setText(accountInformation[5]);

                doneButton.setClickable(false);
                doneButton.setVisibility(View.INVISIBLE);

                deleteButton.setClickable(true);
                deleteButton.setVisibility(View.VISIBLE);

                accountName.setEnabled(false);
                category.setEnabled(false);
                username.setEnabled(false);
                password.setEnabled(false);
                email.setEnabled(false);
                notes.setEnabled(false);
            }
        });

        return builder.create();
    }

    public void setAccountInformationAndIndex() {

        accountInformation = getIntent().getStringArrayExtra("AccountInfo");
        line = getIntent().getIntExtra("LineIndex", 0);
        categoryIndex = getIntent().getIntExtra("CategoryIndex", 0);


        accountName.setText(accountInformation[0]); accountName.setEnabled(false);
        category.setSelection(categoryIndex); category.setEnabled(false);
        username.setText(accountInformation[2]); username.setEnabled(false);
        password.setText(accountInformation[3]); password.setEnabled(false);
        email.setText(accountInformation[4]); email.setEnabled(false);
        notes.setText(accountInformation[5]); notes.setEnabled(false);


    }

    public boolean checkForDuplicate(String name, String category, int line) {
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
            if (fields[0].equals(name) && fields[1].equals(category) && i != line) return true;     // returns true if there is already an account made with the given account name in the given category
        }
        return false;
    }

    public String[] alphabetize(String[] strings) {

        List<String> temp = new LinkedList<String>();
        for (int i = 0; i < strings.length; i++) {
            temp.add(strings[i]);
        }
        Collections.sort(temp, String.CASE_INSENSITIVE_ORDER);
        int x = temp.size();
        for (int i = 0; i < x; i++) {
            strings[i] = temp.remove(0);
        }
        return strings;
    }
}
