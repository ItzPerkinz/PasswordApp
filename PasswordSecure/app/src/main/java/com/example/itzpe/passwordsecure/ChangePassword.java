package com.example.itzpe.passwordsecure;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.EditText;


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
                MainActivity m = new MainActivity();
                m.changePassword(this, np1);
                this.finish();
            }
        }

    }
}
