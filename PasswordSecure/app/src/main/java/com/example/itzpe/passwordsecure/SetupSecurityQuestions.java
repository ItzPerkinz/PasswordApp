package com.example.itzpe.passwordsecure;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

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
            Dialog d = confirmQuest(this);
            d.show();

        }
        else {
            TextView tv = (TextView) findViewById(R.id.errorMessage);
            tv.setVisibility(View.VISIBLE);
        }
    }

    public Dialog confirmQuest(Activity a) {
        final Activity me = a;
        final MainActivity m = new MainActivity();
        final EditText answer1 = (EditText) findViewById(R.id.answer1);
        final EditText answer2 = (EditText) findViewById(R.id.answer2);
        final EditText answer3 = (EditText) findViewById(R.id.answer3);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CONFIRM");
        builder.setMessage("Are you sure you want these to be your security questions?");
        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // set questions
                TextView tv = (TextView) findViewById(R.id.errorMessage);
                tv.setVisibility(View.INVISIBLE);

                String x = answer1.getText().toString();
                String y = answer2.getText().toString();
                String z = answer3.getText().toString();
                m.changeSecurityQuestions(me, x, y, z);

                Intent main2 = new Intent(me, Main2Activity.class);
                startActivity(main2);
                me.finish();
                Context ctx = getApplicationContext();
                Toast toast = Toast.makeText(ctx, "Successfully set up account.", Toast.LENGTH_SHORT);
                toast.show();


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
