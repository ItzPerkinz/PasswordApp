package com.example.itzpe.passwordsecure;

import android.content.Context;
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

public class AllAccounts extends AppCompatActivity {

    private StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

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
            }
        });

    }

    public void onListItemClick(ListView l, View v, int position, long id) {

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

    public String[] alphabetize(String[] strings) {

        List<String> temp = new LinkedList<String>();
        for (int i = 0; i < strings.length; i++) {
            temp.add(strings[i]);
        }
        Collections.sort(temp, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < temp.size(); i++) {
            strings[i] = temp.remove(0);
        }
        return strings;
    }




}
