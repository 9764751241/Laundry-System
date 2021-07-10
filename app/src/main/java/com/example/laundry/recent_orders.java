package com.example.laundry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.laundry.Admin.manage_laundry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;

public class recent_orders extends AppCompatActivity {
    protected Cursor cursor;
    ArrayList<R_Model> thelist;
    ListView listview;
    List<R_Model> listItems;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    ProgressDialog progressDialog;
    private String str_id;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_orders);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, User_Login.class));
        }

        getSupportActionBar().setTitle("Recent Orders");
        id=SharedPrefManager.getInstance(this).getUserId();
        str_id = String.valueOf(id).trim();
        recyclerView = (RecyclerView) findViewById(R.id.list4);
        recyclerView.setLayoutManager(new LinearLayoutManager(recent_orders.this));
        progressDialog = new ProgressDialog(this);
        listItems = new ArrayList<>();

        r_refresh_list();
    }

    public void r_refresh_list(){
        listItems.clear();
        adapter = new R_MyAdapter(listItems,getApplicationContext());
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.URL_RECENT_ORDERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(!jsonObject.getBoolean("error")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        Toast.makeText(recent_orders.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        for (int i = 0; i<jsonArray.length(); i++){
                            JSONObject o = jsonArray.getJSONObject(i);
                            R_Model item = new R_Model(
                                    o.getString("id"),
                                    o.getString("type"),
                                    o.getString("stype"),
                                    o.getString("amount")
                            );
                            listItems.add(item);
                            adapter = new R_MyAdapter(listItems,getApplicationContext());
                            recyclerView.setAdapter(adapter);
                        }
                    }else{
                        Toast.makeText(recent_orders.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(recent_orders.this, "Failed",Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String , String> getParams() throws AuthFailureError {
                Map<String , String> params = new HashMap<>();
                params.put("id", str_id);
                return params;
            }
        };
        RequestHandler.getInstance(recent_orders.this).addToRequestQueue(stringRequest);
    }
}
