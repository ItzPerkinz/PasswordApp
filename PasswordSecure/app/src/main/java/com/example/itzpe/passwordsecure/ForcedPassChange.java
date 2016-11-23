package com.example.itzpe.passwordsecure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.content.Intent;

public class ForcedPassChange extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forced_pass_change);
    }

    public void changePassword(View v) {
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
                m.changePassword(this, entry);
                String test = "";
                SharedPreferences settings = getSharedPreferences("Settings", 0);
                if (test.equals(settings.getString("Question1", ""))) {
                    Intent secQuest = new Intent(this, SetupSecurityQuestions.class);
                    startActivity(secQuest);
                    this.finish();
                }
                else {
                    Intent main2 = new Intent(this, Main2Activity.class);
                    startActivity(main2);
                    this.finish();
                }
            }
        }

    }
}


