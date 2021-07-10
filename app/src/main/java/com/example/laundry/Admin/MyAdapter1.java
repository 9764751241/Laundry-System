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
import com.example.laundry.edit_order;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapter1 extends RecyclerView.Adapter<MyAdapter1.ViewHolder>{

    private List<Model1> listItems1;
    private Context context;
    private ProgressDialog dialog;
    private String re;


    public MyAdapter1(List<Model1> listItems1, Context context) {
        this.listItems1 = listItems1;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView id1;
        public TextView name1;
        public TextView type1;
        public TextView stype1;
        public TextView price1;
        public TextView status1;
        public CardView card_view1;
        public ViewHolder(View itemView ) {
            super(itemView);
            id1 = (TextView) itemView.findViewById(R.id.id1);
            name1 = (TextView) itemView.findViewById(R.id.name1);
            type1 = (TextView) itemView.findViewById(R.id.type1);
            stype1 = (TextView) itemView.findViewById(R.id.stype1);
            price1 = (TextView) itemView.findViewById(R.id.price1);
            status1 = (TextView) itemView.findViewById(R.id.a_status1);
            card_view1 = (CardView) itemView.findViewById(R.id.card_view1);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item1, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Model1 listItem1 = listItems1.get(position);
        holder.id1.setText(listItem1.getId1());
        if(listItem1.getStatus1().equals("In")){
            re="Pending Confirm";
        }else if (listItem1.getStatus1().equals("Out")){
            re="Processing";
        } else {
            re="Ready";
        }
        holder.name1.setText(Html.fromHtml("<b>Order ID :&nbsp;"+listItem1.getId1()+"</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><font size=12px>"+re+"</b>"));
        holder.type1.setText(listItem1.getType1());
        holder.stype1.setText(listItem1.getStype1());
        holder.price1.setText(Html.fromHtml("Total Amount :&nbsp;"+listItem1.getPrice1()));
        holder.status1.setText(listItem1.getStatus1());

        holder.card_view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                final ProgressDialog dialog = new ProgressDialog(view.getContext());
                dialog.setMessage("Loading Delete Data");
                final CharSequence[] dialogitem = {"View Details","Confirm Order","Edit Order","Delete Order"};
                builder.setTitle(Html.fromHtml("<b>Order ID :"+"&nbsp;"+listItem1.getId1()));
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0 :
                                Intent intent = new Intent(view.getContext(), order_details.class);
                                intent.putExtra("id", listItem1.getId1());
                                intent.putExtra("name",listItem1.getName1());
                                intent.putExtra("type",listItem1.getType1());
                                intent.putExtra("stype",listItem1.getStype1());
                                intent.putExtra("price", listItem1.getPrice1());
                                view.getContext().startActivity(intent);
                                break;
                            case 1 :
                                    AlertDialog.Builder builderDel2 = new AlertDialog.Builder(view.getContext());
                                    builderDel2.setTitle(listItem1.getName1());
                                    builderDel2.setMessage("Are You Sure,You Want to Confirm Order?");
                                    builderDel2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialog.show();
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_ORDER_STATUS_OUT, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    dialog.hide();
                                                    dialog.dismiss();
                                                    try{
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        if (!jsonObject.getBoolean("error")) {
                                                            Toast.makeText(view.getContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                            manage_orders.ma1.refresh_list1();
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
                                            }) {
                                                protected HashMap<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("status", listItem1.getId1());
                                                    return (HashMap<String, String>) params;
                                                }
                                            };
                                            RequestHandler.getInstance(view.getContext()).addToRequestQueue(stringRequest);
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    builderDel2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    builderDel2.create().show();
                                break;
                            case 2 :
                                    Intent intent2 = new Intent(view.getContext(), a_edit_order.class);
                                intent2.putExtra("id", listItem1.getId1());
                                intent2.putExtra("name",listItem1.getName1());
                                intent2.putExtra("type",listItem1.getType1());
                                intent2.putExtra("stype",listItem1.getStype1());
                                intent2.putExtra("price", listItem1.getPrice1());
                                    view.getContext().startActivity(intent2);
                                break;
                            case  3:
                                if(listItem1.getStatus1().equals("In")){
                                AlertDialog.Builder builderDel1 = new AlertDialog.Builder(view.getContext());
                                builderDel1.setTitle(listItem1.getName1());
                                builderDel1.setMessage("Are You Sure, You Want to Delete Data?");
                                builderDel1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialog.show();
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_DELETE_ORDER, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                dialog.hide();
                                                dialog.dismiss();
                                                try{
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    if (!jsonObject.getBoolean("error")) {
                                                        Toast.makeText(view.getContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                        manage_orders.ma1.refresh_list1();
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
                                                params.put("id",listItem1.getId1());
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
                                }else{
                                    Toast.makeText(view.getContext(),"Under Process, Not Possible",Toast.LENGTH_SHORT).show();
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
        return listItems1.size();
    }
}