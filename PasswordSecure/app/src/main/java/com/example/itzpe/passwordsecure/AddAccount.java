package com.example.itzpe.passwordsecure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import java.security.SecureRandom;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.encryption.pbe.PBEByteEncryptor;





public class AddAccount extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        Spinner spinner = (Spinner) findViewById(R.id.categories);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.cats, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        category = parent.getItemAtPosition(pos).toString();

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void addAccount(View v) {
        String accntName = ""; String username = ""; String password = ""; String email = ""; String notes = "";
        EditText aN = (EditText) findViewById(R.id.accountName);
        EditText un = (EditText) findViewById(R.id.username);
        EditText pw = (EditText) findViewById(R.id.password);
        EditText em = (EditText) findViewById(R.id.email);
        EditText no = (EditText) findViewById(R.id.notes);

        StringBuilder entry = new StringBuilder();
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("seniorproj");
        encryptor.initialize();

        //AES aes = new AES();
        //aes.setKey("key");

        if (aN.getText().length() > 0) { accntName = aN.getText().toString(); }
        if (un.getText().length() > 0) { username = un.getText().toString(); }
        String passwordencrypted = "";
        if (pw.getText().length() > 0) {
            password = pw.getText().toString();
            //password = aes.encrypt(password); }
            passwordencrypted = encryptor.encrypt(password);
        }
        String emailencrypted = "";
        if (em.getText().length() > 0) {
            email = em.getText().toString();
            //email = aes.encrypt(email); }
            emailencrypted = encryptor.encrypt(email);
        }
        String notesencrypted = "";
        if (no.getText().length() > 0) {
            notes = no.getText().toString();
            //notes = aes.encrypt(notes);}
            notesencrypted = encryptor.encrypt(notes);
        }
        entry.append(accntName); entry.append("::");
        entry.append(category); entry.append("::");
        entry.append(username); entry.append("::");
        entry.append(passwordencrypted); entry.append("::");
        entry.append(emailencrypted); entry.append("::");
        entry.append(notesencrypted);
        
        String test = entry.toString();
        Log.d("Test", test, new Throwable("X"));


        password = encryptor.decrypt(passwordencrypted);
        email = encryptor.decrypt(emailencrypted);
        notes = encryptor.decrypt(notesencrypted);

        StringBuilder test2 = new StringBuilder();
        test2.append(password); test2.append("::");
        test2.append(email); test2.append("::");
        test2.append(notes); test2.append("::");

        String blah = test2.toString();
        Log.d("Test", blah, new Throwable("X"));

    }

    public void generateRandomPassword(View v) {

        // generates a random password using characters from the characters String
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
        String pwd = RandomStringUtils.random( 15, 0, 0, false, false, characters.toCharArray(), new SecureRandom() );

        EditText et = (EditText) findViewById(R.id.password);
        et.setText(pwd);
    }
}
