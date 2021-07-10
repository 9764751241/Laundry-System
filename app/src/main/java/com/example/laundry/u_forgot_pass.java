package com.example.laundry;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class u_forgot_pass extends AppCompatActivity implements View.OnClickListener {
    EditText et_E,et_M,et_P,et_CP;
    Button b_f,b_s;
    ProgressDialog progressDialog;
    LinearLayout l1;
    String s_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_forgot_pass);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }
        ActionBar ab=getSupportActionBar();
        ab.setTitle("Forgot Password");
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0000ff")));

        et_E=findViewById(R.id.etEmail);
        et_M=findViewById(R.id.etMob);
        et_P=findViewById(R.id.N_P);
        et_CP=findViewById(R.id.N_CP);
        b_f=findViewById(R.id.btnForgot);
        b_s=findViewById(R.id.save_data);
        l1=findViewById(R.id.l1);
        b_f.setOnClickListener(this);
        b_s.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

    }
    public void forgot(){
        s_email = et_E.getText().toString().trim();
        final String mob = et_M.getText().toString().trim();

        if(s_email.isEmpty() || mob.isEmpty()){
            Toast.makeText(u_forgot_pass.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Constants.URL_CHECK,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (!obj.getBoolean("error")) {
                                    Toast.makeText(u_forgot_pass.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                            et_P.setVisibility(View.VISIBLE);
                                            et_CP.setVisibility(View.VISIBLE);
                                            b_s.setVisibility(View.VISIBLE);
                                            l1.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(u_forgot_pass.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", s_email);
                    params.put("mob", mob);
                    return params;
                }
            };
            RequestHandler.getInstance(u_forgot_pass.this).addToRequestQueue(stringRequest);
        }
    }
    public void save(){
        final String pass = et_P.getText().toString().trim();
        final String npass = et_CP.getText().toString().trim();

        if(pass.isEmpty() || npass.isEmpty()){
            Toast.makeText(u_forgot_pass.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }
        else if (pass.equals(npass)){

            progressDialog.show();
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Constants.URL_FORGOT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (!obj.getBoolean("error")) {
                                    Toast.makeText(u_forgot_pass.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), User_Login.class));
                                    finish();
                                } else {
                                    Toast.makeText(u_forgot_pass.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", s_email);
                    params.put("newp", pass);
                    return params;
                }
            };
            RequestHandler.getInstance(u_forgot_pass.this).addToRequestQueue(stringRequest);
        }else {
            Toast.makeText(u_forgot_pass.this, "Password didn't match", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view==b_f){
            forgot();
        }
        if (view==b_s){
            save();
        }
    }
}