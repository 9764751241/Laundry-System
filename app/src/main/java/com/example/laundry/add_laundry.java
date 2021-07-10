package com.example.laundry;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.laundry.Admin.Admin_Login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class add_laundry extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinlaun,spinlaun_s;
    ArrayList<String> laun_array=new ArrayList<>();
    ArrayList<String> laun_s_array=new ArrayList<>();
    ArrayAdapter<String> laun_adapter;
    ArrayAdapter<String> laun_s_adapter;
    RequestQueue req;
    private TextView price,no;
    private Button add;
    private String selectedLaun1,selectedLaun2,str_price;
    private ProgressDialog progressDialog;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_laundry);
        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, User_Login.class));
        }
        ActionBar ab=getSupportActionBar();
        ab.setTitle("Add Laundry");
        req= Volley.newRequestQueue(this);
        spinlaun=findViewById(R.id.spinLaun);
        spinlaun_s=findViewById(R.id.spinLaun_s);
        no=findViewById(R.id.txtno);
        add=findViewById(R.id.btnAdd_laun);
        price=findViewById(R.id.price);
        id=SharedPrefManager.getInstance(this).getUserId();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String str_no = no.getText().toString().trim();
                final String str_price = price.getText().toString().trim();
                final String str_id = String.valueOf(id).trim();
                if(str_no.isEmpty()){
                    Toast.makeText(add_laundry.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.setMessage("Adding Laundry...");
                    progressDialog.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            Constants.URL_ADDLAUN,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    no.setText("");
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        if(!obj.getBoolean("error")) {
                                            Toast.makeText(add_laundry.this,obj.getString("message"), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(add_laundry.this,obj.getString("message"), Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.hide();
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("no", str_no);
                            params.put("laun1", selectedLaun1);
                            params.put("laun2", selectedLaun2);
                            params.put("price", str_price);
                            params.put("id", str_id);
                            return params;
                        }
                    };
                    RequestHandler.getInstance(add_laundry.this).addToRequestQueue(stringRequest);
                }
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        String url="http://192.168.1.196:8080/Laundry-System/laun.php";
        JsonObjectRequest jor=new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray ja=response.getJSONArray("laun_type");
                    for(int i=0; i < ja.length(); i++)
                    {
                        JSONObject jo=ja.getJSONObject(i);
                        String laun_name=jo.optString("laun_type_desc");
                        laun_array.add(laun_name);
                        laun_adapter=new ArrayAdapter<>(add_laundry.this,android.R.layout.simple_spinner_dropdown_item,laun_array);
                        laun_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spinlaun.setAdapter(laun_adapter);
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
        spinlaun.setOnItemSelectedListener(this);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId()==R.id.spinLaun) {
            laun_s_array.clear();
            selectedLaun1=parent.getSelectedItem().toString();
            String url="http://192.168.1.196:8080/Laundry-System/laun_s.php?laun_type_desc="+selectedLaun1;
            req=Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray=response.getJSONArray( "laun_s_type");
                        for(int i=0; i < jsonArray.length(); i++)
                        {
                            JSONObject jo=jsonArray.getJSONObject(i);
                            String laun_s_name=jo.optString("laun_s_type");
                            laun_s_array.add(laun_s_name);
                            laun_s_adapter=new ArrayAdapter<>(add_laundry.this,android.R.layout.simple_spinner_dropdown_item,laun_s_array);
                            laun_s_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            spinlaun_s.setAdapter(laun_s_adapter);
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
            req.add(jsonObjectRequest);
            spinlaun_s.setOnItemSelectedListener(this);
        }
        if (parent.getId()==R.id.spinLaun_s) {
            selectedLaun2=parent.getSelectedItem().toString();
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Constants.URL_PRICE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (!obj.getBoolean("error")) {
                                    str_price=obj.getString("price");
                                    price.setText(str_price);
                                } else {
                                    Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("selectedLaun1", selectedLaun1);
                    params.put("selectedLaun2",selectedLaun2);
                    return params;
                }
            };
            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}