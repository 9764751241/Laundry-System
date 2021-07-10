package com.example.laundry.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.laundry.R;
import com.example.laundry.add_laundry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class add_type extends AppCompatActivity {
    private Spinner spin;
    public static manage_laundry ma;
    ArrayList<String> laun=new ArrayList<>();
    ArrayAdapter<String> adapter;
    RequestQueue req;
    private EditText stype,price;
    private String s_type,s_stype,s_price;
    Button button;
    Boolean valid = true;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, Admin_Login.class));
        }

        getSupportActionBar().setTitle("Add Type");
        stype = (EditText) findViewById(R.id.ss_stype);
        price = (EditText) findViewById(R.id.ss_price);
        req= Volley.newRequestQueue(this);
        spin=findViewById(R.id.spin);

        progressDialog = new ProgressDialog(this);
        button = (Button) findViewById(R.id.button);

        progressDialog.setMessage("Please wait...");

        String url="http://192.168.1.196:8080/Laundry-System/laun.php";
        JsonObjectRequest jor=new JsonObjectRequest(Request.Method.POST,url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray ja=response.getJSONArray("laun_type");
                    for(int i=0; i < ja.length(); i++)
                    {
                        JSONObject jo=ja.getJSONObject(i);
                        String laun_name=jo.optString("laun_type_desc");
                        laun.add(laun_name);
                        adapter=new ArrayAdapter<>(add_type.this,android.R.layout.simple_spinner_dropdown_item,laun);
                        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spin.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        req.add(jor);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s_type = spin.getSelectedItem().toString();
                s_stype = stype.getText().toString();
                s_price = price.getText().toString();

                if(s_type.isEmpty() || s_stype.isEmpty() || s_price.isEmpty()){
                    Toast.makeText(add_type.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.setMessage("Adding Type...");
                    progressDialog.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_ADD_SUB, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("error")) {
                                Toast.makeText(add_type.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    manage_laundry.ma.refresh_list();
                                    finish();
                                } else {
                                    Toast.makeText(add_type.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.hide();
                            Toast.makeText(add_type.this, "Failed to Data",Toast.LENGTH_SHORT).show();
                        }
                    }){
                        protected Map<String , String> getParams() throws AuthFailureError {
                            Map<String , String> params = new HashMap<>();
                            params.put("type", s_type);
                            params.put("stype", s_stype);
                            params.put("price",s_price);
                            return params;
                        }
                    };
                    RequestHandler.getInstance(add_type.this).addToRequestQueue(stringRequest);
                }
            }
        });
    }
}