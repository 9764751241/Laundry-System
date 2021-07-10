package com.example.laundry.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.laundry.MainActivity;
import com.example.laundry.R;
import com.example.laundry.my_orders;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class admin_dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private int id;
    private String str_id;
    private TextView status,status1,status2;
    private CardView cv1,cv2,cv3;
    private Handler mHandler;
    private CircleImageView a_ProfileImage;
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    String img;
    String encodedImage1;
    View headerView;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, Admin_Login.class));
        }

        this.mHandler = new Handler();
        m_Runnable.run();

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");
        drawer = findViewById(R.id.drawer_layout1);

        status=findViewById(R.id.a_d_status);
        status1=findViewById(R.id.a_d_status1);
        status2=findViewById(R.id.a_d_status2);

        cv1=findViewById(R.id.d_dcv1);
        cv2=findViewById(R.id.d_dcv2);
        cv3=findViewById(R.id.d_dcv3);

        navigationView = findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        id = SharedPrefManager.getInstance(this).getUserId();
        str_id = String.valueOf(id).trim();
        updateNavHeader1();
        a_dashboard();
        a_setProfile(str_id);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(admin_dashboard.this, manage_orders.class));
            }
        });
        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(admin_dashboard.this,confirm_order.class));
            }
        });
        cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(admin_dashboard.this,confirm_order.class));
            }
        });
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.admin_dashboard:
                break;
            case R.id.admin_changepass:
                startActivity(new Intent(this, a_change_password.class));
                break;
            case R.id.admin_manage_type:
                startActivity(new Intent(this, manage_laundry.class));
                break;
            case R.id.admin_manage_orders:
                startActivity(new Intent(this, manage_orders.class));
                break;
            case R.id.admin_confirm_orders:
                startActivity(new Intent(this, confirm_order.class));
                break;
            case R.id.admin_logout:
                com.example.laundry.Admin.SharedPrefManager.getInstance(this).logout();
                finish();
                startActivity(new Intent(this, Admin_Login.class));
                break;
            case R.id.nav_share1:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuLogout1:
                SharedPrefManager.getInstance(this).logout();
                finish();
                startActivity(new Intent(this, Admin_Login.class));
                break;
            case R.id.menuSettings1:
                startActivity(new Intent(this, a_change_password.class));
                break;
        }
        return true;
    }

    public void a_dashboard() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_DASH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                final String st_c=obj.getString("conf");
                                final String st_p=obj.getString("pro");
                                final String st_r=obj.getString("ready");

                                status.setText(st_c);
                                status1.setText(st_p);
                                status2.setText(st_r);
                            }else {
                                final String st_c=obj.getString("conf");
                                final String st_p=obj.getString("pro");
                                final String st_r=obj.getString("ready");

                                status.setText(st_c);
                                status1.setText(st_p);
                                status2.setText(st_r);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Network error, please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", "kl");
                return params;
            }
        };
       RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void updateNavHeader1() {
        TextView navUsername = headerView.findViewById(R.id.txtuser1);
        navUsername.setText(SharedPrefManager.getInstance(this).getUsername());
        a_ProfileImage = headerView.findViewById(R.id.a_profile_image);
        a_ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent;
                AlertDialog.Builder builder = new AlertDialog.Builder(admin_dashboard.this);
                final ProgressDialog dialog = new ProgressDialog(admin_dashboard.this);
                dialog.setMessage("Loading Delete Data");
                final CharSequence[] dialogitem = {"Upload Photo","Remove Photo"};
                builder.setTitle(SharedPrefManager.getInstance(admin_dashboard.this).getUsername());
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0 :
                                Intent gallery = new Intent();
                                gallery.setType("image/*");
                                gallery.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
                                break;
                            case 1 :
                                AlertDialog.Builder builderDel = new AlertDialog.Builder(admin_dashboard.this);
                                builderDel.setMessage("Are You Sure, Remove Profile Photo?");
                                builderDel.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialog.show();
                                        StringRequest stringRequest = new StringRequest(
                                                Request.Method.POST,
                                                Constants.URL_DELETE_PROFILE,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        dialog.hide();
                                                        dialog.dismiss();
                                                        try {
                                                            JSONObject obj = new JSONObject(response);
                                                            if (!obj.getBoolean("error")) {
                                                                Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                                                                a_setProfile(str_id);
                                                            } else {
                                                                Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(getApplicationContext(), "Network error, please try again!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                        ) {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("id", str_id);
                                                return params;
                                            }
                                        };
                                        RequestHandler.getInstance(admin_dashboard.this).addToRequestQueue(stringRequest);
                                        dialogInterface.dismiss();
                                    }
                                });
                                builderDel.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                builderDel.create().show();
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
    }

    private final Runnable m_Runnable = new Runnable() {
        public void run() {
            a_dashboard();
            admin_dashboard.this.mHandler.postDelayed(m_Runnable, 15000);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                a_ProfileImage.setImageBitmap(bitmap);
                imageStore(bitmap);
                a_saveProfile((imageStore(bitmap)),str_id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] imageBytes = stream.toByteArray();
        encodedImage1 = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage1;
    }
    public void a_saveProfile(String p,String id){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_UPDATE_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                                a_setProfile(str_id);
                            } else {
                                Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Network error, please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("img", p);
                params.put("id", id);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void a_setProfile(String id){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                final String str_image=obj.getString("image");
                                if (str_image.equals("NULL")) {
                                    a_ProfileImage.setImageResource(R.drawable.ic_profile);
                                }else{
                                    GetXMLTask task = new GetXMLTask();
                                    task.execute(new String[] { str_image });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Network error, please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            a_ProfileImage.setImageBitmap(result);
        }

        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;
            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();
                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }
    }
}