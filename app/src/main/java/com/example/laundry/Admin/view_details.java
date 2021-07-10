package com.example.laundry.Admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.laundry.R;

public class view_details extends AppCompatActivity {
    private TextView type,stype,price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details2);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, Admin_Login.class));
        }

        ActionBar ab=getSupportActionBar();
        ab.setTitle("Type Details");

        type = (TextView) findViewById(R.id.tv_type);
        stype = (TextView) findViewById(R.id.tv_stype);
        price = (TextView) findViewById(R.id.tv_price);

        type.setText(getIntent().getStringExtra("type"));
        stype.setText(getIntent().getStringExtra("stype"));
        price.setText(getIntent().getStringExtra("price"));
    }
}