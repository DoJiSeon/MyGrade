package com.bliss.csc.mygrade;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class Contents extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        Button toCalculator = (Button)findViewById(R.id.btn_calculator);
        Button toData = (Button)findViewById(R.id.btn_data);
        Button toSite = (Button)findViewById(R.id.btn_site);

        toCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),
                        Calculator.class);
                startActivity(intent);
            }
        });

        toData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),
                        Data.class);
                startActivity(intent);
            }
        });

        toSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),
                        Site.class);
                startActivity(intent);
            }
        });
    }
}
