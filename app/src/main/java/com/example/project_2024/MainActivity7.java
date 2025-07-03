package com.example.project_2024;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MainActivity7 extends AppCompatActivity {
    Button back, sub;
    TextView select, total, pay, rates, success;
    EditText numb, named;
    CheckBox meal, spa, laund, trans;
    RadioButton rb1, rb2, rb3;
    RadioGroup r;

    double totall = 0, amtservice = 0;
    LinkedList<String> choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main7);

        back = findViewById(R.id.backbtn);
        sub = findViewById(R.id.submit);
        numb = findViewById(R.id.numb);
        rates = findViewById(R.id.rate);
        select = findViewById(R.id.select);
        total = findViewById(R.id.total);
        pay = findViewById(R.id.payable);
        meal = findViewById(R.id.meal);
        spa = findViewById(R.id.spa);
        laund = findViewById(R.id.laund);
        trans = findViewById(R.id.trans);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        named = findViewById(R.id.named);
        r = findViewById(R.id.r);
        success = findViewById(R.id.success);

        choose = new LinkedList<>();

        meal.setOnClickListener(view -> {
            if (meal.isChecked()) {
                amtservice += 300;
                totall += 300.00;
                choose.add("Add Meal");
            } else {
                amtservice -= 300;
                totall -= 300.00;
                choose.remove("Add Meal");
            }
            updateUI();
        });

        spa.setOnClickListener(view -> {
            if (spa.isChecked()) {
                amtservice += 500.00;
                totall += 500.00;
                choose.add("Spa and Wellness Service");
            } else {
                amtservice -= 500.00;
                totall -= 500.00;
                choose.remove("Spa and Wellness Service");
            }
            updateUI();
        });

        laund.setOnClickListener(view -> {
            if (laund.isChecked()) {
                amtservice += 200.00;
                totall += 200.00;
                choose.add("Laundry Service");
            } else {
                amtservice -= 200.00;
                totall -= 200.00;
                choose.remove("Laundry Service");
            }
            updateUI();
        });

        trans.setOnClickListener(view -> {
            if (trans.isChecked()) {
                amtservice += 250.00;
                totall += 250.00;
                choose.add("Transportation Service");
            } else {
                amtservice -= 250.00;
                totall -= 250.00;
                choose.remove("Transportation Service");
            }
            updateUI();
        });

        rb1.setOnClickListener(view -> {
            if (rb1.isChecked()) {
                applyDiscount(0.20);
            }
        });

        rb2.setOnClickListener(view -> {
            if (rb2.isChecked()) {
                applyDiscount(0.10);
            }
        });

        rb3.setOnClickListener(view -> {
            if (rb3.isChecked()) {
                applyDiscount(0);
            }
        });

        sub.setOnClickListener(view -> {
            String name = named.getText().toString();
            String numberOfDays = numb.getText().toString();
            String totalAmount = pay.getText().toString();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(numberOfDays) || TextUtils.isEmpty(totalAmount)) {
                Toast.makeText(MainActivity7.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Ensure the number of days is numeric
            if (!isNumeric(numberOfDays)) {
                Toast.makeText(MainActivity7.this, "Please enter a valid number of days.", Toast.LENGTH_SHORT).show();
                return;
            }

            String url = "http://192.168.82.105/reservist/adding.php";
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity7.this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            Toast.makeText(MainActivity7.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity7.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        error.printStackTrace();
                        Toast.makeText(MainActivity7.this, "Request failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }) {

                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", named.getText().toString());
                    params.put("numberofdays", numb.getText().toString());
                    params.put("total", pay.getText().toString());
                    return params;
                }
            };

            requestQueue.add(stringRequest);
        });

        back.setOnClickListener(view -> {
            meal.setChecked(false);
            spa.setChecked(false);
            laund.setChecked(false);
            trans.setChecked(false);
            r.clearCheck();
            numb.setText("");
            select.setText("");
            total.setText("");
            pay.setText("");
            named.setText("");
            Intent z = new Intent(MainActivity7.this, MainActivity10.class);
            startActivity(z);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void updateUI() {
        total.setText(String.valueOf(totall));
        select.setText(choose.toString());
    }

    private void applyDiscount(double discountRate) {
        if (!isNumeric(numb.getText().toString())) {
            Toast.makeText(MainActivity7.this, "Please enter a valid number of days.", Toast.LENGTH_SHORT).show();
            return;
        }
        double numDays = Double.parseDouble(numb.getText().toString());
        double tt = 1500.00 * numDays;
        double discount = (amtservice + tt) * discountRate;
        double discountedAmount = (amtservice + tt) - discount;
        pay.setText(String.valueOf(discountedAmount));
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
