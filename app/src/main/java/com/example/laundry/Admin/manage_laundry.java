package com.example.laundry.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.laundry.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class manage_laundry extends AppCompatActivity {
    public static manage_laundry ma;
    protected Cursor cursor;
    ArrayList<Model> thelist;
    ListView listview;
    List<Model> listItems;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_laundry);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, Admin_Login.class));
        }

        getSupportActionBar().setTitle("Manage Types");
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(manage_laundry.this));
        progressDialog = new ProgressDialog(this);
        listItems = new ArrayList<>();
        ma = this;
        refresh_list();
    }

        public void search1 (String text1){
            listItems.clear();
            adapter = new MyAdapter(listItems, getApplicationContext());
            recyclerView.setAdapter(adapter);

            recyclerView.setItemAnimator(new DefaultItemAnimator());
            progressDialog.setMessage("Fetching Data...");
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SEARCH_TYPE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    progressDialog.hide();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean("error")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            Toast.makeText(manage_laundry.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject o = jsonArray.getJSONObject(i);
                                Model item = new Model(
                                        o.getString("id"),
                                        o.getString("type"),
                                        o.getString("stype"),
                                        o.getString("price")
                                );
                                listItems.add(item);
                                adapter = new MyAdapter(listItems, getApplicationContext());
                                recyclerView.setAdapter(adapter);
                            }
                        } else {
                            Toast.makeText(manage_laundry.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.hide();
                    Toast.makeText(manage_laundry.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("search", text1);
                    return params;
                }
            };
            RequestHandler.getInstance(manage_laundry.this).addToRequestQueue(stringRequest);

    }

    public void refresh_list(){
        listItems.clear();
        adapter = new MyAdapter(listItems,getApplicationContext());
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SELECT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(!jsonObject.getBoolean("error")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        Toast.makeText(manage_laundry.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject o = jsonArray.getJSONObject(i);
                            Model item = new Model(
                                    o.getString("id"),
                                    o.getString("type"),
                                    o.getString("stype"),
                                    o.getString("price")
                            );
                            listItems.add(item);
                            adapter = new MyAdapter(listItems, getApplicationContext());
                            recyclerView.setAdapter(adapter);
                        }
                    }else{
                        Toast.makeText(manage_laundry.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(manage_laundry.this, "Failed",Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String , String> getParams() throws AuthFailureError {
                Map<String , String> params = new HashMap<>();
                params.put("name", "kl");
                return params;
            }
        };
        RequestHandler.getInstance(manage_laundry.this).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        getMenuInflater().inflate(R.menu.add, menu);
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
                search1(newText);
                return false;
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add) {

            Intent tes = new Intent(manage_laundry.this, add_type.class);
            startActivity(tes);
        }
        return super.onOptionsItemSelected(item);
    }
}
