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

public class confirm_order extends AppCompatActivity {
    public static confirm_order co1;
    protected Cursor cursor;
    ArrayList<Model2> thelist;
    ListView listview2;
    List<Model2> listItems2;
    private RecyclerView recyclerView2;
    private RecyclerView.Adapter adapter2;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, Admin_Login.class));
        }

        getSupportActionBar().setTitle("Confirmed Orders");

        recyclerView2 = (RecyclerView) findViewById(R.id.list2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(confirm_order.this));
        progressDialog = new ProgressDialog(this);
        listItems2 = new ArrayList<>();
        co1 = this;
        refresh_list2();

    }

    public void search3(String text1) {
        listItems2.clear();
        adapter2 = new MyAdapter2(listItems2, getApplicationContext());
        recyclerView2.setAdapter(adapter2);

        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CONFIRMED_SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("error")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        Toast.makeText(confirm_order.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject o = jsonArray.getJSONObject(i);
                            Model2 item = new Model2(
                                    o.getString("id"),
                                    o.getString("name"),
                                    o.getString("type"),
                                    o.getString("stype"),
                                    o.getString("price"),
                                    o.getString("status")
                            );
                            listItems2.add(item);
                            adapter2 = new MyAdapter2(listItems2, getApplicationContext());
                            recyclerView2.setAdapter(adapter2);
                        }
                    } else {
                        Toast.makeText(confirm_order.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(confirm_order.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("search1", text1);
                return params;
            }
        };
        RequestHandler.getInstance(confirm_order.this).addToRequestQueue(stringRequest);
    }

    public void refresh_list2(){
        listItems2.clear();
        adapter2 = new MyAdapter2(listItems2,getApplicationContext());
        recyclerView2.setAdapter(adapter2);

        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.URL_CONFIRMED_ORDERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(!jsonObject.getBoolean("error")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        Toast.makeText(confirm_order.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        for (int i = 0; i<jsonArray.length(); i++){
                            JSONObject o = jsonArray.getJSONObject(i);
                            Model2 item = new Model2(
                                    o.getString("id"),
                                    o.getString("name"),
                                    o.getString("type"),
                                    o.getString("stype"),
                                    o.getString("price"),
                                    o.getString("status")
                            );
                            listItems2.add(item);
                            adapter2 = new MyAdapter2(listItems2,getApplicationContext());
                            recyclerView2.setAdapter(adapter2);
                        }
                    }else{
                        Toast.makeText(confirm_order.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(confirm_order.this, "Failed",Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String , String> getParams() throws AuthFailureError {
                Map<String , String> params = new HashMap<>();
                params.put("name", "kl");
                return params;
            }
        };
        RequestHandler.getInstance(confirm_order.this).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_questions);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint("Order id or User name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                search3(newText);
                return false;
            }
        });
        return true;
    }
}
