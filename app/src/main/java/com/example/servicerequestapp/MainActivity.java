package com.example.servicerequestapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;

import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button addBtn, searchBtn, dashboardBtn;
    EditText inputTitle, searchInput;

    ArrayList<RequestModel> list;
    RequestAdapter adapter;

    String BASE_URL = "https://ipt-final-project.onrender.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        addBtn = findViewById(R.id.addBtn);
        dashboardBtn = findViewById(R.id.dashboardBtn);
        inputTitle = findViewById(R.id.inputTitle);
        searchInput = findViewById(R.id.searchInput);
        searchBtn = findViewById(R.id.searchBtn);

        list = new ArrayList<>();
        adapter = new RequestAdapter(list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemActionListener(new RequestAdapter.OnItemActionListener() {
            @Override
            public void onClick(int position) {
                showOptions(position);
            }

            @Override
            public void onLongClick(int position) {
                updateStatus(list.get(position));
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(
                new SwipeToDeleteCallback(this) {

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder vh, int dir) {

                        int pos = vh.getAdapterPosition();
                        RequestModel deleted = list.get(pos);

                        list.remove(pos);
                        adapter.notifyItemRemoved(pos);

                        Snackbar.make(recyclerView, "Deleted", Snackbar.LENGTH_LONG)
                                .setAction("UNDO", v -> {
                                    list.add(pos, deleted);
                                    adapter.notifyItemInserted(pos);
                                })
                                .show();

                        deleteData(deleted.id);
                    }
                });

        helper.attachToRecyclerView(recyclerView);

        loadData("");

        addBtn.setOnClickListener(v -> addData());

        searchBtn.setOnClickListener(v ->
                loadData(searchInput.getText().toString().trim())
        );

        dashboardBtn.setOnClickListener(v ->
                startActivity(new Intent(this, DashboardActivity.class))
        );
    }

    private void loadData(String keyword) {

        String url;

        try {
            if (keyword.isEmpty()) {
                url = BASE_URL + "requests";
            } else {
                url = BASE_URL + "requests?search=" +
                        URLEncoder.encode(keyword, "UTF-8");
            }
        } catch (Exception e) {
            url = BASE_URL + "requests";
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(new StringRequest(Request.Method.GET, url,
                res -> {
                    try {
                        JSONArray arr = new JSONArray(res);
                        list.clear();

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject o = arr.getJSONObject(i);

                            list.add(new RequestModel(
                                    o.getInt("id"),
                                    o.getString("title"),
                                    o.optString("status", "pending")
                            ));
                        }

                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        Toast.makeText(this, "Parse Error", Toast.LENGTH_SHORT).show();
                    }
                },
                err -> Toast.makeText(this, "API Error", Toast.LENGTH_SHORT).show()
        ));
    }

    private void addData() {

        String title = inputTitle.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Enter something", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject json = new JSONObject();

        try {
            json.put("user_id", 1);
            json.put("title", title);
            json.put("description", "Android");
        } catch (Exception ignored) {}

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL + "requests",
                json,
                r -> {
                    inputTitle.setText("");
                    loadData("");
                },
                e -> Toast.makeText(this, "Add Error", Toast.LENGTH_SHORT).show()
        ));
    }

    private void showOptions(int pos) {

        View v = LayoutInflater.from(this)
                .inflate(R.layout.dialog_actions, null);

        AlertDialog d = new AlertDialog.Builder(this)
                .setView(v)
                .create();

        d.show();

        RequestModel item = list.get(pos);

        v.findViewById(R.id.editBtn).setOnClickListener(x -> {
            d.dismiss();
            showEdit(pos, item.id);
        });

        v.findViewById(R.id.doneBtn).setOnClickListener(x -> {
            d.dismiss();
            updateStatus(item);
        });
    }

    private void updateStatus(RequestModel item) {

        JSONObject j = new JSONObject();

        try {
            j.put("title", item.title);
            j.put("description", "Updated");
            j.put("status", "done");
        } catch (Exception ignored) {}

        RequestQueue q = Volley.newRequestQueue(this);

        q.add(new JsonObjectRequest(
                Request.Method.PUT,
                BASE_URL + "requests/" + item.id,
                j,
                r -> loadData(""),
                e -> Toast.makeText(this, "Update Error", Toast.LENGTH_SHORT).show()
        ));
    }

    private void showEdit(int pos, int id) {

        EditText input = new EditText(this);
        input.setText(list.get(pos).title);

        new AlertDialog.Builder(this)
                .setTitle("Edit Request")
                .setView(input)
                .setPositiveButton("Update", (d,w)->{
                    updateData(id, input.getText().toString());
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateData(int id, String title) {

        JSONObject j = new JSONObject();

        try {
            j.put("title", title);
            j.put("description", "Edited");
            j.put("status", "pending");
        } catch (Exception ignored) {}

        RequestQueue q = Volley.newRequestQueue(this);

        q.add(new JsonObjectRequest(
                Request.Method.PUT,
                BASE_URL + "requests/" + id,
                j,
                r -> loadData(""),
                e -> Toast.makeText(this, "Edit Error", Toast.LENGTH_SHORT).show()
        ));
    }

    private void deleteData(int id) {

        RequestQueue q = Volley.newRequestQueue(this);

        q.add(new StringRequest(
                Request.Method.DELETE,
                BASE_URL + "requests/" + id,
                r -> {},
                e -> Toast.makeText(this, "Delete Error", Toast.LENGTH_SHORT).show()
        ));
    }
}