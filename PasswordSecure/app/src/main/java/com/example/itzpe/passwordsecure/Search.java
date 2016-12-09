package com.example.itzpe.passwordsecure;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Stack;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.encryption.pbe.PBEByteEncryptor;
import org.w3c.dom.Text;




public class Search extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String selectedCategory = "";
    StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    ArrayAdapter<String> adapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        encryptor.setPassword("seniorproj");
        encryptor.initialize();


        Spinner spinner = (Spinner) findViewById(R.id.categories);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.cats, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        String[] results = setupList();

        StringBuilder sb = new StringBuilder();
        sb.append(results.length);

        Log.d("Results", sb.toString(), new Throwable("X"));
        ListView lv = (ListView) findViewById(R.id.results);
        adapter2 = new ArrayAdapter<String>(lv.getContext(), android.R.layout.simple_list_item_1, results);
        lv.setAdapter(adapter2);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        selectedCategory = parent.getItemAtPosition(pos).toString();

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public String[] setupList() {
        String wholestring = "";
        // fill wholestring
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
        String[] lines = {};
        if (!wholestring.equals("")) lines = wholestring.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String[] fields = lines[i].split("::");
            // decrypt
            if (fields.length == 6) {
                String password = encryptor.decrypt(fields[3]);
                String email = encryptor.decrypt(fields[4]);
                String notes = encryptor.decrypt(fields[5]);
            }
            if (!(lines.length == 0)) lines[i] = fields[0] + " -- " + fields[1];
        }
        return lines;

    }

    public void narrowSearch(View v) {
        EditText et = (EditText) findViewById(R.id.accountName);
        String accountName = "";
        if (et.getText().length() > 0) accountName = et.getText().toString();


        String[] total = setupList();
        Stack<String> stack = new Stack<String>();

        for (int i = 0; i < total.length; i++) {
            String[] nameAndCat = total[i].split(" -- ");
            // empty accntname
            if (accountName.equals("")) {
                if (nameAndCat[1].equals(selectedCategory)) {
                    String push = nameAndCat[0] + " -- " + nameAndCat[1];
                    stack.push(push);
                }
            }
            // both accntname and category
            else {
                if (nameAndCat[0].equals(accountName) && nameAndCat[1].equals(selectedCategory)) {
                    String push = nameAndCat[0] + " -- " + nameAndCat[1];
                    stack.push(push);
                }
            }
        }
        String[] results = new String[stack.size()];
        int counter = 0;
        while (!stack.empty()) {
            results[counter] = stack.pop();
            counter++;
        }

        //Log.d("Debug", results[0], new Throwable("X") );
        if (results.length == 0) {
            Context ctx = getApplicationContext();
            Toast toast = Toast.makeText(ctx, "No accounts found.", 3);
            toast.show(); }


        ListView lv = (ListView) findViewById(R.id.results);
        lv.setAdapter(null);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(lv.getContext(), android.R.layout.simple_list_item_1, results);
        lv.setAdapter(adapter3);


    }

}
