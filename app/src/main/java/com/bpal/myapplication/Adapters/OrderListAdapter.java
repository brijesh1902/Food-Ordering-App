package com.bpal.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bpal.myapplication.DBModel.Order;
import com.bpal.myapplication.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private Context mcontext;
    private ArrayList<Order> mdata;

    public OrderListAdapter(Context context, ArrayList<Order> data) {
        mcontext = context;
        mdata =  data;
    }

    @NonNull
    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        View view = layoutInflater.inflate(R.layout.ordersfulllist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderListAdapter.ViewHolder holder, int position) {
        final Order options = mdata.get(position);

        holder.name.setText(options.getPname());
        Glide.with(mcontext).load(options.getPimage()).into(holder.image);
        holder.qty.setText("Quantity: "+options.getPquantity());
        int sp = (Integer.parseInt(options.getPprice()))*(Integer.parseInt(options.getPquantity()));
        holder.price.setText("₹ "+sp );

    }


    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, price, qty;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            qty = itemView.findViewById(R.id.pdqty);
            name = itemView.findViewById(R.id.pdname);
            price = itemView.findViewById(R.id.pdprice);
            image = itemView.findViewById(R.id.pdi);

        }

    }
}