package com.example.servicerequestapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class DashboardActivity extends AppCompatActivity {

    TextView totalTxt, pendingTxt, doneTxt;
    Button backBtn, logoutBtn;

    String BASE_URL = "https://ipt-final-project.onrender.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        totalTxt = findViewById(R.id.totalTxt);
        pendingTxt = findViewById(R.id.pendingTxt);
        doneTxt = findViewById(R.id.doneTxt);
        backBtn = findViewById(R.id.backBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        loadDashboard();

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        // ✅ LOGOUT FIX
        logoutBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadDashboard() {

        String url = BASE_URL + "dashboard";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                com.android.volley.Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        int total = response.optInt("total", 0);
                        int pending = response.optInt("pending", 0);
                        int done = response.optInt("done", 0);

                        totalTxt.setText("Total: " + total);
                        pendingTxt.setText("Pending: " + pending);
                        doneTxt.setText("Done: " + done);

                    } catch (Exception e) {
                        Toast.makeText(this, "Parse Error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "API Error", Toast.LENGTH_LONG).show()
        );

        queue.add(request);
    }
}