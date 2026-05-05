package com.example.servicerequestapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.*;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    EditText email, password, name;
    Button registerBtn;
    TextView goLoginTop;

    String BASE_URL = "https://ipt-final-project.onrender.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        registerBtn = findViewById(R.id.registerBtn);
        goLoginTop = findViewById(R.id.goLoginTop);

        goLoginTop.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            overridePendingTransition(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
            );
            finish();
        });

        registerBtn.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {

        String n = name.getText().toString().trim();
        String e = email.getText().toString().trim();
        String p = password.getText().toString().trim();

        if (n.isEmpty() || e.isEmpty() || p.isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject json = new JSONObject();

        try {
            json.put("name", n);
            json.put("email", e);
            json.put("password", p);
        } catch (Exception ignored) {}

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL + "register",
                json,
                r -> {
                    Toast.makeText(this, "Registered!", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(this, LoginActivity.class));

                    overridePendingTransition(
                            android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right
                    );

                    finish();
                },
                error -> {
                    Toast.makeText(this, "Register Failed", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
        ));
    }
}