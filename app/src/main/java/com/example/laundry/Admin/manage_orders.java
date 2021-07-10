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

public class manage_orders extends AppCompatActivity {
    public static manage_orders ma1;
    protected Cursor cursor;
    ArrayList<Model1> thelist;
    ListView listview;
    List<Model1> listItems1;
    private RecyclerView recyclerView1;
    private RecyclerView.Adapter adapter1;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, Admin_Login.class));
        }

        getSupportActionBar().setTitle("Manage Orders");
        recyclerView1 = (RecyclerView) findViewById(R.id.list1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(manage_orders.this));
        progressDialog = new ProgressDialog(this);
        listItems1 = new ArrayList<>();
        ma1 = this;
        refresh_list1();
    }

            public void search2(String text1){
                    listItems1.clear();
                    adapter1 = new MyAdapter1(listItems1,getApplicationContext());
                    recyclerView1.setAdapter(adapter1);

                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                    progressDialog.setMessage("Fetching Data...");
                    progressDialog.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.URL_SEARCH_ORDER, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                if(!jsonObject.getBoolean("error")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    Toast.makeText(manage_orders.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                for (int i = 0; i<jsonArray.length(); i++){
                                    JSONObject o = jsonArray.getJSONObject(i);
                                    Model1 item = new Model1(
                                            o.getString("id1"),
                                            o.getString("name1"),
                                            o.getString("type1"),
                                            o.getString("stype1"),
                                            o.getString("price1"),
                                            o.getString("status1")
                                    );
                                    listItems1.add(item);
                                    adapter1 = new MyAdapter1(listItems1,getApplicationContext());
                                    recyclerView1.setAdapter(adapter1);
                                }
                                }else{
                                    Toast.makeText(manage_orders.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.hide();
                            Toast.makeText(manage_orders.this, "Failed",Toast.LENGTH_SHORT).show();
                        }
                    }){
                        protected Map<String , String> getParams() throws AuthFailureError {
                            Map<String , String> params = new HashMap<>();
                            params.put("search1", text1);
                            return params;
                        }
                    };
                    RequestHandler.getInstance(manage_orders.this).addToRequestQueue(stringRequest);

            }

    public void refresh_list1(){
        listItems1.clear();
        adapter1 = new MyAdapter1(listItems1,getApplicationContext());
        recyclerView1.setAdapter(adapter1);

        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.URL_ORDERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(!jsonObject.getBoolean("error")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        Toast.makeText(manage_orders.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        for (int i = 0; i<jsonArray.length(); i++){
                            JSONObject o = jsonArray.getJSONObject(i);
                            Model1 item = new Model1(
                                    o.getString("id1"),
                                    o.getString("name1"),
                                    o.getString("type1"),
                                    o.getString("stype1"),
                                    o.getString("price1"),
                                    o.getString("status1")
                            );
                            listItems1.add(item);
                            adapter1 = new MyAdapter1(listItems1,getApplicationContext());
                            recyclerView1.setAdapter(adapter1);
                        }
                    }else{
                        Toast.makeText(manage_orders.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(manage_orders.this, "Failed",Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String , String> getParams() throws AuthFailureError {
                Map<String , String> params = new HashMap<>();
                params.put("name", "kl");
                return params;
            }
        };
        RequestHandler.getInstance(manage_orders.this).addToRequestQueue(stringRequest);
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
                search2(newText);
                return false;
            }
        });
        return true;
    }
}
