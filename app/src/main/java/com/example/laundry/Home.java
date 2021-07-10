package com.example.laundry;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.laundry.Admin.Admin_Login;

public class Home extends AppCompatActivity {

    private Button admin,user,nuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        admin=(Button)findViewById(R.id.btnAdmin);
        user=(Button)findViewById(R.id.btnUser);
        nuser=(Button)findViewById(R.id.btnNew_User);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToMain=new Intent(Home.this, Admin_Login.class);
                startActivity(intToMain);
            }
        });
        nuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToMain=new Intent(Home.this, User_Register.class);
                startActivity(intToMain);
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToMain=new Intent(Home.this,User_Login.class);
                startActivity(intToMain);
            }
        });
    }
}