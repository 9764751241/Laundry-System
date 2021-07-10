package com.example.laundry.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
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

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder>{

    private List<Model2> listItems2;
    private Context context;
    private ProgressDialog dialog;
    private String re2;

    public MyAdapter2(List<Model2> listItems2, Context context) {
        this.listItems2 = listItems2;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView id2;
        public TextView name2;
        public TextView type2;
        public TextView stype2;
        public TextView price2;
        public TextView status2;
        public CardView card_view2;
        public ViewHolder(View itemView ) {
            super(itemView);
            id2 = (TextView) itemView.findViewById(R.id.id2);
            name2 = (TextView) itemView.findViewById(R.id.name2);
            type2 = (TextView) itemView.findViewById(R.id.type2);
            stype2 = (TextView) itemView.findViewById(R.id.stype2);
            price2 = (TextView) itemView.findViewById(R.id.price2);
            status2 = (TextView) itemView.findViewById(R.id.a_status2);
            card_view2 = (CardView) itemView.findViewById(R.id.card_view2);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item2, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Model2 listItem2 = listItems2.get(position);
        holder.id2.setText(listItem2.getId2());
        if(listItem2.getStatus2().equals("In")){
            re2="Pending Confirm";
        }else if (listItem2.getStatus2().equals("Out")){
            re2="Processing";
        } else {
            re2="Ready";
        }
        holder.name2.setText(Html.fromHtml("<b>Order ID :&nbsp;"+listItem2.getId2()+"</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><font size=12px>"+re2+"</b>"));
        holder.type2.setText(listItem2.getType2());
        holder.stype2.setText(listItem2.getStype2());
        holder.price2.setText(Html.fromHtml("Total Amount :&nbsp;"+listItem2.getPrice2()));
        holder.status2.setText(listItem2.getStatus2());

        holder.card_view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                final ProgressDialog dialog = new ProgressDialog(view.getContext());
                dialog.setMessage("Loading Delete Data");
                final CharSequence[] dialogitem = {"View Details","Status to Ready","Deliver to Customer"};
                builder.setTitle(Html.fromHtml("<b>Order ID :"+"&nbsp;"+listItem2.getId2()));
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0 :
                                Intent intent = new Intent(view.getContext(), order_details.class);
                                intent.putExtra("id", listItem2.getId2());
                                intent.putExtra("name",listItem2.getName2());
                                intent.putExtra("type",listItem2.getType2());
                                intent.putExtra("stype",listItem2.getStype2());
                                intent.putExtra("price", listItem2.getPrice2());
                                view.getContext().startActivity(intent);
                                break;
                            case 1 :
                                if(listItem2.getStatus2().equals("Out")) {
                                    AlertDialog.Builder builderDel = new AlertDialog.Builder(view.getContext());
                                    builderDel.setTitle(listItem2.getName2());
                                    builderDel.setMessage("Are You Sure, Cloths are Ready?");
                                    builderDel.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialog.show();
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.URL_ORDER_STATUS_READY, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    dialog.hide();
                                                    dialog.dismiss();
                                                    try{
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        if (!jsonObject.getBoolean("error")) {
                                                            Toast.makeText(view.getContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                            confirm_order.co1.refresh_list2();
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
                                                    params.put("status",listItem2.getId2());
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
                                }else {
                                    Toast.makeText(view.getContext(), "Already Processed", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case  2:
                                if(listItem2.getStatus2().equals("Ready")){
                                AlertDialog.Builder builderDel1 = new AlertDialog.Builder(view.getContext());
                                builderDel1.setTitle(listItem2.getName2());
                                builderDel1.setMessage("Are You Sure, to Deliver Customer?");
                                builderDel1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialog.show();
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CLAIM, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                dialog.hide();
                                                dialog.dismiss();
                                                try{
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    if (!jsonObject.getBoolean("error")) {
                                                        Toast.makeText(view.getContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                        confirm_order.co1.refresh_list2();
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
                                                params.put("id",listItem2.getId2());
                                                return (HashMap<String, String>) params;
                                            }
                                        };
                                        RequestHandler.getInstance(view.getContext()).addToRequestQueue(stringRequest);
                                        dialogInterface.dismiss();
                                    }
                                });
                                builderDel1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                builderDel1.create().show();
                                }else {
                                    Toast.makeText(view.getContext(), "Order Not Ready", Toast.LENGTH_SHORT).show();
                                }
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
        return listItems2.size();
    }
}