package com.example.project_2024;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity3 extends AppCompatActivity {
    Button signs;
    EditText name, word;
    TextView suc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main3);

        name = findViewById(R.id.name);
        word = findViewById(R.id.word);
        signs = findViewById(R.id.signs);
        suc = findViewById(R.id.suc);

        signs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = name.getText().toString().trim();
                String password = word.getText().toString().trim();

                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    signupUser (username, password);
                } else {
                    Toast.makeText(MainActivity3.this, "PLEASE FILL IN ALL FIELDS!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void signupUser (final String username, final String password) {
        String url = "http://192.168.82.105/reservist/signup.php";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity3.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(MainActivity3.this, "SIGN-UP SUCCESSFULLY!", Toast.LENGTH_SHORT).show();
                        Intent a = new Intent(MainActivity3.this, MainActivity.class);
                        a.putExtra("username", username);
                        a.putExtra("password", password);
                        startActivity(a);
                        name.setText("");
                        word.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(MainActivity3.this, "SIGN-UP FAILED! " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}