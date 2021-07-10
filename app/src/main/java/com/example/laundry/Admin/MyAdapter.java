package com.example.laundry.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.laundry.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private List<Model> listItems;
    private Context context;
    private ProgressDialog dialog;


    public MyAdapter(List<Model> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView id;
        public TextView type;
        public TextView stype;
        public TextView price;
        public CardView card_view;
        public ViewHolder(View itemView ) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.id);
            type = (TextView) itemView.findViewById(R.id.type);
            stype = (TextView) itemView.findViewById(R.id.stype);
            price = (TextView) itemView.findViewById(R.id.price);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Model listItem = listItems.get(position);
        holder.id.setText(listItem.getId());
        holder.type.setText(listItem.getStype());
        holder.stype.setText(listItem.getPrice());
        holder.price.setText(listItem.getType());

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                final ProgressDialog dialog = new ProgressDialog(view.getContext());
                dialog.setMessage("Loading Delete Data");
                final CharSequence[] dialogitem = {"View Data","Edit Data","Delete Data"};
                builder.setTitle(listItem.getType());
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0 :
                                Intent intent = new Intent(view.getContext(), view_details.class);
                                intent.putExtra("id", listItem.getId());
                                intent.putExtra("type",listItem.getType());
                                intent.putExtra("stype",listItem.getStype());
                                intent.putExtra("price", listItem.getPrice());
                                view.getContext().startActivity(intent);
                                break;
                            case 1 :

                                Intent intent2 = new Intent(view.getContext(), edit_details.class);
                                intent2.putExtra("id", listItem.getId());
                                intent2.putExtra("type",listItem.getType());
                                intent2.putExtra("stype",listItem.getStype());
                                intent2.putExtra("price", listItem.getPrice());
                                view.getContext().startActivity(intent2);
                                break;
                            case 2 :
                                AlertDialog.Builder builderDel = new AlertDialog.Builder(view.getContext());
                                builderDel.setMessage("Are You Sure, You Want to Delete Data?");
                                builderDel.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialog.show();

                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_DELETE_LAUNDRY, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                dialog.hide();
                                                dialog.dismiss();
                                                try{
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    if (!jsonObject.getBoolean("error")) {
                                                        Toast.makeText(view.getContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                        manage_laundry.ma.refresh_list();
                                                    } else {
                                                        Toast.makeText(view.getContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                                    }
                                                }catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                dialog.hide();
                                                dialog.dismiss();
                                            }
                                        }){
                                            protected HashMap<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params= new HashMap<>();
                                                params.put("id",listItem.getId());
                                                return (HashMap<String, String>) params;

                                            }
                                        };
                                        RequestHandler.getInstance(view.getContext()).addToRequestQueue(stringRequest);
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

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}