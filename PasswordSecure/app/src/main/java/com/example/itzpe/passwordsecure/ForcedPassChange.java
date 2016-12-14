package com.example.itzpe.passwordsecure;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ForcedPassChange extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forced_pass_change);
        TextView tv = (TextView) findViewById(R.id.errorMessage);
        tv.setVisibility(View.INVISIBLE);
    }

    public void changePassword(View v) {
        EditText passwordText = (EditText) findViewById(R.id.newPass);
        EditText confirmText = (EditText) findViewById(R.id.confirm);
        int entry = 0;
        int entry2 = 1;

        if (passwordText.getText().length() == 5 && confirmText.getText().length() == 5 ) {
            String value = passwordText.getText().toString();
            entry = Integer.parseInt(value);
            String value2 = confirmText.getText().toString();
            entry2 = Integer.parseInt(value2);

            if (entry == entry2) {
                Dialog d = confirmPass(this);
                d.show();
            }
            else {
                TextView tv = (TextView) findViewById(R.id.errorMessage);
                tv.setVisibility(View.VISIBLE);
            }
        }
        else {
            TextView tv = (TextView) findViewById(R.id.errorMessage);
            tv.setVisibility(View.VISIBLE);
        }



    }

    public Dialog confirmPass(Activity a) {
        final Activity me = a;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CONFIRM");
        builder.setMessage("Are you sure you want this to be your main password?");
        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // set password

                MainActivity m = new MainActivity();
                EditText passwordText = (EditText) findViewById(R.id.newPass);
                EditText confirmText = (EditText) findViewById(R.id.confirm);
                int entry = 0;
                int entry2 = 1;

                if (passwordText.getText().length() == 5 && confirmText.getText().length() == 5 ) {
                    String value = passwordText.getText().toString();
                    entry = Integer.parseInt(value);
                    String value2 = confirmText.getText().toString();
                    entry2 = Integer.parseInt(value2);

                    if (entry == entry2) {
                        TextView tv = (TextView) findViewById(R.id.errorMessage);
                        tv.setVisibility(View.INVISIBLE);
                        m.changePassword(me, entry);
                        String test = "";
                        SharedPreferences settings = getSharedPreferences("Settings", 0);
                        if (test.equals(settings.getString("Question1", ""))) {
                            Intent secQuest = new Intent(me, SetupSecurityQuestions.class);
                            startActivity(secQuest);
                            me.finish();
                        }
                        else {
                            Intent main2 = new Intent(me, Main2Activity.class);
                            startActivity(main2);
                            me.finish();
                        }
                    }
                    else {
                        TextView tv = (TextView) findViewById(R.id.errorMessage);
                        tv.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    TextView tv = (TextView) findViewById(R.id.errorMessage);
                    tv.setVisibility(View.VISIBLE);
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
}


