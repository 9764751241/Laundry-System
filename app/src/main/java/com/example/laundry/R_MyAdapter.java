package com.example.laundry;


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
import com.example.laundry.Admin.Model1;
import com.example.laundry.Admin.edit_details;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class R_MyAdapter extends RecyclerView.Adapter<R_MyAdapter.ViewHolder>{

    private List<R_Model> listItems1;
    private Context context;
    private ProgressDialog dialog;

    public R_MyAdapter(List<R_Model> listItems, Context context) {
        this.listItems1 = listItems;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView r_id;
        public TextView r_o_id;
        public TextView r_type;
        public TextView r_stype;
        public TextView r_amount;
        public CardView card_view;
        public ViewHolder(View itemView ) {
            super(itemView);
            r_id = (TextView) itemView.findViewById(R.id.r_id);
            r_o_id = (TextView) itemView.findViewById(R.id.r_o_id);
            r_type = (TextView) itemView.findViewById(R.id.r_type);
            r_stype = (TextView) itemView.findViewById(R.id.r_stype);
            r_amount = (TextView) itemView.findViewById(R.id.r_amount);
            card_view = (CardView) itemView.findViewById(R.id.card_v);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.r_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final R_Model listItem1 = listItems1.get(position);
        holder.r_id.setText(listItem1.getId());
        holder.r_o_id.setText(Html.fromHtml("<b>Order ID :&nbsp;"+listItem1.getId()+"</b>"));
        holder.r_type.setText(listItem1.getType());
        holder.r_stype.setText(listItem1.getStype());
        holder.r_amount.setText(Html.fromHtml("Total Amount :&nbsp;"+listItem1.getAmount()));

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                final ProgressDialog dialog = new ProgressDialog(view.getContext());
                dialog.setMessage("Loading Delete Data");
                final CharSequence[] dialogitem = {"View Details"};
                builder.setTitle(listItem1.getType());
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0 :
                                Intent intent = new Intent(view.getContext(),R_view_details.class);
                                intent.putExtra("id", listItem1.getId());
                                intent.putExtra("type",listItem1.getType());
                                intent.putExtra("stype",listItem1.getStype());
                                intent.putExtra("price", listItem1.getAmount());
                                view.getContext().startActivity(intent);
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