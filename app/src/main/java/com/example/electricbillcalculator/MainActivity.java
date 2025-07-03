package com.example.electricbillcalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText watt, per, tot;
    Button comp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        watt = (EditText)findViewById(R.id.etnum1);
        per  = (EditText) findViewById(R.id.etnum2);
        tot  = (EditText) findViewById(R.id.etnum3);
        comp = (Button) findViewById(R.id.btncom);

        comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double watts = Double.parseDouble(watt.getText().toString());
                double amts = Double.parseDouble(per.getText().toString());
                double tota = watts * amts;

                tot.setText(Double.toString(tota));



            }
        });


    }

}