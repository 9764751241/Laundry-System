package com.example.laundry;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class view_details extends AppCompatActivity {
    private TextView t_id,t_name,t_contact,t_email,t_address,t_date,t_type,t_stype,t_noof,t_prunit,t_amo,t_rdate;
    private TextView co,rd;
    private ProgressDialog progressDialog;
    private String str_id,str_id1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, User_Login.class));
        }

        ActionBar ab=getSupportActionBar();
        ab.setTitle("Order Details");

        t_id=findViewById(R.id.tvid);
        t_name=findViewById(R.id.tvname);
        t_contact=findViewById(R.id.tvcontact);
        t_email=findViewById(R.id.tvemail);
        t_address=findViewById(R.id.tvaddress);
        t_date=findViewById(R.id.tvdate);
        t_type=findViewById(R.id.tvtype);
        t_stype=findViewById(R.id.tvstype);
        t_noof=findViewById(R.id.noc);
        t_prunit=findViewById(R.id.tvcost);
        t_amo=findViewById(R.id.tvamount);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Data...");

        str_id = getIntent().getStringExtra("id");

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                final String st_id=obj.getString("order_id");
                                final String st_name=obj.getString("customer_name");
                                final String st_contact=obj.getString("contactno");
                                final String st_email=obj.getString("email");
                                final String st_address=obj.getString("address");
                                final String st_date=obj.getString("laun_date_received");
                                final String st_type=obj.getString("laun_type_id");
                                final String st_stype=obj.getString("laun_s_type_id");
                                final int st_weight=Integer.parseInt(obj.getString("laun_weight"));
                                final int st_am=Integer.parseInt(obj.getString("amount"));
                                int amount=st_weight*st_am;

                                t_id.setText(st_id);
                                t_name.setText(st_name);
                                t_contact.setText(st_contact);
                                t_email.setText(st_email);
                                t_address.setText(st_address);
                                t_date.setText(st_date);
                                t_type.setText(st_type);
                                t_stype.setText(st_stype);
                                t_noof.setText(String.valueOf(st_weight));
                                t_prunit.setText(String.valueOf(st_am));
                                t_amo.setText(String.valueOf(amount));
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("id",str_id);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}