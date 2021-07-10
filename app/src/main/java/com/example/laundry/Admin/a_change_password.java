package com.example.laundry.Admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.laundry.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class a_change_password extends AppCompatActivity {
    private EditText ed_new,ed_conf,ed_old;
    private Button bt_cp;
    private ProgressDialog progressDialog;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_change_password);
        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, Admin_Login.class));
        }
        ActionBar ab=getSupportActionBar();
        ab.setTitle("Change Password");
        ed_old=findViewById(R.id.txtolspass);
        ed_new=findViewById(R.id.txtnewpass);
        ed_conf=findViewById(R.id.txtconfpass);
        bt_cp=findViewById(R.id.btnchange);
        id= SharedPrefManager.getInstance(this).getUserId();

        progressDialog = new ProgressDialog(this);
        bt_cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str_oldp = ed_old.getText().toString().trim();
                final String str_newp = ed_new.getText().toString().trim();
                final String str_confp = ed_conf.getText().toString().trim();
                final String str_id = String.valueOf(id).trim();

                if(str_newp.isEmpty() || str_confp.isEmpty() || str_oldp.isEmpty()){
                    Toast.makeText(a_change_password.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }

                else if(str_newp.equals(str_confp)){

                    progressDialog.setMessage("Updating Password...");
                    progressDialog.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            Constants.URL_ACHANGEPASS,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    ed_new.setText("");
                                    ed_old.setText("");
                                    ed_conf.setText("");
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        if(!obj.getBoolean("error")) {
                                            Toast.makeText(a_change_password.this,obj.getString("message"), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(a_change_password.this,obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                            params.put("oldpass", str_oldp);
                            params.put("newpass", str_newp);
                            params.put("id", str_id);
                            return params;
                        }
                    };
                    RequestHandler.getInstance(a_change_password.this).addToRequestQueue(stringRequest);
                }
                else {
                    Toast.makeText(a_change_password.this, "Password didn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

