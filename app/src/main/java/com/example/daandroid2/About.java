package com.example.daandroid2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class About extends AppCompatActivity {
    Button btnCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar;
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        addControls();
        addEvents();
    }



    private void addControls() {
        btnCall=findViewById(R.id.btnCall);
    }
    private void addEvents() {
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "0357716720";

                if (phoneNumber.isEmpty()) {
                    Toast.makeText(About.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                } else {
                    // Kích hoạt Intent quay số
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phoneNumber));
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= new MenuInflater(this);
        inflater.inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.styleBar)
        {
            Intent intent= new Intent(About.this, StyleActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.allBookBar)
        {
            Intent intent= new Intent(About.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.aboutBar)
        {
            Intent intent= new Intent(About.this, About.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}