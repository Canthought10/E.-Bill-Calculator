package com.example.project_2024;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button logbtn;
    private TextView sign, about;
    private EditText user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        user = findViewById(R.id.name);
        pass = findViewById(R.id.word);
        logbtn = findViewById(R.id.signs);
        sign = findViewById(R.id.sign);
        about = findViewById(R.id.about);

        // Login button click listener
        logbtn.setOnClickListener(view -> {
            String username = user.getText().toString().trim();
            String password = pass.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(MainActivity.this, "PLEASE FILL IN ALL FIELDS!", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(username, password);
            }
        });

        // Sign-up button click listener
        sign.setOnClickListener(view -> {
            Intent signUpIntent = new Intent(MainActivity.this, MainActivity3.class);
            startActivity(signUpIntent);
        });

        // About button click listener
        about.setOnClickListener(view -> {
            Intent aboutIntent = new Intent(MainActivity.this, MainActivity9.class);
            startActivity(aboutIntent);
        });

        // Adjust window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loginUser(String username, String password) {
        String url = "http://192.168.82.105/reservist/fetch.php"; // Ensure the server URL is correct
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Handle server response
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String message = jsonResponse.getString("message");

                        if ("Connected Successfully".equals(message)) {
                            JSONArray jsonArray = jsonResponse.getJSONArray("data");
                            boolean loginSuccess = false;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject row = jsonArray.getJSONObject(i);
                                String users = row.getString("username");
                                String passw = row.getString("password");

                                if (users.equals(username) && passw.equals(password)) {
                                    loginSuccess = true;
                                    break;
                                }
                            }

                            if (loginSuccess) {
                                Toast.makeText(MainActivity.this, "LOGIN SUCCESSFULLY!", Toast.LENGTH_SHORT).show();
                                Intent dashboardIntent = new Intent(MainActivity.this, MainActivity10.class); // Replace with actual dashboard activity
                                startActivity(dashboardIntent);
                                finish(); // Prevent user from going back to login screen
                            } else {
                                Toast.makeText(MainActivity.this, "LOGIN UNSUCCESSFUL!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Server error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error parsing response.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Handle network error
                    Toast.makeText(MainActivity.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Send username and password to the server
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
