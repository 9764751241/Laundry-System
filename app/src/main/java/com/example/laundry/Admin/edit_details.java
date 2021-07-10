package com.example.laundry.Admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.laundry.R;
import com.example.laundry.User_Login;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class edit_details extends AppCompatActivity {
    private EditText stype,price;
    private String Id,s_stype,s_price;
    Button button;
    Boolean valid = true;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, Admin_Login.class));
        }

        getSupportActionBar().setTitle("Edit Details");

        stype = findViewById(R.id.et_stype);
        price= (EditText) findViewById(R.id.et_price);
        progressDialog = new ProgressDialog(this);
        button = (Button) findViewById(R.id.button);

        Id = getIntent().getStringExtra("id");
        stype.setText(getIntent().getStringExtra("stype"));
        price.setText(getIntent().getStringExtra("price"));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s_stype = stype.getText().toString();
                s_price = price.getText().toString();

                if(TextUtils.isEmpty(s_stype)){
                    stype.setError("Sub Type Cannot be Empty");
                    valid = false;
                }else {
                    valid = true;

                    if(TextUtils.isEmpty(s_price)){
                        price.setError("Price  Cannot be Empty");
                        valid = false;
                    }else {
                        valid = true;
                    }
                }

                if(valid){
                    progressDialog.setMessage("Updating Data...");
                    progressDialog.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_UPDATE, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("error")) {
                                Toast.makeText(edit_details.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    manage_laundry.ma.refresh_list();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.hide();
                            Toast.makeText(edit_details.this, "Failed",Toast.LENGTH_SHORT).show();
                        }
                    }){
                        protected Map<String , String> getParams() throws AuthFailureError {
                            Map<String , String> params = new HashMap<>();
                            params.put("id", Id);
                            params.put("stype", s_stype);
                            params.put("price", s_price);
                            return params;
                        }
                    };
                    RequestHandler.getInstance(edit_details.this).addToRequestQueue(stringRequest);
                }
            }
        });
    }
}
