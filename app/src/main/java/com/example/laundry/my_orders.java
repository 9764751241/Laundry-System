package com.example.laundry;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class my_orders extends AppCompatActivity {
    public static my_orders mo;
    protected Cursor cursor;
    ArrayList<Model> thelist;
    ListView listview;
    List<Model> listItems;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    ProgressDialog progressDialog;
    private String str_id;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, User_Login.class));
        }

        getSupportActionBar().setTitle("My Orders");
        id = SharedPrefManager.getInstance(this).getUserId();
        str_id = String.valueOf(id).trim();
        recyclerView = (RecyclerView) findViewById(R.id.list3);
        recyclerView.setLayoutManager(new LinearLayoutManager(my_orders.this));
        progressDialog = new ProgressDialog(this);
        listItems = new ArrayList<>();
        mo = this;
        refresh_list();
    }

        public void search(String text1){
                    listItems.clear();
                    adapter = new MyAdapter(listItems,getApplicationContext());
                    recyclerView.setAdapter(adapter);

                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    progressDialog.setMessage("Fetching Data...");
                    progressDialog.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_MY_ORDERS_SEARCH, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                if(!jsonObject.getBoolean("error")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    Toast.makeText(my_orders.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    for (int i = 0; i<jsonArray.length(); i++){
                                        JSONObject o = jsonArray.getJSONObject(i);
                                        Model item = new Model(
                                                o.getString("id"),
                                                o.getString("name"),
                                                o.getString("type"),
                                                o.getString("stype"),
                                                o.getString("price"),
                                                o.getString("status")
                                        );
                                        listItems.add(item);
                                        adapter = new MyAdapter(listItems,getApplicationContext());
                                        recyclerView.setAdapter(adapter);
                                    }
                                }else{
                                    Toast.makeText(my_orders.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.hide();
                            Toast.makeText(my_orders.this, "Failed",Toast.LENGTH_SHORT).show();
                        }
                    }){
                        protected Map<String , String> getParams() throws AuthFailureError {
                            Map<String , String> params = new HashMap<>();
                            params.put("search", text1);
                            params.put("id", str_id);
                            return params;
                        }
                    };
                    RequestHandler.getInstance(my_orders.this).addToRequestQueue(stringRequest);
            }

    public void refresh_list(){
        listItems.clear();
        adapter = new MyAdapter(listItems,getApplicationContext());
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.URL_MY_ORDERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(!jsonObject.getBoolean("error")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        Toast.makeText(my_orders.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        for (int i = 0; i<jsonArray.length(); i++){
                            JSONObject o = jsonArray.getJSONObject(i);
                            Model item = new Model(
                                    o.getString("id"),
                                    o.getString("name"),
                                    o.getString("type"),
                                    o.getString("stype"),
                                    o.getString("price"),
                                    o.getString("status")
                            );
                            listItems.add(item);
                            adapter = new MyAdapter(listItems,getApplicationContext());
                            recyclerView.setAdapter(adapter);
                        }
                    }else{
                        Toast.makeText(my_orders.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(my_orders.this, "Failed",Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String , String> getParams() throws AuthFailureError {
                Map<String , String> params = new HashMap<>();
                params.put("id", str_id);
                return params;
            }
        };
        RequestHandler.getInstance(my_orders.this).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_questions);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint("Search by cloth type");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });
        return true;
    }
}
