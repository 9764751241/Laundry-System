package com.example.laundry;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class User_Register extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{
    private EditText ed_name,ed_mobno,ed_address,ed_email,ed_password,ed_conpassword;
    private ProgressDialog progressDialog;
    private Button buttonRegister;
    private TextView singin;
    private RadioGroup radioSexGroup;
    private String str_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__register);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        ActionBar ab=getSupportActionBar();
        ab.setTitle("User Registration");
        ed_name=findViewById(R.id.txtname);
        ed_mobno=findViewById(R.id.txtmobno);
        ed_address=findViewById(R.id.txtaddress);
        ed_email=findViewById(R.id.txtemail);
        ed_password=findViewById(R.id.txtpass);
        ed_conpassword=findViewById(R.id.txtconfpass);
        singin=findViewById(R.id.tv_sign_in);

        radioSexGroup=findViewById(R.id.gender_group);
        radioSexGroup.setOnCheckedChangeListener(this);



        buttonRegister = findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(this);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str_name = ed_name.getText().toString().trim();
                final String str_mobno = ed_mobno.getText().toString().trim();
                final String str_address = ed_address.getText().toString().trim();
                final String str_email = ed_email.getText().toString().trim();
                final String str_pass = ed_password.getText().toString().trim();
                final String str_confpass = ed_conpassword.getText().toString().trim();

                if(str_name.isEmpty() || str_mobno.isEmpty() || str_address.isEmpty() || str_email.isEmpty() || str_pass.isEmpty() || str_confpass.isEmpty()){
                    Toast.makeText(User_Register.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }

                else if(str_pass.equals(str_confpass)){

                    progressDialog.setMessage("Registering user...");
                    progressDialog.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            Constants.URL_REGISTER,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    ed_name.setText("");
                                    ed_mobno.setText("");
                                    ed_address.setText("");
                                    ed_email.setText("");
                                    ed_password.setText("");
                                    ed_conpassword.setText("");
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);

                                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                            params.put("name", str_name);
                            params.put("gender", str_gender);
                            params.put("mobno", str_mobno);
                            params.put("address", str_address);
                            params.put("email", str_email);
                            params.put("password", str_pass);
                            return params;
                        }
                    };
                    RequestHandler.getInstance(User_Register.this).addToRequestQueue(stringRequest);
                }
                else {
                    Toast.makeText(User_Register.this, "Password didn't match", Toast.LENGTH_SHORT).show();
                }

            }
        });
        singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(User_Register.this,User_Login.class));
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.radioMale:
                str_gender="Male";
                break;
            case R.id.radioFemale:
                str_gender="Female";
                break;
        }
    }
}

