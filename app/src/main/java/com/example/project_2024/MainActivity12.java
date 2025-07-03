package com.example.project_2024;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity12 extends AppCompatActivity {

    EditText etname, etnumberofdays;
    TextView tvid, tvtotal;
    Button delete, update;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main12);  // Ensure this layout exists

        // Initialize views
        tvid = findViewById(R.id.tvids);
        etname = findViewById(R.id.et2);
        etnumberofdays = findViewById(R.id.et3);
        tvtotal = findViewById(R.id.totall);
        delete = findViewById(R.id.delbtn);
        update = findViewById(R.id.upbtn);

        // Retrieve data passed from the previous activity
        Intent intent = getIntent();
        if (intent != null) {
            String id = intent.getStringExtra("id");
            String name = intent.getStringExtra("name");
            String numberofdays = intent.getStringExtra("numberofdays");
            String total = intent.getStringExtra("total");

            // Check if any of the data is missing or null
            if (id != null && !id.isEmpty() && name != null && !name.isEmpty() && numberofdays != null && !numberofdays.isEmpty() && total != null && !total.isEmpty()) {
                // Set the values to the respective views
                tvid.setText(id); // Ensure only ID is set, without extra text
                etname.setText(name);
                etnumberofdays.setText(numberofdays);
                tvtotal.setText("TOTAL: " + total);  // Format the total display
            } else {
                Toast.makeText(MainActivity12.this, "Invalid data received", Toast.LENGTH_SHORT).show();
                Log.e("MainActivity12", "Missing or invalid intent data");
            }
        } else {
            Toast.makeText(MainActivity12.this, "No data received", Toast.LENGTH_SHORT).show();
            Log.e("MainActivity12", "No intent received");
        }


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validate input fields
                if (etname.getText().toString().isEmpty() || etnumberofdays.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity12.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Log inputs for debugging
                Log.d("UpdateClick", "ID: " + tvid.getText().toString());
                Log.d("UpdateClick", "Name: " + etname.getText().toString());
                Log.d("UpdateClick", "Number of Days: " + etnumberofdays.getText().toString());

                // Update database
                requestQueue = Volley.newRequestQueue(MainActivity12.this);
                String url = "http://192.168.82.105/reservist/updatedata.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("VolleyResponse", "Response: " + response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(MainActivity12.this, message, Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Log.e("VolleyResponse", "Error parsing response", e);
                                    Toast.makeText(MainActivity12.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                if (error.networkResponse != null) {
                                    Log.e("VolleyError", "Status Code: " + error.networkResponse.statusCode);
                                    Log.e("VolleyError", "Response: " + new String(error.networkResponse.data));
                                }
                                Toast.makeText(MainActivity12.this, "Error in request: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", tvid.getText().toString());
                        params.put("name", etname.getText().toString());
                        params.put("numberofdays", etnumberofdays.getText().toString());
                        return params;
                    }
                };

                requestQueue.add(stringRequest);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validate ID field
                if (tvid.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity12.this, "No ID provided to delete", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Confirm deletion
                new AlertDialog.Builder(MainActivity12.this)
                        .setTitle("Confirm Deletion")
                        .setMessage("Are you sure you want to delete this record?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Send delete request
                            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity12.this);
                            String url = "http://192.168.82.105/reservist/deletedata.php";  // Make sure this PHP script exists on your server

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                String message = jsonObject.getString("message");
                                                Toast.makeText(MainActivity12.this, message, Toast.LENGTH_LONG).show();

                                                // Optionally, finish activity after deletion
                                                finish();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Toast.makeText(MainActivity12.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            error.printStackTrace();
                                            Toast.makeText(MainActivity12.this, "Error in request: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }) {
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("id", tvid.getText().toString()); // Send ID to delete
                                    return params;
                                }
                            };

                            requestQueue.add(stringRequest);
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });


        // Handle edge-to-edge display, if applicable
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
