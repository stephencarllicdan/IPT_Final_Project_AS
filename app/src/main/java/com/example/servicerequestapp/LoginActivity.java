package com.example.servicerequestapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.*;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button loginBtn;
    TextView goRegisterTop;

    String BASE_URL = "https://ipt-final-project.onrender.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        goRegisterTop = findViewById(R.id.goRegisterTop);

        loginBtn.setOnClickListener(v -> loginUser());

        goRegisterTop.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));

            overridePendingTransition(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
            );
        });
    }

    private void loginUser() {

        String e = email.getText().toString().trim();
        String p = password.getText().toString().trim();

        if (e.isEmpty() || p.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject json = new JSONObject();

        try {
            json.put("email", e);
            json.put("password", p);
        } catch (Exception ignored) {}

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL + "login",
                json,
                response -> {
                    try {
                        if (response.getBoolean("success")) {

                            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(this, MainActivity.class));

                            overridePendingTransition(
                                    android.R.anim.fade_in,
                                    android.R.anim.fade_out
                            );

                            finish();

                        } else {
                            Toast.makeText(this, "Invalid login", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e1) {
                        Log.e("LOGIN_PARSE", e1.toString());
                    }
                },
                error -> {
                    Toast.makeText(this, "Server Error", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
        ));
    }
}