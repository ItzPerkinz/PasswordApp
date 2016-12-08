package com.example.itzpe.passwordsecure;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {

    public static final String preferences = "Settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences settings = getSharedPreferences(preferences, 0);


        //SharedPreferences.Editor ed = settings.edit();
        //ed.clear();
        //ed.commit();


        if (settings.getInt("MainPass", 00000) != 00000) {
            CheckedTextView fi = (CheckedTextView) findViewById(R.id.firstInstructions);
            fi.setVisibility(View.INVISIBLE);
        }





    }

    public void forgotPassword(View v) {
        Intent forgotpass = new Intent(this, ForgotPassword.class);
        startActivity(forgotpass);
        this.finish();
    }

    public void loginButtonOnClick(View v) {
        EditText passwordText = (EditText) findViewById(R.id.passwordText);
        int entry = 0;
        SharedPreferences settings = getSharedPreferences(preferences, 0);
        if (passwordText.getText().length() != 0 && passwordText.getText().length() == 5) {
            String value = passwordText.getText().toString();
            entry = Integer.parseInt(value);

            if (entry == settings.getInt("MainPass", 00000)) {
                TextView tv = (TextView) findViewById(R.id.errorMessage);
                tv.setVisibility(View.INVISIBLE);
                // 1st time use -- force password change
                if (entry == 00000) {
                    Intent fpass = new Intent(this, ForcedPassChange.class);
                    startActivity(fpass);
                    this.finish();
                }
                else if (settings.getString("Question1", "").equals("")) {
                    Intent seqQuest = new Intent(this, SetupSecurityQuestions.class);
                    startActivity(seqQuest);
                    this.finish();
                }
                // normal use
                else {
                    Intent main2 = new Intent(this, Main2Activity.class);
                    startActivity(main2);
                }
            }
            else {
                TextView tv = (TextView) findViewById(R.id.errorMessage);
                tv.setVisibility(View.VISIBLE);
            }
        }

        else {
            TextView tv = (TextView) findViewById(R.id.firstInstructions);
            tv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Changes the main password to login to the app.
    public void changePassword(Context ctx, int newPass)
    {
        /* this.password = newPass; */
        SharedPreferences settings = ctx.getSharedPreferences(preferences,0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("MainPass", newPass);
        editor.apply();
    }

    public void changeSecurityQuestions(Context ctx, String s1, String s2, String s3) {
        SharedPreferences settings = ctx.getSharedPreferences(preferences, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Question1", s1);
        editor.putString("Question2", s2);
        editor.putString("Question3", s3);
        editor.apply();
    }

}
