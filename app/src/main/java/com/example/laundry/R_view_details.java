package com.example.laundry;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class R_view_details extends AppCompatActivity {
    private TextView t_id,t_name,t_contact,t_email,t_address,t_date,t_type,t_stype,t_noof,t_prunit,t_amo,t_rdate;
    private TextView co,rd;
    private ProgressDialog progressDialog;
    private String str_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_view_details);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, User_Login.class));
        }

        ActionBar ab=getSupportActionBar();
        ab.setTitle("Recent Order Details");

        t_id=findViewById(R.id.tvid);
        t_name=findViewById(R.id.tvname);
        t_contact=findViewById(R.id.tvcontact);
        t_email=findViewById(R.id.tvemail);
        t_address=findViewById(R.id.tvaddress);
        t_date=findViewById(R.id.tvdate);
        t_type=findViewById(R.id.tvtype);
        t_stype=findViewById(R.id.tvstype);
        t_noof=findViewById(R.id.noc);
        t_amo=findViewById(R.id.tvamount);
        t_rdate=findViewById(R.id.tvrdate);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Data...");

        str_id = getIntent().getStringExtra("id");

        progressDialog.show();
        StringRequest stringRequest1 = new StringRequest(
                Request.Method.POST,
                Constants.URL_R_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj1 = new JSONObject(response);
                            if (!obj1.getBoolean("error")) {
                                final String st_id = obj1.getString("order_id");
                                final String st_name = obj1.getString("customer_name");
                                final String st_contact = obj1.getString("contactno");
                                final String st_email = obj1.getString("email");
                                final String st_address = obj1.getString("address");
                                final String st_date = obj1.getString("laun_date_received");
                                final String st_type = obj1.getString("laun_type_id");
                                final String st_stype = obj1.getString("laun_s_type_id");
                                final String st_weight = obj1.getString("laun_weight");
                                final String st_am = obj1.getString("amount");
                                final String st_rdate = obj1.getString("rdate");

                                t_id.setText(st_id);
                                t_name.setText(st_name);
                                t_contact.setText(st_contact);
                                t_email.setText(st_email);
                                t_address.setText(st_address);
                                t_date.setText(st_date);
                                t_type.setText(st_type);
                                t_stype.setText(st_stype);
                                t_noof.setText(st_weight);
                                t_amo.setText(st_am);
                                t_rdate.setText(st_rdate);
                            } else {
                                Toast.makeText(getApplicationContext(), obj1.getString("message"), Toast.LENGTH_SHORT).show();
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
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest1);
    }
}