package com.example.itzpe.passwordsecure;

import android.content.Context;
import android.content.Intent;
import android.icu.text.IDNA;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.AdapterView;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class AllAccounts extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    public int index;
    private int cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_accounts);

        encryptor.setPassword("seniorproj");
        encryptor.initialize();

        // add data for display
        String[] results = loadData();


        ListView elv = (ListView) findViewById(R.id.results);

        ListView lv = (ListView) findViewById(R.id.results);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(lv.getContext(), android.R.layout.simple_list_item_1, results);
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
        cat = pos;

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
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
        modify.putExtra("AccountInfo", info);
        modify.putExtra("LineIndex", index);
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
        modify.putExtra("CategoryIndex", i);
        startActivity(modify);
        this.finish();

    }

    public String[] loadData() {
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
        Log.d("WholeStringRead", wholestring);
        String[] lines = {};
        if (!wholestring.equals("")) lines = wholestring.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String x = lines[i] + "a";
            String[] fields = x.split("::");
            fields[fields.length-1] = fields[fields.length-1].substring(0,fields[fields.length-1].length()-1);
            Log.d("Line", lines[i]);
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

    public String[] findAccount(String entry) {
        String[] aAndc = entry.split(" -- ");
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("seniorproj");
        //encryptor.setAlgorithm("AES");
        encryptor.initialize();

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
