package com.example.itzpe.passwordsecure;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ChangePassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
    }

    public void changePassword(View v) {
        SharedPreferences settings = getSharedPreferences("Settings", 0);

        EditText oldPass = (EditText) findViewById(R.id.oldPass);
        EditText newPass1 = (EditText) findViewById(R.id.newPass1);
        EditText newPass2 = (EditText) findViewById(R.id.newPass2);

        int op = 0; int np1 = 0; int np2 = 0;

        // All entries must be 5 numbers
        if (oldPass.getText().length() == 5 && newPass1.getText().length() == 5 && newPass2.getText().length() == 5) {
            String old = oldPass.getText().toString(); op = Integer.parseInt(old);
            String new1 = newPass1.getText().toString(); np1 = Integer.parseInt(new1);
            String new2 = newPass2.getText().toString(); np2 = Integer.parseInt(new2);

            int oldpassword = settings.getInt("MainPass", 00000);

            // old password equals the old password stored in SharedPreferences and the new passwords match each other
            if (op == oldpassword && np1 == np2) {
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

    Dialog confirmPass(Activity a) {
        final Activity me = a;
        final MainActivity m = new MainActivity();
        final EditText answer1 = (EditText) findViewById(R.id.answer1);
        final EditText answer2 = (EditText) findViewById(R.id.answer2);
        final EditText answer3 = (EditText) findViewById(R.id.answer3);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CONFIRM");
        builder.setMessage("Are you sure you want to change your password?");
        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // set password

                SharedPreferences settings = getSharedPreferences("Settings", 0);
                int op = 0; int np1 = 0; int np2 = 0;

                EditText oldPass = (EditText) findViewById(R.id.oldPass);
                EditText newPass1 = (EditText) findViewById(R.id.newPass1);
                EditText newPass2 = (EditText) findViewById(R.id.newPass2);

                String old = oldPass.getText().toString(); op = Integer.parseInt(old);
                String new1 = newPass1.getText().toString(); np1 = Integer.parseInt(new1);
                String new2 = newPass2.getText().toString(); np2 = Integer.parseInt(new2);

                int oldpassword = settings.getInt("MainPass", 00000);

                TextView tv = (TextView) findViewById(R.id.errorMessage);
                tv.setVisibility(View.INVISIBLE);

                MainActivity m = new MainActivity();
                m.changePassword(me, np1);
                me.finish();

                Context ctx = getApplicationContext();
                Toast toast = Toast.makeText(ctx, "Successfully changed password.", Toast.LENGTH_LONG);
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
