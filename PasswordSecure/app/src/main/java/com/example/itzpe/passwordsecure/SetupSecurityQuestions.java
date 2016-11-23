package com.example.itzpe.passwordsecure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SetupSecurityQuestions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_security_questions);
        //TextView tv = (TextView) findViewById(R.id.errorMessage);
        //tv.setVisibility(View.INVISIBLE);
    }


    public void ConfirmSecurityQuestions(View v) {
        MainActivity m = new MainActivity();
        EditText answer1 = (EditText) findViewById(R.id.answer1);
        EditText answer2 = (EditText) findViewById(R.id.answer2);
        EditText answer3 = (EditText) findViewById(R.id.answer3);

        String entry = "";
        if (answer1.getText().length() > 0 && answer2.getText().length() > 0 && answer3.getText().length() > 0) {

            TextView tv = (TextView) findViewById(R.id.errorMessage);
            tv.setVisibility(View.INVISIBLE);

            String x = answer1.getText().toString();
            String y = answer2.getText().toString();
            String z = answer3.getText().toString();
            m.changeSecurityQuestions(this, x, y, z);

            Intent main2 = new Intent(this, Main2Activity.class);
            startActivity(main2);
            this.finish();
        }
        else {
            TextView tv = (TextView) findViewById(R.id.errorMessage);
            tv.setVisibility(View.VISIBLE);
        }
    }
}
