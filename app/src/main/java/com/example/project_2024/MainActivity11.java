package com.example.project_2024;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity11 extends AppCompatActivity {

    ListView list;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main11);


        list = findViewById(R.id.ListView);
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(adapter);


        fetchData();


        list.setOnItemClickListener((parent, view, position, id) -> {
            String userData = arrayList.get(position);
            try {

                String[] lines = userData.split("\n");


                if (lines.length < 4) {
                    throw new Exception("Malformed data: Not enough lines");
                }

                String Id = getDataFromLine(lines[0]);
                String name = getDataFromLine(lines[1]);
                String numberofdays = getDataFromLine(lines[2]);
                String total = getDataFromLine(lines[3]);


                Intent intent = new Intent(MainActivity11.this, MainActivity12.class);
                intent.putExtra("id", Id);
                intent.putExtra("name", name);
                intent.putExtra("numberofdays", numberofdays);
                intent.putExtra("total", total);
                startActivity(intent);

            } catch (Exception e) {
                Toast.makeText(MainActivity11.this, "Error parsing item data", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private String getDataFromLine(String line) throws Exception {
        String[] parts = line.split(":");
        if (parts.length < 2) {
            throw new Exception("Malformed line: " + line);
        }
        return parts[1].trim();
    }

    public void fetchData() {
        requestQueue = Volley.newRequestQueue(this);
        String url = "http://192.168.82.105/reservist/fetchdata.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {

                        JSONObject jsonResponse = new JSONObject(response.trim());
                        String message = jsonResponse.getString("message");

                        if ("Connection success".equals(message)) {
                            JSONArray jsonArray = jsonResponse.getJSONArray("data");
                            arrayList.clear();

                            if (jsonArray.length() == 0) {
                                Toast.makeText(MainActivity11.this, "No record found", Toast.LENGTH_SHORT).show();
                            } else {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject user = jsonArray.getJSONObject(i);
                                    String id = user.optString("id", "N/A");
                                    String name = user.optString("name", "N/A");
                                    String numberofdays = user.optString("numberofdays", "N/A");
                                    String total = user.optString("total", "0");

                                    String displayText = String.format(
                                            "ID: %s\nName: %s\nNumberofDays: %s\nTotal: %s",
                                            id, name, numberofdays, total);
                                    arrayList.add(displayText);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(MainActivity11.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity11.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("ERROR: ", error.toString());
                    Toast.makeText(MainActivity11.this, "Request failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Add the request to the request queue
        requestQueue.add(stringRequest);
    }
}
