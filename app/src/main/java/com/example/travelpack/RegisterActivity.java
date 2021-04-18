package com.example.travelpack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText name, email, password;
    TextView loginLink;
    Button signupBtn;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginLink = findViewById(R.id.gotoLogin);
        signupBtn = findViewById(R.id.btnSignup);
        name = findViewById(R.id.inputName);
        email = findViewById(R.id.inputEmailAddr);
        password = findViewById(R.id.inputPasswd);
        DB = new DBHelper(this);

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                startActivity(intent);

                String user_email = email.getText().toString();
                String user_password = password.getText().toString();
                String user_name = name.getText().toString();

                if (user_email.equals("") || user_password.equals("") || user_name.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkuser = DB.checkEmail(user_email);

                    if (checkuser == false) {
                        Boolean insert = DB.insertData(user_email, user_name, user_password);

                        if (insert == true) {
                            Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "User already exists! Please sign in.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}