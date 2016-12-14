package com.example.itzpe.passwordsecure;

import android.content.Intent;
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
import java.util.Collections;
import java.util.LinkedList;
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
    ArrayAdapter<String> adapter2;
    private String accountSelected;
    private int index;
    private int cat;

    private StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();


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
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = position;
                String itemVal = (String) parent.getItemAtPosition(index);
                Log.d("Click", itemVal);
                callModify(itemVal);
            }
        });


    }

    public void onListItemClick(ListView l, View v, int position, long id) {

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        selectedCategory = parent.getItemAtPosition(pos).toString();
        cat = pos;

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
            String x = lines[i] + "a";
            String[] fields = x.split("::");
            fields[fields.length-1] = fields[fields.length-1].substring(0,fields[fields.length-1].length()-1);
            // decrypt
            if (fields.length == 6) {
                if (!fields[3].equals("")) { String password = encryptor.decrypt(fields[3]); }
                else { String password = ""; }
                if (!fields[4].equals("")) { String email = encryptor.decrypt(fields[4]); }
                else { String email = ""; }
                if (!fields[5].equals("")) { String notes = encryptor.decrypt(fields[5]); }
                else { String notes = ""; }
            }
            if (!(lines.length == 0)) lines[i] = fields[0] + " -- " + fields[1];
        }
        lines = alphabetize(lines);
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

            String storedCategoryLower = nameAndCat[1].toLowerCase();
            String selectedCategoryLower = selectedCategory.toLowerCase();

            // empty accntname
            if (accountName.equals("")) {
                if (storedCategoryLower.equals(selectedCategoryLower)) {
                    String push = nameAndCat[0] + " -- " + nameAndCat[1];
                    stack.push(push);
                }
            }
            // both accntname and category
            else {
                String storedAccountNameLower = nameAndCat[0].toLowerCase();
                String selectedAccountNameLower = accountName.toLowerCase();

                if (storedAccountNameLower.contains(selectedAccountNameLower) && storedCategoryLower.equals(selectedCategoryLower)) {
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
        results = alphabetize(results);

        //Log.d("Debug", results[0], new Throwable("X") );
        if (results.length == 0) {
            Context ctx = getApplicationContext();
            Toast toast = Toast.makeText(ctx, "No accounts found.", Toast.LENGTH_LONG);
            toast.show(); }


        ListView lv = (ListView) findViewById(R.id.results);
        lv.setAdapter(null);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(lv.getContext(), android.R.layout.simple_list_item_1, results);
        lv.setAdapter(adapter3);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = position;
                String itemVal = (String) parent.getItemAtPosition(index);
                Log.d("Click", itemVal);
                callModify(itemVal);
            }
        });



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

    public void callModify(String entry) {
        String[] info = findAccount(entry);

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("seniorproj");
        encryptor.initialize();

        if (!info[3].equals("")) { info[3] = encryptor.decrypt(info[3]); }
        if (!info[4].equals("")) { info[4] = encryptor.decrypt(info[4]); }
        if (!info[5].equals("")) { info[5] = encryptor.decrypt(info[5]); }

        Intent modify = new Intent(this, ModifyAccount.class);
        int i = 0;
        switch (info[1]) {
            case "General": i = 0; break;
            case "Entertainment": i = 1; break;
            case "Purchasing": i = 2; break;
            case "School": i = 3; break;
            case "Work": i = 4; break;
            case "Personal": i = 5; break;
            case "Gaming": i = 6; break;
            case "Other": i = 7; break;
        }
        modify.putExtra("AccountInfo", info);
        modify.putExtra("LineIndex", index);
        modify.putExtra("CategoryIndex", i);
        startActivity(modify);
        this.finish();

    }

    public String[] findAccount(String entry) {
        String[] aAndc = entry.split(" -- ");

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
            String x = lines[i] + "a";
            String[] fields = x.split("::");
            fields[fields.length-1] = fields[fields.length-1].substring(0,fields[fields.length-1].length()-1);
            if (aAndc[0].equals(fields[0]) && aAndc[1].equals(fields[1])) {
                index = i;
                return fields;
            }
        }

        String[] ret = {""};
        return ret;
    }



}
