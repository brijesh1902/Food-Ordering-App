package com.bpal.myapplication.Adapters;

import android.content.Context;
import android.view.ContextMenu;
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
import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context mcontext;
    private ArrayList<Order> mdata;

    public CartAdapter(Context context, List<Order> data) {
        mcontext = context;
        mdata = (ArrayList<Order>) data;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        View view = layoutInflater.inflate(R.layout.orderlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartAdapter.ViewHolder holder, int position) {
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView name, price, qty;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            qty = itemView.findViewById(R.id.pdtqty);
            name = itemView.findViewById(R.id.pdtname);
            price = itemView.findViewById(R.id.pdtprice);
            image = itemView.findViewById(R.id.pdti);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select action");
            menu.add(0,0, getAdapterPosition(), "Delete");
        }


    }
}
