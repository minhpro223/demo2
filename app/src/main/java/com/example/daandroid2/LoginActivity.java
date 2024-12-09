package com.example.daandroid2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText EtUser=findViewById(R.id.etUser);
        EditText EtPass=findViewById(R.id.etPassword);
        Button btnLogin=findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v->{
            String username= EtUser.getText().toString().trim();
            String password= EtPass.getText().toString().trim();

            if(username.equals("admin")&& password.equals("admin")){
                Toast.makeText(this,"Login Succesful",Toast.LENGTH_SHORT).show();
                //Chuyen Sang Mainacitvity
                Intent intent=new Intent(LoginActivity.this, StyleActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(this,"Wrong",Toast.LENGTH_SHORT).show();
            }

        });
    }

}