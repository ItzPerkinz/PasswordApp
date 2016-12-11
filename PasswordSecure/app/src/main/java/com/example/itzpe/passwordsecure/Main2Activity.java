package com.example.itzpe.passwordsecure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

    }

    // search for an account to view/modify
    public void search(View v) {
        Intent search = new Intent(this, Search.class);
        startActivity(search);
    }

    // change the app's password
    public void changePassword(View v) {
        Intent cP = new Intent(this, ChangePassword.class);
        startActivity(cP);
    }

    // add account
    public void manageAccounts(View v) {
        Intent add = new Intent(this, AddAccount.class);
        startActivity(add);
    }

    // manage your favorite passwords
    public void displayAllAccounts(View v) {
        Intent allAcc = new Intent(this, AllAccounts.class);
        startActivity(allAcc);
    }

}
