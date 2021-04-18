package com.example.travelpack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    TextView registerLink;
    Button loginBtn;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerLink = findViewById(R.id.gotoRegister);
        loginBtn = findViewById(R.id.btnLogin);
        email = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputPassword);
        DB = new DBHelper(this);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, AddTripActivity.class);
//                startActivity(intent);

                String user_email = email.getText().toString();
                String user_pass = password.getText().toString();

                if (user_email.equals("") || user_pass.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please enter all the field", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkuserpass = DB.checkEmailPassword(user_email, user_pass);

                    if (checkuserpass == true) {
                        Toast.makeText(LoginActivity.this, "Sign in successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, AddTripActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}