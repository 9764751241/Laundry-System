package com.example.laundry.Admin;

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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.laundry.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class a_edit_order extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String Id;
    private Spinner spinlaun1,spinlaun_s1;
    ArrayList<String> laun_array1=new ArrayList<>();
    ArrayList<String> laun_s_array1=new ArrayList<>();
    ArrayAdapter<String> laun_adapter1;
    ArrayAdapter<String> laun_s_adapter1;
    RequestQueue req1;
    private TextView price1,no1;
    private Button update;
    private String selectedLaun1,selectedLaun2,str_price;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_edit_order);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, Admin_Login.class));
        }
        ActionBar ab=getSupportActionBar();
        ab.setTitle("Edit Order");
        Id = getIntent().getStringExtra("id");

        req1= Volley.newRequestQueue(this);
        spinlaun1=findViewById(R.id.spinLaun1);
        spinlaun_s1=findViewById(R.id.spinLaun_s1);
        no1=findViewById(R.id.txtno1);
        update=findViewById(R.id.btn_update_laun);
        price1=findViewById(R.id.price1);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String str_no = no1.getText().toString().trim();
                final String str_price = price1.getText().toString().trim();
                if(str_no.isEmpty()){
                    Toast.makeText(a_edit_order.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.setMessage("Updating Order...");
                    progressDialog.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            Constants.URL_A_EDIT_ORDERS,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    no1.setText("");
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        if(!obj.getBoolean("error")) {
                                            Toast.makeText(a_edit_order.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                            manage_orders.ma1.refresh_list1();
                                        } else {
                                            Toast.makeText(a_edit_order.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                            params.put("id", Id);
                            return params;
                        }
                    };
                    RequestHandler.getInstance(a_edit_order.this).addToRequestQueue(stringRequest);
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
                        laun_array1.add(laun_name);
                        laun_adapter1=new ArrayAdapter<>(a_edit_order.this,android.R.layout.simple_spinner_dropdown_item,laun_array1);
                        laun_adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spinlaun1.setAdapter(laun_adapter1);
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
        req1.add(jor);
        spinlaun1.setOnItemSelectedListener(this);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId()==R.id.spinLaun1) {
            laun_s_array1.clear();
            selectedLaun1=parent.getSelectedItem().toString();
            String url="http://192.168.1.196:8080/Laundry-System/laun_s.php?laun_type_desc="+selectedLaun1;
            req1=Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray=response.getJSONArray( "laun_s_type");
                        for(int i=0; i < jsonArray.length(); i++)
                        {
                            JSONObject jo=jsonArray.getJSONObject(i);
                            String laun_s_name=jo.optString("laun_s_type");
                            laun_s_array1.add(laun_s_name);
                            laun_s_adapter1=new ArrayAdapter<>(a_edit_order.this,android.R.layout.simple_spinner_dropdown_item,laun_s_array1);
                            laun_s_adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            spinlaun_s1.setAdapter(laun_s_adapter1);
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
            req1.add(jsonObjectRequest);
            spinlaun_s1.setOnItemSelectedListener(this);
        }
        if (parent.getId()==R.id.spinLaun_s1) {
            selectedLaun2=parent.getSelectedItem().toString();
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    com.example.laundry.Constants.URL_PRICE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (!obj.getBoolean("error")) {
                                    str_price=obj.getString("price");
                                    price1.setText(str_price);
                                } else {
                                    Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
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
